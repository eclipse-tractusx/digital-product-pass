/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2022, 2023 Contributors to the CatenaX (ng) GitHub Organisation.
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

package tools;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class jsonToolsTest {

    String testJson;
    String escapedJson;

    TestJsonClass testJsonObject;

    @BeforeEach
    void setUp() {
        testJson =
                "{" +
                    "\"string1\":\"testString\"," +
                    "\"string2\":\"testString\"," +
                    "\"object1\":" +
                        "{" +
                            "\"object1String\":\"testString\"," +
                            "\"object1Object1\":" +
                                "{" +
                                    "\"object1Object1String\":\"testString\"," +
                                    "\"object1Object1Integer\":123456" +
                                "}" +
                        "}," +
                    "\"object2\":" +
                        "{" +
                            "\"object2Integer\":123456,"+
                            "\"object2String\":\"testString\"" +
                        "}," +
                    "\"integer1\":123456789," +
                    "\"array1\":[\"element1\",\"element2\"]" +
                "}";
        escapedJson = "\"{\"string1\":\"testString\",\"string2\":\"testString\",\"object1\": {\"object1String\": \"testString\",\"object1Object1\": {\"object1Object1String\": \"testString\",\"object1Object1Integer\": 123456}},\"object2\": {\"object2Integer\": 123456,\"object2String\": \"testString\"},\"integer1\": 123456789,\"array1\": [\"element1\", \"element2\"]}\"";
        this.loadJson();
    }

    public static class TestJsonClass{
        @JsonProperty("string1")
        String string1;

        @JsonProperty("string2")
        String string2;

        @JsonProperty("object1")
        JsonNode object1;
        @JsonProperty("object2")
        JsonNode object2;

        @JsonProperty("integer1")
        Integer integer1;

        @JsonProperty("array1")
        List<String> array1;

        public TestJsonClass(String string1, String string2, JsonNode object1, JsonNode object2, Integer integer1, List<String> array1) {
            this.string1 = string1;
            this.string2 = string2;
            this.object1 = object1;
            this.object2 = object2;
            this.integer1 = integer1;
            this.array1 = array1;
        }

        public TestJsonClass() {
        }
    }

    @Test
    void isJson() {
        assertTrue(jsonTools.isJson(testJson));
    }


    @Test
    void loadJson() {
        TestJsonClass json = null;
        try {
            json = (TestJsonClass) jsonTools.loadJson(testJson, TestJsonClass.class);
        }catch (Exception e){
            fail("It was not possible to load json: " + e.getMessage());
        }
        assertNotNull(json);
        this.testJsonObject = json;
    }

    @Test
    void escapeJson() {
        String newJson = null;
        try {
            newJson = jsonTools.escapeJson(testJson);
        }catch (Exception e) {
            fail("It was not possible to escape json: " + e.getMessage());
        }
        assertNotNull(newJson);
        logTools.printTest("Escaped Json: " + newJson);
    }

    @Test
    void parseJson() {
        Object newJson = null;
        try {
            newJson = jsonTools.parseJson(testJson);
        }catch (Exception e) {
            fail("It was not possible to parse json: " + e.getMessage());
        }
        assertNotNull(newJson);
    }

    @Test
    void dumpJson() {
        String dumpJson = null;
        try {
            testJsonObject = (TestJsonClass) jsonTools.loadJson(testJson, TestJsonClass.class);
            dumpJson =  jsonTools.toJson(testJsonObject, false);
        }catch (Exception e){
            fail("It was not possible to load json: " + e.getMessage());
        }
        assertNotNull(dumpJson);
        logTools.printTest("Dumped Test Object: "  + dumpJson);
    }

    @Test
    void toJson() {
        String json = null;
        try {
            json = jsonTools.toJson(testJsonObject, false);
        }catch (Exception e) {
            fail("It was not possible to parse json: " + e.getMessage());
        }
        assertEquals(json, testJson);
    }

    @Test
    void getValue() {
        String pathSep = "///";
        String keyPath = "object1///object1Object1///object1Object1String";
        String expectedValue = "testString";
        String receivedValue = null;
        try {
            receivedValue = (String) jsonTools.getValue(this.testJsonObject, keyPath,pathSep, null);
        }catch (Exception e) {
            fail("It was not possible to get value for path ["+keyPath+"]: " + e.getMessage());
        }
        assertEquals(expectedValue, receivedValue);
    }

    @Test
    void setValue() {
    }

    @Test
    void toJsonNode() {
    }

    @Test
    void bindJsonNode() {
        String json = null;
        try {
            json = jsonTools.toJson(testJsonObject, false);
        }catch (Exception e) {
            fail("It was not possible to parse json: " + e.getMessage());
        }
        assertEquals(json, testJson);
    }
}
