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
        <template v-if="propsData.localIdentifiers">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <AttributeField
              icon="mdi-newspaper-variant-outline"
              :attributes-list="propsData.localIdentifiers"
              label="Part instance Id"
            />
            <template v-if="propsData.gtin">
              <Field
                icon="mdi-arrow-down-circle-outline"
                label="Gtin"
                :value="propsData.gtin"
              />
            </template>
          </v-col>
        </template>
        <template v-if="propsData.additionalCode">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <AttributeField
              icon="mdi-newspaper-variant-outline"
              :attributes-list="propsData.additionalCode"
              label="TARIC"
            />
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import AttributeField from "../AttributeField.vue";
import Field from "../Field.vue";

export default {
  name: "IdentificationComponent",
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
      propsData: this.$props.data.aspect.identification,
    };
  },
};
</script>
