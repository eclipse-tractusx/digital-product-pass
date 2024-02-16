<!--
  Catena-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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
  <div class="header-container">
    <v-container fluid class="header">
      <v-row
        class="d-flex justify-xl-between justify-lg-between justify-md-center justify-sm-center justify-center align-center"
      >
        <v-col class="v-col-auto logo-container d-flex align-center">
          <router-link to="/">
            <!-- To change the logo simply replace "CatenaLogo" with "BatteryLogo" or "BatteryLogotype" in :src  -->
            <img :src="CatenaLogo" alt="logo" class="logo" />
          </router-link>
        </v-col>
        <v-col class="content d-flex justify-between align-center">
          <slot></slot>
        </v-col>
        <v-col class="v-col-auto" style="padding: 0">
          <v-container fluid class="d-flex align-center justify-content-end profile-container">
            <v-row>
              <v-col class="d-flex justify-content-between align-center help-container">
                <a id="lnkHelp" target="_blank" :href="userGuideUrl">
                  <v-btn class="help-btn" :ripple="{ class: 'ripple-background' }" rounded
                    >{{ $t("header.helpBtn") }}
                    <Tooltip>
                      {{ $t("header.helpTooltip") }}
                    </Tooltip>
                  </v-btn>
                </a>
              </v-col>
              <v-col class="d-flex justify-content-between align-center profile">
                <v-menu>
                  <template #activator="{ props }">
                    <img v-bind="props" :src="Profile" alt="profile" class="buttons" />
                  </template>
                  <v-list class="menu-dropdown" rounded="xl">
                    <div class="profile-menu-header">
                      <span>
                        {{ email ? email : "Test user" }}
                        <p class="role">{{ role }}</p>
                      </span>
                    </div>
                    <v-btn :ripple="{ class: 'ripple-background' }" class="menu-btn" @click="logout">
                      <span class="profile-text">{{ $t("header.signOut") }}</span>
                    </v-btn>
                    <div class="profile-menu-lang">
                      <a
                        :class="{
                          language: true,
                          inactive: activeLanguage !== 'de',
                          active: activeLanguage === 'de',
                        }"
                        @click="setLanguage('de')"
                        >DE</a
                      >
                      <a
                        :class="{
                          language: true,
                          inactive: activeLanguage !== 'en',
                          active: activeLanguage === 'en',
                        }"
                        @click="setLanguage('en')"
                        >EN</a
                      >
                    </div>
                  </v-list>
                </v-menu>
              </v-col>
            </v-row>
          </v-container>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import CatenaLogo from "@/media/Catena-X_Logo_mit_Zusatz_2021.svg";
import BatteryLogo from "@/media/BatteryLogo.svg";
import BatteryLogotype from "@/media/BatteryLogotype.svg";
import Profile from "@/media/profile.svg";
import Tooltip from "@/components/general/Tooltip.vue";
import { inject } from "vue";
import { REPO_ENDPOINT, COMMIT_ID } from "@/services/service.const";

export default {
  name: "HeaderComponent",
  components: {
    Tooltip,
  },
  setup() {
    return {
      CatenaLogo,
      BatteryLogo,
      BatteryLogotype,
      Profile,
    };
  },
  data() {
    return {
      activeLanguage: this.$i18n.locale,
      profileHover: false,
      hamburgerMenu: false,
      profileMenu: false,
      email: "",
      role: "",
      auth: inject("authentication"),
      tab: null,
      lang: "en-EN",
      userGuideUrl:
        REPO_ENDPOINT + "/blob/" + COMMIT_ID + "/docs/user%20manual/User%20Manual%20Product%20Viewer%20App.md",
    };
  },
  mounted() {
    if (this.auth.isUserAuthenticated) {
      this.email = this.auth.getUserName();
      this.name = this.auth.getName();
      this.role = this.auth.getRole();
    }
  },
  methods: {
    setLanguage(lang) {
      this.activeLanguage = lang;
      this.$i18n.locale = lang;
    },
    logout() {
      this.auth.logout();
    },
    scanQRCode() {
      this.$router.push({ name: "SearchView" });
    },
  },
};
</script>
