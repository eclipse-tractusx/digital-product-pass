import { createApp } from 'vue'
import App from './App.vue'
//import 'bootstrap'
//import 'bootstrap/dist/css/bootstrap.min.css'
//import 'vue-material/dist/vue-material.min.css'
import router from './router'

//createApp(App).use(store).use(router).mount('#app')
createApp(App).use(router).mount('#app')