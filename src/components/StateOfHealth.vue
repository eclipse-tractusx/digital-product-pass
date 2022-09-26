<template>
  <SectionHeader title="3. State of Health" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-title-container">
      <span class="sub-title">Timestamp 23.06.2022 - 14:30:45</span>
    </div>
    <div class="sub-section-container">
      <Field
        data-cy="remaining-capacity"
        label="Remaining capacity"
        :unit="stateOfHealth.remainingCapacity.unit"
        :value="stateOfHealth.remainingCapacity.value"
      />
      <Field
        label="Overall capacity fade"
        :unit="stateOfHealth.overallCapacityFade.unit"
        :value="stateOfHealth.overallCapacityFade.value"
      />
      <Field
        label="Remaining power capability and power fade"
        :unit="stateOfHealth.remainingPowerCapabilityAndPowerFade.unit"
        :value="stateOfHealth.remainingPowerCapabilityAndPowerFade.value"
      />
      <Field
        label="Remaining round trip efficiency"
        :unit="stateOfHealth.remainingRoundTripEfficiency.unit"
        :value="stateOfHealth.remainingRoundTripEfficiency.value"
      />
      <Field
        label="Actual cooling demand"
        :unit="stateOfHealth.actualCoolingDemand.unit"
        :value="stateOfHealth.actualCoolingDemand.value"
      />
      <Field
        label="Evolution of self-discharging rates"
        :unit="stateOfHealth.evolutionOfSelfDischargingRates.unit"
        :value="stateOfHealth.evolutionOfSelfDischargingRates.value"
      />
      <Field
        label="Ohmic resistance and/or electrochemical impedance"
        :unit="stateOfHealth.ohmicResistanceAndOrElectrochemicalImpedance.unit"
        :value="
          stateOfHealth.ohmicResistanceAndOrElectrochemicalImpedance.value
        "
      />
      <Field
        label="The dates of manufacturing of the battery"
        :day="stateOfHealth.theDatesOfManufacturingOfTheBattery.day"
        :month="stateOfHealth.theDatesOfManufacturingOfTheBattery.month"
        :year="stateOfHealth.theDatesOfManufacturingOfTheBattery.year"
      />
      <Field
        label="The dates of putting battery into service"
        :day="stateOfHealth.theDatesOfPuttingBatteryIntoService.day"
        :month="stateOfHealth.theDatesOfPuttingBatteryIntoService.month"
        :year="stateOfHealth.theDatesOfPuttingBatteryIntoService.year"
      />
      <Field
        label="Energy throughput"
        :unit="stateOfHealth.energyThroughput.unit"
        :value="stateOfHealth.energyThroughput.value"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import Field from "./Field.vue";

export default {
  name: "StateOfHealth",
  components: {
    Field,
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    stateOfHealth: {
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

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 40px 40px 20px 40px;
  width: 100%;
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
