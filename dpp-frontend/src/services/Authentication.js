/**
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
 */

import { REDIRECT_URI, INIT_OPTIONS, BPN_CHECK, BPN, ROLE_CHECK } from "@/services/service.const";
import Keycloak from "keycloak-js";
import authUtil from "@/utils/authUtil";
import jsonUtil from "@/utils/jsonUtil";
export default class Authentication {
    constructor() {
        this.keycloak = new Keycloak(INIT_OPTIONS);
    }
    isAuthorized(parsedToken) {
        if (!BPN_CHECK && !ROLE_CHECK) {
            return true;
        }

        if (!parsedToken) {
            return false;
        }

        // Get conditions for authorization
        let bpnAuthorized = authUtil.checkBpn(parsedToken, BPN);
        let roleAuthorized = this.hasRoles();

        // Authorize according to configuration
        if (BPN_CHECK && ROLE_CHECK && bpnAuthorized && roleAuthorized) {
            // In case both a valid everything needs to be true
            return true;
        } else if (!BPN_CHECK && ROLE_CHECK && roleAuthorized) {
            // In case just the role is valid just the role needs to be true
            return true;
        } else if (BPN_CHECK && !ROLE_CHECK && bpnAuthorized) {
            // In case the
            return true;
        }

        return false;
    }
    keycloakInit(app) {
        var authProperties = app.config.globalProperties.$authProperties;
        this.keycloak
            .init({ onLoad: INIT_OPTIONS.onLoad })
            .then((auth) => {
                if (!auth) {
                    window.location.reload();
                } else {
                    // Check if the refresh token is valid and authenticated
                    if (this.keycloak.tokenParsed) {
                        authProperties.loginReachable = true;
                    }
                    authProperties.isAuthorized = this.isAuthorized(this.keycloak.tokenParsed);
                }

                app.config.globalProperties.$authProperties = authProperties;
                app.mount("#app");
                //Token Refresh
                setInterval(() => {
                    this.updateToken(60, app);
                }, 60000);
            })
            .catch((e) => {
                console.log(e);
                authProperties.loginReachable = false;
                authProperties.isAuthorized = false;
                app.config.globalProperties.$authProperties = authProperties;
                app.mount("#app");
            });
    }
    getAccessToken() {
        return this.keycloak.token;
    }

    getRefreshedToken() {
        return this.keycloak.refreshToken;
    }

    updateToken(minimumValidity, app) {
        this.keycloak
            .updateToken(minimumValidity)
            .then((refreshed) => {
                if (refreshed) {
                    // Check if the refresh token is valid and authenticated
                    if (this.keycloak.parsedToken) {
                        app.config.globalProperties.$authProperties.loginReachable = true;
                    } else {
                        app.config.globalProperties.$authProperties.loginReachable = false;
                    }
                    app.config.globalProperties.$authProperties.isAuthorized = this.isAuthorized(
                        this.keycloak.parsedToken
                    );
                    console.info("Token refreshed " + refreshed);
                } else {
                    console.warn(
                        "Token not refreshed, valid for " +
                            Math.round(
                                this.keycloak.tokenParsed.exp + this.keycloak.timeSkew - new Date().getTime() / 1000
                            ) +
                            " seconds"
                    );
                }
            })
            .catch(() => {
                console.error("updateToken -> Failed to refresh token");
            });
    }

    isUserAuthenticated() {
        return this.keycloak.authenticated;
    }
    getClientId() {
        return this.keycloak.clientId;
    }
    getUserName() {
        return this.keycloak.tokenParsed.email;
    }
    getName() {
        return this.keycloak.tokenParsed.name;
    }
    getBpn() {
        return this.keycloak.tokenParsed.bpn;
    }
    getSessionId() {
        return this.keycloak.sessionId;
    }
    hasRoles() {
        try {
            let clientId = this.getClientId();
            if (!clientId || !this.keycloak.resourceAccess) {
                return false;
            }

            if (!jsonUtil.exists(clientId, this.keycloak.resourceAccess)) {
                return false;
            }

            let appIdResource = jsonUtil.get(clientId, this.keycloak.resourceAccess, ".", null);
            if (appIdResource == null || !jsonUtil.exists("roles", appIdResource)) {
                return false;
            }

            let roleList = jsonUtil.get("roles", appIdResource, ".", null);
            if (roleList == null) {
                return false;
            }
            return roleList.length > 0;
        } catch (e) {
            return false;
        }
    }
    getRole() {
        let clientRoles = "";
        let clientId = this.getClientId();
        if (this.hasRoles()) {
            clientRoles = this.keycloak.resourceAccess[clientId].roles;
        }
        return clientRoles.length == 1 ? clientRoles[0] : clientRoles;
    }
    logout() {
        let logoutOptions = { redirectUri: REDIRECT_URI };
        this.keycloak
            .logout(logoutOptions)
            .then((success) => {
                console.log("--> log: logout success ", success);
            })
            .catch((error) => {
                console.log("--> log: logout error ", error);
            });
    }
}
