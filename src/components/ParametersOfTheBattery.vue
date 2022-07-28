<template>
  <SectionHeader title="4. Parameters of the battery"/>

  <div class="section-content">
    <div class="sub-section-container">
      <Field
          label="State of charge"
          v-bind:unit="parametersOfTheBattery.stateOfCharge.unit"
          v-bind:value="parametersOfTheBattery.stateOfCharge.value"
      />
      <Field
          label="Maximum Allowed Battery Power"
          v-bind:unit="parametersOfTheBattery.maximumAllowedBatteryPower.unit"
          v-bind:value="parametersOfTheBattery.maximumAllowedBatteryPower.value"
      />
      <Field
          label="Minimal voltage with temperature ranges when relevant"
          v-bind:tempMax="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
          v-bind:tempMin="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
          v-bind:tempUnit="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
          v-bind:test="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
          v-bind:unit="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
          v-bind:value="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .value
        "
      />
      <Field
          label="Nominal voltage with temperature ranges when relevant"
          v-bind:tempMax="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
          v-bind:tempMin="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
          v-bind:tempUnit="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
          v-bind:test="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
          v-bind:unit="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
          v-bind:value="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .value
        "
      />
      <Field
          label="Maximum voltage with temperature ranges when relevant"
          v-bind:tempMax="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
          v-bind:tempMin="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
          v-bind:tempUnit="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
          v-bind:test="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
          v-bind:unit="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
          v-bind:value="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .value
        "
      />
      <Field
          label="Original power capability with temperature ranges when relevant"
          v-bind:tempMax="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .max.value
        "
          v-bind:tempMin="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .min.value
        "
          v-bind:tempUnit="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .min.unit
        "
          v-bind:test="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.refTest
        "
          v-bind:unit="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.unit
        "
          v-bind:value="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.value
        "
      />

      <Field
          label="Capacity threshold for exhaustion"
          v-bind:unit="parametersOfTheBattery.capacityThresholdForExhaustion.unit"
          v-bind:value="
          parametersOfTheBattery.capacityThresholdForExhaustion.value
        "
      />

      <Field
          label="Temperature range the battery can withstand when not in use"
          v-bind:tempRangeMax="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.max
            .value
        "
          v-bind:tempRangeMin="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
            .value
        "
          v-bind:tempUnit="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
            .unit
        "
          v-bind:test="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.refTest
        "
      />
      <Field
          label="C-rate of Relevant Cycle-life Test"
          v-bind:value="parametersOfTheBattery.cRateOfRelevantCycleLifeTest.value"
      />
      <Field
          label="Energy Roundtrip Efficiency"
          v-bind:unit="parametersOfTheBattery.energyRoundtripEfficiency.unit"
          v-bind:value="parametersOfTheBattery.energyRoundtripEfficiency.value"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
          label="Rated Capacity"
          v-bind:unit="parametersOfTheBattery.ratedCapacity.unit"
          v-bind:value="parametersOfTheBattery.ratedCapacity.value"
      />
      <Field
          label="Applied discharge rate"
          v-bind:unit="parametersOfTheBattery.appliedDischargeRate.unit"
          v-bind:value="parametersOfTheBattery.appliedDischargeRate.value"
      />
      <Field
          label="Applied charge rate"
          v-bind:unit="parametersOfTheBattery.appliedChargeRate.unit"
          v-bind:value="parametersOfTheBattery.appliedChargeRate.value"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
          label="Capacity fade"
          v-bind:unit="parametersOfTheBattery.originalPower.unit"
          v-bind:value="parametersOfTheBattery.originalPower.value"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
          label="Original power"
          v-bind:unit="parametersOfTheBattery.capacityFade.unit"
          v-bind:value="parametersOfTheBattery.capacityFade.value"
      />
      <Field
          label="Power capability at 80% state of charger"
          v-bind:unit="
          parametersOfTheBattery.powerCapabilityAt80StateOfCharge.unit
        "
          v-bind:value="
          parametersOfTheBattery.powerCapabilityAt80StateOfCharge.value
        "
      />
      <Field
          label="Power capability at 20% state of charge"
          v-bind:unit="
          parametersOfTheBattery.powerCapabilityAt20StateOfCharge.unit
        "
          v-bind:value="
          parametersOfTheBattery.powerCapabilityAt20StateOfCharge.value
        "
      />
      <Field
          label="Power capability at 20% state of charge"
          v-bind:tempRangeMax="parametersOfTheBattery.temperatureRanges.max.value"
          v-bind:tempRangeMin="parametersOfTheBattery.temperatureRanges.min.value"
          v-bind:tempUnit="parametersOfTheBattery.temperatureRanges.min.unit"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
          label="Power fade"
          v-bind:unit="parametersOfTheBattery.powerFade.unit"
          v-bind:value="parametersOfTheBattery.powerFade.value"
      />
      <Field
          label="Maximum allowed battery energy"
          v-bind:unit="parametersOfTheBattery.maximumAllowedBatteryEnergy.unit"
          v-bind:value="parametersOfTheBattery.maximumAllowedBatteryEnergy.value"
      />
      <Field
          label="Ratio between maximum allowed battery power (W) and battery energy
          (Wh)"
          v-bind:unit="
          parametersOfTheBattery
            .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.unit
        "
          v-bind:value="
          parametersOfTheBattery
            .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.value
        "
      />
      <Field
          label="Internal Resistance: Cell"
          v-bind:unit="parametersOfTheBattery.internalResistanceCell.unit"
          v-bind:value="parametersOfTheBattery.internalResistanceCell.value"
      />
      <Field
          label="Internal Resistance: Pack"
          v-bind:unit="parametersOfTheBattery.internalResistancePack.unit"
          v-bind:value="parametersOfTheBattery.internalResistancePack.value"
      />

      <Field
          label="Internal Resistance Increase: Pack"
          v-bind:unit="parametersOfTheBattery.internalResistanceIncreasePack.unit"
          v-bind:value="
          parametersOfTheBattery.internalResistanceIncreasePack.value
        "
      />
      <Field
          label="Expected Lifetime"
          v-bind:value="parametersOfTheBattery.expectedLifetime.value"
      />
      <Field
          label="Depth of discharge in the cycle-life test"
          v-bind:unit="
          parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.unit
        "
          v-bind:value="
          parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.value
        "
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";
import Field from "./Field.vue";

export default {
  name: "ParametersOfTheBattery",
  props: {
    sectionTitle: String,
    parametersOfTheBattery: {},
  },
  components: {
    Field,
    SectionHeader,
    SectionContent,
  },
};
</script>

<style scoped>
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}
</style>
