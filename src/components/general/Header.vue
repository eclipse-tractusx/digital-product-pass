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
  <div class="header-container">
    <v-container fluid="true">
      <v-row class="d-flex justify-content-between">
        <v-col class="v-col-auto logo-container d-flex align-center">
          <router-link to="/">
            <img :src="CatenaLogo" alt="logo" class="logo" />
          </router-link>
        </v-col>
        <v-col class="content">
          <slot></slot>
        </v-col>
        <v-col class="v-col-auto">
          <v-container fluid="true" class="d-flex align-center justify-content-end">
            <v-row>
              <v-col>
                <router-link to="/">
                  <v-btn>Help</v-btn>
                </router-link>
              </v-col>
              <v-col>
                <v-menu>
                  <template #activator="{ props }">
                    <img v-bind="props" :src="Profile" alt="profile" class="buttons" />
                  </template>
                  <v-list class="dropdown" rounded="xl">
                    <div class="profile-menu-header">
                      <span>
                        {{ email }}
                        <p class="role">{{ role }}</p>
                      </span>
                    </div>
                    <div class="menu-btn" @click="logout">
                      <span class="profile-text">Logout</span>
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
import CatenaLogo from "../../media/Catena-X_Logo_mit_Zusatz_2021.svg";
import Profile from "../../media/profile.svg";
import { inject } from "vue";
export default {
  name: "HeaderComponent",
  setup() {
    return {
      CatenaLogo,
      Profile,
    };
  },
  data() {
    return {
      profileHover: false,
      hamburgerMenu: false,
      profileMenu: false,
      email: "",
      role: "",
      auth: inject("authentication"),
      tab: null,
    };
  },
  mounted() {
    if (this.auth.isUserAuthenticated) {
      this.email = this.auth.getUserName();
      this.role = this.auth.getRole();
    }
  },
  methods: {
    logout() {
      this.auth.logout();
    },
    scanQRCode() {
      this.$router.push({ name: "SearchView" });
    },
  },
};
</script>

<style scoped>
.dropdown {
  margin-top: 20px;
  border-radius: 16px;
  width: 256px;
  padding: 0;
}
.header-container {
  height: 85px;
  display: flex;
  width: 100%;
  /* margin: 0 12% 0 0; */
  padding: 0 30px 0 30px;
  border-bottom: 2px solid lightgray;
  background-color: #ffff;
  z-index: 99999;
  font-size: 14px!important;
}
.logo-container {
  padding-top: 10px;
}
.logo {
  height: 40px;
  left: 40px;
}
.content {
  display: flex;
  align-items: center;
  justify-content: center;
}
.no-tabs {
  display: none;
}
.right-manu-wrapper {
  width: 20%;
  display: flex;
  justify-content: flex-end;
}
.code {
  padding: 0;
  margin: 0;
}
.buttons {
  cursor: pointer;
}
.profile-menu-header {
  background-color: #f3f3f3;
  border-radius: 16px 16px 0 0;
  padding: 17px 24px 0 24px;
  font-size: 14px;
  font-weight: 500;
  white-space: normal;
}
.role {
  padding: 3px 0 16px 0;
  font-size: 14px;
  color: #888888;
}
.menu-btn {
  display: flex;
  border-top: 1px solid #dcdcdc;
  align-items: center;
  cursor: pointer;
}
.profile-text {
  padding: 17px 0 35px 24px;
  font-size: 14px;
  cursor: pointer;
  font-weight: 500;
}
p {
  font-size: 14px;
  font-weight: 500;
}
@media (max-width: 750px) {
  .right-manu-wrapper {
    display: none;
  }
  .logo {
    height: 45px;
    left: 0;
  }
  .header-container {
    width: 100%;
    margin: 0;
  }
}
</style>