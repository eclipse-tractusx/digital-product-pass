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
      <DialogComponent :disabled="!verificationData.vc" icon="mdi-check-decagram" class="contract-modal">
        <v-btn
          rounded="pill"
          :color="!verificationData.verified ? 'red' : !verificationData.vc ? 'grey' : 'green'"
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
            :icon="verificationData.vc ? 'mdi-check-decagram' : 'mdi-check-decagram-outline'"
          ></v-icon>
          {{
            !verificationData.verified || !verificationData.vc
              ? $t("passportHeader.unverified")
              : $t("passportHeader.verification")
          }}
        </v-btn>
        <template v-slot:title v-if="verificationData.vc">
          {{ $t("passportHeader.verification") }}
        </template>
        <template v-slot:text v-if="verificationData.vc">
          <div v-if="!verificationData.error">
            <ul>
              <li class="verification" v-if="verificationData.owner">
                <span class="verification-label"> {{ $t("passportHeader.owner") }}: </span>
                <span class="verification-value">
                  {{ verificationData.owner }}
                </span>
              </li>
              <li class="verification" v-if="verificationData.issuer">
                <span class="verification-label"> {{ $t("passportHeader.issuer") }}: </span>
                <span class="verification-value">
                  {{ verificationData.issuer }}
                </span>
              </li>
              <li class="verification" v-if="verificationData.wallet">
                <span class="verification-label"> {{ $t("passportHeader.wallet") }}: </span>
                <span class="verification-value">
                  {{ verificationData.wallet }}
                </span>
              </li>
              <li class="verification" v-if="verificationData.issuedAt">
                <span class="verification-label"> {{ $t("passportHeader.issuedAt") }}: </span>
                <span class="verification-value">
                  {{ callFormatTimestamp(verificationData.issuedAt) }}
                </span>
              </li>
              <li class="verification" v-if="verificationData.expiresAt">
                <span class="verification-label"> {{ $t("passportHeader.expirationDate") }}: </span>
                <span class="verification-value">
                  {{ callFormatTimestamp(verificationData.expiresAt) }}
                </span>
              </li>
              <!-- proof -->
              <div class="proof-container" v-if="verificationData.proof">
                <div class="proof-title">
                  {{ $t("passportHeader.proof") }}
                </div>
                <div class="jws-container" v-if="verificationData.proof.jws">
                  <div class="verification jws-label">{{ $t("passportHeader.jws") }}:</div>
                  <div class="jws">
                    {{ verificationData.proof.jws }}
                  </div>
                </div>
                <v-divider></v-divider>
                <div class="field-container">
                  <div v-if="verificationData.proof.type">
                    <span class="verification-label proof-value"> {{ $t("passportHeader.type") }}: </span>
                    <span class="verification-value proof-value">
                      {{ verificationData.proof.type }}
                    </span>
                  </div>
                  <div v-if="verificationData.proof.type">
                    <span class="verification-label proof-value"> {{ $t("passportHeader.proofPurpose") }}: </span>
                    <span class="verification-value proof-value">
                      {{ verificationData.proof.proofPurpose }}
                    </span>
                  </div>
                  <div v-if="verificationData.proof.type">
                    <span class="verification-label proof-value"> {{ $t("passportHeader.verificationMethod") }}: </span>
                    <span class="verification-value proof-value">
                      {{ verificationData.proof.verificationMethod }}
                    </span>
                  </div>
                  <div v-if="verificationData.proof.type">
                    <span class="verification-label proof-value"> {{ $t("passportHeader.created") }}: </span>
                    <span class="verification-value proof-value">
                      {{ verificationData.proof.created }}
                    </span>
                  </div>
                </div>
              </div>
              <div class="reload-verification" v-if="reloadVerificationData">
                <li class="verification" v-if="reloadVerificationData.status">
                  <span class="verification-label"> {{ $t("passportHeader.status") }}: </span>
                  <span class="verification-value">
                    {{
                      reloadVerificationData.status === 200
                        ? $t("passportHeader.verified")
                        : $t("passportHeader.unverified")
                    }}
                  </span>
                </li>
                <li class="verification" v-if="reloadVerificationData.message">
                  <span class="verification-label"> {{ $t("passportHeader.message") }}: </span>
                  <span class="verification-value">
                    {{ reloadVerificationData.message }}
                  </span>
                </li>
                <li class="verification" v-if="reloadVerificationData.lastUpdated">
                  <span class="verification-label"> {{ $t("passportHeader.lastUpdated") }}: </span>
                  <span class="verification-value">
                    {{ reloadVerificationData.lastUpdated }}
                  </span>
                </li>
              </div>
            </ul>
          </div>
          <div v-else>
            <ul>
              <li class="verification">
                <span class="verification-label"> {{ $t("passportHeader.error") }}: </span>
                <span class="verification-value">
                  {{ verificationData.error }}
                </span>
              </li>
            </ul>
          </div>
          <div class="btn-wrapper">
            <v-btn
              rounded="pill"
              color="#0F71CB"
              size="large"
              class=""
              @click="reloadVerification()"
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
import { inject } from "vue";
import BackendService from "@/services/BackendService";

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
    vcAspect: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      verificationData: this.$props.verification,
      reloadVerificationData: {
        message: null,
        status: null,
        lastUpdated: null,
      },
      aspect: this.$props.vcAspect,
      auth: inject("authentication"),
    };
  },

  methods: {
    callFormatTimestamp(date) {
      return passportUtil.formatTimestamp(date);
    },
    reloadVerification() {
      this.backendService = new BackendService();
      let result = this.backendService.reloadVerification(this.auth, this.aspect);
      result.then((response) => {
        this.reloadVerificationData.status = response.status;
        this.reloadVerificationData.message = response.message;
        let lastUpdated = new Date().toLocaleString();
        this.reloadVerificationData.lastUpdated = lastUpdated;
      });
    },
  },
};
</script>
