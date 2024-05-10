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

import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.ProcessConfig;
import org.eclipse.tractusx.digitalproductpass.managers.DtrSearchManager;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Dtr;
import org.eclipse.tractusx.digitalproductpass.services.DataTransferService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import utils.*;

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
    @Mock
    private EdcUtil edcUtil;
    @BeforeAll
    void setUpAll() {
        fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);
        dtrConfig = initDtrConfig();
        jsonUtil = new JsonUtil(fileUtil);
        edcUtil = new EdcUtil(jsonUtil, new PolicyUtil());
        env =  Mockito.mock(Environment.class);
        HttpUtil httpUtil = new HttpUtil(env);
        dataTransferService = Mockito.mock(DataTransferService.class);
        ProcessConfig processConfig = new ProcessConfig();
        processConfig.setDir("process");
        processManager = new ProcessManager(httpUtil, jsonUtil, fileUtil, processConfig);
        dtrSearchManager = new DtrSearchManager(fileUtil, edcUtil, jsonUtil, new PolicyUtil(), dataTransferService, dtrConfig, processManager);

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
}