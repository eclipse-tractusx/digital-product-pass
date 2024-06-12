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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {utils.YamlUtil.class, utils.FileUtil.class})
class YamlUtilTest {

    @Autowired
    private YamlUtil yamlUtil;
    String yaml;
    @BeforeEach
    void setUp() {
        yaml = "object1:\n  object2:\n    string1: testString\nstring2: testString2";
    }
    @Test
    void parseYml() {
        Map<String, Object> parsedYaml = null;
        String expected = "testString2";
        try{
            parsedYaml = yamlUtil.parseYml(yaml);
        } catch(Exception e){
            fail("It was not possible to parse yaml: " + e.getMessage());
        }
        assertEquals(expected,parsedYaml.get("string2"));
    }

    @Test
    void dumpYml() {
        Map<String, Object> parsedYaml;
        String dumpYaml = null;
        try{
            parsedYaml = yamlUtil.parseYml(yaml);
            if(parsedYaml == null){
                fail("It was not possible to parse yaml!");
            }
            dumpYaml = yamlUtil.dumpYml(parsedYaml, 2, true);
        } catch(Exception e){
            fail("It was not possible to dump yaml: " + e.getMessage());
        }
        assertNotNull(dumpYaml);
    }
}
