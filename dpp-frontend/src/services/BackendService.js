/**
 * Tractus-X - Digital Product Passport Application
 *
 * Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
 * Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
 * Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import { BACKEND_URL, API_MAX_RETRIES, API_DELAY, AUTO_SIGN } from "@/services/service.const";
import axios from "axios";
import jsonUtil from "@/utils/jsonUtil.js";
import threadUtil from "../utils/threadUtil";
import store from "../store/index"
export default class BackendService {

    splitIds(id, sep = ":") {
        let ids = id.split(sep);
        // Check if id is not empty
        if (ids == null || ids.length == 0) {
            throw new Error('The Id must not be empty!');
        }
        // Check if the syntax is correct and it contains tree parts
        if (ids.length != 3) {
            throw new Error('The Id must have 3 elements!');
        }

        // Check if the first part contains the catena-x prefix
        if (ids[0].toUpperCase() !== "CX") {
            throw new Error('Invalid Id prefix!');
        }

        if (ids[1] == undefined || ids[1] == null || ids[1] === "" || ids[2] == undefined || ids[2] == null || ids[2] === "") {
            throw new Error('One of the Ids is empty!');
        }

        // Build and separate the different ids
        return {
            "prefix": ids[0],
            "discoveryId": ids[1],
            "serializedId": ids[2]
        }
    }

    async searchAsset(id, authentication) {
        let processResponse = null;
        // Try to get the negotiation contract
        let ids = null;
        try {
            ids = this.splitIds(id);
        } catch (e) {
            return this.getErrorMessage(
                e.message,
                500,
                "Internal Server Error"
            )
        }
        if (ids == null) {
            return this.getErrorMessage(
                "Failed to parse the search id!",
                500,
                "Internal Server Error"
            )
        }
        try {
            processResponse = await this.createProcess(ids["discoveryId"], authentication);
        } catch (e) {
            return processResponse;
        }

        // Check if the process is successful or if the data is not retrieved
        if (!processResponse || (jsonUtil.exists("status", processResponse) && processResponse["status"] != 200) || !jsonUtil.exists("data", processResponse)) {
            return processResponse;
        }

        let processId = jsonUtil.get("data.processId", processResponse, ".", null);

        store.commit('setProcessId', processId);


        // Check if the process id is not empty
        if (processId == null || processId === "") {
            return this.getErrorMessage(
                "The contract request was not valid, no process id exists",
                500,
                "Internal Server Error"
            )

        }

        let negotiationResponse = null;


        // Try to get the negotiation contract
        try {
            negotiationResponse = await this.searchContract(ids["serializedId"], processId, authentication);
        } catch (e) {
            return negotiationResponse;
        }


        // Check if the negotiation status is successful or if the data is not retrieved
        if (!negotiationResponse || (jsonUtil.exists("status", negotiationResponse) && negotiationResponse["status"] != 200) || !jsonUtil.exists("data", negotiationResponse)) {
            return negotiationResponse;
        }

        // Get negotiation property
        return negotiationResponse;
    } 
    async cancelNegotiation(token, processId, contractId, authentication){
        let processStatus = null;
        // Use selects here a contract
        let negotiation = {
            "processId": processId,
            "contractId": contractId,
            "token":  token
        }
        try {
            processStatus = await this.cancelContract(negotiation, authentication);
        } catch (e) {
            return null;
        }

        return processStatus;
    }
    async declineNegotiation(token, processId, authentication){
        let processStatus = null;
        // Use selects here a contract
        let negotiation = {
            "processId": processId,
            "token":  token
        }
        try {
            processStatus = await this.declineContract(negotiation, authentication);
        } catch (e) {
            return null;
        }

        return processStatus;
    }
    async negotiateAsset(contracts, token, processId, authentication, contractId=null, policyId=null){
        let contract = null;
        // Use selects here a contract
        if(contractId == null){
            contract = contracts[Object.keys(contracts)[0]];
            if(contract == null){
                return this.getErrorMessage(
                    "The contract selected is incorrect or does not exists!",
                    500,
                    "Internal Server Error"
                )
            }
            contractId = contract["@id"];
        }
        let negotiation = {
            "processId": processId,
            "contractId": contractId,
            "token":  token
        }
        if(!AUTO_SIGN){
            if(contractId == null){
                return this.getErrorMessage(
                    "At least one contract needs to be selected",
                    500,
                    "Internal Server Error"
                )
            }
            if(policyId == null){
                return this.getErrorMessage(
                    "At least one policy needs to be selected",
                    500,
                    "Internal Server Error"
                )
            }
            negotiation["policyId"] = policyId;
            negotiation["contractId"] = contractId;
        }
        let processStatus = null;

        store.commit('setSearchContractId', contractId);

        // If is not existing we return an error imitating the response
        if (!token || !contractId) {
            return this.getErrorMessage(
                "The contract request was not valid",
                500,
                "Internal Server Error"
            )

        }

        // Sign the contract request
        try {
            processStatus = await this.agreeContract(negotiation, authentication);
        } catch (e) {
            return processStatus;
        }
        // Check if the process status is successful or if the data is not retrieved
        if (!processStatus || (jsonUtil.exists("status", processStatus) && processStatus["status"] != 200) || !jsonUtil.exists("data", processStatus)) {
            return processStatus;
        }


        let status = jsonUtil.get("data.status", processStatus, ".", null);
        if (status == "FAILED") {
            return this.getErrorMessage(
                "The negotiation process has failed",
                500,
                "Internal Server Error"
            )
        }

        let loopBreakStatus = ["COMPLETED", "FAILED", "DECLINED", "RECEIVED"]
        let maxRetries = API_MAX_RETRIES;
        let waitingTime = API_DELAY;
        let retries = 0;
        let statusResponse = null;

        while (retries < maxRetries) {
            statusResponse = await this.getStatus(processId, authentication)
            status = jsonUtil.get("data.status", statusResponse);
            if (loopBreakStatus.includes(status) || status == null) {
                break;
            }
            await threadUtil.sleep(waitingTime);
            retries++;
        }
        let history = jsonUtil.get("data.history", statusResponse);
        if(jsonUtil.exists("transfer-completed", history) && jsonUtil.exists("data-received", history)){
            return await this.retrievePassport(negotiation, authentication);
        }
        // Get status again
        statusResponse = await this.getStatus(processId, authentication)
        status = jsonUtil.get("data.status", statusResponse);
        history = jsonUtil.get("data.history", statusResponse);
        if(jsonUtil.exists("transfer-completed", history) && jsonUtil.exists("data-received", history)){
            return await this.retrievePassport(negotiation, authentication);
        }

        retries = 0;
        // Until the transfer is completed or the status is failed
        while(retries < maxRetries){
            // Wait
            await threadUtil.sleep(waitingTime);
            // Refresh the values
            statusResponse = await this.getStatus(processId, authentication);
            status = jsonUtil.get("data.status", statusResponse);
            history = jsonUtil.get("data.history", statusResponse);
            if((jsonUtil.exists("transfer-completed", history) && jsonUtil.exists("data-received", history)) || status === "FAILED"){
                break;
            }
            retries++;
        }
        
        // If the status is failed...
        if (status === "FAILED") {
            return this.getErrorMessage(
                "Failed to retrieve passport!",
                500,
                "Internal Server Error"
            )
        }
        // If is not failed return the passport
        return await this.retrievePassport(negotiation, authentication);
    }
    
    getErrorMessage(message, status, statusText) {
        return {
            "message": message,
            "status": status,
            "statusText": statusText
        }
    }
    getHeadersCredentials(authentication) {
        let params = this.getHeaders(authentication);
        params["withCredentials"] = true;
        return params;

    }
    getHeaders(authentication) {
        return {
            headers: {
                'Accept': 'application/json',
                'Authorization': "Bearer " + authentication.getAccessToken()
            }
        }

    }

    getSearchBody(id, processId) {
        return {
            "id": id,
            "processId": processId
        }
    }
    async getStatus(processId, authentication) {
        return new Promise(resolve => {
            axios.get(`${BACKEND_URL}/api/contract/status/` + processId, this.getHeadersCredentials(authentication))
                .then((response) => {
                    // Setting the status to the Store state
                    store.commit('setStatusData', response.data);
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }

    async getIrsState(processId, authentication) {
        return new Promise(resolve => {
            axios.get(`${BACKEND_URL}/api/irs/` + processId + `/state`, this.getHeadersCredentials(authentication))
                .then((response) => {
                    resolve(response);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }
                });
        });
    }
    async getIrsData(processId, authentication) {
        return new Promise(resolve => {
            axios.get(`${BACKEND_URL}/api/irs/` + processId + `/components`, this.getHeadersCredentials(authentication))
                .then((response) => {
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }
                });
        });
    }
    async retrievePassport(body, authentication) {
        return new Promise(resolve => {
            axios.post(`${BACKEND_URL}/api/data`, body, this.getHeadersCredentials(authentication))
                .then((response) => {
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }
    async cancelContract(body, authentication) {
        return new Promise(resolve => {
            axios.post(`${BACKEND_URL}/api/contract/cancel`, body, this.getHeadersCredentials(authentication))
                .then((response) => {
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }
    async declineContract(body, authentication) {
        return new Promise(resolve => {
            axios.post(`${BACKEND_URL}/api/contract/decline`, body, this.getHeadersCredentials(authentication))
                .then((response) => {
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }
    async agreeContract(body, authentication) {
        return new Promise(resolve => {
            axios.post(`${BACKEND_URL}/api/contract/agree`, body, this.getHeadersCredentials(authentication))
                .then((response) => {
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }
    async searchContract(id, processId, authentication) {
        return new Promise(resolve => {
            let body = this.getSearchBody(id, processId);
            axios.post(`${BACKEND_URL}/api/contract/search`, body, this.getHeaders(authentication))
                .then((response) => {
                    // Setting the status to the Store state
                    store.commit('setSearchData', response.data);
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }
    async createProcess(discoveryId, authentication, type = "manufacturerPartId") {
        return new Promise(resolve => {
            let body = {
                "id": discoveryId,
                "type": type
            }
            axios.post(`${BACKEND_URL}/api/contract/create`, body, this.getHeaders(authentication))
                .then((response) => {
                    resolve(response.data);
                })
                .catch((e) => {
                    if (e.response.data) {
                        resolve(e.response.data);
                    } else if (e.request) {
                        resolve(e.request);
                    } else {
                        resolve(e.message)
                    }

                });
        });
    }
}
