import { createApp } from "vue";
import App from "./App.vue";
import store from "./store/index";

import BootstrapVue3 from "bootstrap-vue-3";

import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-3/dist/bootstrap-vue-3.css";

import router from "./router";

//createApp(App).use(store).use(router).mount('#app')
const app = createApp(App);
app.use(store);
app.use(router);
app.use(BootstrapVue3);
app.mount("#app");
