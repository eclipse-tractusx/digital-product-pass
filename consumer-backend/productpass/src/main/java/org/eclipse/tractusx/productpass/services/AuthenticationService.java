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
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.auth.UserInfo;
import org.eclipse.tractusx.productpass.models.edc.Jwt;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService extends BaseService {
    private final VaultService vaultService;
    private final Environment env;

    private final HttpUtil httpUtil;

    private final JsonUtil jsonUtil;

    public String tokenUri;
    public String clientId;
    public String clientSecret;

    @Autowired
    public AuthenticationService(VaultService vaultService, Environment env, HttpUtil httpUtil, JsonUtil jsonUtil) throws ServiceInitializationException {
        this.vaultService = vaultService;
        this.env = env;
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.init(env);
        this.checkEmptyVariables(List.of("clientId", "clientSecret"));
    }
    public void init(Environment env) {
        this.tokenUri = env.getProperty("configuration.keycloak.tokenUri", String.class,  "");
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (this.tokenUri.isEmpty()) {
            missingVariables.add("tokenUri");
        }
        if (clientId == null || clientId.isEmpty()) {
            missingVariables.add("clientId");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            missingVariables.add("clientSecret");
        }
        return missingVariables;
    }

    public JwtToken getToken(){
        try{
            this.clientId = (String) vaultService.getLocalSecret("client.id");
            this.clientSecret = (String) vaultService.getLocalSecret("client.secret");

            this.checkEmptyVariables();
            HttpHeaders headers = httpUtil.getHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            Map<String,?> body = Map.of(
                    "grant_type", "client_credentials",
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "scope", "openid profile email"
            );

            String encodedBody = httpUtil.mapToParams(body, false);
            ResponseEntity<?> response = httpUtil.doPost(this.tokenUri, String.class, headers, httpUtil.getParams(), encodedBody, false, false);
            String responseBody = (String) response.getBody();
            JsonNode json = jsonUtil.toJsonNode(responseBody);
            JwtToken token = (JwtToken) jsonUtil.bindJsonNode(json, JwtToken.class);
            return token;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getToken",
                    e,
                    "It was not possible to retrieve the token!");
        }
    }

    public Boolean isAuthenticated(String jwtToken){
        UserInfo userInfo = null;
        try {
            userInfo = this.getUserInfo(jwtToken);
        }catch (Exception e){
            return false;
        }
        return userInfo != null;
    }
    public Boolean isAuthenticated(HttpServletRequest httpRequest){
        String token = httpUtil.getAuthorizationToken(httpRequest);
        if(token == null){
            return false;
        }
        /*
        Jwt jwtToken = httpUtil.parseToken(token);
        // If the end user has no bpn available block
        if(jwtToken.getPayload().containsKey("bpn")){
            return false;
        }

        if(jwtToken.getPayload().get("bpn") != vaultService.getLocalSecret("edc.participantId")){
            return false;
        }
        */

        UserInfo userInfo = null;
        try {
            userInfo = this.getUserInfo(token);
        }catch (Exception e){
            return false;
        }
        return userInfo != null;
    }

    public UserInfo getUserInfo(String jwtToken){
        try{
            HttpHeaders headers = httpUtil.getHeadersWithToken(jwtToken);
            headers.add("Accept", "application/json");
            String userInfoUri= env.getProperty("configuration.keycloak.userInfoUri", String.class, "");
            ResponseEntity<?> response = httpUtil.doPost(userInfoUri, String.class, headers, httpUtil.getParams(), false, false);
            String responseBody = (String) response.getBody();
            JsonNode json = jsonUtil.toJsonNode(responseBody);
            return (UserInfo) jsonUtil.bindJsonNode(json, UserInfo.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getUserInfo",
                    e,
                    "It was not possible to getUserInfo!");
        }
    }



}
