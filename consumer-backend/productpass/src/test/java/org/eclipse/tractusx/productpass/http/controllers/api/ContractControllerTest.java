package org.eclipse.tractusx.productpass.http.controllers.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tractusx.productpass.config.DiscoveryConfig;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.config.PassportConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.DtrSearchManager;
import org.eclipse.tractusx.productpass.managers.ProcessDataModel;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.productpass.models.http.Response;
import org.eclipse.tractusx.productpass.models.http.requests.DiscoverySearch;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.models.http.requests.TokenRequest;
import org.eclipse.tractusx.productpass.models.manager.History;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.services.*;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.util.ReflectionTestUtils;
import utils.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContractControllerTest {
    @Mock
    private HttpServletRequest httpRequest;
    @Mock
    private HttpServletResponse httpResponse;
    private DataTransferService dataService;
    @Mock
    private VaultService vaultService;
    private AasService aasService;
    private AuthenticationService authService;
    private PassportConfig passportConfig;
    private DiscoveryConfig discoveryConfig;
    private DtrConfig dtrConfig;
    @Mock
    private Environment env;
    ProcessManager processManager;
    DtrSearchManager dtrSearchManager;
    private ProcessConfig processConfig;
    CatenaXService catenaXService;
    HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private FileUtil fileUtil;
    private YamlUtil yamlUtil;
    private ContractController contractController;
    private Map<String, Object> configuration;

    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);

        fileUtil = new FileUtil();
        yamlUtil = new YamlUtil(fileUtil);
        jsonUtil = new JsonUtil(fileUtil);

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

        Map<String, Object> passport = (Map<String, Object>) jsonUtil.toMap(configuration.get("passport"));
        passportConfig = new PassportConfig();
        passportConfig.setSearchIdSchema(passport.get("searchIdSchema").toString());
        dtrConfig = initDtrConfig();
        discoveryConfig = initDiscoveryConfig();
        processConfig = new ProcessConfig();
        processConfig.setDir("process");


        dataService = new DataTransferService(env, httpUtil, jsonUtil,vaultService, processManager, dtrConfig);
        authService = new AuthenticationService(vaultService, env, httpUtil, jsonUtil);
        catenaXService = Mockito.spy(new CatenaXService(env, fileUtil, httpUtil, jsonUtil, vaultService, dtrSearchManager, authService, discoveryConfig, dataService, dtrConfig));
        processManager = Mockito.spy(new ProcessManager(httpUtil,jsonUtil, fileUtil, processConfig));
        dtrSearchManager = new DtrSearchManager(fileUtil,jsonUtil, dataService, dtrConfig, processManager);
        aasService = new AasService(env, httpUtil, jsonUtil, authService, dtrConfig, dtrSearchManager, processManager, dataService, passportConfig);

        contractController = initController();

    }

    private ContractController initController() {
        ContractController controller = new ContractController();
        ReflectionTestUtils.setField(controller, "httpRequest", httpRequest);
        ReflectionTestUtils.setField(controller, "httpResponse", httpResponse);
        ReflectionTestUtils.setField(controller, "dataService", dataService);
        ReflectionTestUtils.setField(controller, "vaultService", vaultService);
        ReflectionTestUtils.setField(controller, "aasService", aasService);
        ReflectionTestUtils.setField(controller, "authService", authService);
        ReflectionTestUtils.setField(controller, "passportConfig", passportConfig);
        ReflectionTestUtils.setField(controller, "discoveryConfig", discoveryConfig);
        ReflectionTestUtils.setField(controller, "dtrConfig", dtrConfig);
        ReflectionTestUtils.setField(controller, "env", env);
        ReflectionTestUtils.setField(controller, "processManager", processManager);
        ReflectionTestUtils.setField(controller, "dtrSearchManager", dtrSearchManager);
        ReflectionTestUtils.setField(controller, "processConfig", processConfig);
        ReflectionTestUtils.setField(controller, "catenaXService", catenaXService);
        ReflectionTestUtils.setField(controller, "httpUtil", httpUtil);
        ReflectionTestUtils.setField(controller, "jsonUtil", jsonUtil);


        return  controller;
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

    private DiscoveryConfig initDiscoveryConfig() {
        DiscoveryConfig discoveryConfig = new DiscoveryConfig();
        Map<String, Object> discovery = (Map<String, Object>) jsonUtil.toMap(configuration.get("discovery"));
        discoveryConfig.setEndpoint(discovery.get("endpoint").toString());
        discoveryConfig.setEdc((DiscoveryConfig.EDCConfig) jsonUtil.bindObject(discovery.get("edc"), DiscoveryConfig.EDCConfig.class));
        discoveryConfig.setBpn((DiscoveryConfig.BPNConfig) jsonUtil.bindObject(discovery.get("bpn"), DiscoveryConfig.BPNConfig.class));
        return discoveryConfig;
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
    @Order(1)
    void createNotAuthorizedResponse() {
        Response response = contractController.create(null);

        assertEquals(401, response.getStatus());
        assertEquals("Not Authorized", response.getStatusText());
    }
    @Test
    void createBadRequestMissingMandatoryParametersResponse() {
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Response response = contractController.create(new DiscoverySearch());

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("One or all the mandatory parameters [id] are missing", response.getMessage());
    }

    @Test
    void createNotFoundBpnNumberResponse() {

        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        DiscoverySearch discoverySearch = new DiscoverySearch();
        discoverySearch.setId("XYZ78901");
        Response response = contractController.create(discoverySearch);

        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getStatusText());
        assertEquals("Failed to get the bpn number from the discovery service", response.getMessage());

    }

    @Test
    void createInternalServerErrorCantStartProcess() {

        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        BpnDiscovery.BpnEndpoint bpnDiscoveryEndpoint = new BpnDiscovery.BpnEndpoint();
        bpnDiscoveryEndpoint.setKey("bpn");
        bpnDiscoveryEndpoint.setResourceId(UUID.randomUUID().toString());
        bpnDiscoveryEndpoint.setType("TEST");
        bpnDiscoveryEndpoint.setValue("BPN00000");
        BpnDiscovery bpnDiscovery = new BpnDiscovery(new ArrayList<>(){{add(bpnDiscoveryEndpoint);}});

        doReturn(List.of(bpnDiscovery)).when(catenaXService).getBpnDiscovery(anyString(), anyString());


        DiscoverySearch discoverySearch = new DiscoverySearch();
        discoverySearch.setId("XYZ78901");
        Response response = contractController.create(discoverySearch);

        LogUtil.printMessage(jsonUtil.toJson(response, true));

        assertEquals(500, response.getStatus());
        assertEquals("Internal Server Error", response.getStatusText());
        assertEquals("It was not possible to create the process and search for the decentral digital twin registries", response.getMessage());

    }

    @Test
    @Order(2)
    void searchNotAuthorizeResponse() {
        Response response = contractController.search(null);

        assertEquals(401, response.getStatus());
        assertEquals("Not Authorized", response.getStatusText());
    }

    @Test
    void searchBadRequestMissingMandatoryParametersResponse() {
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Response response = contractController.search(new Search());

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("One or all the mandatory parameters [id] are missing", response.getMessage());
    }

    @Test
    void searchBadRequestProcessIdNullResponse() {

        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Search search = new Search();
        search.setId("XYZ78901");
        Response response = contractController.search(search);

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("No processId was found on the request body!", response.getMessage());

    }

    @Test
    void searchBadRequestProcessIdEmptyResponse() {

        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Search search = new Search();
        search.setId("XYZ78901");
        search.setProcessId("");
        Response response = contractController.search(search);

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("Process id is required for decentral digital twin registry searches!", response.getMessage());

    }

    @Test
    void searchBadRequestSearchStatusDoesNotExistResponse() {

        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Search search = new Search();
        search.setId("XYZ78901");
        search.setProcessId(UUID.randomUUID().toString());
        Response response = contractController.search(search);

        LogUtil.printMessage(jsonUtil.toJson(response, true));

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("The searchStatus id does not exists!", response.getMessage());

    }

    @Test
    @Order(3)
    void statusNotAuthorizeResponse() {
        Response response = contractController.status(null);

        assertEquals(401, response.getStatus());
        assertEquals("Not Authorized", response.getStatusText());
    }

    @Test
    void statusBadRequestProcessIdDoesNotExistResponse() {

        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);
        doReturn(new ProcessDataModel()).when(processManager).loadDataModel(httpRequest);

        Response response = contractController.status(UUID.randomUUID().toString());

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("The process id does not exists!", response.getMessage());
    }

    @Test
    @Order(4)
    void cancelNotAuthorizeResponse() {
        Response response = contractController.cancel(null);

        assertEquals(401, response.getStatus());
        assertEquals("Not Authorized", response.getStatusText());
    }

    @Test
    void cancelBadRequestMissingMandatoryParametersResponse() {
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Response response = contractController.cancel(new TokenRequest());

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("One or all the mandatory parameters [processId, contractId, token] are missing", response.getMessage());
    }

    @Test
    void cancelForbiddenContractDeclineResponse() {
        String statusStr = "contract-decline";
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Status status = new Status();
        status.setHistory(new HashMap<>());
        status.setHistory(statusStr, new History("id", statusStr, DateTimeUtil.getTimestamp()));
        doReturn(true).when(processManager).checkProcess(eq(httpRequest), anyString());
        doReturn(new Process()).when(processManager).getProcess(eq(httpRequest), anyString());
        doReturn(status).when(processManager).getStatus(anyString());

        TokenRequest requestToken = new TokenRequest();
        requestToken.setContractId(UUID.randomUUID().toString());
        requestToken.setProcessId(UUID.randomUUID().toString());
        requestToken.setToken(UUID.randomUUID().toString());

        Response response = contractController.cancel(requestToken);

        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getStatusText());
        assertEquals("This contract was declined! Please request a new one", response.getMessage());
    }

    @Test
    void cancelForbiddenNegotiationCanceledResponse() {
        String statusStr = "negotiation-canceled";
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Status status = new Status();
        status.setHistory(new HashMap<>());
        status.setHistory(statusStr, new History("id", statusStr, DateTimeUtil.getTimestamp()));
        doReturn(true).when(processManager).checkProcess(eq(httpRequest), anyString());
        doReturn(new Process()).when(processManager).getProcess(eq(httpRequest), anyString());
        doReturn(status).when(processManager).getStatus(anyString());

        TokenRequest requestToken = new TokenRequest();
        requestToken.setContractId(UUID.randomUUID().toString());
        requestToken.setProcessId(UUID.randomUUID().toString());
        requestToken.setToken(UUID.randomUUID().toString());

        Response response = contractController.cancel(requestToken);

        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getStatusText());
        assertEquals("This negotiation has already been canceled! Please request a new one", response.getMessage());
    }

    @Test
    void cancelBadRequestNoContractResponse() {
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Status status = new Status();
        status.setHistory(new HashMap<>());
        doReturn(true).when(processManager).checkProcess(eq(httpRequest), anyString());
        doReturn(new Process()).when(processManager).getProcess(eq(httpRequest), anyString());
        doReturn(status).when(processManager).getStatus(anyString());

        TokenRequest requestToken = new TokenRequest();
        requestToken.setContractId(UUID.randomUUID().toString());
        requestToken.setProcessId(UUID.randomUUID().toString());
        requestToken.setToken(UUID.randomUUID().toString());

        Response response = contractController.cancel(requestToken);

        LogUtil.printMessage(jsonUtil.toJson(response, true));

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("No contract is available!", response.getMessage());
    }


    @Test
    @Order(5)
    void signNotAuthorizeResponse() {
        Response response = contractController.sign(null);

        assertEquals(401, response.getStatus());
        assertEquals("Not Authorized", response.getStatusText());
    }

    @Test
    void signBadRequestMissingMandatoryParametersResponse() {
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Response response = contractController.sign(new TokenRequest());

        assertEquals(400, response.getStatus());
        assertEquals("Bad Request", response.getStatusText());
        assertEquals("One or all the mandatory parameters [processId, contractId, token] are missing", response.getMessage());
    }

    @Test
    void cancelForbiddenContractSignedResponse() {
        String statusStr = "contract-signed";
        String token = authService.getToken().getAccessToken();
        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        Status status = new Status();
        status.setHistory(new HashMap<>());
        status.setHistory(statusStr, new History("id", statusStr, DateTimeUtil.getTimestamp()));
        doReturn(true).when(processManager).checkProcess(eq(httpRequest), anyString());
        doReturn(new Process()).when(processManager).getProcess(eq(httpRequest), anyString());
        doReturn(status).when(processManager).getStatus(anyString());

        TokenRequest requestToken = new TokenRequest();
        requestToken.setContractId(UUID.randomUUID().toString());
        requestToken.setProcessId(UUID.randomUUID().toString());
        requestToken.setToken(UUID.randomUUID().toString());

        Response response = contractController.sign(requestToken);

        assertEquals(403, response.getStatus());
        assertEquals("Forbidden", response.getStatusText());
        assertEquals("This contract is already signed!", response.getMessage());
    }
}
