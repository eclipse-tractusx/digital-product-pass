<!--
  Tractus-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

<template v-if="callCurrentData">
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              callCurrentData.active?.other ||
                callCurrentData.hazardous ||
                callCurrentData.substancesOfConcern
            )
          "
        >
          <template v-if="callCurrentData.active?.other">
            <template v-for="attr in callCurrentData.active.other" :key="attr">
              <div class="element-chart-label">
                {{ $t("sections.materials.otherMaterials") }}
              </div>
              <template
                v-for="attrChild in attr.materialIdentification"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('materialIdentification')"
                  :value="attrChild.name"
                  :label="$t('sections.materials.name')"
                />
                <Field
                  :icon="callIconFinder('materialIdentification')"
                  :value="attrChild.type + ':' + ' ' + attrChild.id"
                  :label="$t('sections.materials.materialIdentification')"
                />
              </template>
              <Field
                :icon="callIconFinder(attr.location)"
                :value="attr.location"
                :label="$t('sections.materials.location')"
              />
              <Field
                :icon="callIconFinder('recycledContent')"
                :value="attr.recycled"
                :label="$t('sections.materials.recycledContent')"
                unit="%"
              />

              <template v-for="attrChild in attr.documentation" :key="attr">
                <Field
                  :icon="callIconFinder('serial')"
                  :value="attrChild.content"
                  :label="attrChild.header"
                />
              </template>
            </template>
          </template>
          <template v-if="callCurrentData.hazardous">
            <div class="element-chart-label">
              {{ $t("sections.materials.hazardous") }}
            </div>
            <template
              v-for="(attr, key) in callCurrentData.hazardous"
              :key="key"
            >
              <Field
                :icon="callIconFinder(key)"
                :value="attr.concentration"
                unit="%"
                :label="key"
              />
            </template>
          </template>
          <template v-if="callCurrentData.substancesOfConcern">
            <template
              v-for="(attr, key) in callCurrentData.substancesOfConcern.content"
              :key="key"
            >
              <DialogComponent class="field-dialog">
                <Field
                  :info="attr.exemption ? attr.exemption : null"
                  :icon="callIconFinder('concentration')"
                  :value="attr.concentration"
                  :unit="attr.unit"
                  :label="$t('sections.materials.concentration')"
                />
                <template v-slot:title>
                  {{ $t("sections.materials.exemption") }}
                </template>
                <template v-slot:text>
                  {{ attr.exemption }}
                </template>
              </DialogComponent>
              <template v-if="attr.concentrationRange">
                <template
                  v-for="childAttr in attr.concentrationRange"
                  :key="childAttr"
                >
                  <Field
                    :icon="callIconFinder('concentrationRange')"
                    :value="childAttr.min + ' - ' + childAttr.max"
                    :unit="attr.unit"
                    :label="$t('sections.materials.concentrationRange')"
                  />
                </template>
              </template>
              <template v-if="attr.id">
                <template v-for="childAttr in attr.id" :key="childAttr">
                  <Field
                    :icon="callIconFinder('id')"
                    :value="childAttr.id"
                    :label="childAttr.type"
                  />
                  <Field
                    :icon="callIconFinder('id')"
                    :value="childAttr.name"
                    :label="$t('sections.materials.name')"
                  />
                </template>
              </template>
              <template v-if="attr.documentation">
                <template
                  v-for="childAttr in attr.documentation"
                  :key="childAttr"
                >
                  <Field
                    :icon="callIconFinder('documentation')"
                    :value="childAttr.content"
                    :label="$t('sections.materials.documentation')"
                    :subText="childAttr.header"
                  />
                </template>
              </template>
              <Field
                :icon="callIconFinder('location')"
                :value="attr.location"
                :label="$t('sections.materials.location')"
              />
              <template v-if="attr.hazardClassification">
                <DialogComponent class="field-dialog">
                  <Field
                    :info="
                      attr.hazardClassification.statement
                        ? attr.hazardClassification.statement
                        : null
                    "
                    :icon="callIconFinder('hazardClassification')"
                    :value="attr.hazardClassification.category"
                    :subText="attr.hazardClassification.class"
                    :label="$t('sections.materials.hazardClassification')"
                  />
                  <template v-slot:title>
                    {{ $t("sections.materials.statement") }}
                  </template>
                  <template v-slot:text>
                    {{ attr.hazardClassification.statement }}
                  </template>
                </DialogComponent>
              </template>
            </template>
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="callHasContent(callCurrentData.materialComposition)"
        >
          <template v-if="callCurrentData.materialComposition">
            <template
              v-for="attr in callCurrentData.materialComposition.content"
              :key="attr"
            >
              <Field
                :icon="callIconFinder('concentration')"
                :value="attr.concentration"
                :unit="attr.unit"
                :label="$t('sections.materials.concentration')"
              />
              <template v-if="attr.id">
                <template v-for="childAttr in attr.id" :key="childAttr">
                  <Field
                    :icon="callIconFinder('id')"
                    :value="childAttr.id"
                    :label="childAttr.type"
                  />
                  <Field
                    :icon="callIconFinder('id')"
                    :value="childAttr.name"
                    :label="$t('sections.materials.name')"
                  />
                </template>
              </template>
              <template v-if="attr.documentation">
                <template
                  v-for="childAttr in attr.documentation"
                  :key="childAttr"
                >
                  <Field
                    :icon="callIconFinder('documentation')"
                    :value="childAttr.content"
                    :label="$t('sections.materials.documentation')"
                    :subText="childAttr.header"
                  />
                </template>
              </template>
              <Field
                :icon="callIconFinder('recycled')"
                :value="attr.recycled"
                :unit="attr.unit"
                :label="$t('sections.materials.recycled')"
              />
              <Field
                :icon="callIconFinder('renewable')"
                :value="attr.renewable"
                :unit="attr.unit"
                :label="$t('sections.materials.renewable')"
              />
            </template>
          </template>
        </v-col>
        <template v-if="callCurrentData.composition">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label"></div>
            <template
              v-if="this.numberOfLocations == 1 || this.numberOfLocations > 2"
            >
              <Doughnut :data="chart" :options="options" />
            </template>
            <template v-else>
              <div class="battery-graph">
                <div class="graph-icon">
                  <v-icon>mdi-battery-outline</v-icon>
                </div>

                <div class="composition-graph">
                  <div
                    v-for="(detail, detailIndex) in formattedComposition"
                    :key="detailIndex"
                    class="composition-section"
                    :style="
                      detailIndex === 0
                        ? 'align-items: end'
                        : 'align-items: start'
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
                        :style="
                          getBarStyle(detailIndex, index, component.value)
                        "
                      >
                        <div
                          :style="
                            detailIndex === 0 ? 'right:12px' : 'left:12px'
                          "
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
                <p class="type">
                  {{ chemistry }}
                </p>
              </div>
            </template>
          </v-col>
        </template>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="callHasContent(callCurrentData.active)"
        >
          <div class="element-chart-label" style="margin-bottom: 15px">
            {{ $t("sections.materials.recyclateContent") }}
          </div>

          <ElementChart
            :data="callCurrentData.active"
            style="margin-left: 12px"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import ElementChart from "../../passport/ElementChart.vue";
import passportUtil from "@/utils/passportUtil.js";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Doughnut } from "vue-chartjs";
import DialogComponent from "../../general/Dialog.vue";

ChartJS.register(ArcElement, Tooltip, Legend);

export default {
  name: "MaterialsComponent",
  components: {
    Field,
    DialogComponent,
    ElementChart,
    Doughnut,
  },
  props: {
    data: {
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      chart: null,
      options: {
        responsive: true,
      },
      numberOfLocations: 0,
      formattedComposition: [],
      propsData: this.$props.data.aspect?.materials || [],
      tppData:
        this.$props.data.aspect?.productUnspecificParameters?.materials || [],
      chemistry: this.$props.data.aspect?.identification?.chemistry || "",
    };
  },
  computed: {
    callCurrentData() {
      return passportUtil.currentData(this.tppData, this.propsData);
    },
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    callHasContent(data) {
      return passportUtil.hasContent(data);
    },
    callToSentenceCase(data) {
      return passportUtil.toSentenceCase(data);
    },
    parseData() {
      const compositionsByLocation = {};

      this.callCurrentData.composition.forEach((item) => {
        const { location, id, concentration, unit } = item;
        if (!compositionsByLocation[location]) {
          compositionsByLocation[location] = {
            title: `${this.$t("sections.materials.compositionOf")} ${location}`,
            composition: [],
          };
        }
        compositionsByLocation[location].composition.push({
          label: id[0].name,
          value: concentration,
          unit: unit || "%",
        });
      });

      this.formattedComposition = Object.values(compositionsByLocation);
      this.numberOfLocations = Object.keys(compositionsByLocation).length;

      if (this.numberOfLocations === 1 || this.numberOfLocations > 2) {
        this.prepareChartData();
      }
    },
    completeComponents() {
      this.formattedComposition.forEach((detail) => {
        let total = detail.composition.reduce(
          (sum, component) => sum + component.value,
          0
        );
        if (total < 100) {
          detail.composition.push({
            label: "Other",
            value: 100 - total,
            unit: "%",
          });
        } else if (total > 100) {
          let factor = 100 / total;
          detail.composition = detail.composition.map((component) => ({
            ...component,
            value: component.value * factor,
          }));
        }
      });
    },
    prepareChartData() {
      this.chart = {
        labels: this.formattedComposition.map((comp) => comp.title),
        datasets: [
          {
            data: this.formattedComposition.map((comp) =>
              comp.composition.reduce((acc, curr) => acc + curr.value, 0)
            ),
            backgroundColor: this.formattedComposition.map((_, index) =>
              this.getColorForChart(index)
            ),
          },
        ],
      };
    },
    getBarStyle(detailIndex, index, value) {
      const baseStyle = {
        height: `${value * 1.5}px`,
        backgroundColor: this.getColor(`${detailIndex}.${index}`),
      };
      const borderRadiusStyle = this.getBorderRadiusStyle(detailIndex, index);
      return { ...baseStyle, ...borderRadiusStyle };
    },

    getBorderRadiusStyle(detailIndex, index) {
      let style = {};
      if (detailIndex === 0 && index === 0) {
        style["border-top-left-radius"] = "6px";
      }
      if (detailIndex === 1 && index === 0) {
        style["border-top-right-radius"] = "6px";
      }
      if (
        detailIndex === 1 &&
        index === this.formattedComposition[detailIndex].composition.length - 1
      ) {
        style["border-bottom-right-radius"] = "6px";
      }
      if (
        detailIndex === 0 &&
        index === this.formattedComposition[detailIndex].composition.length - 1
      ) {
        style["border-bottom-left-radius"] = "6px";
      }
      return style;
    },
    getColorForChart(index) {
      const colors = [
        "#676BC6",
        "#FFEBCC",
        "#FFD700",
        "#BDB76B",
        "#FF4500",
        "#2E8B57",
        "#D2691E",
        "#88982D",
        "#428C5B",
      ];
      return colors[index % colors.length];
    },

    getColor(label) {
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
      return colors[label] || "#337B89";
    },
  },
  mounted() {
    if (this.callCurrentData.composition) {
      this.parseData();
      this.completeComponents();
    }
  },
};
</script>

