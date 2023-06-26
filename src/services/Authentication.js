/**
 * Catena-X - Product Passport Consumer Frontend
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
 */

import { REDIRECT_URI, INIT_OPTIONS } from "@/services/service.const";
import Keycloak from 'keycloak-js';

export default class Authentication {
    constructor() {
      this.keycloak = new Keycloak(INIT_OPTIONS);
    }
    keycloakInit(app) {
      this.keycloak.init({ onLoad: INIT_OPTIONS.onLoad }).then((auth) => {
        if (!auth) {
          window.location.reload();
        }
        else {
          app.mount('#app');
        }
        //Token Refresh
        setInterval(() => {
          this.updateToken(60);
        }, 60000);

      }).catch((e) => {
        console.log(e);
        console.error("keycloakInit -> Login Failure");
      });
    }
    getAccessToken() {
      return this.keycloak.token;
    }

    getRefreshedToken() {
      return this.keycloak.refreshToken;
    }

    updateToken(minimumValidity) {
      this.keycloak.updateToken(minimumValidity).then((refreshed) => {
        if (refreshed) {
          console.info('Token refreshed' + refreshed);
        } else {
          console.warn('Token not refreshed, valid for '
                    + Math.round(this.keycloak.tokenParsed.exp + this.keycloak.timeSkew - new Date().getTime() / 1000) + ' seconds');
        }
      }).catch(() => {
        console.error("updateToken -> Failed to refresh token");
      });
    }

    isUserAuthenticated() {
      return this.keycloak.authenticated;
    }
    getClientId() {
      return this.keycloak.clientId;
    }
    decodeAccessToken() {
      return JSON.parse(window.atob(this.keycloak.token.split(".")[1]));
    }
    getUserName() {
      return this.decodeAccessToken().email;
    }
    getName() {
      return this.decodeAccessToken().name;
    }
    getSessionId() {
      return this.keycloak.sessionId;
    }
    getRole() {
      let clientRoles = '';
      clientRoles = this.keycloak.resourceAccess[this.getClientId()].roles;
      return clientRoles.length == 1 ? clientRoles[0] : clientRoles;
    }
    logout() {
      let logoutOptions = { redirectUri: REDIRECT_URI };
      this.keycloak.logout(logoutOptions).then((success) => {
        console.log("--> log: logout success ", success);
      }).catch((error) => {
        console.log("--> log: logout error ", error);
      });
    }
}
