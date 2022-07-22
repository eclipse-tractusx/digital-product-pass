<template>
  <SectionHeader title="5. Dismantling Procedures" />

  <div class="section-content">
    <div class="sub-section-container">
      <a
        :href="dismantlingProcedures.vehicleDismantlingProcedure.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          dismantlingProcedures.vehicleDismantlingProcedure.value
        }}</span>
      </a>
      <a
        :href="dismantlingProcedures.batteryDismantlingProcedure.url"
        target="_blank"
        class="field-container"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          dismantlingProcedures.batteryDismantlingProcedure.value
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
  name: "DismantlingProcedures",
  props: {
    sectionTitle: String,
    dismantlingProcedures: {},
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
</style>
