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

<template v-if="propsData">
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col
          sm="12"
          md="12"
          class="pa-0 ma-0"
          style="display: flex; margin-bottom: 12px"
        >
          <AttributeField
            data-cy="composition"
            icon="mdi-newspaper-variant-outline"
            :attributes-list="propsData.compositionOfBattery"
            :label="$t('sections.batteryComposition.batteryComposites')"
            style="background: #f9f9f9; min-width: 300px"
          />
          <Field
            icon="mdi-select-all"
            :label="$t('sections.batteryComposition.criticalRawMaterials')"
            :value="propsData.criticalRawMaterials"
            style="min-width: 300px"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import AttributeField from "../AttributeField.vue";
import Field from "../Field.vue";

export default {
  name: "BatteryComposition",
  components: {
    AttributeField,
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
      propsData: this.$props.data.aspect.composition,
    };
  },
};
</script>
