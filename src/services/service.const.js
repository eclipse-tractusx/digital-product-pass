const TWIN_REGISTRY_URL = "http://localhost:4243";
const AAS_PROXY_URL = "http://localhost:4245";
const MOCK_AUTH_URL = "https://mock--server.herokuapp.com";
const GOOGLE_CHART_API_URL = "https://chart.googleapis.com";
const DUMMY_SERVICE = "http://localhost:3000";
const SERVER_URL = "https://materialpass.int.demo.catena-x.net";
const BASE_URL = "https://semantics.int.demo.catena-x.net";

let INIT_OPTIONS = {};
let REDIRECT_URI = ""
// for production
if (window.location.href.includes(SERVER_URL)) {
    INIT_OPTIONS = {
        url: 'https://centralidp.demo.catena-x.net/auth/',
        clientId: 'Cl13-CX-Battery',
        realm: 'CX-Central',
        onLoad: 'login-required'
    };
    REDIRECT_URI = "https://materialpass.int.demo.catena-x.net/";
}
else {
    INIT_OPTIONS = {
        url: 'http://localhost:8088/auth/',
        clientId: 'Cl13-CX-Battery',
        realm: 'CX-Central',
        onLoad: 'login-required'
    };
    REDIRECT_URI = "http://localhost:8082/";
}

export {TWIN_REGISTRY_URL, AAS_PROXY_URL, MOCK_AUTH_URL, GOOGLE_CHART_API_URL, DUMMY_SERVICE, INIT_OPTIONS, REDIRECT_URI, BASE_URL, SERVER_URL};
