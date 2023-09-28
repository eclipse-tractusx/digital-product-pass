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
import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
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
    AuthenticationService authService;
    IrsConfig irsConfig;
    @Autowired
    public IrsService(Environment env, IrsConfig irsConfig, HttpUtil httpUtil, JsonUtil jsonUtil,AuthenticationService authService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.irsConfig = irsConfig;
        this.init(env);
    }

    public Object startJob(){
        try {
            this.checkEmptyVariables();
            // Add body
            Object body = Map.of(
                    "searchParam", search
            );

            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
            headers.add("Content-Type", "application/json");

            ResponseEntity<?> response = httpUtil.doPost(this.irsEndpoint, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return jsonUtil.bindJsonNode(result, Object.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"searchComponents",
                    e,
                    "It was not possible to search for the drill down of components!");
        }
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.irsEndpoint.isEmpty()) {
            missingVariables.add("irs.endpoint");
        }
        return missingVariables;
    }

    public void init(Environment env){
        this.irsEndpoint = this.irsConfig.getEndpoint();
    }
}
