<template>
  <div>
    <div class="header-container">
      <div class="logo-container">
        <router-link to="/">
          <img :src="CatenaLogo" alt="logo" class="logo" />
        </router-link>
      </div>
      <v-container class="tabs">
        <v-tabs v-model="tab" :class="batteryId ? 'no-tabs' : ''">
          <v-tab value="one">History page</v-tab>
          <v-tab value="two">QR code scanner</v-tab>
        </v-tabs>
      </v-container>
      <div class="right-manu-wrapper">
        <div class="right-menu-container">
          <v-menu>
            <template #activator="{ props }">
              <img
                v-bind="props"
                :src="Profile"
                alt="profile"
                class="buttons"
              />
            </template>
            <v-list class="dropdown" rounded="xl">
              <div class="profile-menu-header">
                <span class="profile-text">
                  {{ username }}
                  <p class="role">{{ role }}</p>
                </span>
              </div>
              <div class="menu-btn">
                <span class="profile-text" @click="logout">Logout</span>
              </div>
            </v-list>
          </v-menu>
        </div>
      </div>
    </div>
    <v-container v-if="!batteryId">
      <v-window v-model="tab">
        <v-main>
          <v-window-item value="one">
            <BatteryPassport />
          </v-window-item>
          <v-window-item value="two">
            <div class="ghost"></div>
            <QRScanner />
          </v-window-item>
        </v-main>
      </v-window>
    </v-container>
    <div v-if="batteryId" class="id-container">
      <div class="id-wrapper">
        <h1>
          BatteryID:
          {{
            batteryId.batteryIdentification.batteryIDDMCCode
              ? batteryId.batteryIdentification.batteryIDDMCCode
              : "—"
          }}
        </h1>
      </div>
      <div
        v-if="
          batteryId.batteryIdentification.batteryIDDMCCode ==
          'X123456789012X12345678901234566'
        "
        class="code-container"
      >
        <img
          :src="X123456789012X12345678901234566"
          alt="profile"
          class="code"
          width="170"
          height="170"
        />
      </div>
      <div
        v-else-if="
          batteryId.batteryIdentification.batteryIDDMCCode == 'NCR186850B'
        "
        class="code-container"
      >
        <img
          :src="NCR186850B"
          alt="profile"
          class="code"
          width="170"
          height="170"
        />
      </div>
      <div
        v-if="batteryId.batteryIdentification.batteryIDDMCCode == 'IMR18650V1'"
        class="code-container"
      >
        <img
          :src="IMR18650V1"
          alt="profile"
          class="code"
          width="170"
          height="170"
        />
      </div>
    </div>
    <Footer v-if="!batteryId" />
  </div>
</template>

<script>
import CatenaLogoType from "../assets/logotype.png";
import CatenaLogo from "../assets/Catena-X_Logo_mit_Zusatz_2021.svg";
import Profile from "../assets/profile.svg";
import Notifications from "../assets/notifications.svg";
import Settings from "../assets/settings.svg";
import QRScannerIcon from "../assets/qr-icon.svg";
import QrCode from "../assets/BMW_test-battery-1.svg";
import IMR18650V1 from "../assets/IMR18650V1.svg";
import X123456789012X12345678901234566 from "../assets/X123456789012X12345678901234566.svg";
import NCR186850B from "../assets/NCR186850B.svg";
import QRScanner from "../views/QRScanner.vue";
import BatteryPassport from "../components/BatteryPassport.vue";
import Footer from "../components/Footer.vue";
import Logout from "../assets/logout.png";
import { inject } from "vue";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Header",
  components: {
    QRScanner,
    BatteryPassport,
    Footer,
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
      QrCode,
      IMR18650V1,
      X123456789012X12345678901234566,
      NCR186850B,
      Logout,
    };
  },

  data() {
    return {
      profileHover: false,
      hamburgerMenu: false,
      profileMenu: false,
      username: "",
      role: "",
      auth: inject("authentication"),
      tab: null,
    };
  },
  mounted() {
    if (this.auth.isUserAuthenticated) {
      this.username = this.auth.getUserName();
      this.role = this.auth.getRole();
    }
    console.log("user hello", this.username);
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
  display: flex;
  width: 100%;
  margin: 30px 12% 0 0;
  padding: 0 4% 20px;
  border-bottom: 2px solid lightgray;
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
  margin: 2% 12% 6% 12%;
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
  padding: 17px 0 0 0;
}

.role {
  padding: 3px 0 16px 24px;
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
    margin: 30px;
  }

  .id-container {
    margin: 45px 0 0 30px;
    padding: 20px 0;
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
