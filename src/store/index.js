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

import { createStore } from 'vuex';
import CryptoJS from "crypto-js";

export default createStore({

  //   plugins: [createPersistedState({

  //     storage: window.localStorage,

  //   })],
  state: {
    email: '',
    password: '',
    role: '',
    clientId: '',
    clientSecret: '',
    sessionId: ''
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

  },
  mutations: {
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
    }

  },
  actions: {},
  modules: {},
});
