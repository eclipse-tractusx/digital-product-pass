/**
 * Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Home from './components/Home.vue';
import PageNotFound from './views/PageNotFound.vue';
import { createRouter, createWebHistory } from "vue-router";
import Header from "./components/Header.vue";

const routes = [
  {
    path: "/",
    name: "Header",
    component: Header,
  },
  {
    path: "/:catchAll(.*)",
    name: "PageNotFound",
    component: PageNotFound,
  },
  {
    path: "/PageNotFound",
    name: "PageNotFound",
    component: PageNotFound,
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
