package org.eclipse.tractusx.productpass.managers;

import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.models.manager.Process;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.UndeclaredThrowableException;
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
        assertEquals("[org.eclipse.tractusx.productpass.managers.ProcessDataModel] The process does not exists!", exception.getMessage());
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
        assertEquals("[org.eclipse.tractusx.productpass.managers.ProcessDataModel] It was not possible to start the process, " +
                "[org.eclipse.tractusx.productpass.managers.ProcessDataModel] The process does not exists!", exception.getMessage());
    }
}
