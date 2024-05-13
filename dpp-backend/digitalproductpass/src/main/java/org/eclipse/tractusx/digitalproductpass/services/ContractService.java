/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA 
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.digitalproductpass.config.DiscoveryConfig;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ControllerException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.edc.AssetSearch;
import org.eclipse.tractusx.digitalproductpass.models.http.Response;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.DiscoverySearch;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.*;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.policy.Set;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * This class consists exclusively of methods to operate on executing the Data Plane operations.
 *
 * <p> The methods defined here are intended to do every needed operations in order to be able to transfer data or passport from Data Plane Endpoint.
 *
 */
@Service
public class ContractService extends BaseService {

    /** ATTRIBUTES **/
    private @Autowired DataTransferService dataService;
    private @Autowired AasService aasService;
    private @Autowired PassportConfig passportConfig;
    private @Autowired DiscoveryConfig discoveryConfig;
    private @Autowired DtrConfig dtrConfig;
    private @Autowired EdcUtil edcUtil;
    private @Autowired PolicyUtil policyUtil;
    @Autowired
    ProcessManager processManager;
    @Autowired
    DtrSearchManager dtrSearchManager;
    @Autowired
    CatenaXService catenaXService;
    @Autowired
    HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;

    /** CONSTRUCTOR(S) **/
    public ContractService() throws ServiceInitializationException {
        this.checkEmptyVariables();
    }

    /** METHODS **/

    /**
     * Creates an empty variables List.
     * <p>
     *
     * @return an empty {@code Arraylist}.
     *
     */
    @Override
    public List<String> getEmptyVariables() {
        return new ArrayList<>();
    }

    /**
     * Creates a new process and checks for the viability of the data retrieval
     * <p>
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   searchBody
     *          the {@code DiscoverySearch} request body.
     *
     * @return  a {@code Response} response object of the method.
     *
     * @throws  ServiceException
     *           if unable to create the process.
     */
    public Response createCall (HttpServletResponse httpResponse, DiscoverySearch searchBody) throws ServiceException {
        try {
            Response response = httpUtil.getInternalError();
            try {
                // If the discovery id type is not defined use the default specified in the configuration
                if(searchBody.getType() == null || searchBody.getType().isEmpty()) {
                    searchBody.setType(this.discoveryConfig.getBpn().getKey()); // Set default configuration key as default
                }
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
                for (BpnDiscovery bpnDiscovery : bpnDiscoveries) {
                    bpnList.addAll(bpnDiscovery.mapBpnNumbers());
                }
                if (bpnList.size() == 0) {
                    response.message = "The asset was not found in the BPN Discovery!";
                    response.status = 404;
                    response.statusText = "Not Found";
                    return httpUtil.buildResponse(response, httpResponse);
                }
                String processId = processManager.initProcess();
                LogUtil.printMessage("Creating process [" + processId + "] for " + searchBody.getType() + ": " + searchBody.getId());
                ConcurrentHashMap<String, List<Dtr>> dataModel = null;
                if (dtrConfig.getTemporaryStorage().getEnabled()) {
                    try {
                        dataModel = this.dtrSearchManager.loadDataModel();
                    } catch (Exception e) {
                        LogUtil.printWarning("Failed to load data model from disk!");
                    }
                }
                // This checks if the cache is deactivated or if the bns are not in the dataModel,  if one of them is not in the data model then we need to check for them
                if (!dtrConfig.getTemporaryStorage().getEnabled() || ((dataModel == null) || !jsonUtil.checkJsonKeys(dataModel, bpnList, ".", false))) {
                    catenaXService.searchDTRs(bpnList, processId);
                } else {
                    boolean requestDtrs = false;
                    // Take the results from cache
                    for (String bpn : bpnList) {
                        List<Dtr> dtrs = null;
                        try {
                            dtrs = (List<Dtr>) jsonUtil.bindReferenceType(dataModel.get(bpn), new TypeReference<List<Dtr>>() {});
                        } catch (Exception e) {
                            throw new ControllerException(this.getClass().getName(), e, "Could not bind the reference type!");
                        }

                        if (dtrs == null || dtrs.size() == 0) {
                            continue;
                        }
                        Long currentTimestamp = DateTimeUtil.getTimestamp();
                        // Iterate over every DTR and add it to the file
                        for (Dtr dtr : dtrs) {

                            Long validUntil = dtr.getValidUntil();
                            //Check if invalid time has come
                            if (dtr.getInvalid() && validUntil > currentTimestamp) {
                                continue;
                            }

                            if (dtr.getContractId() == null || dtr.getContractId().isEmpty() || validUntil == null || validUntil < currentTimestamp) {
                                requestDtrs = true; // If the cache invalidation time has come request Dtrs
                                break;
                            }

                            processManager.addSearchStatusDtr(processId, dtr); //Add valid DTR to status
                        }

                        // If the refresh from the cache is necessary
                        if (requestDtrs) {
                            dtrSearchManager.deleteBpns(dataModel, bpnList); // Delete BPN numbers
                            catenaXService.searchDTRs(bpnList, processId); // Start again the search
                            break;
                        }
                    }
                }

                SearchStatus status = processManager.getSearchStatus(processId);
                if (status == null) {
                    return httpUtil.buildResponse(httpUtil.getNotFound("It was not possible to search for the decentralized digital twin registries"), httpResponse);
                }
                if (status.getDtrs().isEmpty()) {
                    return httpUtil.buildResponse(httpUtil.getNotFound("No valid decentralized digital twin registries found for the configured policies!"), httpResponse);
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
        } catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"createCall",
                    e,
                    "It was not possible to create the process!");
        }
    }

    /**
     * Searches the passport of an asset.
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} the http request object
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   searchBody
     *          the {@code Search} request body.
     *
     * @return  a {@code Response} response object of the method.
     *
     * @throws  ServiceException
     *           if unable to find any digital twin.
     */
    public Response searchCall(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Search searchBody) throws ServiceException {
        try {
            Response response = httpUtil.getInternalError();
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
            // If the id type is not defined use the default specified in the configuration
            if(searchBody.getIdType() == null || searchBody.getIdType().isEmpty()){
                searchBody.setIdType(this.passportConfig.getDefaultIdType());
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
            String bpn = assetSearch.getBpn();

            /*[1]=========================================*/
            // Get catalog with all the contract offers
            if(connectorAddress == null){
                LogUtil.printError("The connector address is empty!");
            }
            if(bpn == null){
                LogUtil.printError("The bpn is empty!");
            }
            if(assetId == null){
                LogUtil.printError("The assetId is empty!");
            }
            Catalog catalog = null;
            Map<String, Dataset> datasets = null;
            Long startedTime = DateTimeUtil.getTimestamp();
            try {
                catalog = dataService.getContractOfferCatalog(connectorAddress, bpn, assetId);
                datasets = edcUtil.filterValidContracts(dataService.getContractOffers(catalog), this.passportConfig.getPolicyCheck());
            } catch (ServiceException e) {
                LogUtil.printError("The EDC is not reachable, it was not possible to retrieve catalog! Trying again...");
                catalog = dataService.getContractOfferCatalog(connectorAddress, bpn, assetId);
                datasets = edcUtil.filterValidContracts(dataService.getContractOffers(catalog), this.passportConfig.getPolicyCheck());
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
                catalog = dataService.getContractOfferCatalog(connectorAddress, bpn, assetId);
                datasets = edcUtil.filterValidContracts(dataService.getContractOffers(catalog), this.passportConfig.getPolicyCheck());
                if (datasets == null) { // If the contract catalog is not reachable retry...
                    response.message = "Asset Id not found in any contract!";
                    response.status = 404;
                    response.statusText = "Not Found";
                    return httpUtil.buildResponse(response, httpResponse);
                }
            }
            processManager.setProviderBpn(processId, catalog.getParticipantId());
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
        } catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"searchCall",
                    e,
                    "It was not possible to find digital twins!");
        }
    }

    /**
     * Does the contract agreement for a contract negotiation
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} the http request object
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   tokenRequestBody
     *          the {@code TokenRequest} token request body.
     *
     * @return  a {@code Response} response object of the method
     *
     * @throws  ServiceException
     *           if unable to do the contract agreement
     */
    public Response doContractAgreement(HttpServletRequest httpRequest, HttpServletResponse httpResponse, TokenRequest tokenRequestBody) throws ServiceException {
        try {
            Response response = httpUtil.getInternalError();
            Long signedAt = DateTimeUtil.getTimestamp();

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
            String seedId = String.join("|", availableContracts.keySet()); // Generate Seed
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
            Set policy = null;
            DataTransferService.NegotiateContract contractNegotiation = null;
            // This function will always get a complaint policy from the allowed ones
            policy = policyUtil.getCompliantPolicyById(dataset, policyId, passportConfig.getPolicyCheck());
            if (policy == null) {
                response = httpUtil.getBadRequest("The policy selected is not allowed per configuration or does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            LogUtil.printMessage("[ASSET"+(policyId == null?"-":"-AUTO-")+"NEGOTIATION] [PROCESS "+processId + "] Selected [POLICY "+policy.getId()+"]:["+this.jsonUtil.toJson(policy, false)+"]!");
            LogUtil.printMessage("[ASSET-NEGOTIATION] [PROCESS "+processId + "] Selected [CONTRACT "+dataset.getId()+"]:["+this.jsonUtil.toJson(dataset, false)+"]!");

            contractNegotiation = dataService
                    .new NegotiateContract(
                    processManager.loadDataModel(httpRequest),
                    processId,
                    status.getProviderBpn(),
                    dataset,
                    processManager.getStatus(processId).getEndpoint(),
                    policy
            );
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
        } catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"doContractAgreement",
                    e,
                    "It was not possible to do the contract agreement!");
        }
    }

    /**
     * Cancels the contract negotiation.
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} the http request object
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   tokenRequestBody
     *          the {@code TokenRequest} token request body.
     *
     * @return  a {@code Response} response object of the method
     *
     * @throws  ServiceException
     *           if unable to cancel the contract.
     */
    public Response cancelCall(HttpServletRequest httpRequest, HttpServletResponse httpResponse, TokenRequest tokenRequestBody) {
        try {
            Response response = httpUtil.getInternalError();
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
        } catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"cancelCall",
                    e,
                    "It was not possible to cancel the contract!");
        }
    }

    /**
     * Declines a contract agreement.
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} the http request object
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   tokenRequestBody
     *          the {@code TokenRequest} token request body.
     *
     * @return  a {@code Response} response object of the method
     *
     * @throws  ServiceException
     *           if unable to decline the contract agreement.
     */
    public Response declineContractAgreement(HttpServletRequest httpRequest, HttpServletResponse httpResponse, TokenRequest tokenRequestBody) {
        try {
            Response response = httpUtil.getInternalError();
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
        } catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"cancelContractAgreement",
                    e,
                    "It was not possible to cancel the contract agreement!");
        }

    }

    /**
     * Get the status from the process with the given processId
     * <p>
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   processId
     *          the {@code String} process identification.
     *
     * @return  a {@code Response} response object of the method.
     *
     */
    public Response statusCall(HttpServletResponse httpResponse, String processId) {
        // Get status
        Response response = httpUtil.getResponse();
        response.data = processManager.getStatus(processId);
        return httpUtil.buildResponse(response, httpResponse);
    }

    /**
     * Retrieves the Passport data.
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} the http request object
     * @param   httpResponse
     *          the {@code HttpServletResponse} the http response object
     * @param   tokenRequestBody
     *          the {@code TokenRequest} token request body.
     *
     * @return  a {@code Response} response object of the method
     *
     * @throws  ServiceException
     *           if unable to retrieve the passport.
     */
    public Response getDataCall (HttpServletRequest httpRequest, HttpServletResponse httpResponse, TokenRequest tokenRequestBody) throws ServiceException {
        try {
            Response response = httpUtil.getInternalError();
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

            if (status.historyExists("contract-decline")) {
                response = httpUtil.getForbiddenResponse("The contract for this passport has been declined!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has been canceled! Please request a new one");
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
            Dataset dataset = availableContracts.get(contractId);

            if (dataset == null) {
                response.message = "The Contract Selected was not found!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (!status.historyExists("data-received")) {
                status = processManager.getStatus(processId); // Retry to get the status before giving an error
                if(!status.historyExists("data-received")) {
                    response = httpUtil.getNotFound("The data is not available!");
                    return httpUtil.buildResponse(response, httpResponse);
                }
            }

            if (status.historyExists("data-retrieved")) {
                response = httpUtil.getNotFound("The data was already retrieved and is no longer available!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            String semanticId = status.getSemanticId();
            JsonNode passport = processManager.loadPassport(processId);
            if (passport == null) {
                response = httpUtil.getNotFound("Failed to load passport!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Map<String, Object> negotiation = processManager.loadNegotiation(processId);
            Map<String, Object> transfer = processManager.loadTransfer(processId);
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "metadata", Map.of(
                            "contract", dataset,
                            "negotiation", negotiation,
                            "transfer", transfer
                    ),
                    "aspect", passport,
                    "semanticId", semanticId
            );
            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getDataCall",
                    e,
                    "It was not possible to retrieve the passport!");
        }
    }
}
