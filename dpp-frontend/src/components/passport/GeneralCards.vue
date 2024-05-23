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
          <span class="card-title">{{ $t(card.title) }} </span>
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
              {{ $t(card.label) }}
            </div>
            <div class="card-value">
              {{ card.value ? card.value : "-" }} {{ card.valueUnits }}
            </div>
            <v-divider></v-divider>
            <div>
              <div class="card-second-label">
                {{ $t(card.secondLabel) }}
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
                {{ $t(card.description.title) }}
              </template>
              <template v-slot:text>
                {{ $t(card.description.value) }}
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
  name: "GeneralCards",
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
          title: "generalCards.titleGeneral",
          label: "generalCards.productName",
          secondLabel: "generalCards.productType",
          icon: "mdi-pound",
          value: this.$props.data.aspect.typology
            ? this.$props.data.aspect.typology.shortName
            : "-",
          secondValue: this.$props.data.aspect.typology
            ? this.$props.data.aspect.typology.class.code
            : "-",
        },
        {
          title: "generalCards.titleManufacturing",
          label: "generalCards.manufacturerId",
          secondLabel: "generalCards.dateOfManufacturing",
          icon: "mdi-chart-timeline-variant-shimmer",
          value: this.$props.data.aspect.operation.manufacturer
            ? this.$props.data.aspect.operation.manufacturer.manufacturer
            : "-",
          secondValue: this.$props.data.aspect.operation.manufacturer
            ? this.$props.data.aspect.operation.manufacturer.manufacturingDate
            : "-",
          description: {
            title: "generalCards.descriptionManufacturingTitle",
            value: "generalCards.descriptionManufacturingValue",
          },
        },
        {
          title: "generalCards.titleAspectVersion",
          label: "generalCards.currentVersion",
          secondLabel: "generalCards.issued",
          icon: "mdi-aspect",
          value: this.$props.data.aspect.metadata.version,
          secondValue: this.$props.data.aspect.metadata.issueDate,
          description: {
            title: "generalCards.descriptionAspectVersionTitle",
            value: "generalCards.descriptionAspectVersionValue",
          },
        },
        {
          title: "generalCards.titleSustainability",
          label: "generalCards.totalCo2Footprint",
          secondLabel: "generalCards.warrantyPeriod",
          icon: "mdi-leaf",
          value:
            this.$props.data.aspect.sustainability &&
            this.$props.data.aspect.sustainability["PEF"] &&
            this.$props.data.aspect.sustainability["PEF"].carbon
              ? this.$props.data.aspect.sustainability["PEF"].carbon[0].value
              : "-",
          valueUnits: (this.$props.data.aspect.sustainability["PEF"].carbon[0].unit !== ""
              ? this.$props.data.aspect.sustainability["PEF"].carbon[0].unit
              : "t CO₂") + " Total",
          secondValue: this.$props.data.aspect.commercial
            ? this.$props.data.aspect.commercial.warranty
            : "-",
          secondValueUnits:
            this.$props.data.aspect.commercial &&
            this.$props.data.aspect.commercial.warranty
              ? "months"
              : "",
          description: {
            title: "generalCards.descriptionSustainabilityTitle",
            value: "generalCards.descriptionSustainabilityValue",
          },
        },
      ],
    };
  },
};
</script>
