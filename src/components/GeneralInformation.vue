<template>
  <SectionHeader title="1. General information" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <Field label="Battery ID" :value="generalInformation.batteryId" />
      <Field label="Battery Type" :value="generalInformation.batteryType" />
      <Field label="Battery Model" :value="generalInformation.batteryModel" />
    </div>
    <div class="sub-section-container">
      <Field
        class="full-width"
        label="Importer Information"
        :value="generalInformation.importerInformation.importerName"
      />
      <Field
        class="longer"
        label="Address"
        :city="generalInformation.importerInformation.address.city"
        :country="generalInformation.importerInformation.address.country"
        :postal="generalInformation.importerInformation.address.postal"
        :value="generalInformation.importerInformation.address.street"
      />
      <Field
        label="Contact phone number"
        :value="generalInformation.importerInformation.address.phoneNumber"
      />
      <Field
        label="Email"
        :value="generalInformation.importerInformation.address.email"
      />
    </div>
    <div class="sub-section-container">
      <Field
        label="Dimensions of the battery"
        :height="generalInformation.dimensions.height.value"
        :length="generalInformation.dimensions.length.value"
        :unit="generalInformation.dimensions.length.unit"
        :width="generalInformation.dimensions.width.value"
      />
      <Field
        label="Weight of the battery"
        :unit="generalInformation.weight.unit"
        :value="generalInformation.weight.value"
      />
      <Field
        label="Date of Manufacture"
        :day="generalInformation.manufacuringDate.day"
        :month="generalInformation.manufacuringDate.month"
        :year="generalInformation.manufacuringDate.year"
      />
      <Field
        label="Place of Manufacturing"
        :value="generalInformation.manufacturingPlace"
      />
      <Field
        class="two-third-width"
        label="Data of placing on the market"
        :day="generalInformation.placedToMarketDate.day"
        :month="generalInformation.placedToMarketDate.month"
        :year="generalInformation.placedToMarketDate.year"
      />
      <Field
        class="longer"
        label="Period for which the Commercial Warranty for the calendar life
          applies"
        :value="generalInformation.warranty"
      />
      <Field label="Status of the battery" :value="generalInformation.status" />
      <Field
        label="CO2 Footprint Total"
        :unit="generalInformation.co2.co2FootprintTotalKG.unit"
        :value="generalInformation.co2.co2FootprintTotalKG.value"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import Field from "./Field.vue";

export default {
  name: "GeneralInformation",
  components: {
    Field,
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    generalInformation: {
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

.hidden {
  display: none;
}

.longer {
  padding-bottom: 50px;
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
}
</style>
