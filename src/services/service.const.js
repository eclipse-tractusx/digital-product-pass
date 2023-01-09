
const TWIN_REGISTRY_URL = "http://localhost:4243";
const AAS_PROXY_URL = "http://localhost:4245";
const MOCK_AUTH_URL = "https://mock--server.herokuapp.com";
const GOOGLE_CHART_API_URL = "https://chart.googleapis.com";
const DUMMY_SERVICE = "http://localhost:3000";
const SERVER_URL_INT = "https://materialpass.int.demo.catena-x.net";
const SERVER_URL_DEV = "https://materialpass.dev.demo.catena-x.net";
const CX_REGISTRY_URL_INT = "https://semantics.int.demo.catena-x.net";
const CX_REGISTRY_URL_DEV = "https://semantics.dev.demo.catena-x.net";
const IDP_URL_INT = "https://centralidp.int.demo.catena-x.net/auth/";
const IDP_URL_DEV = "https://centralidp.dev.demo.catena-x.net/auth/";
const API_KEY = "X_API_KEY";
const AAS_REGISTRY_CLIENT = 'VUE_APP_CLIENT_ID';
const AAS_REGISTRY_SECRET = 'VUE_APP_CLIENT_SECRET';

let SERVER_URL = "";
let INIT_OPTIONS = {};
let IDP_URL = "";
let REDIRECT_URI = "";
let CX_REGISTRY_URL = "";
let CLIENT_CREDENTIALS = {
  grant_type: 'client_credentials',
  client_id: AAS_REGISTRY_CLIENT,
  client_secret: AAS_REGISTRY_SECRET,
  scope: 'openid profile email'
};

if (window.location.href.includes("materialpass.int.demo.catena-x.net")) { // for integration
  {
    INIT_OPTIONS = {
      url: IDP_URL_INT,
      clientId: 'Cl13-CX-Battery',
      realm: 'CX-Central',
      onLoad: 'login-required'
    };
    REDIRECT_URI = SERVER_URL_INT;
    IDP_URL = IDP_URL_INT;
    CX_REGISTRY_URL = CX_REGISTRY_URL_INT;

  }
}
else if (window.location.href.includes("materialpass.dev.demo.catena-x.net")) { // for development
  {
    INIT_OPTIONS = {
      url: IDP_URL_DEV,
      clientId: 'Cl13-CX-Battery',
      realm: 'CX-Central',
      onLoad: 'login-required'
    };
    REDIRECT_URI = SERVER_URL_DEV;
    IDP_URL = IDP_URL_DEV;
    CX_REGISTRY_URL = CX_REGISTRY_URL_DEV;
  }
  
}
else { // for local run
  INIT_OPTIONS = {
    url: 'http://localhost:8088/auth/',
    clientId: 'Cl13-CX-Battery',
    realm: 'CX-Central',
    onLoad: 'login-required'
  };
  REDIRECT_URI = "http://localhost:8080/";
  SERVER_URL = SERVER_URL_INT; // this server url should come from DEV. Because DEV is not working at the moment, we use INT Server for testing purpose. Once, DEV is up and running, we change this to DEV.  
  CX_REGISTRY_URL = CX_REGISTRY_URL_INT;
}

export {TWIN_REGISTRY_URL, AAS_PROXY_URL, MOCK_AUTH_URL, GOOGLE_CHART_API_URL, DUMMY_SERVICE, INIT_OPTIONS, REDIRECT_URI, CX_REGISTRY_URL, SERVER_URL, API_KEY, CLIENT_CREDENTIALS, IDP_URL};

