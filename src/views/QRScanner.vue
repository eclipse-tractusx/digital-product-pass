<template>
  <div class="qr-container">
    <router-link to="/dashboard"> </router-link>

    <v-snackbar
      v-if="error ? (snackbar = true) : (snackbar = false)"
      v-model="snackbar"
      location="top"
      content-class="snackbar"
      variant="plain"
    >
      {{ error }}
    </v-snackbar>
    <div v-if="!error">
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
        <!-- <form class="input-form" @submit.prevent="onClick">
          <input
            v-model="typedCode"
            class="input"
            type="text"
            placeholder="Type ID"
          />
          <button class="submit-btn">
            <img :src="Search" alt="search" />
          </button>
        </form> -->
      </div>
    </div>
    <div v-else class="error-frame">
      <Spinner class="spinner-container" />
    </div>
  </div>
</template>

<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../assets/logo.png";
import Flesh from "../assets/flesh.svg";
import Close from "../assets/close.svg";
import QRFrame from "../assets/qrFrame.svg";
import Search from "../assets/qrSearch.svg";
import Logout from "../assets/logout.png";
import Profile from "../assets/profile.svg";
import Notifications from "../assets/notifications.svg";
import Settings from "../assets/settings.svg";
import { inject } from "vue";
import Spinner from "../components/Spinner.vue";

export default {
  name: "PassportView",
  components: {
    QrcodeStream,
    Spinner,
  },
  setup() {
    return {
      CatenaLogo,
      Flesh,
      Close,
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
      snackbar: false,
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
.tooltip {
  position: relative;
  display: inline-block;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
}

.error-frame {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 85vh;
}

.qr-container {
  position: fixed;
  z-index: -1;
  top: 132px;
  bottom: 0;
  right: 0;
  left: 0;
}

.qrcode-stream {
  max-width: 500%;
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
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.qrcode-stream-camera {
  width: 160%;
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

.toast-alert {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px 10px 20px 60px;
  margin-top: 10px;
  background-color: #b3cb2c;
  border-radius: 6px;
  top: -12vh;
  left: 50%;
  z-index: 99;
  min-width: auto;
  white-space: nowrap;
  transform: translate(-50%, -50%);
  box-shadow: 3px 3px 20px 3px #e7e7e7;
}

.display-toast-alert {
  top: 4vh;
}

.close {
  padding-left: 20px;
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

  .error-message {
    font-size: 12px;
  }

  .close {
    padding: 0;
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

  .toast-alert {
    padding: 70px 10px 40px 20px;
    width: 100vw;
  }

  .display-toast-alert {
    top: 10px;
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
