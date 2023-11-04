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
  <div class="section">
    <v-container class="ma-0">
      <template
        v-if="
          propsData.semanticId ==
          'urn:bamm:io.catenax.transmission:3.0.1#Transmission'
        "
      >
        <v-row class="section">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template
              v-if="propsData.aspect.sustainability.substancesOfConcern"
            >
              <div
                v-for="(attr, index) in propsData.aspect.sustainability
                  .substancesOfConcern"
                :key="index"
              >
                <Field
                  icon="mdi-image-size-select-small"
                  label="Substances of concern"
                  :value="attr"
                />
              </div>
            </template>
            <template
              v-if="propsData.aspect.sustainability.responsibleSourcingDocument"
            >
              <InstructionsField
                :field="
                  propsData.aspect.sustainability.responsibleSourcingDocument
                "
              />
            </template>
          </v-col>
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template
              v-if="propsData.aspect.sustainability.substancesOfConcern"
            >
              <div
                v-for="(attr, key) in propsData.aspect.sustainability
                  .recyclateContent"
                :key="key"
              >
                <Field
                  icon="mdi-image-size-select-small"
                  :label="key"
                  :value="attr"
                />
              </div>
            </template>
            <template
              v-if="propsData.aspect.sustainability.criticalRawMaterials"
            >
              <div
                v-for="(attr, key) in propsData.aspect.sustainability
                  .criticalRawMaterials"
                :key="key"
              >
                <Field
                  icon="mdi-image-size-select-small"
                  label="Critical raw materials"
                  :value="attr"
                />
              </div>
            </template>
          </v-col>
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template
              v-if="
                propsData.aspect.sustainability.carbonFootprint
                  .crossSectoralStandardsUsed
              "
            >
              <div
                v-for="(attr, key) in propsData.aspect.sustainability
                  .carbonFootprint.crossSectoralStandardsUsed"
                :key="key"
              >
                <Field
                  icon="mdi-image-size-select-small"
                  label="Cross sectoral standard"
                  :value="attr.crossSectoralStandard"
                />
              </div>
            </template>
            <template
              v-if="
                propsData.aspect.sustainability.carbonFootprint
                  .co2FootprintTotal
              "
            >
              <Field
                icon="mdi-image-size-select-small"
                label="Co2 footprint total"
                :value="
                  propsData.aspect.sustainability.carbonFootprint
                    .co2FootprintTotal
                "
              />
            </template>
            <template
              v-if="
                propsData.aspect.sustainability.carbonFootprint
                  .productOrSectorSpecificRules
              "
            >
              <div
                v-for="(attr, key) in propsData.aspect.sustainability
                  .carbonFootprint.productOrSectorSpecificRules"
                :key="key"
              >
                <Field
                  icon="mdi-image-size-select-small"
                  label="Operator"
                  :value="attr.operator"
                />
                <Field
                  icon="mdi-image-size-select-small"
                  label="Rule names"
                  :value="attr.ruleNames"
                />
                <Field
                  icon="mdi-image-size-select-small"
                  label="Other operator name"
                  :value="attr.otherOperatorName"
                />
              </div>
            </template>
          </v-col>
        </v-row>
      </template>
      <template v-else>
        <v-row class="section">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template v-if="propsData.aspect.sustainability.state">
              <Field
                icon="mdi-image-size-select-small"
                label="State"
                :value="propsData.aspect.sustainability.state"
              />
            </template>
            <template v-if="propsData.aspect.sustainability.material">
              <AttributeField
                icon="mdi-image-size-select-small"
                label="Material"
                :attributes-list="propsData.aspect.sustainability.material"
              />
            </template>
          </v-col>
          <template v-if="propsData.aspect.sustainability.carbonFootprint">
            <v-col sm="12" md="4" class="pa-0 ma-0">
              <template
                v-if="
                  propsData.aspect.sustainability.carbonFootprint
                    .carbonContentTotal
                "
              >
                <Field
                  icon="mdi-image-size-select-small"
                  label="Carbon content total"
                  :value="
                    propsData.aspect.sustainability.carbonFootprint
                      .carbonContentTotal
                  "
                />
              </template>
              <template
                v-if="
                  propsData.aspect.sustainability.carbonFootprint
                    .crossSectoralStandard
                "
              >
                <Field
                  icon="mdi-image-size-select-small"
                  label="Cross sectoral standard"
                  :value="
                    propsData.aspect.sustainability.carbonFootprint
                      .crossSectoralStandard
                  "
                />
              </template>
              <template
                v-if="
                  propsData.aspect.sustainability.carbonFootprint
                    .productOrSectorSpecificRules
                "
              >
                <AttributeField
                  icon="mdi-image-size-select-small"
                  label="Material"
                  :attributes-list="
                    propsData.aspect.sustainability.carbonFootprint
                      .productOrSectorSpecificRules
                  "
                />
              </template>
            </v-col>
          </template>
        </v-row>
      </template>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import AttributeField from "../AttributeField.vue";
import InstructionsField from "../InstructionsField.vue";

export default {
  name: "SustainabilityComponent",
  components: {
    AttributeField,
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
      propsData: this.$props.data,
    };
  },
};
</script>
