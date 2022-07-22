<template>
  <SectionHeader title="1. General information" />

  <div class="section-content">
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Battery ID</span>
        <span class="field-value">{{ generalInformation.batteryId }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Battery Type</span>
        <span class="field-value">{{ generalInformation.batteryType }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Battery Model</span>
        <span class="field-value">{{ generalInformation.batteryModel }}</span>
      </div>
    </div>
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Importer Information</span>
        <span class="field-value">{{
          generalInformation.importerInformation.importerName
        }}</span>
      </div>
      <div class="field-container">
        <span class="field-label"></span>
        <span class="field-value">{{}}</span>
      </div>
      <div class="field-container">
        <span class="field-label"></span>
        <span class="field-value">{{}}</span>
      </div>
      <div class="field-container longer">
        <span class="field-label">Address</span>
        <span class="field-value">{{
          generalInformation.importerInformation.address.street
        }}</span>
        <span class="field-value">{{
          generalInformation.importerInformation.address.city
        }}</span>
        <span class="field-value">{{
          generalInformation.importerInformation.address.postal
        }}</span>
        <span class="field-value">{{
          generalInformation.importerInformation.address.country
        }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Contact phone number</span>
        <span class="field-value">{{
          generalInformation.importerInformation.address.phoneNumber
        }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Email</span>
        <span class="field-value">{{
          generalInformation.importerInformation.address.email
        }}</span>
      </div>
    </div>
    <div class="sub-section-container">
      <div class="field-container">
        <span class="field-label">Dimensions of the battery</span>
        <span class="field-value"
          >L:{{ generalInformation.dimensions.length.value
          }}{{ generalInformation.dimensions.length.unit }}, H:{{
            generalInformation.dimensions.height.value
          }}{{ generalInformation.dimensions.height.unit }}, W:{{
            generalInformation.dimensions.width.value
          }}{{ generalInformation.dimensions.width.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Weight of the battery</span>
        <span class="field-value"
          >{{ generalInformation.weight.value
          }}{{ generalInformation.weight.unit }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Date of Manufacture</span>
        <span class="field-value"
          >{{ generalInformation.manufacuringDate.day }}/{{
            generalInformation.manufacuringDate.month
          }}/{{ generalInformation.manufacuringDate.year }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label">Place of Manufacturing</span>
        <span class="field-value">{{
          generalInformation.manufacturingPlace
        }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Data of placing on the market</span>
        <span class="field-value"
          >{{ generalInformation.placedToMarketDate.day }}/{{
            generalInformation.placedToMarketDate.month
          }}/{{ generalInformation.placedToMarketDate.year }}</span
        >
      </div>
      <div class="field-container">
        <span class="field-label"></span>
        <span class="field-value">{{}}</span>
      </div>
      <div class="field-container">
        <span class="field-label"
          >Period for which the Commercial Warranty for the calendar life
          applies</span
        >
        <span class="field-value">{{ generalInformation.warranty }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">Status of the battery</span>
        <span class="field-value">{{ generalInformation.status }}</span>
      </div>
      <div class="field-container">
        <span class="field-label">CO2 Footprint Total</span>
        <span class="field-value"
          >{{ generalInformation.co2.co2FootprintTotalKG.value
          }}{{ generalInformation.co2.co2FootprintTotalKG.unit }}</span
        >
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import SectionContent from "./SectionContent.vue";

export default {
  name: "GeneralInformation",
  props: {
    sectionTitle: String,
    generalInformation: {},
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
</style>
