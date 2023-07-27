/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package org.eclipse.tractusx.productpass.managers;

import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.exceptions.ManagerException;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin3;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.edc.Jwt;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.productpass.models.manager.History;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.eclipse.tractusx.productpass.models.manager.SearchStatus;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.negotiation.*;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.eclipse.tractusx.productpass.models.passports.PassportV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Component
public class ProcessManager {

    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;

    private FileUtil fileUtil;
    private @Autowired ProcessConfig processConfig;
    private @Autowired Environment env;
    private final String metaFileName = "meta";
    private final String datasetFileName = "dataset";
    private final String negotiationFileName = "negotiation";
    private final String transferFileName = "transfer";

    private final String processDataModelName = "processDataModel";

    private final String digitalTwinFileName = "digitalTwin";
    private final String passportFileName = "passport";
    private final String searchFileName = "search";

    @Autowired
    public ProcessManager(HttpUtil httpUtil, JsonUtil jsonUtil, FileUtil fileUtil, ProcessConfig processConfig) {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.fileUtil = fileUtil;
        this.processConfig = processConfig;
    }

    public ProcessDataModel loadDataModel(HttpServletRequest httpRequest) {
        try {
            ProcessDataModel processDataModel = (ProcessDataModel) httpUtil.getSessionValue(httpRequest, this.processDataModelName);
            if (processDataModel == null) {
                processDataModel = new ProcessDataModel();
                this.httpUtil.setSessionValue(httpRequest, "processDataModel", processDataModel);
                LogUtil.printMessage("[PROCESS] Process Data Model created for Session ["+this.httpUtil.getSessionId(httpRequest)+"], the server is ready to start processing requests...");
            }
            return processDataModel;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Failed to load Process DataModel!");
        }
    }

    public void saveDataModel(HttpServletRequest httpRequest, ProcessDataModel dataModel) {
        try {
            httpUtil.setSessionValue(httpRequest, this.processDataModelName, dataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Failed to save Process DataModel!");
        }
    }

    public Process getProcess(HttpServletRequest httpRequest, String processId) {
        try {
            // Getting a process
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            return dataModel.getProcess(processId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to get process [" + processId + "]");
        }
    }


    public String generateStatusToken(Status status, String contractId) {
        return CrypUtil.sha256("signToken=[" + status.getCreated() + "|" + status.id + "|" + contractId + "|" + processConfig.getSignToken() + "]"); // Add extra level of security, that just the user that has this token can sign
    }

    public String generateToken(Process process, String contractId) {
        return CrypUtil.sha256("signToken=[" + process.getCreated() + "|" + process.id + "|" + contractId + "|" + processConfig.getSignToken() + "]"); // Add extra level of security, that just the user that has this token can sign
    }

    public Boolean checkProcess(HttpServletRequest httpRequest, String processId) {
        try {
            // Getting a process
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            return dataModel.processExists(processId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to check if process exists [" + processId + "]");
        }
    }
    public Boolean checkProcess(String processId) {
        try {
            // Getting a process
            String path = this.getProcessFilePath(processId, this.metaFileName);
            return fileUtil.pathExists(path);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to check if process exists [" + processId + "]");
        }
    }




    public void startNegotiation(HttpServletRequest httpRequest, String processId, Runnable contractNegotiation) {
        try {
            // Start the negotiation
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            dataModel.startProcess(processId, contractNegotiation);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to start negotiation for [" + processId + "]");
        }
    }

    public void setProcess(HttpServletRequest httpRequest, Process process) {
        try { // Setting and updating a process
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            dataModel.addProcess(process);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to set process [" + process.id + "]");
        }
    }

    public void setProcessState(HttpServletRequest httpRequest, String processId, String processState) {
        try { // Setting and updating a process state
            ProcessDataModel dataModel = (ProcessDataModel) httpUtil.getSessionValue(httpRequest, this.processDataModelName);
            dataModel.setState(processId, processState);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to set process state [" + processState + "] for process [" + processId + "]");
        }
    }
    public String getProcessState(HttpServletRequest httpRequest, String processId) {
        try { // Setting and updating a process state
            ProcessDataModel dataModel = (ProcessDataModel) httpUtil.getSessionValue(httpRequest, this.processDataModelName);
            return dataModel.getState(processId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to get process state for process [" + processId + "]");
        }
    }
    private String getProcessDir(String processId, Boolean absolute) {
        String dataDir = fileUtil.getDataDir();
        if (absolute) {
            return Path.of(dataDir, processConfig.getDir(), processId).toAbsolutePath().toString();
        } else {
            return Path.of(dataDir, processConfig.getDir(), processId).toString();
        }
    }

    private String getTmpProcessDir(String processId, Boolean absolute) {
        String tmpDir = fileUtil.getTmpDir();
        if (absolute) {
            return Path.of(tmpDir, processConfig.getDir(), processId).toAbsolutePath().toString();
        } else {
            return Path.of(tmpDir, processConfig.getDir(), processId).toString();
        }
    }
    public Process createProcess(HttpServletRequest httpRequest) {
        return this.createProcess(httpRequest, "");
    }

    public Process createProcess(HttpServletRequest httpRequest, String connectorAddress) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(CrypUtil.getUUID(), "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id, connectorAddress, createdTime); // Set the status from the process in file system logs.
        return process;
    }
    public SearchStatus setSearch(String processId, Search search) {
        try {
            String path = this.getTmpProcessFilePath(processId, this.searchFileName);
            SearchStatus searchStatus = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Temporary process file does not exists for id ["+processId+"]!");
            }
            searchStatus = (SearchStatus) jsonUtil.fromJsonFileToObject(path, SearchStatus.class);
            searchStatus.setSearch(search);
            jsonUtil.toJsonFile(path, searchStatus, processConfig.getIndent()); // Store the plain JSON
            return searchStatus;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the search in search status file");
        }
    }
    public String addSearchStatusDtr(String processId, Dtr dtr) {
        try {
            String path = this.getTmpProcessFilePath(processId, this.searchFileName);
            SearchStatus searchStatus = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Temporary process file does not exists for id ["+processId+"]!");
            }
            searchStatus = (SearchStatus) jsonUtil.fromJsonFileToObject(path, SearchStatus.class);
            searchStatus.addDtr(dtr);
            return jsonUtil.toJsonFile(path, searchStatus, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the search status file");
        }
    }
    public String initProcess() {
        try {
            String processId = CrypUtil.getUUID();
            SearchStatus searchProcess = new SearchStatus();
            String searchFilePath = this.getTmpProcessFilePath(processId, this.searchFileName);
            jsonUtil.toJsonFile(searchFilePath, searchProcess, processConfig.getIndent()); // Store the plain JSON
            return processId;
        }catch (Exception e){
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create tmp process file!");
        }
    }


    public Process createProcess(String processId, HttpServletRequest httpRequest) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(processId, "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id,"", createdTime); // Set the status from the process in file system logs.
        return process;
    }
    public Process createProcess(HttpServletRequest httpRequest, String processId, List<String> bpns) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(processId, "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id,"", createdTime); // Set the status from the process in file system logs.
        this.setBpns(process.id, bpns);
        return process;
    }

    public Process createProcess(HttpServletRequest httpRequest, List<String> bpns) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(CrypUtil.getUUID(), "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id,"", createdTime); // Set the status from the process in file system logs.
        this.setBpns(process.id, bpns);
        return process;
    }


    public String newStatusFile(String processId, String connectorAddress, Long created){
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            return jsonUtil.toJsonFile(
                    path,
                    new Status(
                        processId,
                        "CREATED",
                        connectorAddress,
                        created,
                        DateTimeUtil.getTimestamp()
                    ),
                    processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create the status file");
        }
    }
    public String getTmpProcessFilePath(String processId, String filename) {
        String processDir = this.getTmpProcessDir(processId, false);
        return Path.of(processDir, filename + ".json").toAbsolutePath().toString();
    }
    public String getProcessFilePath(String processId, String filename) {
        String processDir = this.getProcessDir(processId, false);
        return Path.of(processDir, filename + ".json").toAbsolutePath().toString();
    }

    public Status getStatus(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            return (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to get the status file");
        }
    }

    public String setBpns(String processId, List<String> bpns) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setBpns(bpns);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }

    public String setStatus(String processId, String historyId, History history) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setStatus(history.getStatus());
            statusFile.setModified(DateTimeUtil.getTimestamp());
            statusFile.setHistory(historyId, history);
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    public String setStatus(String processId, String status) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }
            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setStatus(status);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    public String setBpnFound(HttpServletRequest httpRequest, String processId, List<String> bpnList) {

        this.setProcessState(httpRequest, processId, "CREATED");

        return this.setStatus(processId, "bpn-found", new History(
                processId,
                "CREATED"
        ));
    }

    public String setDecline(HttpServletRequest httpRequest, String processId) {

        this.setProcessState(httpRequest, processId, "ABORTED");

        return this.setStatus(processId, "contract-decline", new History(
                processId,
                "DECLINED"
        ));
    }
    public String cancelProcess(HttpServletRequest httpRequest, String processId) {
    try {
        Process process = this.getProcess(httpRequest, processId);
        Thread thread = process.getThread();
        if (thread == null) {
            throw new ManagerException(this.getClass().getName(), "Thread not found!");
        }
        this.setProcessState(httpRequest, processId, "TERMINATED");
        thread.interrupt(); // Interrupt thread

        return this.setStatus(processId, "negotiation-canceled", new History(
                processId,
                "CANCELLED"
        ));
    }catch (Exception e){
        throw new ManagerException(this.getClass().getName(),e, "It was not possible to cancel the negotiation thread for process ["+processId+"]!");
    }
    }
    public String setSigned(HttpServletRequest httpRequest, String processId, String contractId, Long signedAt) {

        this.setProcessState(httpRequest, processId, "STARTING");

        return this.setStatus(processId, "contract-signed", new History(
                contractId,
                "SIGNED",
                signedAt
        ));
    }

    public Dataset loadDataset(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.datasetFileName);
            return (Dataset) jsonUtil.fromJsonFileToObject(path, Dataset.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the dataset for process id [" + processId + "]");
        }
    }
    public SearchStatus getSearchStatus(String processId) {
        try {
            String path = this.getTmpProcessFilePath(processId, this.searchFileName);
            return (SearchStatus) jsonUtil.fromJsonFileToObject(path, SearchStatus.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the search status for process id [" + processId + "]");
        }
    }


    public Map<String, Object> loadNegotiation(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.negotiationFileName);
            return (Map<String, Object>) jsonUtil.fromJsonFileToObject(path, Map.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the negotiation file for process id [" + processId + "]");
        }
    }
    public  Map<String, Object>  loadTransfer(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.transferFileName);
            return (Map<String, Object>) jsonUtil.fromJsonFileToObject(path, Map.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the transfer file for process id [" + processId + "]");
        }
    }
    public String saveProcessPayload(String processId, Object payload, String fileName, Long startedTime, String assetId, String status, String eventKey) {
        try {
            Boolean encrypt = env.getProperty("passport.dataTransfer.encrypt", Boolean.class, true);
            // Define history
            History history = new History(
                    assetId,
                    status,
                    startedTime
            );
            // Set status
            this.setStatus(processId, eventKey, history);
            String path = this.getProcessFilePath(processId, fileName);
            String returnPath = "";
            if(eventKey.equals("passport-received") && encrypt) {
                returnPath = fileUtil.toFile(path, payload.toString(), false);
            }else {
                returnPath = jsonUtil.toJsonFile(path, payload, processConfig.getIndent());
            }
            if (returnPath == null) {
                history.setStatus("FAILED");
                this.setStatus(processId, assetId, history);
            }
            return returnPath;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the payload [" + assetId + "] with eventKey [" + eventKey + "]!");
        }
    }

    public String saveProcessPayload(String processId, Object payload, String fileName, String assetId, String status, String eventKey) {
        try {
            return this.saveProcessPayload(processId, payload, fileName, DateTimeUtil.getTimestamp(), assetId, status, eventKey);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Failed to save payload!");
        }
    }
    public String saveNegotiation(String processId, Negotiation negotiation, Boolean dtr) {
        try {
            String fileName = !dtr?this.negotiationFileName:"digital-twin-registry/"+this.negotiationFileName;
            String path = this.getProcessFilePath(processId,fileName);
            Map<String, Object> negotiationPayload = (Map<String, Object>) jsonUtil.fromJsonFileToObject(path, Map.class);
            negotiationPayload.put("get", Map.of("response", negotiation));

            return this.saveProcessPayload(
                    processId,
                    negotiationPayload,
                    fileName,
                    negotiation.getContractAgreementId(),
                    "ACCEPTED",
                    "negotiation-accepted");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the negotiation!");
        }
    }
    public String saveTransfer(String processId, Transfer transfer, Boolean dtr) {
        try {
            String fileName = !dtr?this.transferFileName:"digital-twin-registry/"+this.transferFileName;
            String path = this.getProcessFilePath(processId, fileName);
            Map<String, Object> transferPayload = (Map<String, Object>) jsonUtil.fromJsonFileToObject(path, Map.class);
            transferPayload.put("get", Map.of( "response", transfer));

            return this.saveProcessPayload(
                    processId,
                    transferPayload,
                    fileName,
                    transfer.getId(),
                    !dtr?"COMPLETED":"FOUND-DTR",
                    "transfer-completed");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the transfer!");
        }
    }
    public String saveNegotiationRequest(String processId, NegotiationRequest negotiationRequest, IdResponse negotiationResponse, Boolean dtr) {
        try {
            return this.saveProcessPayload(
                    processId,
                    Map.of("init",Map.of("request", negotiationRequest, "response", negotiationResponse)),
                    !dtr?this.negotiationFileName:"digital-twin-registry/"+this.negotiationFileName,
                    negotiationResponse.getId(),
                    !dtr?"NEGOTIATING":"NEGOTIATING-DTR",
                    !dtr?"negotiation-request":"dtr-negotiation-request");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the negotiation request!");
        }
    }

    public String saveTransferRequest(String processId, TransferRequest transferRequest, IdResponse transferResponse, Boolean dtr) {
        try {
            return this.saveProcessPayload(
                    processId,
                    Map.of("init",Map.of("request", transferRequest, "response", transferResponse)),
                    !dtr?this.transferFileName:"digital-twin-registry/"+this.transferFileName,
                     transferResponse.getId(),
                    !dtr?"TRANSFERRING":"TRANSFERRING-DTR",
                    !dtr?"transfer-request":"dtr-transfer-request");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the transfer request!");
        }
    }

    public String setEndpoint(String processId, String endpoint){
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setEndpoint(endpoint);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    public String getContractId(DataPlaneEndpoint endpointData){

        if(!endpointData.offerIdExists()) {
            Jwt token = httpUtil.parseToken(endpointData.getAuthCode());
            return (String) token.getPayload().get("cid");
        }

        return endpointData.getOfferId();
    }
    public PassportV3 loadPassport(String processId){
        try {
            String path = this.getProcessFilePath(processId, this.passportFileName);
            History history = new History(
                    processId,
                    "RETRIEVED"
            );
            if(!fileUtil.pathExists(path)){
                throw new ManagerException(this.getClass().getName(), "Passport file ["+path+"] not found!");
            }
            PassportV3 passport = null;
            Boolean encrypt = env.getProperty("passport.dataTransfer.encrypt", Boolean.class, true);
            if(encrypt){
                Status status = this.getStatus(processId);
                History negotiationHistory = status.getHistory("negotiation-accepted");
                String decryptedPassportJson = CrypUtil.decryptAes(fileUtil.readFile(path), this.generateStatusToken(status, negotiationHistory.getId()));
                // Delete passport file

                passport =  (PassportV3) jsonUtil.loadJson(decryptedPassportJson, PassportV3.class);
            }else{
                passport =  (PassportV3) jsonUtil.fromJsonFileToObject(path, PassportV3.class);
            }

            if(passport == null){
                throw new ManagerException(this.getClass().getName(), "Failed to load the passport");
            }
            Boolean deleteResponse = fileUtil.deleteFile(path);

            if(deleteResponse==null){
                LogUtil.printStatus("[PROCESS " + processId +"] Passport file not found, failed to delete!");
            } else if (deleteResponse) {
                LogUtil.printStatus("[PROCESS " + processId +"] Passport file deleted successfully!");
            } else{
                LogUtil.printStatus("[PROCESS " + processId +"] Failed to delete passport file!");
            }

            this.setStatus(processId,"passport-retrieved", history);
            return passport;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the passport!");
        }
    }
    public String savePassport(String processId, DataPlaneEndpoint endpointData, Passport passport) {
        try {
            // Retrieve the configuration
            Boolean prettyPrint = env.getProperty("passport.dataTransfer.indent", Boolean.class, true);
            Boolean encrypt = env.getProperty("passport.dataTransfer.encrypt", Boolean.class, true);

            Object passportContent = passport;
            Status status = getStatus(processId);
            if(encrypt) {
                // Get token content or the contractID from properties
                String contractId = this.getContractId(endpointData);
                if(contractId == null){
                    throw new ManagerException(this.getClass().getName(), "The Contract Id is null! It was not possible to save the passport!");
                }
                passportContent = CrypUtil.encryptAes(jsonUtil.toJson(passport, prettyPrint), this.generateStatusToken(status, contractId)); // Encrypt the data with the token
            }
            // Save payload
            return this.saveProcessPayload(
                    processId,
                    passportContent,
                    this.passportFileName,
                    endpointData.getId(),
                    "RECEIVED",
                    "passport-received");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the passport!");
        }
    }

    public String saveDigitalTwin(String processId, DigitalTwin digitalTwin, Long startedTime) {
        try {
            return this.saveProcessPayload(
                    processId,
                    digitalTwin,
                    this.digitalTwinFileName,
                    startedTime,
                    digitalTwin.getIdentification(),
                    "READY",
                    "digital-twin-request");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the digitalTwin!");
        }
    }

    public String saveDigitalTwin3(String processId, DigitalTwin3 digitalTwin, Long startedTime) {
        try {
            return this.saveProcessPayload(
                    processId,
                    digitalTwin,
                    this.digitalTwinFileName,
                    startedTime,
                    digitalTwin.getIdentification(),
                    "READY",
                    "digital-twin-request");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the digitalTwin!");
        }
    }



    public String saveDataset(String processId, Dataset dataset, Long startedTime, Boolean dtr) {
        try {
            return this.saveProcessPayload(
                    processId,
                    dataset,
                    !dtr?this.datasetFileName:"digital-twin-registry/"+this.datasetFileName,
                    startedTime,
                    dataset.getId(),
                    "AVAILABLE",
                    "contract-dataset");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the dataset!");
        }
    }

}
