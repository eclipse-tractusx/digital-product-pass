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

package org.eclipse.tractusx.digitalproductpass.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.request.NegotiationRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.request.TransferRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Negotiation;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.*;

import java.nio.file.Path;
import java.util.Map;

/**
 * This class consists exclusively of methods to operate on managing the Processes.
 *
 * <p> The methods defined here are intended to do every needed operations to run the processes.
 *
 */
@Component
public class ProcessManager {

    /** ATTRIBUTES **/
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

    /** CONSTRUCTOR(S) **/
    @Autowired
    public ProcessManager(HttpUtil httpUtil, JsonUtil jsonUtil, FileUtil fileUtil, ProcessConfig processConfig) {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.fileUtil = fileUtil;
        this.processConfig = processConfig;
    }

    /** METHODS **/

    /**
     * Loads the process data model from the HTTP session, if exists, otherwise creates it.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code ProcessDataModel} object with the loaded data or empty if created.
     *
     * @throws ManagerException
     *           if unable to load or create the process data model.
     */
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

    /**
     * Saves the process data model into a given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   dataModel
     *          the {@code ProcessDataModel} object to save into the HTTP session.
     *
     * @throws ManagerException
     *           if unable to save the process data model.
     */
    @SuppressWarnings("Unused")
    public void saveDataModel(HttpServletRequest httpRequest, ProcessDataModel dataModel) {
        try {
            httpUtil.setSessionValue(httpRequest, this.processDataModelName, dataModel);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Failed to save Process DataModel!");
        }
    }

    /**
     * Gets the Process from an HTTP session with a given processId.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Process} object, if exists.
     *
     * @throws ManagerException
     *           if unable to get the process.
     */
    public Process getProcess(HttpServletRequest httpRequest, String processId) {
        try {
            // Getting a process
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            return dataModel.getProcess(processId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to get process [" + processId + "]");
        }
    }

    /**
     * Generates a SHA256 status token for a given contractId and status.
     * <p>
     * @param   status
     *          the {@code Status} object of the process.
     * @param   contractId
     *          the {@code String} identification of the contract negotiation.
     *
     * @return  a {@code String} with the generated token.
     *
     */
    public String generateStatusToken(Status status, String contractId) {
        return CrypUtil.sha256("signToken=[" + status.getCreated() + "|" + status.id + "|" + contractId + "|" + processConfig.getSignToken() + "]"); // Add extra level of security, that just the user that has this token can sign
    }

    /**
     * Generates a SHA256 status token for a given contractId and process.
     * <p>
     * @param   process
     *          the {@code Process} object.
     * @param   contractId
     *          the {@code String} identification of the contract negotiation.
     *
     * @return  a {@code String} with the generated token.
     *
     */
    public String generateToken(Process process, String contractId) {
        return CrypUtil.sha256("signToken=[" + process.getCreated() + "|" + process.id + "|" + contractId + "|" + processConfig.getSignToken() + "]"); // Add extra level of security, that just the user that has this token can sign
    }

    /**
     * Checks if a Process with the given processId in the given HTTP session exists.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  true if the process exists, false otherwise.
     *
     * @throws ManagerException
     *           if unable to check if the process exists.
     */
    public Boolean checkProcess(HttpServletRequest httpRequest, String processId) {
        try {
            // Getting a process
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            return dataModel.processExists(processId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to check if process exists [" + processId + "]");
        }
    }

    /**
     * Checks if a Process with the given processId exists in the storage file.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  true if the process exists, false otherwise.
     *
     * @throws ManagerException
     *           if unable to check if the process exists.
     */
    public Boolean checkProcess(String processId) {
        try {
            // Getting a process
            String path = this.getProcessFilePath(processId, this.metaFileName);
            return fileUtil.pathExists(path);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to check if process exists [" + processId + "]");
        }
    }

    /**
     * Starts the contract negotiation process for a given HTTP session with the given processId and the task to run.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   contractNegotiation
     *          the {@code Runnable} task for the contract negotiation.
     *
     * @return  true if the process exists, false otherwise.
     *
     * @throws ManagerException
     *           if unable to start the process.
     */
    public void startNegotiation(HttpServletRequest httpRequest, String processId, Runnable contractNegotiation) {
        try {
            // Start the negotiation
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            dataModel.startProcess(processId, contractNegotiation);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to start negotiation for [" + processId + "]");
        }
    }

    /**
     * Sets the given process into the Process Data Model of the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   process
     *          the {@code Process} object to set.
     *
     * @throws ManagerException
     *           if unable to set the process.
     */
    public void setProcess(HttpServletRequest httpRequest, Process process) {
        try { // Setting and updating a process
            ProcessDataModel dataModel = this.loadDataModel(httpRequest);
            dataModel.addProcess(process);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to set process [" + process.id + "]");
        }
    }

    /**
     * Sets the process state of the process with the given processId in the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   processState
     *          the {@code String} value of the new process's state (e.g: "FAILED", "NEGOTIATED", "COMPLETED").
     *
     * @throws ManagerException
     *           if unable to set the process state.
     */
    public void setProcessState(HttpServletRequest httpRequest, String processId, String processState) {
        try { // Setting and updating a process state
            ProcessDataModel dataModel = (ProcessDataModel) httpUtil.getSessionValue(httpRequest, this.processDataModelName);
            dataModel.setState(processId, processState);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to set process state [" + processState + "] for process [" + processId + "]");
        }
    }

    /**
     * Gets the process state of the process with the given processId in the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code String} with the process state value.
     *
     * @throws ManagerException
     *           if unable to get the process state.
     */
    @SuppressWarnings("Unused")
    public String getProcessState(HttpServletRequest httpRequest, String processId) {
        try { // Setting and updating a process state
            ProcessDataModel dataModel = (ProcessDataModel) httpUtil.getSessionValue(httpRequest, this.processDataModelName);
            return dataModel.getState(processId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to get process state for process [" + processId + "]");
        }
    }

    /**
     * Gets the directory's path of the process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   absolute
     *          a {@code Boolean} value to specify if the path is to be the absolute path.
     *
     * @return  a {@code String} with the directory's path of the process, the path will be the absolute path, if the boolean parameter is true.
     *
     */
    private String getProcessDir(String processId, Boolean absolute) {
        String dataDir = fileUtil.getDataDir();
        if (absolute) {
            return Path.of(dataDir, processConfig.getDir(), processId).toAbsolutePath().toString();
        } else {
            return Path.of(dataDir, processConfig.getDir(), processId).toString();
        }
    }

    /**
     * Gets the temporary directory's path of the process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   absolute
     *          a {@code Boolean} value to specify if the path is to be the absolute path.
     *
     * @return  a {@code String} with the directory's path of the process, the path will be the absolute path, if the boolean parameter is true.
     *
     */
    private String getTmpProcessDir(String processId, Boolean absolute) {
        String tmpDir = fileUtil.getTmpDir();
        if (absolute) {
            return Path.of(tmpDir, processConfig.getDir(), processId).toAbsolutePath().toString();
        } else {
            return Path.of(tmpDir, processConfig.getDir(), processId).toString();
        }
    }


    /**
     * Creates a new process in the given HTTP session and set its status in the file system logs.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   connectorAddress
     *          the {@code String} URL address the of the connector.
     *
     * @return  a {@code Process} object representing the created process.
     *
     */
    public Process createProcess(HttpServletRequest httpRequest, String connectorAddress) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(CrypUtil.getUUID(), "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id, connectorAddress, createdTime, true); // Set the status from the process in file system logs.
        return process;
    }

    /**
     * Deletes the temporary process directory with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  true if the temporary directory was successfully deleted, false otherwise.
     *
     * @throws ManagerException
     *           if unable to delete the temporary process directory.
     */
    public Boolean deleteSearchDir(String processId){
        try {
            String path = this.getTmpProcessDir(processId, false);
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Temporary process file does not exists for id ["+processId+"]!");
            }
            fileUtil.deleteDir(path);
            return true;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the search in search status file");
        }
    }

    /**
     * Sets the process search status with the given processId with the given {@code Search} object data.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   search
     *          the {@code Search} object to set.
     *
     * @return  the {@code SearchStatus} object updated.
     *
     * @throws ManagerException
     *           if unable to set the search data.
     */
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

    /**
     * Updates the process search status with the given processId with the given {@code Dtr} object data.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   dtr
     *          the {@code DTR} object data of the Digital Twin Registry.
     *
     * @return  the {@code String} file path where data was storage.
     *
     * @throws ManagerException
     *           if unable to set the search data.
     */
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

    /**
     * Initiates a new empty process and stores it in the temporary directory.
     * <p>
     *
     * @return  the {@code String} process identification.
     *
     * @throws ManagerException
     *           if unable to initiate the process.
     */
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

    /**
     * Creates a new Process with the given processId into the given HTTP session.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code Process} object created.
     *
     * @throws ManagerException
     *           if unable to create the process.
     */
    public Process createProcess(String processId, HttpServletRequest httpRequest) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(processId, "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id,"", createdTime, true); // Set the status from the process in file system logs.
        return process;
    }
    /**
     * Creates a new Process with the given processId into the given HTTP session.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   childrenCondition
     *          the {@code Boolean} condition which enables if the search will request the irs for children
     * @param   httpRequest
     *          the HTTP request.
     *
     * @return  a {@code Process} object created.
     *
     * @throws ManagerException
     *           if unable to create the process.
     */
    public Process createProcess(String processId,Boolean childrenCondition, HttpServletRequest httpRequest) {
        Long createdTime = DateTimeUtil.getTimestamp();
        Process process = new Process(processId, "CREATED", createdTime);
        LogUtil.printMessage("Process Created [" + process.id + "], waiting for user to sign or decline...");
        this.setProcess(httpRequest, process); // Add process to session storage
        this.newStatusFile(process.id,"", createdTime, childrenCondition); // Set the status from the process in file system logs.
        return process;
    }

    /**
     * Creates a new Status file for the given processId, setting it with the given connector address and timestamp.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   connectorAddress
     *          the {@code String} URL address the of the connector.
     * @param   created
     *          the {@code Long} timestamp of the creation.
     * @param   childrenCondition
     *          the {@code Boolean} condition which enables if the search will request the irs for children
     *
     * @return  a {@code String} file path of the created file.
     *
     * @throws ManagerException
     *           if unable to create the status file.
     */
    public String newStatusFile(String processId, String connectorAddress, Long created, Boolean childrenCondition){
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            return jsonUtil.toJsonFile(
                    path,
                    new Status(
                        processId,
                        "CREATED",
                        created,
                        DateTimeUtil.getTimestamp(),
                        new JobHistory(),
                        connectorAddress,
                        "",
                        "",
                            childrenCondition,
                        "",
                        Map.of(), ""
                    ),
                    processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create the status file");
        }
    }

    /**
     * Gets the temporary process file path for a given processId and filename.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   filename
     *          the {@code String} name of the file without the extension (e.g: ".json").
     *
     * @return  a {@code String} file path of the temporary process file.
     *
     */
    public String getTmpProcessFilePath(String processId, String filename) {
        String processDir = this.getTmpProcessDir(processId, false);
        return Path.of(processDir, filename + ".json").toAbsolutePath().toString();
    }

    /**
     * Gets the process file path for a given processId and filename.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   filename
     *          the {@code String} name of the file without the extension (e.g: ".json").
     *
     * @return  a {@code String} file path of the process file.
     *
     */
    public String getProcessFilePath(String processId, String filename) {
        String processDir = this.getProcessDir(processId, false);
        return Path.of(processDir, filename + ".json").toAbsolutePath().toString();
    }

    /**
     * Gets the process status of the process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Status} object with the status information.
     *
     * @throws ManagerException
     *           if unable to get the status file.
     */
     synchronized public Status getStatus(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            return (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to get the status file");
        }
    }

    /**
     * Sets the BPN number of the process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   bpn
     *          the {@code String} BPN number.
     *
     * @return  a {@code String} file path of the process file.
     *
     * @throws ManagerException
     *           if unable to get the status file.
     */
    public String setBpn(String processId, String bpn) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setBpn(bpn);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    /**
     * Sets the Provider BPN number of the process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   providerBpn
     *          the {@code String} BPN number.
     *
     * @return  a {@code String} file path of the process file.
     *
     * @throws ManagerException
     *           if unable to get the status file.
     */
    public String setProviderBpn(String processId, String providerBpn) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setProviderBpn(providerBpn);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }

     /**
     * Set the children condition in the status file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   childrenCondition
     *          the {@code Boolean} condition that indicate if the process is accepting children or not
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
     @SuppressWarnings("Unused")
    public String setChildrenCondition(String processId, Boolean childrenCondition) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setChildren(childrenCondition);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
   /**
     * Set current tree state of the process
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   state
     *          the {@code String} actual state from the tree
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setTreeState(String processId, String state) {
       try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setTreeState(state);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }

    /**
     * Adds the job history in the status file 
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   jobHistory
     *          the {@code JobHistory} job history from the irs 
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setJobHistory(String processId, JobHistory jobHistory) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }
            jsonUtil.setFileValue(path, "job", ".", jobHistory, true);
            return jsonUtil.setFileValue(path, "modified", ".", DateTimeUtil.getTimestamp(), true);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    /**
     * Adds the job history in the status file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   children
     *          the {@code Integer} number of children found
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setJobChildrenFound(String processId, Integer children) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            JobHistory jobHistory = statusFile.getJob();
            jobHistory.setChildren(children);
            jobHistory.setUpdated(DateTimeUtil.getTimestamp());
            statusFile.setJob(jobHistory);
            String searchId = jobHistory.searchId;
            statusFile.setHistory(searchId, new History(searchId, searchId+"-DRILLDOWN-COMPLETED"));
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }
    /**
     * Sets the semantic id in the status file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   semanticId
     *          the {@code String}  semantic ID in the status file
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setSemanticId(String processId, String semanticId) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setSemanticId(semanticId);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to set the semanticId!");
        }
    }

    /**
     * Sets the semantic id in the status file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   endpointId
     *          the {@code String} endpointId from the digital twin registry
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String saveDtr(String processId, String endpointId) {
        try {
            SearchStatus searchStatus = this.getSearchStatus(processId);
            Dtr dtr = searchStatus.getDtr(endpointId);
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setDtr(dtr);
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        }catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the dtr in the status file!");
        }
    }
    /**
     * Sets the semantic id in the status file
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   semanticId
     *          the {@code String}  semantic ID in the status file
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String saveTransferInfo(String processId, String connectorAddress, String semanticId, String dataPlaneUrl, String bpn, Boolean childrenCondition) {
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setSemanticId(semanticId);
            statusFile.setEndpoint(connectorAddress);
            statusFile.setDataPlaneUrl(dataPlaneUrl);
            statusFile.setBpn(bpn);
            statusFile.setChildren(childrenCondition);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to set the semanticId!");
        }
    }
    /**
     * Sets the history of the process's status containing the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   historyId
     *          the {@code String} identification of the process's history entry.
     * @param   history
     *          the {@code History} object with updated values.
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
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

    /**
     * Sets the process's status containing the given processId with the given status.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   status
     *          the {@code String} status to update.
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    @SuppressWarnings("Unused")
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

    /**
     * Sets the process's status containing the given processId with "bpn-found" status, and
     * the process state as "CREATED" in the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    @SuppressWarnings("Unused")
    public String setBpnFound(HttpServletRequest httpRequest, String processId) {

        this.setProcessState(httpRequest, processId, "CREATED");

        return this.setStatus(processId, "bpn-found", new History(
                processId,
                "CREATED"
        ));
    }

    /**
     * Sets the process's status containing the given processId with "contract-decline" status, and
     * the process state as "ABORTED" in the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setDecline(HttpServletRequest httpRequest, String processId) {

        this.setProcessState(httpRequest, processId, "ABORTED");

        return this.setStatus(processId, "contract-decline", new History(
                processId,
                "DECLINED"
        ));
    }

    /**
     * Cancels the process containing the given processId of the given HTTP session and sets it status as "negotiation-canceled", and
     * the process state as "TERMINATED" in the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
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
        } catch (Exception e){
            throw new ManagerException(this.getClass().getName(),e, "It was not possible to cancel the negotiation thread for process ["+processId+"]!");
        }
    }

    /**
     * Sets the history of the process's status containing the given processId with "contract-agreed" historyId, the "AGREED" status
     * and with the given contractId. Also updates the process state as "STARTING" in the given HTTP session.
     * <p>
     * @param   httpRequest
     *          the HTTP request.
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   signedAt
     *          the {@code Long} timestamp when the contract was signed.
     * @param   policyId
     *          the {@code String} timestamp when the contract was signed.
     * @return  a {@code String} file path of the process status file.
     *
     * @throws ManagerException
     *           if unable to update the status file.
     */
    public String setAgreed(HttpServletRequest httpRequest, String processId, Long signedAt, String contractId, String policyId) {
        this.setProcessState(httpRequest, processId, "STARTING");
        return this.setStatus(processId, "contract-agreed", new History(
                contractId+"/"+policyId,
                "AGREED",
                signedAt
        ));
    }

    /**
     * Loads the {@code Dataset} object from the Process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Dataset} object with the loaded data.
     *
     * @throws ManagerException
     *           if unable to load the dataset.
     */
    @Deprecated
    public Dataset loadDataset(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.datasetFileName);
            return (Dataset) jsonUtil.fromJsonFileToObject(path, Dataset.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the dataset for process id [" + processId + "]");
        }
    }
    /**
     * Loads the {@code Dataset} object from the Process with the given processId by a contract id
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Dataset} object with the loaded data.
     *
     * @throws ManagerException
     *           if unable to load the dataset.
     */
    public Dataset loadDataset(String processId, String contractId) {
        try {
            String path = this.getProcessFilePath(processId, this.datasetFileName);
            Map<String, Dataset> datasets = (Map<String, Dataset>) jsonUtil.fromJsonFileToObject(path, Map.class);
            if(datasets == null){
                throw new ManagerException(this.getClass().getName(),"It was not possible to load the dataset for contract id [" + contractId + "] process id [" + processId + "]");
            }
            return datasets.get(contractId);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the dataset for process id [" + processId + "]");
        }
    }
    /**
     * Loads the {@code Dataset} objects from the Process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Dataset} object with the loaded data.
     *
     * @throws ManagerException
     *           if unable to load the dataset.
     */
    public Map<String, Dataset> loadDatasets(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.datasetFileName);
            return (Map<String, Dataset>) this.jsonUtil.bindReferenceType(jsonUtil.fromJsonFileToObject(path, Map.class), new TypeReference<Map<String, Dataset>>() {});
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the dataset for process id [" + processId + "]");
        }
    }


    /**
     * Loads the {@code SearchStatus} object from the Process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code SearchStatus} object with the status data.
     *
     * @throws ManagerException
     *           if unable to load the search status.
     */
    public SearchStatus getSearchStatus(String processId) {
        try {
            String path = this.getTmpProcessFilePath(processId, this.searchFileName);
            return (SearchStatus) jsonUtil.fromJsonFileToObject(path, SearchStatus.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the search status for process id [" + processId + "]");
        }
    }

    /**
     * Loads the negotiation information from the Process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Map<String, Object>} object with the negotiation data.
     *
     * @throws ManagerException
     *           if unable to load the negotiation file.
     */
    public Map<String, Object> loadNegotiation(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.negotiationFileName);
            return (Map<String, Object>) jsonUtil.fromJsonFileToObject(path, Map.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the negotiation file for process id [" + processId + "]");
        }
    }

    /**
     * Loads the transfer data from the Process with the given processId.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code Map<String, Object>} object with the transfer data.
     *
     * @throws ManagerException
     *           if unable to load the transfer file.
     */
    public  Map<String, Object>  loadTransfer(String processId) {
        try {
            String path = this.getProcessFilePath(processId, this.transferFileName);
            return (Map<String, Object>) jsonUtil.fromJsonFileToObject(path, Map.class);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the transfer file for process id [" + processId + "]");
        }
    }

    /**
     * Saves the given payload into the Process with the given processId and updates its status history with the
     * given information. Setting the started time with the given timestamp by the startedTime parameter.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   payload
     *          the {@code Object} object representing the payload.
     * @param   fileName
     *          the {@code String} name of the file to store the payload.
     * @param   startedTime
     *          the {@code Long} timestamp when the process's event started.
     * @param   assetId
     *          the {@code String} identification of the asset.
     * @param   status
     *          the {@code String} status to update.
     * @param   eventKey
     *          the {@code String} name of the event to save (e.g: "negotiation-accepted", "transfer-completed", etc.).
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the payload for the specified event.
     */
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
            if(eventKey.equals("data-received") && encrypt) {
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
    /**
     * Saves the given payload into the Tmp Process with the given processId and updates its status history with the
     * given information. Setting the started time with the given timestamp by the startedTime parameter.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   payload
     *          the {@code Object} object representing the payload.
     * @param   fileName
     *          the {@code String} name of the file to store the payload.
     * @param   assetId
     *          the {@code String} identification of the asset.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the payload for the specified event.
     */
    public String saveTmpProcessPayload(String processId, Object payload, String fileName, String assetId) {
        try {
            String path = this.getTmpProcessFilePath(processId, fileName);
            return jsonUtil.toJsonFile(path, payload, processConfig.getIndent());
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the tmp payload [" + assetId + "]!");
        }
    }

    /**
     * Saves the given payload into the Process with the given processId and updates its status history with the
     * given information.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   payload
     *          the {@code Object} object representing the payload.
     * @param   fileName
     *          the {@code String} name of the file to store the payload.
     * @param   assetId
     *          the {@code String} identification of the asset.
     * @param   status
     *          the {@code String} status to update.
     * @param   eventKey
     *          the {@code String} name of the event to save (e.g: "negotiation-accepted", "transfer-completed", etc.).
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the payload for the specified event.
     */
    public String saveProcessPayload(String processId, Object payload, String fileName, String assetId, String status, String eventKey) {
        try {
            return this.saveProcessPayload(processId, payload, fileName, DateTimeUtil.getTimestamp(), assetId, status, eventKey);
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "Failed to save payload!");
        }
    }

    /**
     * Saves the given {@code Negotation} object with negotiation data in the Process with the given
     * processId and updates its status history.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   negotiation
     *          the {@code Negotiation} object with the updated data.
     * @param   dtr
     *          if true the negotiation is a "digital-twin-registry" type negotiation or "negotiation" type otherwise.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the negotiation.
     */
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
                    !dtr?"ACCEPTED":"DTR-ACCEPTED",
                    !dtr?"negotiation-accepted":"dtr-negotiation-accepted");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the negotiation!");
        }
    }

    /**
     * Saves the given {@code Transfer} object with transfer data in the Process with the given
     * processId and updates its status history.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   transfer
     *          the {@code Transfer} object with the updated data.
     * @param   dtr
     *          if true the transfer is a "digital-twin-registry" type transfer or "transfer" type otherwise.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the transfer.
     */
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
                    !dtr?"transfer-completed":"dtr-transfer-completed");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the transfer!");
        }
    }

    /**
     * Saves the given {@code NegotiationRequest} object with negotiation request data and the negotiation response in the Process with the given
     * processId and updates its status history.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   negotiationRequest
     *          the {@code NegotiationRequest} object with the updated data.
     * @param   negotiationResponse
     *          the {@code NegotiationResponse} object with the negotiation's response data.
     * @param   dtr
     *          if true the negotiation is a "digital-twin-registry" type negotiation or "negotiation" type otherwise.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the negotiation request.
     */
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

    /**
     * Saves the given {@code TransferRequest} object with transfer request data and the transfer response in the Process with the given
     * processId and updates its status history.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   transferRequest
     *          the {@code TransferRequest} object with the updated data.
     * @param   transferResponse
     *          the {@code TransferResponse} object with the transfer's response data.
     * @param   dtr
     *          if true the transfer is a "digital-twin-registry" type transfer or "transfer" type otherwise.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the transfer request.
     */
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

    /**
     * Saves the given {@code Endpoint} URL endpoint in the Process containing the given processId and updates its status.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   endpoint
     *          the {@code String} endpoint URL of the target passport.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the endpoint.
     */
    public String setEndpoint(String processId, String endpoint, String dataPlaneUrl){
        try {
            String path = this.getProcessFilePath(processId, this.metaFileName);
            Status statusFile = null;
            if (!fileUtil.pathExists(path)) {
                throw new ManagerException(this.getClass().getName(), "Process file does not exists for id ["+processId+"]!");
            }

            statusFile = (Status) jsonUtil.fromJsonFileToObject(path, Status.class);
            statusFile.setEndpoint(endpoint);
            statusFile.setDataPlaneUrl(dataPlaneUrl);
            statusFile.setModified(DateTimeUtil.getTimestamp());
            return jsonUtil.toJsonFile(path, statusFile, processConfig.getIndent()); // Store the plain JSON
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to create/update the status file");
        }
    }

    /**
     * Extracts the contractId from the given {@code DataPlaneEndpoint} object.
     * <p>
     * @param   endpointData
     *          the {@code DataPlaneEndpoint} object with data plane endpoint data.
     *
     * @return  a {@code String} identification of the contract.
     *
     */
    public String getContractId(EndpointDataReference endpointData){

        return endpointData.getPayload().getContractId();
    }

    /**
     * Loads the transferred passport of the Process with the given processId after the negotiations being succeeded.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     *
     * @return  a {@code PassportV3} object with the loaded passport data.
     *
     * @throws ManagerException
     *           if unable to load the passport.
     */
    public JsonNode loadPassport(String processId){
        try {
            String path = this.getProcessFilePath(processId, this.passportFileName);
            History history = new History(
                    processId,
                    "RETRIEVED"
            );
            if(!fileUtil.pathExists(path)){
                throw new ManagerException(this.getClass().getName(), "Passport file ["+path+"] not found!");
            }
            JsonNode passport = null;
            Boolean encrypt = env.getProperty("passport.dataTransfer.encrypt", Boolean.class, true);
            if(encrypt){
                Status status = this.getStatus(processId);
                History negotiationHistory = status.getHistory("negotiation-accepted");
                String decryptedPassportJson = CrypUtil.decryptAes(fileUtil.readFile(path), this.generateStatusToken(status, negotiationHistory.getId()));
                // Delete passport file

                passport =  (JsonNode) jsonUtil.loadJson(decryptedPassportJson, JsonNode.class);
            }else{
                passport =  (JsonNode) jsonUtil.fromJsonFileToObject(path, JsonNode.class);
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

            this.setStatus(processId,"data-retrieved", history);
            return passport;
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to load the passport!");
        }
    }

    /**
     * Saves the given Passport in the Process with the given processId encrypted with a token generated by
     * data extracted from the {@code DataPlaneEndpoint} object.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   endpointData
     *          the {@code DataPlaneEndpoint} object with data plane endpoint data.
     * @param   passport
     *          the {@code Passport} object with the passport data to update.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the passport.
     */
    public String savePassport(String processId, EndpointDataReference endpointData, JsonNode passport) {
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
                    throw new ManagerException(this.getClass().getName(), "The Contract Id is null! It was not possible to save the digital product passport!");
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
                    "data-received");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the digital product passport!");
        }
    }

    /**
     * Saves the given {@code DigitalTwin} object in the Process with the given processId with the given timestamp.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   digitalTwin
     *          the {@code DigitalTwin} object to save.
     * @param   startedTime
     *          the {@code Long} timestamp when the process's event started.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the {@code DigitalTwin}.
     */
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

    /**
     * Saves the given {@code Dataset} object in the Process with the given processId with the given timestamp.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   dataset
     *          the {@code Dataset} object to save.
     * @param   startedTime
     *          the {@code Long} timestamp when the process's event started.
     * @param   dtr
     *          if true the file name is a "digital-twin-registry" type or "dataset" type otherwise.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the data set.
     */
    @Deprecated
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

    /**
     * Saves the given {@code Datasets} object in the Process with the given processId with the given timestamp.
     * <p>
     * @param   processId
     *          the {@code String} id of the application's process.
     * @param   datasets
     *          the {@code Dataset} object to save.
     * @param   startedTime
     *          the {@code Long} timestamp when the process's event started.
     *
     * @return  a {@code String} file path of the file where data was stored.
     *
     * @throws ManagerException
     *           if unable to save the data set.
     */
    public String saveDatasets(String processId, Map<String, Dataset> datasets, String contractIds, Long startedTime, Boolean dtr) {
        try {
            return this.saveProcessPayload(
                    processId,
                    datasets,
                    !dtr?this.datasetFileName:"digital-twin-registry/"+this.datasetFileName,
                    startedTime,
                    contractIds,
                    "AVAILABLE",
                    "contract-dataset");
        } catch (Exception e) {
            throw new ManagerException(this.getClass().getName(), e, "It was not possible to save the dataset!");
        }
    }
}
