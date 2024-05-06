/*********************************************************************************
 *
 * Tractus-X - Digital Product Pass Application
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

package services;

import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.digitalproductpass.config.SecurityConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.models.auth.JwtToken;
import org.eclipse.tractusx.digitalproductpass.models.auth.UserInfo;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.services.VaultService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import utils.FileUtil;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.YamlUtil;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private final String mockedTokenPath = "/dpp/token/MockedToken.json";
    private final String mockedUserInfoPath = "/dpp/token/MockedUserInfo.json";
    private JwtToken mockedToken;
    private UserInfo mockedUserInfo;
    @Mock
    private VaultService vaultService;
    @Mock
    private Environment env;
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private YamlUtil yamlUtil;
    private FileUtil fileUtil;
    private SecurityConfig securityConfig;
    @Mock
    private HttpServletRequest httpRequest;

    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);
        httpUtil = new HttpUtil(env);
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        yamlUtil = new YamlUtil(fileUtil);
        securityConfig = new SecurityConfig();
        securityConfig.setAuthorization(new SecurityConfig.AuthorizationConfig(false, false));
        securityConfig.setStartUpChecks(new SecurityConfig.StartUpCheckConfig(false, false));
        String configurationFilePath = Paths.get(fileUtil.getBaseClassDir(this.getClass()), "application-test.yml").toString();
        Map<String, Object> application = yamlUtil.readFile(configurationFilePath);
        Map<String, Object> configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));
        Map<String, Object> keycloak = (Map<String, Object>) jsonUtil.toMap(configuration.get("keycloak"));
        when(env.getProperty("configuration.keycloak.tokenUri", String.class,  "")).thenReturn(keycloak.get("tokenUri").toString());
        when(env.getProperty("configuration.keycloak.userInfoUri", String.class, "")).thenReturn(keycloak.get("userInfoUri").toString());
        authenticationService = Mockito.spy(new AuthenticationService(vaultService, env, httpUtil, jsonUtil, securityConfig));

        mockedToken = (JwtToken) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), mockedTokenPath).toString(), JwtToken.class);
        mockedUserInfo = (UserInfo) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), mockedUserInfoPath).toString(), UserInfo.class);
        doReturn(mockedToken).when(authenticationService).getToken();
        doReturn(mockedUserInfo).when(authenticationService).getUserInfo(mockedToken.getAccessToken());
    }

    @Test
    void getToken() {

        JwtToken jwtToken = authenticationService.getToken();

        assertNotNull(jwtToken);
        assertNotNull(jwtToken.getAccessToken());
        assertFalse(jwtToken.getAccessToken().isEmpty());
    }

    @Test
    void isAuthenticated() {
        JwtToken jwtToken = authenticationService.getToken();

        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + jwtToken.getAccessToken());

        assertTrue(authenticationService.isAuthenticated(httpRequest));
    }

    @Test
    void getUserInfo() {
        JwtToken jwtToken = authenticationService.getToken();

        UserInfo userInfo = authenticationService.getUserInfo(jwtToken.getAccessToken());

        assertNotNull(userInfo);
    }
}
