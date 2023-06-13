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

<template >
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <template v-if="propsData.negotiation">
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <div class="element-chart-label">Negotiation</div>
            <Field
              icon="mdi-file-swap-outline"
              label="Contract ID"
              :value="propsData.negotiation.id"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Creation Date"
              :value="formattedDate(propsData.negotiation.createdAt)"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Update Date"
              :value="formattedDate(propsData.negotiation.updatedAt)"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Contract type"
              :value="propsData.negotiation.type"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Contract state"
              :value="propsData.negotiation.state"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Contract agreement ID"
              :value="propsData.negotiation.contractAgreementId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Counter Party Address"
              :value="propsData.negotiation.counterPartyAddress"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Protocol"
              :value="propsData.negotiation.protocol"
            />
          </v-col>
        </template>
        <template v-if="propsData.contractOffer">
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <div class="element-chart-label">Contract Offer</div>

            <Field
              icon="mdi-file-swap-outline"
              label="Connector ID"
              :value="propsData.contractOffer.connectorId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Contract offer ID"
              :value="propsData.contractOffer.id"
            />
          </v-col>
        </template>
        <template v-if="propsData.transfer">
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <div class="element-chart-label">Transfer</div>

            <Field
              icon="mdi-file-swap-outline"
              label="Transfer ID"
              :value="propsData.transfer.id"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer Creation Date"
              :value="formattedDate(propsData.transfer.createdAt)"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer Update Date"
              :value="formattedDate(propsData.transfer.updatedAt)"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer type"
              :value="propsData.transfer.type"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer state"
              :value="propsData.transfer.state"
            />
            <Field
              icon="mdi-file-swap-outline"
              label=" State timestamp"
              :value="formattedDate(propsData.transfer.stateTimestamp)"
            />
          </v-col>
        </template>
        <template v-if="propsData.transferRequest">
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <div class="element-chart-label">Transfer request</div>
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer request ID"
              :value="propsData.transferRequest.id"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer request connector ID"
              :value="propsData.transferRequest.connectorId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Connector Address"
              :value="propsData.transferRequest.connectorAddress"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Contract Id"
              :value="propsData.transferRequest.contractId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Asset ID"
              :value="propsData.transferRequest.assetId"
            />
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
export default {
  name: "ContractInformation",
  components: {
    Field,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
      propsData: this.$props.data.metadata,
      exampleTimestamp: 1666358400,
    };
  },
  methods: {
    formattedDate(timestamp) {
      const date = new Date(timestamp);
      const formattedDate = date.toLocaleDateString("en-GB", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      });
      return formattedDate;
    },
  },
};
</script>
