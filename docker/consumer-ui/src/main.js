import { createApp } from "vue";
import App from "./App.vue";
import BootstrapVue3 from "bootstrap-vue-3";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-vue-3/dist/bootstrap-vue-3.css";
import router from "./router";
import authentication from "@/services/authentication"
import { INIT_OPTIONS, KEYCLOAK } from "@/services/service.const"

const app = createApp(App).use(router);
app.use(BootstrapVue3);

let auth = new authentication();

KEYCLOAK.init({ onLoad: INIT_OPTIONS.onLoad }).then((authentication) => {
    if (!authentication) {
        window.location.reload();
    }
    else {
        app.mount('#app')
    }
    //Token Refresh
  setInterval(() => {
    auth.updateToken(60)
  }, 60000)

}).catch((e) => {
    console.log(e)
    console.log('Login Failure')
});