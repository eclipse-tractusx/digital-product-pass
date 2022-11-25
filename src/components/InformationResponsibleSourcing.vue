<template>
  <SectionHeader
    title="7. Information responsible sourcing"
    @click="toggle = !toggle"
  />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <a
        data-cy="responsible-raw-materials-report"
        :href="informationResponsibleSourcing.responsibleRawMaterialsReport.url"
        class="field-container"
        target="_blank"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">{{
          informationResponsibleSourcing.responsibleRawMaterialsReport.value
        }}</span>
      </a>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import Pdf from "../assets/pdf.svg";

export default {
  name: "InformationResponsibleSourcing",
  components: {
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    informationResponsibleSourcing: {
      type: Object,
      default: Object,
    },
  },
  setup() {
    return {
      Pdf,
    };
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

  .field-container {
    width: 100%;
  }
}
</style>
