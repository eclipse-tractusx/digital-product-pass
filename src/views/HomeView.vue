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
    <Header>
      <v-tabs v-model="tab" show-arrows>
        <v-tab value="one" class="text-uppercase" data-cy="history-tab"
          >History Page</v-tab
        >
        <v-tab value="two" class="text-uppercase" data-cy="QR-scanner-tab"
          >Search Passport</v-tab
        >
      </v-tabs>
    </Header>
    <v-container>
      <v-window v-model="tab">
        <v-main>
          <v-window-item value="one">
            <WelcomeView />
          </v-window-item>
          <v-window-item value="two">
            <QRScannerView />
          </v-window-item>
        </v-main>
      </v-window>
    </v-container>
    <Footer />
  </div>
</template>

<script>
import QRScannerView from "./SearchView.vue";
import WelcomeView from "./WelcomeView.vue";
import Footer from "../components/general/Footer.vue";
import Header from "../components/general/Header.vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "HomeView",
  components: {
    QRScannerView,
    WelcomeView,
    Footer,
    Header,
  },
  data() {
    return {
      tab: null,
    };
  },
};
</script>

<style scoped>
</style>
