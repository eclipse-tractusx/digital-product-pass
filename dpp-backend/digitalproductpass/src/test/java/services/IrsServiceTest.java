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

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.config.SecurityConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.auth.JwtToken;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobRequest;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.services.IrsService;
import org.eclipse.tractusx.digitalproductpass.services.VaultService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import utils.FileUtil;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.YamlUtil;

import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IrsServiceTest {

    private IrsService irsService;
    private IrsConfig irsConfig;
    private final String testJobResposnsePath = "/dpp/irs/TestJobResponse.json";
    private final String testStartJobResposnsePath = "/dpp/irs/TestStartJobResponse.json";
    private Map<String, Object> configuration;
    private final String mockedTokenPath = "/dpp/token/MockedToken.json";
    private JwtToken mockedToken;
    @Mock
    private Environment env;
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private YamlUtil yamlUtil;
    private SecurityConfig securityConfig;
    private FileUtil fileUtil;
    private ProcessManager processManager;
    private TreeManager treeManager;
    private AuthenticationService authenticationService;
    @Mock
    private VaultService vaultService;
    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        yamlUtil = new YamlUtil(fileUtil);
        securityConfig = new SecurityConfig();
        securityConfig.setAuthorization(new SecurityConfig.AuthorizationConfig(false, false));
        securityConfig.setStartUpChecks(new SecurityConfig.StartUpCheckConfig(false, false));
        httpUtil = Mockito.spy(new HttpUtil(env));

        String configurationFilePath = Paths.get(fileUtil.getBaseClassDir(this.getClass()), "application-test.yml").toString();
        Map<String, Object> application = yamlUtil.readFile(configurationFilePath);
        configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));
        String mockClientId="xynasdnsda12312";
        String mockClientTestReturn="212123asddsad54546";
        String mockApiKey="aqweas1230sad";
        String mockBpn="BPNL00000000000";

        when(vaultService.getLocalSecret("client.id")).thenReturn(mockClientId);
        when(vaultService.getLocalSecret("client.secret")).thenReturn(mockClientTestReturn);

        when(vaultService.getLocalSecret("edc.apiKey")).thenReturn(mockApiKey);
        when(vaultService.getLocalSecret("edc.participantId")).thenReturn(mockBpn);


        env = initEnv();
        irsConfig = initIrsConfig();
        ProcessConfig processConfig = new ProcessConfig();
        PassportConfig passportConfig = new PassportConfig();
        authenticationService = Mockito.spy(new AuthenticationService(vaultService, env, httpUtil, jsonUtil, securityConfig));
        processManager = Mockito.spy(new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig));
        treeManager = new TreeManager(fileUtil, jsonUtil, processManager, irsConfig, passportConfig);

        irsService = new IrsService(env, processManager, irsConfig, treeManager, httpUtil, vaultService, jsonUtil, authenticationService);

        mockedToken = (JwtToken) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), mockedTokenPath).toString() , JwtToken.class);
        doReturn(mockedToken).when(authenticationService).getToken();
    }

    private IrsConfig initIrsConfig() {
        Map<String, Object> irs = (Map<String, Object>) jsonUtil.toMap(configuration.get("irs"));

        IrsConfig irsConfig = new IrsConfig();
        irsConfig.setEnabled((Boolean) irs.get("enabled"));
        irsConfig.setEndpoint(irs.get("endpoint").toString());
        irsConfig.setPaths((IrsConfig.Paths) jsonUtil.bindObject(irs.get("paths"), IrsConfig.Paths.class));
        irsConfig.setTree((IrsConfig.TreeConfig) jsonUtil.bindObject(irs.get("tree"), IrsConfig.TreeConfig.class));
        irsConfig.setCallbackUrl(irs.get("callbackUrl").toString());

        return irsConfig;
    }

    private MockEnvironment initEnv() {
        MockEnvironment env = new MockEnvironment();

        Map<String, Object> keycloak = (Map<String, Object>) jsonUtil.toMap(configuration.get("keycloak"));
        env.setProperty("configuration.keycloak.tokenUri", keycloak.get("tokenUri").toString());
        env.setProperty("configuration.keycloak.userInfoUri", keycloak.get("userInfoUri").toString());

        return env;
    }

    @BeforeEach
    void setUp() {
        doReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testStartJobResposnsePath).toString() , JsonNode.class), HttpStatus.OK))
                .when(httpUtil).doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(JobRequest.class), eq(false), eq(false));

        doReturn(new ResponseEntity<>(jsonUtil.toJson(jsonUtil.fromJsonFile(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testJobResposnsePath).toString() ), true), HttpStatus.OK))
                .when(httpUtil).doGet(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), eq(true), eq(false));

        doReturn("TEST_PATH").when(processManager).setJobHistory(anyString(), any(JobHistory.class));
    }

    @Test
    void startJob() {
        String processId = UUID.randomUUID().toString();
        String globalAssetId = UUID.randomUUID().toString();
        String searchId = UUID.randomUUID().toString();
        String bpn = UUID.randomUUID().toString();

        Map<String, String> result = irsService.startJob(processId, globalAssetId, searchId, bpn);

        assertNotNull(result);
        assertEquals("0bbc712b-17a1-4c9d-9c9c-a7fae8082841", result.get("id"));
    }

    @Test
    void getChildren() {
        String expectedJobId = "0bbc712b-17a1-4c9d-9c9c-a7fae8082841";
        String processId = UUID.randomUUID().toString();
        String path = "test.path";
        String globalAssetId = UUID.randomUUID().toString();
        String bpn = UUID.randomUUID().toString();

        String jobId = irsService.getChildren(processId, path, globalAssetId, bpn);

        assertEquals(expectedJobId, jobId);
    }

    @Test
    void getJob() {
        JobResponse test = (JobResponse) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testJobResposnsePath).toString() , JobResponse.class);
        String jobId = "0bbc712b-17a1-4c9d-9c9c-a7fae8082841";
        JobResponse jobResponse = irsService.getJob(jobId);

        assertNotNull(jobResponse);
        assertEquals(test.getJob().getId(), jobResponse.getJob().getId());
        assertEquals(test.getJob().getGlobalAssetId(), jobResponse.getJob().getGlobalAssetId());
        assertEquals(test.getJob().getState(), jobResponse.getJob().getState());
        assertEquals(jobId, jobResponse.getJob().getId());
    }


}
