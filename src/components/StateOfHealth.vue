<template>
  <SectionHeader title="3. State of Health"/>

  <div class="section-content">
    <div class="sub-title-container">
      <span class="sub-title">Timestamp 23.06.2022 - 14:30:45</span>
    </div>
    <div class="sub-section-container">
      <Field
          label="Remaining capacity"
          v-bind:unit="stateOfHealth.remainingCapacity.unit"
          v-bind:value="stateOfHealth.remainingCapacity.value"
      />
      <Field
          label="Overall capacity fade"
          v-bind:unit="stateOfHealth.overallCapacityFade.unit"
          v-bind:value="stateOfHealth.overallCapacityFade.value"
      />
      <Field
          label="Remaining power capability and power fade"
          v-bind:unit="stateOfHealth.remainingPowerCapabilityAndPowerFade.unit"
          v-bind:value="stateOfHealth.remainingPowerCapabilityAndPowerFade.value"
      />
      <Field
          label="Remaining round trip efficiency"
          v-bind:unit="stateOfHealth.remainingRoundTripEfficiency.unit"
          v-bind:value="stateOfHealth.remainingRoundTripEfficiency.value"
      />
      <Field
          label="Actual cooling demand"
          v-bind:unit="stateOfHealth.actualCoolingDemand.unit"
          v-bind:value="stateOfHealth.actualCoolingDemand.value"
      />
      <Field
          label="Evolution of self-discharging rates"
          v-bind:unit="stateOfHealth.evolutionOfSelfDischargingRates.unit"
          v-bind:value="stateOfHealth.evolutionOfSelfDischargingRates.value"
      />
      <Field
          label="Ohmic resistance and/or electrochemical impedance"
          v-bind:unit="
          stateOfHealth.ohmicResistanceAndOrElectrochemicalImpedance.unit
        "
          v-bind:value="
          stateOfHealth.ohmicResistanceAndOrElectrochemicalImpedance.value
        "
      />
      <Field
          label="The dates of manufacturing of the battery"
          v-bind:day="stateOfHealth.theDatesOfManufacturingOfTheBattery.day"
          v-bind:month="stateOfHealth.theDatesOfManufacturingOfTheBattery.month"
          v-bind:year="stateOfHealth.theDatesOfManufacturingOfTheBattery.year"
      />
      <Field
          label="The dates of putting battery into service"
          v-bind:day="stateOfHealth.theDatesOfPuttingBatteryIntoService.day"
          v-bind:month="stateOfHealth.theDatesOfPuttingBatteryIntoService.month"
          v-bind:year="stateOfHealth.theDatesOfPuttingBatteryIntoService.year"
      />
      <Field
          label="Energy throughput"
          v-bind:unit="stateOfHealth.energyThroughput.unit"
          v-bind:value="stateOfHealth.energyThroughput.value"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";
import Field from "./Field.vue";

export default {
  name: "StateOfHealth",
  props: {
    sectionTitle: String,
    stateOfHealth: {},
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

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 40px 40px 20px 40px;
  width: 100%;
}
</style>
