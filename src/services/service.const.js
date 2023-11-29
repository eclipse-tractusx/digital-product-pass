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
const IDP_URL = "https://centralidp.int.demo.catena-x.net/auth/";

// Get urls that can be empty
let serverUrl = "http://localhost:8080";
let backendUrl = "DATA_URL";
let retries = 'APP_API_MAX_RETRIES';
let timeout = 'APP_API_TIMEOUT';
let irsDelay = 'APP_IRS_DELAY';
let irsMaxWaitingTime = 'APP_IRS_WAITING_TIME'; 
let delay = 'APP_API_DELAY';
let commitId = "REPO_COMMIT_ID";
let repoEndpoint = "REPO_ENDPOINT_URL";
let bpnCheck = "AUTH_BPN_CHECK";
let bpn = "AUTH_BPN_NUMBER";
let portalUrl = "APP_PORTAL_URL";
let adminEmail = "APP_ADMIN_EMAIL";

// Default values if the value is not specified
serverUrl = (serverUrl != null && serverUrl !== "") ? serverUrl : "https://materialpass.int.demo.catena-x.net"
backendUrl = (backendUrl != null && backendUrl !== "")  ? backendUrl : serverUrl
adminEmail = (adminEmail != null && adminEmail !== "") ? adminEmail : "admin@example.com"
portalUrl = (portalUrl != null && portalUrl !== "") ? portalUrl : "https://portal.int.demo.catena-x.net"
bpnCheck = (bpnCheck === "true")


// Default Variables if value is not specified or is not a integer
timeout = numberUtil.parseInt(timeout, 60000);
delay = numberUtil.parseInt(delay, 1000);
retries = numberUtil.parseInt(retries, 20);
irsDelay = numberUtil.parseInt(irsDelay, 30000);
irsMaxWaitingTime = numberUtil.parseInt(irsMaxWaitingTime, 30);

// Define constants
const SERVER_URL = serverUrl;
const BACKEND_URL = backendUrl;
const API_MAX_RETRIES = retries;
const API_TIMEOUT = timeout;
const IRS_DELAY = irsDelay;
const IRS_MAX_WAITING_TIME = irsMaxWaitingTime;
const API_DELAY = delay;
const COMMIT_ID = commitId;
const REPO_ENDPOINT = repoEndpoint;
const BPN_CHECK = bpnCheck;
const BPN = bpn;
const PORTAL_URL = portalUrl;
const ADMIN_EMAIL = adminEmail;
// Initialize configuration objects
let INIT_OPTIONS = {
  url: null,
  clientId: 'Cl13-CX-Battery', // Catena-X ClientId for Battery Pass
  realm: 'CX-Central', // Catena-X Realm
  onLoad: 'login-required'
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
export { INIT_OPTIONS, REDIRECT_URI, SERVER_URL, IDP_URL, BACKEND_URL, VERSION, API_TIMEOUT, API_DELAY, API_MAX_RETRIES, COMMIT_ID, REPO_ENDPOINT,IRS_DELAY, IRS_MAX_WAITING_TIME, BPN_CHECK, BPN, PORTAL_URL, ADMIN_EMAIL};
