<!--
  Tractus-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->

<template>
  <v-app>
    <v-main>
      <template v-if="$authProperties.loginReachable">
        <template v-if="$authProperties.isAuthorized">
          <router-view />
        </template>
        <template v-else>
          <NotAuthorizedComponent />
        </template>
      </template>
      <template v-else>
        <NotAvailableComponent />
      </template>
    </v-main>
  </v-app>
</template>

<script>

import NotAuthorizedComponent from '@/components/general/NotAuthorizedComponent.vue';
import NotAvailableComponent from '@/components/general/NotAvailableComponent.vue';
export default {
  components: { NotAuthorizedComponent, NotAvailableComponent },
  name: "App"
};
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

#app {
  font-family: "Libre Franklin", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-image: url('@/media/Home.png');
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  overflow-x: hidden;
}

.v-container {
  max-width: 100% !important;
}

.table-borderless > :not(caption) > * > * {
  border-bottom-width: 0;
  padding-left: 40px !important;
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: rgba(179, 203, 44, 0.15);
}

.table-striped tbody tr:nth-of-type(even) {
  background-color: rgba(179, 203, 44, 0.05);
}

.table > thead {
  vertical-align: bottom;
  color: gray;
  font-size: 12px;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}
.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}

.v-main {
  background-color: #f9f9f9;
  background-size: cover;
}
</style>
