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
import org.checkerframework.checker.units.qual.K;
import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.irs.Job;
import org.eclipse.tractusx.productpass.models.irs.JobRequest;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.HttpUtil;
import utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is a service responsible for handling the communication with a external IRS component.
 *
 * <p> It contains all the methods and properties to communicate with the IRS Service.
 * Its is called by the IRS Controller and can be used by managers to update information.
 *
 */
@Service
public class IrsService extends BaseService {

    HttpUtil httpUtil;

    JsonUtil jsonUtil;

    String irsEndpoint;
    String irsJobPath;

    AuthenticationService authService;
    IrsConfig irsConfig;
    VaultService vaultService;
    @Autowired
    public IrsService(Environment env, IrsConfig irsConfig, HttpUtil httpUtil,VaultService vaultService, JsonUtil jsonUtil,AuthenticationService authService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.irsConfig = irsConfig;
        this.vaultService = vaultService;
        this.init(env);
    }

    public Object startJob(String globalAssetId) throws ServiceException{
        try {
            // In case the BPN is not known use the backend BPN.
            return this.startJob(globalAssetId, (String) this.vaultService.getLocalSecret("edc.bpn"));
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"startJob",
                    e,
                    "It was not possible to start a IRS job! Because of invalid BPN configuration!");
        }
    }

    public Map<String, String> startJob(String globalAssetId, String bpn){
        try {
            this.checkEmptyVariables();
            String url = this.irsEndpoint + "/" + this.irsJobPath;
            // Build the Job request for the IRS
            JobRequest body = new JobRequest(
                    new ArrayList<>(),
                    "asBuilt",
                    false,
                    false,
                    "downward",
                    1,
                    false
            );
            body.setKey(globalAssetId, bpn);
            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Map<String, String>) jsonUtil.bindJsonNode(result, Map.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"startJob",
                    e,
                    "It was not possible to start a IRS job!");
        }
    }

    public Job getJob(String jobId) {
        try {
            String url = this.irsEndpoint + "/" + this.irsJobPath + "/" + jobId;
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
            String responseBody = (String) response.getBody();
            return (Job) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), Job.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getJob",
                    e,
                    "It was not possible to get the IRS job!");
        }

    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.irsEndpoint.isEmpty()) {
            missingVariables.add("irs.endpoint");
        }
        if (this.irsJobPath.isEmpty()) {
            missingVariables.add("irs.paths.job");
        }
        return missingVariables;
    }

    public void init(Environment env){
        this.irsEndpoint = this.irsConfig.getEndpoint();
        this.irsJobPath = this.irsConfig.getPaths().getJob();
    }
}
