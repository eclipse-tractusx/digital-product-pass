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
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <div class="element-chart-label">{{ $t('sections.instructions.packagingInstructions') }}</div>
          <template v-if="propsData.packagingInstructions">
            <InstructionsField :field="propsData.packagingInstructions" />
          </template>
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <div class="element-chart-label">{{ $t('sections.instructions.transportationInstructions') }}</div>
          <template v-if="propsData.transportationInstructions">
            <InstructionsField :field="propsData.transportationInstructions" />
          </template>
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <div class="element-chart-label">{{ $t('sections.instructions.dismantlingProcedure') }}</div>
          <template v-if="propsData.dismantlingProcedure">
            <InstructionsField :field="propsData.dismantlingProcedure" />
          </template>
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <div class="element-chart-label">{{ $t('sections.instructions.safetyMeasures') }}</div>
          <template v-if="propsData.safetyMeasures">
            <InstructionsField :field="propsData.safetyMeasures" />
          </template>
          <div class="element-chart-label">{{ $t('sections.instructions.vehicleDismantlingProcedure') }}</div>
          <template v-if="propsData.vehicleDismantlingProcedure">
            <InstructionsField :field="propsData.vehicleDismantlingProcedure" />
          </template>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import InstructionsField from "../InstructionsField.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "InstructionsComponent",
  components: {
    InstructionsField,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.aspect.instructions,
    };
  },
  methods: {
    callIconFinder(property) {
      return passportUtil.iconFinder(property);
    },
  },
};
</script>
