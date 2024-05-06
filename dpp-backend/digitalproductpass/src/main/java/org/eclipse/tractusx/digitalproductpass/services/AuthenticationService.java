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
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.digitalproductpass.config.SecurityConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.models.auth.JwtToken;
import org.eclipse.tractusx.digitalproductpass.models.auth.UserInfo;
import org.eclipse.tractusx.digitalproductpass.models.edc.Jwt;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
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

    /** ATTRIBUTES **/
    private final VaultService vaultService;
    private final Environment env;
    private final HttpUtil httpUtil;
    private final JsonUtil jsonUtil;

    private final SecurityConfig securityConfig;

    public String tokenUri;
    public String clientId;
    public String clientSecret;

    /** CONSTRUCTOR(S) **/
    @Autowired
    public AuthenticationService(VaultService vaultService, Environment env, HttpUtil httpUtil, JsonUtil jsonUtil, SecurityConfig securityConfig) throws ServiceInitializationException {
        this.vaultService = vaultService;
        this.env = env;
        this.httpUtil = httpUtil;
        this.jsonUtil = jsonUtil;
        this.securityConfig = securityConfig;
        this.init(env);
        this.checkEmptyVariables(List.of("clientId", "clientSecret"));
    }

    /** METHODS **/

    /**
     * Initiates the main needed variables for Authetication Service by loading from the environment variables.
     **/
    public void init(Environment env) {
        this.tokenUri = env.getProperty("configuration.keycloak.tokenUri", String.class,  "");
    }

    /**
     * Gets the authentication token for the technical user.
     * <p>
     *
     * @return  a {@code JwtToken} object with all token's authentication parameters.
     *
     * @throws  ServiceException
     *           if unable to retrieve the token.
     */
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

    /**
     * Checks if the user is authenticated.
     * <p>
     * @param   jwtToken
     *          the {@code JwtToken} object regarding the authentication token.
     *
     * @return  true if the user is authenticated, false otherwise.
     *
     */
    @SuppressWarnings("Unused")
    public Boolean isAuthenticated(String jwtToken){
        UserInfo userInfo = null;
        try {
            userInfo = this.getUserInfo(jwtToken);
        }catch (Exception e){
            return false;
        }
        return userInfo != null;
    }

    public Boolean isUserInfoAvailable(String token){
        UserInfo userInfo = null;
        try {
            userInfo = this.getUserInfo(token);
        }catch (Exception e){
            return false;
        }
        return userInfo != null;
    }

    public Boolean tokenContainsSameBpn(Jwt jwtToken){
        try {
            if (!jwtToken.getPayload().containsKey("bpn")) {
                return false;
            }

            return jwtToken.getPayload().get("bpn").equals(vaultService.getLocalSecret("edc.participantId"));
        }catch (Exception e){
            return false;
        }
    }
    /**
     * Checks if the user is authenticated.
     * <p>
     * @param   jwtToken
     *          the {@code Jwt} token from the request
     *
     * @return  true if the token contains any role in the client id, false otherwise.
     *
     */
    public Boolean tokenContainsAnyRole(Jwt jwtToken){
        try {
            if (!jwtToken.getPayload().containsKey("resource_access")) {
                return false;
            }
            String appId = (String) vaultService.getLocalSecret("appId");
            if(appId == null || appId.equals("")){
                return false;
            }
            Map<String, Object> resourceAccess = (Map<String, Object>) jsonUtil.toMap(jwtToken.getPayload().get("resource_access"));

            if (!resourceAccess.containsKey(appId)) {
                return false;
            }
            Map<String, Object> appIdResource = (Map<String, Object>) jsonUtil.toMap(resourceAccess.get(appId));

            if (!appIdResource.containsKey("roles")) {
                return false;
            }

            List<String> roles = (List<String>) appIdResource.get("roles");
            return roles.size() > 0;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Checks if the user is Api Key authenticated.
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} object representing the HTTP request.
     *
     * @return  true if the user is authenticated, false otherwise.
     *
     */
    public Boolean isApiKeyAuthenticated(HttpServletRequest httpRequest) {
        try {
            final String xApiKey = httpRequest.getHeader(securityConfig.getAuthentication().getHeader());
            String vaultApiKey = vaultService.getLocalSecret("oauth.apiKey").toString();
            if (vaultApiKey == null || vaultApiKey.isEmpty())
                throw new ServiceException(this.getClass().getName()+"."+"isApiKeyAuthenticated", "API key is null or empty!");
            return vaultApiKey.equals(xApiKey);
        } catch (Exception e) {
            throw new ServiceException(this.getClass().getName()+"."+"isApiKeyAuthenticated", e, "There was a error when checking for the api key authentication!");
        }
    }

    /**
     * Checks if the user is authenticated.
     * <p>
     * @param   httpRequest
     *          the {@code HttpServletRequest} object representing the HTTP request.
     *
     * @return  true if the user is authenticated, false otherwise.
     *
     */
    public Boolean isAuthenticated(HttpServletRequest httpRequest){
        String token = httpUtil.getAuthorizationToken(httpRequest);
        if(token == null){
            return false;
        }

        SecurityConfig.AuthorizationConfig authorizationConfig = this.securityConfig.getAuthorization();
        // In this block starts the Authorization configuration for all the Secure APIs

        Boolean bpnAuth = authorizationConfig.getBpnAuth();
        Boolean roleAuth = authorizationConfig.getRoleAuth();

        if(!bpnAuth && !roleAuth){
            return this.isUserInfoAvailable(token);
        }
        // Check the authorization based on the jwt token received
        Jwt jwtToken = httpUtil.parseToken(token);

        Boolean containsSameBpn = this.tokenContainsSameBpn(jwtToken);
        Boolean containsAnyRole = this.tokenContainsAnyRole(jwtToken);

        boolean authorized = false;

        // Cross the authentication following the configuration rules
        if((bpnAuth && roleAuth) && (containsSameBpn && containsAnyRole)){
            authorized = true;
        } else if ((!bpnAuth && roleAuth) && containsAnyRole) {
            authorized = true;
        } else if ((bpnAuth && !roleAuth) && containsSameBpn) {
            authorized = true;
        }

        // If authorized check if the user info is available
        if(authorized){
            return this.isUserInfoAvailable(token);
        }

        return false;
    }

    /**
     * Gets the user's information from the given token.
     * <p>
     * @param   jwtToken
     *          the {@code String} authentication token.
     *
     * @return  a {@code UserInfo} object with the user's information.
     *
     * @throws  ServiceException
     *           if unable to retrieve the user's information.
     */
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

    /**
     * Creates a List of missing variables needed to proceed with the authentication.
     * <p>
     *
     * @return an {@code Arraylist} with the environment variables missing in the configuration for the authentication.
     *
     */
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
}
