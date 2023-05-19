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

package org.eclipse.tractusx.productpass.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.productpass.config.VaultConfig;
import org.eclipse.tractusx.productpass.exceptions.ServiceException;
import org.eclipse.tractusx.productpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.productpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import utils.*;
import utils.exceptions.UtilException;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VaultService extends BaseService {


    private final FileUtil fileUtil;

    private final JsonUtil jsonUtil;

    private final YamlUtil yamlUtil;

    private final VaultConfig vaultConfig;

    @Autowired
    public VaultService(VaultConfig vaultConfig, FileUtil fileUtil, JsonUtil jsonUtil, YamlUtil yamlUtil) throws ServiceInitializationException {
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.yamlUtil = yamlUtil;
        this.vaultConfig = vaultConfig;
        this.createLocalVaultFile(true);
        this.checkEmptyVariables();
    }


    public Object getLocalSecret(String localSecretPath) {
        try {
            String secret = null;
            String filePath = this.createLocalVaultFile(true);
            Map<String, Object> content = yamlUtil.readFile(filePath);
            try {
                secret = (String) jsonUtil.getValue(content,localSecretPath, ".",null);
            }catch (Exception e){
                LogUtil.printException(e, "["+this.getClass().getName()+"."+"getLocalSecret] " + "There was an error while searching the secret ["+localSecretPath+"] in file!");
                //throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret", e, "There was an error while searching the secret ["+localSecretPath+"] in file!");
            }
            if(secret == null){
                LogUtil.printError("["+this.getClass().getName()+"."+"getLocalSecret] " + "Secret ["+localSecretPath+"] not found in file!");
                //throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret", "Secret ["+localSecretPath+"] not found in file!");
            }
            return secret;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret",
                    e,
                    "It was not possible to get secret from file.");
        }
    }
    public String createLocalVaultFile(Boolean searchSystemVariables){
        try {
            String dataDir = fileUtil.createDataDir("VaultConfig");
            String filePath = Path.of(dataDir, this.vaultConfig.getFile()).toAbsolutePath().toString();
            if(!fileUtil.pathExists(filePath)){
                LogUtil.printWarning("No vault token file found, creating yaml file in ["+filePath+"]");
                fileUtil.createFile(filePath); // Create YAML token file
            }
            String fileContent = fileUtil.readFile(filePath);
            Map<String, Object> vaultFileContent = null;
            try {
                vaultFileContent = yamlUtil.parseYml(fileContent);
            }catch (Exception e){
                LogUtil.printError("It was not possible to parse Yaml file ["+filePath+"]! Invalid format! Recreating file...");
            }
            if(vaultFileContent == null){
                vaultFileContent = new HashMap<>();
            }
            boolean update = false;
            String newDefaultValue = this.vaultConfig.getDefaultValue();
            for (String attribute: this.vaultConfig.getAttributes()){
                if(jsonUtil.getValue(vaultFileContent, attribute, this.vaultConfig.getPathSep(), null) != null) {
                    continue;
                }
                try {
                    if(searchSystemVariables){
                        newDefaultValue = SystemUtil.getEnvironmentVariable(attribute, this.vaultConfig.getDefaultValue());
                    }
                    vaultFileContent = (Map<String, Object>) jsonUtil.setValue(vaultFileContent, attribute, newDefaultValue, ".", null);
                }catch (Exception e){
                    throw new ServiceException(this.getClass().getName(),
                            e,
                            "It was not possible to set value in attribute "+attribute);
                }
                if(vaultFileContent == null){
                    throw new ServiceException(this.getClass().getName(),
                            "It was not possible to set value in attribute "+attribute);
                }
                update = true;
            }


            if(update){
                String vaultYAML = yamlUtil.dumpYml(vaultFileContent, this.vaultConfig.getIndent(), this.vaultConfig.getPrettyPrint());
                fileUtil.toFile(filePath, vaultYAML, false); // Create YAML token file
            }

            return filePath;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(),
                    e,
                    "It was not possible to create secrets file");
        }
    }

    @Override
    public List<String> getEmptyVariables() {
        return new ArrayList<>();
    }
}
