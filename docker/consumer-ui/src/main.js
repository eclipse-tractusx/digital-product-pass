import { createApp } from "vue";
import App from "./App.vue";
import BootstrapVue3 from "bootstrap-vue-3";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-3/dist/bootstrap-vue-3.css";
import router from "./router";
import authentication from "@/services/authentication"

const app = createApp(App).use(router);
app.use(BootstrapVue3);

let auth = new authentication();
app.provide('authentication', auth);
auth.keycloakInit(app);