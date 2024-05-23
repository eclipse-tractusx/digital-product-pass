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
    <v-container class="ma-0">
      <v-row class="section">
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <AttributeField
            icon="mdi-battery-plus"
            :attributes-list="electrolyteComposition"
            :label="$t('sections.cellChemistry.electrolyteComposition')"
            data-cy="electrolyte-composition"
            style="margin-bottom: 12px"
          />
        </v-col>
        <v-col sm="12" md="6" class="pa-0 ma-0">
          <div class="battery-graph">
            <div class="graph-icon">
              <v-icon>mdi-battery-outline</v-icon>
            </div>

            <div class="composition-graph">
              <div
                v-for="(detail, detailIndex) in batteryData.composition"
                :key="detailIndex"
                class="composition-section"
                :style="
                  detailIndex === 0 ? 'align-items: end' : 'align-items: start'
                "
              >
                <div
                  class="composition-title"
                  :style="
                    detailIndex === 0
                      ? 'align-self: flex-start'
                      : 'align-self: flex-end'
                  "
                >
                  {{ detail.title }}
                </div>
                <div class="composition-bar-container">
                  <div
                    v-for="(component, index) in detail.composition"
                    :key="index"
                    class="composition-bar"
                    :style="[
                      {
                        height: component.value * 1.5 + 'px',
                        backgroundColor: getColor(detailIndex + '.' + index),
                      },
                      detailIndex === 0 && index === 0
                        ? { 'border-top-left-radius': '6px' }
                        : {},
                      detailIndex === 1 && index === 0
                        ? { 'border-top-right-radius': '6px' }
                        : {},
                      detailIndex === 1 &&
                      index === detail.composition.length - 1
                        ? { 'border-bottom-right-radius': '6px' }
                        : {},
                      detailIndex === 0 &&
                      index === detail.composition.length - 1
                        ? { 'border-bottom-left-radius': '6px' }
                        : {},
                    ]"
                  >
                    <div
                      :style="detailIndex === 0 ? 'right:12px' : 'left:12px'"
                      class="component-label-line"
                    ></div>
                    <div
                      class="component-label-container"
                      :style="{
                        left: detailIndex === 0 ? '0' : 'auto',
                        right: detailIndex === 0 ? 'auto' : '0',
                      }"
                    >
                      {{ component.label }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <p class="type">{{ batteryData.title }}</p>
          </div>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <div class="element-chart-label" style="margin-bottom: 15px">
            {{ $t("sections.cellChemistry.recyclateContent") }}
          </div>
          <ElementChart :data="propsData" style="margin-left: 12px" />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script >
import AttributeField from "../AttributeField.vue";
import ElementChart from "../../passport/ElementChart.vue";
import batteryDetails from "../../../config/templates/batteryGraph.json";

export default {
  name: "CellChemistry",
  components: {
    AttributeField,
    ElementChart,
  },
  setup() {
    const getColor = (label) => {
      const colors = {
        "0.0": "#676BC6",
        0.1: "#FFEBCC",
        0.2: "#FFD700",
        0.3: "#BDB76B",
        0.4: "#FF4500",
        0.5: "#2E8B57",
        0.6: "#D2691E",
        "1.0": "#88982D",
        1.1: "#428C5B",
        1.2: "#F0F5D5",
        1.3: "#337B89",
        1.4: "#303030",
        1.5: "#486079",
        1.6: "#008B8B",
        1.7: "#B8860B",
        1.8: "#32CD32",
        1.9: "#FFA07A",
        "1.10": "#6A5ACD",
      };
      return colors[label] || "#333";
    };

    return {
      getColor,
    };
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      batteryData: batteryDetails,
      propsData: [
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
      electrolyteComposition: [
        {
          materialPercentageMassFraction: 19,
          materialName: "Pb",
          materialWeight: 2.5,
        },
      ],
    };
  },
  async mounted() {
    if (this.batteryData && this.batteryData.batteryDetails) {
      this.completeComponents(this.batteryData.batteryDetails);
    }
  },
  methods: {
    getLabelPosition(index, arrayLength) {
      const baseOffset = 20;
      const length = 30 - arrayLength;
      const position = length + baseOffset * index;
      return position;
    },
    completeComponents(batteryDetails) {
      batteryDetails.forEach((detail) => {
        const total = detail.composition.reduce(
          (sum, component) => sum + component.value,
          0
        );
        if (total < 100) {
          const difference = 100 - total;
          detail.composition.push({
            label: "Other",
            value: difference,
            unit: "%",
          });
        } else if (total > 100) {
          const factor = 100 / total;
          detail.composition = detail.composition.map((component) => ({
            label: component.label,
            value: component.value * factor,
            unit: component.unit,
          }));
        }
      });
    },
  },
};
</script>

