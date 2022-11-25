import Home from './components/Home.vue';
import { createRouter, createWebHistory } from "vue-router";
import QRScanner from "./views/QRScanner.vue";

const routes = [
  {
    path: "/",
    name: "QRScanner",
    component: QRScanner,
  },
  {
    path: '/dashboard',
    name: 'Home',
    component: Home,
  },
  {
    path: "/:id",
    name: "PassportView",
    component: () => import("./views/PassportView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes: routes,
  linkActiveClass: "active",
});

export default router;
