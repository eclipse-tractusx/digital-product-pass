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
              :icon="callIconFinder(card.icon)"
              size="x-large"
            ></v-icon>
          </span>

          <v-container
            class="pa-0"
            v-if="card.title == 'batteryCards.titleSustainability'"
          >
            <v-row class="sustainability pa-0 ma-0">
              <ElementChart
                :data="card.value"
                style="margin: 24px 4px 2px 12px; padding: 0"
              />

              <div class="ma-0 pa-0; co2-container">
                <span class="co2-value" style="padding-bottom: 0">
                  {{ card.secondValue ? card.secondValue : "-" }}
                  {{ card.secondValueUnits }}
                </span>
                <div class="co2-label" style="padding-top: 0">
                  {{ $t(card.secondLabel) }}
                </div>
                <div>
                  <img
                    :src="getImageByKey(separateCollectionImage)"
                    alt="Separate collection"
                    style="margin-top: 12px"
                  />
                </div>
              </div>
            </v-row>
          </v-container>
          <div v-else>
            <div class="card-label">
              {{ $t(card.label) }}
            </div>
            <div class="card-value">
              {{ card.value ? card.value : "-" }} {{ card.valueUnits }}
            </div>
            <v-divider></v-divider>
            <div
              v-if="card.title == 'batteryCards.titleHealth'"
              style="margin-bottom: 60px"
            >
              <div class="charging-cycles-title">
                {{ $t("batteryCards.chargingCycles") }}
              </div>
              <BarChart :currentValue="currentValue" :maxValue="maxValue" />
            </div>
            <div v-else>
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
import ElementChart from "./ElementChart.vue";
import BarChart from "./BarChart.vue";
import passportUtil from "@/utils/passportUtil.js";
import DialogComponent from "../general/Dialog.vue";
import noCollection from "../../media/noCollection.svg";
import Battery00 from "../../media/Battery00.svg";
import BatteryCd from "../../media/BatteryCd.svg";
import BatteryHg from "../../media/BatteryHg.svg";
import BatteryPb from "../../media/BatteryPb.svg";

export default {
  name: "BatteryCards",
  components: {
    ElementChart,
    BarChart,
    DialogComponent,
  },
  props: {
    data: {
      type: Object,
      default: () => ({}),
    },
  },

  setup() {
    return {
      noCollection,
      Battery00,
      BatteryCd,
      BatteryHg,
      BatteryPb,
    };
  },
  data() {
    return {
      separateCollectionImage: "BATTERY_CD",
      currentValue:
        this.$props.data.aspect.batteryCycleLife.cycleLifeTestDepthOfDischarge,
      maxValue: this.$props.data.aspect.batteryCycleLife.expectedLifetime,
      cards: [
        {
          title: "batteryCards.titleGeneral",
          label: "batteryCards.labelGeneral",
          secondLabel: "batteryCards.secondLabelGeneral",
          icon: "general",
          value: this.$props.data.aspect.batteryIdentification
            ? this.$props.data.aspect.batteryIdentification.batteryType
            : "-",
          secondValue: this.$props.data.aspect.batteryIdentification
            ? this.$props.data.aspect.batteryIdentification.batteryModel
            : "-",
        },
        {
          title: "batteryCards.titlePerformance",
          label: "batteryCards.labelPerformance",
          secondLabel: "batteryCards.secondLabelPerformance",
          icon: "performance",
          value:
            this.$props.data.aspect.electrochemicalProperties.ratedCapacity,
          valueUnits: "kWh",
          secondValueUnits: "kW",
          secondValue: this.$props.data.aspect.electrochemicalProperties
            .batteryPower
            ? this.$props.data.aspect.electrochemicalProperties.batteryPower
                .originalPowerCapability
            : "-",
          description: {
            title: "batteryCards.descriptionPerformanceTitle",
            value: "batteryCards.descriptionPerformanceValue",
          },
        },
        {
          title: "batteryCards.titleHealth",
          label: "batteryCards.labelHealth",
          secondLabel: "batteryCards.secondLabelHealth",
          icon: "health",
          value: this.$props.data.aspect.stateOfBattery
            ? this.$props.data.aspect.stateOfBattery.stateOfHealth
            : "-",
          valueUnits: "%",
          secondValue: this.$props.data.aspect.batteryIdentification
            ? this.$props.data.aspect.batteryIdentification.batteryModel
            : "-",
          description: {
            title: "batteryCards.descriptionHealthTitle",
            value: "batteryCards.descriptionHealthValue",
          },
        },
        {
          title: "batteryCards.titleSustainability",
          icon: "sustainability",
          secondLabel: "batteryCards.secondLabelSustainability",

          value: [
            {
              materialPercentageMassFraction: 47,
              materialName: "Ni",
              materialWeight: 2.5,
            },
            {
              materialPercentageMassFraction: 9,
              materialName: "Co",
              materialWeight: 2.5,
            },
            {
              materialPercentageMassFraction: 19,
              materialName: "Li",
              materialWeight: 2.5,
            },
            {
              materialPercentageMassFraction: 0,
              materialName: "Pb",
              materialWeight: 2.5,
            },
          ],
          cathodeCompositionOther: [
            {
              materialPercentageMassFraction: 19,
              materialName: "Pb",
              materialWeight: 2.5,
            },
          ],
          secondValue: this.$props.data.aspect.cO2FootprintTotal,
          description: {
            title: "batteryCards.descriptionHSustainabilityTitle",
            value: "batteryCards.descriptionSustainabilityValue",
          },
        },
      ],
    };
  },

  methods: {
    getImageByKey(key) {
      const imageMap = {
        BATTERY_00: this.Battery00,
        BATTERY_CD: this.BatteryCd,
        BATTERY_HG: this.BatteryHg,
        BATTERY_PB: this.BatteryPb,
      };
      return imageMap.hasOwnProperty(key) ? imageMap[key] : this.noCollection;
    },
    callIconFinder(icon) {
      return passportUtil.iconFinder(icon);
    },
  },
};
</script>
