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

package managers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mocks.MockedHttpSession;
import org.eclipse.tractusx.digitalproductpass.config.IrsConfig;
import org.eclipse.tractusx.digitalproductpass.config.PassportConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.managers.TreeManager;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobResponse;
import org.eclipse.tractusx.digitalproductpass.models.manager.Node;
import org.eclipse.tractusx.digitalproductpass.models.manager.NodeComponent;
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
import java.nio.file.Paths;
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
    private final String testJobResposnsePath = "/dpp/irs/TestJobResponse.json";
    private final String testJobHistoryPath = "/dpp/irs/TestJobHistory.json";
    private final String testTreeDataModelPath = "/dpp/irs/TestTreeDataModel.json";
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
        String configurationFilePath = Paths.get(fileUtil.getBaseClassDir(this.getClass()), "application-test.yml").toString();
        Map<String, Object> application = yamlUtil.readFile(configurationFilePath);

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

        testJobResponse = (JobResponse) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testJobResposnsePath).toString(), JobResponse.class);
        testJobHistory = (JobHistory) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testJobHistoryPath).toString(), JobHistory.class);
        treeDataModel = (Map<String, Node>) jsonUtil.toMap(jsonUtil.fromJsonFile(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testTreeDataModelPath).toString()));
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
