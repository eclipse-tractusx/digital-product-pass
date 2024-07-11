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
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col sm="12" md="4" class="pa-0 ma-0" v-if="callHasContent(propsData.contract)">
          <template v-if="propsData.contract">
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
                <RecursiveComponent :jsonData="propsData.contract['odrl:hasPolicy']" />
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
                <RecursiveComponent :jsonData="propsData.contract['dcat:distribution']" />
              </template>
            </DialogComponent>
            <Field
              icon="mdi-file-swap-outline"
              :label="$t('sections.exchange.edcId')"
              :value="propsData.contract['id']"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0" v-if="callHasContent(propsData.negotiation)">
          <template v-if="propsData.negotiation">
            <div class="element-chart-label">
              {{ $t("sections.exchange.negotiationInit") }}
            </div>

            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.request')"
                :value="propsData.negotiation.init.request['@type']"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.request") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent :jsonData="propsData.negotiation.init.request" />
              </template>
            </DialogComponent>
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.response')"
                :value="propsData.negotiation.init.response['@id']"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.response") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent :jsonData="propsData.negotiation.init.response" />
              </template>
            </DialogComponent>
            <div class="element-chart-label">
              {{ $t("sections.exchange.negotiationGet") }}
            </div>
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.response')"
                :value="propsData.negotiation.get.response['@id']"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.response") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent :jsonData="propsData.negotiation.get.response" />
              </template>
            </DialogComponent>
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.transfer">
            <div class="element-chart-label">
              {{ $t("sections.exchange.transferInit") }}
            </div>
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.request')"
                :value="propsData.transfer.init.request.assetId"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.request") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent :jsonData="propsData.transfer.init.request" />
              </template>
            </DialogComponent>
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.response')"
                :value="propsData.transfer.init.response['@id']"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.response") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent :jsonData="propsData.transfer.init.response" />
              </template>
            </DialogComponent>
            <div class="element-chart-label">
              {{ $t("sections.exchange.transferGet") }}
            </div>
            <DialogComponent class="field-dialog">
              <Field
                info
                icon="mdi-file-swap-outline"
                :label="$t('sections.exchange.response')"
                :value="propsData.transfer.get.response['@id']"
              />
              <template v-slot:title>
                <h3>{{ $t("sections.exchange.response") }}</h3>
              </template>
              <template v-slot:text>
                <RecursiveComponent :jsonData="propsData.transfer.get.response" />
              </template>
            </DialogComponent>
          </template>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import DialogComponent from "../../general/Dialog.vue";
import RecursiveComponent from "../../general/RecursiveComponent.vue";
import passportUtil from "@/utils/passportUtil.js";

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
    callHasContent(data) {
      return passportUtil.hasContent(data);
    },
  },
};
</script>
