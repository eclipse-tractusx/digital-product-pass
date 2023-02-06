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
    <Header />
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
import Logout from "../media/logout.png";
import { inject } from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "HomeView",
  components: {
    QRScannerView,
    WelcomeView,
    Footer,
    Header,
  },
  props: {
    batteryId: {
      type: Object,
      default: null,
    },
  },

  setup() {
    return {
      CatenaLogoType,
      CatenaLogo,
      Profile,
      Notifications,
      QRScannerIcon,
      Settings,
      IMR18650V1,
      NCR186850B,
      Logout,
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
      this.$router.push({ name: "ScanPassport" });
    },
  },
};
</script>

<style scoped>
.tooltip {
  position: relative;
  display: inline-block;
}

h1 {
  font-weight: bold;
}

.ghost {
  height: 54vh;
}

.dropdown {
  margin-top: 20px;
  border-radius: 16px;
  width: 256px;
  padding: 0;
}

.header-container {
  position: fixed;
  top: 0;
  display: flex;
  width: 100%;
  margin: 0 12% 0 0;
  padding: 30px 4% 20px;
  border-bottom: 2px solid lightgray;
  background-color: #ffff;
  z-index: 99999;
}

.logo-container {
  padding-top: 10px;
}

.logo {
  height: 40px;
  left: 40px;
}

.tabs {
  display: flex;
  align-items: center;
  justify-content: center;
}

.no-tabs {
  display: none;
}

.logo-type {
  height: 49px;
}

.code-container {
  width: 40%;
  display: flex;
  justify-content: flex-end;
}

.id-wrapper {
  width: 60%;
  line-break: anywhere;
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

.id-container {
  display: flex;
  align-items: center;
  width: 76%;
  margin: 12em 12% 6% 12%;
  padding: 20px 0;
}

.buttons {
  margin: 15px 0 0 30px;
  cursor: pointer;
}

.profile-container {
  position: relative;
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
}

.menu-btn:first-child {
  border-top: none;
}

.menu-btn:hover {
  background: rgba(15, 113, 203, 0.05);
  color: #0d55af;
  cursor: pointer;
}

.profile-text {
  padding: 17px 0 35px 24px;
  font-size: 14px;
  font-weight: 500;
}

p {
  font-size: 14px;
  font-weight: 500;
}

.menu-profile {
  padding: 16px;
}

.toggle-button {
  position: absolute;
  top: 0.75rem;
  right: 1rem;
  display: none;
  flex-direction: column;
  justify-content: space-between;
  width: 30px;
  height: 21px;
  z-index: 1;
}

.toggle-button .bar {
  height: 4px;
  width: 100%;
  background-color: #b3cb2c;
  border-radius: 10px;
}

@media (max-width: 750px) {
  .right-manu-wrapper {
    display: none;
  }

  .toggle-button {
    display: flex;
    margin-right: 36px;
  }

  .code-container {
    display: none;
  }

  .logo {
    height: 45px;
    left: 0;
  }

  .logo-type {
    display: none;
  }

  .header-container {
    width: 100%;
    margin: 0;
  }

  .id-container {
    margin: 12em 0 2em 3em;
    padding: 20px 0;
    width: 85%;
  }

  .id-wrapper {
    width: 100%;
  }

  h1 {
    font-size: 25px;
    line-height: 36px;
  }

  .hamburger-menu {
    display: flex;
    flex-direction: column;
    align-items: center;
    position: absolute;
    width: 100%;
    background-color: #b3cb2c;
    height: auto;
    min-height: 270px;
    padding: 80px 0 0 0;
    z-index: 1;
  }

  .links {
    margin: 12px;
    font-weight: bold;
  }

  h3 {
    color: white;
  }

  .toggle-button .white-bar {
    background-color: white;
  }

  .toggle-button-color {
    background-color: white;
  }

  .profile-menu-mobile {
    display: flex;
    flex-direction: column;
    background-color: white;
    width: 100%;
    justify-content: center;
    align-items: center;
    border: 1px solid #dcdcdc;
  }

  .mobile-menu-links {
    text-align: center;
    font-weight: bold;
    font-size: 16px;
    border: 1px solid #dcdcdc;
    width: 100%;
    min-height: 60px;
    padding: 16px 0 0 0px;
  }
}
</style>
