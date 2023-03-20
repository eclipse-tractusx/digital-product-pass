<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<template>
  <div class="section">
    <div class="sub-section-container">
      <Field
        data-cy="remaining-capacity"
        label="Rated capacity"
        unit="W"
        :value="propsData.ratedCapacity"
      />
      <Field
        label="Energy roundtrip efficiency change"
        unit="%"
        :value="propsData.batteryEnergy.energyRoundtripEfficiencyChange"
      />
      <Field
        label="Maximum allowed battery energy"
        unit="%"
        :value="propsData.batteryEnergy.maximumAllowedBatteryEnergy"
      />
      <Field
        label="Energy roundtrip efficiency"
        unit="%"
        :value="propsData.batteryEnergy.energyRoundtripEfficiency"
      />

      <Field
        label="Ratio maximum allowed battery power and maximum allowed battery energy"
        unit=""
        :value="
          propsData.ratioMaximumAllowedBatteryPowerAndMaximumAllowedBatteryEnergy
        "
      />
      <Field
        label="Nominal voltage"
        unit="%"
        :value="propsData.batteryVoltage.nominalVoltage"
      />
      <Field
        label="Max voltage"
        unit="%"
        :value="propsData.batteryVoltage.maxVoltage"
      />
      <Field
        label="Min voltage"
        unit="%"
        :value="propsData.batteryVoltage.minVoltage"
      />
      <Field
        label="Cell internal resistance"
        unit="%"
        :value="propsData.internalResistance.cellInternalResistance"
      />
      <Field
        label="Pack internal resistance increase"
        unit="%"
        :value="propsData.internalResistance.packInternalResistanceIncrease"
      />
      <Field
        label="Pack internal resistance"
        unit="%"
        :value="propsData.internalResistance.packInternalResistance"
      />

      <Field
        label="Capacity threshold exhaustion"
        unit="%"
        :value="propsData.capacityThresholdExhaustion"
      />
      <Field
        label="Original power capability"
        unit="%"
        :value="propsData.batteryPower.originalPowerCapability"
      />
      <Field
        label="Power fade"
        unit="%"
        :value="propsData.batteryPower.powerFade"
      />
      <Field
        label="Original power capability limits"
        unit="%"
        :value="propsData.batteryPower.originalPowerCapabilityLimits"
      />
      <Field
        label="Maximum allowed battery power"
        unit="%"
        :value="propsData.batteryPower.maximumAllowedBatteryPower"
      />
      <Field
        label="Power capability at 20 charge"
        unit="%"
        :value="propsData.batteryPower.powerCapabilityAt20Charge"
      />
      <Field
        label="Original power"
        unit="%"
        :value="propsData.batteryPower.originalPower"
      />
      <Field
        label="Power capability at 80 charge"
        unit="%"
        :value="propsData.batteryPower.powerCapabilityAt80Charge"
      />
      <Field label="Capacity fade" unit="%" :value="propsData.capacityFade" />
    </div>
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
