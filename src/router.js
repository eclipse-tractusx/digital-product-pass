import Home from './components/Home.vue';
import { createRouter, createWebHistory } from "vue-router";
import Header from "./components/Header.vue";

const routes = [
  {
    path: "/",
    name: "Header",
    component: Header,
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
