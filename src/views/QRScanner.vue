<template>
  <div class="qr-container">
    <img :src="CatenaLogo" alt="logo" class="logo" />
    <p>{{ error }}</p>
    <p>{{ decodedString }}</p>
    <div class="header-container">
      <div>back</div>
      <div>Scan QR code</div>
      <div @click="torch = !torch">flash</div>
    </div>
    <div class="qr-frame"></div>
    <qrcode-stream
      :torch="torch"
      class="test"
      @init="onInit"
      @decode="onDecode"
    ></qrcode-stream>
  </div>
</template>

<script>
// @ is an alias to /src
import { QrcodeStream } from "vue3-qrcode-reader";
import CatenaLogo from "../assets/logo.png";
export default {
  name: "PassportView",
  components: {
    QrcodeStream,
  },
  setup() {
    return {
      CatenaLogo,
    };
  },

  data() {
    return {
      error: "",
      decodedString: "",
      torch: false,
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
      console.log(decodedString);
      window.location.replace(decodedString);
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
  background-color: #fff;
  opacity: 0.3;
  z-index: 3;
  box-shadow: 0 0 10px 0 black;
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
  width: 300px;
  height: 300px;
  border: 6px solid #b3cb2c;
  z-index: 10;
}

.qrcode-stream-overlay {
  position: absolute;
  backdrop-filter: blur(9px);
}

.test {
}
</style>
