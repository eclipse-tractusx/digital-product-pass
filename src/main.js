import { createApp } from 'vue';
import App from './App.vue';
import store from './store';
import vuetify from './plugins/vuetify';
import { loadFonts } from './plugins/webfontloader';
import router from './router';
import authentication from '@/services/Authentication';
import Vue3EasyDataTable from 'vue3-easy-data-table';
import 'vue3-easy-data-table/dist/style.css';

loadFonts();

const app = createApp(App);
app.use(vuetify);
app.component('EasyDataTable', Vue3EasyDataTable);
app.use(store);
app.use(router);

let auth = new authentication();
app.provide('authentication', auth);
auth.keycloakInit(app);
