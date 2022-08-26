<template v-if="safetyMeasures">
  <SectionHeader title="6. Safety information" v-on:click="toggle = !toggle" />

  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Safety measures</span>
      </div>
      <a v-if="safetyMeasures.occupationalSafety.url" :href="safetyMeasures.occupationalSafety.url" target="_blank"
        class="field-container" data-cy="occupational-safety">
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
            safetyMeasures.occupationalSafety.value
        }}</span>
      </a>
      <a v-if="safetyMeasures.fireProtection.url" :href="safetyMeasures.fireProtection.url" target="_blank"
        class="field-container">
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
            safetyMeasures.fireProtection.value
        }}</span>
      </a>
      <a v-if="safetyMeasures.usableExtinguishingAgent.url" :href="safetyMeasures.usableExtinguishingAgent.url"
        target="_blank" class="field-container">
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
            safetyMeasures.usableExtinguishingAgent.value
        }}</span>
      </a>
    </div>
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Packaging Instructions</span>
      </div>
      <a v-if="safetyMeasures.instructionsForSafelyPackagingBatteries.url"
        :href="safetyMeasures.instructionsForSafelyPackagingBatteries.url" target="_blank" class="field-container">
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
            safetyMeasures.instructionsForSafelyPackagingBatteries.value
        }}</span>
      </a>
    </div>
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Transportation Instructions</span>
      </div>
      <a v-if="safetyMeasures.instructionsForSafelyTransportingBatteries.url"
        :href="safetyMeasures.instructionsForSafelyTransportingBatteries.url" target="_blank" class="field-container">
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
            safetyMeasures.instructionsForSafelyTransportingBatteries.value
        }}</span>
      </a>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";
import Pdf from "../assets/pdf.svg";

export default {
  name: "safetyMeasures",
  props: {
    sectionTitle: String,
    safetyMeasures: {},
  },
  components: {
    SectionHeader,
    SectionContent,
  },
  setup() {
    return {
      Pdf,
    };
  },
  data() {
    return {
      toggle: false
    }
  }
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

.icon {
  width: 27px;
  height: 36px;
  margin: 20px;
}

.field-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  align-items: center;
  width: 33%;
  min-height: 120px;
  cursor: pointer;
  padding: 20px 0 60px 0;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}

.field-value {
  font-size: 14px;
  line-height: 20px;
  font-weight: bold;
}

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 22px 40px 22px 40px;
  width: 100%;
}

.hidden {
  display: none;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
    flex-direction: column;
    margin-bottom: 0;
  }

  .sub-section-container {
    flex-direction: column;
  }

  .field-container {
    width: 100%;
    border-bottom: solid 1px #edefe5;
  }

  .longer {
    padding-bottom: 0;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
