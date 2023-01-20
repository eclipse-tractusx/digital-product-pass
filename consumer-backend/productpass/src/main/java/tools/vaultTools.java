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

import tools.exceptions.ToolException;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class vaultTools {
    private vaultTools() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    public static final configTools configuration = new configTools();
    private static final String ATTRIBUTES_PATH_SEP = (String) configuration.getConfigurationParam("vault.pathSep", ".", ".");
    private static final List<String> VAULT_ATTRIBUTES = (List<String>) configuration.getConfigurationParam("vault.attributes", ".", List.of("apiKey"));
    private static final Integer INDENT = (Integer) configuration.getConfigurationParam("vault.indent", ".", 2);
    private static final Boolean PRETTY_PRINT = (Boolean) configuration.getConfigurationParam("vault.prettyPrint", ".", 2);
    private static final String DEFAULT_VALUE = (String) configuration.getConfigurationParam("vault.defaultValue", ".", null);
    private static final String TOKEN_FILE_NAME = (String) configuration.getConfigurationParam("vault.file", ".", null);
    public static String createLocalVaultFile(Boolean searchSystemVariables){
        try {
            String dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = Path.of(dataDir, TOKEN_FILE_NAME).toAbsolutePath().toString();
            if(!fileTools.pathExists(filePath)){
                logTools.printWarning("No vault token file found, creating yaml file in ["+filePath+"]");
                fileTools.createFile(filePath); // Create YAML token file
            }
            String fileContent = fileTools.readFile(filePath);
            Map<String, Object> vaultFileContent = null;
            try {
                vaultFileContent = yamlTools.parseYml(fileContent);
            }catch (Exception e){
                logTools.printError("It was not possible to parse Yaml file ["+filePath+"]! Invalid format! Recreating file...");
            }
            if(vaultFileContent == null){
                vaultFileContent = new HashMap<>();
            }
            boolean update = false;
            String newDefaultValue = DEFAULT_VALUE;
            for (String attribute: VAULT_ATTRIBUTES){
                if(jsonTools.getValue(vaultFileContent, attribute, ATTRIBUTES_PATH_SEP, null) != null) {
                    continue;
                }
                try {
                    if(searchSystemVariables){
                        newDefaultValue = systemTools.getEnvironmentVariable(attribute, DEFAULT_VALUE);
                    }
                    vaultFileContent = (Map<String, Object>) jsonTools.setValue(vaultFileContent, attribute, newDefaultValue, ".", null);
                }catch (Exception e){
                    throw new ToolException(vaultTools.class,
                            e,
                            "It was not possible to set value in attribute "+attribute);
                }
                if(vaultFileContent == null){
                    throw new ToolException(vaultTools.class,
                            "It was not possible to set value in attribute "+attribute);
                }
                update = true;
            }


            if(update){
                String vaultYAML = yamlTools.dumpYml(vaultFileContent, INDENT , PRETTY_PRINT);
                fileTools.toFile(filePath, vaultYAML, false); // Create YAML token file
            }

            return filePath;
        }catch (Exception e){
            throw new ToolException(vaultTools.class,
                    e,
                    "It was not possible to create secrets file");
        }
    }
}
