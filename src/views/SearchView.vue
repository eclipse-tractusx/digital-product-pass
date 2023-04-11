<!--
  Catena-X - Product Passport Consumer Frontend
 
  Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
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
  <div>
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
    <div class="qr-container" data-cy="qr-container">
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
          <SearchInput />
        </div>
      </div>
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
  setup() {
    SearchInput;
    return {
      CatenaLogo,
      QRFrame,
    };
  },

  data() {
    return {
      QRtoggle: false,
      error: "",
      decodedString: "",
    };
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
  },
};
</script>

<style scoped>
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
  margin-bottom: 290px;
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
  .qr-container {
    margin-bottom: 0;
  }
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
}
@media (max-width: 670px) {
  .qr-container {
    margin-bottom: 0;
  }
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
  .qr-container {
    margin-bottom: 0;
  }
  .qr-frame {
    position: absolute;
    top: 75% !important;
    left: 50% !important;
    transform: translate(-50%, -50%);
    z-index: 10;
  }
}
</style>
