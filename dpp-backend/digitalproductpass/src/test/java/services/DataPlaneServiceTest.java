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

package services;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.eclipse.tractusx.digitalproductpass.services.DataPlaneService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import utils.FileUtil;
import utils.HttpUtil;
import utils.JsonUtil;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataPlaneServiceTest {

    private DataPlaneService dataPlaneService;
    private final String testPassportPath = "/dpp/payloads/TestPassport.json";
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

        when(httpUtil.doGet(anyString(), eq(JsonNode.class), any(HttpHeaders.class), any(), eq(true), eq(true)))
                .then(invocation -> {
                    JsonNode passport = (JsonNode) jsonUtil.fromJsonFileToObject(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testPassportPath).toString(), JsonNode.class);
                    return new ResponseEntity<>(passport, HttpStatus.OK);
                });
        when(httpUtil.doGet(anyString(), eq(String.class), any(HttpHeaders.class), any(), eq(true), eq(true)))
                .then(invocation -> {
                    String passport = fileUtil.readFile(Paths.get(fileUtil.getBaseClassDir(this.getClass()), testPassportPath).toString());
                    return new ResponseEntity<>(passport, HttpStatus.OK);
                });

    }

    @Test
    void getPassportFromEndpoint() {
        String id = UUID.randomUUID().toString();
        String endpoint = UUID.randomUUID().toString();
        String authKey = UUID.randomUUID().toString();
        String authCode = UUID.randomUUID().toString();
        EndpointDataReference dataPlaneEndpoint = EndpointDataReference.builder().id(id)
                .payload(
                EndpointDataReference.Payload.builder().dataAddress(
                        EndpointDataReference.DataAddress.builder().properties(
                                EndpointDataReference.Properties.builder().endpoint(endpoint).authorization(authCode).build()
                        ).build()
                    ).build()
                ).build();
        String dataPLaneEndpointStr = "test.endpoint";

        JsonNode passport = dataPlaneService.getPassportFromEndpoint(dataPlaneEndpoint, dataPLaneEndpointStr);

        assertNotNull(passport);
        assertEquals("\"27.04.2022\"", passport.get("datePlacedOnMarket").toString());
        assertEquals("\"96\"", passport.get("warrantyPeriod").toString());
        assertEquals("210", passport.get("cO2FootprintTotal").toString());
    }

}
