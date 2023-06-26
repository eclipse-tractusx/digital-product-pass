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

package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.productpass.exceptions.ControllerException;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.ProcessDataModel;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.productpass.models.manager.History;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.negotiation.*;
import org.eclipse.tractusx.productpass.models.passports.PassportV3;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import javax.xml.crypto.Data;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataTransferService extends BaseService {



    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;

    public String apiKey;
    public String bpnNumber;

    public String edcEndpoint;

    public String managementPath;
    public String catalogPath;
    public String negotiationPath;
    public String transferPath;

    public Environment env;

    public ProcessManager processManager;

    @Autowired
    public DataTransferService(Environment env, HttpUtil httpUtil, JsonUtil jsonUtil, VaultService vaultService, ProcessManager processManager) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.processManager = processManager;
        this.env = env;
        this.init(vaultService, env);
        this.checkEmptyVariables(List.of("apiKey")); // Add API Key as optional for initialization
    }

    public void init(VaultService vaultService, Environment env){
        this.apiKey = (String) vaultService.getLocalSecret("edc.apiKey");
        this.bpnNumber = (String) vaultService.getLocalSecret("edc.participantId");
        this.edcEndpoint = env.getProperty("configuration.edc.endpoint", "");
        this.catalogPath = env.getProperty("configuration.edc.catalog", "");
        this.managementPath = env.getProperty("configuration.edc.management", "");
        this.negotiationPath = env.getProperty("configuration.edc.negotiation", "");
        this.transferPath = env.getProperty("configuration.edc.transfer", "");
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.edcEndpoint == null || this.edcEndpoint.isEmpty()) {
            missingVariables.add("endpoint");
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            missingVariables.add("apiKey");
        }
        if (this.bpnNumber == null || this.bpnNumber.isEmpty()) {
            missingVariables.add("bpnNumber");
        }
        if (this.managementPath == null || this.managementPath.isEmpty()) {
            missingVariables.add("management");
        }
        if (this.catalogPath == null || this.catalogPath.isEmpty()) {
            missingVariables.add("catalog");
        }
        if (this.negotiationPath == null || this.negotiationPath.isEmpty()) {
            missingVariables.add("negotiation");
        }
        if (this.transferPath == null || this.transferPath.isEmpty()) {
            missingVariables.add("transfer");
        }

        return missingVariables;
    }

    public Dataset getContractOfferByAssetId(String assetId, String providerUrl) throws ControllerException {
        /*
         *   This method receives the assetId and looks up for targets with the same name.
         */
        try {
            Catalog catalog = this.getContractOfferCatalog(providerUrl);
            Map<String, Integer> offers = catalog.loadContractOffersMapByAssetId();
            if (!offers.containsKey(assetId)) {
                return null;
            }
            Integer index = offers.get(assetId);
            return catalog.getContractOffers().get(index);
        } catch (Exception e) {
            throw new ControllerException(this.getClass().getName(), e, "It was not possible to get Contract Offer for assetId [" + assetId + "]");
        }
    }

    public class NegotiateContract implements Runnable{
        private NegotiationRequest negotiationRequest;
        private ProcessDataModel dataModel;
        private Dataset dataset;

        private Negotiation negotiation;
        private Transfer transfer;
        private TransferRequest transferRequest;
        private IdResponse negotiationResponse;

        private IdResponse tranferResponse;
        private Integer negotiationAttempts;

        private Integer transferAttempts;
        private Status status;

        private String processId;

        public NegotiateContract(ProcessDataModel dataModel, String processId, Dataset dataset, Status status){
            this.dataModel = dataModel;
            this.processId = processId;
            this.dataset = dataset;
            this.status = status;
            this.negotiationRequest = this.buildRequest(dataset, status);
        }

        public NegotiationRequest buildRequest(Dataset dataset, Status status){
            Offer contractOffer = this.buildOffer(dataset);
            return new NegotiationRequest(
                    jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/")),
                    status.getEndpoint(),
                    bpnNumber,
                    contractOffer
            );
        }

        public TransferRequest buildTransferRequest(Dataset dataset, Status status, Negotiation negotiation){
            try {
                Offer contractOffer = this.buildOffer(dataset);
                String receiverEndpoint = env.getProperty("configuration.edc.receiverEndpoint") + "/" + this.processId; // Send process Id to identification the session.
                TransferRequest.TransferType transferType = new TransferRequest.TransferType();

                transferType.setContentType("application/octet-stream");
                transferType.setIsFinite(true);

                TransferRequest.DataDestination dataDestination = new TransferRequest.DataDestination();
                dataDestination.setProperties(new Properties("HttpProxy"));

                TransferRequest.PrivateProperties privateProperties = new TransferRequest.PrivateProperties();
                privateProperties.setReceiverHttpEndpoint(receiverEndpoint);
                return new TransferRequest(
                        jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/")),
                        dataset.getAssetId(),
                        status.getEndpoint(),
                        negotiation.getContractAgreementId(),
                        dataDestination,
                        false,
                        privateProperties,
                        "dataspace-protocol-http",
                        transferType
                );
            }catch (Exception e){
                throw new ServiceException(this.getClass().getName(), e, "Failed to build the transfer request!");
            }
        }
        @Override
        public void run() {
            // NEGOTIATIONGIH PROCESS
            try {
                processManager.saveNegotiationRequest(processId, negotiationRequest, new IdResponse(processId, null));
                this.negotiationResponse = this.requestNegotiation(this.negotiationRequest);
                processManager.saveNegotiationRequest(processId, negotiationRequest, negotiationResponse);
                this.negotiation = this.getNegotiationData(negotiationResponse);
                if(this.negotiation == null){
                    return;
                }
                processManager.saveNegotiation(this.processId, this.negotiation);
                String state = this.negotiation.getState();
                if (!(state.equals("CONFIRMED") || state.equals("FINALIZED"))) {
                    throw new ServiceException(this.getClass().getName(), "Contract Negotiation Process Failed ["+this.negotiation.getId()+"]");
                }
            }catch (Exception e){
                processManager.setStatus(this.processId, "negotiation-failed", new History(
                        this.processId,
                        "FAILED"
                ));
                this.dataModel.setState(processId, "FAILED");
                throw new ServiceException(this.getClass().getName(), e, "Failed to do the contract negotiation!");
            }

            if(this.dataModel.getState(processId).equals("TERMINATED")){
                LogUtil.printMessage("Terminated process " + processId + "stopped transfer!");
                return;
            };
            this.dataModel.setState(processId, "NEGOTIATED");
            LogUtil.printStatus("[PROCESS "+ this.processId+"] Negotiation Finished with status ["+negotiation.getState()+"]!");
            // TRANSFER PROCESS
            try{
                this.transferRequest = buildTransferRequest(this.dataset, this.status, this.negotiation);
                processManager.saveTransferRequest(this.processId, transferRequest, new IdResponse(processId, null));
                this.tranferResponse = this.requestTransfer(transferRequest);
                processManager.saveTransferRequest(this.processId, transferRequest, this.tranferResponse);
                this.transfer = this.getTransferData(this.tranferResponse);
                if(this.transfer == null){
                    return;
                }
                processManager.saveTransfer(this.processId, transfer);
                if (!transfer.getState().equals("COMPLETED")) {
                    throw new ServiceException(this.getClass().getName(), "Transfer Process Failed ["+this.tranferResponse.getId()+"]");
                }
            }catch (Exception e){
                processManager.setStatus(processId, "transfer-failed", new History(
                        processId,
                        "FAILED"
                ));
                this.dataModel.setState(processId, "FAILED");
                throw new ServiceException(this.getClass().getName(), e, "Failed to do the contract transfer");
            }
            this.dataModel.setState(processId, "COMPLETED");
            LogUtil.printStatus("[PROCESS "+ this.processId+"] Negotiation and Transfer Completed!");
        }
        public Negotiation getNegotiationData(IdResponse negotiationResponse) {
            Negotiation negotiation = null;
            try {
                negotiation = seeNegotiation(negotiationResponse.getId(), this.processId, this.dataModel);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to get the negotiation ["+negotiationResponse.getId()+"]");
            }
            return negotiation;
        }

        public IdResponse requestNegotiation(NegotiationRequest negotiationRequest) {
            IdResponse negotiationResponse = null;
            try {
                negotiationResponse = doContractNegotiation(negotiationRequest);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to start the negotiation for offer ["+negotiationRequest.getOffer().getOfferId()+"]");
            }

            if (negotiationResponse.getId() == null) {
                throw new ServiceException(this.getClass().getName(), "The ID from the Offer is null ["+negotiationRequest.getOffer().getOfferId()+"]");
            }
            LogUtil.printMessage("[PROCESS "+ this.processId+"] Negotiation Requested ["+negotiationResponse.getId()+"]");
            return negotiationResponse;
        }
        public IdResponse requestTransfer(TransferRequest transferRequest) {
            IdResponse transferResponse = null;
            try {
                transferResponse = initiateTransfer(transferRequest);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to start the transfer for contract  ["+transferRequest.getContractId()+"]");
            }
            if (transferResponse.getId() == null) {
                throw new ServiceException(this.getClass().getName(), "The ID from the transfer is null for contract  ["+transferRequest.getContractId()+"]");
            }
            LogUtil.printStatus("[PROCESS "+ this.processId+"] Transfer Requested ["+transferResponse.getId()+"]");
            return transferResponse;
        }

        public Transfer getTransferData(IdResponse transferData){
            /*[8]=========================================*/
            // Check for transfer updates and the status
            Transfer transfer = null;
            try {
                transfer = seeTransfer(transferData.getId(), this.processId, this.dataModel);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName(), e, "Failed to get the negotiation ["+transferData.getId()+"]");
            }
            return transfer;
        }

        public void setNegotiationRequest(NegotiationRequest negotiationRequest) {
            this.negotiationRequest = negotiationRequest;
        }

        public Dataset getDataset() {
            return dataset;
        }

        public void setDataset(Dataset dataset) {
            this.dataset = dataset;
        }

        public Offer buildOffer(Dataset dataset){
            Set policyCopy = (Set) jsonUtil.bindObject(dataset.getPolicy(), Set.class);
            policyCopy.setId(null);
            return new Offer(
                    dataset.getPolicy().getId(),
                    dataset.getAssetId(),
                    policyCopy
            );
        }

        public Negotiation getNegotiation() {
            return negotiation;
        }

        public void setNegotiation(Negotiation negotiation) {
            this.negotiation = negotiation;
        }

        public NegotiationRequest getNegotiationRequest() {
            return negotiationRequest;
        }

        public ProcessDataModel getDataModel() {
            return dataModel;
        }

        public void setDataModel(ProcessDataModel dataModel) {
            this.dataModel = dataModel;
        }

        public Integer getNegotiationAttempts() {
            return negotiationAttempts;
        }

        public void setNegotiationAttempts(Integer negotiationAttempts) {
            this.negotiationAttempts = negotiationAttempts;
        }

        public Integer getTransferAttempts() {
            return transferAttempts;
        }

        public void setTransferAttempts(Integer transferAttempts) {
            this.transferAttempts = transferAttempts;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public String getProcessId() {
            return processId;
        }

        public void setProcessId(String processId) {
            this.processId = processId;
        }

        public Transfer getTransfer() {
            return transfer;
        }

        public void setTransfer(Transfer transfer) {
            this.transfer = transfer;
        }

        public TransferRequest getTransferRequest() {
            return transferRequest;
        }

        public void setTransferRequest(TransferRequest transferRequest) {
            this.transferRequest = transferRequest;
        }

        public IdResponse getNegotiationResponse() {
            return negotiationResponse;
        }

        public void setNegotiationResponse(IdResponse negotiationResponse) {
            this.negotiationResponse = negotiationResponse;
        }

        public IdResponse getTranferResponse() {
            return tranferResponse;
        }

        public void setTranferResponse(IdResponse tranferResponse) {
            this.tranferResponse = tranferResponse;
        }
    }

    public Catalog getContractOfferCatalog(String providerUrl) {
        try {
            this.checkEmptyVariables();

            String url = CatenaXUtil.buildManagementEndpoint(env, this.catalogPath);
            // Simple catalog request query with no limitation.
            Object body = new CatalogRequest(
                jsonUtil.newJsonNode(),
                providerUrl,
                new CatalogRequest.QuerySpec()
            );

            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Catalog) jsonUtil.bindJsonNode(result, Catalog.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }
    public IdResponse doContractNegotiation(NegotiationRequest negotiationRequest) {
        try {
            this.checkEmptyVariables();
            LogUtil.printDebug("["+negotiationRequest.getOffer().getOfferId()+"] ===== [INITIALIZING CONTRACT NEGOTIATION] ===========================================");
            String url = CatenaXUtil.buildManagementEndpoint(env, this.negotiationPath);
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), negotiationRequest, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (IdResponse) jsonUtil.bindJsonNode(result, IdResponse.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                    e,
                    "It was not possible to retrieve the contract negotiation!");
        }
    }
    public IdResponse doContractNegotiations(Offer contractOffer, String providerUrl) {
        try {
            this.checkEmptyVariables();
            NegotiationRequest body = new NegotiationRequest(
                    jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/")),
                    providerUrl,
                    this.bpnNumber,
                    contractOffer
            );
            return this.doContractNegotiation(body);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                    e,
                    "It was not possible to execute the contract negotiation!");
        }
    }

    public Negotiation seeNegotiation(String id, String processId, ProcessDataModel dataModel) {
        try {
            this.checkEmptyVariables();

            String endpoint = CatenaXUtil.buildManagementEndpoint(env, this.negotiationPath);
            // Get variables from configuration
            String url = endpoint + "/" + id;
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            Map<String, Object> params = httpUtil.getParams();
            JsonNode body = null;
            String actualState = "";
            boolean sw = true;
            Instant start = Instant.now();
            Instant end = start;
            LogUtil.printDebug("["+id+"] ===== [STARTING CHECKING STATUS FOR CONTRACT NEGOTIATION]  ===========================================");
            while (sw) {
                ResponseEntity<?> response = httpUtil.doGet(url, JsonNode.class, headers, params, false, false);
                body = (JsonNode) response.getBody();
                if(body == null){
                    sw = false;
                    throw new ServiceException(this.getClass().getName() + "." + "getNegotiations",
                            "No response received from url [" + url + "]!");
                }
                if (!body.has("edc:state") || body.get("edc:state") == null) {
                    LogUtil.printDebug("["+id+"] ===== [ERROR CONTRACT NEGOTIATION] ===========================================");
                    throw new ServiceException(this.getClass().getName() + "." + "getNegotiations",
                            "It was not possible to do contract negotiations!");
                }
                String state = body.get("edc:state").asText();
                if (state.equals("CONFIRMED") || state.equals("ERROR") || state.equals("FINALIZED")  || state.equals("TERMINATED") || state.equals("TERMINATING")) {
                    sw = false;
                    LogUtil.printDebug("["+id+"] ===== [FINISHED CONTRACT NEGOTIATION] ===========================================");
                }
                if (!state.equals(actualState)) {
                    actualState = state; // Update current state
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    LogUtil.printDebug("["+id+"] The contract negotiation status changed: [" + state + "] - TIME->[" + timeElapsed + "]s");
                    start = Instant.now();
                }
                if(dataModel.getState(processId).equals("TERMINATED")){
                    LogUtil.printStatus("["+id+"] The negotiation was cancelled");
                    return null;
                }
            }
            return (Negotiation) jsonUtil.bindJsonNode(body, Negotiation.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getNegotiation",
                    e,
                    "It was not possible to see the contract negotiation!");
        }
    }


    public IdResponse initiateTransfer(TransferRequest transferRequest) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            // Get variables from configuration
            String url = CatenaXUtil.buildManagementEndpoint(env, this.transferPath);

            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            Object body = transferRequest;
            ResponseEntity<?> response = httpUtil.doPost(url, String.class, headers, httpUtil.getParams(), body, false, false);
            String responseBody = (String) response.getBody();
            return (IdResponse) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), IdResponse.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doTransferProcess",
                    e,
                    "It was not possible to initiate transfer process!");
        }
    }

    public Transfer seeTransfer(String id, String processId, ProcessDataModel dataModel) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            String endpoint = CatenaXUtil.buildManagementEndpoint(env, this.transferPath);
            String path = endpoint + "/" + id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", this.apiKey);
            Map<String, Object> params = httpUtil.getParams();
            JsonNode body =  null;
            String actualState = "";
            boolean sw = true;
            Instant start = Instant.now();
            Instant end = start;
            LogUtil.printDebug("["+id+"] ===== [STARTING CONTRACT TRANSFER] ===========================================");
            while (sw) {
                ResponseEntity<?> response = httpUtil.doGet(path, JsonNode.class, headers, params, false, false);
                body = (JsonNode) response.getBody();
                if(body == null){
                    sw = false;
                    throw new ServiceException(this.getClass().getName() + "." + "getNegotiations",
                            "No response received from url [" + path + "]!");
                }
                if (!body.has("edc:state") || body.get("edc:state") == null) {
                    LogUtil.printDebug("["+id+"] ===== [ERROR CONTRACT TRANSFER]===========================================");
                    throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                            "It was not possible to do the transfer process!");
                }
                String state = body.get("edc:state").asText();
                if (state.equals("COMPLETED") || state.equals("ERROR") || state.equals("FINALIZED")  || state.equals("VERIFIED") || state.equals("TERMINATED") || state.equals("TERMINATING")) {
                    LogUtil.printDebug("["+id+"] ===== [FINISHED CONTRACT TRANSFER] ["+id+"]===========================================");
                    sw = false;
                }
                if (!state.equals(actualState)) {
                    actualState = state; // Update current state
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    LogUtil.printDebug("["+id+"] The data transfer status changed: [" + state + "] - TIME->[" + timeElapsed + "]s");
                    start = Instant.now();
                }
                if(dataModel.getState(processId).equals("TERMINATED")){
                    LogUtil.printStatus("["+id+"] The transfer was cancelled");
                    return null;
                }
            }
            return (Transfer) jsonUtil.bindJsonNode(body, Transfer.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                    e,
                    "It was not possible to transfer the contract! " + id);
        }
    }


    public PassportV3 getPassportV3(String transferProcessId, String endpoint) {
        try {
            this.checkEmptyVariables();
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Accept", "application/octet-stream");
            boolean retry = false;

            ResponseEntity<?> response = null;
            try {
                response = httpUtil.doGet(endpoint, String.class, headers, params, false, false);
            }catch (Exception e){
                throw new ServiceException(this.getClass().getName() + ".getPassportV3", "It was not possible to get passport with id " + transferProcessId);
            }
            String responseBody = (String) response.getBody();
            return (PassportV3) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), PassportV3.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getPassportV3",
                    e,
                    "It was not possible to retrieve the getPassport V1 for transferProcessId ["+transferProcessId+"]!");
        }
    }

    /*
    STATIC FUNCTIONS
     */
    public static String generateTransferId(Negotiation negotiation, String connectorId, String connectorAddress) {
        return CrypUtil.sha256(DateTimeUtil.getDateTimeFormatted("yyyyMMddHHmmssSSS") + negotiation.getId() + connectorId + connectorAddress);
    }

}
