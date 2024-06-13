/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.verification.config.WalletConfig;
import org.eclipse.tractusx.digitalproductpass.backend.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.backend.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.backend.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.backend.models.service.BaseService;
import org.eclipse.tractusx.digitalproductpass.backend.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.backend.services.VaultService;
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
 * This class is a service responsible for handling the communication with verification of data from the wallet component.
 *
 * <p> It contains all the methods and properties to communicate with the Wallet Service.
 * Its is called by the API Controller and is used by dpp backend component to show verified data.
 */
@Service
public class WalletService extends BaseService {

    /** ATTRIBUTES **/
    HttpUtil httpUtil;
    JsonUtil jsonUtil;
    String walletUrl;
    String verifyEndpoint;
    String callbackUrl;
    String apiKey;
    AuthenticationService authService;
    WalletConfig walletConfig;
    ProcessManager processManager;
    WalletService walletService;
    VaultService vaultService;

    /** CONSTRUCTOR(S) **/
    @Autowired
    public WalletService(Environment env, ProcessManager processManager, WalletConfig walletConfig, WalletService walletService, HttpUtil httpUtil, VaultService vaultService, JsonUtil jsonUtil, AuthenticationService authService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.processManager = processManager;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.walletConfig = walletConfig;
        this.walletService = walletService;
        this.vaultService = vaultService;
        this.init(env);
    }

    public WalletService() {
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for Data Transfer Service by loading from the environment variables and Vault.
     **/
    public void init(Environment env) {
        this.walletUrl = this.walletConfig.getUrl();
        this.verifyEndpoint = this.walletConfig.getEndpoints().getVerify();
        this.apiKey = (String) this.vaultService.getLocalSecret("wallet.apiKey");
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
        if (this.walletUrl.isEmpty()) {
            missingVariables.add("wallet.url");
        }
        if (this.verifyEndpoint.isEmpty()) {
            missingVariables.add("wallet.endpoints.verify");
        }
        if (this.apiKey.isEmpty()) {
            missingVariables.add("wallet.apiKey");
        }
        return missingVariables;
    }

    /**
     * Starts a Job in the IRS for a specific globalAssetId with the param BPN
     * <p>
     * @param   requestBody
     *          the {@code String} request body containing verifiable content
     *
     * @return  a {@code Map<String, String>} map object with the irs first response
     *
     * @throws ServiceException
     *           if unable to start the verification process
     */
    public JsonNode startVerification(JsonNode requestBody) {
        try {


            this.checkEmptyVariables();
            String url = this.walletUrl + this.verifyEndpoint;


            HttpHeaders headers = httpUtil.getHeadersWithApiKey(this.apiKey);
            headers.add("Content-Type", "application/vc+ld+json");

            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), requestBody, false, false);
            return (JsonNode) response.getBody();
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "startVerification",
                    e,
                    "It was not possible to start a verification process from the wallet!");
        }
    }

}
