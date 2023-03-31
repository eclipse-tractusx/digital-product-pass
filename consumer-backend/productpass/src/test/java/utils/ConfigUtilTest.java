/*********************************************************************************
 *
 * Catena-X - Product Passport Consumer Backend
 *
 * Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConfigUtilTest {
    public static final ConfigUtil configuration = new ConfigUtil();

    List<String> mandatoryAttributes;

    public static final String pathSep = ".";
    public static final Object defaultValue = null;
    @BeforeAll
    static void beforeAll() {

    }
    @BeforeEach
    void setUp() {
        mandatoryAttributes = new ArrayList<String>();
        // Values mandatory in configuration
        mandatoryAttributes.add( "LogUtil"+pathSep+ "level");
        mandatoryAttributes.add("LogUtil"+pathSep+ "async");
        mandatoryAttributes.add("maxRetries");
        mandatoryAttributes.add("variables"+pathSep+"default"+pathSep+"providerUrl");
        mandatoryAttributes.add( "variables"+pathSep+ "default"+pathSep+ "serverUrl");
        mandatoryAttributes.add("variables"+pathSep+ "default"+pathSep+ "registryUrl");
        mandatoryAttributes.add( "passport"+pathSep+ "versions");
        mandatoryAttributes.add( "vault"+pathSep+ "type");
        mandatoryAttributes.add( "vault"+pathSep+ "file");
        mandatoryAttributes.add( "vault"+pathSep+ "uri");
        mandatoryAttributes.add( "vault"+pathSep+ "pathSep");
        mandatoryAttributes.add("vault"+pathSep+ "prettyPrint");
        mandatoryAttributes.add("vault"+pathSep+ "indent");
        mandatoryAttributes.add("vault"+pathSep+ "defaultValue");
        mandatoryAttributes.add("vault"+pathSep+ "attributes");
    }


    @Test
    void getConfiguration() {
        Map<String, Object> newJson = null;
        try {
            newJson = configuration.getConfiguration();
        }catch (Exception e) {
            fail("It was not possible to get configuration: " + e.getMessage());
        }
        assertNotNull(newJson);
    }

    @Test
    void getConfigurationParam() {
        try {
            Object value = null;
            for (String attr :mandatoryAttributes){
                value = configuration.getConfigurationParam(attr, pathSep, defaultValue);
                if(value == "" || value == null){
                    throw new Exception("Configuration param ["+attr+"] is missing!");
                }
            }
        }catch (Exception e) {
            fail("It was not possible to get configuration param " + e.getMessage());
        }
        LogUtil.printTest("[SUCCESS] All configuration parameters are configured!");
    }

}
