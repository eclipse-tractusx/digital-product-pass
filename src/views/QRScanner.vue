<template>
  <div class="qr-container">
    <img :src="CatenaLogo" alt="logo" class="logo" />
    <p>{{ error }}</p>
    <div class="header-container">
      <div class="empty-pusher" />
      <h1 class="top-layer">Scan QR code</h1>
      <div class="top-layer" @click="torch = !torch">
        <img :src="Flesh" alt="flesh" />
      </div>
    </div>
    <div class="qr-frame">
      <img :src="QRFrame" alt="frame" />
    </div>
    <qrcode-stream
      :torch="torch"
      class="test"
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
</template>

<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../assets/logo.png";
import Flesh from "../assets/flesh.svg";
import QRFrame from "../assets/qrFrame.svg";
import Search from "../assets/qrSearch.svg";
export default {
  name: "PassportView",
  components: {
    QrcodeStream,
  },
  setup() {
    return {
      CatenaLogo,
      Flesh,
      Search,
      QRFrame,
    };
  },

  data() {
    return {
      error: "",
      decodedString: "",
      torch: false,
      MATERIAL_URL: process.env.VUE_APP_MATERIAL_URL,
      typedCode: "",
    };
  },
  async created() {
    this.loading = false;
  },
  methods: {
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

.top-layer {
  z-index: 99;
  opacity: 1;
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
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  height: 400px;
  z-index: 10;
}

.qrcode-stream-overlay {
  position: absolute;
  backdrop-filter: blur(9px);
}

.input-form {
  position: absolute;
  top: 62%;
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
  width: 48px;
}
</style>
