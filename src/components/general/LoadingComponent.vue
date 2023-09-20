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
  <div class="loading-container">
    <v-col class="v-col-auto dpp-id-container">
      <div class="dpp-id">Asset Id: {{ id }}</div>
      <v-progress-linear
        :model-value="
          initialProgressValue +
          (processId ? this.stepsNames.registrySearch.progressValue : 0) +
          (searchContractId
            ? this.stepsNames.contractSearch.progressValue
            : 0) +
          (statusData.data.history['negotiation-accepted']
            ? this.stepsNames.contractNegotiation.progressValue
            : 0) +
          (statusData.data.history['transfer-request']
            ? this.stepsNames.contractTransfer.progressValue
            : 0) +
          (statusData.data.history['passport-received']
            ? this.stepsNames.passportRetrieval.progressValue
            : 0)
        "
        color="light-blue"
        height="10"
        striped
      ></v-progress-linear>
    </v-col>
    <v-timeline
      side="end"
      align="start"
      truncate-line="both"
      class="timeline-container"
      style="height: auto; min-width: 60vw"
    >
      <!-- Registry Search step -->

      <StepperItem
        :condition="processId"
        :stepTitle="this.stepsNames.registrySearch.stepTitle"
        :successStepSubTitle="
          this.stepsNames.registrySearch.successStepSubtitle
        "
        :initialStepSubTitle="
          this.stepsNames.registrySearch.initialStepSubtitle
        "
        displayId
        :idLabel="'Created Process Id: ' + processId"
      />
      <!-- Contract Search step -->
      <StepperItem
        :condition="searchContractId"
        :stepTitle="this.stepsNames.contractSearch.stepTitle"
        :successStepSubTitle="
          this.stepsNames.contractSearch.successStepSubtitle
        "
        :initialStepSubTitle="
          this.stepsNames.contractSearch.initialStepSubtitle
        "
        displayId
        :idLabel="'Contract Id: ' + searchContractId"
      />
      <!-- Contract Negotiation step -->
      <StepperItem
        :condition="statusData.data.history['negotiation-accepted']"
        :stepTitle="this.stepsNames.contractNegotiation.stepTitle"
        :successStepSubTitle="
          this.stepsNames.contractNegotiation.successStepSubtitle
        "
        :initialStepSubTitle="
          this.stepsNames.contractNegotiation.initialStepSubtitle
        "
        displayId
        :idLabel="
          statusData.data.history['negotiation-accepted']
            ? statusData.data.history['negotiation-accepted'].status
            : ''
        "
      />
      <!-- Contract Transfer step -->
      <StepperItem
        :condition="statusData.data.history['transfer-request']"
        :stepTitle="this.stepsNames.contractTransfer.stepTitle"
        :successStepSubTitle="
          this.stepsNames.contractTransfer.successStepSubtitle
        "
        :initialStepSubTitle="
          this.stepsNames.contractTransfer.initialStepSubtitle
        "
        displayId
        :idLabel="
          statusData.data.history['transfer-request']
            ? 'Transfer Id: ' + statusData.data.history['transfer-request'].id
            : ''
        "
      />
      <!-- Passport Retrieval step -->
      <StepperItem
        :condition="statusData.data.history['passport-received']"
        :stepTitle="this.stepsNames.passportRetrieval.stepTitle"
        :successStepSubTitle="
          this.stepsNames.passportRetrieval.successStepSubtitle
        "
        :initialStepSubTitle="
          this.stepsNames.passportRetrieval.initialStepSubtitle
        "
      />
    </v-timeline>
  </div>
</template>

<script>
import { mapState } from "vuex";
import StepperItem from "./StepperItem.vue";

export default {
  name: "LoadingComponent",
  components: {
    StepperItem,
  },
  data() {
    return {
      initialProgressValue: 10,
      stepsNames: {
        registrySearch: {
          stepTitle: "Registry Search",
          initialStepSubtitle: "Searching...",
          successStepSubtitle: "Registries available",
          progressValue: 25,
        },
        contractSearch: {
          stepTitle: "Contract Search",
          initialStepSubtitle: "Searching...",
          successStepSubtitle: "Contract available",
          progressValue: 20,
        },
        contractNegotiation: {
          stepTitle: "Contract Negotiation",
          initialStepSubtitle: "Negotiating...",
          successStepSubtitle: "Contract status: ",
          progressValue: 10,
        },
        contractTransfer: {
          stepTitle: "Contract Transfer",
          initialStepSubtitle: "Transferring...",
          successStepSubtitle: "Transfer completed",
          progressValue: 15,
        },
        passportRetrieval: {
          stepTitle: "Passport Retrieval",
          initialStepSubtitle: "Retrieving... ",
          successStepSubtitle: "",
          progressValue: 20,
        },
      },
    };
  },
  props: {
    id: { type: [String, Number], default: "" },
  },
  computed: {
    ...mapState(["statusData", "processId", "searchContractId"]),
  },
  methods: {},
};
</script>

