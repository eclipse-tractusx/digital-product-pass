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

package org.eclipse.tractusx.productpass.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import utils.ConfigUtil;
import utils.LogUtil;

@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AppListener {
    @Autowired
    BuildProperties buildProperties;

    public static final ConfigUtil configuration = new ConfigUtil();

    public final String tokenUri = (String) configuration.getConfigurationParam("keycloak.tokenUri", ".", null);

    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() {
        String serverStartUpMessage = "\n\n" +
                "************************************************\n" +
                buildProperties.getName()+"\n" +
                "Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA\n" +
                "Copyright (c) 2022, 2023: Contributors to the CatenaX (ng) GitHub Organisation.\n" +
                "Version: "+ buildProperties.getVersion()  + "\n\n" +
                 "\n\n-------------> [ SERVER STARTED ] <-------------\n" +
                "Listening to requests...\n\n";

        LogUtil.printMessage(serverStartUpMessage);
        LogUtil.printMessage(tokenUri);
        LogUtil.printMessage("[ LOGGING STARTED ] <-----------------------------------------");
        LogUtil.printMessage("Creating log file...");

       }

}
