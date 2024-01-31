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
                <template v-if="showContractModal">
                  <v-card class="contract-container">
                    <div class="title-container">Choose a policy</div>
                    <v-radio-group class="content-container" v-model="radios">
                      <!-- Loop over the grouped policies -->
                      <template
                        v-for="(group, contractId) in groupedPolicies"
                        :key="contractId"
                      >
                        <div class="policy-group-label">
                          <span class="policy-group-label-mobile"
                            >Contract ID:</span
                          >
                          {{ contractId }}
                        </div>
                        <v-radio
                          v-for="(item, index) in group"
                          :key="`${contractId}_${index}`"
                          @click="chooseContract(contractId, item['@id'])"
                          :value="`${contractId}_${index}`"
                          :label="
                            'Policy type: ' +
                            item['odrl:permission']['odrl:action']['odrl:type']
                          "
                        >
                        </v-radio>
                      </template>
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
                        @click="
                          callAcceptContract(
                            this.contractToSign.contract,
                            this.contractToSign.policy
                          )
                        "
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
                </template>
                <template v-if="declineContractModal">
                  <v-card class="contract-container">
                    <div class="title-container">
                      Are you sure you want to decline?
                    </div>
                    <div class="policy-group-label">
                      <div class="back-to-homepage">
                        This will take you back to the Homepage
                      </div>
                    </div>
                    <v-row class="pt-8 justify-center">
                      <v-btn
                        color="#0F71CB"
                        class="text-none"
                        variant="outlined"
                        @click="cancelDeclineContract()"
                        >Cancel</v-btn
                      >
                      <v-btn
                        class="text-none ms-4 text-white"
                        color="red-darken-4"
                        @click="confirmDeclineContract()"
                        >Yes, Decline</v-btn
                      >
                    </v-row>
                  </v-card>
                </template>
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
    declineContractModal: false,
    showContractModal: true,
  }),
  computed: {
    ...mapState(["searchData", "contractToSign"]),
    groupedPolicies() {
      return this.policies.reduce((groups, policy) => {
        const contractId = Object.keys(policy)[0];
        if (!groups[contractId]) {
          groups[contractId] = [];
        }
        groups[contractId].push(policy[contractId]);
        return groups;
      }, {});
    },
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
      const firstPolicyObj = this.policies[0];
      const initialContractToSign = Object.keys(firstPolicyObj)[0];
      const initialPolicyToSign = firstPolicyObj[initialContractToSign]["@id"];
      // Commit the contract ID to the store
      this.$store.commit("setContractToSign", {
        contract: initialContractToSign,
        policy: initialPolicyToSign,
      });
    } else {
      console.error("No policies found");
    }

    this.shouldShowOverlay();
  },
  methods: {
    extractPolicies(contracts) {
      let contractPolicies = [];

      for (let key in contracts) {
        // eslint-disable-next-line no-prototype-builtins
        if (contracts.hasOwnProperty(key)) {
          const contract = contracts[key];

          if (Array.isArray(contract["odrl:hasPolicy"])) {
            contract["odrl:hasPolicy"].forEach((policy) => {
              let policyEntry = {};
              policyEntry[key] = policy;
              contractPolicies.push(policyEntry);
            });
          } else {
            // Create an entry with the contract key and the policy object
            let policyEntry = {};
            policyEntry[key] = contract["odrl:hasPolicy"];
            contractPolicies.push(policyEntry);
          }
        }
      }
      return (this.policies = contractPolicies);
    },
    toggleDetails() {
      this.details = !this.details;
      if (this.details) {
        this.detailsTitle = "Less details";
      } else {
        this.detailsTitle = "More details";
      }
    },
    chooseContract(contract, policy) {
      console.log("Contract chosen - contractToSign:", this.contractToSign);
      console.log("Contract chosen - policies:", this.policies);

      return (this.contractToSign = store.commit("setContractToSign", {
        contract: contract,
        policy: policy,
      }));
    },
    shouldShowOverlay() {
      if (this.policies.length > 1) {
        return (this.showOverlay = true);
      }
    },
    callAcceptContract(contract, policy) {
      alert("contract: " + contract + " " + "policy: " + policy);
      try {
        // let response = await this.backendService.acceptContract(contract, policy);
        // return response;
      } catch (error) {
        console.error("Error accepting contract", error);
      } finally {
        this.contractItems = [];
      }
    },
    declineContract() {
      this.declineContractModal = true;
      this.showContractModal = false;
    },
    confirmDeclineContract() {
      this.$router.push("/");
    },
    cancelDeclineContract() {
      this.declineContractModal = false;
      this.showContractModal = true;
    },
  },
};
</script>

