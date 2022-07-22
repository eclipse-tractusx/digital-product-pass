<template>
  <div>
    <div class="header-container profile-container">
      <div class="logo-container">
        <img :src="CatenaLogo" alt="logo" class="logo" />
      </div>
      <div class="right-manu-wrapper">
        <div class="right-menu-container">
          <img :src="Settings" alt="settings" class="buttons" />
          <img :src="Notifications" alt="profile" class="buttons" />
          <span>
            <span @mouseover="hover = true">
              <img
                :src="Profile"
                alt="profile"
                class="buttons"
                title="User profile"
              />
            </span>
            <div class="profile-menu" v-if="hover" @mouseleave="hover = false">
              <div class="menu-btn">
                <img :src="Profile" alt="profile" class="menu-profile" />
                <!--TODO: Profile page onClick-->
                <h3>
                  {{ username }}
                  <p>Manage your account</p>
                </h3>
              </div>
              <div class="menu-btn">
                <h3 v-on:click="logout">Sign out</h3>
              </div>
            </div>
          </span>
        </div>
      </div>
    </div>
    <div v-if="batteryId" class="id-container">
      <div class="id-wrapper">
        <h1>
          BatteryID:
          {{ batteryId.batteryId ? batteryId.batteryId : "—" }}
        </h1>
      </div>
      <div class="code-container">
        <img :src="QrCode" alt="profile" class="code"  width="140" height="140"/>
      </div>
    </div>
  </div>
</template>

<script>
import CatenaLogo from "../assets/logotype.png";
import Profile from "../assets/profile.svg";
import Notifications from "../assets/notifications.svg";
import Settings from "../assets/settings.svg";
import QrCode from "../assets/BMW_test-battery-1.svg";
import Logout from "../assets/logout.png";

export default {
  name: "Header",

  components: {
    CatenaLogo,
    Profile,
    Settings,
    Logout,
  },
  data() {
    return {
      hover: false,
      username: "",
      role: "",
    };
  },
  setup() {
    return {
      CatenaLogo,
      Profile,
      Notifications,
      Settings,
      QrCode,
      Logout,
    };
  },
  methods: {
    logout() {
      localStorage.clear();
      this.$router.push({ name: "Login" });
    },
    scanQRCode() {
      this.$router.push({ name: "ScanPassport" });
    },
  },
  mounted() {
    let user = localStorage.getItem("user-info");
    if (user) {
      this.username = JSON.parse(user).email;
      this.role = JSON.parse(user).role;
    }
  },
  props: {
    batteryId: {},
  },
};
</script>

<style scoped>
.header-container {
  display: flex;

  width: 76%;
  margin: 4% 12% 0 12%;
}
.logo-container {
  display: block;
  width: 50%;
  height: fit-content;
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
  width: 50%;
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
}
.logo {
  display: block;
  width: 209px;
  height: 49px;
  margin: 0 0 7% -64px;
}
.buttons {
  width: 26px;
  height: 26px;
  margin: 15px 0px 15px 30px;
}
.profile-container {
  position: relative;
}
.profile-menu {
  position: absolute;
  min-width: 342px;

  border: solid 1px #ffa600;
  right: 0;

  z-index: 1;
  background-color: white;
  cursor: pointer;
}
.menu-btn {
  display: flex;
  border-top: solid 1px #ffa600;
  padding: 16px;
  align-items: center;
}
.menu-btn:first-child {
  border-top: none;
}
.menu-btn:hover {
  background-color: #f8f9fa;
}
h3 {
  padding: 0 16px 0 12px;
}
p {
  color: #cccccc;
  font-size: 14px;
  font-weight: normal;
}
.menu-profile {
  padding: 16px;
}
</style>
