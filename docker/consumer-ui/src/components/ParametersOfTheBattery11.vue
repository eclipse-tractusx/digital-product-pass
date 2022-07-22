<template>
  <SectionHeader title="4. Parameters of the battery" />

  <div class="section-content">
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">State of charge</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.stateOfCharge.value
          }}{{ parametersOfTheBattery.stateOfCharge.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Maximum Allowed Battery Power</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.maximumAllowedBatteryPower.value
          }}{{ parametersOfTheBattery.maximumAllowedBatteryPower.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Minimal voltage with temperature ranges when relevant</span
        >
        <span class="field-value"
          >{{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.value
          }}{{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.unit
          }}</span
        >
        <span class="test"
          >Temp ranges:
          {{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.tempRanges.min
              .value
          }}
          {{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.tempRanges.min
              .unit
          }}
          —
          {{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.tempRanges.max
              .value
          }}
          {{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.tempRanges.max
              .unit
          }}
        </span>
        <span class="test"
          >Referance test:
          {{
            parametersOfTheBattery
              .minimalVoltageWithTemperatureRangesWhenRelevant.refTest
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Nominal voltage with temperature ranges when relevant</span
        >
        <span class="field-value"
          >{{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.value
          }}{{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.unit
          }}</span
        >
        <span class="test"
          >Temp ranges:
          {{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.tempRanges.min
              .value
          }}
          {{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.tempRanges.min
              .unit
          }}
          —
          {{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.tempRanges.max
              .value
          }}
          {{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.tempRanges.max
              .unit
          }}
        </span>
        <span class="test"
          >Referance test:
          {{
            parametersOfTheBattery
              .nominalVoltageWithTemperatureRangesWhenRelevant.refTest
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Maximum voltage with temperature ranges when relevant</span
        >
        <span class="field-value"
          >{{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.value
          }}{{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.unit
          }}</span
        >
        <span class="test"
          >Temp ranges:
          {{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.tempRanges.min
              .value
          }}
          {{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.tempRanges.min
              .unit
          }}
          —
          {{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.tempRanges.max
              .value
          }}
          {{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.tempRanges.max
              .unit
          }}
        </span>
        <span class="test"
          >Referance test:
          {{
            parametersOfTheBattery
              .maximumVoltageWithTemperatureRangesWhenRelevant.refTest
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Original power capability with temperature ranges when relevant</span
        >
        <span class="field-value"
          >{{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant.value
          }}{{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant.unit
          }}</span
        >
        <span class="test"
          >Temp ranges:
          {{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant
              .tempRanges.min.value
          }}
          {{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant
              .tempRanges.min.unit
          }}
          —
          {{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant
              .tempRanges.max.value
          }}
          {{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant
              .tempRanges.max.unit
          }}
        </span>
        <span class="test"
          >Referance test:
          {{
            parametersOfTheBattery
              .originalPowerCapabilityWithTemperatureRangesWhenRelevant.refTest
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Capacity threshold for exhaustion </span>
        <span class="field-value"
          >{{ parametersOfTheBattery.capacityThresholdForExhaustion.value
          }}{{
            parametersOfTheBattery.capacityThresholdForExhaustion.unit
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Temperature range the battery can withstand when not in use
        </span>
        <span class="field-value">
          {{
            parametersOfTheBattery
              .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
              .value
          }}
          {{
            parametersOfTheBattery
              .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.min
              .unit
          }}
          —
          {{
            parametersOfTheBattery
              .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.max
              .value
          }}
          {{
            parametersOfTheBattery
              .temperatureRangeTheBatteryCanWithstandWhenNotInUse.tempRanges.max
              .unit
          }}
        </span>

        <span class="test"
          >Referance test:
          {{
            parametersOfTheBattery
              .temperatureRangeTheBatteryCanWithstandWhenNotInUse.refTest
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">C-rate of Relevant Cycle-life Test</span>
        <span class="field-value">{{
          parametersOfTheBattery.cRateOfRelevantCycleLifeTest.value
        }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Energy Roundtrip Efficiency</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.energyRoundtripEfficiency.value
          }}{{ parametersOfTheBattery.energyRoundtripEfficiency.unit }}</span
        >
      </div>
    </div>
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Rated Capacity</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.ratedCapacity.value
          }}{{ parametersOfTheBattery.ratedCapacity.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Applied discharge rate</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.appliedDischargeRate.value
          }}{{ parametersOfTheBattery.appliedDischargeRate.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Applied charge rate</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.appliedChargeRate.value
          }}{{ parametersOfTheBattery.appliedChargeRate.unit }}</span
        >
      </div>
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Capacity fade</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.originalPower.value
          }}{{ parametersOfTheBattery.originalPower.unit }}</span
        >
      </div>
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Original power</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.capacityFade.value
          }}{{ parametersOfTheBattery.capacityFade.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Power capability at 80% state of charge</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.powerCapabilityAt80StateOfCharge.value
          }}{{
            parametersOfTheBattery.powerCapabilityAt80StateOfCharge.unit
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Power capability at 20% state of charge</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.powerCapabilityAt20StateOfCharge.value
          }}{{
            parametersOfTheBattery.powerCapabilityAt20StateOfCharge.unit
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Temperature ranges</span>
        <span class="field-value">
          {{ parametersOfTheBattery.temperatureRanges.min.value }}
          {{ parametersOfTheBattery.temperatureRanges.min.unit }}
          —
          {{ parametersOfTheBattery.temperatureRanges.max.value }}
          {{ parametersOfTheBattery.temperatureRanges.max.unit }}</span
        >
      </div>
    </div>
    <!-- Subsection -->
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Power fade</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.powerFade.value
          }}{{ parametersOfTheBattery.powerFade.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Maximum allowed battery energy</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.maximumAllowedBatteryEnergy.value
          }}{{ parametersOfTheBattery.maximumAllowedBatteryEnergy.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Ratio between maximum allowed battery power (W) and battery energy
          (Wh)</span
        >
        <span class="field-value"
          >{{
            parametersOfTheBattery
              .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.value
          }}{{
            parametersOfTheBattery
              .ratioBetweenMaximumAllowedBatteryPowerWAndBatteryEnergyWh.unit
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Internal Resistance: Cell</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.internalResistanceCell.value
          }}{{ parametersOfTheBattery.internalResistanceCell.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Internal Resistance: Pack</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.internalResistancePack.value
          }}{{ parametersOfTheBattery.internalResistancePack.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Internal Resistance Increase: Pack</span>
        <span class="field-value"
          >{{ parametersOfTheBattery.internalResistanceIncreasePack.value
          }}{{
            parametersOfTheBattery.internalResistanceIncreasePack.unit
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Expected Lifetime</span>
        <span class="field-value">{{
          parametersOfTheBattery.expectedLifetime.value
        }}</span>
      </div>
      <div class="field-container">
        <span class="field-label"
          >Depth of discharge in the cycle-life test</span
        >
        <span class="field-value"
          >{{ parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.value
          }}{{
            parametersOfTheBattery.depthOfDischargeInTheCycleLifeTest.unit
          }}</span
        >
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";

export default {
  name: "ParametersOfTheBattery",
  props: {
    sectionTitle: String,
    parametersOfTheBattery: {},
  },
  components: {
    SectionHeader,
    SectionContent,
  },
};
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
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
.field-container {
  display: flex;
  flex-direction: column;
  width: 33%;
  min-height: 120px;
}
.field-label {
  padding: 30px 0px 10px 40px;
  font-size: 12px;
  color: #777777;
}
.field-value {
  padding-left: 40px;
  font-size: 14px;
  line-height: 20px;
  font-weight: bold;
}
.longer {
  padding-bottom: 50px;
}
.test {
  font-size: 12px;

  padding: 4px 0 4px 40px;
}
</style>
