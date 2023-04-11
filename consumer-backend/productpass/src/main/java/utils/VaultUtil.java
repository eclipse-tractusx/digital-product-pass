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

import utils.exceptions.UtilException;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VaultUtil {
    private VaultUtil() {
        throw new IllegalStateException("Tool/Utility Class Illegal Initialization");
    }

    public static final ConfigUtil configuration = new ConfigUtil();
    private static final String ATTRIBUTES_PATH_SEP = (String) configuration.getConfigurationParam("vault.pathSep", ".", ".");
    private static final List<String> VAULT_ATTRIBUTES = (List<String>) configuration.getConfigurationParam("vault.attributes", ".", List.of("apiKey"));
    private static final Integer INDENT = (Integer) configuration.getConfigurationParam("vault.indent", ".", 2);
    private static final Boolean PRETTY_PRINT = (Boolean) configuration.getConfigurationParam("vault.prettyPrint", ".", 2);
    private static final String DEFAULT_VALUE = (String) configuration.getConfigurationParam("vault.defaultValue", ".", null);
    private static final String TOKEN_FILE_NAME = (String) configuration.getConfigurationParam("vault.file", ".", null);
    public static String createLocalVaultFile(Boolean searchSystemVariables){
        try {
            String dataDir = FileUtil.createDataDir("VaultConfig");
            String filePath = Path.of(dataDir, TOKEN_FILE_NAME).toAbsolutePath().toString();
            if(!FileUtil.pathExists(filePath)){
                LogUtil.printWarning("No vault token file found, creating yaml file in ["+filePath+"]");
                FileUtil.createFile(filePath); // Create YAML token file
            }
            String fileContent = FileUtil.readFile(filePath);
            Map<String, Object> vaultFileContent = null;
            try {
                vaultFileContent = YamlUtil.parseYml(fileContent);
            }catch (Exception e){
                LogUtil.printError("It was not possible to parse Yaml file ["+filePath+"]! Invalid format! Recreating file...");
            }
            if(vaultFileContent == null){
                vaultFileContent = new HashMap<>();
            }
            boolean update = false;
            String newDefaultValue = DEFAULT_VALUE;
            for (String attribute: VAULT_ATTRIBUTES){
                if(JsonUtil.getValue(vaultFileContent, attribute, ATTRIBUTES_PATH_SEP, null) != null) {
                    continue;
                }
                try {
                    if(searchSystemVariables){
                        newDefaultValue = SystemUtil.getEnvironmentVariable(attribute, DEFAULT_VALUE);
                    }
                    vaultFileContent = (Map<String, Object>) JsonUtil.setValue(vaultFileContent, attribute, newDefaultValue, ".", null);
                }catch (Exception e){
                    throw new UtilException(VaultUtil.class,
                            e,
                            "It was not possible to set value in attribute "+attribute);
                }
                if(vaultFileContent == null){
                    throw new UtilException(VaultUtil.class,
                            "It was not possible to set value in attribute "+attribute);
                }
                update = true;
            }


            if(update){
                String vaultYAML = YamlUtil.dumpYml(vaultFileContent, INDENT , PRETTY_PRINT);
                FileUtil.toFile(filePath, vaultYAML, false); // Create YAML token file
            }

            return filePath;
        }catch (Exception e){
            throw new UtilException(VaultUtil.class,
                    e,
                    "It was not possible to create secrets file");
        }
    }
}
