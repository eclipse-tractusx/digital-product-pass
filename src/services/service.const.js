
const TWIN_REGISTRY_URL = "http://localhost:4243";
const AAS_PROXY_URL = "http://localhost:4245";
const MOCK_AUTH_URL = "https://mock--server.herokuapp.com";
const GOOGLE_CHART_API_URL = "https://chart.googleapis.com";
const DUMMY_SERVICE = "http://localhost:3000";
const INT_SERVER_URL = "https://materialpass.int.demo.catena-x.net";
const DEV_SERVER_URL = "https://materialpass.dev.demo.catena-x.net";
const CX_REGISTRY_URL = "https://semantics.int.demo.catena-x.net";
const IDP_URL = "https://centralidp.int.demo.catena-x.net/auth/";
const API_KEY = "X_API_KEY";
const AAS_REGISTRY_CLIENT = 'VUE_APP_CLIENT_ID';
const AAS_REGISTRY_SECRET = 'VUE_APP_CLIENT_SECRET';

let SERVER_URL = "";
let INIT_OPTIONS = {};
let REDIRECT_URI = "";
let CLIENT_CREDENTIALS = {
  grant_type: 'client_credentials',
  client_id: AAS_REGISTRY_CLIENT,
  client_secret: AAS_REGISTRY_SECRET,
  scope: 'openid profile email'
};
INIT_OPTIONS = {
  url: IDP_URL,
  clientId: 'Cl13-CX-Battery',
  realm: 'CX-Central',
  onLoad: 'login-required'
};
if (window.location.href.includes("materialpass.int.demo.catena-x.net")) { // for integration
  REDIRECT_URI = INT_SERVER_URL;
}
else if (window.location.href.includes("materialpass.dev.demo.catena-x.net")) { // for development
  REDIRECT_URI = DEV_SERVER_URL;
}
else { // for local run
  INIT_OPTIONS = {
    url: 'http://localhost:8088/auth/',
    clientId: 'Cl13-CX-Battery',
    realm: 'CX-Central',
    onLoad: 'login-required'
  };
  REDIRECT_URI = "http://localhost:8080/";
  SERVER_URL = INT_SERVER_URL; // this server url should come from DEV. Because DEV is not working at the moment, we use INT Server for testing purpose. Once, DEV is up and running, we change this to DEV.  
}

export {TWIN_REGISTRY_URL, AAS_PROXY_URL, MOCK_AUTH_URL, GOOGLE_CHART_API_URL, DUMMY_SERVICE, INIT_OPTIONS, REDIRECT_URI, CX_REGISTRY_URL, SERVER_URL, API_KEY, CLIENT_CREDENTIALS, IDP_URL};

