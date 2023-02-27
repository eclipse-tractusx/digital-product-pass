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

<template  >
  <div class="section">
    <SectionHeader
      title="3. Electrochemical properties"
      @click="toggle = !toggle"
    />
    <div class="section-content" :class="[toggle ? 'hidden' : '']">
      <div class="sub-section-container">
        <Field
          data-cy="remaining-capacity"
          label="Rated capacity"
          unit="W"
          :value="electrochemicalProperties.ratedCapacity"
        />
        <Field
          label="Energy roundtrip efficiency change"
          unit="%"
          :value="
            electrochemicalProperties.batteryEnergy
              .energyRoundtripEfficiencyChange
          "
        />
        <Field
          label="Maximum allowed battery energy"
          unit="%"
          :value="
            electrochemicalProperties.batteryEnergy.maximumAllowedBatteryEnergy
          "
        />
        <Field
          label="Energy roundtrip efficiency"
          unit="%"
          :value="
            electrochemicalProperties.batteryEnergy.energyRoundtripEfficiency
          "
        />

        <Field
          label="Ratio maximum allowed battery power and maximum allowed battery energy"
          unit=""
          :value="
            electrochemicalProperties.ratioMaximumAllowedBatteryPowerAndMaximumAllowedBatteryEnergy
          "
        />
        <Field
          label="Nominal voltage"
          unit="%"
          :value="electrochemicalProperties.batteryVoltage.nominalVoltage"
        />
        <Field
          label="Max voltage"
          unit="%"
          :value="electrochemicalProperties.batteryVoltage.maxVoltage"
        />
        <Field
          label="Min voltage"
          unit="%"
          :value="electrochemicalProperties.batteryVoltage.minVoltage"
        />
        <Field
          label="Cell internal resistance"
          unit="%"
          :value="
            electrochemicalProperties.internalResistance.cellInternalResistance
          "
        />
        <Field
          label="Pack internal resistance increase"
          unit="%"
          :value="
            electrochemicalProperties.internalResistance
              .packInternalResistanceIncrease
          "
        />
        <Field
          label="Pack internal resistance"
          unit="%"
          :value="
            electrochemicalProperties.internalResistance.packInternalResistance
          "
        />

        <Field
          label="Capacity threshold exhaustion"
          unit="%"
          :value="electrochemicalProperties.capacityThresholdExhaustion"
        />
        <Field
          label="Original power capability"
          unit="%"
          :value="
            electrochemicalProperties.batteryPower.originalPowerCapability
          "
        />
        <Field
          label="Power fade"
          unit="%"
          :value="electrochemicalProperties.batteryPower.powerFade"
        />
        <Field
          label="Original power capability limits"
          unit="%"
          :value="
            electrochemicalProperties.batteryPower.originalPowerCapabilityLimits
          "
        />
        <Field
          label="Maximum allowed battery power"
          unit="%"
          :value="
            electrochemicalProperties.batteryPower.maximumAllowedBatteryPower
          "
        />
        <Field
          label="Power capability at 20 charge"
          unit="%"
          :value="
            electrochemicalProperties.batteryPower.powerCapabilityAt20Charge
          "
        />
        <Field
          label="Original power"
          unit="%"
          :value="electrochemicalProperties.batteryPower.originalPower"
        />
        <Field
          label="Power capability at 80 charge"
          unit="%"
          :value="
            electrochemicalProperties.batteryPower.powerCapabilityAt80Charge
          "
        />
        <Field
          label="Capacity fade"
          unit="%"
          :value="electrochemicalProperties.capacityFade"
        />
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "../../general/SectionHeader.vue";
import Field from "../Field.vue";

export default {
  name: "ElectrochemicalProperties",
  components: {
    Field,
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    electrochemicalProperties: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
    };
  },
};
</script>
