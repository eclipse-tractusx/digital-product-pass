<template>
  <div class="container">
    <div class="left-container" :class="{ hidden: isHidden }">
      <h2>Catena-X Battery Passport</h2>
      <p>
        The term "Battery Passport" refers to a digital document that contains
        essential information about a battery, specifically in the context of
        electric vehicles (EVs). This document includes details about the
        battery's manufacturer, specifications, performance characteristics,
        health status, and lifecycle data. The purpose of a Battery Passport is
        to facilitate proper maintenance, servicing, and recycling of the
        battery, while also enabling traceability and compliance.
      </p>
    </div>
    <div class="right-container">
      <button class="toggle-button" @click="toggleVisibility">
        <v-icon
          class="arrow-icon"
          :icon="isHidden ? 'mdi-arrow-right' : 'mdi-arrow-left'"
        ></v-icon>
      </button>
      <v-container class="search-page">
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
          <SearchInput />
        </div>
        <v-row data-cy="qr-container">
          <div v-if="!error">
            <v-col class="qr-container" cols="12" v-if="QRtoggle">
              <div class="qr-frame">
                <img :src="QRFrame" alt="frame" class="frame" />
              </div>
              <qrcode-stream
                :torch="torch"
                class="qrcode-stream"
                @init="onInit"
                @decode="onDecode"
              ></qrcode-stream>
            </v-col>
            <v-col cols="12" v-else class="qr-container">
              <SearchInput />
            </v-col>
          </div>
        </v-row>
      </v-container>
    </div>
  </div>
</template>



<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../media/logo.png";
import QRFrame from "../media/qrFrame.svg";
import SearchInput from "../components/general/SearchInput.vue";

export default {
  name: "QRScannerView",
  components: {
    QrcodeStream,
    SearchInput,
  },
  data() {
    return {
      isHidden: false,
      QRtoggle: true,
      error: "",
      decodedString: "",
    };
  },
  setup() {
    SearchInput;
    return {
      CatenaLogo,
      QRFrame,
    };
  },
  methods: {
    toggleVisibility() {
      this.isHidden = !this.isHidden;
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
  },
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: space-between;
  height: 90vh;
  padding: 100px 35px 0 35px;
}

.left-container {
  width: 50%;
  padding: 20px;
  background-color: #fff;
  position: relative;
  border: 1px solid #dcdcdc;
  border-radius: 10px;
}

.hidden {
  display: none;
}

.right-container {
  flex: 1; /* Adjusted to take remaining space */
  padding: 20px;
  background-color: #fff;
  text-align: center;
  position: relative;
  border: 1px solid #dcdcdc;
  border-radius: 10px;
}

.toggle-button {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 2px solid #0f71cb;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
}

.arrow-icon {
  color: #0f71cb; /* Updated color */
  font-size: 20px;
}
</style>
