package managers;

import org.eclipse.tractusx.digitalproductpass.Application;
import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.config.VaultConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.manager.SearchStatus;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.Catalog;
import org.eclipse.tractusx.digitalproductpass.services.DataTransferService;
import org.eclipse.tractusx.digitalproductpass.services.VaultService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import utils.*;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DtrSearchManagerTest {

    private DtrSearchManager dtrSearchManager;
    private final String testAssetPath = "/dpp/assets/TestAsset.json";
    @Mock
    private ProcessManager processManager;
    @Mock
    private DataTransferService dataTransferService;
    @Mock
    private DtrConfig dtrConfig;
    @Mock
    Environment env;
    @Mock
    private FileUtil fileUtil;
    @Mock
    private JsonUtil jsonUtil;

    @BeforeAll
    void setUpAll() {
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        dtrConfig = initDtrConfig();

        env =  Mockito.mock(Environment.class);
        HttpUtil httpUtil = new HttpUtil(env);
        dataTransferService = Mockito.mock(DataTransferService.class);
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setDir("process");
        processManager = new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig);
        dtrSearchManager = new DtrSearchManager(fileUtil, jsonUtil, dataTransferService, dtrConfig, processManager);

        fileUtil.deleteFile(dtrSearchManager.getDataModelPath());
        dtrSearchManager.loadDataModel();

        when(dtrConfig.getTemporaryStorage().getEnabled()).thenReturn(true);
    }

    private DtrConfig initDtrConfig() {
        DtrConfig dtrConfig = new DtrConfig();
        DtrConfig.Timeouts timeouts = new DtrConfig.Timeouts();
        timeouts.setSearch(10);
        timeouts.setDtrRequestProcess(40);
        timeouts.setNegotiation(10);
        timeouts.setTransfer(10);
        timeouts.setDigitalTwin(20);
        dtrConfig.setTimeouts(timeouts);
        dtrConfig.setTemporaryStorage(new DtrConfig.TemporaryStorage(true, 12));
        return dtrConfig;
    }

    @Test
    void createDataModelFile() {
        Map<String, List<Dtr>> initialDtrDataModel = dtrSearchManager.getDtrDataModel();

        assertTrue(initialDtrDataModel.isEmpty());

        String dtrDataModelPath = dtrSearchManager.createDataModelFile();
        Map<String, List<Dtr>> newDtrDataModel = dtrSearchManager.getDtrDataModel();

        assertNotNull(dtrDataModelPath);
        assertNotNull(newDtrDataModel);
        assertTrue(newDtrDataModel.isEmpty());
    }

    @Test
    void addConnectionToBpnEntry() {
        Map<String, List<Dtr>> initialDtrDataModel = dtrSearchManager.getDtrDataModel();

        assertTrue(initialDtrDataModel.isEmpty());

        String bpn = "BPN001";
        String contractId = UUID.randomUUID().toString();
        String assetId = UUID.randomUUID().toString();
        String endpoint = "dtr.endpoint";
        Long validUntil = DateTimeUtil.getTimestamp();
        Dtr newDtr = new Dtr(contractId, endpoint, assetId, bpn, validUntil);

        dtrSearchManager.addConnectionToBpnEntry(bpn, newDtr);

        ConcurrentHashMap<String, List<Dtr>> updatedDtrDataModel = dtrSearchManager.getDtrDataModel();
        Dtr updatedDtr = updatedDtrDataModel.get(bpn).get(0);

        assertEquals(1, updatedDtrDataModel.get(bpn).size());
        assertEquals(newDtr, updatedDtr);
        assertEquals(bpn, updatedDtr.getBpn());
        assertEquals(contractId, updatedDtr.getContractId());
        assertEquals(assetId, updatedDtr.getAssetId());

        dtrSearchManager.deleteBpns(dtrSearchManager.getDtrDataModel(), List.of(bpn));
        dtrSearchManager.saveDtrDataModel();
    }

    @Test
    void deleteBpns() {
        int maxDtrs = 4;
        int count = 0;
        List<String> bpnsList = new ArrayList<>();
        while (count < maxDtrs) {
            String bpn = UUID.randomUUID().toString();
            String endpoint = UUID.randomUUID().toString();
            Dtr dtr = new Dtr();
            dtr.setBpn(bpn);
            dtr.setEndpoint(endpoint);
            bpnsList.add(bpn);
            dtrSearchManager.addConnectionToBpnEntry(bpn, dtr);
            ++count;
        }

        ConcurrentHashMap<String, List<Dtr>> dtrDataModel = dtrSearchManager.getDtrDataModel();

        assertEquals(4, dtrDataModel.size());

        dtrSearchManager.deleteBpns(dtrDataModel, bpnsList);

        assertEquals(0 , dtrDataModel.size());
    }

    @Test
    void saveAndLoadDataModel() {
        ConcurrentHashMap<String, List<Dtr>> dtrDataModel = dtrSearchManager.getDtrDataModel();

        assertTrue(dtrDataModel.isEmpty());

        String bpn1 = "BPN001";
        String contractId1 = UUID.randomUUID().toString();
        String assetId1 = UUID.randomUUID().toString();
        String endpoint1 = "dtr.endpoint1";
        Long validUntil1 = DateTimeUtil.getTimestamp();
        Dtr dtr1 = new Dtr(contractId1, endpoint1, assetId1, bpn1, validUntil1);
        String bpn2 = "BPN002";
        String contractId2 = UUID.randomUUID().toString();
        String assetId2 = UUID.randomUUID().toString();
        String endpoint2 = "dtr.endpoint2";
        Long validUntil2 = DateTimeUtil.getTimestamp();
        Dtr dtr2 = new Dtr(contractId2, endpoint2, assetId2, bpn2, validUntil2);
        String contractId3 = UUID.randomUUID().toString();
        String assetId3 = UUID.randomUUID().toString();
        String endpoint3 = "dtr.endpoint3";
        Long validUntil3 = DateTimeUtil.getTimestamp();
        Dtr dtr3 = new Dtr(contractId3, endpoint3, assetId3, bpn2, validUntil3);

        dtrSearchManager.addConnectionToBpnEntry(bpn1, dtr1);
        dtrSearchManager.addConnectionToBpnEntry(bpn2, dtr2);
        dtrSearchManager.addConnectionToBpnEntry(bpn2, dtr3);
        dtrSearchManager.saveDtrDataModel(dtrDataModel);
        dtrDataModel = dtrSearchManager.loadDataModel();

        assertFalse(dtrDataModel.isEmpty());
        assertEquals(2, dtrDataModel.size());
        assertEquals(1, dtrDataModel.get(bpn1).size());
        assertEquals(2, dtrDataModel.get(bpn2).size());

        dtrSearchManager.deleteBpns(dtrSearchManager.getDtrDataModel(), List.of(bpn1, bpn2));
        dtrSearchManager.saveDtrDataModel();
    }

    @Test
    void updateProcess() { //TODO
        String bpn1 = "BPN001";
        String contractId1 = UUID.randomUUID().toString();
        String assetId1 = UUID.randomUUID().toString();
        String endpoint1 = "dtr.endpoint1";
        Long validUntil1 = DateTimeUtil.getTimestamp();
        Dtr dtr1 = new Dtr(contractId1, endpoint1, assetId1, bpn1, validUntil1);

    }

    @Test
    void searchEndpoint() { //TODO
        String file = Paths.get(fileUtil.getBaseClassDir(this.getClass()), testAssetPath).toString();
        Object contractOffer = jsonUtil.fromJsonFileToObject(file, Object.class);
        assertNotNull(contractOffer);
        Catalog catalog = new Catalog();
        catalog.setContractOffer(contractOffer);
        String connectionUrl = "test.connection.url";
        when(dataTransferService.searchDigitalTwinCatalog(connectionUrl)).thenReturn(catalog);
        String bpn = "BPN000001";
        String testProcessId = processManager.initProcess();

        dtrSearchManager.searchEndpoint(testProcessId, bpn, connectionUrl);

        SearchStatus searchStatus = processManager.getSearchStatus(testProcessId);

      //  assertEquals(bpn, searchStatus.getDtrs().get(0).getBpn());
      //  assertEquals(connectionUrl, searchStatus.getDtrs().get(0).getEndpoint());
    }
}
