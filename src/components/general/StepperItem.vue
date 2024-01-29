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
  <v-timeline-item
    :icon="condition ? 'mdi-check' : null"
    :dot-color="[condition ? 'green' : 'grey']"
    :size="condition ? 'large' : 'small'"
    :class="{ completed: condition }"
  >
    <div class="timeline-item">
      <div>
        <strong>
          <div class="step-title" :class="{ 'completed-step': condition }">
            {{ stepTitle }}
          </div></strong
        >
        <div class="text-caption-container">
          <p>
            {{ condition ? successStepSubTitle : initialStepSubTitle }}
          </p>
          <div v-if="displayId">
            <p class="text-caption">
              {{ condition ? idLabel : "" }}
            </p>
            <template v-if="contractSign && policies.length > 1">
              <v-overlay class="contract-modal" v-model="showOverlay">
                <v-card class="contract-container">
                  <div class="title-container">Choose a policy</div>
                  <v-radio-group class="content-container" v-model="radios">
                    <v-radio
                      v-for="(item, index) in policies"
                      :key="index"
                      @click="chooseContract(item)"
                      :value="index"
                      :label="
                        'Policy type: ' +
                        item['odrl:permission']['odrl:action']['odrl:type']
                      "
                    >
                    </v-radio>
                  </v-radio-group>
                  <v-row class="pt-8 justify-center">
                    <v-btn
                      color="#0F71CB"
                      class="text-none"
                      variant="outlined"
                      @click="declineContract()"
                      >Decline</v-btn
                    >
                    <v-btn
                      class="text-none ms-4 text-white"
                      color="#0F71CB"
                      @click="callAcceptContract(this.contractToSign['@id'])"
                      >Agree</v-btn
                    >
                  </v-row>
                  <v-row>
                    <v-btn
                      variant="text"
                      @click="toggleDetails"
                      class="details-btn text-none"
                    >
                      {{ detailsTitle }}
                    </v-btn>
                  </v-row>
                  <v-row v-if="details">
                    <div class="json-viewer-container">
                      <JsonViewer
                        class="json-viewer"
                        :value="contractItems"
                        sort
                        theme="jv-light"
                      />
                    </div>
                  </v-row>
                </v-card>
              </v-overlay>
            </template>
          </div>
        </div>
      </div>
    </div>
  </v-timeline-item>
</template>

<script>
import { mapState } from "vuex";
import store from "../../store/index";
import BackendService from "@/services/BackendService";
import { JsonViewer } from "vue3-json-viewer";
import "vue3-json-viewer/dist/index.css";
import { reactive } from "vue";
export default {
  name: "StepperItem",
  components: {
    JsonViewer,
  },
  props: {
    condition: { type: [String, Number], default: "" },
    stepTitle: { type: [String, Number], default: "" },
    successStepSubTitle: { type: [String, Number], default: "" },
    initialStepSubTitle: { type: [String, Number], default: "" },
    displayId: { type: Boolean, default: false },
    contractSign: { type: Boolean, default: false },
    idLabel: { type: [String, Number], default: "" },
  },
  data: () => ({
    showOverlay: false,
    contractItems: reactive([]),
    radios: 0,
    details: false,
    detailsTitle: "More details",
    policies: [],
  }),
  computed: {
    ...mapState(["searchData", "contractToSign"]),
  },
  async created() {
    this.backendService = new BackendService();
  },

  mounted() {
    // Initialize contractItems from searchData
    this.contractItems = this.searchData.contracts;

    // Extract policies
    this.extractPolicies(this.contractItems);

    // Check if policies array has elements and then access the @id of the first element
    if (this.policies.length > 0) {
      const contractId = this.policies[0];

      // Commit the contract ID to the store
      this.$store.commit("setContractToSign", contractId);

      // Store the ID in state
      this.contractToSign = contractId;
    } else {
      console.error("No policies found");
    }

    this.shouldShowOverlay();
  },
  methods: {
    extractPolicies(contracts) {
      const policies = [];
      contracts.forEach((contract) => {
        if (contract["odrl:hasPolicy"]) {
          // Check if 'odrl:hasPolicy' is an array
          if (Array.isArray(contract["odrl:hasPolicy"])) {
            contract["odrl:hasPolicy"].forEach((policy) => {
              policies.push(policy);
            });
          } else {
            // 'odrl:hasPolicy' is a single object
            policies.push(contract["odrl:hasPolicy"]);
          }
        }
      });
      return (this.policies = policies);
    },
    toggleDetails() {
      this.details = !this.details;
      if (this.details) {
        this.detailsTitle = "Less details";
      } else {
        this.detailsTitle = "More details";
      }
    },
    operatorMapper(operator) {
      let opr = operator.replace("odrl:", "");
      if (opr == "eq") {
        return " = ";
      }
      return opr;
    },
    chooseContract(contract) {
      return (this.contractToSign = store.commit(
        "setContractToSign",
        contract
      ));
    },
    shouldShowOverlay() {
      if (this.contractItems.length > 1) {
        return (this.showOverlay = true);
      }
    },
    async callAcceptContract(contractToSign) {
      try {
        // let response = await this.backendService.acceptContract(contractToSign);
        // return response;
        alert(contractToSign);
      } catch (error) {
        console.error("Error accepting contract", error);
      } finally {
        this.contractItems = [];
      }
    },
    declineContract() {
      this.$router.push("/");
    },
  },
};
</script>

