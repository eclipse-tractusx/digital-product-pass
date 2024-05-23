<!--
  Tractus-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->

<template>
  <qrcode-stream
    class="qrcode-stream"
    @init="onInit"
    @decode="onDecode"
    :camera="facingMode"
  >
  </qrcode-stream>
</template>

<script>
import { QrcodeStream } from "vue3-qrcode-reader";
import store from "@/store/index";

export default {
  name: "QRcodeStream",
  components: {
    QrcodeStream,
  },
  data() {
    return {
      error: "",
      decodedString: "",
    };
  },
  props: {
    facingMode: {
      type: String,
      default: "front",
    },
  },
  methods: {
    async onInit(promise) {
      try {
        await promise;
      } catch (error) {
        store.commit("setQrError", error.name);
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
        path: `/${this.processDecodedString(decodedString)}`,
      });
    },
    processDecodedString(decodedString) {
      const match = /CX:.*$/.exec(decodedString);
      // This regex looks for a match "CX:" and returns it + everything to the right
      return match ? match[0] : decodedString;
    },
  },
};
</script>
