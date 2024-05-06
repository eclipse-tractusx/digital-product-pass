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
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label"></div>
          <template v-if="propsData.torqueConverter">
            <div
              v-for="(attr, index) in propsData.torqueConverter"
              :key="index"
            >
              <Field
                :icon="callIconFinder('torqueConverter')"
                :label="$t('sections.productSpecificParameters.torqueConverter')"
                :value="attr"
              />
            </div>
          </template>
          <template v-if="propsData.driveType">
            <Field
              :icon="callIconFinder('driveType')"
              :label="$t('sections.productSpecificParameters.driveType')"
              :value="propsData.driveType"
            />
          </template>
          <template v-if="propsData.oilType">
            <Field
              :icon="callIconFinder('oilType')"
              :label="$t('sections.productSpecificParameters.oilType')"
              :value="propsData.oilType"
            />
          </template>
          <template v-if="propsData.spreading">
            <Field
              :icon="callIconFinder('spreading')"
              :label="$t('sections.productSpecificParameters.spreading')"
              :value="propsData.spreading"
            />
          </template>
          <template v-if="propsData.torque">
            <Field
              :icon="callIconFinder('Torque')"
              :label="$t('sections.productSpecificParameters.torque')"
              :value="propsData.torque"
            />
          </template>
          <template v-if="propsData.power">
            <Field
              :icon="callIconFinder('Power')"
              :label="$t('sections.productSpecificParameters.power')"
              :value="propsData.power"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label">{{ $t('sections.productSpecificParameters.standardGearRatio') }}</div>
          <template v-if="propsData.standardGearRatio">
            <Field
              :icon="callIconFinder('Gear')"
              :label="$t('sections.productSpecificParameters.gear')"
              :value="propsData.standardGearRatio.gear"
            />
            <Field
              :icon="callIconFinder('Ratio')"
              :label="$t('sections.productSpecificParameters.ratio')"
              :value="propsData.standardGearRatio.ratio"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label">{{ $t('sections.productSpecificParameters.speedResistance') }}</div>
          <template v-if="propsData.speedResistance">
            <Field
              :icon="callIconFinder('Speed')"
              :label="$t('sections.productSpecificParameters.speed')"
              :value="propsData.speedResistance.speed"
            />
            <Field
              :icon="callIconFinder('Gear')"
              :label="$t('sections.productSpecificParameters.gear')"
              :value="propsData.speedResistance.gear"
            />
          </template>
          <template v-if="propsData.oilCapacity">
            <Field
              :icon="callIconFinder('oilCapacity')"
              :label="$t('sections.productSpecificParameters.oilCapacity')"
              :value="propsData.oilCapacity"
            />
          </template>
          <template v-if="propsData.oilCapacity">
            <div
              v-for="(attr, index) in propsData.electricPerformance"
              :key="index"
            >
              <Field
                :icon="callIconFinder('Electric performance')"
                :label="$t('sections.productSpecificParameters.electricPerformance')"
                :value="attr"
              />
            </div>
          </template>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "ProductSpecificParameters",
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
      propsData: this.$props.data.aspect.productSpecificParameters,
    };
  },
  methods: {
    callIconFinder(property) {
      return passportUtil.iconFinder(property);
    },
  },
};
</script>
