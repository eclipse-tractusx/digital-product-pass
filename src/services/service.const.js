const TWIN_REGISTRY_URL = "http://localhost:4243";
const AAS_PROXY_URL = "http://localhost:4245";
const MOCK_AUTH_URL = "https://mock--server.herokuapp.com";
const GOOGLE_CHART_API_URL = "https://chart.googleapis.com";
const DUMMY_SERVICE = "http://localhost:3000";

const BASE_URL = "https://semantics.int.demo.catena-x.net";


// for production 
const INIT_OPTIONS = {
    url: 'https://centralidp.demo.catena-x.net/auth/',
    clientId: 'Cl4-CX-DigitalTwin',
    realm: 'CX-Central',
    onLoad: 'login-required'
};

const REDIRECT_URI = "https://materialpass.int.demo.catena-x.net/";

// for development use only
// const INIT_OPTIONS = {
//     url: 'http://localhost:8088/auth/',
//     clientId: 'Cl13-CX-Battery',
//     realm: 'CX-Central',
//     onLoad: 'login-required'
// };
// const REDIRECT_URI = "http://localhost:8082/";




export {TWIN_REGISTRY_URL, AAS_PROXY_URL, MOCK_AUTH_URL, GOOGLE_CHART_API_URL, DUMMY_SERVICE, INIT_OPTIONS, REDIRECT_URI, BASE_URL};
