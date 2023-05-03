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
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label">Capacity</div>
          <Field
            icon="mdi-image-size-select-small"
            data-cy="remaining-capacity"
            label="Rated capacity"
            unit="W"
            :value="propsData.ratedCapacity"
          />
          <Field
            icon="mdi-arrow-down-circle-outline"
            label="Capacity fade"
            unit="%"
            :value="propsData.capacityFade"
          />
          <Field
            icon="mdi-arrow-bottom-right-thin-circle-outline"
            label="Capacity threshold exhaustion"
            unit="%"
            :value="propsData.capacityThresholdExhaustion"
          />
        </v-col>
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label">Efficiency</div>
          <Field
            icon="mdi-lightning-bolt-outline"
            label="Original power capability"
            unit="%"
            :value="propsData.batteryPower.originalPowerCapability"
          />
          <Field
            icon="mdi-arrow-bottom-right-thin-circle-outline"
            label="Original power capability limits"
            unit="%"
            :value="propsData.batteryPower.originalPowerCapabilityLimits"
          />
        </v-col>
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label"></div>
          <Field
            icon="mdi-battery"
            label="Maximum allowed battery energy (MABE)"
            unit="%"
            :value="propsData.batteryEnergy.maximumAllowedBatteryEnergy"
          />
          <Field
            icon="mdi-battery"
            label="Maximum allowed battery power (MABP)"
            unit="%"
            :value="propsData.batteryPower.maximumAllowedBatteryPower"
          />
          <Field
            icon="mdi-battery"
            label="Ratio MABE vs. MABP"
            unit=""
            :value="
              propsData.ratioMaximumAllowedBatteryPowerAndMaximumAllowedBatteryEnergy
            "
          />
        </v-col>
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label"></div>
          <Field
            icon="mdi-battery"
            label="Power capability at 20 charge"
            unit="%"
            :value="propsData.batteryPower.powerCapabilityAt20Charge"
          />
          <Field
            icon="mdi-battery"
            label="Power capability at 80 charge"
            unit="%"
            :value="propsData.batteryPower.powerCapabilityAt80Charge"
          />
          <Field
            icon="mdi-battery"
            label="Power fade"
            unit="%"
            :value="propsData.batteryPower.powerFade"
          />
        </v-col>
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label">Resistance</div>
          <Field
            icon="mdi-table-split-cell"
            label="Cell internal resistance"
            unit="%"
            :value="propsData.internalResistance.cellInternalResistance"
          />

          <Field
            icon="mdi-omega"
            label="Pack internal resistance"
            unit="%"
            :value="propsData.internalResistance.packInternalResistance"
          />
          <Field
            icon="mdi-arrow-up"
            label="Pack internal resistance increase"
            unit="%"
            :value="propsData.internalResistance.packInternalResistanceIncrease"
          />
        </v-col>
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label">Voltage</div>
          <Field
            icon="mdi-lightning-bolt-outline"
            label="Nominal voltage"
            unit="%"
            :value="propsData.batteryVoltage.nominalVoltage"
          />
          <Field
            icon="mdi-arrow-down-circle-outline"
            label="Max voltage"
            unit="%"
            :value="propsData.batteryVoltage.maxVoltage"
          />
          <Field
            icon="mdi-arrow-down-circle-outline"
            label="Min voltage"
            unit="%"
            :value="propsData.batteryVoltage.minVoltage"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";

export default {
  name: "ElectrochemicalProperties",
  components: {
    Field,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.passport.electrochemicalProperties,
    };
  },
};
</script>
