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
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <div class="element-chart-label">
            {{ $t("sections.electrochemicalProperties.capacity") }}
          </div>
          <Field
            icon="mdi-image-size-select-small"
            data-cy="remaining-capacity"
            :label="$t('sections.electrochemicalProperties.ratedCapacity')"
            unit="kWh"
            :value="propsData.ratedCapacity"
          />
          <Field
            icon="mdi-arrow-down-circle-outline"
            :label="$t('sections.electrochemicalProperties.capacityFade')"
            unit="%"
            :value="propsData.capacityFade"
          />
          <Field
            icon="mdi-arrow-bottom-right-thin-circle-outline"
            :label="
              $t(
                'sections.electrochemicalProperties.capacityThresholdExhaustion'
              )
            "
            unit="%"
            :value="propsData.capacityThresholdExhaustion"
          />
        </v-col>
        <template v-if="propsData.batteryPower">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.electrochemicalProperties.efficiency") }}
            </div>
            <Field
              icon="mdi-lightning-bolt-outline"
              :label="
                $t('sections.electrochemicalProperties.originalPowerCapability')
              "
              unit="kW"
              :value="propsData.batteryPower.originalPowerCapability"
            />
            <Field
              icon="mdi-arrow-bottom-right-thin-circle-outline"
              :label="
                $t(
                  'sections.electrochemicalProperties.originalPowerCapabilityLimits'
                )
              "
              unit="kW"
              :value="propsData.batteryPower.originalPowerCapabilityLimits"
            />
          </v-col>
        </template>
        <template v-if="propsData.batteryEnergy">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label"></div>
            <Field
              icon="mdi-battery"
              label="Maximum allowed battery energy (MABE)"
              unit="kWh"
              :value="propsData.batteryEnergy.maximumAllowedBatteryEnergy"
            />
            <template v-if="propsData.batteryPower">
              <Field
                icon="mdi-battery"
                :label="
                  $t(
                    'sections.electrochemicalProperties.maximumAllowedBatteryPower'
                  )
                "
                unit="kW"
                :value="propsData.batteryPower.maximumAllowedBatteryPower"
              />
            </template>
            <Field
              icon="mdi-battery"
              :label="$t('MABEvsMABP')"
              unit="%"
              :value="
                propsData.ratioMaximumAllowedBatteryPowerAndMaximumAllowedBatteryEnergy
              "
            />
          </v-col>
        </template>
        <template v-if="propsData.batteryPower">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label"></div>
            <Field
              icon="mdi-battery"
              :label="
                $t(
                  'sections.electrochemicalProperties.powerCapabilityAt20Charge'
                )
              "
              unit="%"
              :value="propsData.batteryPower.powerCapabilityAt20Charge"
            />
            <Field
              icon="mdi-battery"
              :label="
                $t(
                  'sections.electrochemicalProperties.powerCapabilityAt80Charge'
                )
              "
              unit="%"
              :value="propsData.batteryPower.powerCapabilityAt80Charge"
            />
            <Field
              icon="mdi-battery"
              :label="$t('sections.electrochemicalProperties.powerFade')"
              unit="%"
              :value="propsData.batteryPower.powerFade"
            />
          </v-col>
        </template>
        <template v-if="propsData.internalResistance">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.electrochemicalProperties.resistance") }}
            </div>

            <Field
              icon="mdi-omega"
              :label="
                $t('sections.electrochemicalProperties.packInternalResistance')
              "
              unit="Ω"
              :value="propsData.internalResistance.packInternalResistance"
            />
            <Field
              icon="mdi-arrow-up"
              :label="
                $t(
                  'sections.electrochemicalProperties.packInternalResistanceIncrease'
                )
              "
              unit="%"
              :value="
                propsData.internalResistance.packInternalResistanceIncrease
              "
            />
            <Field
              icon="mdi-table-split-cell"
              :label="
                $t('sections.electrochemicalProperties.cellInternalResistance')
              "
              unit="Ω"
              :value="propsData.internalResistance.cellInternalResistance"
            />
          </v-col>
        </template>
        <template v-if="propsData.batteryVoltage">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <div class="element-chart-label">
              {{ $t("sections.electrochemicalProperties.voltage") }}
            </div>
            <Field
              icon="mdi-lightning-bolt-outline"
              :label="$t('sections.electrochemicalProperties.nominalVoltage')"
              unit="V"
              :value="propsData.batteryVoltage.nominalVoltage"
            />
            <Field
              icon="mdi-arrow-down-circle-outline"
              :label="$t('sections.electrochemicalProperties.maxVoltage')"
              unit="V"
              :value="propsData.batteryVoltage.maxVoltage"
            />
            <Field
              icon="mdi-arrow-down-circle-outline"
              :label="$t('sections.electrochemicalProperties.minVoltage')"
              unit="V"
              :value="propsData.batteryVoltage.minVoltage"
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
  name: "ElectrochemicalProperties",
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
      propsData: this.$props.data.aspect.electrochemicalProperties,
    };
  },
};
</script>
