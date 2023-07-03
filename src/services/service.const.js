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

import numberUtil from "@/utils/numberUtil";

// Independentant Constants (If not defined will not crash the system)
const VERSION = "Local";

// Mandatory URLs
const IDP_URL = "https://centralidp.dev.demo.catena-x.net/auth/";

// Get urls that can be empty
let serverUrl = "http://localhost:8080";
let backendUrl = "https://materialpass.dev.demo.catena-x.net";
let passver = 'v3.0.1';
let retries = 'APP_API_MAX_RETRIES';
let timeout = 'APP_API_TIMEOUT';
let delay = 'APP_API_DELAY';
let commitId = "a36bcd0bb1d69bc735ac40a9ad6f4211eb4da19c";
let repoEndpoint = "https://github.com/eclipse-tractusx/digital-product-pass";


// Default values if the value is not specified
serverUrl = (serverUrl!= null && serverUrl !== "SERV"+"_"+"URL")?serverUrl:"https://materialpass.int.demo.catena-x.net"
backendUrl=(backendUrl != null && backendUrl !== "BACK"+"_"+"VER")?backendUrl:serverUrl
passver = (passver != null && passver !== "PASS"+"_"+"VER")?passver:"v3.0.1"
// Default Variables if value is not specified or is not a integer
timeout = numberUtil.parseInt(timeout, 60000);
delay = numberUtil.parseInt(delay, 2000);
retries = numberUtil.parseInt(retries, 5);

// Define constants
const SERVER_URL = serverUrl;
const BACKEND_URL = backendUrl;
const PASSPORT_VERSION = passver;
const API_MAX_RETRIES = retries;
const API_TIMEOUT = timeout;
const API_DELAY = delay;
const COMMIT_ID = commitId;
const REPO_ENDPOINT = repoEndpoint;

// Initialize configuration objects
let INIT_OPTIONS = {};
let REDIRECT_URI = "";

if (window.location.href.includes("localhost")){ //Modify credentials for local runs
  INIT_OPTIONS = {
    url: (IDP_URL != null && IDP_URL !== "IDENTITY"+"_"+"PROVIDER"+"_"+"URL")?IDP_URL:"http://localhost:8088/auth/", //Point to IDP service if specified or localh
    clientId: 'Cl13-CX-Battery', // Catena-X ClientId for Battery Pass
    realm: 'CX-Central', // Catena-X Realm
    onLoad: 'login-required'
  };
  REDIRECT_URI = "http://localhost:8080/"; 
}else{
  INIT_OPTIONS = {
    url: IDP_URL,
    clientId: 'Cl13-CX-Battery', // Catena-X ClientId for Battery Pass
    realm: 'CX-Central', // Catena-X Realm
    onLoad: 'login-required'
  };
  REDIRECT_URI = SERVER_URL;
}
// Export all the CONSTANTS and VARIABLES
export {INIT_OPTIONS, REDIRECT_URI, SERVER_URL, IDP_URL, BACKEND_URL, PASSPORT_VERSION, VERSION, API_TIMEOUT, API_DELAY, API_MAX_RETRIES, COMMIT_ID, REPO_ENDPOINT };

