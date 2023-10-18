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
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <template v-if="propsData.mandatory">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">Mandatory</div>
            <Field
              icon="mdi-image-size-select-small"
              label="Safety transportation"
              :value="propsData.mandatory.safetyTransportation.header"
              :link="propsData.mandatory.safetyTransportation.content"
            />
            <Field
              icon="mdi-image-size-select-small"
              label="Substance of concern disassembly"
              :value="propsData.mandatory.substanceOfConcernDisassembly.header"
              :link="propsData.mandatory.substanceOfConcernDisassembly.content"
            />
            <Field
              icon="mdi-image-size-select-small"
              label="Safety transportation"
              :value="propsData.mandatory.safetyTransportation.header"
              :link="propsData.mandatory.safetyTransportation.content"
            />
            <Field
              icon="mdi-image-size-select-small"
              label="Substance of concern instruction"
              :value="propsData.mandatory.substanceOfConcernInstruction.header"
              :link="propsData.mandatory.substanceOfConcernInstruction.content"
            />
          </v-col>
        </template>
        <template v-if="propsData.mandatory.declarationOfConformity">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">Other</div>
            <AttributeField
              icon="mdi-image-size-select-small"
              label="Declaration of conformity"
              :attributes-list="propsData.mandatory.declarationOfConformity"
            />
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import AttributeField from "../AttributeField.vue";

export default {
  name: "SourcesComponent",
  components: {
    Field,
    AttributeField,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.passport.sources,
    };
  },
};
</script>
