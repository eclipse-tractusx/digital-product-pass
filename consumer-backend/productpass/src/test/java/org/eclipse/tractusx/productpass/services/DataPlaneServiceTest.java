package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import utils.FileUtil;
import utils.HttpUtil;
import utils.JsonUtil;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataPlaneServiceTest {

    private DataPlaneService dataPlaneService;
    private final String testPassportPath = "/src/test/resources/dpp/payloads/TestPassport.json";
    @Mock
    private HttpUtil httpUtil;
    private JsonUtil jsonUtil;

    @BeforeAll
    void setUpAll() throws ServiceInitializationException {
        MockitoAnnotations.openMocks(this);
        FileUtil fileUtil = new FileUtil();
        jsonUtil = new JsonUtil(fileUtil);

        dataPlaneService = new DataPlaneService();
        ReflectionTestUtils.setField(dataPlaneService, "httpUtil", httpUtil);
        ReflectionTestUtils.setField(dataPlaneService, "jsonUtil", jsonUtil);

        when(httpUtil.getHeaders()).thenReturn(new HttpHeaders());
        when(httpUtil.getParams()).thenReturn(new HashMap<>());

        when(httpUtil.doGet(anyString(), any(Class.class), any(HttpHeaders.class), any(), eq(true), eq(true)))
                .then(invocation -> {
                    JsonNode passport = (JsonNode) jsonUtil.fromJsonFileToObject(FileUtil.getWorkdirPath() + testPassportPath, JsonNode.class);
                    return new ResponseEntity<>(passport, HttpStatus.OK);
                });

    }

    @Test
    void getPassportFromEndpoint() {
        String id = UUID.randomUUID().toString();
        String endpoint = UUID.randomUUID().toString();
        String authKey = UUID.randomUUID().toString();
        String authCode = UUID.randomUUID().toString();
        DataPlaneEndpoint dataPlaneEndpoint = new DataPlaneEndpoint();
        dataPlaneEndpoint.setId(id);
        dataPlaneEndpoint.setEndpoint(endpoint);
        dataPlaneEndpoint.setAuthKey(authKey);
        dataPlaneEndpoint.setAuthCode(authCode);
        String dataPLaneEndpointStr = "test.endpoint";

        JsonNode passport = dataPlaneService.getPassportFromEndpoint(dataPlaneEndpoint, dataPLaneEndpointStr);

        assertNotNull(passport);
        assertEquals("\"27.04.2022\"", passport.get("datePlacedOnMarket").toString());
        assertEquals("\"96\"", passport.get("warrantyPeriod").toString());
        assertEquals("210", passport.get("cO2FootprintTotal").toString());
    }

}
