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

import org.eclipse.tractusx.digitalproductpass.exceptions.DataModelException;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessDataModel;
import org.eclipse.tractusx.digitalproductpass.models.manager.Process;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProcessDataModelTest {

    @Mock
    private ProcessDataModel processDataModel;
    @Mock
    private Process testProcess;

    private final String processId = "1";
    private final String initProcessState = "INITIATED";


    @BeforeEach
    void setUp() {
        processDataModel = new ProcessDataModel();
        testProcess = new Process(processId, initProcessState);
        processDataModel.setDataModel(new HashMap<String, Process>() {{
            put(processId, testProcess);
        }});
    }

    @Test
    void setAndGetDataModel() {
        String newProcessId = "2";
        String newProcessState = "TEST";
        Process newProcess = new Process(newProcessId, newProcessState);

        Map<String, Process> initialMap = processDataModel.getDataModel();
        processDataModel.setDataModel(Map.of(processId, testProcess, newProcessId, newProcess));
        Map<String, Process> updatedMap = processDataModel.getDataModel();

        //Initial Map tests
        assertNotNull(initialMap);
        assertTrue(initialMap.containsKey("1"));
        assertEquals("1", initialMap.get(processId).getId());
        assertTrue(initialMap.size() == 1);
        //Updated Map tests
        assertNotNull(updatedMap);
        assertTrue(updatedMap.containsKey("2"));
        assertEquals("2", updatedMap.get(newProcessId).getId());
        assertTrue(updatedMap.size() == 2);
    }

    @Test
    void addAndGetProcess() {
        processDataModel.addProcess(testProcess);

        Process process = processDataModel.getProcess(processId);
        assertNotNull(process);
        assertEquals("1", process.getId());
        assertEquals("INITIATED", process.getState());
    }

    @Test
    void setAndGetState() {
        String initialState = processDataModel.getState(processId);
        processDataModel.setState(processId, "UPDATED");

        String updatedState = processDataModel.getState(processId);

        assertEquals("INITIATED", initialState);
        assertEquals("UPDATED", updatedState);
    }

    @Test()
    void setStateThrowsDataModelExceptionByNonexistentProcessId() {
        Throwable exception = assertThrows(DataModelException.class, () -> processDataModel.setState("100", "TEST"));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessDataModel] The process does not exists!", exception.getMessage());
    }

    @Test
    void processExists() {
        assertTrue(processDataModel.processExists(processId));
        assertFalse(processDataModel.processExists("100"));
    }

    @Test
    void startProcessSuccessfully() {
        processDataModel.startProcess(processId, new Runnable() {
            @Override
            public void run() {}
        });
        Process process = processDataModel.getProcess(processId);
        assertNotNull(process);
        assertEquals("RUNNING", process.getState());
    }

    @Test
    void startProcessThrowsDataModelExceptionByNonexistentProcessId() {
        Throwable exception = assertThrows(DataModelException.class, () -> processDataModel.startProcess("100", new Runnable() {
            @Override
            public void run() {}
        }));
        assertEquals("[org.eclipse.tractusx.digitalproductpass.managers.ProcessDataModel] It was not possible to start the process, " +
                "[org.eclipse.tractusx.digitalproductpass.managers.ProcessDataModel] The process does not exists!", exception.getMessage());
    }
}
