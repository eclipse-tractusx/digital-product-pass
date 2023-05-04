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
      <template v-if="propsData.batteryIdentification">
        <v-row>
          <v-col sm="12" md="9" class="pa-0 ma-0">
            <DialogComponent>
              <Field
                data-cy="battery-id"
                icon="mdi-fingerprint"
                label="Battery ID (DMC)"
                :value="propsData.batteryIdentification.batteryIDDMCCode"
              />
              <template v-slot:title>
                {{ descriptions.BatteryId.title }}
              </template>
              <template v-slot:text>
                {{ descriptions.BatteryId.value }}
              </template>
            </DialogComponent>
          </v-col>
        </v-row>
      </template>
      <v-row class="section">
        <template v-if="propsData.batteryIdentification">
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
        </template>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <Field
            icon="mdi-license"
            label="Warranty"
            :value="propsData.warrantyPeriod"
            unit="month"
          />
        </v-col>
      </v-row>
      <template v-if="propsData.physicalDimensions">
        <v-row class="section">
          <v-col sm="12" md="9" class="pa-0 ma-0">
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
      </template>
    </v-container>
    <v-container class="container-width-50">
      <v-row class="section">
        <v-col cols="12" class="pa-0 ma-0">
          <Field
            icon="mdi-calendar-range"
            style="background: #f9f9f9"
            label="Date of Manufacture"
            :day="propsData.manufacturing.dateOfManufacturing"
          />
        </v-col>
      </v-row>
      <template v-if="propsData.manufacturing">
        <v-row>
          <v-col
            v-if="propsData.manufacturing.address"
            cols="12"
            class="pa-0 ma-0"
          >
            <Field
              icon="mdi-map-marker-outline"
              style="background: #f9f9f9"
              label="Place of Manufacturing"
              :value="propsData.manufacturing.address.locality.value"
            />
          </v-col>
        </v-row>
      </template>
      <v-row>
        <v-col cols="12" class="pa-0 ma-0">
          <Field
            style="background: #f9f9f9"
            icon="mdi-calendar-range"
            label="Date of placing on the market"
            :day="propsData.datePlacedOnMarket"
          />
        </v-col>
      </v-row>
    </v-container>
    <v-container class="container-width-80">
      <v-row style="min-height: 180px">
        <template v-if="propsData.manufacturer">
          <v-col v-if="propsData.manufacturer.address" md="5" class="pa-0 ma-0">
            <Field
              style="min-height: 168px"
              icon="mdi-factory"
              label="Manufacturer Information"
              :city="propsData.manufacturer.address.locality.value"
              :country="propsData.manufacturer.address.country.shortName"
              :postal="propsData.manufacturer.address.postCode.value"
              :value="propsData.manufacturer.name"
            />
          </v-col>
        </template>
        <template v-if="propsData.manufacturer">
          <v-col v-if="propsData.manufacturer.contact" md="7" class="pa-0 ma-0">
            <Field
              style="min-height: 168px"
              icon="mdi-at"
              label="Manufaturer Contact"
              :phone="propsData.manufacturer.contact.phoneNumber"
              :email="propsData.manufacturer.contact.email"
              :website="propsData.manufacturer.contact.website"
              value=" "
            />
          </v-col>
        </template>
      </v-row>
      <v-row>
        <v-col v-if="propsData.manufacturer" md="12" class="pa-0 ma-0">
          <Field
            icon="mdi-molecule-co2"
            label="CO2 Footprint"
            :value="propsData.cO2FootprintTotal"
            unit="CO2e/kWh"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import DialogComponent from "../../general/Dialog.vue";
import descriptions from "../../../config/templates/descriptions.json";

export default {
  name: "GeneralInformation",
  components: {
    Field,
    DialogComponent,
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
      descriptions: descriptions,
      toggle: false,
      propsData: this.$props.data.passport,
    };
  },
};
</script>
