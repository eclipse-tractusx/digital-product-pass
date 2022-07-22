import { createRouter, createWebHistory } from 'vue-router'
import Home from './components/Home.vue'
import SignUp from './components/SignUp.vue'
import Login from './components/Login.vue'
import ScanPassport from './components/ScanPassport.vue'
import PassportView from "./views/PassportView.vue";


const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/',
    name: 'Passport',
    component: PassportView,
    props: (route) => ({ provider: route.query.provider, battery: route.query.battery })
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "./views/AboutView.vue"),
  },
  {
    path: '/sign-up',
    name: 'SignUp',
    component: SignUp
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/api/scanpassport',
    name: 'ScanPassport',
    component: ScanPassport
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;