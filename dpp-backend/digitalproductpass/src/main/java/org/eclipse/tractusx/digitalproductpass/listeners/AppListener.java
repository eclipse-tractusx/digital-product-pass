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

package org.eclipse.tractusx.digitalproductpass.listeners;

import org.eclipse.tractusx.digitalproductpass.config.DtrConfig;
import org.eclipse.tractusx.digitalproductpass.config.SecurityConfig;
import org.eclipse.tractusx.digitalproductpass.models.auth.JwtToken;
import org.eclipse.tractusx.digitalproductpass.models.catenax.Discovery;
import org.eclipse.tractusx.digitalproductpass.models.edc.Jwt;
import org.eclipse.tractusx.digitalproductpass.services.AuthenticationService;
import org.eclipse.tractusx.digitalproductpass.services.CatenaXService;
import org.eclipse.tractusx.digitalproductpass.services.DataTransferService;
import org.eclipse.tractusx.digitalproductpass.services.VaultService;
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
import utils.HttpUtil;
import utils.LogUtil;

/**
 * This class consists exclusively of methods to operate on the Application's
 * Event listeners.
 *
 * <p>
 * The methods defined here are the event listeners needed to run the
 * application.
 */
@Component
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class AppListener {

    /**
     * ATTRIBUTES
     **/
    @Autowired
    BuildProperties buildProperties;
    @Autowired
    CatenaXService catenaXService;
    @Autowired
    SecurityConfig securityConfig;
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

    /**
     * METHODS
     **/
    @EventListener(ApplicationStartedEvent.class)
    public void started() {
        try {
            SecurityConfig.StartUpCheckConfig startUpConfig = securityConfig.getStartUpChecks();
            Boolean bpnCheck = startUpConfig.getBpnCheck();
            Boolean edcCheck = startUpConfig.getEdcCheck();

            if (bpnCheck || edcCheck) {
                try {
                    LogUtil.printMessage("========= [ EXECUTING PRE-CHECKS ] ================================");
                    String participantId = (String) vaultService.getLocalSecret("edc.participantId");
                    if (participantId.isEmpty()) {
                        throw new Exception("[" + this.getClass().getName()
                                + ".onStartUp] ParticipantId configuration does not exists in Vault File!");
                    }
                    if (edcCheck) {
                        try {
                            LogUtil.printMessage(
                                    "[ EDC Connection Test ] Testing connection with the EDC Consumer, this may take some seconds...");
                            Boolean healthStatus = dataTransferService.checkEdcConsumerConnection();
                            if(!healthStatus){
                                throw new Exception("[" + this.getClass().getName()
                                        + ".onStartUp] The EDC consumer endpoint configured is not ready to receive requests!");
                            }
                            LogUtil.printMessage(
                                    "[ EDC Connection Test ] EDC consumer is ready and accessible!");
                            if(bpnCheck) {
                                if (!dataTransferService.isApplicationEdc(participantId)) {
                                    throw new Exception("[" + this.getClass().getName()
                                            + ".onStartUp] Incorrect BPN Number configuration, expected the same participant id as the EDC consumer!");
                                }
                                LogUtil.printMessage(
                                        "[ EDC Connection Test ] The EDC Business Partner Number is the same as the Digital Product Pass Application one!");
                            }
                            LogUtil.printMessage(
                                    "[ EDC Connection Test ] The EDC consumer checks concluded successfully!");
                        } catch (Exception e) {
                            throw new IncompatibleConfigurationException(e.getMessage());
                        }
                    }
                    if (bpnCheck) {
                        try {
                            LogUtil.printMessage("[ BPN Number Check ] Checking the token from the technical user...");
                            JwtToken token = authService.getToken();
                            if (token == null) {
                                throw new Exception("[" + this.getClass().getName()
                                        + ".onStartUp] Not possible to get technical user credentials!");
                            }
                            Jwt jwtToken = httpUtil.parseToken(token.getAccessToken());
                            if (jwtToken == null) {
                                throw new Exception("[" + this.getClass().getName()
                                        + ".onStartUp] The technical user JwtToken is empty!");
                            }
                            if (!jwtToken.getPayload().containsKey("bpn")) {
                                throw new Exception("[" + this.getClass().getName()
                                        + ".onStartUp] The technical user JwtToken does not specify any BPN number!");
                            }
                            String techUserBpn = (String) jwtToken.getPayload().get("bpn");
                            if (!techUserBpn.equals(participantId)) {
                                throw new Exception("[" + this.getClass().getName()
                                        + ".onStartUp] The technical user does not has the same BPN number as the EDC Consumer and the Backend! Access not allowed!");
                            }
                            LogUtil.printMessage(
                                    "[ BPN Number Check ] Technical User BPN matches the EDC Consumer and the Backend participantId!");
                        } catch (Exception e) {
                            throw new IncompatibleConfigurationException(e.getMessage());
                        }
                    }
                    LogUtil.printMessage("========= [ PRE-CHECKS COMPLETED ] ================================");
                } catch (Exception e) {
                    throw new IncompatibleConfigurationException(e.getMessage());
                }
            }
            SecurityConfig.AuthorizationConfig authorizationConfig = securityConfig.getAuthorization();
            Boolean bpnAuth = authorizationConfig.getBpnAuth();
            Boolean roleAuth = authorizationConfig.getRoleAuth();

            if (!bpnAuth && !roleAuth) {
                return;
            }

            LogUtil.printMessage("========= [ EXECUTING AUTHORIZATION PRE-CHECKS ] ================================");
            if (bpnAuth) {
                String participantId = (String) vaultService.getLocalSecret("edc.participantId");
                if (participantId.isEmpty()) {
                    throw new Exception("[" + this.getClass().getName()
                            + ".onStartUp] ParticipantId configuration does not exists in Vault File!");
                }
                LogUtil.printMessage("[ BPN AUTHORIZATION CHECK ] The following bpn [ " + participantId
                        + " ] is required in authenticated tokens");
            }
            if (roleAuth) {
                String appId = (String) vaultService.getLocalSecret("appId");
                if (appId.isEmpty()) {
                    throw new Exception("[" + this.getClass().getName()
                            + ".onStartUp] The appId configuration does not exists in Vault File!");
                }
                LogUtil.printMessage(
                        "[ ROLE AUTHORIZATION CHECK ] The authenticated tokens in requests shall contain roles within this appId [ "
                                + appId + " ]");
            }
            LogUtil.printMessage("========= [ AUTHORIZATION PRE-CHECKS COMPLETED ] ================================");
        } catch (Exception e) {
            throw new IncompatibleConfigurationException(e.getMessage());
        }

    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartUp() {
        String ascii = "\n" +
                "    ____  _       _ __        __   ____                 __           __     ____                     \n"
                +
                "   / __ \\(_)___ _(_) /_____ _/ /  / __ \\_________  ____/ /_  _______/ /_   / __ \\____ ___________    \n"
                +
                "  / / / / / __ `/ / __/ __ `/ /  / /_/ / ___/ __ \\/ __  / / / / ___/ __/  / /_/ / __ `/ ___/ ___/    \n"
                +
                " / /_/ / / /_/ / / /_/ /_/ / /  / ____/ /  / /_/ / /_/ / /_/ / /__/ /_   / ____/ /_/ (__  |__  )     \n"
                +
                "/_____/_/\\__, /_/\\__/\\__,_/_/  /_/   /_/   \\____/\\__,_/\\__,_/\\___/\\__/  /_/    \\__,_/____/____/      \n"
                +
                "        /____/                                                                                       \n"
                +
                "                                                                                \\\\/ersion: v"
                + buildProperties.getVersion() + "\n\n";
        System.out.print(ascii);
        String serverStartUpMessage = "\n\n" +
                "**********************************************************************\n\n" +
                " " + buildProperties.getName() + "\n" +
                " Copyright (c) 2022, 2024: BMW AG, Henkel AG & Co. KGaA\n" +
                " Copyright (c) 2023, 2024: CGI Deutschland B.V. & Co. KG\n" +
                " Copyright (c) 2022, 2024: Contributors to the Eclipse Foundation.\n\n" +
                "**********************************************************************\n\n";
        System.out.print(serverStartUpMessage);
        System.out.print("\n========= [ APPLICATION STARTED ] ====================================\n" +
                "Listening to requests...\n\n");
        Discovery discovery = catenaXService.start(); // Start the CatenaX service (we need the bpnDiscovery and
        // edcDiscovery addresses)
        if (discovery == null) {
            LogUtil.printError(
                    "\n*************************************[CRITICAL ERROR]*************************************" +
                            "\nIt was not possible to start the application correctly..." +
                            "\nPlease configure the Discovery Service Endpoint property:" +
                            "\n\t- [backend.discovery.hostname]" +
                            "\nMake sure that the Keycloak App Id is available" +
                            "\n\t- [oauth.appId]" +
                            "\nMake sure that the Technical User Credentials are correctly configured:" +
                            "\n\t- [oauth.techUser.clientId]" +
                            "\n\t- [oauth.techUser.clientSecret]" +
                            "\nThis user should be able to retrieve the token from the following Keycloak Endpoint:" +
                            "\n\t- [backend.configuration.tokenUrl]" +
                            "\n\t- [backend.configuration.userInfoUrl]" +
                            "\n*****************************************************************************************\n");
        }

    }
}
