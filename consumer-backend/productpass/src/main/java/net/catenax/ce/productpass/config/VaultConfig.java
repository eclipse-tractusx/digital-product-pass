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

package net.catenax.ce.productpass.config;

import net.catenax.ce.productpass.exceptions.ConfigException;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import tools.*;

import java.net.URI;
import java.util.Map;

@Configuration
public class VaultConfig extends AbstractVaultConfiguration {

    public static final configTools configuration = new configTools();
    private final String vaultUri = (String) configuration.getConfigurationParam("vault.uri", ".", null);
    public String dataDir;

    @Override
    public ClientAuthentication clientAuthentication() {
        try{
            this.dataDir = fileTools.createDataDir("VaultConfig");
            String filePath = vaultTools.createLocalVaultFile(true);
            Map<String, Object> content = yamlTools.readFile(filePath);
            String token = (String) content.get("token");
            if(stringTools.isEmpty(token)){
                logTools.printFatal("Please add the Vault token to ["+filePath+"] file, in order to start the application.");
                //throw new ConfigException("VaultConfig.clientAuthentication", "Token field is empty in ["+filePath+"], no token is set.");
                token = "00000000000-00000000000000000-00000000000";
            }
            return (TokenAuthentication) new TokenAuthentication(token);
        } catch (Exception e) {
            throw new ConfigException("VaultConfig.clientAuthentication", e, "It was not possible to set the ClientAuthentication Token");
        }
    }

    @Override
    public VaultEndpoint vaultEndpoint() {
        try {
            return VaultEndpoint.from(new URI(vaultUri));
        } catch (Exception e) {
            throw new ConfigException("VaultConfig.vaultEndpoint", e, "It was not possible to connect to the VaultEndpoint URI ["+this.vaultUri+"]");
        }
    }
}
