package org.eclipse.tractusx.productpass.managers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mocks.MockedHttpSession;
import org.eclipse.tractusx.productpass.config.IrsConfig;
import org.eclipse.tractusx.productpass.config.PassportConfig;
import org.eclipse.tractusx.productpass.config.ProcessConfig;
import org.eclipse.tractusx.productpass.models.irs.JobHistory;
import org.eclipse.tractusx.productpass.models.irs.JobResponse;
import org.eclipse.tractusx.productpass.models.manager.Node;
import org.eclipse.tractusx.productpass.models.manager.NodeComponent;
import org.junit.jupiter.api.AfterAll;
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TreeManagerTest {

    private TreeManager treeManager;
    private JobResponse testJobResponse;
    private JobHistory testJobHistory;
    private Map<String, Node> treeDataModel;
    private final String testJobResposnsePath = "/src/test/resources/dpp/irs/TestJobResponse.json";
    private final String testJobHistoryPath = "/src/test/resources/dpp/irs/TestJobHistory.json";
    private final String testTreeDataModelPath = "/src/test/resources/dpp/irs/TestTreeDataModel.json";
    private String baseDataDirPath;
    private String testProcessId;
    private Map<String, Object> configuration;
    @Mock
    private HttpServletRequest request;
    private HttpSession httpSession;
    private IrsConfig irsConfig;
    private PassportConfig passportConfig;
    private ProcessConfig processConfig;
    private ProcessManager processManager;
    private FileUtil fileUtil;
    private JsonUtil jsonUtil;
    private HttpUtil httpUtil;
    private YamlUtil yamlUtil;
    @Mock
    private Environment env;

    @BeforeAll
    void setUpAll() {
        MockitoAnnotations.openMocks(this);

        fileUtil = new FileUtil();
        yamlUtil = new YamlUtil(fileUtil);
        jsonUtil = new JsonUtil(fileUtil);

        Map<String, Object> application = yamlUtil.readFile(FileUtil.getWorkdirPath() + "/src/main/resources/application.yml");
        configuration = (Map<String, Object>) jsonUtil.toMap(application.get("configuration"));

        httpUtil = Mockito.spy(new HttpUtil(env));
        httpSession = new MockedHttpSession();

        when(request.getSession()).thenReturn(httpSession);

        irsConfig = initIrsConfig();
        Map<String, Object> passport = (Map<String, Object>) jsonUtil.toMap(configuration.get("passport"));
        passportConfig = new PassportConfig();
        passportConfig.setSearchIdSchema(passport.get("searchIdSchema").toString());
        processConfig = new ProcessConfig();
        processConfig.setDir("process");
        baseDataDirPath = Path.of(fileUtil.getDataDir(), processConfig.getDir()).toString();

        processManager = new ProcessManager(httpUtil,jsonUtil, fileUtil, processConfig);

        treeManager = new TreeManager(fileUtil, jsonUtil, processManager, irsConfig, passportConfig);

        testProcessId = processManager.initProcess();
        processManager.createProcess(testProcessId, request);

        testJobResponse = (JobResponse) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testJobResposnsePath, JobResponse.class);
        testJobHistory = (JobHistory) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testJobHistoryPath, JobHistory.class);
        treeDataModel = (Map<String, Node>) jsonUtil.toMap(jsonUtil.fromJsonFile(FileUtil.getWorkdirPath() + testTreeDataModelPath));
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

    @Test
    void getTreeFilePath() {
        String treePath = treeManager.getTreeFilePath(testProcessId);

        assertNotNull(treePath);
        assertTrue(treePath.contains(testProcessId));
        assertTrue(treePath.contains(irsConfig.getTree().getFileName()));
    }

    @Test
    void createTreeFile() {
        String path = treeManager.createTreeFile(testProcessId);

        assertNotNull(path);
        assertTrue(treeManager.treeExists(testProcessId));
    }

    @Test
    void generateSearchId() {
        String globalAssetId = UUID.randomUUID().toString();

        String searchId = TreeManager.generateSearchId(testProcessId, globalAssetId);

        assertNotNull(searchId);
        assertEquals(32, searchId.getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    void populateAndGetTree() {

        treeManager.createTreeFile(testProcessId);
        processManager.setJobHistory(testProcessId, testJobHistory);

        String path = treeManager.populateTree(treeDataModel,testProcessId, testJobHistory, testJobResponse);
        assertNotNull(path);

        Map<String, Node> treeMap = treeManager.getTree(testProcessId);
        assertNotNull(treeMap);

        Node parent = (Node) jsonUtil.bindObject(treeMap.get("urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d"), Node.class);
        assertNotNull(parent);
        assertEquals("urn:uuid:82e78a83-3ddd-64c1-455d-8f7629833f17", parent.getId());
        assertEquals("Battery_BAT-XYZ789", parent.getIdShort());
        assertEquals("CX:XYZ78901:BAT-XYZ789", parent.getSearchId());

        Node child = parent.getChild("urn:uuid:d8ec6acc-1ad7-47b4-bc7e-612122d9d552");
        assertNotNull(child);
        assertEquals("urn:uuid:ace301f6-92c5-4623-a022-c2a30dfee0e2", child.getId());
        assertEquals("BatteryModule_EVMODULE-TRJ712", child.getIdShort());
        assertEquals("CX:XYZ78901:EVMODULE-TRJ712", child.getSearchId());
    }

    @Test
    void getTreeComponents() {
        treeManager.createTreeFile(testProcessId);
        processManager.setJobHistory(testProcessId, testJobHistory);

        treeManager.populateTree(treeDataModel,testProcessId, testJobHistory, testJobResponse);

        List<NodeComponent> treeComponents = treeManager.getTreeComponents(testProcessId);

        assertNotNull(treeComponents);
        assertEquals(1, treeComponents.size());

        NodeComponent parent = treeComponents.get(0);

        assertNotNull(parent);
        assertEquals("urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d", parent.getId());
        assertEquals("Battery_BAT-XYZ789", parent.getName());
        assertEquals("CX:XYZ78901:BAT-XYZ789", parent.getSearchId());

        List<NodeComponent> children = parent.getChildren();

        assertNotNull(children);
        assertEquals(1, children.size());
        NodeComponent child = children.get(0);

        assertNotNull(child);
        assertEquals("urn:uuid:d8ec6acc-1ad7-47b4-bc7e-612122d9d552", child.getId());
        assertEquals("BatteryModule_EVMODULE-TRJ712", child.getName());
        assertEquals("CX:XYZ78901:EVMODULE-TRJ712", child.getSearchId());

    }

}
