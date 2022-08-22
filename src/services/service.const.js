const TWIN_REGISTRY_URL = "http://localhost:4243";
const AAS_PROXY_URL = "http://localhost:4245";
const MOCK_AUTH_URL = "https://mock--server.herokuapp.com";
const GOOGLE_CHART_API_URL = "https://chart.googleapis.com";
const DUMMY_SERVICE = "http://localhost:3000";
const SERVER_URL = "https://materialpass.int.demo.catena-x.net";

let INIT_OPTIONS = {};
let REDIRECT_URI = "";
if (window.location.href.includes(SERVER_URL)) {
    // production mode start
    INIT_OPTIONS = {
        url: 'https://centralidp.demo.catena-x.net/auth/',
        clientId: 'Cl13-CX-Battery',
        realm: 'CX-Central',
        onLoad: 'login-required'
    };
    REDIRECT_URI = "https://materialpass.int.demo.catena-x.net/";
    // production mode end
}
else {
    // development mode start
    INIT_OPTIONS = {
        url: 'http://localhost:8088/auth/',
        clientId: 'Cl13-CX-Battery',
        realm: 'CX-Central',
        onLoad: 'login-required'
    };
    REDIRECT_URI = "http://localhost:8080/";
    // development end
}
export { TWIN_REGISTRY_URL, AAS_PROXY_URL, MOCK_AUTH_URL, GOOGLE_CHART_API_URL, DUMMY_SERVICE, INIT_OPTIONS, REDIRECT_URI, SERVER_URL };
