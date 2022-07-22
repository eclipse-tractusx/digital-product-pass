<template>
  <SectionHeader title="6. Safety information" />

  <div class="section-content">
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Safety measures</span>
      </div>
      <a
        :href="safetyInformation.occupationalSafety.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          safetyInformation.occupationalSafety.value
        }}</span>
      </a>
      <a
        :href="safetyInformation.fireProtection.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          safetyInformation.fireProtection.value
        }}</span>
      </a>
      <a
        :href="safetyInformation.usableExtinguishingAgent.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          safetyInformation.usableExtinguishingAgent.value
        }}</span>
      </a>
    </div>
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Packaging Instructions</span>
      </div>
      <a
        :href="safetyInformation.instructionsForSafelyPackagingBatteries.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          safetyInformation.instructionsForSafelyPackagingBatteries.value
        }}</span>
      </a>
    </div>
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Transportation Instructions</span>
      </div>
      <a
        :href="safetyInformation.instructionsForSafelyTransportingBatteries.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          safetyInformation.instructionsForSafelyTransportingBatteries.value
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
  name: "SafetyInformation",
  props: {
    sectionTitle: String,
    safetyInformation: {},
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
  margin: 40px;
}
.field-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 33%;
  min-height: 120px;
  cursor: pointer;
  padding: 40px 0 80px 0;
}
.sub-section-container {
  display: flex;

  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}
.field-value {
  padding-left: 40px;
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
  padding: 40px 40px 20px 40px;
  width: 100%;
}
</style>
