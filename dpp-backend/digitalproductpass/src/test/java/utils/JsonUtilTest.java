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

package utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {utils.JsonUtil.class, utils.FileUtil.class})
class JsonUtilTest {

    @Autowired
    private JsonUtil jsonUtil;

    String testJson;
    String escapedJson;

    TestJsonClass testJsonObject;

    Map<String, Object> mappedTestObject;


    @BeforeEach
    void setUp() {
        testJson =
                "{" +
                    "\"string1\":\"testString1\"," +
                    "\"string2\":\"testString2\"," +
                    "\"object1\":" +
                        "{" +
                            "\"object1String\":\"testString\"," +
                            "\"object1Object1\":" +
                                "{" +
                                    "\"object1Object1String\":\"testString3\"," +
                                    "\"object1Object1Integer\":123456" +
                                "}" +
                        "}," +
                    "\"object2\":" +
                        "{" +
                            "\"object2Integer\":123456,"+
                            "\"object2String\":\"testString4\"" +
                        "}," +
                    "\"integer1\":123456789," +
                    "\"array1\":[\"element1\",\"element2\"]" +
                "}";
        escapedJson = "\"{\"string1\":\"testString\",\"string2\":\"testString\",\"object1\": {\"object1String\": \"testString\",\"object1Object1\": {\"object1Object1String\": \"testString\",\"object1Object1Integer\": 123456}},\"object2\": {\"object2Integer\": 123456,\"object2String\": \"testString\"},\"integer1\": 123456789,\"array1\": [\"element1\", \"element2\"]}\"";
        this.loadJson();
        this.toMap();
    }

    @Test
    void toMap() {
        Map<String, Object> map = null;
        try {
            map = (Map<String, Object>) jsonUtil.toMap(testJsonObject);
        }catch (Exception e){
            fail("It was not possible to map json object: " + e.getMessage());
        }
        assertNotNull(map);
        this.mappedTestObject = map;
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
        assertTrue(jsonUtil.isJson(testJson));
    }


    @Test
    void loadJson() {
        TestJsonClass json = null;
        try {
            json = (TestJsonClass) jsonUtil.loadJson(testJson, TestJsonClass.class);
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
            newJson = jsonUtil.escapeJson(testJson);
        }catch (Exception e) {
            fail("It was not possible to escape json: " + e.getMessage());
        }
        assertNotNull(newJson);
        LogUtil.printTest("Escaped Json: " + newJson);
    }

    @Test
    void parseJson() {
        Object newJson = null;
        try {
            newJson = jsonUtil.parseJson(testJson);
        }catch (Exception e) {
            fail("It was not possible to parse json: " + e.getMessage());
        }
        assertNotNull(newJson);
    }

    @Test
    void toJson() {
        String dumpJson = null;
        try {
            dumpJson =  jsonUtil.toJson(testJsonObject, false);
        }catch (Exception e){
            fail("It was not possible to load json: " + e.getMessage());
        }
        assertNotNull(dumpJson);
        LogUtil.printTest("Dumped Test Object: "  + dumpJson);
    }


    @Test
    void getValue() {
        String pathSep = "///";
        String keyPath = "object1///object1Object1///object1Object1String";
        String expectedValue = "testString3";
        String receivedValue = null;
        try {
            receivedValue = (String) jsonUtil.getValue(testJsonObject, keyPath,pathSep, null);
        }catch (Exception e) {
            fail("It was not possible to get value for path ["+keyPath+"]: " + e.getMessage());
        }
        assertEquals(expectedValue, receivedValue);
    }

    @Test
    void setValue() {
        String pathSep = "///";
        String keyPath = "object1///object1Object1///object1Object1Integer";
        Integer expectedValue = 789456123;
        Integer settedValue = null;
        HashMap<String, Object> newJsonObject = null;
        try {
            newJsonObject = (HashMap<String, Object>) jsonUtil.setValue(mappedTestObject, keyPath, expectedValue,pathSep, null);
            settedValue = (Integer) jsonUtil.getValue(newJsonObject, keyPath, pathSep, null);
        }catch (Exception e) {
            fail("It was not possible to set value for path ["+keyPath+"]: " + e.getMessage());
        }
        assertEquals(expectedValue, settedValue);
    }

    @Test
    void toJsonNode() {
        JsonNode jsonNode = null;
        Integer expectedValue = 123456789;
        Integer receivedValue = null;
        try {
            jsonNode = jsonUtil.toJsonNode(testJson);
        }catch (Exception e) {
            fail("It was not possible to parse string to jsonNode: " + e.getMessage());
        }
        try{
            receivedValue = jsonNode.get("integer1").asInt();
        }catch (Exception e) {
            fail("Received Value is not a integer!: " + e.getMessage());
        }
        assertEquals(expectedValue, receivedValue);
    }

    @Test
    void bindJsonNode() {
        TestJsonClass obj = null;
        JsonNode jsonNode = null;
        String expectedValue = "testString2";
        try {
            jsonNode = jsonUtil.toJsonNode(testJson);
            obj = (TestJsonClass) jsonUtil.bindJsonNode(jsonNode,TestJsonClass.class);
        }catch (Exception e) {
            fail("It was not possible to bind json: " + e.getMessage());
        }
        assertEquals(obj.string2, expectedValue);
    }
}
