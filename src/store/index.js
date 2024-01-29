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
            "contracts": [
                {
                    "@id": "9b3c0977-6b14-4201-bd76-55f681a92872",
                    "@type": "dcat:Dataset",
                    "odrl:hasPolicy": {
                        "@id": "3:365e6fbe-bb34-11ec-8422-0242ac120002-61125dc3-5e6f-4f4b-838d-447432b97918:dc616f20-2781-450a-837a-290d861c8e0a",
                        "@type": "odrl:Set",
                        "odrl:permission": {
                            "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27",
                            "odrl:action": {
                                "odrl:type": "USE"
                            },
                            "odrl:constraint": {
                                "odrl:or": [
                                    {
                                        "odrl:leftOperand": "Membership",
                                        "odrl:operator": {
                                            "@id": "odrl:eq"
                                        },
                                        "odrl:rightOperand": "active"
                                    },
                                    {
                                        "odrl:leftOperand": "FrameworkAgreement.sustainability",
                                        "odrl:operator": {
                                            "@id": "odrl:eq"
                                        },
                                        "odrl:rightOperand": "active"
                                    }
                                ]
                            }
                        },
                        "odrl:prohibition": [],
                        "odrl:obligation": [],
                        "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27"
                    },
                    "dcat:distribution": [
                        {
                            "@type": "dcat:Distribution",
                            "dct:format": {
                                "@id": "HttpProxy"
                            },
                            "dcat:accessService": "1795254a-e354-46c7-9d88-04608b05ca9f"
                        },
                        {
                            "@type": "dcat:Distribution",
                            "dct:format": {
                                "@id": "AmazonS3"
                            },
                            "dcat:accessService": "1795254a-e354-46c7-9d88-04608b05ca9f"
                        }
                    ],
                    "edc:description": "Battery Passport test data",
                    "edc:id": "365e6fbe-bb34-11ec-8422-0242ac120002-61125dc3-5e6f-4f4b-838d-447432b97918"
                },
                {
                    "@id": "5c4fbb7d-cf02-4401-a7a3-f0ec1c506f33",
                    "@type": "dcat:Dataset",
                    "odrl:hasPolicy": [{
                        "@id": "2:1f4a64f0-aba9-498a-917c-4936c24c50cd-49a06ad2-64b7-46c8-9f3b-a718c462ca23:ac45d75a-2542-4d1a-a0fc-034c705418a9",
                        "@type": "odrl:Set",
                        "odrl:permission": {
                            "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27",
                            "odrl:action": {
                                "odrl:type": "USE"
                            },
                            "odrl:constraint": {
                                "odrl:or": [
                                    {
                                        "odrl:leftOperand": "Membership",
                                        "odrl:operator": {
                                            "@id": "odrl:eq"
                                        },
                                        "odrl:rightOperand": "active"
                                    },
                                    {
                                        "odrl:leftOperand": "FrameworkAgreement.sustainability",
                                        "odrl:operator": {
                                            "@id": "odrl:eq"
                                        },
                                        "odrl:rightOperand": "active"
                                    }
                                ]
                            }
                        },
                        "odrl:prohibition": [],
                        "odrl:obligation": [],
                        "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27"
                    }, {
                        "@id": "2:67deb076-464c-4e8d-8931-087cee0afe2f:ac45d75a-2542-4d1a-a0fc-034c705418a9",
                        "@type": "odrl:Set",
                        "odrl:permission": {
                            "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27",
                            "odrl:action": {
                                "odrl:type": "USE"
                            },
                            "odrl:constraint": {
                                "odrl:and": [
                                    {
                                        "odrl:leftOperand": "DPP",
                                        "odrl:operator": {
                                            "@id": "odrl:eq"
                                        },
                                        "odrl:rightOperand": "active"
                                    }
                                ]
                            }
                        },
                        "odrl:prohibition": {
                            "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27",
                            "odrl:action": {
                                "odrl:type": "DISTRIBUTE"
                            },
                            "odrl:duties": [{
                                "odrl:action": {
                                    "@id": "odrl:compensate"
                                },
                                "odrl:constraint": {
                                    "odrl:name": "odrl:payAmount",
                                    "odrl:operator": {
                                        "@id": "odrl:eq"
                                    },
                                    "odrl:rightOperand": { "@value": "0.0", "@type": "xsd:decimal" },
                                    "odrl:unit": "http://example.com/iso4217a/EUR"
                                }
                            }],
                            "odrl:constraint": {
                                "odrl:leftOperand": "event",
                                "odrl:operator": "odrl:lt",
                                "odrl:rightOperand": {
                                    "@id": "odrl:policyUsage"
                                }
                            }
                        },
                        "odrl:obligation": [],
                        "odrl:target": "urn:uuid:748cf682-6747-33cb-630b-c35a29970f27"
                    }
                    ],
                    "dcat:distribution": [
                        {
                            "@type": "dcat:Distribution",
                            "dct:format": {
                                "@id": "HttpProxy"
                            },
                            "dcat:accessService": "1795254a-e354-46c7-9d88-04608b05ca9f"
                        },
                        {
                            "@type": "dcat:Distribution",
                            "dct:format": {
                                "@id": "AmazonS3"
                            },
                            "dcat:accessService": "1795254a-e354-46c7-9d88-04608b05ca9f"
                        }
                    ],
                    "edc:description": "Battery Passport test data",
                    "edc:id": "1f4a64f0-aba9-498a-917c-4936c24c50cd-49a06ad2-64b7-46c8-9f3b-a718c462ca23"
                }
            ],
            "token": "688787d8ff144c502c7f5cffaafe2cc588d86079f9de88304c26b0cb99ce91c6",
            "id": "ccbf6bfb-c7e1-4db4-8225-9fa95ee82f7f"
        },
        contractToSign: null,
        processId: null,
        searchContractId: null,
        irsState: false,
        qrError: ""
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
        getQrError(state) {
            return state.qrError;
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
        },
        setQrError(state, qrError) {
            state.qrError = qrError;
        }
    },
    actions: {},
    modules: {},
});
