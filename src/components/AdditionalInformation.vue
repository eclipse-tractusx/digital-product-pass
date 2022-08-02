<template v-if="additionalInformation">
  <SectionHeader title="8. Additional information" />

  <div class="section-content">
    <div class="sub-section-container longer">
      <a
        v-if="additionalInformation.linkToTheLabelElement.url"
        :href="additionalInformation.linkToTheLabelElement.url"
        target="_blank"
        class="field-container"
        data-cy="link-to-the-label-element"
      >
        <span class="field-label">Label Elements</span>
        <span class="field-value">{{
          additionalInformation.linkToTheLabelElement.value
        }}</span>
      </a>
      <div
        class="field-container"
        v-if="
          additionalInformation
            .symbolIndicatingSeparateCollectionAndHeavyMetalContent.value
        "
      >
        <span class="field-label"
          >Symbol indicating ‘separate collection’ and heavy metal content
        </span>
        <img :src="Trashcan" alt="profile" class="icon" />

        <span class="field-value">{{
          additionalInformation
            .symbolIndicatingSeparateCollectionAndHeavyMetalContent.value
        }}</span>
      </div>

      <div class="field-container">
        <span class="field-label"></span>
        <span class="field-value"></span>
      </div>
      <a
        v-if="additionalInformation.carbonFootprintDeclaration.url"
        :href="additionalInformation.carbonFootprintDeclaration.url"
        target="_blank"
        class="field-container"
      >
        <span class="field-label">Carbon Footprint Declaration</span>
        <span class="field-value">{{
          additionalInformation.carbonFootprintDeclaration.value
        }}</span>
      </a>

      <Field
        label="Carbon Footprint Performance Class"
        v-bind:value="
          additionalInformation.carbonFootprintPerformanceClass.value
        "
      />
      <div
        class="list-container"
        v-if="additionalInformation.hazardousSubstancesContainedInTheBattery"
      >
        <ul>
          <span class="list-label"
            >Hazardous substances contained in the battery</span
          >
          <li
            :key="substances"
            v-for="substances in additionalInformation.hazardousSubstancesContainedInTheBattery"
          >
            {{ substances }}
          </li>
        </ul>
      </div>
      <a
        v-if="additionalInformation.informationAsAResultOfBatteryUse.url"
        :href="additionalInformation.informationAsAResultOfBatteryUse.url"
        target="_blank"
        class="field-container"
      >
        <span class="field-label">Information as a result of battery use</span>
        <span class="field-value">{{
          additionalInformation.informationAsAResultOfBatteryUse.value
        }}</span>
      </a>
      <a
        v-if="additionalInformation.euDeclarationOfConformity.url"
        :href="additionalInformation.euDeclarationOfConformity.url"
        target="_blank"
        class="field-container"
      >
        <span class="field-label">EU declaration of conformity</span>
        <span class="field-value">{{
          additionalInformation.euDeclarationOfConformity.value
        }}</span>
      </a>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";
import Trashcan from "@/assets/trashcan.svg";
import Field from "./Field.vue";

export default {
  name: "AdditionalInformation",
  props: {
    sectionTitle: String,
    additionalInformation: {},
  },
  components: {
    Field,
    SectionHeader,
    SectionContent,
  },
  setup() {
    return {
      Trashcan,
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
.field-container {
  display: flex;
  flex-direction: column;
  width: 33%;
  min-height: 120px;
}
.field-label {
  padding: 30px 0px 10px 40px;
  font-size: 12px;
  text-decoration: inherit;
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
.icon {
  width: 37px;
  height: 39px;
  margin-left: 30px;
}
.list-container {
  width: 33%;
  padding-left: 40px;
  padding-bottom: 40px;
}
.list-label {
  padding: 30px 0px 10px 0px;
  font-size: 12px;
  color: #777777;
}
ul {
  display: flex;
  flex-direction: column;
}
li {
  margin-left: 20px;
  font-weight: bold;
}
</style>
