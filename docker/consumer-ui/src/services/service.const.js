import Keycloak from 'keycloak-js'

const TWIN_REGISTRY_URL = "http://localhost:4243";
const AAS_PROXY_URL = "http://localhost:4245";
const MOCK_AUTH_URL = "https://mock--server.herokuapp.com";
const GOOGLE_CHART_API_URL = "https://chart.googleapis.com";
const DUMMY_SERVICE = "http://localhost:3000";

const INIT_OPTIONS = {
    url: "https://centralidp.demo.catena-x.net/auth/",
    clientId: "C0l13-CX-Battery",
    realm: "CX-Central",
    onLoad: "login-required"
};

const REDIRECT_URI = "https://centralidp.demo.catena-x.net/";

const KEYCLOAK = new Keycloak(INIT_OPTIONS)


export {TWIN_REGISTRY_URL, AAS_PROXY_URL, MOCK_AUTH_URL, GOOGLE_CHART_API_URL, DUMMY_SERVICE, INIT_OPTIONS, REDIRECT_URI, KEYCLOAK};
