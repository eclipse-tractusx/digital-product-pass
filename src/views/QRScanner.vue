<template>
  <qrcode-stream @init="onInit"></qrcode-stream>
  <Footer />
</template>

<script>
// @ is an alias to /src
import { QrcodeStream } from "vue3-qrcode-reader";
import Spinner from "@/components/Spinner.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";
import { AAS_PROXY_URL } from "@/services/service.const";
// import { BASE_URL } from '@/services/service.const';
import { inject } from "vue";

export default {
  name: "PassportView",
  components: {
    QrcodeStream,

    Footer,
    Spinner,
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

<style>
.pass-container {
  width: 76%;
  margin: 0 12% 0 12%;
}

.spinner-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.spinner {
  margin: auto;

  width: 8vh;

  animation: rotate 3s infinite;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 750px) {
  .pass-container {
    width: 100%;
    margin: 0;
  }
}
</style>
