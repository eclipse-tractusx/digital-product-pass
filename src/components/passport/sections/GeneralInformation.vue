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
    <v-container>
      <v-row v-if="propsData.batteryIdentification">
        <v-col sm="12" md="9" class="pa-0 ma-0">
          <Field
            data-cy="battery-id"
            icon="mdi-fingerprint"
            label="Battery ID (DMC)"
            :value="propsData.batteryIdentification.batteryIDDMCCode"
          />
        </v-col>
      </v-row>
      <v-row class="section">
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <Field
            label="Battery Type"
            icon="mdi-battery-unknown"
            :value="propsData.batteryIdentification.batteryType"
          />
        </v-col>
        <v-col sm="12" md="5" class="pa-0 ma-0">
          <Field
            icon="mdi-battery"
            label="Battery Model"
            :value="propsData.batteryIdentification.batteryModel"
          />
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <Field
            icon="mdi-license"
            label="Warranty"
            :value="propsData.warrantyPeriod"
            unit="months"
          />
        </v-col>
      </v-row>
      <v-row class="section">
        <v-col
          sm="12"
          md="9"
          v-if="propsData.physicalDimensions"
          class="pa-0 ma-0"
        >
          <Field
            icon="mdi-ruler"
            label="Dimensions of the battery"
            :height="propsData.physicalDimensions.height"
            :length="propsData.physicalDimensions.length"
            unit="mm"
            :width="propsData.physicalDimensions.width"
          />
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <Field
            icon="mdi-scale"
            label="Weight"
            unit="kg"
            :value="propsData.physicalDimensions.weight"
          />
        </v-col>
      </v-row>
    </v-container>
    <v-container class="container-width-70">
      <v-row class="section">
        <v-col sm="12" md="8" class="pa-0 ma-0">
          <Field
            icon="mdi-calendar-range"
            style="background: #f9f9f9"
            label="Date of Manufacture"
            :day="propsData.manufacturing.dateOfManufacturing"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col sm="12" md="8" class="pa-0 ma-0">
          <Field
            icon="mdi-map-marker-outline"
            style="background: #f9f9f9"
            label="Place of Manufacturing"
            :value="propsData.manufacturing.address.locality.value"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col md="8" class="pa-0 ma-0">
          <Field
            style="background: #f9f9f9"
            icon="mdi-calendar-range"
            label="Date of placing on the market"
            :day="propsData.datePlacedOnMarket"
          />
        </v-col>
      </v-row>
    </v-container>
    <v-container class="container-width-60">
      <v-row>
        <v-col md="12" class="pa-0 ma-0">
          <Field
            v-if="propsData.manufacturer"
            style="background: #f9f9f9"
            icon="mdi-factory"
            label="Manufacturer Information"
            :city="propsData.manufacturer.address.locality.value"
            :country="propsData.manufacturer.address.country.shortName"
            :phone="propsData.manufacturer.contact.phoneNumber"
            :email="propsData.manufacturer.contact.email"
            :website="propsData.manufacturer.contact.website"
            :postal="propsData.manufacturer.address.postCode.value"
            :value="propsData.manufacturer.name"
          />
        </v-col>
      </v-row>
    </v-container>
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
