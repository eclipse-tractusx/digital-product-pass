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
      <div class="dpp-id">DPP ID: {{ id }}</div>
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
      style="height: auto; min-width: 60vw"
    >
      <!-- Registry Search step -->
      <v-timeline-item
        :icon="processId ? 'mdi-check' : null"
        :dot-color="[processId ? 'green' : 'grey']"
        :size="processId ? 'large' : 'small'"
        :class="{ completed: processId }"
      >
        <div class="d-flex">
          <div>
            <strong>
              <div class="title" :class="{ 'completed-step': processId }">
                {{ this.stepsNames.registrySearch.stepTitle }}
              </div></strong
            >
            <div class="text-caption">
              <p>
                {{
                  processId
                    ? this.stepsNames.registrySearch.successStepSubtitle
                    : this.stepsNames.registrySearch.initialStepSubtitle
                }}
              </p>
              <p>{{ processId ? "Created Process Id: " + processId : "" }}</p>
            </div>
          </div>
        </div>
      </v-timeline-item>
      <!-- Contract Search step -->
      <v-timeline-item
        :icon="searchContractId ? 'mdi-check' : null"
        :dot-color="[searchContractId ? 'green' : 'grey']"
        :size="searchContractId ? 'large' : 'small'"
        :class="{ completed: searchContractId }"
      >
        <div class="d-flex">
          <div>
            <strong>
              <div
                class="title"
                :class="{
                  'completed-step': searchContractId,
                }"
              >
                {{ this.stepsNames.contractSearch.stepTitle }}
              </div></strong
            >
            <div class="text-caption">
              <p>
                {{
                  searchContractId
                    ? this.stepsNames.contractSearch.successStepSubtitle
                    : this.stepsNames.contractSearch.initialStepSubtitle
                }}
              </p>
              <p>
                {{ searchContractId ? "Contract Id: " + searchContractId : "" }}
              </p>
            </div>
          </div>
        </div>
      </v-timeline-item>
      <!-- Contract Negotiation step -->
      <v-timeline-item
        :icon="
          statusData.data.history['negotiation-accepted'] ? 'mdi-check' : null
        "
        :dot-color="[
          statusData.data.history['negotiation-accepted'] ? 'green' : 'grey',
        ]"
        :size="
          statusData.data.history['negotiation-accepted'] ? 'large' : 'small'
        "
        :class="{
          completed: statusData.data.history['negotiation-accepted'],
        }"
      >
        <div class="d-flex">
          <div>
            <strong>
              <div
                class="title"
                :class="{
                  'completed-step':
                    statusData.data.history['negotiation-accepted'],
                }"
              >
                {{ this.stepsNames.contractNegotiation.stepTitle }}
              </div></strong
            >
            <div class="text-caption">
              <p>
                {{
                  statusData.data.history["negotiation-accepted"]
                    ? this.stepsNames.contractNegotiation.successStepSubtitle +
                      statusData.data.history["negotiation-accepted"].status
                    : this.stepsNames.contractNegotiation.initialStepSubtitle
                }}
              </p>
            </div>
          </div>
        </div>
      </v-timeline-item>
      <!-- Contract Transfer step -->
      <v-timeline-item
        :icon="statusData.data.history['transfer-request'] ? 'mdi-check' : null"
        :dot-color="[
          statusData.data.history['transfer-request'] ? 'green' : 'grey',
        ]"
        :size="statusData.data.history['transfer-request'] ? 'large' : 'small'"
        :class="{
          completed: statusData.data.history['transfer-request'],
        }"
      >
        <div class="d-flex">
          <div>
            <strong>
              <div
                class="title"
                :class="{
                  'completed-step': statusData.data.history['transfer-request'],
                }"
              >
                {{ this.stepsNames.contractTransfer.stepTitle }}
              </div></strong
            >
            <div class="text-caption">
              <p class="step-subtitle">
                {{
                  statusData.data.history["transfer-request"]
                    ? this.stepsNames.contractTransfer.successStepSubtitle
                    : this.stepsNames.contractTransfer.initialStepSubtitle
                }}
              </p>
              <p>
                {{
                  statusData.data.history["transfer-request"]
                    ? "Transfer Id: " +
                      statusData.data.history["transfer-request"].id
                    : ""
                }}
              </p>
            </div>
          </div>
        </div>
      </v-timeline-item>
      <!-- Passport Retrieval step -->
      <v-timeline-item
        :icon="
          statusData.data.history['passport-received'] ? 'mdi-check' : null
        "
        :dot-color="[
          statusData.data.history['passport-received'] ? 'green' : 'grey',
        ]"
        :size="statusData.data.history['passport-received'] ? 'large' : 'small'"
        :class="{
          completed: statusData.data.history['passport-received'],
        }"
      >
        <div class="d-flex">
          <div>
            <strong>
              <div
                class="title"
                :class="{
                  'completed-step':
                    statusData.data.history['passport-received'],
                }"
              >
                {{ this.stepsNames.passportRetrieval.stepTitle }}
              </div></strong
            >
            <div class="text-caption">
              <p>
                {{
                  statusData.data.history["passport-received"]
                    ? this.stepsNames.passportRetrieval.successStepSubtitle
                    : this.stepsNames.passportRetrieval.initialStepSubtitle
                }}
              </p>
            </div>
          </div>
        </div>
      </v-timeline-item>
    </v-timeline>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "LoadingComponent",
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

