<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "App",
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
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-image: url(https://portal.dev.demo.catena-x.net/assets/images/frame/Home.png);
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  overflow-x: hidden;
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

.snackbar {
  border: 1px solid red;
  border-radius: 8px;
  background-color: white;
  font-weight: 400;
  margin-top: 25px;
  box-shadow: 0px 20px 40px rgba(80, 80, 80, 0.3);
}

v-main {
  background-image: url(https://portal.dev.demo.catena-x.net/assets/images/frame/Home.png);
  background-repeat: no-repeat;
  background-size: cover;
  background-position: center;
  overflow-x: hidden;
}
</style>
