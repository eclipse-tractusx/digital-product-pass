package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.config.VaultConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.productpass.models.manager.Status;
import org.eclipse.tractusx.productpass.models.negotiation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.env.MockEnvironment;
import utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataTransferServiceTest {

    private DataTransferService dataTransferService;
    private Dataset dataSet;
    private Set policy;
    private String bpn;
    private final String testPolicyPath = "/src/test/resources/dpp/contractpolicies/TestPolicy.json";
    private final String testDTCatalogPath = "/src/test/resources/dpp/catalogs/TestDigitalTwinCatalog.json";
    private final String testCOCatalogPath = "/src/test/resources/dpp/catalogs/TestContractOfferCatalog.json";
    private final String testResponseInitNegotiationPath = "/src/test/resources/dpp/negotiation/TestResponseInitNegotiation.json";
    private final String testResponseNegotiationPath = "/src/test/resources/dpp/negotiation/TestResponseNegotiation.json";
    private final String testResponseInitTransferPath = "/src/test/resources/dpp/transfer/TestResponseInitTransfer.json";
    private final String testResponseTransferPath = "/src/test/resources/dpp/transfer/TestResponseTransfer.json";
    @Mock
    private VaultService vaultService;
    @Mock
    private ProcessManager processManager;
    @Mock
    private DtrConfig dtrConfig;
    @Mock
    private Environment env;
    @Mock
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;
    private YamlUtil yamlUtil;
    private FileUtil fileUtil;

    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);
        dtrConfig = initDtrConfig();
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        yamlUtil = new YamlUtil(fileUtil);
        env = initEnv();
        Map<String, Object> vaultConfig = yamlUtil.readFile(fileUtil.getDataDir() + "/VaultConfig/vault.token.yml");
        Map<String, Object> edc = (Map<String, Object>) jsonUtil.toMap(vaultConfig.get("edc"));
        when(vaultService.getLocalSecret("edc.apiKey")).thenReturn(edc.get("apiKey").toString());
        when(vaultService.getLocalSecret("edc.participantId")).thenReturn(edc.get("participantId").toString());

        bpn = edc.get("participantId").toString();

        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setDir("process");
        processManager = new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig);

        dataTransferService = new DataTransferService(env, httpUtil,jsonUtil, vaultService, processManager, dtrConfig);

        when(httpUtil.getHeaders()).thenReturn(new HttpHeaders());
        when(httpUtil.getParams()).thenReturn(new HashMap<>());
    }

    @BeforeEach
    void setUp() {
        Set policy = (Set) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() +  testPolicyPath, Set.class);

        Dataset dataSet = new Dataset();
        String dataSetId = UUID.randomUUID().toString();
        String assetId = UUID.randomUUID().toString();
        dataSet.setAssetName("TEST");
        dataSet.setId(dataSetId);
        dataSet.setType("test-type");
        dataSet.setAssetId(assetId);
        dataSet.setPolicy(List.of(policy));

        this.dataSet = dataSet;
        this.policy = policy;
    }

    private MockEnvironment initEnv() {
        MockEnvironment env = new MockEnvironment();
        Map<String, Object> application = yamlUtil.readFile(FileUtil.getWorkdirPath() + "/src/main/resources/application.yml");
        Map<String, Object> configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));
        Map<String, Object> edc = (Map<String, Object>) jsonUtil.toMap(configuration.get("edc"));
        env.setProperty("configuration.edc.endpoint", edc.get("endpoint").toString());
        env.setProperty("configuration.edc.catalog", edc.get("catalog").toString());
        env.setProperty("configuration.edc.management", edc.get("management").toString());
        env.setProperty("configuration.edc.negotiation", edc.get("negotiation").toString());
        env.setProperty("configuration.edc.transfer", edc.get("transfer").toString());

        return env;
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

    private VaultConfig initVaultConfig() {
        Map<String, Object> application = yamlUtil.readFile(FileUtil.getWorkdirPath() + "/src/main/resources/application.yml");
        Map<String, Object> configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));
        Map<String, Object> vault = (Map<String, Object>) jsonUtil.toMap(configuration.get("vault"));
        VaultConfig vaultConfig = new VaultConfig();
        vaultConfig.setFile(vault.get("file").toString());

        return vaultConfig;
    }

    @Test
    void checkEdcConsumerConnection() {
        when(httpUtil.doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false)))
                .thenReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testCOCatalogPath, JsonNode.class), HttpStatus.OK));

        String participantId = dataTransferService.checkEdcConsumerConnection();

        assertNotNull(participantId);
        assertEquals(vaultService.getLocalSecret("edc.participantId"), participantId);
    }

    @Test
    void buildRequestAndOffer() {
        Status status = new Status();
        status.setEndpoint("test.endpoint");

        NegotiationRequest negotiationRequest = dataTransferService.buildRequest(dataSet, status, bpn);

        assertNotNull(negotiationRequest);
        assertEquals(status.getEndpoint(), negotiationRequest.getConnectorAddress());
        assertEquals(bpn, negotiationRequest.getConnectorId());

        Offer offer = negotiationRequest.getOffer();

        assertNotNull(offer);
        assertEquals(dataSet.getAssetId(), offer.getAssetId());
        assertEquals(policy.getId(), offer.getOfferId());
        assertNotNull(offer.getPolicy());
    }

    @Test
    void getContractOfferCatalog() {
        String providerUrl = UUID.randomUUID().toString();
        Catalog catalog = (Catalog) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testCOCatalogPath, Catalog.class);
        String assetId = catalog.getParticipantId();

        when(httpUtil.doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false)))
                .then(invocation -> {
                    CatalogRequest body = invocation.getArgument(4);
                    if (body.getProviderUrl().equals(providerUrl)) {
                        return new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testCOCatalogPath, JsonNode.class), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                });

        Catalog offerCatalog = dataTransferService.getContractOfferCatalog(providerUrl, assetId);

        assertNotNull(offerCatalog);
        assertNotEquals(catalog, offerCatalog);
        assertEquals(catalog.getId(), offerCatalog.getId());
        assertEquals(catalog.getType(), offerCatalog.getType());
        assertEquals(catalog.getParticipantId(), offerCatalog.getParticipantId());
        assertEquals(catalog.getContext(), offerCatalog.getContext());

        Map<String, String> contractOffer = (Map<String, String>) jsonUtil.toMap(offerCatalog.getContractOffers());
        assertEquals("batterypass test data", contractOffer.get("edc:description"));

    }

    @Test
    void searchDigitalTwinCatalog() {
        String providerUrl = UUID.randomUUID().toString();
        Catalog catalog = (Catalog) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testDTCatalogPath, Catalog.class);

        when(httpUtil.doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false)))
                .then(invocation -> {
                    CatalogRequest body = invocation.getArgument(4);
                    if (body.getProviderUrl().equals(CatenaXUtil.buildDataEndpoint(providerUrl))) {
                        return new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testDTCatalogPath, JsonNode.class), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                });

        Catalog digitalTwinCatalog = dataTransferService.searchDigitalTwinCatalog(providerUrl);

        assertNotNull(digitalTwinCatalog);
        assertNotEquals(catalog, digitalTwinCatalog);
        assertEquals(catalog.getId(), digitalTwinCatalog.getId());
        assertEquals(catalog.getType(), digitalTwinCatalog.getType());
        assertEquals(catalog.getParticipantId(), digitalTwinCatalog.getParticipantId());
        assertEquals(catalog.getContext(), digitalTwinCatalog.getContext());

        Map<String, String> contractOffer = (Map<String, String>) jsonUtil.toMap(digitalTwinCatalog.getContractOffers());
        assertEquals("data.core.digitalTwinRegistry", contractOffer.get("edc:type"));
    }

    @Test
    void doContractNegotiationAndSeeNegotiation() {
        String providerUrl = UUID.randomUUID().toString();

        Offer offer = dataTransferService.buildOffer(dataSet, 0);

        when(httpUtil.doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(NegotiationRequest.class), eq(false), eq(false)))
                .then(invocation -> {
                    NegotiationRequest body = invocation.getArgument(4);
                    if (body instanceof NegotiationRequest) {
                        return new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testResponseInitNegotiationPath, JsonNode.class), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                });

        IdResponse response = dataTransferService.doContractNegotiation(offer, bpn, providerUrl);

        assertNotNull(response);
        assertEquals("189f4957-0fbe-4d73-b215-977e3303a45e", response.getId());
        assertEquals("edc:IdResponseDto", response.getType());

        when(httpUtil.doGet(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), eq(false), eq(false)))
                .thenReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testResponseNegotiationPath, JsonNode.class), HttpStatus.OK));

        Negotiation negotiation = dataTransferService.seeNegotiation(response.getId());

        assertEquals(response.getId(), negotiation.getId());
        assertEquals("edc:ContractNegotiationDto", negotiation.getType());
        assertEquals("FINALIZED", negotiation.getState());
    }

    private Negotiation getNegotiation() {
        String providerUrl = UUID.randomUUID().toString();
        Offer offer = dataTransferService.buildOffer(dataSet, 0);

        when(httpUtil.doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(NegotiationRequest.class), eq(false), eq(false)))
                .then(invocation -> {
                    NegotiationRequest body = invocation.getArgument(4);
                    if (body instanceof NegotiationRequest) {
                        return new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testResponseInitNegotiationPath, JsonNode.class), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                });

        IdResponse negotiationResponse = dataTransferService.doContractNegotiation(offer, bpn, providerUrl);

        when(httpUtil.doGet(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), eq(false), eq(false)))
                .thenReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testResponseNegotiationPath, JsonNode.class), HttpStatus.OK));

        return dataTransferService.seeNegotiation(negotiationResponse.getId());
    }

    @Test
    void initiateTransferAndSeeTransfer() {

        Negotiation negotiation = getNegotiation();
        Status status = new Status();
        status.setEndpoint("test.endpoint");

        TransferRequest transferRequest = new TransferRequest(
                jsonUtil.toJsonNode(Map.of("odrl", "http://www.w3.org/ns/odrl/2/")),
                dataSet.getAssetId(),
                status.getEndpoint(),
                bpn,
                negotiation.getContractAgreementId(),
                null,
                false,
                null,
                "dataspace-protocol-http",
                null
        );

        when(httpUtil.doPost(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), any(Object.class), eq(false), eq(false)))
                .thenReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testResponseInitTransferPath, JsonNode.class).toString(), HttpStatus.OK));

        IdResponse response = dataTransferService.initiateTransfer(transferRequest);

        assertNotNull(response);
        assertEquals("9ab72e5b-f2d4-4f60-85e6-0985f9b6b579", response.getId());
        assertEquals("edc:IdResponseDto", response.getType());

        when(httpUtil.doGet(anyString(), any(Class.class), any(HttpHeaders.class), any(Map.class), eq(false), eq(false)))
                .thenReturn(new ResponseEntity<>(jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testResponseTransferPath, JsonNode.class), HttpStatus.OK));

        Transfer transfer = dataTransferService.seeTransfer(response.getId());

        assertEquals(response.getId(), transfer.getId());
        assertEquals("edc:TransferProcessDto", transfer.getType());
        assertEquals("COMPLETED", transfer.getState());
    }
}
