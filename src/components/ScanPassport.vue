<template>
  <img style="margin-top:150px" src="../assets/catenaX-logo.png" class="logo"/>
  <br />
  <div class="margin-top">
    <p class="h4">Scan Battery Passport</p>
  </div>
  <br />
   <div>
        <img src="../assets/QR.png" width="200" height="200" />
      </div>
  <div class="margin-top" hidden>
    <div class="container">
      <div class="col-md-4 center">
        <input
          class="form-control"
          v-model="host"
          type="text"
          placeholder="http://host:port"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          class="form-control"
          v-model="provider"
          type="text"
          placeholder="Battery Provider"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          class="form-control"
          v-model="batteryNumber"
          type="text"
          placeholder="Battery Number"
        />
      </div>
      <br />

      <div class="col-md-4 center">
        <button class="btn btn-success btn-signup" v-on:click="generateQRCode">
          Generate QR code
        </button>
      </div>
      <div>
        <img :src=generatedQrCodeUrl />
      </div>

      <div>
         <button class="btn btn-success btn-signup" v-on:click="redirect">
          Get access
        </button>
      </div>

    </div>
  </div>
</template>

<script type="text/jsx">
import { QrcodeStream, QrcodeDropZone, QrcodeCapture } from 'vue-qrcode-reader'

export default {
  components: {
    QrcodeStream,
    QrcodeDropZone,
    QrcodeCapture
  },
  name: "ScanPassport",
  data() {
    return {
      host: '',
      provider:'',
      batteryNumber:'',
      generatedQrCodeUrl:'',
      link:''
    };
  },
  mounted(){
      alert('sasas')
  },
  methods: {
    generateQRCode() {
      let url = `${this.host}/providers/${this.provider}/battery/${this.batteryNumber}`;
      this.link = url
      if (this.host == '' || this.provider == '' || this.batteryNumber == '')
        alert('Please fill all fields..!')
      else
        this.generatedQrCodeUrl = `https://chart.googleapis.com/chart?cht=qr&chs=400x400&chl=${url}`;
      
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
    redirect(url){

      window.location.href = "http://localhost:8080"
      localStorage.setItem("battery-info",JSON.stringify({"provider": this.provider, "battery": this.batteryNumber}))



    }
  },
  mounted() {
  },
};
</script>