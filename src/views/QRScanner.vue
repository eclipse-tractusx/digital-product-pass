<template>
  <div class="qr-container">
    <router-link to="/dashboard">
      <img :src="CatenaLogo" alt="logo" class="logo" />
    </router-link>
    <p>{{ error }}</p>
    <div class="header-container">
      <div class="left-menu-wrapper">
        <div class="left-menu-container">
          <div class="empty-pusher"></div>
          <h2 class="top-layer">Scan QR code</h2>
          <div class="top-layer" @click="torch = !torch">
            <img :src="Flesh" alt="flesh" />
          </div>
        </div>
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
            <div v-if="hover" class="profile-menu" @mouseleave="hover = false">
              <div class="menu-btn">
                <img :src="Profile" alt="profile" class="menu-profile" />
                <span class="profile-text">
                  {{ username }}
                  <p>{{ role }}</p>
                </span>
              </div>
              <div class="menu-btn">
                <span class="profile-text" @click="logout">Sign out</span>
              </div>
            </div>
          </span>
        </div>
      </div>
    </div>
    <div class="qr-frame">
      <img :src="QRFrame" alt="frame" class="frame" />
    </div>
    <qrcode-stream
      :torch="torch"
      class="qrcode-stream"
      @init="onInit"
      @decode="onDecode"
    ></qrcode-stream>
    <div>
      <form class="input-form" @submit.prevent="onClick">
        <input
          v-model="typedCode"
          class="input"
          type="text"
          placeholder="Type ID"
        />
        <button class="submit-btn">
          <img :src="Search" alt="search" />
        </button>
      </form>
    </div>
  </div>
  <div class="footer-container">
    <Footer />
  </div>
</template>

<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../assets/logo.png";
import Flesh from "../assets/flesh.svg";
import QRFrame from "../assets/qrFrame.svg";
import Search from "../assets/qrSearch.svg";
import Footer from "@/components/Footer.vue";
import Logout from "../assets/logout.png";
import Profile from "../assets/profile.svg";
import Notifications from "../assets/notifications.svg";
import Settings from "../assets/settings.svg";
import { inject } from "vue";

export default {
  name: "PassportView",
  components: {
    QrcodeStream,
    Footer,
  },
  setup() {
    return {
      CatenaLogo,
      Flesh,
      Search,
      QRFrame,
      Profile,
      Notifications,
      Settings,
      Logout,
    };
  },

  data() {
    return {
      hover: false,
      error: "",
      decodedString: "",
      torch: false,
      MATERIAL_URL: process.env.VUE_APP_MATERIAL_URL,
      typedCode: "",
      username: "",
      role: "",
      auth: inject("authentication"),
    };
  },
  mounted() {
    if (this.auth.isUserAuthenticated) {
      this.username = this.auth.getUserName();
      this.role = this.auth.getRole();
    }
  },
  async created() {
    this.loading = false;
  },
  methods: {
    logout() {
      this.auth.logout();
    },
    async onInit(promise) {
      try {
        await promise;
      } catch (error) {
        if (error.name === "NotAllowedError") {
          this.error = "ERROR: you need to grant camera access permission";
        } else if (error.name === "NotFoundError") {
          this.error = "ERROR: no camera on this device";
        } else if (error.name === "NotSupportedError") {
          this.error = "ERROR: secure context required (HTTPS, localhost)";
        } else if (error.name === "NotReadableError") {
          this.error = "ERROR: is the camera already in use?";
        } else if (error.name === "OverconstrainedError") {
          this.error = "ERROR: installed cameras are not suitable";
        } else if (error.name === "StreamApiNotSupportedError") {
          this.error = "ERROR: Stream API is not supported in this browser";
        } else if (error.name === "InsecureContextError") {
          this.error =
            "ERROR: Camera access is only permitted in secure context. Use HTTPS or localhost rather than HTTP.";
        } else {
          this.error = `ERROR: Camera error (${error.name})`;
        }
      }
    },
    onDecode(decodedString) {
      this.decodedString = decodedString;
      this.$router.push({
        path: `/${decodedString}`,
      });
    },

    onClick() {
      this.$router.push({
        path: `/${this.typedCode}`,
      });
    },
  },
};
</script>

<style scoped>
.qr-container {
  position: relative;
  max-height: 900px;
  overflow: hidden;
}

.qrcode-stream {
  max-width: 100%;
}

.header-container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 137px;
  background-color: rgba(255, 255, 255, 0.3);
  z-index: 3;
  box-shadow: 0 0 10px 0 rgb(81, 81, 81);
}

.logo {
  position: absolute;
  top: 40px;
  left: 50px;
  height: 49px;
  z-index: 5;
}

.qr-frame {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  height: 400px;
  z-index: 10;
}

.top-layer {
  white-space: nowrap;

  text-overflow: ellipsis;
}

.qrcode-stream-overlay {
  position: absolute;
  backdrop-filter: blur(9px);
}

.input-form {
  position: absolute;
  top: 80%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.input {
  position: relative;
  width: 418px;
  height: 48px;
  border: 2px solid #b3cb2c;
  border-radius: 4px;
  padding-left: 10px;
}

.submit-btn {
  position: absolute;
  right: 0;
  height: 48px;
  background: none;
  border: none;
  cursor: pointer;
}

.empty-pusher {
  width: 56px;
}

.right-manu-wrapper {
  display: flex;
  justify-content: flex-end;
  width: 100%;
  padding-right: 100px;
}

.left-menu-wrapper {
  position: absolute;

  width: 20%;
  left: 0;
  right: 0;
  margin-left: auto;
  margin-right: auto;
}

.left-menu-container {
  display: flex;
  align-items: center;
}

.buttons {
  width: 26px;
  height: 26px;
  margin: 15px 0 15px 30px;
  cursor: pointer;
}

.profile-container {
  position: relative;
}

.profile-menu {
  position: absolute;
  margin-right: 100px;
  min-width: 342px;
  border: solid 1px #ffa600;
  right: 0;
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

.profile-text {
  padding: 0 16px 0 12px;
  font-size: 18px;
  font-weight: bold;
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

@media (max-width: 856px) {
  .right-manu-wrapper {
    padding-right: 50px;
  }

  .profile-menu {
    margin-right: 50px;
  }
  .frame {
    width: 250px;
    height: 250px;
  }
  .qr-frame {
    width: 250px;
    height: 250px;
  }

  .input {
    position: relative;
    width: 50vw;
  }
}
@media (max-width: 670px) {
  .right-manu-wrapper {
    display: none;
  }

  .frame {
    width: 150px;
    height: 150px;
  }
  .qr-frame {
    width: 150px;
    height: 150px;
  }

  h2 {
    font-size: 16px;
  }
}
@media (max-width: 570px) {
  .right-manu-wrapper {
    display: none;
  }

  .header-container {
    height: 90px;
  }

  h2 {
    font-size: 16px;
  }
  .logo {
    position: absolute;
    top: 20px;
    left: 50px;
    height: 49px;
    z-index: 5;
  }
}

@media (max-width: 375px) {
  .qr-container {
    position: relative;
    height: 80vh;
    overflow: hidden;
  }

  .right-manu-wrapper {
    display: none;
  }
  .input {
    position: relative;
    width: 80vw;
  }
}
</style>
