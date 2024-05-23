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
  <div>
    <HeaderComponent>
      <div class="header-title">
        {{ welcomeUser ? $t("home.welcome") + welcomeUser : $t("home.home") }}
      </div>
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
import { inject } from "vue";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "HomeView",
  data() {
    return {
      auth: inject("authentication"),
    };
  },
  computed: {
    welcomeUser() {
      if (this.auth.isUserAuthenticated) {
        return " " + this.auth.getName() + "!";
      } else {
        return null;
      }
    },
  },
  components: {
    SearchView,
    FooterComponent,
    HeaderComponent,
  },
};
</script>

