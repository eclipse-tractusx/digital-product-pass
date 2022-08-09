import { KEYCLOAK, REDIRECT_URI } from "@/services/service.const"

export default class authentication {

    getKeycloakInstance() {
        //TODO
    }
    getAccessToken() {
        return KEYCLOAK.token;
    }

    getRefreshedToken() {
        return KEYCLOAK.refreshToken;
    }

    updateToken(minimumValidity) {
        KEYCLOAK.updateToken(minimumValidity).then((refreshed) => {
            if (refreshed) {
                console.info('Token refreshed' + refreshed);
            } else {
                console.warn('Token not refreshed, valid for '
                    + Math.round(KEYCLOAK.tokenParsed.exp + KEYCLOAK.timeSkew - new Date().getTime() / 1000) + ' seconds');
            }
        }).catch(() => {
            console.error('Failed to refresh token');
        });
    }

    isUserAuthenticated() {
        return KEYCLOAK.authenticated;
    }
    getClientId() {
        return KEYCLOAK.clientId;
    }
    decodeAccessToken() {
        return JSON.parse(window.atob(KEYCLOAK.token.split(".")[1]));
    }
    getUserName() {
        return this.decodeAccessToken().preferred_username;
    }
    getRole() {
        let clientId = this.getClientId();
        let clientRoles = this.decodeAccessToken().resource_access[clientId].roles;
        return clientRoles.length == 1 ? clientRoles[0] : clientRoles;
    }
    logout() {
        var logoutOptions = { redirectUri: REDIRECT_URI };

        KEYCLOAK.logout(logoutOptions).then((success) => {
            console.log("--> log: logout success ", success);
        }).catch((error) => {
            console.log("--> log: logout error ", error);
        });

    }
}


