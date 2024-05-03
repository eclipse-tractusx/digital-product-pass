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

package utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.tractusx.digitalproductpass.managers.ProcessManager;
import org.eclipse.tractusx.digitalproductpass.models.edc.EndpointDataReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.nio.file.Path;

/**
 * This class consists exclusively of methods to operate on Passports.
 *
 * <p> The methods defined here are intended to read, save and parse Passport's type objects.
 *
 */
@Component
public class PassportUtil {
    private final JsonUtil jsonUtil;
    private final FileUtil fileUtil;
    private final String transferDir;

    private final ProcessManager processManager;

    @Autowired
    public PassportUtil(JsonUtil jsonUtil, FileUtil fileUtil,ProcessManager processManager, Environment env) {
        this.transferDir = env.getProperty("passport.dataTransfer.dir", String.class, "data/transfer");
        this.jsonUtil = jsonUtil;
        this.fileUtil = fileUtil;
        this.processManager = processManager;
    }

    /**
     * Saves the target {@code Passport} object to a JSON file.
     * <p>
     * @param   passport
     *          the passport object to save.
     * @param   endpointData
     *          the data plane object containing information related to the endpoint from where the specified passport was retrieved.
     * @param   prettyPrint
     *          boolean to specify a pretty or compact print of the passport into the JSON file.
     * @param   encrypted
     *          boolean to specify if the passport is to be saved with encryption.
     *
     * @return  a {@code String} object representing the target JSON file path.
     *
     * @throws  UtilException
     *          if unable to save the Passport to a JSON file.
     */
    @SuppressWarnings("Unused")
    public String savePassport(JsonNode passport, EndpointDataReference endpointData, Boolean prettyPrint, Boolean encrypted){
        try {
            fileUtil.createDir(this.transferDir);
            String path = Path.of(this.transferDir, endpointData.getId() + ".json").toAbsolutePath().toString();
            return this.savePassport(passport, endpointData, prettyPrint, encrypted, path);
        }catch (Exception e){
            throw new UtilException(PassportUtil.class, e, "Something went wrong while creating the path and saving the passport for transfer ["+endpointData.getId()+"]");
        }
    }

    /**
     * Saves the target {@code Passport} object to a JSON file specified by a {@code String} filePath.
     * <p>
     * @param   passport
     *          the passport object to save.
     * @param   endpointData
     *          the data plane object containing information related to the endpoint from where the specified passport was retrieved.
     * @param   prettyPrint
     *          boolean to specify a pretty or compact print of the passport into the JSON file.
     * @param   encrypted
     *          boolean to specify if the passport is to be saved with encryption.
     * @param   filePath
     *          the path representation to the target JSON file as a String.
     *
     * @return  a {@code String} object representing the target JSON file path.
     *
     * @throws  UtilException
     *          if unable to save the Passport to a JSON file.
     */
    public String savePassport(JsonNode passport, EndpointDataReference endpointData, Boolean prettyPrint, Boolean encrypted, String filePath){
        try {
            if(!encrypted) {
                return jsonUtil.toJsonFile(filePath, passport, prettyPrint); // Store the plain JSON
            }else{
                // Get token content or the contractID from properties
                String contractId = processManager.getContractId(endpointData);
                if(contractId == null){
                    throw new UtilException(PassportUtil.class, "The Contract Id is null! It was not possible to save the passport!");
                }
                return fileUtil.toFile(filePath, CrypUtil.encryptAes(jsonUtil.toJson(passport, prettyPrint), contractId+endpointData.getId()), false); // Store Encrypted
            }
        }catch (Exception e){
            throw new UtilException(PassportUtil.class, e, "Something went wrong while saving the passport for transfer ["+endpointData.getId()+"]");
        }
    }
}
