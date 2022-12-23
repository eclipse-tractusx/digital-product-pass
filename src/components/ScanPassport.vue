<template>
  <img
    class="logo"
    alt="catena-x logo"
    src="../assets/catenaX-logo.png"
    style="margin-top: 150px"
  />
  <br />
  <div class="margin-top">
    <p class="h4">Scan Battery Passport</p>
  </div>
  <br />
  <div>
    <img height="200" alt="QR code" src="../assets/QR.png" width="200" />
  </div>
  <div class="margin-top" hidden>
    <div class="container">
      <div class="col-md-4 center">
        <input
          v-model="host"
          class="form-control"
          placeholder="http://host:port"
          type="text"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          v-model="provider"
          class="form-control"
          placeholder="Battery Provider"
          type="text"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          v-model="batteryNumber"
          class="form-control"
          placeholder="Battery Number"
          type="text"
        />
      </div>
      <br />

      <div class="col-md-4 center">
        <button class="btn btn-success btn-signup" @click="generateQRCode">
          Generate QR code
        </button>
      </div>
      <div>
        <img alt="generated QR code URL" :src="generatedQrCodeUrl" />
      </div>

      <div>
        <button class="btn btn-success btn-signup" @click="redirect">
          Get access
        </button>
      </div>
    </div>
  </div>
</template>

<script type="text/jsx">
import { GOOGLE_CHART_API_URL } from "@/services/service.const";

export default {
  name: "ScanPassport",
  components: {},
  data() {
    return {
      host: "",
      provider: "",
      batteryNumber: "",
      generatedQrCodeUrl: "",
      link: "",
    };
  },
  mounted() {
    alert("salsa");
  },
  methods: {
    generateQRCode() {
      let url = `${this.host}/providers/${this.provider}/battery/${this.batteryNumber}`;
      this.link = url;
      if (this.host === "" || this.provider === "" || this.batteryNumber === "")
        alert("Please fill all fields..!");
      else
        this.generatedQrCodeUrl = `${GOOGLE_CHART_API_URL}/chart?cht=qr&chs=400x400&chl=${url}`;
    },
    // readfromwebcam(){
    //   var Instascan = require('instascan');
    //   var scanner = new Instascan.Scanner({ video: document.getElementById('preview') });
    //   scanner.addListener('scan', function (content, image) {
    //     console.log(content);
    //   });

    //   Instascan.Camera.getCameras().then(function (cameras) {
    //     if (cameras.length > 0) {
    //       scanner.start(cameras[0]);
    //     }
    //   });
    // },
    redirect() {
      window.location.href = "http://localhost:8080";
      localStorage.setItem(
        "battery-info",
        JSON.stringify({ provider: this.provider, battery: this.batteryNumber })
      );
    },
  },
};
</script>
