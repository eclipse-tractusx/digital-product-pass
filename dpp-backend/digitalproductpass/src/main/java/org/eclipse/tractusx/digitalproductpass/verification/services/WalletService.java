/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2024 CGI Deutschland B.V. & Co. KG
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

package org.eclipse.tractusx.digitalproductpass.verification.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.core.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.core.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.core.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.core.models.edc.CheckResult;
import org.eclipse.tractusx.digitalproductpass.core.models.service.BaseService;
import org.eclipse.tractusx.digitalproductpass.core.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.core.services.VaultService;
import org.eclipse.tractusx.digitalproductpass.verification.config.VerificationConfig;
import org.eclipse.tractusx.digitalproductpass.verification.config.WalletConfig;
import org.sonarsource.scanner.api.internal.shaded.minimaljson.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.CatenaXUtil;
import utils.HttpUtil;
import utils.JsonUtil;

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

    /**
     * ATTRIBUTES
     **/
    HttpUtil httpUtil;
    JsonUtil jsonUtil;
    String walletUrl;
    String healthEndpoint;

    String verifyEndpoint;
    String apiKey;
    String bpn;
    AuthenticationService authService;
    VerificationConfig verificationConfig;
    ProcessManager processManager;
    VaultService vaultService;
    WalletConfig walletConfig;

    /**
     * CONSTRUCTOR(S)
     **/
    @Autowired
    public WalletService(Environment env, ProcessManager processManager, VerificationConfig verificationConfig, HttpUtil httpUtil, VaultService vaultService, JsonUtil jsonUtil, AuthenticationService authService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.processManager = processManager;
        this.jsonUtil = jsonUtil;
        this.authService = authService;
        this.verificationConfig = verificationConfig;
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
        this.walletConfig = verificationConfig.getWallet();
        this.walletUrl = this.walletConfig.getUrl();
        this.healthEndpoint = this.walletConfig.getEndpoints().getHealth();
        this.verifyEndpoint = this.walletConfig.getEndpoints().getVerify();
        this.apiKey = (String) this.vaultService.getLocalSecret("wallet.apiKey");
        this.bpn = (String) this.vaultService.getLocalSecret("edc.participantId");
    }

    /**
     * Creates a List of missing variables needed to proceed with the request.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the configuration for the request.
     */
    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.walletConfig == null) {
            missingVariables.add("wallet");
        }
        if (this.walletUrl == null || this.walletUrl.isEmpty()) {
            missingVariables.add("wallet.url");
        }
        if (this.healthEndpoint == null || this.healthEndpoint.isEmpty()) {
            missingVariables.add("wallet.endpoints.health");
        }
        if (this.verifyEndpoint == null || this.verifyEndpoint.isEmpty()) {
            missingVariables.add("wallet.endpoints.verify");
        }
        if (this.bpn == null || this.bpn.isEmpty()) {
            missingVariables.add("edc.participantId");
        }
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            missingVariables.add("wallet.apiKey");
        }
        return missingVariables;
    }
    /**
     * Health check for the wallet endpoint
     * <p>
     *
     * @return a {@code Map<String, String>} map object with the irs first response
     * @throws ServiceException if unable to start the verification process
     */
    public Boolean checkHealth(){
        try {
            this.checkEmptyVariables();
            String endpoint = this.walletUrl + this.healthEndpoint;
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = httpUtil.getHeaders();
            ResponseEntity<?> response = null;
            try {
                response = httpUtil.doGet(endpoint, JsonNode.class, headers, params, false, false);
            } catch (Exception e) {
                throw new ServiceException(this.getClass().getName() + ".checkHealth", "It was not possible to get health status from the wallet endpoint ["+endpoint+"]!");
            }
            JsonNode responseBody = (JsonNode) response.getBody();
            return responseBody != null;
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "checkHealth",
                    e,
                    "It was not possible to get readiness status from the edc consumer!");
        }


    }

    /**
     * Starts a Job in the IRS for a specific globalAssetId with the param BPN
     * <p>
     *
     * @param verifiableCredential the {@code String} request body containing verifiable credential content
     * @return a {@code Map<String, String>} map object with the irs first response
     * @throws ServiceException if unable to start the verification process
     */
    public JsonNode verifyCredential(JsonNode verifiableCredential) {
        try {
            this.checkEmptyVariables();
            String url = this.walletUrl + this.verifyEndpoint;
            HttpHeaders headers = httpUtil.getHeadersWithApiKey(this.apiKey);
            headers.add("Content-Type", "application/vc+ld+json"); // Verifiable credential type
            headers.add("BPN", this.bpn); // Add BPN to the request

            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), verifiableCredential, false, false);
            return (JsonNode) response.getBody();
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "startVerification",
                    e,
                    "It was not possible to start a verification process from the wallet!");
        }
    }

}