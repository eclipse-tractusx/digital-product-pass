<template>
  <div class="container">
    <div class="left-container" :class="{ hidden: isHidden }">
      <div class="left-container-text">
        <h2>Catena-X Battery Passport</h2>
        <p>
          The term "Battery Passport" refers to a digital document that contains
          essential information about a battery, specifically in the context of
          electric vehicles (EVs). This document includes details about the
          battery's manufacturer, specifications, performance characteristics,
          health status, and lifecycle data. The purpose of a Battery Passport
          is to facilitate proper maintenance, servicing, and recycling of the
          battery, while also enabling traceability and compliance.
        </p>
      </div>
      <div class="img-container">
        <img :src="BatteryScanning" class="image" alt="Battery scanning" />
      </div>
    </div>
    <div class="right-container">
      <div class="logotype-container">
        <img :src="LogotypeDPP" alt="DPP logo" />
      </div>

      <v-icon
        @click="showWelcome"
        size="large"
        :class="{ hidden: !isHidden }"
        class="arrow-icon"
        icon="mdi-arrow-right"
      ></v-icon>

      <v-icon
        @click="hideWelcome"
        size="large"
        :class="{ hidden: isHidden }"
        class="arrow-icon"
        icon="mdi-arrow-left"
      ></v-icon>

      <v-container class="search-page">
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
              <div class="stream-container">
                <v-icon
                  size="x-large"
                  class="close-btn"
                  @click="closeQRScanner"
                  start
                  md
                  icon="mdi-close-thick"
                ></v-icon>
                <qrcode-stream
                  :torch="torch"
                  class="qrcode-stream"
                  @init="onInit"
                  @decode="onDecode"
                ></qrcode-stream>
              </div>
            </v-col>
            <v-col cols="12" v-else class="qr-container">
              <SearchInput />
              <v-icon
                class="qrScanner-btn"
                :class="{ hidden: !isHidden }"
                @click="openQRScanner"
                start
                md
                icon="mdi-qrcode-scan"
              ></v-icon>
            </v-col>
          </div>
        </v-row>
      </v-container>
      <div class="guide">
        📖 Want to find out more? Read our Get
        <a class="advanced-search-link" @click="openExternalLink"
          >Started Guide</a
        >
      </div>
    </div>
  </div>
</template>



<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../media/logo.png";
import QRFrame from "../media/qrFrame.svg";
import BatteryScanning from "../media/battery-img.jpeg";
import LogotypeDPP from "../media/logotypeDPP.svg";
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
      QRtoggle: false,
      error: "",
      decodedString: "",
    };
  },
  setup() {
    SearchInput;
    return {
      BatteryScanning,
      LogotypeDPP,
      CatenaLogo,
      QRFrame,
    };
  },
  methods: {
    hideWelcome() {
      this.isHidden = true;
      this.QRtoggle = true;
    },
    showWelcome() {
      this.isHidden = false;
      this.QRtoggle = false;
    },
    closeQRScanner() {
      this.QRtoggle = false;
    },
    openQRScanner() {
      this.QRtoggle = true;
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
    openExternalLink() {
      window.open(
        "https://portal.int.demo.catena-x.net/documentation/?path=docs",
        "_blank"
      );
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
  gap: 32px;
}

.left-container {
  width: 50%;
  position: relative;
}

.left-container-text {
  padding: 45px;
  margin-bottom: 23px;
  background-color: #fff;
  position: relative;
  border: 1px solid #dcdcdc;
  border-radius: 10px;
  height: 49%;
}

h2 {
  margin-bottom: 20px;
}

.guide {
  position: absolute;
  left: 50%;
  bottom: 38px;
  width: 80%;
  transform: translate(-50%, -50%);
  font-weight: normal;
  font-size: 16px;
}

.img-container {
  width: 100%;
  height: 49%;
  position: relative;
  border-radius: 10px;
  overflow: hidden;
}

.image {
  width: 100%;
  height: auto;
  background-size: cover;
  background-repeat: no-repeat;

  background-position: center;
}

.hidden {
  display: none;
}

.right-container {
  flex: 1;
  padding: 20px;
  background-color: #fff;
  text-align: center;
  position: relative;
  border: 1px solid #dcdcdc;
  border-radius: 10px;
}

.arrow-icon {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 2px solid #0f71cb;
  background-color: #fff;
  color: #0f71cb;
  font-size: 20px;
  cursor: pointer;
}
.advanced-search-link {
  color: #0f71cb;
  text-decoration: underline;
  cursor: pointer;
}

.logotype-container {
  margin-top: 26px;
}

.qrcode-stream {
  border: 2px solid #0f71cb;
  border-radius: 8px;
}
.stream-container {
  position: relative;
  width: 380px;
  height: 380px;
}
.close-btn {
  position: absolute;
  right: 10px;
  top: 20px;
  color: #0f71cb;
  font-size: 20px;
  cursor: pointer;
  z-index: 10;
}
.qrScanner-btn {
  position: absolute;
  right: 0px;
  top: 29px;
  height: 56px;
  width: 56px;
  color: #0f71cb;
  background-color: #f4f7fa;
  font-size: 20px;
  cursor: pointer;
  z-index: 10;
  border: 1px solid #0f71cb;
  border-radius: 0 8px 8px 0;
}
</style>
