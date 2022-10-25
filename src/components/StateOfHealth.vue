<template  >
  <SectionHeader title="3. State of Health" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <Field
        data-cy="remaining-capacity"
        label="Rated capacity"
        unit="W"
        :value="stateOfHealth.ratedCapacity"
      />
      <Field
        label="Energy roundtrip efficiency change"
        unit="%"
        :value="stateOfHealth.batteryEnergy.energyRoundtripEfficiencyChange"
      />
      <Field
        label="Maximum allowed battery energy"
        unit="%"
        :value="stateOfHealth.batteryEnergy.maximumAllowedBatteryEnergy"
      />
      <Field
        label="Energy roundtrip efficiency"
        unit="%"
        :value="stateOfHealth.batteryEnergy.energyRoundtripEfficiency"
      />

      <Field
        label="Ratio maximum allowed battery power and maximum allowed battery energy"
        unit=""
        :value="
          stateOfHealth.ratioMaximumAllowedBatteryPowerAndMaximumAllowedBatteryEnergy
        "
      />
      <Field
        label="Nominal voltage"
        unit="%"
        :value="stateOfHealth.batteryVoltage.nominalVoltage"
      />
      <Field
        label="Max voltage"
        unit="%"
        :value="stateOfHealth.batteryVoltage.maxVoltage"
      />
      <Field
        label="Min voltage"
        unit="%"
        :value="stateOfHealth.batteryVoltage.minVoltage"
      />
      <Field
        label="Cell internal resistance"
        unit="%"
        :value="stateOfHealth.internalResistance.cellInternalResistance"
      />
      <Field
        label="Pack internal resistance increase"
        unit="%"
        :value="stateOfHealth.internalResistance.packInternalResistanceIncrease"
      />
      <Field
        label="Pack internal resistance"
        unit="%"
        :value="stateOfHealth.internalResistance.packInternalResistance"
      />

      <Field
        label="Capacity threshold exhaustion"
        unit="%"
        :value="stateOfHealth.capacityThresholdExhaustion"
      />
      <Field
        label="Original power capability"
        unit="%"
        :value="stateOfHealth.batteryPower.originalPowerCapability"
      />
      <Field
        label="Power fade"
        unit="%"
        :value="stateOfHealth.batteryPower.powerFade"
      />
      <Field
        label="Original power capability limits"
        unit="%"
        :value="stateOfHealth.batteryPower.originalPowerCapabilityLimits"
      />
      <Field
        label="Maximum allowed battery power"
        unit="%"
        :value="stateOfHealth.batteryPower.maximumAllowedBatteryPower"
      />
      <Field
        label="Power capability at 20 charge"
        unit="%"
        :value="stateOfHealth.batteryPower.powerCapabilityAt20Charge"
      />
      <Field
        label="Original power"
        unit="%"
        :value="stateOfHealth.batteryPower.originalPower"
      />
      <Field
        label="Power capability at 80 charge"
        unit="%"
        :value="stateOfHealth.batteryPower.powerCapabilityAt80Charge"
      />
      <Field
        label="Capacity fade"
        unit="%"
        :value="stateOfHealth.capacityFade"
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
