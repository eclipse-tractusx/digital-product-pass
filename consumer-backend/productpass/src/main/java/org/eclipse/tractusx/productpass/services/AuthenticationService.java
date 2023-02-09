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

package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.auth.UserInfo;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utils.ConfigUtil;
import utils.HttpUtil;
import utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationService extends BaseService {
    @Autowired
    private VaultService vaultService;

    public static final ConfigUtil configuration = new ConfigUtil();
    public final String userInfoUri = (String) configuration.getConfigurationParam("keycloak.userInfoUri", ".", null);
    public final String tokenUri = (String) configuration.getConfigurationParam("keycloak.tokenUri", ".", null);
    public final String realm = (String) configuration.getConfigurationParam("keycloak.realm", ".", null);
    public final String resource = (String) configuration.getConfigurationParam("keycloak.resource", ".", null);
    public String clientId;
    public String clientSecret;

    public AuthenticationService() throws ServiceInitializationException {
        this.checkEmptyVariables(List.of("clientId", "clientSecret"));
    }

    @Override
    public List<String> getEmptyVariables() {
        List<String> missingVariables = new ArrayList<>();
        if (tokenUri == null || tokenUri.isEmpty()) {
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
            HttpHeaders headers = HttpUtil.getHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded");
            Map<String,?> body = Map.of(
                    "grant_type", "client_credentials",
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "scope", "openid profile email"
            );

            String encodedBody = HttpUtil.mapToParams(body, false);
            ResponseEntity<?> response = HttpUtil.doPost(tokenUri, String.class, headers, HttpUtil.getParams(), encodedBody, false, false);
            String responseBody = (String) response.getBody();
            JsonNode json = JsonUtil.toJsonNode(responseBody);
            JwtToken token = (JwtToken) JsonUtil.bindJsonNode(json, JwtToken.class);
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

    public UserInfo getUserInfo(String jwtToken){
        try{
            HttpHeaders headers = HttpUtil.getHeadersWithToken(jwtToken);
            headers.add("Accept", "application/json");

            ResponseEntity<?> response = HttpUtil.doPost(userInfoUri, String.class, headers, HttpUtil.getParams(), false, false);
            String responseBody = (String) response.getBody();
            JsonNode json = JsonUtil.toJsonNode(responseBody);
            return (UserInfo) JsonUtil.bindJsonNode(json, UserInfo.class);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getUserInfo",
                    e,
                    "It was not possible to getUserInfo!");
        }
    }



}
