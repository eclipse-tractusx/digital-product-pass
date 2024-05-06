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
            {}
        ],
        searchData: {
            "contracts": {
            },
        },
        contractToSign: {},
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
        },
        resetLoadingState (state) {
            state.searchData = {
                "data": {
                    "history": {
                    }
                }
            };
            state.searchContractId = null;
        }
    },
    actions: {},
    modules: {},
});
