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
import { BACKEND_URL, API_MAX_RETRIES, API_DELAY } from "@/services/service.const";
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

    async getPassport(version, id, authentication) {
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
            negotiationResponse = await this.searchContract(ids["serializedId"], version, processId, authentication);
        } catch (e) {
            return negotiationResponse;
        }


        // Check if the negotiation status is successful or if the data is not retrieved
        if (!negotiationResponse || (jsonUtil.exists("status", negotiationResponse) && negotiationResponse["status"] != 200) || !jsonUtil.exists("data", negotiationResponse)) {
            return negotiationResponse;
        }

        // Get negotiation property
        let negotiation = negotiationResponse["data"];
        let processStatus = null;
        // Check if the attributes in data exist

        let token = jsonUtil.get("token", negotiation, ".", null);
        let contractId = jsonUtil.get("contract.@id", negotiation, ".", null);

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
            processStatus = await this.signContract(negotiation, authentication);
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

        if (status === "COMPLETED" || status === "RECEIVED") {
            return await this.retrievePassport(negotiation, authentication);
        }

        return this.getErrorMessage(
            "Failed to retrieve passport!",
            500,
            "Internal Server Error"
        )
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
    getRequestBody(negotiation) {
        return {
            "processId": negotiation["id"],
            "contractId": negotiation["contract"]["@id"],
            "token": negotiation["token"]
        }
    }

    getSearchBody(id, version, processId) {
        return {
            "id": id,
            "version": version,
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
    async retrievePassport(negotiation, authentication) {
        return new Promise(resolve => {
            let body = this.getRequestBody(negotiation);
            axios.post(`${BACKEND_URL}/api/passport`, body, this.getHeadersCredentials(authentication))
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
    async signContract(negotiation, authentication) {
        return new Promise(resolve => {
            let body = this.getRequestBody(negotiation);
            axios.post(`${BACKEND_URL}/api/contract/sign`, body, this.getHeadersCredentials(authentication))
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
    async searchContract(id, version, processId, authentication) {
        return new Promise(resolve => {
            let body = this.getSearchBody(id, version, processId);
            axios.post(`${BACKEND_URL}/api/contract/search`, body, this.getHeaders(authentication))
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
