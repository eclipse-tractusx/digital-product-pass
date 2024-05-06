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
  <div class="loading-container">
    <v-col class="v-col-auto dpp-id-container">
      <div class="dpp-id">{{ $t("loading.assetId") }} {{ id }}</div>
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
          (statusData.data.history['data-received']
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
        :stepTitle="$t(this.stepsNames.registrySearch.stepTitle)"
        :successStepSubTitle="
          $t(this.stepsNames.registrySearch.successStepSubtitle)
        "
        :initialStepSubTitle="
          $t(this.stepsNames.registrySearch.initialStepSubtitle)
        "
        displayId
        :idLabel="$t('loading.createdProcess') + processId"
      />
      <!-- Contract Search step -->
      <StepperItem
        :condition="searchContractId"
        :stepTitle="$t(this.stepsNames.contractSearch.stepTitle)"
        :successStepSubTitle="
          $t(this.stepsNames.contractSearch.successStepSubtitle)
        "
        :initialStepSubTitle="
          $t(this.stepsNames.contractSearch.initialStepSubtitle)
        "
        displayId
        :idLabel="$t('loading.contractId') + searchContractId"
      />
      <!-- Contract Negotiation step -->
      <StepperItem
        :condition="statusData.data.history['negotiation-accepted']"
        :stepTitle="$t(this.stepsNames.contractNegotiation.stepTitle)"
        :successStepSubTitle="
          $t(this.stepsNames.contractNegotiation.successStepSubtitle)
        "
        :initialStepSubTitle="
          $t(this.stepsNames.contractNegotiation.initialStepSubtitle)
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
        :stepTitle="$t(this.stepsNames.contractTransfer.stepTitle)"
        :successStepSubTitle="
          $t(this.stepsNames.contractTransfer.successStepSubtitle)
        "
        :initialStepSubTitle="
          $t(this.stepsNames.contractTransfer.initialStepSubtitle)
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
        :condition="statusData.data.history['data-received']"
        :stepTitle="$t(this.stepsNames.passportRetrieval.stepTitle)"
        :successStepSubTitle="
          $t(this.stepsNames.passportRetrieval.successStepSubtitle)
        "
        :initialStepSubTitle="
          $t(this.stepsNames.passportRetrieval.initialStepSubtitle)
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
          stepTitle: "loading.registrySearch",
          initialStepSubtitle: "loading.searching",
          successStepSubtitle: "loading.registriesAvailable",
          progressValue: 25,
        },
        contractSearch: {
          stepTitle: "loading.contractSearch",
          initialStepSubtitle: "loading.searching",
          successStepSubtitle: "loading.contractAvailable",
          progressValue: 20,
        },
        contractNegotiation: {
          stepTitle: "loading.contractNegotiation",
          initialStepSubtitle: "loading.negotiating",
          successStepSubtitle: "loading.contractStatus",
          progressValue: 10,
        },
        contractTransfer: {
          stepTitle: "loading.contractTransfer",
          initialStepSubtitle: "loading.transferring",
          successStepSubtitle: "loading.transferCompleted",
          progressValue: 15,
        },
        passportRetrieval: {
          stepTitle: "loading.passportRetrieval",
          initialStepSubtitle: "loading.retrieving",
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

