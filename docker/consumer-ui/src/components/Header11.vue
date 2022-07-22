<template>
  <div>
    <div class="header-container">
      <div class="logo-container">
        <img :src="CatenaLogo" alt="logo" class="logo" />
      </div>
      <div class="right-manu-wrapper">
        <div class="right-menu-container">
          <img :src="Settings" alt="settings" class="buttons" />
          <img :src="Notifications" alt="profile" class="buttons" />
          <img :src="Profile" alt="profile" class="buttons" />
        </div>
      </div>
    </div>
    <div class="id-container">
      <div class="id-wrapper">
        <h1>
          BatteryID:
          {{ batteryId.batteryId ? batteryId.batteryId : "missing data" }}
        </h1>
      </div>
      <div class="code-container">
        <img :src="QrCode" alt="profile" class="code" />
      </div>
    </div>
  </div>
</template>

<script>
import CatenaLogo from "../assets/logotype.png";
import Profile from "../assets/profile.svg";
import Notifications from "../assets/notifications.svg";
import Settings from "../assets/settings.svg";
import QrCode from "../assets/qrcode.svg";
import Logout from "../assets/logout.png";

export default {
  name: "Header",
  props: {
    batteryId: {},
  },
  components: {
    CatenaLogo,
    Profile,
    Settings,
    Logout,
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
      this.username = JSON.parse(user).name;
      this.role = JSON.parse(user).role;
    }
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
</style>
