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
  <div class="home-page-container">
    <div class="left-container" :class="{ hidden: isHidden }">
      <div class="left-container-text">
        <h2>{{ $t("searchView.title") }}</h2>
        <p>{{ $t("searchView.welcomeMessage") }}</p>
      </div>
      <div class="img-container">
        <img :src="BatteryScanning" class="image" alt="Battery scanning" />
      </div>
    </div>
    <div class="right-container">
      <v-icon
        @click="showWelcome"
        size="large"
        :class="{ hidden: !isHidden }"
        class="arrow-icon"
        icon="mdi-arrow-left"
      ></v-icon>
      <div class="inner-right-container">
        <div class="logotype-container">
          <img :src="LogotypeDPP" alt="DPP logo" />
        </div>
        <v-container class="search-page">
          <div v-if="qrError" class="qr-container">
            <div class="text-container">
              <p class="text">{{ $t("searchView.errorCameraOff") }}</p>
              <p class="text">{{ $t("searchView.errorTypeID") }}</p>
              <p class="error">{{ $t(`searchView.cameraError.${qrError}`) }}</p>
            </div>
            <SearchInput class="search-input" />
          </div>
          <v-row data-cy="qr-container">
            <div v-if="!qrError">
              <v-col class="qr-container" cols="12" v-if="QRtoggle">
                <v-btn rounded v-if="multipleCameras">
                  <v-icon
                    icon="mdi-camera-flip-outline"
                    @click="toggleCamera"
                    style="color: #0f71cb"
                  ></v-icon>
                </v-btn>
                <div class="stream-container">
                  <v-icon
                    size="x-large"
                    class="close-btn"
                    @click="closeQRScanner"
                    start
                    md
                    icon="mdi-close-thick"
                  ></v-icon>
                  <QrcodeStream
                    :facingMode="facingMode"
                    v-if="QRtoggle"
                    :key="reloadReader"
                  />
                </div>
              </v-col>
              <v-col cols="12" v-else class="qr-container">
                <SearchInput class="search-input" />
                <v-icon
                  class="qrScanner-btn"
                  @click="hideWelcome"
                  start
                  md
                  icon="mdi-qrcode-scan"
                ></v-icon>
              </v-col>
            </div>
          </v-row>
        </v-container>
      </div>
      <div class="guide">
        📖 {{ $t("searchView.findOutMore") }}
        <a class="advanced-search-link" @click="openExternalLink">{{
          $t("searchView.guide")
        }}</a>
      </div>
    </div>
  </div>
</template>

<script>
import QrcodeStream from "../components/general/QrcodeStrem.vue";
import BatteryScanning from "../media/battery-img.jpeg";
// New picture
// import BatteryScanning from "../media/backgroundart.jpg";
import LogotypeDPP from "../media/logotypeDPP.svg";
import SearchInput from "../components/general/SearchInput.vue";
import { mapState } from "vuex";
import store from "@/store/index";
import { PORTAL_URL } from '../services/service.const';

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
      reloadReader: 0,
      facingMode: "front",

      multipleCameras: true,
    };
  },
  computed: {
    ...mapState(["qrError"]),
  },
  setup() {
    SearchInput;
    return {
      BatteryScanning,
      LogotypeDPP,
    };
  },
  mounted() {
    this.checkCameraPermission();
    this.toggleCamera();
  },
  methods: {
    async toggleCamera() {
      try {
        const devices = await navigator.mediaDevices.enumerateDevices();
        const videoDevices = devices.filter(
          (device) => device.kind === "videoinput"
        );

        if (videoDevices.length > 1) {
          this.facingMode = this.facingMode === "front" ? "rear" : "front";
        } else {
          this.multipleCameras = false;
          this.facingMode = "auto";
        }
      } catch (error) {
        console.error("Error toggling camera:", error);
      }
    },
    async checkCameraPermission() {
      try {
        const permissionStatus = await navigator.permissions.query({
          name: "camera",
        });
        permissionStatus.onchange = () => {
          this.reloadReader += 1;
          store.commit("setQrError", "");
        };
      } catch (error) {
        console.error("Error checking camera permission:", error);
      }
    },
    showWelcome() {
      this.isHidden = false;
      this.QRtoggle = false;
    },
    hideWelcome() {
      this.isHidden = true;
      this.QRtoggle = true;
    },
    closeQRScanner() {
      this.QRtoggle = false;
    },
    openExternalLink() {
      window.open(PORTAL_URL +
        "/documentation/?path=docs",
        "_blank"
      );
    },
  },
};
</script>
