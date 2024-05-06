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
  <div class="section" v-if="propsData">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.substancesOfConcern">
            <template v-for="attr in propsData.substancesOfConcern" :key="attr">
              <Field
                :icon="callIconFinder('carbon')"
                :label="$t('sections.sustainability.substancesOfConcern')"
                :value="attr"
              />
            </template>
          </template>
          <template v-if="propsData.responsibleSourcingDocument">
            <template
              v-for="attr in propsData.responsibleSourcingDocument"
              :key="attr"
            >
              <InstructionsField
                :field="propsData.responsibleSourcingDocument"
              />
            </template>
          </template>
          <template v-if="propsData['PEF']">
            <template v-if="propsData['PEF'].carbon">
              <template v-for="attr in propsData['PEF'].carbon" :key="attr">
                <Field
                  :icon="callIconFinder('carbon')"
                  :label="attr.lifecycle"
                  :value="attr.value"
                  :unit="attr.unit"
                  :subText="attr.type"
                />
              </template>
            </template>
            <template v-if="propsData['PEF'].environmental">
              <template
                v-for="attr in propsData['PEF'].environmental"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('carbon')"
                  :label="attr.lifecycle"
                  :value="attr.value"
                  :unit="attr.unit"
                  :subText="attr.type"
                />
              </template>
            </template>
          </template>
          <template v-if="propsData.state">
            <Field
              :icon="callIconFinder('Sustainability')"
              :label="$t('sections.sustainability.state')"
              :value="propsData.state"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.recyclateContent">
            <template
              v-for="(attr, key) in propsData.recyclateContent"
              :key="key"
            >
              <template v-if="Array.isArray(attr)">
                <template v-for="attr in attr" :key="attr">
                  <Field
                    :icon="callIconFinder('element')"
                    :label="attr.substanceName"
                    :value="attr.substancePercentage"
                    unit="%"
                  />
                </template>
              </template>
              <template v-else>
                <Field
                  :icon="callIconFinder('element')"
                  :label="key"
                  :value="attr"
                  unit="%"
                />
              </template>
            </template>
          </template>
          <template v-if="propsData.material">
            <template v-for="attr in propsData.material.left" :key="attr">
              <Field
                :icon="callIconFinder('material')"
                :label="attr.name.type ? attr.name.type : attr.name"
                :value="computeFieldValue(attr)"
                :unit="attr.unit"
              />
              <template v-for="attr in attr.id" :key="attr">
                <Field
                  :icon="callIconFinder('id')"
                  :label="attr.type"
                  :value="attr.id"
                  :unit="attr.unit"
                />
              </template>
            </template>
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.criticalRawMaterials">
            <template
              v-for="attr in propsData.criticalRawMaterials"
              :key="attr"
            >
              <Field
                :icon="callIconFinder('element')"
                :label="$t('sections.sustainability.criticalRawMaterials')"
                :value="attr"
              />
            </template>
            <template v-if="propsData.carbonFootprint">
              <template
                v-if="propsData.carbonFootprint.crossSectoralStandardsUsed"
              >
                <template
                  v-for="attr in propsData.carbonFootprint
                    .crossSectoralStandardsUsed"
                  :key="attr"
                >
                  <Field
                    :icon="callIconFinder('material')"
                    :label="$t('sections.sustainability.crossSectoralStandard')"
                    :value="attr.crossSectoralStandard"
                  />
                </template>
              </template>
              <template v-if="propsData.carbonFootprint.co2FootprintTotal">
                <Field
                  :icon="callIconFinder('material')"
                  :label="$t('sections.sustainability.co2FootprintTotal')"
                  :value="propsData.carbonFootprint.co2FootprintTotal"
                  unit="kg CO₂ eq"
                />
              </template>

              <template
                v-if="propsData.carbonFootprint.productOrSectorSpecificRules"
              >
                <template
                  v-for="attr in propsData.carbonFootprint
                    .productOrSectorSpecificRules"
                  :key="attr"
                >
                  <Field
                    :icon="callIconFinder('operator')"
                    :label="
                      $t('sections.sustainability.productOrSectorSpecificRules')
                    "
                    :value="attr.operator"
                  />
                </template>
              </template>
            </template>
          </template>
          <template v-if="propsData.critical">
            <template v-for="attr in propsData.critical.left" :key="attr">
              <Field
                :icon="callIconFinder('material')"
                :label="$t('sections.sustainability.critical')"
                :value="attr"
              />
            </template>
          </template>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import InstructionsField from "../InstructionsField.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "SustainabilityComponent",
  components: {
    Field,
    InstructionsField,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.aspect.sustainability,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    computeFieldValue(attr) {
      let result = "";

      if (attr.percentage) {
        result += (result ? ", " : "") + attr.percentage + "%";
      }
      if (attr.name && attr.name.name) {
        result += (result ? ", " : "") + attr.name.name + ": ";
      }
      if (attr.value) {
        result += attr.value;
      } else if (attr.name) {
        result += (result ? ", " : "") + attr.name;
      }

      return result || "No value"; // Return 'No value' if all fields are empty
    },
  },
};
</script>
