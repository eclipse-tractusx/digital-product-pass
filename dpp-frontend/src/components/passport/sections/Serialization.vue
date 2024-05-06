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
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <template v-if="propsData.manufacturingInformation">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.serialization.manufacturingInformation") }}
            </div>
            <Field
              icon="mdi-image-size-select-small"
              :label="$t('sections.serialization.date')"
              :value="propsData.manufacturingInformation.date"
            />
            <Field
              icon="mdi-arrow-down-circle-outline"
              :label="$t('sections.serialization.country')"
              :value="propsData.manufacturingInformation.country"
            />
          </v-col>
        </template>
        <template v-if="propsData.registration">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.serialization.registration") }}
            </div>
            <Field
              icon="mdi-lightning-bolt-outline"
              :label="$t('sections.serialization.customerPartId')"
              :value="propsData.registration.customerPartId"
            />
            <Field
              icon="mdi-arrow-bottom-right-thin-circle-outline"
              :label="$t('sections.serialization.manufacturerPartId')"
              :value="propsData.registration.manufacturerPartId"
            />
          </v-col>
        </template>
        <template v-if="propsData.quantity">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label"></div>
            <Field
              icon="mdi-lightning-bolt-outline"
              :label="$t('sections.serialization.quantity')"
              :value="propsData.quantity.value"
              :unit="propsData.quantity.unit"
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
  name: "SerializationComponent",
  components: {
    Field,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.aspect.serialization,
    };
  },
};
</script>
