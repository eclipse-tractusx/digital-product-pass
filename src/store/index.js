import { createStore } from 'vuex';

export default createStore({
  state: {
    email: '',
    password: '',
    role: ''
  },
  getters: {},
  mutations: {
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
