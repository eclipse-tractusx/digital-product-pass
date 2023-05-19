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
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.negotiation.*;
import org.eclipse.tractusx.productpass.models.passports.PassportV3;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.sonarsource.scanner.api.internal.shaded.minimaljson.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataTransferService extends BaseService {


    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;

    public String APIKey;

    public String serverUrl;
    public String providerUrl;


    @Autowired
    public DataTransferService(Environment env, HttpUtil httpUtil, JsonUtil jsonUtil, VaultService vaultService) throws ServiceInitializationException {
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.init(vaultService, env);
        this.checkEmptyVariables(List.of("APIKey")); // Add API Key as optional for initialization
    }

    public void init(VaultService vaultService, Environment env){
        this.APIKey = (String) vaultService.getLocalSecret("apiKey");
        this.serverUrl = env.getProperty("configuration.endpoints.serverUrl", "");
        this.providerUrl = env.getProperty("configuration.endpoints.providerUrl", "");
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.serverUrl == null || this.serverUrl.isEmpty()) {
            missingVariables.add("serverUrl");
        }
        if (APIKey == null || APIKey.isEmpty()) {
            missingVariables.add("APIKey");
        }
        if (this.serverUrl == null || this.providerUrl.isEmpty()) {
            missingVariables.add("providerUrl");
        }
        return missingVariables;
    }

    public Catalog getContractOfferCatalog(String providerUrl) {
        try {
            this.checkEmptyVariables();
            String provider = providerUrl;
            String path = "/consumer/data/catalog";
            if (providerUrl == null) {
                provider = this.providerUrl;
            }
            String url =  this.serverUrl  + path;
            Map<String, Object> params = httpUtil.getParams();
            params.put("providerUrl", provider);
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            ResponseEntity<?> response = httpUtil.doGet(url, String.class, headers, params, false, false);
            String body = (String) response.getBody();
            JsonNode json = jsonUtil.toJsonNode(body);
            return (Catalog) jsonUtil.bindJsonNode(json, Catalog.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getContractOfferCatalog",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    public Negotiation doContractNegotiations(Offer contractOffer,String providerUrl) {
        try {
            this.checkEmptyVariables();
            contractOffer.open();
            String provider = providerUrl;
            LogUtil.printDebug("["+contractOffer.getId()+"] ===== [INITIALIZING CONTRACT NEGOTIATION] ===========================================", true);
            HttpHeaders headers = httpUtil.getHeaders();
            String path = "/consumer/data/contractnegotiations";
            // Get variables from configuration
            if (providerUrl == null) {
                provider = this.providerUrl;
            }
            if (this.serverUrl .equals("") || APIKey == null) {
                return null;
            }
            String url =  this.serverUrl  + path;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Object body = new NegotiationOffer(contractOffer.getConnectorId(), provider, contractOffer);
            ResponseEntity<?> response = httpUtil.doPost(url, JsonNode.class, headers, httpUtil.getParams(), body, false, false);
            JsonNode result = (JsonNode) response.getBody();
            return (Negotiation) jsonUtil.bindJsonNode(result, Negotiation.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doContractNegotiations",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }

    public Negotiation getNegotiation(String Id) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            String path = "/consumer/data/contractnegotiations";
            // Get variables from configuration
            if (this.serverUrl .equals("") || APIKey == null) {
                return null;
            }
            String url = this.serverUrl  + path + "/" + Id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Map<String, Object> params = httpUtil.getParams();
            JsonNode body = null;
            String actualState = "";
            boolean sw = true;
            Instant start = Instant.now();
            Instant end = start;
            LogUtil.printDebug("["+Id+"] ===== [STARTING CHECKING STATUS FOR CONTRACT NEGOTIATION]  ===========================================", true);
            while (sw) {
                ResponseEntity<?> response = httpUtil.doGet(url, JsonNode.class, headers, params, false, false);
                body = (JsonNode) response.getBody();
                if(body == null){
                    sw = false;
                    throw new ServiceException(this.getClass().getName() + "." + "getNegotiations",
                            "No response received from url [" + url + "]!");
                }
                if (!body.has("state") || body.get("state") == null) {
                    LogUtil.printDebug("["+Id+"] ===== [ERROR CONTRACT NEGOTIATION] ===========================================", true);
                    throw new ServiceException(this.getClass().getName() + "." + "getNegotiations",
                            "It was not possible to do contract negotiations!");
                }
                String state = body.get("state").asText();
                if (state.equals("CONFIRMED") || state.equals("ERROR")) {
                    sw = false;
                    LogUtil.printDebug("["+Id+"] ===== [FINISHED CONTRACT NEGOTIATION] ===========================================", true);
                }
                if (!state.equals(actualState)) {
                    actualState = state; // Update current state
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    LogUtil.printDebug("["+Id+"] The contract negotiation status changed: [" + state + "] - TIME->[" + timeElapsed + "]s", true);
                    start = Instant.now();
                }
            }
            return (Negotiation) jsonUtil.bindJsonNode(body, Negotiation.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getNegotiation",
                    e,
                    "It was not possible to retrieve the catalog!");
        }
    }


    public Transfer initiateTransfer(TransferRequest transferRequest) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            String path = "/consumer/data/transferprocess";
            // Get variables from configuration
            String url = this.serverUrl + path;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Object body = transferRequest;
            ResponseEntity<?> response = httpUtil.doPost(url, String.class, headers, httpUtil.getParams(), body, false, false);
            String responseBody = (String) response.getBody();
            return (Transfer) jsonUtil.bindJsonNode(jsonUtil.toJsonNode(responseBody), Transfer.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "doTransferProcess",
                    e,
                    "It was not possible to initiate transfer process!");
        }
    }

    public Transfer getTransfer(String Id) {
        try {
            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            String path = "/consumer/data/transferprocess";
            String url = this.serverUrl  + path + "/" + Id;
            headers.add("Content-Type", "application/json");
            headers.add("X-Api-Key", APIKey);
            Map<String, Object> params = httpUtil.getParams();
            JsonNode body =  null;
            String actualState = "";
            boolean sw = true;
            Instant start = Instant.now();
            Instant end = start;
            LogUtil.printDebug("["+Id+"] ===== [STARTING CONTRACT TRANSFER] ===========================================", true);
            while (sw) {
                ResponseEntity<?> response = httpUtil.doGet(url, JsonNode.class, headers, params, false, false);
                body = (JsonNode) response.getBody();
                if(body == null){
                    sw = false;
                    throw new ServiceException(this.getClass().getName() + "." + "getNegotiations",
                            "No response received from url [" + url + "]!");
                }
                if (!body.has("state") || body.get("state") == null) {
                    LogUtil.printDebug("["+Id+"] ===== [ERROR CONTRACT TRANSFER]===========================================", true);
                    throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                            "It was not possible to do the transfer process!");
                }
                String state = body.get("state").asText();
                if (state.equals("COMPLETED") || state.equals("ERROR")) {
                    LogUtil.printDebug("["+Id+"] ===== [FINISHED CONTRACT TRANSFER] ["+Id+"]===========================================", true);
                    sw = false;
                }
                if (!state.equals(actualState)) {
                    actualState = state; // Update current state
                    end = Instant.now();
                    Duration timeElapsed = Duration.between(start, end);
                    LogUtil.printDebug("["+Id+"] The data transfer status changed: [" + state + "] - TIME->[" + timeElapsed + "]s", true);
                    start = Instant.now();
                }
            }
            return (Transfer) jsonUtil.bindJsonNode(body, Transfer.class);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName() + "." + "getTransfer",
                    e,
                    "It was not possible to transfer the contract! " + Id);
        }
    }


    public PassportV3 getPassportV3(String transferProcessId) {
        try {
            this.checkEmptyVariables();
            String path = "/consumer_backend";
            String url = this.serverUrl  + path + "/" + transferProcessId;
            Map<String, Object> params = httpUtil.getParams();
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Accept", "application/octet-stream");
            boolean retry = false;
            ResponseEntity<?> response = null;
            try {
                response = httpUtil.doGet(url, String.class, headers, params, false, false);
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
