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

<template>
  <v-container class="cards-container">
    <v-row>
      <v-col
        cols="12"
        md="3"
        v-for="(card, index) in cards"
        :key="index"
        style="padding: 6px !important"
      >
        <div class="card-container fill-height">
          <span class="card-title">{{ card.title }} </span>
          <span>
            <v-icon
              class="card-icon"
              start
              md
              :icon="card.icon"
              size="x-large"
            ></v-icon>
          </span>
          <div>
            <div class="card-label">
              {{ card.label }}
            </div>
            <div class="card-value">
              {{ card.value ? card.value : "-" }} {{ card.valueUnits }}
            </div>
            <v-divider></v-divider>
            <div>
              <div class="card-second-label">
                {{ card.secondLabel }}
              </div>
              <div class="card-second-value">
                {{ card.secondValue ? card.secondValue : "-" }}
                {{ card.secondValueUnits }}
              </div>
            </div>
          </div>
          <span v-if="card.description" class="card-info-icon">
            <DialogComponent>
              <v-icon start md icon="mdi-information-outline"></v-icon>
              <template v-slot:title>
                {{ card.description.title }}
              </template>
              <template v-slot:text>
                {{ card.description.value }}
              </template>
            </DialogComponent>
          </span>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import DialogComponent from "../general/Dialog.vue";

export default {
  name: "BatteryCards",
  components: {
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
      cards: [
        {
          title: "GENERAL",
          label: "Product name",
          secondLabel: "Product type",
          icon: "mdi-pound",
          value: this.$props.data.passport.typology
            ? this.$props.data.passport.typology.name
            : "-",
          secondValue: this.$props.data.passport.typology.nameAtCustomer,
        },
        {
          title: "MANUFACTURING",
          label: "Manufacturer Id",
          secondLabel: "Date of Manufacturing",
          icon: "mdi-chart-timeline-variant-shimmer",
          value: this.$props.data.passport.operation.manufacturer
            ? this.$props.data.passport.operation.manufacturer.manufacturerId
            : "-",
          secondValue: this.$props.data.passport.serialization
            .manufacturingInformation
            ? this.$props.data.passport.serialization.manufacturingInformation
                .date
            : "-",
          description: {
            title: "Manufacturing",
            value: "Description of the manufacturing",
          },
        },
        {
          title: "PASSPORT VERSION",
          label: "Current version",
          secondLabel: "Issued",
          icon: "mdi-passport",
          value: this.$props.data.passport.metadata.version,
          secondValue: this.$props.data.passport.metadata.issueDate,
          description: {
            title: "Passport version",
            value: "Description of the Passport version",
          },
        },
        {
          title: "SUSTAINABILITY",
          label: "Total CO2 footprint",
          secondLabel: "Warranty period",
          icon: "mdi-leaf",
          value: this.$props.data.passport.sustainability.carbonFootprint
            ? this.$props.data.passport.sustainability.carbonFootprint
                .carbonContentTotal
            : "-",
          valueUnits: "t CO2 Total",
          secondValue: this.$props.data.passport.commercial.warranty,
          secondValueUnits: this.$props.data.passport.commercial.warranty
            ? "months"
            : "",
          description: {
            title: "Sustainability",
            value: "Description of the Sustainability",
          },
        },
      ],
    };
  },
};
</script>
