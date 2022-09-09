import { createApp } from 'vue';
import App from './App.vue';
import store from './store/index';
// import BootstrapVue3 from 'bootstrap-vue-3';
// import 'bootstrap/dist/css/bootstrap.css';
// import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';
import router from './router';
import authentication from '@/services/authentication';
import Vue3EasyDataTable from 'vue3-easy-data-table';
import 'vue3-easy-data-table/dist/style.css';

const app = createApp(App);
app.component('EasyDataTable', Vue3EasyDataTable);
app.use(store);
app.use(router);
// app.use(BootstrapVue3);

let auth = new authentication();
app.provide('authentication', auth);
auth.keycloakInit(app);
