<template>
  <SectionHeader
    title="4. Parameters of the battery"
    @click="toggle = !toggle"
  />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <Field
        data-cy="state-of-charge"
        label="State of charge"
        :unit="parametersOfTheBattery.stateOfCharge.unit"
        :value="parametersOfTheBattery.stateOfCharge.value"
      />
      <Field
        label="Maximum Allowed Battery Power"
        :unit="parametersOfTheBattery.maximumAllowedBatteryPower.unit"
        :value="parametersOfTheBattery.maximumAllowedBatteryPower.value"
      />
      <Field
        label="Minimal voltage with temperature ranges when relevant"
        :temp-max="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
        :temp-min="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
        :temp-unit="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
        :test="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
        :unit="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
        :value="
          parametersOfTheBattery.minimalVoltageWithTemperatureRangesWhenRelevant
            .value
        "
      />
      <Field
        label="Nominal voltage with temperature ranges when relevant"
        :temp-max="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
        :temp-min="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
        :temp-unit="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
        :test="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
        :unit="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
        :value="
          parametersOfTheBattery.nominalVoltageWithTemperatureRangesWhenRelevant
            .value
        "
      />
      <Field
        label="Maximum voltage with temperature ranges when relevant"
        :temp-max="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.max.value
        "
        :temp-min="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.value
        "
        :temp-unit="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .tempRanges.min.unit
        "
        :test="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .refTest
        "
        :unit="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .unit
        "
        :value="
          parametersOfTheBattery.maximumVoltageWithTemperatureRangesWhenRelevant
            .value
        "
      />
      <Field
        label="Original power capability with temperature ranges when relevant"
        :temp-max="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .max.value
        "
        :temp-min="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .min.value
        "
        :temp-unit="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.tempRanges
            .min.unit
        "
        :test="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.refTest
        "
        :unit="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.unit
        "
        :value="
          parametersOfTheBattery
            .originalPowerCapabilityWithTemperatureRangesWhenRelevant.value
        "
      />
      <Field
        label="Capacity threshold for exhaustion"
        :unit="parametersOfTheBattery.capacityThresholdForExhaustion.unit"
        :value="parametersOfTheBattery.capacityThresholdForExhaustion.value"
      />
      <Field
        label="Temperature range the battery can withstand when not in use"
        :temp-range-max="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.max
            .value
        "
        :temp-range-min="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
            .value
        "
        :temp-unit="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
            .unit
        "
        :test="
          parametersOfTheBattery
            .temperatureRangeTheBatteryCanWithstandWhenNotInUse.refTest
        "
      />
      <Field
        label="C-rate of Relevant Cycle-life Test"
        :value="parametersOfTheBattery.cRateOfRelevantCycleLifeTest.value"
      />
      <Field
        label="Energy Roundtrip Efficiency"
        :unit="parametersOfTheBattery.energyRoundtripEfficiency.unit"
        :value="parametersOfTheBattery.energyRoundtripEfficiency.value"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Rated Capacity"
        :unit="parametersOfTheBattery.ratedCapacity.unit"
        :value="parametersOfTheBattery.ratedCapacity.value"
      />
      <Field
        label="Applied discharge rate"
        :unit="parametersOfTheBattery.appliedDischargeRate.unit"
        :value="parametersOfTheBattery.appliedDischargeRate.value"
      />
      <Field
        label="Applied charge rate"
        :unit="parametersOfTheBattery.appliedChargeRate.unit"
        :value="parametersOfTheBattery.appliedChargeRate.value"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Capacity fade"
        :unit="parametersOfTheBattery.originalPower.unit"
        :value="parametersOfTheBattery.originalPower.value"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Original power"
        :unit="parametersOfTheBattery.capacityFade.unit"
        :value="parametersOfTheBattery.capacityFade.value"
      />
      <Field
        label="Power capability at 80% state of charger"
        :unit="parametersOfTheBattery.powerCapabilityAt80StateOfCharge.unit"
        :value="parametersOfTheBattery.powerCapabilityAt80StateOfCharge.value"
      />
      <Field
        label="Power capability at 20% state of charge"
        :unit="parametersOfTheBattery.powerCapabilityAt20StateOfCharge.unit"
        :value="parametersOfTheBattery.powerCapabilityAt20StateOfCharge.value"
      />
      <Field
        label="Power capability at 20% state of charge"
        :temp-range-max="parametersOfTheBattery.temperatureRanges.max.value"
        :temp-range-min="parametersOfTheBattery.temperatureRanges.min.value"
        :temp-unit="parametersOfTheBattery.temperatureRanges.min.unit"
      />
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <Field
        label="Power fade"
        :unit="parametersOfTheBattery.powerFade.unit"
        :value="parametersOfTheBattery.powerFade.value"
      />
      <Field
        label="Maximum allowed battery energy"
        :unit="parametersOfTheBattery.maximumAllowedBatteryEnergy.unit"
        :value="parametersOfTheBattery.maximumAllowedBatteryEnergy.value"
      />
      <Field
        label="Ratio between maximum allowed battery power (W) and battery energy(Wh)"
        :unit="
          parametersOfTheBattery
            .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.unit
        "
        :value="
          parametersOfTheBattery
            .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.value
        "
      />
      <Field
        label="Internal Resistance: Cell"
        :unit="parametersOfTheBattery.internalResistanceCell.unit"
        :value="parametersOfTheBattery.internalResistanceCell.value"
      />
      <Field
        label="Internal Resistance: Pack"
        :unit="parametersOfTheBattery.internalResistancePack.unit"
        :value="parametersOfTheBattery.internalResistancePack.value"
      />
      <Field
        label="Internal Resistance Increase: Pack"
        :unit="parametersOfTheBattery.internalResistanceIncreasePack.unit"
        :value="parametersOfTheBattery.internalResistanceIncreasePack.value"
      />
      <Field
        label="Expected Lifetime"
        :value="parametersOfTheBattery.expectedLifetime.value"
      />
      <Field
        label="Depth of discharge in the cycle-life test"
        :unit="parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.unit"
        :value="parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.value"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import Field from "./Field.vue";

export default {
  name: "ParametersOfTheBattery",
  components: {
    Field,
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    parametersOfTheBattery: {
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

.hidden {
  display: none;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
  }

  .section-content {
    margin-bottom: 0;
  }

  .longer {
    padding-bottom: 0;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
