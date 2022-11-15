import { createStore } from 'vuex';
import {cryptTools} from "@/util/tools/cryptTools";
export default createStore({
  state: {
    email: '',
    password: '',
    role: '',
    client_id: "",
    session_id: ""
  },
  getters: {
    getClientId(state){
      return cryptTools.decrypt(state.client_id, state.session_id);
    }
  },
  mutations: {
    setClientId(state, clientId){
      state.client_id = cryptTools.encrypt(clientId, state.session_id);
    },
    setEmail(state, newEmail) {
      state.email = newEmail;
    },
    setPassword(state, newPassword) {
      state.password = newPassword;
      
    },
  },
  actions: {},
  modules: {},
});
