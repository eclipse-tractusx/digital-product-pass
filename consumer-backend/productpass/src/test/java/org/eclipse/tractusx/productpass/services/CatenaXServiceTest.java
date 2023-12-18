package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.productpass.config.DiscoveryConfig;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.DtrSearchManager;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
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
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CatenaXServiceTest {

    private CatenaXService catenaXService;
    private Map<String, Object> configuration;
    private DataTransferService dataTransferService;
    private DtrSearchManager dtrSearchManager;
    private AuthenticationService authenticationService;
    private final String mockedTokenPath = "/src/test/resources/dpp/token/MockedToken.json";
    private JwtToken mockedToken;
    private final String mockedDiscoveryEndpointsPath = "/src/test/resources/dpp/discovery/MockedDiscoveryEndpoints.json";
    @Mock
    private VaultService vaultService;
    private DtrConfig dtrConfig;
    private DiscoveryConfig discoveryConfig;
    private YamlUtil yamlUtil;
    private JsonUtil jsonUtil;

    private HttpUtil httpUtil;
    private FileUtil fileUtil;
    @Mock
    private Environment env;
    private Discovery discovery;

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

        authenticationService = Mockito.spy(new AuthenticationService(vaultService, env, httpUtil, jsonUtil));

        mockedToken = (JwtToken) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + mockedTokenPath, JwtToken.class);
        doReturn(mockedToken).when(authenticationService).getToken();

        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setDir("process");
        ProcessManager processManager = new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig);
        dataTransferService = new DataTransferService(env, httpUtil,jsonUtil, vaultService, processManager, dtrConfig);

        discoveryConfig = initDiscoveryConfig();
        dtrConfig = initDtrConfig();

        dtrSearchManager = new DtrSearchManager(fileUtil, jsonUtil, dataTransferService, dtrConfig, processManager);
        catenaXService = new CatenaXService(env, fileUtil, httpUtil, jsonUtil, vaultService, dtrSearchManager, authenticationService, discoveryConfig, dataTransferService, dtrConfig);

        discovery = new Discovery(new ArrayList<>());

        when(vaultService.setLocalSecret(anyString(), any(List.class))).thenReturn(true);
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
    private DiscoveryConfig initDiscoveryConfig() {
        DiscoveryConfig discoveryConfig = new DiscoveryConfig();
        Map<String, Object> discovery = (Map<String, Object>) jsonUtil.toMap(configuration.get("discovery"));
        discoveryConfig.setEndpoint(discovery.get("endpoint").toString());
        discoveryConfig.setEdc((DiscoveryConfig.EDCConfig) jsonUtil.bindObject(discovery.get("edc"), DiscoveryConfig.EDCConfig.class));
        discoveryConfig.setBpn((DiscoveryConfig.BPNConfig) jsonUtil.bindObject(discovery.get("bpn"), DiscoveryConfig.BPNConfig.class));
        return discoveryConfig;
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

        return dtrConfig;
    }

    @Test
    void getDiscoveryEndpoints() {
        Discovery mockedDefaultDiscovery = (Discovery) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + mockedDiscoveryEndpointsPath, Discovery.class);

        doReturn(new ResponseEntity<>(jsonUtil.toJsonNode(jsonUtil.toJson(mockedDefaultDiscovery, true)), HttpStatus.OK))
                .when(httpUtil).doPost(anyString(), eq(JsonNode.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false));

        Discovery defaultDiscovery = catenaXService.start();

        assertNotNull(defaultDiscovery);
        assertEquals(2, defaultDiscovery.getEndpoints().size());
    }
    @Test
    void addEndpoint() {
        String edcKey = "manufacturerPartId";
        String edcResourceId = UUID.randomUUID().toString();
        Discovery.Endpoint edcEndpoint = new Discovery.Endpoint();
        edcEndpoint.setType(edcKey);
        edcEndpoint.setEndpointAddress("test.edc.address");
        edcEndpoint.setResourceId(edcResourceId);
        String bpnKey = "bpn";
        String bpnResourceId = UUID.randomUUID().toString();
        Discovery.Endpoint bpnEndpoint = new Discovery.Endpoint();
        bpnEndpoint.setType(bpnKey);
        bpnEndpoint.setEndpointAddress("test..bpn.address");
        bpnEndpoint.setResourceId(bpnResourceId);
        discovery.getEndpoints().add(edcEndpoint);

        doReturn(new ResponseEntity<>(jsonUtil.toJsonNode(jsonUtil.toJson(discovery, true)), HttpStatus.OK))
                .when(httpUtil).doPost(anyString(), eq(JsonNode.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false));

        Discovery discoveryUpdated = catenaXService.addEndpoint(edcKey);

        assertNotNull(discoveryUpdated);
        assertEquals(3, discoveryUpdated.getEndpoints().size());

        discovery.getEndpoints().add(bpnEndpoint);
        discoveryUpdated = catenaXService.addEndpoint(bpnKey);

        assertNotNull(discoveryUpdated);
        assertEquals(3, discoveryUpdated.getEndpoints().size());
    }

    @Test
    void getEdcDiscovery() {
        String bpn = UUID.randomUUID().toString();
        EdcDiscoveryEndpoint mockedEdcDiscoveryEndpoint = new EdcDiscoveryEndpoint();
        mockedEdcDiscoveryEndpoint.setBpn(bpn);
        mockedEdcDiscoveryEndpoint.setConnectorEndpoint(List.of("test.endpoint"));

        when(vaultService.getLocalSecret(eq("discovery.edc"))).thenReturn(new ArrayList<>(){{add("test.endpoint");}});
        doReturn(new ResponseEntity<>(jsonUtil.toJsonNode(new ArrayList<>(){{add(mockedEdcDiscoveryEndpoint);}}), HttpStatus.OK))
                .when(httpUtil).doPost(anyString(), eq(JsonNode.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false));

        List<EdcDiscoveryEndpoint> edcDiscoveryEndpoints = catenaXService.getEdcDiscovery(List.of(bpn));

        assertNotNull(edcDiscoveryEndpoints);

        EdcDiscoveryEndpoint edcDiscoveryEndpoint = (EdcDiscoveryEndpoint) jsonUtil.bindObject(edcDiscoveryEndpoints.get(0), EdcDiscoveryEndpoint.class);

        assertEquals(1, edcDiscoveryEndpoints.size());
        assertEquals(bpn, edcDiscoveryEndpoint.getBpn());
        assertEquals("test.endpoint", edcDiscoveryEndpoint.getConnectorEndpoint().get(0));
    }

    @Test
    @Order(4)
    void getBpnDiscovery() {
        String edcKey = "manufacturerPartId";
        String edcResourceId = UUID.randomUUID().toString();
        Discovery.Endpoint edcEndpoint = new Discovery.Endpoint();
        edcEndpoint.setType(edcKey);
        edcEndpoint.setEndpointAddress("test.edc.address");
        edcEndpoint.setResourceId(edcResourceId);
        String bpnKey = "bpn";
        String bpnResourceId = UUID.randomUUID().toString();
        String bpn = "BPN00001";
        Discovery.Endpoint bpnEndpoint = new Discovery.Endpoint();
        bpnEndpoint.setType(bpnKey);
        bpnEndpoint.setEndpointAddress("test.bpn.address");
        bpnEndpoint.setResourceId(bpnResourceId);
        discovery.getEndpoints().add(edcEndpoint);
        discovery.getEndpoints().add(bpnEndpoint);

        when(vaultService.getLocalSecret("discovery.bpn")).thenReturn(new ArrayList<>(){{add("test.endpoint");}});

        Mockito.doAnswer(invocation -> {
            Map<String, List<?>> body = invocation.getArgument(4);
            if (body.containsKey("types")) {
                return new ResponseEntity<>(jsonUtil.toJsonNode(jsonUtil.toJson(discovery, true)), HttpStatus.OK);
            }
            BpnDiscovery.BpnEndpoint bpnDiscoveryEndpoint = new BpnDiscovery.BpnEndpoint();
            bpnDiscoveryEndpoint.setKey(bpnKey);
            bpnDiscoveryEndpoint.setResourceId(bpnResourceId);
            bpnDiscoveryEndpoint.setType("TEST");
            bpnDiscoveryEndpoint.setValue(bpn);
            BpnDiscovery bpnDiscovery = new BpnDiscovery(new ArrayList<>(){{add(bpnDiscoveryEndpoint);}});
            return new ResponseEntity<>(jsonUtil.toJsonNode(jsonUtil.toJson(bpnDiscovery, true)), HttpStatus.OK);
        }).when(httpUtil).doPost(anyString(), eq(JsonNode.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false));

        List<BpnDiscovery> bpnDiscoveryList = catenaXService.getBpnDiscovery(bpnResourceId, bpnKey);

        assertNotNull(bpnDiscoveryList);

        BpnDiscovery bpnDiscoveryEndpoint = (BpnDiscovery) jsonUtil.bindObject(bpnDiscoveryList.get(0), BpnDiscovery.class);

        assertEquals(1, bpnDiscoveryList.size());
        assertTrue(bpnDiscoveryEndpoint.getBpnNumbers().contains(bpn));
        assertEquals(bpnResourceId, bpnDiscoveryEndpoint.getBpns().get(0).getResourceId());

    }
}
