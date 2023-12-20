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
            <template v-if="contractSign && contractItems.length > 1">
              <v-overlay class="contract-modal" v-model="showOverlay">
                <v-card class="contract-container">
                  <v-card-title class="justify-center">
                    Choose a policy
                  </v-card-title>
                  <v-radio-group class="content-container" v-model="radios">
                    <v-radio
                      v-for="(item, index) in contractItems"
                      :key="index"
                      @click="chooseContract(item)"
                      :label="
                        item['odrl:action']['odrl:type'] +
                        ': ' +
                        '[' +
                        item['odrl:constraint']['odrl:or']['odrl:leftOperand'] +
                        ']' +
                        operatorMapper(
                          item['odrl:constraint']['odrl:or']['odrl:operator'][
                            '@id'
                          ]
                        ) +
                        '[' +
                        item['odrl:constraint']['odrl:or'][
                          'odrl:rightOperand'
                        ] +
                        ']'
                      "
                      :value="index"
                    ></v-radio>
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
                        callAcceptContract(this.contractToSign['odrl:target'])
                      "
                      >Agree</v-btn
                    >
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

export default {
  name: "StepperItem",
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
    contractItems: [],
    radios: 0,
  }),
  computed: {
    ...mapState(["searchData", "contractToSign"]),
  },
  async created() {
    this.backendService = new BackendService();
  },
  mounted() {
    this.contractItems =
      this.searchData.contract["odrl:hasPolicy"]["odrl:permission"];
    this.contractToSign = store.commit(
      "setContractToSign",
      this.contractItems[0]
    );

    this.shouldShowOverlay();
  },
  methods: {
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

<style scoped>
.contract-modal {
  justify-content: center;
  align-items: center;
}
.content-container {
  margin-top: 20px;
}
.contract-container {
  padding: 20px 80px 50px 80px;
}
</style>
