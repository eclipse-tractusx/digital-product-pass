/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
 *
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the
 * License for the specific language govern in permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.digitalproductpass.http.controllers.api;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.eclipse.tractusx.digitalproductpass.config.DiscoveryConfig;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ControllerException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.digitalproductpass.models.edc.AssetSearch;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.DiscoverySearch;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Set;
import org.eclipse.tractusx.digitalproductpass.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * This class consists exclusively to define the HTTP methods needed for the Contract negotiation.
 **/
@RestController
@RequestMapping("/api/contract")
@Tag(name = "Contract Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class ContractController {

    /** ATTRIBUTES **/
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;
    private @Autowired AuthenticationService authService;
    private @Autowired PassportConfig passportConfig;
    private @Autowired DiscoveryConfig discoveryConfig;
    private @Autowired DtrConfig dtrConfig;

    private @Autowired EdcUtil edcUtil;
    private @Autowired Environment env;
    @Autowired
    ProcessManager processManager;
    @Autowired
    DtrSearchManager dtrSearchManager;
    private @Autowired ProcessConfig processConfig;
    @Autowired
    CatenaXService catenaXService;
    @Autowired
    HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;

    /** METHODS **/

    /**
     * HTTP POST method to create a new Process.
     * <p>
     * @param   searchBody
     *          the {@code DiscoverySearch} body from the HTTP request with the partInstanceId.
     *
     * @return this {@code Response} HTTP response with the processId of the new Process created.
     *
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Operation(summary = "Creates a process and checks for the viability of the data retrieval")
    public Response create(@Valid @RequestBody DiscoverySearch searchBody) {
        Response response = httpUtil.getInternalError();
        try {
            if (!authService.isAuthenticated(httpRequest)) {
                response = httpUtil.getNotAuthorizedResponse();
                return httpUtil.buildResponse(response, httpResponse);
            }


            searchBody.setType(this.discoveryConfig.getBpn().getKey()); // Set default configuration key as default
            List<String> mandatoryParams = List.of("id");
            if (!jsonUtil.checkJsonKeys(searchBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            List<BpnDiscovery> bpnDiscoveries = null;
            try {
                bpnDiscoveries = catenaXService.getBpnDiscovery(searchBody.getId(), searchBody.getType());
            } catch (Exception e) {
                response.message = "Failed to get the bpn number from the discovery service";
                response.status = 404;
                response.statusText = "Not Found";
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (bpnDiscoveries == null || bpnDiscoveries.size() == 0) {
                response.message = "Failed to get the bpn number from the discovery service, discovery response is null";
                response.status = 404;
                response.statusText = "Not Found";
                return httpUtil.buildResponse(response, httpResponse);
            }
            List<String> bpnList = new ArrayList<>();
            for(BpnDiscovery bpnDiscovery : bpnDiscoveries){
                bpnList.addAll(bpnDiscovery.mapBpnNumbers());
            }
            if(bpnList.size() == 0){
                response.message = "The asset was not found in the BPN Discovery!";
                response.status = 404;
                response.statusText = "Not Found";
                return httpUtil.buildResponse(response, httpResponse);
            }
            String processId = processManager.initProcess();
            LogUtil.printMessage("Creating process [" + processId + "] for "+searchBody.getType() + ": "+ searchBody.getId());
            ConcurrentHashMap<String, List<Dtr>> dataModel = null;
            if(dtrConfig.getTemporaryStorage().getEnabled()) {
                try {
                    dataModel = this.dtrSearchManager.loadDataModel();
                } catch (Exception e) {
                    LogUtil.printWarning("Failed to load data model from disk!");
                }
            }
            // This checks if the cache is deactivated or if the bns are not in the dataModel,  if one of them is not in the data model then we need to check for them
            if(!dtrConfig.getTemporaryStorage().getEnabled() || ((dataModel==null) || !jsonUtil.checkJsonKeys(dataModel, bpnList, ".", false))){
                catenaXService.searchDTRs(bpnList, processId);
            }else{
                boolean requestDtrs = false;
                // Take the results from cache
                for(String bpn: bpnList){
                    List<Dtr> dtrs = null;
                    try {
                        dtrs = (List<Dtr>) jsonUtil.bindReferenceType(dataModel.get(bpn), new TypeReference<List<Dtr>>() {});
                    } catch (Exception e) {
                        throw new ControllerException(this.getClass().getName(), e, "Could not bind the reference type!");
                    }

                    if(dtrs == null || dtrs.size() == 0){
                        continue;
                    }
                    Long currentTimestamp = DateTimeUtil.getTimestamp();
                    // Iterate over every DTR and add it to the file
                    for(Dtr dtr: dtrs){

                        Long validUntil = dtr.getValidUntil();
                        //Check if invalid time has come
                        if(dtr.getInvalid() && validUntil > currentTimestamp){
                            continue;
                        }

                        if(dtr.getContractId() == null || dtr.getContractId().isEmpty() || validUntil == null || validUntil < currentTimestamp){
                            requestDtrs = true; // If the cache invalidation time has come request Dtrs
                            break;
                        }

                        processManager.addSearchStatusDtr(processId, dtr); //Add valid DTR to status
                    }

                    // If the refresh from the cache is necessary
                    if(requestDtrs){
                        dtrSearchManager.deleteBpns(dataModel, bpnList); // Delete BPN numbers
                        catenaXService.searchDTRs(bpnList, processId); // Start again the search
                        break;
                    }
                }
            }

            SearchStatus status = processManager.getSearchStatus(processId);
            if(status == null){
                return httpUtil.buildResponse(httpUtil.getNotFound("It was not possible to search for the decentral digital twin registries"), httpResponse);
            }
            if(status.getDtrs().isEmpty()){
                return httpUtil.buildResponse(httpUtil.getNotFound("No decentral digital twin registry was found"), httpResponse);
            }
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "processId", processId
            );
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            assert response != null;
            response.message = "It was not possible to create the process and search for the decentral digital twin registries";
            LogUtil.printException(e, response.message);
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP POST method to search the passport of an asset.
     * <p>
     * @param   searchBody
     *          the {@code DiscoverySearch} body from the HTTP request with the partInstanceId, passport version and the processId.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @Operation(summary = "Searches for a passport with the following id", responses = {
            @ApiResponse(description = "Default Response Structure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Content of Data Field in Response", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Dataset.class)))
    })
    public Response search(@Valid @RequestBody Search searchBody) {
        Response response = httpUtil.getInternalError();
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            List<String> mandatoryParams = List.of("id");
            if (!jsonUtil.checkJsonKeys(searchBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            Process process = null;
            AssetSearch assetSearch = null;

            // Check for processId
            if(searchBody.getProcessId() == null){
                response = httpUtil.getBadRequest("No processId was found on the request body!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            String processId = searchBody.getProcessId();
            if(processId.isEmpty()){
                response = httpUtil.getBadRequest("Process id is required for decentral digital twin registry searches!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            SearchStatus searchStatus = processManager.getSearchStatus(processId);
            if (searchStatus == null) {
                response = httpUtil.getBadRequest("The searchStatus id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            if(searchStatus.getDtrs().keySet().size() == 0){
                response = httpUtil.getBadRequest("No digital twins are available for this process!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Boolean childrenCondition = searchBody.getChildren();
            String logPrint = "[PROCESS " + processId + "] Creating search for "+searchBody.getIdType() + ": "+ searchBody.getId();
            if(childrenCondition != null){
                LogUtil.printMessage(logPrint + " with drilldown enabled");
                process = processManager.createProcess(processId, childrenCondition, httpRequest); // Store the children condition
            }else {
                LogUtil.printMessage(logPrint + " with drilldown disabled");
                process = processManager.createProcess(processId, httpRequest);
            }
            Status status = processManager.getStatus(processId);
            if (status == null) {
                response = httpUtil.getBadRequest("The status is not available!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            assetSearch = aasService.decentralDtrSearch(process.id, searchBody);


            if(assetSearch == null){
                LogUtil.printWarning("[PROCESS " + processId + "] The decentralized digital twin registry asset search return a null response!");
                status = processManager.getStatus(processId);
                // Here start the algorithm to refresh the dtrs in the cache if the transfer was incompleted
                List<Dtr> dtrList = new ArrayList<Dtr>();
                Map<String, Dtr> dtrs = searchStatus.getDtrs();
                List<String> bpnList = new ArrayList<String>();
                for(String dtrId: searchStatus.getDtrs().keySet()){
                    // Check if any dtr search was incomplete
                    if(!status.historyExists("dtr-"+dtrId+"-transfer-incomplete")) {
                        continue;
                    }
                    // Add the dtr bpn to the update cache list
                    Dtr dtr = dtrs.get(dtrId);
                    String bpn = dtr.getBpn();
                    if(!bpnList.contains(bpn)) {
                        bpnList.add(dtr.getBpn()); // Add bpn to delete in the cache
                    }
                    dtrList.add(dtr);
                }

                // If no bpn numbers need to be updated is because there is no digital twin found
                if(bpnList.size() == 0){
                    response = httpUtil.getBadRequest("No digital twin was found!");
                    return httpUtil.buildResponse(response, httpResponse);
                }

                LogUtil.printWarning("["+dtrList.size()+"] Digital Twin Registries Contracts are invalid and need to be refreshed! For the following BPN Number(s): "+ bpnList.toString());
                // Refresh cache or search id
                if(dtrConfig.getTemporaryStorage().getEnabled()) {
                    ConcurrentHashMap<String, List<Dtr>> dataModel = null;
                    try {
                        dataModel = this.dtrSearchManager.loadDataModel();
                    } catch (Exception e) {
                        LogUtil.printWarning("Failed to load data model from disk!");
                    }
                    dtrSearchManager.deleteBpns(dataModel, bpnList); // Delete BPN numbers
                }
                LogUtil.printMessage("Refreshing ["+bpnList.size()+"] BPN Number Endpoints...");
                catenaXService.searchDTRs(bpnList, processId); // Start again the search for refreshing the dtrs
                assetSearch = aasService.decentralDtrSearch(process.id, searchBody); // Start again the search
                if(assetSearch == null) { // If again was not found then we give an error
                    response = httpUtil.getBadRequest("No digital twin was found! Even after retrying the digital twin transfer!");
                    return httpUtil.buildResponse(response, httpResponse);
                }
            }
            // Assing the variables with the content
            String assetId = assetSearch.getAssetId();
            String connectorAddress = assetSearch.getConnectorAddress();

            /*[1]=========================================*/
            // Get catalog with all the contract offers
            if(connectorAddress == null){
                LogUtil.printError("The connector address is empty!");
            }
            if(assetId == null){
                LogUtil.printError("The assetId is empty!");
            }
            Map<String, Dataset> datasets = null;
            Long startedTime = DateTimeUtil.getTimestamp();
            try {
                datasets = dataService.getContractOffersByAssetId(assetId, connectorAddress);
            } catch (ServiceException e) {
                LogUtil.printError("The EDC is not reachable, it was not possible to retrieve catalog! Trying again...");
                datasets = dataService.getContractOffersByAssetId(assetId, connectorAddress);
                if (datasets == null) { // If the contract catalog is not reachable retry...
                    response.message = "The EDC is not reachable, it was not possible to retrieve catalog! Please try again!";
                    response.status = 502;
                    response.statusText = "Service Not Available";
                    return httpUtil.buildResponse(response, httpResponse);
                }
            }
            // Check if contract offer was not received
            if (datasets == null) {
                // Retry again...
                LogUtil.printWarning("[PROCESS " + process.id + "] No asset id found for the dataset contract offers in the catalog! Requesting catalog again...");
                datasets = dataService.getContractOffersByAssetId(assetId, connectorAddress);
                if (datasets == null) { // If the contract catalog is not reachable retry...
                    response.message = "Asset Id not found in any contract!";
                    response.status = 404;
                    response.statusText = "Not Found";
                    return httpUtil.buildResponse(response, httpResponse);
                }
            }
            String seedId = String.join("|", datasets.keySet());
            LogUtil.printDebug("[PROCESS " + process.id + "] ["+datasets.size()+"] Contracts found for asset [" + assetId + "] in EDC Endpoint [" + connectorAddress + "]");

            response = null;
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "id", process.id,
                    "contracts", datasets,
                    "token", processManager.generateToken(process, seedId)
            );

            processManager.saveDatasets(process.id, datasets, seedId, startedTime, false);
            // After the search is performed the search dir is deleted
            if(!processManager.deleteSearchDir(process.id)){
                LogUtil.printError("It was not possible to delete the search.json file for the process");
            }else{
                LogUtil.printDebug("[PROCESS " + process.id + "] The tmp search directory was deleted successfully!");
            }

            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            assert response != null;
            response.message = "It was not possible to search for the serialized id";
            LogUtil.printException(e, response.message);
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP GET method to get the Process status for the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/status/{processId}", method = RequestMethod.GET)
    @Operation(summary = "Get status from process")
    public Response status(@PathVariable String processId) {
        Response response = httpUtil.getInternalError();
        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            // Check for processId
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Get status
            response = httpUtil.getResponse();
            response.data = processManager.getStatus(processId);
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP POST method to cancel a Process.
     * <p>
     * @param   tokenRequestBody
     *          the {@code TokenRequest} object with the processId, contractId and the authentication token.
     *
     * @return this {@code Response} HTTP response with the status.
     *
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @Operation(summary = "Cancel the negotiation")
    public Response cancel(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Response response = httpUtil.getInternalError();

        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            // Check for the mandatory fields
            List<String> mandatoryParams = List.of("processId", "contractId", "token");
            if (!jsonUtil.checkJsonKeys(tokenRequestBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check for processId
            String processId = tokenRequestBody.getProcessId();
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            Process process = processManager.getProcess(httpRequest, processId);
            if (process == null) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Get status to check for contract id
            String contractId = tokenRequestBody.getContractId();
            Status status = processManager.getStatus(processId);

            // Check if was already declined
            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("This contract was declined! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has already been canceled! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if there is a contract available
            if (!status.historyExists("contract-dataset")) {
                response = httpUtil.getBadRequest("No contract is available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if the contract id is correct
            History history = status.getHistory("contract-dataset");
            if (!history.getId().contains(contractId)) {
                response = httpUtil.getBadRequest("This contract id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, history.getId());
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (status.getStatus().equals("COMPLETED") || status.getStatus().equals("RETRIEVED") || status.historyExists("transfer-request") || status.historyExists("transfer-completed") || status.historyExists("data-received") || status.historyExists("data-retrieved")) {
                response = httpUtil.getForbiddenResponse("This negotiation can not be canceled! It was already transferred!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            String metaFile = null;
            try {
                metaFile = processManager.cancelProcess(httpRequest, processId);
            } catch (Exception e) {
                response.message = "This negotiation can not be canceled! The process has already finished!";
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (metaFile == null) {
                response.message = "Failed to cancel the negotiation!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            LogUtil.printStatus("[PROCESS " + processId + "] Negotiation [" + contractId + "] was canceled!");

            response = httpUtil.getResponse("The negotiation was canceled!");
            response.data = processManager.getStatus(processId);
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }


    /**
     * HTTP POST method to sign a Contract retrieved from provider and start the negotiation.
     * <p>
     *
     * @param tokenRequestBody the {@code TokenRequest} object with the processId, contractId, policyId the authentication token.
     * @return this {@code Response} HTTP response with the status.
     */
    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    @Operation(summary = "Start the negotiation for an specific asset, contract agreement and policy agreement")
    public Response negotiate(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Long signedAt = DateTimeUtil.getTimestamp();
        Response response = httpUtil.getInternalError();

        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            // Check for the mandatory fields
            List<String> mandatoryParams = List.of("processId", "contractId", "token");
            if (!jsonUtil.checkJsonKeys(tokenRequestBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check for processId
            String processId = tokenRequestBody.getProcessId();
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            Process process = processManager.getProcess(httpRequest, processId);
            if (process == null) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Get status to check for contract id
            String contractId = tokenRequestBody.getContractId();
            Status status = processManager.getStatus(processId);

            // Check if was already declined
            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("This contract was declined! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if negotiation is canceled
            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has been canceled! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if was already agreed
            if (status.historyExists("contract-agreed")) {
                response = httpUtil.getForbiddenResponse("This contract is already agreed!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if there is a contract available
            if (!status.historyExists("contract-dataset")) {
                response = httpUtil.getBadRequest("No contract is available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if the contract id is correct
            History history = status.getHistory("contract-dataset");
            if (!history.getId().contains(contractId)) {
                response = httpUtil.getBadRequest("This contract id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Load all the available contracts
            Map<String, Dataset> availableContracts = processManager.loadDatasets(processId);
            String seedId = String.join("|",availableContracts.keySet()); // Generate Seed
            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, seedId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Dataset dataset = availableContracts.get(contractId);

            if (dataset == null) {
                response.message = "The Contract Selected was not found!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            String policyId = tokenRequestBody.getPolicyId();
            DataTransferService.NegotiateContract contractNegotiation = null;
            // Check if policy is available!
            if (policyId != null) {
                Set policy = edcUtil.getPolicyById(dataset, policyId);
                if (policy == null) {
                    response = httpUtil.getBadRequest("The policy selected does not exists!");
                    return httpUtil.buildResponse(response, httpResponse);
                }
                contractNegotiation = dataService
                        .new NegotiateContract(
                        processManager.loadDataModel(httpRequest),
                        processId,
                        status.getBpn(),
                        dataset,
                        processManager.getStatus(processId),
                        policy
                );
            } else {
                // If the policy is not selected get the first one by default
                contractNegotiation = dataService
                        .new NegotiateContract(
                        processManager.loadDataModel(httpRequest),
                        processId,
                        status.getBpn(),
                        dataset,
                        processManager.getStatus(processId)
                );
            }
            String statusPath = processManager.setAgreed(httpRequest, processId, signedAt, contractId, policyId);
            if (statusPath == null) {
                response.message = "Something went wrong when agreeing with the contract!";
                return httpUtil.buildResponse(response, httpResponse);
            }
            LogUtil.printMessage("[PROCESS " + processId + "] Contract [" + contractId + "] Agreed! Starting negotiation...");
            processManager.startNegotiation(httpRequest, processId, contractNegotiation);
            LogUtil.printStatus("[PROCESS " + processId + "] Negotiation for [" + contractId + "] started!");

            response = httpUtil.getResponse("The contract was agreed successfully! Negotiation started!");
            response.data = processManager.getStatus(processId);
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

    /**
     * HTTP POST method to decline a Passport negotiation.
     * <p>
     *
     * @param tokenRequestBody the {@code TokenRequest} object with the processId, contractId and the authentication token.
     * @return this {@code Response} HTTP response with the status.
     */
    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    @Operation(summary = "Decline passport negotiation")
    public Response decline(@Valid @RequestBody TokenRequest tokenRequestBody) {
        Response response = httpUtil.getInternalError();

        // Check for authentication
        if (!authService.isAuthenticated(httpRequest)) {
            response = httpUtil.getNotAuthorizedResponse();
            return httpUtil.buildResponse(response, httpResponse);
        }
        try {
            // Check for the mandatory fields
            List<String> mandatoryParams = List.of("processId", "token");
            if (!jsonUtil.checkJsonKeys(tokenRequestBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check for processId
            String processId = tokenRequestBody.getProcessId();
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            Process process = processManager.getProcess(httpRequest, processId);
            if (process == null) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Get status to check for contract id
            Status status = processManager.getStatus(processId);

            // Check if was already declined
            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("This contract has already been declined!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has been canceled! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if there is a contract available
            if (!status.historyExists("contract-dataset")) {
                response = httpUtil.getBadRequest("No contract is available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if the contract id is correct
            Map<String, Dataset> availableContracts = processManager.loadDatasets(processId);
            String seedId = String.join("|",availableContracts.keySet()); // Generate Seed
            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, seedId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Decline contract
            String statusPath = processManager.setDecline(httpRequest, processId);
            if (statusPath == null) {
                response.message = "Something went wrong when declining the contract!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            LogUtil.printMessage("[PROCESS " + processId + "] Contracts declined!");
            response = httpUtil.getResponse("The contract negotiation was successfully declined");
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }


    }

}
