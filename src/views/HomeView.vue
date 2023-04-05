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
  <div>
    <HeaderComponent>
      <div class="header-title">Search Passport</div>
    </HeaderComponent>
    <v-container>
      <v-window>
        <v-main>
          <v-window-item>
            <SearchView />
          </v-window-item>
        </v-main>
      </v-window>
    </v-container>
    <FooterComponent />
  </div>
</template>

<script>
import SearchView from "./SearchView.vue";
import FooterComponent from "../components/general/Footer.vue";
import HeaderComponent from "../components/general/Header.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "HomeView",
  components: {
    SearchView,
    FooterComponent,
    HeaderComponent,
  },
};
</script>

