/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Verification Add-on
 *
 * Copyright (c) 2022 BMW AG
 * Copyright (c) 2023 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.digitalproductpass.verification.services;
import org.eclipse.tractusx.digitalproductpass.verification.config.SemanticsHubConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * This class is a service responsible for handling the communication with a external IRS component.
 *
 * <p> It contains all the methods and properties to communicate with the IRS Service.
 * Its is called by the IRS Controller and can be used by managers to update information.
 */
@Service
public class SemanticHubService {

    /** ATTRIBUTES **/
    //HttpUtil httpUtil;
    //JsonUtil jsonUtil;
    String semanticsHubEndpoint;
    String tTlFilePath;
    String jsonSchemaPath;
   // AuthenticationService authService;
    SemanticsHubConfig semanticsHubConfig;
    //VaultService vaultService;

    /** CONSTRUCTOR(S) **/
    @Autowired
    public SemanticHubService(Environment env, SemanticsHubConfig semanticsHubConfig) {
        this.semanticsHubConfig = semanticsHubConfig;
        this.init(env);
    }
//    public SemanticHubService(Environment env, SemanticsHubConfig semanticsHubConfig, HttpUtil httpUtil, VaultService vaultService, JsonUtil jsonUtil, AuthenticationService authService) throws ServiceInitializationException {
//        this.httpUtil = httpUtil;
//        this.jsonUtil = jsonUtil;
//        this.authService = authService;
//        this.semanticsHubConfig = semanticsHubConfig;
//        this.vaultService = vaultService;
//        this.init(env);
//    }

    public SemanticHubService() {
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for SemanticHub Service by loading from the environment variables and Vault.
     **/
    public void init(Environment env) {
        this.semanticsHubEndpoint = this.semanticsHubConfig.getEndpoint();
        this.jsonSchemaPath = this.semanticsHubConfig.getPaths().getJsonSchema();
        this.tTlFilePath = this.semanticsHubConfig.getPaths().getTtlFile();
    }

    /**
     * Retrieves .ttl model from the semantics hub models to a file.
     * <p>
     *
     * @return an {@code String} containing requested model resources.
     *
     */
    public String getJsonSchema() throws Exception {
        try {
            String url = this.semanticsHubEndpoint + "/" + this.jsonSchemaPath;
//            Map<String, Object> params = httpUtil.getParams();
//            HttpHeaders headers = httpUtil.getHeadersWithToken(this.authService.getToken().getAccessToken());
//            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, true, false);
//            String responseBody = (String) response.getBody();
//            return (JobResponse) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), JobResponse.class);
        } catch (Exception e) {
            throw new Exception(this.getClass().getName() + "." + "getJsonSchema: It was not possible to get the IRS job!", e);
        }
        return null;
    }
}
