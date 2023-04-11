<!--
  Catena-X - Product Passport Consumer Frontend
 
  Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->


<template v-if="propsData">
  <div class="section">
    <div v-if="propsData.batteryIdentification" class="sub-section-container">
      <Field
        data-cy="battery-id"
        label="Battery ID"
        :value="propsData.batteryIdentification.batteryIDDMCCode"
      />
      <Field
        label="Battery Type"
        :value="propsData.batteryIdentification.batteryType"
      />
      <Field
        label="Battery Model"
        :value="propsData.batteryIdentification.batteryModel"
      />
    </div>
    <div v-if="propsData.manufacturer" class="sub-section-container">
      <Field
        class="full-width"
        label="Manufacturer Information"
        :value="propsData.manufacturer.name"
      />
      <Field
        class="longer"
        label="Address"
        :city="propsData.manufacturer.address.locality.value"
        :country="propsData.manufacturer.address.country.shortName"
        :postal="propsData.manufacturer.address.postCode.value"
        :value="propsData.manufacturer.name"
      />
      <Field
        label="Contact phone number"
        :value="propsData.manufacturer.contact.phoneNumber"
      />
      <Field label="Email" :value="propsData.manufacturer.contact.email" />
    </div>
    <div v-if="propsData.physicalDimensions" class="sub-section-container">
      <Field
        label="Dimensions of the battery"
        :height="propsData.physicalDimensions.height"
        :length="propsData.physicalDimensions.length"
        unit="mm"
        :width="propsData.physicalDimensions.width"
      />

      <Field
        label="Weight of the battery"
        unit="kg"
        :value="propsData.physicalDimensions.weight"
      />

      <Field
        label="Date of Manufacture"
        :day="propsData.manufacturing.dateOfManufacturing"
      />
      <Field
        label="Place of Manufacturing"
        :value="propsData.manufacturing.address.locality.value"
      />
      <Field
        class="two-third-width"
        label="Date of placing on the market"
        :day="propsData.datePlacedOnMarket"
      />
      <Field
        class="longer"
        label="Period for which the Commercial Warranty for the calendar life
          applies"
        :value="propsData.warrantyPeriod"
      />
      <Field
        label="Status of the battery"
        :value="propsData.stateOfBattery.statusBattery"
      />
      <Field
        label="CO2 Footprint Total"
        unit="kg"
        :value="propsData.cO2FootprintTotal"
      />
    </div>
  </div>
</template>

<script>
import Field from "../Field.vue";

export default {
  name: "GeneralInformation",
  components: {
    Field,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    data: {
      type: Object,
      default: Object,
    },
  },

  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.passport,
    };
  },
};
</script>
