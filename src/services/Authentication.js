/**
 * Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { REDIRECT_URI, INIT_OPTIONS, CLIENT_CREDENTIALS, IDP_URL } from "@/services/service.const";
import Keycloak from 'keycloak-js';
import axios from "axios";
import store from '../store/index';
//import BackendService from "@/services/BackendService";


export default class Authentication {
    keycloak;
    constructor() {
      this.keycloak = new Keycloak(INIT_OPTIONS);
    }
    keycloakInit(app) {
      // if((BACKEND === 'true') || (BACKEND == true)) {
      //   BackendService.login();
      // }
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
      // if((BACKEND === 'true') || (BACKEND == true)) {
      //   return BackendService.logout(REDIRECT_URI);
      // }
      this.keycloak.logout(logoutOptions).then((success) => {
        console.log("--> log: logout success ", success);
      }).catch((error) => {
        console.log("--> log: logout error ", error);
      });
    }
    /***** Technical User Authentication *****/

    getAuthTokenForTechnicalUser() {

      // encrypt client credentials using keycloak session id to access in a safe manner// 
      store.commit("setSessionId", this.getSessionId());
      store.commit("setClientId", CLIENT_CREDENTIALS.client_id);
      store.commit("setClientSecret", CLIENT_CREDENTIALS.client_secret);

      const params = new URLSearchParams({

        grant_type: CLIENT_CREDENTIALS.grant_type,
        client_id: store.getters.getClientId,
        client_secret: store.getters.getClientSecret,
        scope: CLIENT_CREDENTIALS.scope
      });

      return new Promise((resolve) => {
        axios({

          method: 'post',
          url: IDP_URL + 'realms/CX-Central/protocol/openid-connect/token',
          data: params.toString(),
          config: {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
          }

        }).then(response => {

          resolve(response.data.access_token);

        }).catch(error => {

          console.error("getAuthTokenForTechnicalUser -> " + error);
          resolve("rejected");

        });
      });
    }
}
