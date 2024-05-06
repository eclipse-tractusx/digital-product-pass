<!--
  Tractus-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

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
      <!-- Transmission passport-->
      <template v-if="propsData.generalInformation">
        <v-row>
          <template v-if="propsData.generalInformation.additionalInformation">
            <v-col sm="12" md="3" class="pa-0 ma-0">
              <Field
                icon="mdi-fingerprint"
                :label="$t('sections.generalInformation.additionalInformation')"
                :value="propsData.generalInformation.additionalInformation"
              />
            </v-col>
          </template>
          <template
            v-if="propsData.generalInformation.physicalDimensionsProperty"
          >
            <v-col sm="12" md="6" class="pa-0 ma-0">
              <Field
                icon="mdi-ruler"
                :label="$t('sections.generalInformation.productDimensions')"
                :height="
                  propsData.generalInformation.physicalDimensionsProperty.height
                "
                :length="
                  propsData.generalInformation.physicalDimensionsProperty.length
                "
                unit="mm"
                :width="
                  propsData.generalInformation.physicalDimensionsProperty.width
                "
              />
              <Field
                icon="mdi-scale"
                :label="$t('sections.generalInformation.weight')"
                unit="kg"
                :value="
                  propsData.generalInformation.physicalDimensionsProperty.weight
                "
              />
            </v-col>
          </template>
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <template v-if="propsData.generalInformation.warrantyPeriod">
              <Field
                icon="mdi-license"
                :label="$t('sections.generalInformation.warranty')"
                :value="propsData.generalInformation.warrantyPeriod"
                unit="months"
              />
            </template>
            <template v-if="propsData.generalInformation.productDescription">
              <Field
                icon="mdi-license"
                :label="$t('sections.generalInformation.productDescription')"
                :value="propsData.generalInformation.productDescription"
              />
            </template>
            <template v-if="propsData.generalInformation.productType">
              <Field
                icon="mdi-license"
                :label="$t('sections.generalInformation.productType')"
                :value="propsData.generalInformation.productType"
              />
            </template>
          </v-col>
        </v-row>
      </template>
      <!-- Transmission passport ends here -->
      <template v-if="propsData.batteryIdentification">
        <v-row>
          <v-col sm="12" md="9" class="pa-0 ma-0">
            <DialogComponent class="field-dialog">
              <Field
                info
                data-cy="battery-id"
                icon="mdi-fingerprint"
                :label="$t('sections.generalInformation.batteryId')"
                :value="propsData.batteryIdentification.batteryIDDMCCode"
              />
              <template v-slot:title>
                {{
                  $t("sections.generalInformation.batteryIdDescriptionTitle")
                }}
              </template>
              <template v-slot:text>
                {{
                  $t("sections.generalInformation.batteryIdDescriptionValue")
                }}
              </template>
            </DialogComponent>
          </v-col>
        </v-row>
      </template>
      <v-row class="section">
        <template v-if="propsData.batteryIdentification">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <Field
              :label="$t('sections.generalInformation.batteryType')"
              icon="mdi-battery-unknown"
              :value="propsData.batteryIdentification.batteryType"
            />
          </v-col>
          <v-col sm="12" md="5" class="pa-0 ma-0">
            <Field
              icon="mdi-battery"
              :label="$t('sections.generalInformation.batteryModel')"
              :value="propsData.batteryIdentification.batteryModel"
            />
          </v-col>
        </template>
        <template v-if="propsData.warrantyPeriod">
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <Field
              icon="mdi-license"
              :label="$t('sections.generalInformation.warranty')"
              :value="propsData.warrantyPeriod"
              unit="month"
            />
          </v-col>
        </template>
      </v-row>
      <template v-if="propsData.physicalDimensions">
        <v-row class="section">
          <v-col sm="12" md="9" class="pa-0 ma-0">
            <Field
              icon="mdi-ruler"
              :label="$t('sections.generalInformation.dimensions')"
              :height="propsData.physicalDimensions.height"
              :length="propsData.physicalDimensions.length"
              unit="mm"
              :width="propsData.physicalDimensions.width"
            />
          </v-col>
          <v-col sm="12" md="3" class="pa-0 ma-0">
            <Field
              icon="mdi-scale"
              :label="$t('sections.generalInformation.weight')"
              unit="kg"
              :value="propsData.physicalDimensions.weight"
            />
          </v-col>
        </v-row>
      </template>
    </v-container>
    <v-container class="container-width-50">
      <template v-if="propsData.manufacturing">
        <v-row class="section">
          <v-col cols="12" class="pa-0 ma-0">
            <Field
              icon="mdi-calendar-range"
              style="background: #f9f9f9"
              :label="$t('sections.generalInformation.dateOfManufacturing')"
              :day="propsData.manufacturing.dateOfManufacturing"
            />
          </v-col>
        </v-row>
      </template>
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
              :label="$t('sections.generalInformation.placeOfManufacturing')"
              :value="propsData.manufacturing.address.locality.value"
            />
          </v-col>
        </v-row>
      </template>
      <template v-if="propsData.datePlacedOnMarket">
        <v-row>
          <v-col cols="12" class="pa-0 ma-0">
            <Field
              style="background: #f9f9f9"
              icon="mdi-calendar-range"
              :label="$t('sections.generalInformation.datePlacedOnMarket')"
              :day="propsData.datePlacedOnMarket"
            />
          </v-col>
        </v-row>
      </template>
    </v-container>
    <v-container class="container-width-80">
      <v-row style="min-height: 180px">
        <template v-if="propsData.manufacturer">
          <v-col v-if="propsData.manufacturer.address" md="5" class="pa-0 ma-0">
            <Field
              style="min-height: 168px"
              icon="mdi-factory"
              :label="$t('sections.generalInformation.manufacturerInformation')"
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
              :label="$t('sections.generalInformation.manufacturerContact')"
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
            :label="
              $t('sections.generalInformation.cO2FootprintTotal') +
              ' ' +
              '(production)'
            "
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

export default {
  name: "GeneralInformation",
  components: {
    Field,
    DialogComponent,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },

  data() {
    return {
      propsData: this.$props.data.aspect,
    };
  },
};
</script>
