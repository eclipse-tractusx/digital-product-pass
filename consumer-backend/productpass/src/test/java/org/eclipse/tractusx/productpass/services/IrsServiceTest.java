package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.config.PassportConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.managers.TreeManager;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.irs.JobHistory;
import org.eclipse.tractusx.productpass.models.irs.JobRequest;
import org.eclipse.tractusx.productpass.models.irs.JobResponse;
import org.junit.jupiter.api.*;
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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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
    private final String testJobResposnsePath = "/src/test/resources/dpp/irs/TestJobResponse.json";
    private final String testStartJobResposnsePath = "/src/test/resources/dpp/irs/TestStartJobResponse.json";
    private Map<String, Object> configuration;
    private final String mockedTokenPath = "/src/test/resources/dpp/token/MockedToken.json";
    private JwtToken mockedToken;
    @Mock
    private Environment env;
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private YamlUtil yamlUtil;
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
        httpUtil = Mockito.spy(new HttpUtil(env));

        Map<String, Object> application = yamlUtil.readFile(FileUtil.getWorkdirPath() + "/src/main/resources/application.yml");
        configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));
        Map<String, Object> vaultConfig = yamlUtil.readFile(fileUtil.getDataDir() + "/VaultConfig/vault.token.yml");
        Map<String, Object> client = (Map<String, Object>) jsonUtil.toMap(vaultConfig.get("client"));
        when(vaultService.getLocalSecret("client.id")).thenReturn(client.get("id").toString());
        when(vaultService.getLocalSecret("client.secret")).thenReturn(client.get("secret").toString());


        env = initEnv();
        irsConfig = initIrsConfig();
        ProcessConfig processConfig = new ProcessConfig();
        PassportConfig passportConfig = new PassportConfig();
        authenticationService = Mockito.spy(new AuthenticationService(vaultService, env, httpUtil, jsonUtil));
        processManager = Mockito.spy(new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig));
        treeManager = new TreeManager(fileUtil, jsonUtil, processManager, irsConfig, passportConfig);

        irsService = new IrsService(env, processManager, irsConfig, treeManager, httpUtil, vaultService, jsonUtil, authenticationService);

        mockedToken = (JwtToken) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + mockedTokenPath, JwtToken.class);
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
        doReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testStartJobResposnsePath, JsonNode.class), HttpStatus.OK))
                .when(httpUtil).doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(JobRequest.class), eq(false), eq(false));

        doReturn(new ResponseEntity<>(jsonUtil.toJson(jsonUtil.fromJsonFile(FileUtil.getWorkdirPath() + testJobResposnsePath), true), HttpStatus.OK))
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
        JobResponse test = (JobResponse) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testJobResposnsePath, JobResponse.class);
        String jobId = "0bbc712b-17a1-4c9d-9c9c-a7fae8082841";
        JobResponse jobResponse = irsService.getJob(jobId);

        assertNotNull(jobResponse);
        assertEquals(test.getJob().getId(), jobResponse.getJob().getId());
        assertEquals(test.getJob().getGlobalAssetId(), jobResponse.getJob().getGlobalAssetId());
        assertEquals(test.getJob().getState(), jobResponse.getJob().getState());
        assertEquals(jobId, jobResponse.getJob().getId());
    }


}
