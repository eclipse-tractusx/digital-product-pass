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
  <div v-if="!error" class="switch-container">
    <div>
      <v-switch
        v-model="QRtoggle"
        color="#0F71CB"
        label="QR Code Scanner"
      ></v-switch>
    </div>
  </div>
  <div v-if="error" class="qr-container">
    <div class="text-container">
      <p class="text">Your camera is off.</p>
      <p class="text">Turn it on or type the ID.</p>
      <p class="error">{{ error }}</p>
    </div>
    <v-form class="form">
      <div class="input-form">
        <input
          v-model="typedCode"
          class="input"
          type="text"
          placeholder="Type ID"
        />
      </div>
      <v-btn
        rounded="pill"
        color="#0F71CB"
        size="small"
        class="submit-btn"
        @click="onClick"
      >
        Search
        <v-icon class="icon" start md icon="mdi-arrow-right"></v-icon>
      </v-btn>
    </v-form>
  </div>
  <div class="qr-container" data-cy="qr-container">
    <router-link to="/dashboard"> </router-link>
    <div v-if="!error">
      <div v-if="QRtoggle">
        <div class="qr-frame">
          <img :src="QRFrame" alt="frame" class="frame" />
        </div>
        <qrcode-stream
          :torch="torch"
          class="qrcode-stream"
          @init="onInit"
          @decode="onDecode"
        ></qrcode-stream>
      </div>
      <div v-else class="qr-container">
        <v-form class="form">
          <div class="input-form">
            <input
              v-model="typedCode"
              class="input"
              type="text"
              placeholder="Type ID"
            />
          </div>
          <v-btn
            rounded="pill"
            color="#0F71CB"
            size="small"
            class="submit-btn"
            @click="onClick"
          >
            Search
            <v-icon class="icon" start md icon="mdi-arrow-right"></v-icon>
          </v-btn>
        </v-form>
      </div>
    </div>
  </div>
</template>

<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../media/logo.png";
import Flesh from "../media/flesh.svg";
import Close from "../media/close.svg";
import QRFrame from "../media/qrFrame.svg";
import Search from "../media/qrSearch.svg";
import Logout from "../media/logout.png";
import Profile from "../media/profile.svg";
import Notifications from "../media/notifications.svg";
import Settings from "../media/settings.svg";
import { inject } from "vue";

export default {
  name: "QRScannerView",
  components: {
    QrcodeStream,
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
      QRtoggle: false,
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
      console.log("clicked");
    },
  },
};
</script>

<style scoped>
.icon {
  padding-left: 20px;
}

.switch-container {
  display: flex;
  justify-content: flex-end;
  margin: 14em 0 0 0;
}

.error {
  font-weight: bold;
  text-align: center;
  padding: 0 0 70px 0;
}

.qr-container {
  top: 132px;
  bottom: 0;
  right: 0;
  left: 0;
}

.qrcode-stream {
  max-width: 500%;
}

.text-container {
  margin: 250px 0 0 0;
}

.text {
  font-size: 2rem;
  text-align: center;
}

.qr-frame {
  position: absolute;
  top: 60%;
  left: 43%;
  transform: translate(-50%, -50%);
  width: 250px;
  height: 250px;
  z-index: 10;
}

.form {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.input-form {
  padding: 17px;
  background: linear-gradient(to right, #f8b500, #f88000);
  border-radius: 35px;
}
.input {
  width: 560px;
  border-radius: 20px;
  color: #444;
  padding: 18px;
  padding-left: 60px;
  font-size: 20px;
  outline: none;
  background-image: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCI+PGRlZnM+PHN0eWxlPi5jbHMtMXtmaWxsOiNhMGEwYTA7fS5jbHMtMntmaWxsOiNhMGEwYTA7fTwvc3R5bGU+PC9kZWZzPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTE1LjUgMTRoLS43OWwtLjI4LS4yN0MxNS40MSAxMi41OSAxNiAxMS4xMSAxNiA5LjUgMTYgNS45MSAxMy4wOSAzIDkuNSAzUzMgNS45MSAzIDkuNSA1LjkxIDE2IDkuNSAxNmMxLjYxIDAgMy4wOS0uNTkgNC4yMy0xLjU3bC4yNy4yOHYuNzlsNSA0Ljk5TDIwLjQ5IDE5bC00Ljk5LTV6bS02IDBDNy4wMSAxNCA1IDExLjk5IDUgOS41UzcuMDEgNSA5LjUgNSAxNCA3LjAxIDE0IDkuNSAxMS45OSAxNCA5LjUgMTR6Ii8+PC9zdmc+Cg==);
  background-repeat: no-repeat;
  background-position: 16px center;
  background-size: 32px 32px;
  border: 1px solid lightgray;
  border-radius: 25px;
  background-color: white;
}

.submit-btn {
  margin-top: 30px;
  height: 56px;
  width: 185px;
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  background: none;
  border: none;
  cursor: pointer;
}

p {
  font-size: 14px;
  font-weight: 500;
}

@media (max-width: 1024px) {
  .qr-frame {
    position: absolute;
    top: 68%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 250px;
    height: 250px;
    z-index: 10;
  }
}

@media (max-width: 856px) {
  .frame {
    width: 250px;
    height: 250px;
  }
  .qr-frame {
    width: 250px;
    height: 250px;
  }
  .qr-frame {
    position: absolute !important;
    top: 70% !important;
    left: 50% !important;
    transform: translate(-50%, -50%) !important;
    z-index: 10 !important;
  }
  .input {
    position: relative;
    width: 50vw;
  }
}
@media (max-width: 670px) {
  .qr-frame {
    position: absolute !important;
    top: 72% !important;
    left: 50% !important;
    transform: translate(-50%, -50%) !important;
    z-index: 10 !important;
  }

  .frame {
    width: 150px;
    height: 150px;
  }
  .qr-frame {
    width: 150px;
    height: 150px;
  }
}
@media (max-width: 570px) {
  .qr-frame {
    position: absolute;
    top: 75% !important;
    left: 50% !important;
    transform: translate(-50%, -50%);
    z-index: 10;
  }
}

@media (max-width: 375px) {
  .input {
    position: relative;
    width: 80vw;
  }
}
</style>
