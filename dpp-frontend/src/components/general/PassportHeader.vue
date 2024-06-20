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
  <div class="id-container">
    <v-btn
      rounded="pill"
      color="#0F71CB"
      size="large"
      class="back-btn"
      href="/"
      variant="outlined"
      style="border: 2px solid; text-transform: initial"
    >
      <v-icon class="icon" start md icon="mdi-arrow-left"></v-icon>
      {{ $t("passportHeader.backBtn") }}
    </v-btn>
    <div class="id-wrapper">
      <p class="id">
        {{ type }}:
        {{ id ? id : "—" }}
      </p>
    </div>
    <template v-if="verificationData">
      <DialogComponent
        :disabled="!verificationData.vc"
        icon="mdi-check-decagram"
        class="contract-modal"
      >
        <v-btn
          rounded="pill"
          :color="verificationData.vc ? 'green' : 'grey'"
          :disabled="!verificationData.vc"
          size="large"
          class="verification-btn"
          variant="outlined"
          style="border: 2px solid; text-transform: initial"
        >
          <v-icon
            class="icon"
            start
            md
            :icon="
              verificationData.vc
                ? 'mdi-check-decagram'
                : 'mdi-check-decagram-outline'
            "
          ></v-icon>
          {{ $t("passportHeader.verification") }}
        </v-btn>
        <template v-slot:title v-if="verificationData.vc">
          {{ $t("passportHeader.verification") }}
        </template>
        <template v-slot:text v-if="verificationData.vc">
          <ul>
            <li class="verification">
              {{ $t("passportHeader.issuer") }}:
              <span class="verification-value">
                {{ verificationData.issuer }}
              </span>
            </li>
            <li class="verification">
              {{ $t("passportHeader.issuedAt") }}:
              <span class="verification-value">
                {{ callFormatTimestamp(verificationData.issuedAt) }}
              </span>
            </li>
            <li class="verification">
              {{ $t("passportHeader.expirationDate") }}:
              <span class="verification-value">
                {{ callFormatTimestamp(verificationData.expirationDate) }}
              </span>
            </li>
          </ul>
          <div class="btn-background">
            <v-btn
              rounded="pill"
              color="#0F71CB"
              size="large"
              class=""
              href="/"
              style="color: white"
            >
              <v-icon class="icon" start md icon="mdi-refresh"></v-icon>
              {{ $t("passportHeader.reloadVerification") }}
            </v-btn>
          </div>
        </template>
      </DialogComponent>
    </template>
  </div>
</template>

<script>
import DialogComponent from "../../components/general/Dialog.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "PassportHeader",
  components: {
    DialogComponent,
  },
  props: {
    id: {
      type: String,
      default: null,
    },
    verification: {
      type: Object,
      default: Object,
    },
    type: {
      type: String,
      default: "Passport Id",
    },
  },
  data() {
    return {
      verificationData: this.$props.verification,
    };
  },
  methods: {
    callFormatTimestamp(date) {
      return passportUtil.formatTimestamp(date);
    },
  },
};
</script>

<style>
.id-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  width: 100%;
  margin-top: 6em;
  padding: 14px 42px 14px 42px;
}
.id-wrapper {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  line-break: anywhere;
}
.btn-background {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
ul {
  margin-bottom: 36px;
}
.id {
  font-size: 14px;
  line-height: 36px;
  font-weight: bold;
}
.verification {
  padding-bottom: 16px;
}
.verification-value {
  font-weight: 600;
}

@media (max-width: 820px) {
  .id-container {
    padding: 14px 42px 88px 42px;
  }
  .id-wrapper {
    margin-top: 42px;
  }
}
</style>
