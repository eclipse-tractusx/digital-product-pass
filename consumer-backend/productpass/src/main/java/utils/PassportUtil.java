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

package utils;

import org.eclipse.tractusx.productpass.exceptions.ManagerException;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.edc.DataPlaneEndpoint;
import org.eclipse.tractusx.productpass.models.edc.Jwt;
import org.eclipse.tractusx.productpass.models.passports.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import utils.exceptions.UtilException;

import java.io.File;
import java.nio.file.Path;

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
    public String savePassport(Passport passport, DataPlaneEndpoint endpointData, Boolean prettyPrint, Boolean encrypted){
        try {
            fileUtil.createDir(this.transferDir);
            String path = Path.of(this.transferDir, endpointData.getId() + ".json").toAbsolutePath().toString();
            return this.savePassport(passport, endpointData, prettyPrint, encrypted, path);
        }catch (Exception e){
            throw new UtilException(PassportUtil.class, e, "Something went wrong while creating the path and saving the passport for transfer ["+endpointData.getId()+"]");
        }
    }

    public String savePassport(Passport passport, DataPlaneEndpoint endpointData, Boolean prettyPrint, Boolean encrypted, String filePath){
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
