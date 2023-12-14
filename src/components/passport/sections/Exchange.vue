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
        <template v-if="propsData.contract">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">Contract</div>
            <Field
              icon="mdi-file-swap-outline"
              label="ID"
              :value="propsData.contract['@id']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Type"
              :value="propsData.contract['@type']"
            />
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                label="Policy ID"
                :value="propsData.contract['odrl:hasPolicy']['@id']"
              />
              <template v-slot:title>
                <h3>Policy</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.contract['odrl:hasPolicy']"
                />
              </template>
            </DialogComponent>
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                label="Distribution"
                value="Distribution list"
              />
              <template v-slot:title>
                <h3>Distribution</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.contract['dcat:distribution']"
                />
              </template>
            </DialogComponent>
            <Field
              icon="mdi-file-swap-outline"
              label="EDC description"
              :value="propsData.contract['edc:description']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC ID"
              :value="propsData.contract['edc:id']"
            />
          </v-col>
        </template>

        <template v-if="propsData.negotiation">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">Negotiation</div>
            <Field
              icon="mdi-file-swap-outline"
              label="Type"
              :value="propsData.negotiation.get.response['@type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="ID"
              :value="propsData.negotiation.get.response['@id']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC Type"
              :value="propsData.negotiation.get.response['edc:type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC protocol"
              :value="propsData.negotiation.get.response['edc:protocol']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC state"
              :value="propsData.negotiation.get.response['edc:state']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC counter party address"
              :value="
                propsData.negotiation.get.response['edc:counterPartyAddress']
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC contract agreement ID"
              :value="
                propsData.negotiation.get.response['edc:contractAgreementId']
              "
            />
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                label="Context"
                :value="propsData.negotiation.get.response['@context'].dct"
              />
              <template v-slot:title>
                <h3>Context</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.negotiation.get.response['@context']"
                />
              </template>
            </DialogComponent>
          </v-col>
        </template>
        <template v-if="propsData.negotiationRequest">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label">Negotiation request</div>
            <Field
              icon="mdi-file-swap-outline"
              label="Type"
              :value="propsData.negotiation.init.request['@type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Connector address"
              :value="propsData.negotiation.init.request.connectorAddress"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Protocol"
              :value="propsData.negotiation.init.request.protocol"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Connector ID"
              :value="propsData.negotiation.init.request.connectorId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Provider ID"
              :value="propsData.negotiation.init.request.providerId"
            />
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                label="Offer"
                value="Offer detail"
              />
              <template v-slot:title>
                <h3>Offer</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.negotiation.request.init.offer"
                />
              </template>
            </DialogComponent>
          </v-col>
        </template>
        <template v-if="propsData.transfer">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">Transfer</div>

            <Field
              icon="mdi-file-swap-outline"
              label="Transfer ID"
              :value="propsData.transfer.get.response['@id']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer type"
              :value="propsData.transfer.get.response['@type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer state"
              :value="propsData.transfer.get.response['edc:state']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC timestamp"
              :value="
                formattedDate(
                  propsData.transfer.get.response['edc:stateTimestamp']
                )
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              label="EDC type"
              :value="propsData.transfer.get.response['edc:type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Transfer type"
              :value="propsData.transfer.get.response['@type']"
            />
            <DialogComponent>
              <Field
                info
                icon="mdi-file-swap-outline"
                label="EDC data request ID"
                :value="
                  propsData.transfer.get.response['edc:dataRequest']['@id']
                "
              />
              <template v-slot:title>
                <h3>EDC data request</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.transfer.get.response['edc:dataRequest']"
                />
              </template>
            </DialogComponent>
            <Field
              icon="mdi-file-swap-outline"
              label="EDC receiver http endpoint"
              :value="
                propsData.transfer.get.response['edc:receiverHttpEndpoint']
              "
            />
          </v-col>
        </template>
        <template v-if="propsData.transferRequest">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label">Transfer request</div>
            <Field
              icon="mdi-file-swap-outline"
              label="Asset ID"
              :value="propsData.transfer.init.request.assetId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Connector address"
              :value="propsData.transfer.init.request.connectorAddress"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Contract Id"
              :value="propsData.transfer.init.request.contractId"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Data destination"
              :value="
                propsData.transfer.init.request.dataDestination.properties.type
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Reciver http endpoint"
              :value="
                propsData.transfer.init.request.privateProperties
                  .receiverHttpEndpoint
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Protocol"
              :value="propsData.transfer.init.request.protocol"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Content type"
              :value="propsData.transfer.init.request.transferType.contentType"
            />
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import DialogComponent from "../../general/Dialog.vue";
import RecursiveComponent from "../../general/RecursiveComponent.vue";

export default {
  name: "ContractExchange",
  components: {
    Field,
    DialogComponent,
    RecursiveComponent,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
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
    arrayName(array) {
      return Object.keys(array)[0];
    },
    toSentenceCase(text) {
      // Convert the string to sentence case
      const words = text.split(/(?=[A-Z])/);
      const firstWord = [words[0].charAt(0).toUpperCase() + words[0].slice(1)];
      const otherWords = words.slice(1).map((word) => word.toLowerCase());
      const formattedWords = firstWord.concat(otherWords);
      return formattedWords.join(" ");
    },
  },
};
</script>
