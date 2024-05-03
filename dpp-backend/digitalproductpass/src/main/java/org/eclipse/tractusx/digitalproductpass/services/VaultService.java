/*********************************************************************************
 *
 * Tractus-X - Digital Product Passport Application
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

package org.eclipse.tractusx.digitalproductpass.services;

import org.eclipse.tractusx.digitalproductpass.config.VaultConfig;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceException;
import org.eclipse.tractusx.digitalproductpass.exceptions.ServiceInitializationException;
import org.eclipse.tractusx.digitalproductpass.models.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class consists exclusively of methods to operate on Vault storage system.
 *
 * <p> The methods defined here are intended to set, save, check, read or create Vault's variables.
 *
 */
@Service
public class VaultService extends BaseService {

    /** ATTRIBUTES **/
    private final FileUtil fileUtil;
    private final JsonUtil jsonUtil;
    private final YamlUtil yamlUtil;
    private final VaultConfig vaultConfig;

    /** CONSTRUCTOR(S) **/
    @Autowired
    public VaultService(VaultConfig vaultConfig, FileUtil fileUtil, JsonUtil jsonUtil, YamlUtil yamlUtil) throws ServiceInitializationException {
        this.fileUtil = fileUtil;
        this.jsonUtil = jsonUtil;
        this.yamlUtil = yamlUtil;
        this.vaultConfig = vaultConfig;
        this.createLocalVaultFile(true);
        this.checkEmptyVariables();
    }

    /** METHODS **/

    /**
     * Sets a new value to an existent parameter in the Vault.
     * <p>
     * @param   localSecretPath
     *          the path to the Vault's secret parameter.
     * @param   value
     *          the {@code Object} value to set.
     *
     * @return  true if the value was successfully update, false otherwise.
     *
     * @throws  ServiceException
     *           if unable to get the secret.
     */
    public Boolean setLocalSecret(String localSecretPath, Object value) {
        try {
            Object response = null;
            String filePath = this.createLocalVaultFile(true);
            Map<String, Object> content = yamlUtil.readFile(filePath);
            try {
                response = (Object) jsonUtil.setValue(content,localSecretPath, value,".",null);
            }catch (Exception e){
                LogUtil.printException(e, "["+this.getClass().getName()+"."+"getLocalSecret] " + "There was an error while setting the secret ["+localSecretPath+"] in file!");
                return false;
            }
            if(response == null){
                LogUtil.printError("["+this.getClass().getName()+"."+"getLocalSecret] " + "Secret ["+localSecretPath+"] failed to be set in the file");
                return false;
            }
            return this.saveVault(filePath, response);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret",
                    e,
                    "It was not possible to get secret from file.");
        }
    }

    /**
     * Saves a given data into the Vault.
     * <p>
     * @param   filePath
     *          the path to the Vault's file.
     * @param   response
     *          the {@code Object} data to save.
     *
     * @return  true if the data was successfully saved, false otherwise.
     *
     * @throws  ServiceException
     *           if unable to save the data into the vault.
     */
    public Boolean saveVault(String filePath, Object response){
        try {
            String vaultYAML = yamlUtil.dumpYml(response, this.vaultConfig.getIndent(), this.vaultConfig.getPrettyPrint());
            if(fileUtil.toFile(filePath, vaultYAML, false).isEmpty()){
                return false;
            };
            return true;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"saveVault",
                    e,
                    "It was not possible to save the vault");
        }
    }

    /**
     * Checks if a secret parameter exists in the Vault.
     * <p>
     * @param   localSecretPath
     *          the path to the Vault's secret parameter.
     *
     * @return  true if the secret exists in the vault, false otherwise.
     *
     * @throws  ServiceException
     *           if unable to get the secret.
     */
    public Boolean secretExists(String localSecretPath){
        try {
            String filePath = this.createLocalVaultFile(true);
            Map<String, Object> content = yamlUtil.readFile(filePath);
            return jsonUtil.checkJsonKeys(content, List.of(localSecretPath), ".", false);
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret",
                    e,
                    "It was not possible to get secret from file.");
        }
    }

    /**
     * Gets the value of a secret parameter from the Vault.
     * <p>
     * @param   localSecretPath
     *          the path to the Vault's secret parameter.
     *
     * @return  the {@code Object} value of the given secret parameter.
     *
     * @throws  ServiceException
     *           if unable to get the secret's value.
     */
    public Object getLocalSecret(String localSecretPath) {
        try {
            Object secret = null;
            String filePath = this.createLocalVaultFile(true);
            Map<String, Object> content = yamlUtil.readFile(filePath);
            try {
                secret =  jsonUtil.getValue(content,localSecretPath, ".",null);
            }catch (Exception e){
                LogUtil.printException(e, "["+this.getClass().getName()+"."+"getLocalSecret] " + "There was an error while searching the secret ["+localSecretPath+"] in file!");
            }
            if(secret == null){
                LogUtil.printError("["+this.getClass().getName()+"."+"getLocalSecret] " + "Secret ["+localSecretPath+"] not found in file!");
            }
            return secret;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName()+"."+"getLocalSecret",
                    e,
                    "It was not possible to get secret from file.");
        }
    }

    /**
     * Creates a new File in the Vault.
     * <p>
     * @param   searchSystemVariables
     *          {@code Boolean} to specify if it's to use the default values configured in Vault environment variables.
     *
     * @return  the {@code String} path of the new file created.
     *
     * @throws  ServiceException
     *           if unable create the new file.
     */
    public String createLocalVaultFile(Boolean searchSystemVariables){
        try {
            String dataDir = fileUtil.createDataDir("VaultConfig");
            String filePath = null;
            try {
                filePath = Path.of(dataDir, this.vaultConfig.getFile()).toAbsolutePath().toString();
            }catch (Exception e){
                throw new ServiceException(this.getClass().getName(), e, "It was not possible to create filepath");
            }
            if(filePath == null){
                throw new ServiceException(this.getClass().getName(), "It was not possible to create filepath response is null");
            }
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
                this.saveVault(filePath, vaultFileContent);
            }

            return filePath;
        }catch (Exception e){
            throw new ServiceException(this.getClass().getName(),
                    e,
                    "It was not possible to create secrets file");
        }
    }

    /**
     * Creates an empty variables List.
     * <p>
     *
     * @return an empty {@code Arraylist}.
     *
     */
    @Override
    public List<String> getEmptyVariables() {
        return new ArrayList<>();
    }
}
