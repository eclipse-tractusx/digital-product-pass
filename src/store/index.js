import { createStore } from 'vuex';
const CryptoJS = require("crypto-js");

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
    getClientId(state){
      return CryptoJS.AES.decrypt(state.clientId, state.sessionId).toString(CryptoJS.enc.Utf8);
    },
    getClientSecret(state){
      return CryptoJS.AES.decrypt(state.clientSecret, state.sessionId).toString(CryptoJS.enc.Utf8);
    },
    getSessionId(state){
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
