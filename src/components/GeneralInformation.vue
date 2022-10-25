<template v-if="generalInformation.batteryIdentification">
  <SectionHeader title="1. General information" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <Field
        data-cy="battery-id"
        label="Battery ID"
        :value="generalInformation.batteryIdentification.batteryIDDMCCode"
      />
      <Field
        label="Battery Type"
        :value="generalInformation.batteryIdentification.batteryType"
      />
      <Field
        label="Battery Model"
        :value="generalInformation.batteryIdentification.batteryModel"
      />
    </div>
    <div class="sub-section-container">
      <Field
        class="full-width"
        label="Importer Information"
        :value="generalInformation.manufacturer.name"
      />
      <Field
        class="longer"
        label="Address"
        :city="generalInformation.manufacturer.address.locality.value"
        :country="generalInformation.manufacturer.address.country.shortName"
        :postal="generalInformation.manufacturer.address.postCode.value"
        :value="generalInformation.manufacturer.name"
      />
      <Field
        label="Contact phone number"
        :value="generalInformation.manufacturer.contact.phoneNumber"
      />
      <Field
        label="Email"
        :value="generalInformation.manufacturer.contact.email"
      />
    </div>
    <div class="sub-section-container">
      <Field
        label="Dimensions of the battery"
        :height="generalInformation.physicalDimensions.height"
        :length="generalInformation.physicalDimensions.length"
        unit="mm"
        :width="generalInformation.physicalDimensions.width"
      />

      <Field
        label="Weight of the battery"
        unit="kg"
        :value="generalInformation.physicalDimensions.weight"
      />

      <Field
        label="Date of Manufacture"
        :day="generalInformation.manufacturing.dateOfManufacturing"
      />
      <Field
        label="Place of Manufacturing"
        :value="generalInformation.manufacturing.address.locality.value"
      />
      <Field
        class="two-third-width"
        label="Data of placing on the market"
        :day="generalInformation.datePlacedOnMarket"
      />
      <Field
        class="longer"
        label="Period for which the Commercial Warranty for the calendar life
          applies"
        :value="generalInformation.warrantyPeriod"
      />
      <Field
        label="Status of the battery"
        :value="generalInformation.stateOfBattery.statusBattery"
      />
      <Field
        label="CO2 Footprint Total"
        unit="kg"
        :value="generalInformation.cO2FootprintTotal"
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
