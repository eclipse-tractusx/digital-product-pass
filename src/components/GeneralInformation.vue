<template>
  <SectionHeader title="1. General information" />

  <div class="section-content">
    <div class="sub-section-container">
      <Field label="Battery ID" v-bind:value="generalInformation.batteryId" />
      <Field
        label="Battery Type"
        v-bind:value="generalInformation.batteryType"
      />
      <Field
        label="Battery Model"
        v-bind:value="generalInformation.batteryModel"
      />
    </div>
    <div class="sub-section-container">
      <Field
        class="full-width"
        label="Importer Information"
        v-bind:value="generalInformation.importerInformation.importerName"
      />
      <Field
        class="longer"
        label="Address"
        v-bind:value="generalInformation.importerInformation.address.street"
        v-bind:city="generalInformation.importerInformation.address.city"
        v-bind:postal="generalInformation.importerInformation.address.postal"
        v-bind:country="generalInformation.importerInformation.address.country"
      />
      <Field
        label="Contact phone number"
        v-bind:value="
          generalInformation.importerInformation.address.phoneNumber
        "
      />
      <Field
        label="Email"
        v-bind:value="generalInformation.importerInformation.address.email"
      />
    </div>
    <div class="sub-section-container">
      <Field
        label="Dimensions of the battery"
        v-bind:length="generalInformation.dimensions.length.value"
        v-bind:unit="generalInformation.dimensions.length.unit"
        v-bind:height="generalInformation.dimensions.height.value"
        v-bind:width="generalInformation.dimensions.width.value"
      />
      <Field
        label="Weight of the battery"
        v-bind:value="generalInformation.weight.value"
        v-bind:unit="generalInformation.weight.unit"
      />
      <Field
        label="Date of Manufacture"
        v-bind:day="generalInformation.manufacuringDate.day"
        v-bind:month="generalInformation.manufacuringDate.month"
        v-bind:year="generalInformation.manufacuringDate.year"
      />

      <Field
        label="Place of Manufacturing"
        v-bind:value="generalInformation.manufacturingPlace"
      />
      <Field
        class="two-third-width"
        label="Data of placing on the market"
        v-bind:day="generalInformation.placedToMarketDate.day"
        v-bind:month="generalInformation.placedToMarketDate.month"
        v-bind:year="generalInformation.placedToMarketDate.year"
      />
      <Field
        label="Period for which the Commercial Warranty for the calendar life
          applies"
        v-bind:value="generalInformation.warranty"
      />
      <Field
        label="Status of the battery"
        v-bind:value="generalInformation.status"
      />
      <Field
        label="CO2 Footprint Total"
        v-bind:value="generalInformation.co2.co2FootprintTotalKG.value"
        v-bind:unit="generalInformation.co2.co2FootprintTotalKG.unit"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";
import Field from "./Field.vue";

export default {
  name: "GeneralInformation",
  props: {
    sectionTitle: String,
    generalInformation: {},
  },
  components: {
    Field,
    SectionHeader,
    SectionContent,
  },
};
</script>

<style scoped>
.full-width {
  width: 100% !important;
}
.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}
.two-third-width {
  width: 66% !important;
}
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}

.longer {
  padding-bottom: 50px;
}
</style>
