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

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mocks.MockedHttpSession;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ManagerException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessDataModel;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.models.dtregistry.DigitalTwin;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.models.http.requests.Search;
import org.eclipse.tractusx.digitalproductpass.models.http.responses.IdResponse;
import org.eclipse.tractusx.digitalproductpass.models.irs.JobHistory;
import org.eclipse.tractusx.digitalproductpass.models.manager.History;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.eclipse.tractusx.digitalproductpass.models.manager.Status;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.catalog.Dataset;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.request.NegotiationRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.request.TransferRequest;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Negotiation;
import org.eclipse.tractusx.digitalproductpass.models.negotiation.response.Transfer;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import utils.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(MockitoJUnitRunner.class)
class ProcessManagerTest {

    private ProcessManager processManager;
    private final String processDataModelName = "processDataModel";
    private String baseDataDirPath;
    private Process testProcess;
    private String testProcessId;
    private List<String> processIdDirToDelete;
    private final String testPassportPath = "/dpp/payloads/TestPassport.json";
    private final String testDigitalTwinPath = "/dpp/digitaltwins/TestDigitalTwin.json";
    @Mock
    Environment env;
    @Mock
    private HttpSession mockedHttpSession;
    @Mock
    private HttpUtil httpUtil;

    @Mock
    private JsonUtil jsonUtil;

    @Mock
    private FileUtil fileUtil;
    @Mock
    private ProcessConfig processConfig;
    @Mock
    private HttpServletRequest mockedHttpServletRequest;

    @BeforeAll
    void setUpAll() {
        env = Mockito.mock(Environment.class);
        httpUtil = new HttpUtil(env);
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        processConfig = new ProcessConfig();
        processConfig.setDir("process");
        processManager = new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig);
        ReflectionTestUtils.setField(processManager, "env", env);
        mockedHttpServletRequest = Mockito.mock(HttpServletRequest.class);
        mockedHttpSession = new MockedHttpSession();
        baseDataDirPath = Path.of(fileUtil.getDataDir(), processConfig.getDir()).toString();
    }

    @BeforeEach
    void setUp() {
        when(mockedHttpServletRequest.getSession()).thenReturn(mockedHttpSession);
        when(env.getProperty("passport.dataTransfer.encrypt", Boolean.class, true)).thenReturn(true);
        when(env.getProperty("passport.dataTransfer.indent", Boolean.class, true)).thenReturn(true);

        processIdDirToDelete = new ArrayList<>();
        testProcessId = processManager.initProcess();
        processIdDirToDelete.add(testProcessId);
        testProcess = processManager.createProcess(testProcessId, mockedHttpServletRequest);
    }

    @AfterAll
    void tearDownAll() {
        for (String processIdToDelete : processIdDirToDelete) {
            String dataDirPath = Path.of(baseDataDirPath, processIdToDelete).toString();
            try {
                //Delete testProcessId dir from tmp directory
                processManager.deleteSearchDir(processIdToDelete);
            } catch (Exception e) {
                //Delete testProcessId dir from data directory
                if (fileUtil.pathExists(dataDirPath))
                    fileUtil.deleteDir(dataDirPath);
                continue;
            }
            //Delete testProcessId dir from data directory
            fileUtil.deleteDir(dataDirPath);
        }
    }

    @Test
    void loadDataModel() {
        ProcessDataModel processDataModel = processManager.loadDataModel(mockedHttpServletRequest);
        assertNotNull(processDataModel);

        ProcessDataModel sessionProcessDataModel = (ProcessDataModel) mockedHttpSession.getAttribute(processDataModelName);
        assertNotNull(sessionProcessDataModel);

        assertEquals(processDataModel, sessionProcessDataModel);
    }

    @Test
    void saveDataModelAndGetProcess() {
        ProcessDataModel initiatedProcessDataModel = processManager.loadDataModel(mockedHttpServletRequest);

        ProcessDataModel newProcessDataModel = new ProcessDataModel();
        newProcessDataModel.addProcess(testProcess);
        processManager.saveDataModel(mockedHttpServletRequest, newProcessDataModel);

        assertNotEquals(initiatedProcessDataModel, newProcessDataModel);
        assertEquals(testProcessId, processManager.getProcess(mockedHttpServletRequest, testProcessId).getId());
        assertEquals("CREATED", processManager.getProcess(mockedHttpServletRequest, testProcessId).getState());

    }

    @Test
    void generateStatusToken() {
        String contractId = UUID.randomUUID().toString();
        String newContractId = UUID.randomUUID().toString();
        Status status = processManager.getStatus(testProcessId);
        String testToken = processManager.generateStatusToken(status, contractId);
        String equalTestToken = processManager.generateStatusToken(status, contractId);
        String diffTestToken = processManager.generateStatusToken(status, newContractId);

        assertNotNull(testToken);
        assertEquals(equalTestToken, testToken);
        assertNotEquals(diffTestToken, testToken);
    }

    @Test
    void generateToken() {
        String contractId = UUID.randomUUID().toString();
        String newContractId = UUID.randomUUID().toString();
        String testToken = processManager.generateToken(testProcess, contractId);
        String equalTestToken = processManager.generateToken(testProcess, contractId);
        String diffTestToken = processManager.generateToken(testProcess, newContractId);

        assertNotNull(testToken);
        assertEquals(equalTestToken, testToken);
        assertNotEquals(diffTestToken, testToken);
    }

    @Test
    void createProcess() {
        String newProcessId = UUID.randomUUID().toString();
        processIdDirToDelete.add(newProcessId);
        Process nullConnectorAddressProcess = processManager.createProcess(mockedHttpServletRequest, "");
        processIdDirToDelete.add(nullConnectorAddressProcess.getId());
        Process connectorAddressProcess = processManager.createProcess(mockedHttpServletRequest, "testAddress");
        processIdDirToDelete.add(connectorAddressProcess.getId());
        Process processWithChildren = processManager.createProcess(newProcessId, true, mockedHttpServletRequest);
        processIdDirToDelete.add("100");

        assertNotNull(nullConnectorAddressProcess);
        assertNotNull(connectorAddressProcess);
        assertNotNull(processWithChildren);

        assertEquals("", processManager.getStatus(nullConnectorAddressProcess.getId()).getEndpoint());
        assertEquals("testAddress", processManager.getStatus(connectorAddressProcess.getId()).getEndpoint());
        assertEquals(true, processManager.getStatus(processWithChildren.getId()).getChildren());
    }

    @Test
    void getNullProcess() {
        assertNull(processManager.getProcess(mockedHttpServletRequest, "1"));
    }

    @Test
    void getProcessThrowsManagerException() {
        Throwable exception = assertThrows(ManagerException.class, () -> processManager.getProcess(null, null));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] It was not possible to get process [null], " +
                        "[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] Failed to load Process DataModel!, " +
                        "Cannot invoke \"jakarta.servlet.http.HttpServletRequest.getSession()\" because \"httpRequest\" is null"
                , exception.getMessage());
    }

    @Test
    void checkProcess() {
        String newProcessId = UUID.randomUUID().toString();
        processIdDirToDelete.add(newProcessId);

        //Check Process from HTTP request
        assertTrue(processManager.checkProcess(mockedHttpServletRequest, testProcessId));
        assertFalse(processManager.checkProcess(mockedHttpServletRequest, newProcessId));

        //Check Process from storage File
        assertTrue(processManager.checkProcess(testProcessId));
        assertFalse(processManager.checkProcess(newProcessId));
    }

    @Test
    void checkProcessThrowsManagerException() {
        Throwable exception = assertThrows(ManagerException.class, () -> processManager.checkProcess( null));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] It was not possible to check if process exists [null], " +
                "Cannot invoke \"String.isEmpty()\" because \"segment\" is null", exception.getMessage());

        exception = assertThrows(ManagerException.class, () -> processManager.checkProcess( null,testProcessId));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] It was not possible to check if process exists ["+ testProcessId +"], " +
                        "[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] Failed to load Process DataModel!, " +
                        "Cannot invoke \"jakarta.servlet.http.HttpServletRequest.getSession()\" because \"httpRequest\" is null",
                exception.getMessage());
    }

    @Test
    void setProcess() {
        String newProcessId = UUID.randomUUID().toString();
        processIdDirToDelete.add(newProcessId);
        Process newProcess = new Process(newProcessId, "NEW");
        Process nullProcess = processManager.getProcess(mockedHttpServletRequest, newProcessId);
        processManager.setProcess(mockedHttpServletRequest, newProcess);

        Process process = processManager.getProcess(mockedHttpServletRequest, newProcessId);

        assertNull(nullProcess);
        assertNotNull(process);
        assertEquals(newProcessId, process.getId());
        assertEquals("NEW", process.getState());

    }
    @Test
    void setProcessThrowsManagerException() {
        Throwable exception = assertThrows(ManagerException.class, () -> processManager.setProcess(null, testProcess));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] It was not possible to set process ["+testProcessId+"], " +
                        "[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] Failed to load Process DataModel!, " +
                        "Cannot invoke \"jakarta.servlet.http.HttpServletRequest.getSession()\" because \"httpRequest\" is null",
                exception.getMessage());
    }

    @Test
    void setAndGetProcessState() {
        String newProcessState = "UPDATED";
        String initialStateProcess = processManager.getProcessState(mockedHttpServletRequest, testProcessId);
        processManager.setProcessState(mockedHttpServletRequest, testProcessId, newProcessState);
        String updatedProcess = processManager.getProcessState(mockedHttpServletRequest, testProcessId);

        assertNotEquals(initialStateProcess, updatedProcess);
        assertEquals("CREATED", initialStateProcess);
        assertEquals("UPDATED", updatedProcess);
    }

    @Test
    void setAndGetStatus() {
        String historyId = "test-status";
        String status = "TEST";
        processManager.setStatus(testProcessId, historyId, new History(testProcessId, status));

        Status updatedStatus = processManager.getStatus(testProcessId);

        assertEquals(status, updatedStatus.getHistory(historyId).getStatus());
        assertEquals(testProcessId, updatedStatus.getHistory(historyId).getId());
    }

    @Test
    void setStatusThrowsManagerException() {
        Throwable exception = assertThrows(ManagerException.class, () -> processManager.setStatus(null, null, null));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] It was not possible to create/update the status file, " +
                "Cannot invoke \"String.isEmpty()\" because \"segment\" is null", exception.getMessage());
    }

    @Test
    void setBpn() {
        String inititalBpn = processManager.getStatus(testProcessId).getBpn();
        String updatedBpn = "BPN000000000";
        processManager.setBpn(testProcessId, updatedBpn);

        Status updatedStatus = processManager.getStatus(testProcessId);

        assertEquals("", inititalBpn);
        assertEquals(updatedBpn, updatedStatus.getBpn());
    }

    @Test
    void setTreeState() {
        String inititalTreeState = processManager.getStatus(testProcessId).getBpn();
        String updatedTreeState = "father/children1";
        processManager.setTreeState(testProcessId, updatedTreeState);

        Status updatedStatus = processManager.getStatus(testProcessId);

        assertEquals("", inititalTreeState);
        assertEquals(updatedTreeState, updatedStatus.getTreeState());
    }

    @Test
    void setJobHistory() {
        String inititalJobHistoryId = processManager.getStatus(testProcessId).getJob().getJobId();

        String jobHistoryId = UUID.randomUUID().toString();
        String searchId = UUID.randomUUID().toString();
        String globalAssetId = UUID.randomUUID().toString();
        JobHistory updateJobHistory = new JobHistory();
        updateJobHistory.setJobId(jobHistoryId);
        updateJobHistory.setSearchId(searchId);
        updateJobHistory.setGlobalAssetId(globalAssetId);
        updateJobHistory.setChildren(2);

        processManager.setJobHistory(testProcessId, updateJobHistory);

        Status updatedJobHistory = processManager.getStatus(testProcessId);

        assertNull(inititalJobHistoryId);
        assertEquals(jobHistoryId, updatedJobHistory.getJob().getJobId());
        assertEquals(searchId, updatedJobHistory.getJob().getSearchId());
        assertEquals(globalAssetId, updatedJobHistory.getJob().getGlobalAssetId());
        assertEquals(2, updatedJobHistory.getJob().getChildren());

        //Test Job Children Found method
        processManager.setJobChildrenFound(testProcessId, 4);
        updatedJobHistory = processManager.getStatus(testProcessId);
        assertEquals(4, updatedJobHistory.getJob().getChildren());
    }

    @Test
    void setSemanticIdAndEndpoint() {
        String inititalSemanticId = processManager.getStatus(testProcessId).getSemanticId();
        String initialEndpoint = processManager.getStatus(testProcessId).getEndpoint();
        String initialDataPlaneUrl = processManager.getStatus(testProcessId).getDataPlaneUrl();
        String updatedSemanticId = "test:1.0.0$SemanticIdTest";
        String updatedEndpoint = "test.com";
        String updatedDataPlaneUrl = "test-dataplane.com";
        processManager.setSemanticId(testProcessId, updatedSemanticId);
        processManager.setEndpoint(testProcessId, updatedEndpoint, updatedDataPlaneUrl);

        Status updatedStatus = processManager.getStatus(testProcessId);

        //SemanticId Tests
        assertEquals("", inititalSemanticId);
        assertEquals(updatedSemanticId, updatedStatus.getSemanticId());
        //Endpoint Tests
        assertEquals("", initialEndpoint);
        assertEquals(updatedEndpoint, updatedStatus.getEndpoint());
        //Data Plane Tests
        assertEquals("", initialDataPlaneUrl);
        assertEquals(updatedDataPlaneUrl, updatedStatus.getDataPlaneUrl());
    }

    @Test
    void setSearchAndGetSearchStatus() {
        Search initialSearch = processManager.getSearchStatus(testProcessId).getSearch();
        String searchId = UUID.randomUUID().toString();
        Search updateSearch = new Search();
        updateSearch.setProcessId(testProcessId);
        updateSearch.setId(searchId);
        updateSearch.setIdType("testType");

        processManager.setSearch(testProcessId, updateSearch);
        Search getUpdatedSearch = processManager.getSearchStatus(testProcessId).getSearch();

        assertNull(initialSearch);
        assertEquals(searchId, getUpdatedSearch.getId());
        assertEquals(testProcessId, getUpdatedSearch.getProcessId());
        assertEquals("testType", getUpdatedSearch.getIdType());
    }

    @Test
    void deleteSearchDir() {
        String processId = processManager.initProcess();
        processIdDirToDelete.add(processId);

        assertTrue(processManager.deleteSearchDir(processId));
    }

    @Test
    void deleteSearchDirThrowsManagerException() {
        Throwable exception = assertThrows(ManagerException.class, () -> processManager.deleteSearchDir("1000"));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] It was not possible to create/update the search in search status file, " +
                        "[org.eclipse.tractusx.digitalproductpass.managers.ProcessManager] Temporary process file does not exists for id [1000]!"
                , exception.getMessage());
    }

    @Test
    void addSearchStatusDtr() {
        int initialDtrCount = processManager.getSearchStatus(testProcessId).getDtrs().size();
        String contractId = UUID.randomUUID().toString();
        String assetId = UUID.randomUUID().toString();
        String endpoint = "dtr.endpoint";
        String bpn = "BPN01";
        Long validUntil = DateTimeUtil.getTimestamp();
        Dtr newDtr = new Dtr(contractId, endpoint, assetId, bpn, validUntil);

        processManager.addSearchStatusDtr(testProcessId, newDtr);

        Dtr updatedDtr = processManager.getSearchStatus(testProcessId).getDtr(CrypUtil.md5(endpoint));

        assertEquals(0, initialDtrCount);

        assertEquals(1, processManager.getSearchStatus(testProcessId).getDtrs().size());
        assertNotNull(updatedDtr);
        assertEquals(contractId, updatedDtr.getContractId());
        assertEquals(assetId, updatedDtr.getAssetId());
        assertEquals(endpoint, updatedDtr.getEndpoint());
        assertEquals(bpn, updatedDtr.getBpn());
        assertEquals(validUntil, updatedDtr.getValidUntil());
    }

    @Test
    void newStatusFile() {
        Status initialStatus = processManager.getStatus(testProcessId);
        String initialConnectorAddress = initialStatus.getEndpoint();
        boolean initialChildren = initialStatus.getChildren();
        long initialCreateTime = initialStatus.getCreated();

        String newConnectorAddress = "test.connector.address";
        boolean newChildren = false;
        long newCreateTime = DateTimeUtil.getTimestamp();
        processManager.newStatusFile(testProcessId, newConnectorAddress, newCreateTime, newChildren);

        Status updatedStatus = processManager.getStatus(testProcessId);

        assertNotEquals(initialConnectorAddress, updatedStatus.getEndpoint());
        assertNotEquals(initialChildren, updatedStatus.getChildren());
        assertNotEquals(initialCreateTime, updatedStatus.getCreated());

        assertEquals(newConnectorAddress, updatedStatus.getEndpoint());
        assertEquals(newChildren, updatedStatus.getChildren());
        assertEquals(newCreateTime, updatedStatus.getCreated());
    }

    @Test
    void saveTransferInfo() {
        String connectorAddress = UUID.randomUUID().toString();
        String semanticId = UUID.randomUUID().toString();
        String dataPlaneUrl = "dataplane.url.endpoint";
        String bpn = "BPN01";
        boolean children = false;

        processManager.saveTransferInfo(testProcessId, connectorAddress, semanticId, dataPlaneUrl, bpn, children);

        Status updatedStatus = processManager.getStatus(testProcessId);

        assertEquals(connectorAddress, updatedStatus.getEndpoint());
        assertEquals(semanticId, updatedStatus.getSemanticId());
        assertEquals(dataPlaneUrl, updatedStatus.getDataPlaneUrl());
        assertEquals(bpn, updatedStatus.getBpn());
        assertEquals(children, updatedStatus.getChildren());
    }

    @Test
    void saveNegotiationRequestAndNegotiation() {
        NegotiationRequest negotiationRequest = new NegotiationRequest();
        negotiationRequest.setProtocol("HTTP");
        negotiationRequest.setCounterPartyAddress("connectorAddress");

        String negotiationId = UUID.randomUUID().toString();
        String contractAgreement = UUID.randomUUID().toString();
        Negotiation negotiation = new Negotiation();
        negotiation.setProtocol("HTTP");
        negotiation.setEdcType("edcType1");
        negotiation.setId(negotiationId);
        negotiation.setState("NEGOTIATED");
        negotiation.setContractAgreementId(contractAgreement);

        Map<String, Object> initialNegotiation = processManager.loadNegotiation(testProcessId);

        assertNull(initialNegotiation);

        processManager.saveNegotiationRequest(testProcessId, negotiationRequest, new IdResponse(testProcessId, null), false);

        processManager.saveNegotiation(testProcessId, negotiation, false);
        Map<String, Object> updatedNegotiation = processManager.loadNegotiation(testProcessId);

        assertFalse(updatedNegotiation.isEmpty());
        assertEquals(2, updatedNegotiation.size());

        Map<String, Object> init = (Map<String, Object>) updatedNegotiation.get("init");
        NegotiationRequest updatedNegotiationRequest = (NegotiationRequest) jsonUtil.bindObject(init.get("request"), NegotiationRequest.class);

        assertEquals("HTTP", updatedNegotiationRequest.getProtocol());

        Map<String, Object> get = (Map<String, Object>) updatedNegotiation.get("get");
        Negotiation updatedNegotiationObj = (Negotiation) jsonUtil.bindObject(get.get("response"), Negotiation.class);

        assertEquals(negotiationId, updatedNegotiationObj.getId());
        assertEquals("NEGOTIATED", updatedNegotiationObj.getState());
        assertEquals("HTTP", updatedNegotiationObj.getProtocol());
        assertEquals("edcType1", updatedNegotiationObj.getEdcType());
    }

    @Test
    void cancelProcessAndSetDecline() {
        processManager.startNegotiation(mockedHttpServletRequest, testProcessId, new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        LogUtil.printTest("NEGOTIATING....!");
                        sleep(1);
                    }
                } catch (InterruptedException e) {
                    LogUtil.printTest("PROCESS STOPED!");
                }
            }
        });
        Process process = processManager.getProcess(mockedHttpServletRequest, testProcessId);

        assertEquals("RUNNING", process.getState());

        processManager.cancelProcess(mockedHttpServletRequest, testProcessId);
        Status status = processManager.getStatus(testProcessId);

        assertEquals("CANCELLED", status.getStatus());

        processManager.setDecline(mockedHttpServletRequest, testProcessId);
        process = processManager.getProcess(mockedHttpServletRequest, testProcessId);
        status = processManager.getStatus(testProcessId);

        assertEquals("ABORTED", process.getState());
        assertEquals("DECLINED", status.getStatus());
    }

    @Test
    void setAgreed() {
        String contractId = UUID.randomUUID().toString();
        String policyId = UUID.randomUUID().toString();
        Long signedAt = DateTimeUtil.getTimestamp();
        processManager.setAgreed(mockedHttpServletRequest, testProcessId, signedAt, contractId, policyId);

        Process process = processManager.getProcess(mockedHttpServletRequest, testProcessId);
        History history = processManager.getStatus(testProcessId).getHistory("contract-agreed");

        assertEquals("STARTING", process.getState());
        assertEquals("AGREED", history.getStatus());
        assertEquals(contractId+"/"+policyId, history.getId());
        assertEquals(signedAt, history.getStarted());
    }

    @Test
    void saveAndLoadDataset() {
        String assetId = UUID.randomUUID().toString();
        String datasetId = UUID.randomUUID().toString();
        Long started = DateTimeUtil.getTimestamp();
        Dataset dataset = new Dataset();
        dataset.setId(datasetId);
        dataset.setAssetId(assetId);
        Dataset initialDataset = processManager.loadDataset(testProcessId);

        assertNull(initialDataset);

        processManager.saveDataset(testProcessId, dataset, started, false);
        Dataset updatedDataset = processManager.loadDataset(testProcessId);

        assertNotNull(updatedDataset);
        assertEquals(datasetId, updatedDataset.getId());
        assertEquals(assetId, updatedDataset.getAssetId());
    }

    @Test
    void saveTransferRequestAndTransfer() {
        TransferRequest transferRequest = new TransferRequest();
        String contractId = UUID.randomUUID().toString();
        String assetId = UUID.randomUUID().toString();
        String connectorId = UUID.randomUUID().toString();
        transferRequest.setProtocol("HTTP");
        transferRequest.setContractId(contractId);
        transferRequest.setAssetId(assetId);
        transferRequest.setConnectorAddress("connectorAddress");

        String transferId = UUID.randomUUID().toString();
        Transfer transfer = new Transfer();
        transfer.setState("COMPLETED");
        transfer.setEdcType("edcType1");
        transfer.setId(transferId);

        Map<String, Object> initialTransfer = processManager.loadTransfer(testProcessId);

        assertNull(initialTransfer);

        processManager.saveTransferRequest(testProcessId, transferRequest, new IdResponse(testProcessId, null), false);

        processManager.saveTransfer(testProcessId, transfer, false);
        Map<String, Object> updatedTransfer = processManager.loadTransfer(testProcessId);

        assertFalse(updatedTransfer.isEmpty());
        assertEquals(2, updatedTransfer.size());

        Map<String, Object> init = (Map<String, Object>) updatedTransfer.get("init");
        TransferRequest updatedTransferRequest = (TransferRequest) jsonUtil.bindObject(init.get("request"), TransferRequest.class);

        assertEquals(contractId, updatedTransferRequest.getContractId());
        assertEquals("HTTP", updatedTransferRequest.getProtocol());

        Map<String, Object> get = (Map<String, Object>) updatedTransfer.get("get");
        Transfer updatedTransferObj = (Transfer) jsonUtil.bindObject(get.get("response"), Transfer.class);

        assertEquals(transferId, updatedTransferObj.getId());
        assertEquals("COMPLETED", updatedTransferObj.getState());
        assertEquals("edcType1", updatedTransferObj.getEdcType());
    }

    @Test
    void loadPassportReturnsManagerException() {
        assertThrows(ManagerException.class, () -> processManager.loadPassport(testProcessId));
    }
    @Test
    void saveAndLoadPassport() {
        EndpointDataReference dataPlaneEndpoint = new EndpointDataReference();
    }

    @Test
    void saveDigitalTwin() {
        String file = Paths.get(fileUtil.getBaseClassDir(this.getClass()), testDigitalTwinPath).toString();
        DigitalTwin digitalTwin = (DigitalTwin) jsonUtil.fromJsonFileToObject(file, DigitalTwin.class);
        Long createdAt = DateTimeUtil.getTimestamp();
        assertNotNull(digitalTwin);
        processManager.saveDigitalTwin(testProcessId, digitalTwin, createdAt);

        Status status = processManager.getStatus(testProcessId);

        assertEquals("READY", status.getHistory("digital-twin-request").getStatus());
        assertEquals(createdAt, status.getHistory("digital-twin-request").getStarted());
    }
}