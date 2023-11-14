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
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label"></div>
          <template v-if="propsData.torqueConverter">
            <div
              v-for="(attr, index) in propsData.torqueConverter"
              :key="index"
            >
              <Field
                :icon="callIconFinder('torqueConverter')"
                label="Torque converter"
                :value="attr"
              />
            </div>
          </template>
          <template v-if="propsData.driveType">
            <Field
              :icon="callIconFinder('driveType')"
              label="Drive type"
              :value="propsData.driveType"
            />
          </template>
          <template v-if="propsData.oilType">
            <Field
              :icon="callIconFinder('oilType')"
              label="Oil type"
              :value="propsData.oilType"
            />
          </template>
          <template v-if="propsData.spreading">
            <Field
              :icon="callIconFinder('spreading')"
              label="Spreading"
              :value="propsData.spreading"
            />
          </template>
          <template v-if="propsData.torque">
            <Field
              :icon="callIconFinder('Torque')"
              label="Torque"
              :value="propsData.torque"
            />
          </template>
          <template v-if="propsData.power">
            <Field
              :icon="callIconFinder('Power')"
              label="Power"
              :value="propsData.power"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label">Standard gear ratio</div>
          <template v-if="propsData.standardGearRatio">
            <Field
              :icon="callIconFinder('Gear')"
              label="Gear"
              :value="propsData.standardGearRatio.gear"
            />
            <Field
              :icon="callIconFinder('Ratio')"
              label="Ratio"
              :value="propsData.standardGearRatio.ratio"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label">Speed resistance</div>
          <template v-if="propsData.speedResistance">
            <Field
              :icon="callIconFinder('Speed')"
              label="Speed"
              :value="propsData.speedResistance.speed"
            />
            <Field
              :icon="callIconFinder('Gear')"
              label="Gear"
              :value="propsData.speedResistance.gear"
            />
          </template>
          <template v-if="propsData.oilCapacity">
            <Field
              :icon="callIconFinder('oilCapacity')"
              label="Oil capacity"
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
                label="Electric performance"
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
