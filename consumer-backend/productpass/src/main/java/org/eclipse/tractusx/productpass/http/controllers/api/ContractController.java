/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

package org.eclipse.tractusx.productpass.http.controllers.api;

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
import org.apache.juli.logging.Log;
import org.eclipse.tractusx.productpass.config.DiscoveryConfig;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.config.PassportConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.managers.DtrDataModelManager;
import org.eclipse.tractusx.productpass.managers.ProcessDataModel;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.dtregistry.EndPoint;
import org.eclipse.tractusx.productpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.http.requests.DiscoverySearch;
import org.eclipse.tractusx.productpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.manager.History;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.negotiation.Catalog;
import org.eclipse.tractusx.productpass.models.negotiation.Dataset;
import org.eclipse.tractusx.productpass.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import utils.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/contract")
@Tag(name = "Contract Controller")
@SecurityRequirement(name = "BearerAuthentication")
public class ContractController {
    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;
    private @Autowired DataTransferService dataService;
    private @Autowired VaultService vaultService;
    private @Autowired AasService aasService;
    private @Autowired AuthenticationService authService;
    private @Autowired PassportConfig passportConfig;
    private @Autowired DiscoveryConfig discoveryConfig;
    private @Autowired DtrConfig dtrConfig;
    private @Autowired Environment env;
    @Autowired
    ProcessManager processManager;
    @Autowired
    DtrDataModelManager dtrDataModelManager;
    private @Autowired ProcessConfig processConfig;

    @Autowired
    CatenaXService catenaXService;

    @Autowired
    HttpUtil httpUtil;
    private @Autowired JsonUtil jsonUtil;


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

            BpnDiscovery bpnDiscovery = null;
            try {
                bpnDiscovery = catenaXService.getBpnDiscovery(searchBody.getId(), searchBody.getType());
            } catch (Exception e) {
                response.message = "Failed to get the bpn number from the discovery service";
                response.status = 404;
                response.statusText = "Not Found";
                return httpUtil.buildResponse(response, httpResponse);
            }
            if (bpnDiscovery == null) {
                response.message = "Failed to get the bpn number from the discovery service, discovery response is null";
                response.status = 404;
                response.statusText = "Not Found";
                return httpUtil.buildResponse(response, httpResponse);
            }
            List<String> bpnList = bpnDiscovery.getBpnNumbers();
            Process process = processManager.createProcess(httpRequest, bpnList);

            ConcurrentHashMap<String, List<Dtr>> dataModel  = null;
            try {
                dataModel = this.dtrDataModelManager.loadDataModel();
            }catch (Exception e){
                LogUtil.printWarning("Failed to load data model from disk!");
            }
            List<EdcDiscoveryEndpoint> edcEndpointBinded = null;
            // This checks if the bns are in the dataModel, if one of them is not in the data model then we need to check for them
            if((dataModel==null) || !jsonUtil.checkJsonKeys(dataModel, bpnList, ".", false)){
                List<EdcDiscoveryEndpoint> edcEndpoints = catenaXService.getEdcDiscovery(bpnDiscovery.getBpnNumbers());
                try {
                    edcEndpointBinded = (List<EdcDiscoveryEndpoint>) jsonUtil.bindReferenceType(edcEndpoints, new TypeReference<List<EdcDiscoveryEndpoint>>() {});
                } catch (Exception e) {
                    throw new DataModelException(this.getClass().getName(), e, "Could not bind the reference type!");
                }
                if(!this.dtrConfig.getInternalDtr().isEmpty()) {
                    edcEndpointBinded.stream().filter(endpoint -> endpoint.getBpn().equals(vaultService.getLocalSecret("edc.participantId"))).forEach(endpoint -> {
                        endpoint.getConnectorEndpoint().add(this.dtrConfig.getInternalDtr());
                    });
                }
                dataModel = catenaXService.searchDTRs(edcEndpoints);
            }
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "dataModel", dataModel,
                    "processId", process.id
            );
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            assert response != null;
            return httpUtil.buildResponse(httpUtil.getInternalError(e.getMessage()), httpResponse);
        }
    }

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
            List<String> mandatoryParams = List.of("id", "version", "processId");
            if (!jsonUtil.checkJsonKeys(searchBody, mandatoryParams, ".", false)) {
                response = httpUtil.getBadRequest("One or all the mandatory parameters " + mandatoryParams + " are missing");
                return httpUtil.buildResponse(response, httpResponse);
            }

            List<String> versions = passportConfig.getVersions();
            // Initialize variables
            // Check if version is available
            if (!versions.contains(searchBody.getVersion())) {
                return httpUtil.buildResponse(httpUtil.getForbiddenResponse("This passport version is not available at the moment!"), httpResponse);
            }


            // Check for processId
            String processId = searchBody.getProcessId();
            if (!processManager.checkProcess(httpRequest, processId)) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }


            Process process = processManager.getProcess(httpRequest, processId);
            if (process == null) {
                response = httpUtil.getBadRequest("The process id does not exists!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            Status status = processManager.getStatus(processId);
            if (status == null) {
                response = httpUtil.getBadRequest("Not status is available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            ConcurrentHashMap<String, List<Dtr>> dataModel  = null;
            try {
                dataModel = this.dtrDataModelManager.loadDataModel();
            }catch (Exception e){
                LogUtil.printWarning("Failed to load data model from disk!");
            }

            ProcessDataModel processDataModel = processManager.loadDataModel(httpRequest);
            DataTransferService.DigitalTwinRegistryTransfer dtrTransfer = null;
            HashMap<String, Object> bpnNumberTreads = new HashMap<String, Object>();
            for(String bpn: status.getBpns()){
                if(!jsonUtil.keyExists(dataModel, bpn)){
                    LogUtil.printWarning("No Digital Twin found for BPN ["+bpn+"]!");
                    continue;
                }
                HashMap<String, Thread> executingThreads = new HashMap<String, Thread>();
                List<Dtr> dtrs = (List<Dtr>) jsonUtil.bindReferenceType(dataModel.get(bpn), new TypeReference<List<Dtr>>() {});
                for(Dtr dtr: dtrs){
                    dtrTransfer = dataService.new DigitalTwinRegistryTransfer(
                            processDataModel,
                            processId,
                            status,
                            searchBody,
                            bpn,
                            dtr
                    );
                    executingThreads.put(dtr.getEndpoint(), ThreadUtil.runThread(dtrTransfer));
                }
                bpnNumberTreads.put(bpn, executingThreads);
            }
            if(bpnNumberTreads.size() == 0){
                response = httpUtil.getBadRequest("No Digital Twin Registry found!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            //// TODO: get asset id from process history
            String assetId = null;
            String connectorAddress = null;

            /*[1]=========================================*/
            // Get catalog with all the contract offers

            Dataset dataset = null;
            Long startedTime = DateTimeUtil.getTimestamp();
            try {
                dataset = dataService.getContractOfferByAssetId(assetId, connectorAddress);
            } catch (ControllerException e) {
                response.message = "The EDC is not reachable, it was not possible to retrieve catalog!";
                response.status = 502;
                response.statusText = "Bad Gateway";
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if contract offer was not received
            if (dataset == null) {
                response.message = "Asset Id not found in any contract!";
                response.status = 404;
                response.statusText = "Not Found";
                return httpUtil.buildResponse(response, httpResponse);
            }
            LogUtil.printDebug("[PROCESS " + process.id + "] Contract found for asset [" + assetId + "] in EDC Endpoint [" + connectorAddress + "]");

            response = null;
            response = httpUtil.getResponse();
            response.data = Map.of(
                    "id", process.id,
                    "contract", dataset,
                    "token", processManager.generateToken(process, dataset.getId())
            );

            if (processConfig.getStore()) {
                processManager.saveDataset(process.id, dataset, startedTime, false);
            }

            return httpUtil.buildResponse(response, httpResponse);
        } catch (Exception e) {
            assert response != null;
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }

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
            if (!history.getId().equals(contractId)) {
                response = httpUtil.getBadRequest("This contract id is incorrect!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, contractId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            if (status.getStatus().equals("COMPLETED") || status.getStatus().equals("RETRIEVED") || status.historyExists("transfer-request") || status.historyExists("transfer-completed") || status.historyExists("passport-received") || status.historyExists("passport-retrieved")) {
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


    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @Operation(summary = "Sign contract retrieved from provider and start negotiation")
    public Response sign(@Valid @RequestBody TokenRequest tokenRequestBody) {
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
            if (status.historyExists("negotiation-canceled")) {
                response = httpUtil.getForbiddenResponse("This negotiation has been canceled! Please request a new one");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if was already signed
            if (status.historyExists("contract-signed")) {
                response = httpUtil.getForbiddenResponse("This contract is already signed!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if there is a contract available
            if (!status.historyExists("contract-dataset")) {
                response = httpUtil.getBadRequest("No contract is available!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check if the contract id is correct
            History history = status.getHistory("contract-dataset");
            if (!history.getId().equals(contractId)) {
                response = httpUtil.getBadRequest("This contract id is incorrect!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, contractId);
            String token = tokenRequestBody.getToken();
            if (!expectedToken.equals(token)) {
                response = httpUtil.getForbiddenResponse("The token is invalid!");
                return httpUtil.buildResponse(response, httpResponse);
            }
            Dataset dataset = processManager.loadDataset(processId);

            if (dataset == null) {
                response.message = "Dataset not found!";
                return httpUtil.buildResponse(response, httpResponse);
            }

            String statusPath = processManager.setSigned(httpRequest, processId, contractId, signedAt);
            if (statusPath == null) {
                response.message = "Something went wrong when signing the contract!";
                return httpUtil.buildResponse(response, httpResponse);
            }
            LogUtil.printMessage("[PROCESS " + processId + "] Contract [" + contractId + "] signed! Starting negotiation...");

            DataTransferService.NegotiateContract contractNegotiation = dataService
                    .new NegotiateContract(
                    processManager.loadDataModel(httpRequest),
                    processId,
                    dataset,
                    processManager.getStatus(processId)
            );
            processManager.startNegotiation(httpRequest, processId, contractNegotiation);
            LogUtil.printStatus("[PROCESS " + processId + "] Negotiation for [" + contractId + "] started!");

            response = httpUtil.getResponse("The contract was signed successfully! Negotiation started!");
            response.data = processManager.getStatus(processId);
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }
    }


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
            History history = status.getHistory("contract-dataset");
            if (!history.getId().equals(contractId)) {
                response = httpUtil.getBadRequest("This contract id is incorrect!");
                return httpUtil.buildResponse(response, httpResponse);
            }

            // Check the validity of the token
            String expectedToken = processManager.generateToken(process, contractId);
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

            LogUtil.printMessage("[PROCESS " + processId + "] Contract [" + contractId + "] declined!");
            response = httpUtil.getResponse("The contract negotiation was successfully declined");
            return httpUtil.buildResponse(response, httpResponse);

        } catch (Exception e) {
            response.message = e.getMessage();
            return httpUtil.buildResponse(response, httpResponse);
        }


    }

}
