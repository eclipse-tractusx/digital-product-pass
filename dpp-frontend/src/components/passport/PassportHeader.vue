<!--
  Tractus-X -  Digital Product Pass Application
  
  Copyright (c) 2022, 2024 BMW AG
  Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

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
      <DialogComponent :disabled="!verificationData.vc" :icon="!verificationData.vc ? 'mdi-shield-off-outline' : (!verificationData.verified ? 'mdi-shield-alert-outline': 'mdi-shield-check')" class="contract-modal">
        <v-btn
          rounded="pill"
          :color="!verificationData.vc ? 'grey' : (!verificationData.verified ? 'red' : 'green')"
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
            :icon="!verificationData.vc ? 'mdi-shield-off-outline' : (!verificationData.verified ? 'mdi-shield-alert-outline': 'mdi-shield-check')"
          ></v-icon>
          {{
            !verificationData.vc ? $t("passportHeader.unverifiable") : (!verificationData.verified
              ? $t("passportHeader.unverified") : $t("passportHeader.verified"))
          }}
        </v-btn>
        <template v-slot:title v-if="verificationData.vc">
          {{
            !verificationData.vc ? $t("passportHeader.unverifiable") : (!verificationData.verified
              ? $t("passportHeader.unverified") : $t("passportHeader.verified"))
          }}
        </template>
        <template v-slot:text v-if="verificationData.vc">
          <div class="verification-list">
            <table style="width:100%">
              <tr class="verification">
                <td v-if="verificationData.owner" class="verification">
                  <span class="verification-label"> {{ $t("passportHeader.owner") }}: </span>
                  <span class="verification-value">
                     {{ verificationData.owner }}
                  </span>
                </td>
                 <td v-if="verificationData.issuer" class="verification">
                  <span class="verification-label"> {{ $t("passportHeader.issuer") }}: </span>
                  <span class="verification-value">
                    {{ verificationData.issuer }}
                  </span>
                </td>
              </tr>
              <tr class="verification" > 
                <td v-if="verificationData.wallet" colspan="2" class="verification">
                <span class="verification-label"> {{ $t("passportHeader.wallet") }}: </span>
                <a style="color:blue" target="_blank" :href="'https://dev.uniresolver.io/#'+verificationData.wallet">
                  <span class="verification-value">
                    {{ verificationData.wallet }}
                  </span>
                </a>
                </td>
              </tr>
              <tr class="verification">
                <td v-if="verificationData.issuedAt" class="verification">
                    <span class="verification-label"> {{ $t("passportHeader.issuedAt") }}: </span>
                <span class="verification-value">
                  {{ callFormatTimestamp(verificationData.issuedAt) }}
                </span>
                </td>
                 <td v-if="verificationData.expiresAt" class="verification">
                <span class="verification-label"> {{ $t("passportHeader.expirationDate") }}: </span>
                <span class="verification-value">
                  {{ callFormatTimestamp(verificationData.expiresAt) }}
                </span>
                </td>
              </tr>
            </table>
            <ul>
              <!-- proof -->
              <div class="proof-container" v-if="verificationData.proof">
                <div class="proof-title" style="width: 100%">
                  <span>
                  {{ $t("passportHeader.proof") }}
                  </span>
                </div>
                
                <div class="jws-container" v-if="verificationData.proof.jws">
                  <div class="verification jws-label">{{ $t("passportHeader.jws") }}:</div>
                  <div class="jws">
                    {{ verificationData.proof.jws }}
                  </div>
                  <v-icon
                    :style="!verificationData.verified ? 'color: red': (reloadVerificationData.message?'color: green':'color: blue')"
                    class="icon"
                    end
                    sm
                    :icon="!verificationData.verified ? 'mdi-lock-open-remove-outline': 'mdi-lock-check-outline'"
                  ></v-icon>
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
                <li class="verification" v-if="reloadVerificationData.lastUpdated">
                  <span class="verification-label"> {{ $t("passportHeader.lastUpdated") }}: </span>
                  <span class="verification-value">
                    {{ reloadVerificationData.lastUpdated }}
                  </span>
                </li>
              </div>
            </ul>
            <ul v-if="verificationData.verified && reloadVerificationData.message">
              <li class="verification" style="color: green">
                  <span class="verification-value">
                    {{ reloadVerificationData.message }}
                  </span>
              </li>
            </ul>
            <ul v-if="!verificationData.verified">
              <li class="verification error">
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
              <v-icon class="icon" start md icon="mdi-shield-sync-outline"></v-icon>
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
        this.verificationData.verified = response.data;
        if(response.data){
          this.verificationData.error = null;
          this.reloadVerificationData.message = response.message;
        }else{
          this.reloadVerificationData.message=null;
          this.verificationData.error = response.message;
        }
        let lastUpdated = new Date().toLocaleString();
        this.reloadVerificationData.lastUpdated = lastUpdated;
      });
    },
  },
};
</script>
