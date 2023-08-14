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


import numberUtil from "@/utils/numberUtil";

// Independentant Constants (If not defined will not crash the system)
const VERSION = "APP_VERSION";

// Mandatory URLs
const IDP_URL = "IDENTITY_PROVIDER_URL";

// Get urls that can be empty
let serverUrl = "HOST_URL";
let backendUrl = "DATA_URL";
let passVer = 'PASS_VERSION';
let retries = 'APP_API_MAX_RETRIES';
let timeout = 'APP_API_TIMEOUT';
let delay = 'APP_API_DELAY';
let clientId = "KEYCLOAK_CLIENTID";
let realm = "KEYCLOAK_REALM";
let onLoad = "KEYCLOAK_ONLOAD";
let commitId = "REPO_COMMIT_ID";
let repoEndpoint = "REPO_ENDPOINT_URL";

// Default values if the value is not specified
serverUrl = (serverUrl != null && serverUrl !== "") ? serverUrl : "https://materialpass.int.demo.catena-x.net"
backendUrl = (backendUrl != null && backendUrl !== "")  ? backendUrl : serverUrl
passVer = (passVer != null && passVer !== "") ? passVer : "v3.0.1"
clientId = (clientId != null && clientId !== "") ? clientId : "Cl13-CX-Battery"
realm = (realm != null && realm !== "") ? realm : "CX-Central"
onLoad = (onLoad != null && onLoad !== "") ? onLoad : "login-required"

// Default Variables if value is not specified or is not a integer
timeout = numberUtil.parseInt(timeout, 60000);
delay = numberUtil.parseInt(delay, 1000);
retries = numberUtil.parseInt(retries, 20);

// Define constants
const SERVER_URL = serverUrl;
const BACKEND_URL = backendUrl;
const PASSPORT_VERSION = passVer;
const API_MAX_RETRIES = retries;
const API_TIMEOUT = timeout;
const API_DELAY = delay;
const CLIENT_ID = clientId;
const REALM = realm;
const ONLOAD = onLoad;
const COMMIT_ID = commitId;
const REPO_ENDPOINT = repoEndpoint;

// Initialize configuration objects
let INIT_OPTIONS = {
  url: null,
  clientId: CLIENT_ID, // Catena-X ClientId for Battery Pass
  realm: REALM, // Catena-X Realm
  onLoad: ONLOAD
};
let REDIRECT_URI = "";

if (window.location.href.includes("localhost")) { //Modify credentials for local runs
  INIT_OPTIONS["url"] = (IDP_URL != null) ? IDP_URL : "http://localhost:8088/auth/", //Point to IDP service if specified or localhost
  REDIRECT_URI = "http://localhost:8080/";
} else {
  INIT_OPTIONS["url"] = IDP_URL;
  REDIRECT_URI = SERVER_URL;
}
// Export all the CONSTANTS and VARIABLES
export { INIT_OPTIONS, REDIRECT_URI, SERVER_URL, IDP_URL, BACKEND_URL, PASSPORT_VERSION, VERSION, API_TIMEOUT, API_DELAY, API_MAX_RETRIES, COMMIT_ID, REPO_ENDPOINT };