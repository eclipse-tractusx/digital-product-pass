<template>
  <SectionHeader title="3. State of Health" />

  <div class="section-content">
    <div class="sub-title-container">
      <span class="sub-title">Timestamp 23.06.2022 - 14:30:45</span>
    </div>
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Remaining capacity</span>
        <span class="field-value"
          >{{ stateOfHealth.remainingCapacity.value
          }}{{ stateOfHealth.remainingCapacity.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Overall capacity fade</span>
        <span class="field-value"
          >{{ stateOfHealth.overallCapacityFade.value
          }}{{ stateOfHealth.overallCapacityFade.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Remaining power capability and power fade</span
        >
        <span class="field-value"
          >{{ stateOfHealth.remainingPowerCapabilityAndPowerFade.value
          }}{{ stateOfHealth.remainingPowerCapabilityAndPowerFade.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Remaining round trip efficiency</span>
        <span class="field-value"
          >{{ stateOfHealth.remainingRoundTripEfficiency.value
          }}{{ stateOfHealth.remainingRoundTripEfficiency.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Actual cooling demand</span>
        <span class="field-value"
          >{{ stateOfHealth.actualCoolingDemand.value
          }}{{ stateOfHealth.actualCoolingDemand.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Evolution of self-discharging rates</span>
        <span class="field-value"
          >{{ stateOfHealth.evolutionOfSelfDischargingRates.value
          }}{{ stateOfHealth.evolutionOfSelfDischargingRates.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >Ohmic resistance and/or electrochemical impedance</span
        >
        <span class="field-value"
          >{{ stateOfHealth.ohmicResistanceAndOrElectrochemicalImpedance.value
          }}{{
            stateOfHealth.ohmicResistanceAndOrElectrochemicalImpedance.unit
          }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >The dates of manufacturing of the battery</span
        >
        <span class="field-value"
          >{{ stateOfHealth.theDatesOfManufacturingOfTheBattery.day }}/{{
            stateOfHealth.theDatesOfManufacturingOfTheBattery.month
          }}/{{ stateOfHealth.theDatesOfManufacturingOfTheBattery.year }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"
          >The dates of putting battery into service</span
        >
        <span class="field-value"
          >{{ stateOfHealth.theDatesOfPuttingBatteryIntoService.day }}/{{
            stateOfHealth.theDatesOfPuttingBatteryIntoService.month
          }}/{{ stateOfHealth.theDatesOfPuttingBatteryIntoService.year }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Energy throughput</span>
        <span class="field-value"
          >{{ stateOfHealth.energyThroughput.value
          }}{{ stateOfHealth.energyThroughput.unit }}</span
        >
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";

export default {
  name: "StateOfHealth",
  props: {
    sectionTitle: String,
    stateOfHealth: {},
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
