<template>
  <div class="qr-container">
    <img :src="CatenaLogo" alt="logo" class="logo" />
    <div class="header-container">
      <div>back</div>
      <div>Scan QR code</div>
      <div>flash</div>
    </div>
    <div class="qr-frame"></div>
    <qrcode-stream class="test" @init="onInit"></qrcode-stream>
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
    return {};
  },
  async created() {
    this.loading = false;
  },
  methods: {},
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
