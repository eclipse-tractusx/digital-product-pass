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

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobRequest;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.DateTimeUtil;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is a service responsible for handling the communication with a external IRS component.
 *
 * <p> It contains all the methods and properties to communicate with the IRS Service.
 * Its is called by the IRS Controller and can be used by managers to update information.
 */
@Service
public class IrsService extends BaseService {

    /** ATTRIBUTES **/
    HttpUtil httpUtil;
    JsonUtil jsonUtil;
    String irsEndpoint;
    String irsJobPath;
    String callbackUrl;
    AuthenticationService authService;
    IrsConfig irsConfig;
    ProcessManager processManager;
    TreeManager treeManager;
    VaultService vaultService;

    /** CONSTRUCTOR(S) **/
    @Autowired
    public IrsService(Environment env, ProcessManager processManager, IrsConfig irsConfig, TreeManager treeManager, HttpUtil httpUtil, VaultService vaultService, JsonUtil jsonUtil, AuthenticationService authService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.processManager = processManager;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.irsConfig = irsConfig;
        this.treeManager = treeManager;
        this.vaultService = vaultService;
        this.init(env);
    }

    public IrsService() {
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for Data Transfer Service by loading from the environment variables and Vault.
     **/
    public void init(Environment env) {
        this.irsEndpoint = this.irsConfig.getEndpoint();
        this.irsJobPath = this.irsConfig.getPaths().getJob();
        this.callbackUrl = this.irsConfig.getCallbackUrl();
    }
    /**
     * Creates a List of missing variables needed to proceed with the request.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the configuration for the request.
     *
     */
    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.irsEndpoint.isEmpty()) {
            missingVariables.add("irs.endpoint");
        }
        if (this.irsJobPath.isEmpty()) {
            missingVariables.add("irs.paths.job");
        }
        if (this.callbackUrl.isEmpty()) {
            missingVariables.add("irs.callbackUrl");
        }
        return missingVariables;
    }
    /**
     * Starts a Job in the IRS for a specific globalAssetId with the backend BPN
     * <p>
     * @param   processId
     *          the {@code String} process id of the job
     * @param   globalAssetId
     *          the {@code String} global asset id from the digital twin
     * @param   searchId
     *          the {@code String} search id provided by the backend to identify the job
     *
     * @return  a {@code Map<String, String>} map object with the irs first response
     *
     * @throws ServiceException
     *           if unable to start the IRS job
     */
    public Map<String, String> startJob(String processId, String globalAssetId, String searchId) throws ServiceException {
        try {
            // In case the BPN is not known use the backend BPN.
            return this.startJob(processId, globalAssetId, searchId, (String) this.vaultService.getLocalSecret("edc.bpn"));
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "startJob",
                    e,
                    "It was not possible to start a IRS job! Because of invalid BPN configuration!");
        }
    }
    /**
     * Starts a Job in the IRS for a specific globalAssetId with the param BPN
     * <p>
     * @param   processId
     *          the {@code String} process id of the job
     * @param   globalAssetId
     *          the {@code String} global asset id from the digital twin
     * @param   searchId
     *          the {@code String} search id provided by the backend to identify the job
     * @param   bpn
     *          the {@code String} bpn number from the provider to search
     *
     * @return  a {@code Map<String, String>} map object with the irs first response
     *
     * @throws ServiceException
     *           if unable to start the IRS job
     */
    public Map<String, String> startJob(String processId, String globalAssetId,  String searchId, String bpn) {
        try {
            this.checkEmptyVariables();
            String url = this.irsEndpoint + "/" + this.irsJobPath;
            // Build the Job request for the IRS

            String backendUrl = this.callbackUrl +  "/" + processId + "/" + searchId; // Add process id and search id
            Map<String, String> params = Map.of(
                    "id", globalAssetId,
                    "state", "COMPLETED"
            );
            String callbackUrl = httpUtil.buildUrl(backendUrl, params, false);
            JobRequest body = new JobRequest(
                    new ArrayList<>(),
                    "asBuilt",
                    false,
                    false,
                    "downward",
                    1,
                    false,
                    callbackUrl
            );
            body.setKey(globalAssetId, bpn);
            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Map<String, String>) jsonUtil.bindJsonNode(result, Map.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "startJob",
                    e,
                    "It was not possible to start a IRS job!");
        }
    }
    /**
     * Gets the children from a specific node in the tree
     * <p>
     * @param   processId
     *          the {@code String} process id of the job
     * @param   path
     *          the {@code String} path of the current node in the tree
     * @param   globalAssetId
     *          the {@code String} global asset id from the digital twin
     * @param   bpn
     *          the {@code String} bpn number from the provider to search
     *
     * @return  a {@code String} of the Job Id created to get the children asynchronously
     *
     * @throws ServiceException
     *           if unable go request the children
     */
    public String getChildren(String processId, String path, String globalAssetId, String bpn) {
        try {
            String searchId = TreeManager.generateSearchId(processId, globalAssetId);
            Long created = DateTimeUtil.getTimestamp();
            Map<String, String> irsResponse = null;
            boolean error = true;
            String exception = "";
            String jobId = null;
            try {
                irsResponse = this.startJob(processId, globalAssetId, searchId, bpn);
            }catch (Exception e) {
                exception = "["+e.getMessage()+"]";
            }
            if(irsResponse != null){
                jobId = irsResponse.get("id");
            }

            if(jobId!=null && !jobId.equals("")){
                error = false;
            }else{
                exception = "The Job Id is null or empty! " + exception;
            }
            if(error){
                LogUtil.printError("[PROCESS "+ processId + "] Failed to create the IRS Job for the globalAssetId [" + globalAssetId+"]! "+exception);
                this.processManager.setJobHistory(
                        processId,
                        new JobHistory(
                                exception,
                                searchId,
                                globalAssetId,
                                path,
                                created,
                                created,
                                -2
                        )
                );
                return null;
            }
            LogUtil.printMessage("[PROCESS "+ processId + "] Job with id [" + jobId + "] created in the IRS for the globalAssetId [" + globalAssetId+"]");
            this.processManager.setJobHistory(
                    processId,
                    new JobHistory(
                        jobId,
                        searchId,
                        globalAssetId,
                        path,
                        created,
                        created,
                    -1
                    )
            );
            return jobId;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getChildren", e, "It was not possible to get the children for the digital twin");
        }
    }
    /**
     * Retrieves a Job from the IRS by id
     * <p>
     * @param   jobId
     *          the {@code String} id from the job to retrieve
     *
     * @return  a {@code JobResponse} object which contains the job information
     *
     * @throws  ServiceException
     *           if unable to retrieve the job
     */
    public JobResponse getJob(String jobId) {
        try {
            String url = this.irsEndpoint + "/" + this.irsJobPath + "/" + jobId;
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (JobResponse) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), JobResponse.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getJob",
                    e,
                    "It was not possible to get the IRS job!");
        }

    }


}
