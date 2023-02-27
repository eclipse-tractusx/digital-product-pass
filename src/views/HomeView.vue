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
      <v-tabs v-model="tab" show-arrows class="menu">
        <v-tab
          :ripple="{ class: 'ripple-background' }"
          class="nav-items"
          value="one"
          data-cy="history-tab"
          >History Page</v-tab
        >
        <v-tab
          :ripple="{ class: 'ripple-background' }"
          class="nav-items"
          value="two"
          data-cy="QR-scanner-tab"
          >Search Passport</v-tab
        >
      </v-tabs>
    </HeaderComponent>
    <v-container>
      <v-window v-model="tab">
        <v-main>
          <v-window-item value="one">
            <WelcomeView />
          </v-window-item>
          <v-window-item value="two">
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
import WelcomeView from "./WelcomeView.vue";
import FooterComponent from "../components/general/Footer.vue";
import HeaderComponent from "../components/general/Header.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "HomeView",
  components: {
    SearchView,
    WelcomeView,
    FooterComponent,
    HeaderComponent,
  },
  data() {
    return {
      tab: null,
    };
  },
};
</script>

