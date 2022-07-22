<template>
  <SectionHeader title="4. Parameters of the battery" />

  <div class="section-content">
    <div class="sub-section-container">
      <Field
        label="State of charge"
        v-bind:value="parametersOfTheBattery.stateOfCharge.value"
        v-bind:unit="parametersOfTheBattery.stateOfCharge.unit"
      />
      <Field
        label="Maximum Allowed Battery Power"
        v-bind:value="parametersOfTheBattery.maximumAllowedBatteryPower.value"
        v-bind:unit="parametersOfTheBattery.maximumAllowedBatteryPower.unit"
      />
      <Field
        label="Minimal voltage with temperature ranges when relevant"
        v-bind:value="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .value
        "
        v-bind:unit="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
        v-bind:tempMin="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
        v-bind:tempMax="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
        v-bind:tempUnit="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
        v-bind:test="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
      />
      <Field
        label="Nominal voltage with temperature ranges when relevant"
        v-bind:value="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .value
        "
        v-bind:unit="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
        v-bind:tempMin="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
        v-bind:tempMax="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
        v-bind:tempUnit="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
        v-bind:test="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
      />
      <Field
        label="Maximum voltage with temperature ranges when relevant"
        v-bind:value="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .value
        "
        v-bind:unit="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
        v-bind:tempMin="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
        v-bind:tempMax="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
        v-bind:tempUnit="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
        v-bind:test="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
      />
      <Field
        label="Original power capability with temperature ranges when relevant"
        v-bind:value="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.value
        "
        v-bind:unit="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.unit
        "
        v-bind:tempMin="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .min.value
        "
        v-bind:tempMax="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .max.value
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
      />

      <Field
        label="Capacity threshold for exhaustion"
        v-bind:value="
          parametersOfTheBattery.capacityThresholdForExhaustion.value
        "
        v-bind:unit="parametersOfTheBattery.capacityThresholdForExhaustion.unit"
      />

      <Field
        label="Temperature range the battery can withstand when not in use"
        v-bind:tempRangeMin="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
            .value
        "
        v-bind:tempRangeMax="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.max
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
        v-bind:value="parametersOfTheBattery.energyRoundtripEfficiency.value"
        v-bind:unit="parametersOfTheBattery.energyRoundtripEfficiency.unit"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Rated Capacity"
        v-bind:value="parametersOfTheBattery.ratedCapacity.value"
        v-bind:unit="parametersOfTheBattery.ratedCapacity.unit"
      />
      <Field
        label="Applied discharge rate"
        v-bind:value="parametersOfTheBattery.appliedDischargeRate.value"
        v-bind:unit="parametersOfTheBattery.appliedDischargeRate.unit"
      />
      <Field
        label="Applied charge rate"
        v-bind:value="parametersOfTheBattery.appliedChargeRate.value"
        v-bind:unit="parametersOfTheBattery.appliedChargeRate.unit"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Capacity fade"
        v-bind:value="parametersOfTheBattery.originalPower.value"
        v-bind:unit="parametersOfTheBattery.originalPower.unit"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Original power"
        v-bind:value="parametersOfTheBattery.capacityFade.value"
        v-bind:unit="parametersOfTheBattery.capacityFade.unit"
      />
      <Field
        label="Power capability at 80% state of charger"
        v-bind:value="
          parametersOfTheBattery.powerCapabilityAt80StateOfCharge.value
        "
        v-bind:unit="
          parametersOfTheBattery.powerCapabilityAt80StateOfCharge.unit
        "
      />
      <Field
        label="Power capability at 20% state of charge"
        v-bind:value="
          parametersOfTheBattery.powerCapabilityAt20StateOfCharge.value
        "
        v-bind:unit="
          parametersOfTheBattery.powerCapabilityAt20StateOfCharge.unit
        "
      />
      <Field
        label="Power capability at 20% state of charge"
        v-bind:tempRangeMin="parametersOfTheBattery.temperatureRanges.min.value"
        v-bind:tempRangeMax="parametersOfTheBattery.temperatureRanges.max.value"
        v-bind:tempUnit="parametersOfTheBattery.temperatureRanges.min.unit"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Power fade"
        v-bind:value="parametersOfTheBattery.powerFade.value"
        v-bind:unit="parametersOfTheBattery.powerFade.unit"
      />
      <Field
        label="Maximum allowed battery energy"
        v-bind:value="parametersOfTheBattery.maximumAllowedBatteryEnergy.value"
        v-bind:unit="parametersOfTheBattery.maximumAllowedBatteryEnergy.unit"
      />
      <Field
        label="Ratio between maximum allowed battery power (W) and battery energy
          (Wh)"
        v-bind:value="
          parametersOfTheBattery
            .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.value
        "
        v-bind:unit="
          parametersOfTheBattery
            .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.unit
        "
      />
      <Field
        label="Internal Resistance: Cell"
        v-bind:value="parametersOfTheBattery.internalResistanceCell.value"
        v-bind:unit="parametersOfTheBattery.internalResistanceCell.unit"
      />
      <Field
        label="Internal Resistance: Pack"
        v-bind:value="parametersOfTheBattery.internalResistancePack.value"
        v-bind:unit="parametersOfTheBattery.internalResistancePack.unit"
      />

      <Field
        label="Internal Resistance Increase: Pack"
        v-bind:value="
          parametersOfTheBattery.internalResistanceIncreasePack.value
        "
        v-bind:unit="parametersOfTheBattery.internalResistanceIncreasePack.unit"
      />
      <Field
        label="Expected Lifetime"
        v-bind:value="parametersOfTheBattery.expectedLifetime.value"
      />
      <Field
        label="Depth of discharge in the cycle-life test"
        v-bind:value="
          parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.value
        "
        v-bind:unit="
          parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.unit
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
