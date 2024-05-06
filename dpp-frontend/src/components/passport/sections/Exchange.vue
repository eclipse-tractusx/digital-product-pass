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

<template >
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <template v-if="propsData.contract">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.exchange.contract") }}
            </div>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.id')"
              :value="propsData.contract['@id']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.type')"
              :value="propsData.contract['@type']"
            />
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.policyId')"
                :value="propsData.contract['odrl:hasPolicy']['@id']"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.policy") }}</h3>
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
                :label="$t('sections.exchange.distribution')"
                :value="$t('sections.exchange.distributionList')"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.distribution") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.contract['dcat:distribution']"
                />
              </template>
            </DialogComponent>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcDescription')"
              :value="propsData.contract['edc:description']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcId')"
              :value="propsData.contract['edc:id']"
            />
          </v-col>
        </template>
        <template v-if="propsData.negotiation">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.exchange.negotiation") }}
            </div>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.type')"
              :value="propsData.negotiation.get.response['@type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.id')"
              :value="propsData.negotiation.get.response['@id']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcType')"
              :value="propsData.negotiation.get.response['edc:type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcProtocol')"
              :value="propsData.negotiation.get.response['edc:protocol']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcState')"
              :value="propsData.negotiation.get.response['edc:state']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcCounterPartyAddress')"
              :value="
                propsData.negotiation.get.response['edc:counterPartyAddress']
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcContractAgreementId')"
              :value="
                propsData.negotiation.get.response['edc:contractAgreementId']
              "
            />
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.context')"
                :value="propsData.negotiation.get.response['@context'].dct"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.context") }}</h3>
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
            <div class="element-chart-label">
              {{ $t("sections.exchange.negotiationRequest") }}
            </div>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.type')"
              :value="propsData.negotiation.init.request['@type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              label="Connector address"
              :value="propsData.negotiation.init.request.connectorAddress"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.protocol')"
              :value="propsData.negotiation.init.request.protocol"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.connectorId')"
              :value="propsData.negotiation.init.request.connectorId"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.providerId')"
              :value="propsData.negotiation.init.request.providerId"
            />
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.offer')"
                :value="$t('sections.exchange.offerDetail')"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.offer") }}</h3>
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
            <div class="element-chart-label">
              {{ $t("sections.exchange.transfer") }}
            </div>

            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.transferId')"
              :value="propsData.transfer.get.response['@id']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.transferType')"
              :value="propsData.transfer.get.response['@type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.transferState')"
              :value="propsData.transfer.get.response['edc:state']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcTimestamp')"
              :value="
                formattedDate(
                  propsData.transfer.get.response['edc:stateTimestamp']
                )
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcType')"
              :value="propsData.transfer.get.response['edc:type']"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.transferType')"
              :value="propsData.transfer.get.response['@type']"
            />
            <DialogComponent>
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.edcDataRequestId')"
                :value="
                  propsData.transfer.get.response['edc:dataRequest']['@id']
                "
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.edcDataRequest") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent
                  :jsonData="propsData.transfer.get.response['edc:dataRequest']"
                />
              </template>
            </DialogComponent>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcReceiverHttpEndpoint')"
              :value="
                propsData.transfer.get.response['edc:receiverHttpEndpoint']
              "
            />
          </v-col>
        </template>
        <template v-if="propsData.transferRequest">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.exchange.transferRequest") }}
            </div>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.assetId')"
              :value="propsData.transfer.init.request.assetId"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.connectorAddress')"
              :value="propsData.transfer.init.request.connectorAddress"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.contractId')"
              :value="propsData.transfer.init.request.contractId"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.dataDestination')"
              :value="
                propsData.transfer.init.request.dataDestination.properties.type
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.reciverHttpEndpoint')"
              :value="
                propsData.transfer.init.request.privateProperties
                  .receiverHttpEndpoint
              "
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.protocol')"
              :value="propsData.transfer.init.request.protocol"
            />
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.contentType')"
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
