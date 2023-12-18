package org.eclipse.tractusx.productpass.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mocks.MockedHttpSession;
import org.bouncycastle.asn1.x509.GeneralName;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.config.PassportConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.DtrSearchManager;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
import org.eclipse.tractusx.productpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.productpass.models.dtregistry.SubModel;
import org.eclipse.tractusx.productpass.models.edc.AssetSearch;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.manager.History;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import utils.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AasServiceTest {

    private AasService aasService;
    private Map<String, Object> configuration;
    String mockedDigitalTwin;
    private final String digitalTwinId = "365e6fbe-bb34-11ec-8422-0242ac120002";
    private final String semanticId = "urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass";
    private final String testDigitalTwinPath = "/src/test/resources/dpp/digitaltwins/TestDigitalTwin.json";
    private String baseDataDirPath;
    private String testProcessId;
    @Mock
    private Environment env;
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private FileUtil fileUtil;
    private YamlUtil yamlUtil;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession mockedHttpSession;
    private AuthenticationService authenticationService;
    private final String mockedTokenPath = "/src/test/resources/dpp/token/MockedToken.json";
    private JwtToken mockedToken;
    @Mock
    private VaultService vaultService;
    private DtrConfig dtrConfig;
    private ProcessConfig processConfig;
    private PassportConfig passportConfig;
    private DtrSearchManager dtrSearchManager;
    private ProcessManager processManager;
    private DataTransferService dataTransferService;
    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        yamlUtil = new YamlUtil(fileUtil);

        Map<String, Object> application = yamlUtil.readFile(FileUtil.getWorkdirPath() + "/src/main/resources/application.yml");
        configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));

        env = initEnv();
        httpUtil = Mockito.spy(new HttpUtil(env));


        Map<String, Object> vaultConfig = yamlUtil.readFile(fileUtil.getDataDir() + "/VaultConfig/vault.token.yml");
        Map<String, Object> client = (Map<String, Object>) jsonUtil.toMap(vaultConfig.get("client"));
        when(vaultService.getLocalSecret("client.id")).thenReturn(client.get("id").toString());
        when(vaultService.getLocalSecret("client.secret")).thenReturn(client.get("secret").toString());
        Map<String, Object> edc = (Map<String, Object>) jsonUtil.toMap(vaultConfig.get("edc"));
        when(vaultService.getLocalSecret("edc.apiKey")).thenReturn(edc.get("apiKey").toString());
        when(vaultService.getLocalSecret("edc.participantId")).thenReturn(edc.get("participantId").toString());

        dtrConfig = initDtrConfig();
        processConfig = new ProcessConfig();
        processConfig.setDir("process");
        baseDataDirPath = Path.of(fileUtil.getDataDir(), processConfig.getDir()).toString();
        processManager = new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig);
        dtrSearchManager = new DtrSearchManager(fileUtil,jsonUtil, dataTransferService, dtrConfig, processManager);
        dataTransferService = new DataTransferService(env, httpUtil, jsonUtil,vaultService, processManager, dtrConfig);
        authenticationService = Mockito.spy(new AuthenticationService(vaultService, env, httpUtil, jsonUtil));
        mockedToken = (JwtToken) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + mockedTokenPath, JwtToken.class);
        doReturn(mockedToken).when(authenticationService).getToken();

        passportConfig = new PassportConfig();

        aasService = new AasService(env, httpUtil, jsonUtil, authenticationService, dtrConfig, dtrSearchManager, processManager, dataTransferService, passportConfig);

        mockedDigitalTwin = jsonUtil.toJson(jsonUtil.fromJsonFile(FileUtil.getWorkdirPath() + testDigitalTwinPath), true);
        testProcessId = processManager.initProcess();

        mockedHttpSession = new MockedHttpSession();

        doReturn(new ResponseEntity<>(new ArrayList(){{add(digitalTwinId);}}, HttpStatus.OK))
                .when(httpUtil).doGet(anyString(), eq(ArrayList.class), any(HttpHeaders.class), any(Map.class), eq(true), eq(false));

        doReturn(new ResponseEntity<>(mockedDigitalTwin, HttpStatus.OK))
                .when(httpUtil).doGet(anyString(), eq(String.class), any(HttpHeaders.class), any(Map.class), eq(true), eq(false));
    }

    @AfterAll
    void tearDownAll() {
        String dataDirPath = Path.of(baseDataDirPath, testProcessId).toString();
        try {
            //Delete testProcessId dir from tmp directory
            processManager.deleteSearchDir(testProcessId);
        } catch (Exception e) {
            //Delete testProcessId dir from data directory
            if (fileUtil.pathExists(dataDirPath))
                fileUtil.deleteDir(dataDirPath);
        }
        //Delete testProcessId dir from data directory
        fileUtil.deleteDir(dataDirPath);
    }

    private DtrConfig initDtrConfig() {
        DtrConfig dtrConfig = new DtrConfig();
        DtrConfig.Timeouts timeouts = new DtrConfig.Timeouts();
        timeouts.setSearch(10);
        timeouts.setNegotiation(40);
        timeouts.setTransfer(10);
        timeouts.setDigitalTwin(20);
        dtrConfig.setTimeouts(timeouts);
        dtrConfig.setTemporaryStorage(new DtrConfig.TemporaryStorage(true, 12));

        Map<String, Object> dtr = (Map<String, Object>) jsonUtil.toMap(configuration.get("dtr"));
        Map<String, Object> decentralApis = (Map<String, Object>) jsonUtil.toMap(dtr.get("decentralApis"));
        DtrConfig.DecentralApis decentralApisObj = new DtrConfig.DecentralApis();
        decentralApisObj.setSearch(decentralApis.get("search").toString());
        decentralApisObj.setDigitalTwin(decentralApis.get("digitalTwin").toString());
        decentralApisObj.setSubModel(decentralApis.get("subModel").toString());
        dtrConfig.setDecentralApis(decentralApisObj);

        dtrConfig.setSemanticIdTypeKey(dtr.get("semanticIdTypeKey").toString());

        return dtrConfig;
    }

    private MockEnvironment initEnv() {
        MockEnvironment env = new MockEnvironment();
        Map<String, Object> edc = (Map<String, Object>) jsonUtil.toMap(configuration.get("edc"));
        env.setProperty("configuration.edc.endpoint", edc.get("endpoint").toString());
        env.setProperty("configuration.edc.catalog", edc.get("catalog").toString());
        env.setProperty("configuration.edc.management", edc.get("management").toString());
        env.setProperty("configuration.edc.negotiation", edc.get("negotiation").toString());
        env.setProperty("configuration.edc.transfer", edc.get("transfer").toString());

        Map<String, Object> keycloak = (Map<String, Object>) jsonUtil.toMap(configuration.get("keycloak"));
        env.setProperty("configuration.keycloak.tokenUri", keycloak.get("tokenUri").toString());
        env.setProperty("configuration.keycloak.userInfoUri", keycloak.get("userInfoUri").toString());

        return env;
    }

    @Test
    void searchAndGetDigitalTwin() {
        DigitalTwin mockedDigitalTwinObj = (DigitalTwin) jsonUtil.bindObject(jsonUtil.toJsonNode(mockedDigitalTwin), DigitalTwin.class);
        String assetType = "DigitalTwin";
        String assetId = digitalTwinId;
        int position = 0;

        DigitalTwin digitalTwin = aasService.searchDigitalTwin(assetType, assetId, position, null, null);

        assertNotNull(digitalTwin);
        assertEquals(digitalTwinId, digitalTwin.getIdentification());
        assertEquals(mockedDigitalTwinObj.getIdShort(), digitalTwin.getIdShort());
        assertEquals(mockedDigitalTwinObj.getGlobalAssetId(), digitalTwin.getGlobalAssetId());
    }

    @Test
    void getPathEndpoint() {
        String pathSearch = aasService.getPathEndpoint("search");
        assertEquals(dtrConfig.getDecentralApis().getSearch(), pathSearch);

        String pathDigitalTwin = aasService.getPathEndpoint("digitalTwin");
        assertEquals(dtrConfig.getDecentralApis().getDigitalTwin(), pathDigitalTwin);

        String pathSubmodel = aasService.getPathEndpoint("subModel");
        assertEquals(dtrConfig.getDecentralApis().getSubModel(), pathSubmodel);
    }

    @Test
    void searchAndGetSubModelBySemanticId() {
        DigitalTwin mockedDigitalTwinObj = (DigitalTwin) jsonUtil.bindObject(jsonUtil.toJsonNode(mockedDigitalTwin), DigitalTwin.class);

        SubModel submodel = aasService.searchSubModelBySemanticId(mockedDigitalTwinObj, semanticId);

        assertNotNull(submodel);
        assertEquals(semanticId, submodel.getSemanticId().getKeys().get(0).getValue());
        assertEquals(dtrConfig.getSemanticIdTypeKey(), submodel.getSemanticId().getKeys().get(0).getType());
        assertEquals(mockedDigitalTwinObj.getSubmodelDescriptors().get(0).getIdentification(), submodel.getIdentification());
    }


    @Test
    void getTokenHeader() {
        HttpHeaders headers = aasService.getTokenHeader(null);

        assertNotNull(headers);
        assertEquals(1, headers.get("Authorization").size());
        assertTrue(headers.get("Authorization").get(0).contains("Bearer"));
    }


    @Test
    void decentralDtrSearch() {
        Search searchBody = new Search();
        searchBody.setProcessId(testProcessId);
        searchBody.setId("IMR18650V1");

        String historyId = "digital-twin-found";
        String historyStatus = "TEST";
        String endpoint = "test.endpoint";


        when(request.getSession()).thenReturn(mockedHttpSession);

        processManager.createProcess(testProcessId, request);
        processManager.setEndpoint(testProcessId, endpoint);
        processManager.setStatus(testProcessId, historyId, new History(testProcessId, historyStatus));
        AssetSearch result = aasService.decentralDtrSearch(testProcessId, searchBody);

        assertNotNull(result);
        assertEquals(testProcessId, result.getAssetId());
        assertEquals(endpoint, result.getConnectorAddress());
    }

}
