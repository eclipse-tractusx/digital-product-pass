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

import { createStore } from 'vuex';
import CryptoJS from "crypto-js";

export default createStore({

    state: {
        email: '',
        password: '',
        role: '',
        clientId: '',
        clientSecret: '',
        sessionId: '',
        statusData: {
            "data": {
                "history": {
                }
            }
        },
        irsData: [
            {
                "id": "urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d",
                "name": "Battery_BAT-XYZ789",
                "searchId": "CX:XYZ78901:BAT-XYZ789",
                "path": "/urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d",
                "children": [
                    {
                        "id": "urn:uuid:d8ec6acc-1ad7-47b4-bc7e-612122d9d552",
                        "name": "BatteryModule_EVMODULE-TRJ712",
                        "searchId": "CX:XYZ78901:EVMODULE-TRJ712",
                        "path": "/urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d/urn:uuid:d8ec6acc-1ad7-47b4-bc7e-612122d9d552",
                        "children": []
                    }
                ]
            }
        ],
        searchData: {
            "contract": {
                "@id": "urn:uuid:0ec8cf2b-f58e-3f13-b5ef-e7dd01d15b19",
                "@type": "dcat:Dataset",
                "odrl:hasPolicy": {
                    "@id": "MDc4NjZjMTQtOGE0YS00ZjQ3LWEyYzgtMWI5MmEwOGQ1NTlm:dXJuOnV1aWQ6MGVjOGNmMmItZjU4ZS0zZjEzLWI1ZWYtZTdkZDAxZDE1YjE5:N2ZjNWNjM2UtMjhjZC00OWI5LTg3MjItZTQ5ODk2MDM1ZDBk",
                    "@type": "odrl:Set",
                    "odrl:permission": [{
                        "odrl:target": "urn:uuid:d6a0ed29-8ba4-fc00-169c-72d3986e0000",
                        "odrl:action": {
                            "odrl:type": "USE"
                        },
                        "odrl:constraint": {
                            "odrl:or": {
                                "odrl:leftOperand": "PURPOSE",
                                "odrl:operator": {
                                    "@id": "odrl:eq"
                                },
                                "odrl:rightOperand": "ID 3.0 Trace"
                            }
                        }
                    },

                    {
                        "odrl:target": "urn:uuid:d6a0ed29-8ba4-fc00-169c-72d3986e1111",
                        "odrl:action": {
                            "odrl:type": "ACCESS"
                        },
                        "odrl:constraint": {
                            "odrl:or": {
                                "odrl:leftOperand": "PURPOSE",
                                "odrl:operator": {
                                    "@id": "odrl:eq"
                                },
                                "odrl:rightOperand": "DPP"
                            }
                        }
                    }],
                    "odrl:prohibition": [],
                    "odrl:obligation": [],
                    "odrl:target": "urn:uuid:0ec8cf2b-f58e-3f13-b5ef-e7dd01d15b19"
                },
                "dcat:distribution": [{
                    "@type": "dcat:Distribution",
                    "dct:format": {
                        "@id": "HttpProxy"
                    },
                    "dcat:accessService": "1bcbba32-d074-4bdc-b6fb-80f4e6202d3a"
                }, {
                    "@type": "dcat:Distribution",
                    "dct:format": {
                        "@id": "AmazonS3"
                    },
                    "dcat:accessService": "1bcbba32-d074-4bdc-b6fb-80f4e6202d3a"
                }],
                "edc:description": "Digital Product Passport (DPP) test data",
                "edc:id": "urn:uuid:0ec8cf2b-f58e-3f13-b5ef-e7dd01d15b19"
            },
            "id": "a4a816d9-86af-4abd-bffc-d1964698d39b",
            "token": "5e2e1afd6bdca53de86368b9884b67b0755f4653c5b31cb5b6bc9f070a043672"
        },
        contractToSign: null,
        processId: null,
        searchContractId: null,
        irsState: false
    },
    getters: {
        getClientId(state) {
            return CryptoJS.AES.decrypt(state.clientId, state.sessionId).toString(CryptoJS.enc.Utf8);
        },
        getClientSecret(state) {
            return CryptoJS.AES.decrypt(state.clientSecret, state.sessionId).toString(CryptoJS.enc.Utf8);
        },
        getSessionId(state) {
            return state.sessionId;
        },
        getProcessId(state) {
            return state.processId;
        },
        getIrsData(state) {
            return state.irsData;
        },
        getIrsState(state) {
            return state.irsState;
        },
        getSearchData(state) {
            return state.searchData;
        },
        getContractToSign(state) {
            return state.contractToSign;
        },
    },
    mutations: {
        setSearchData(state, newSearchData) {
            state.searchData = newSearchData;
        },
        setContractToSign(state, newContractToSign) {
            state.contractToSign = newContractToSign;
        },
        setEmail(state, newEmail) {
            state.email = newEmail;
        },
        setPassword(state, newPassword) {
            state.password = newPassword;
        },
        setClientId(state, clientId) {
            let bytes = CryptoJS.AES.encrypt(clientId, state.sessionId);
            state.clientId = bytes.toString();
        },
        setClientSecret(state, clientSecret) {
            let bytes = CryptoJS.AES.encrypt(clientSecret, state.sessionId);
            state.clientSecret = bytes.toString();
        },
        setSessionId(state, sessionId) {
            state.clientSecret = sessionId;
        },
        setStatusData(state, data) {
            state.statusData = data;
        },
        setIrsData(state, data) {
            state.irsData = data;
        },
        setIrsState(state, irsState) {
            state.irsState = irsState;
        },
        setSearchContractId(state, contractId) {
            state.searchContractId = contractId;
        },
        setProcessId(state, processId) {
            state.processId = processId;
        }
    },
    actions: {},
    modules: {},
});
