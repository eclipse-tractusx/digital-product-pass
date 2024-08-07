<!--
  Tractus-X -  Digital Product Pass Application
  
  Copyright (c) 2022, 2024 BMW AG
  Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

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
  name: "TransmissionCards",
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
          secondLabel: "transmissionCards.partID",
          icon: "mdi-pound",
          value:
            this.$props.data.aspect.generic?.identification?.type
              ?.nameAtManufacturer,
          secondValue:
            this.$props.data.aspect.generic?.identification?.type
              ?.manufacturerPartId,
        },
        {
          title: "generalCards.titleManufacturing",
          label: "generalCards.manufacturerId",
          secondLabel: "generalCards.dateOfManufacturing",
          icon: "mdi-chart-timeline-variant-shimmer",
          value:
            this.$props.data.aspect.generic?.operation?.manufacturer
              ?.manufacturer,
          secondValue:
            this.$props.data.aspect.generic?.operation?.manufacturer
              ?.manufacturingDate,
          description: {
            title: "Manufacturing",
            value: "Description of the manufacturing",
          },
        },
        {
          title: "transmissionCards.parameters",
          label: "transmissionCards.driveType",
          secondLabel: "transmissionCards.oilType",
          icon: "mdi-aspect",
          value: this.$props.data.aspect.specific?.driveType?.[0],
          secondValue: this.$props.data.aspect.specific?.oil?.oilType?.[0],
          description: {
            title: "Specific parameters",
            value: "Description of the Specific parameters",
          },
        },
        {
          title: "generalCards.titleSustainability",
          label: "transmissionCards.carbonFootprint",
          secondLabel: "generalCards.warrantyPeriod",
          icon: "mdi-leaf",
          value:
            this.$props.data.aspect.generic?.sustainability?.productFootprint
              ?.carbon?.[0]?.value,

          valueUnits:
            this.$props.data.aspect.generic?.sustainability?.productFootprint
              ?.carbon?.[0]?.unit,
          secondValue:
            this.$props.data.aspect.generic?.characteristics?.warranty,
          secondValueUnits: this.$props.data.aspect.generic?.characteristics
            ?.warranty
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
