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

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.tractusx.productpass.Application;
import org.eclipse.tractusx.productpass.exceptions.DataModelException;
import org.eclipse.tractusx.productpass.http.controllers.api.ContractController;
import org.eclipse.tractusx.productpass.config.DiscoveryConfig;
import org.eclipse.tractusx.productpass.config.DtrConfig;
import org.eclipse.tractusx.productpass.managers.ProcessDataModel;
import org.eclipse.tractusx.productpass.managers.ProcessManager;
import org.eclipse.tractusx.productpass.models.auth.JwtToken;
import org.eclipse.tractusx.productpass.models.catenax.Dtr;
import org.eclipse.tractusx.productpass.models.edc.Jwt;
import org.eclipse.tractusx.productpass.models.http.requests.Search;
import org.eclipse.tractusx.productpass.services.AuthenticationService;
import org.eclipse.tractusx.productpass.services.DataTransferService;
import org.eclipse.tractusx.productpass.services.VaultService;
import org.eclipse.tractusx.productpass.models.catenax.BpnDiscovery;
import org.eclipse.tractusx.productpass.models.catenax.Discovery;
import org.eclipse.tractusx.productpass.models.catenax.EdcDiscoveryEndpoint;
import org.eclipse.tractusx.productpass.services.CatenaXService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.IncompatibleConfigurationException;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import utils.HttpUtil;
import utils.JsonUtil;
import utils.LogUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AppListener {
    @Autowired
    BuildProperties buildProperties;
    @Autowired
    JsonUtil jsonUtil;

    @Autowired
    DiscoveryConfig discoveryConfig;
    @Autowired
    CatenaXService catenaXService;

    @Autowired
    DtrConfig dtrConfig;
    @Autowired
    AuthenticationService authService;
    @Autowired
    VaultService vaultService;

    @Autowired
    HttpUtil httpUtil;
    @Autowired
    Environment env;
    @Autowired
    DataTransferService dataTransferService;

    @EventListener(ApplicationStartedEvent.class)
    public void started() {
        Boolean preChecks = env.getProperty("configuration.security.check.enabled", Boolean.class, true);
        if (!preChecks) {
            return;
        }

        Boolean bpnCheck = env.getProperty("configuration.security.check.bpn", Boolean.class, true);
        Boolean edcCheck = env.getProperty("configuration.security.check.edc", Boolean.class, true);
        if (!bpnCheck && !edcCheck) {
            return;
        }
        try {
            LogUtil.printMessage("========= [ EXECUTING PRE-CHECKS ] ================================");
            String participantId = (String) vaultService.getLocalSecret("edc.participantId");
            if (participantId.isEmpty()) {
                throw new Exception("[" + this.getClass().getName() + ".onStartUp] ParticipantId configuration does not exists in Vault File!");
            }
            if (edcCheck) {
                try {
                    LogUtil.printMessage("[ EDC Connection Test ] Testing connection with the EDC Consumer, this may take some seconds...");
                    String bpnNumber = dataTransferService.checkEdcConsumerConnection();
                    if (!participantId.equals(bpnNumber)) {
                        throw new Exception("[" + this.getClass().getName() + ".onStartUp] Incorrect BPN Number configuration, expected the same participant id as the EDC consumer connector!");
                    }
                    LogUtil.printMessage("[ EDC Connection Test ] The EDC consumer is available for receiving connections!");
                } catch (Exception e) {
                    throw new IncompatibleConfigurationException(e.getMessage());
                }
            }
            if (!bpnCheck) {
                return;
            }
            try {
                LogUtil.printMessage("[ BPN Number Check ] Checking the token from the technical user...");
                JwtToken token = authService.getToken();
                if (token == null) {
                    throw new Exception("[" + this.getClass().getName() + ".onStartUp] Not possible to get technical user credentials!");
                }
                Jwt jwtToken = httpUtil.parseToken(token.getAccessToken());
                if (jwtToken == null) {
                    throw new Exception("[" + this.getClass().getName() + ".onStartUp] The technical user JwtToken is empty!");
                }
                if (!jwtToken.getPayload().containsKey("bpn")) {
                    throw new Exception("[" + this.getClass().getName() + ".onStartUp] The technical user JwtToken does not specify any BPN number!");
                }
                String techUserBpn = (String) jwtToken.getPayload().get("bpn");
                if (!techUserBpn.equals(participantId)) {
                    throw new Exception("[" + this.getClass().getName() + ".onStartUp] The technical user does not has the same BPN number as the EDC Consumer and the Backend! Access not allowed!");
                }
                LogUtil.printMessage("[ BPN Number Check ] Technical User BPN matches the EDC Consumer and the Backend participantId!");
            } catch (Exception e) {
                throw new IncompatibleConfigurationException(e.getMessage());
            }
        } catch (Exception e) {
            throw new IncompatibleConfigurationException(e.getMessage());
        }

    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() {
        LogUtil.printMessage("========= [ APPLICATION STARTED ] =================================");
        String serverStartUpMessage = "\n\n" +
                "************************************************\n" +
                buildProperties.getName() + "\n" +
                "Copyright (c) 2022, 2023: BASF SE, BMW AG, Henkel AG & Co. KGaA\n" +
                "Copyright (c) 2022, 2023: Contributors to the CatenaX (ng) GitHub Organisation.\n" +
                "Version: " + buildProperties.getVersion() + "\n\n" +
                "\n\n-------------> [ SERVER STARTED ] <-------------\n" +
                "Listening to requests...\n\n";

        LogUtil.printMessage(serverStartUpMessage);
        LogUtil.printMessage("========= [ LOGGING STARTED ] ================================");
        LogUtil.printMessage("Creating log file...");
        if(!dtrConfig.getCentral()) {
            Discovery discovery = catenaXService.start(); // Start the CatenaX service if the central attribute is set to false (we need the bpnDiscovery and edcDiscovery addresses)
            if (discovery == null) {
                LogUtil.printError("\n*************************************[CRITICAL ERROR]*************************************" +
                        "\nIt was not possible to start the application correctly..." +
                        "\nPlease configure the Discovery Service Endpoint property:" +
                        "\n\t- [application.configuration.discovery.endpoint]" +
                        "\nMake sure that the Technical User Credentials are correctly configured:" +
                        "\n\t- [avp.helm.clientId]" +
                        "\n\t- [avp.helm.clientSecret]" +
                        "\nThis user should be able to retrieve the token from the following Keycloak Endpoint:" +
                        "\n\t- [application.configuration.keycloak.tokenUri]" +
                        "\n*****************************************************************************************\n"
                );
            }
        }
       }

        // Store the process manager in memory
    }
