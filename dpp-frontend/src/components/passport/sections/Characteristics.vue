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
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <template v-if="propsData.physicalDimension">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template
              v-if="
                propsData.physicalDimension.width ||
                propsData.physicalDimension.length ||
                propsData.physicalDimension.height
              "
            >
              <Field
                icon="mdi-image-size-select-small"
                :label="
                  $t('sections.characteristics.physicalDimensionsProperty')
                "
                :width="propsData.physicalDimension.width.value"
                :length="propsData.physicalDimension.length.value"
                :height="propsData.physicalDimension.height.value"
                :unit="propsData.physicalDimension.width.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.grossWeight">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.grossWeight')"
                :value="propsData.physicalDimension.grossWeight.value"
                :unit="propsData.physicalDimension.grossWeight.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.weight">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.weight')"
                :value="propsData.physicalDimension.weight"
                :unit="propsData.physicalDimension.weight.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.weightOrVolume">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.weightOrVolume')"
                :value="propsData.physicalDimension.weightOrVolume.left.value"
                :unit="propsData.physicalDimension.weightOrVolume.left.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.grossVolume">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.grossVolume')"
                :value="propsData.physicalDimension.grossVolume.value"
                :unit="propsData.physicalDimension.grossVolume.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.diameter">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.diameter')"
                :value="propsData.physicalDimension.diameter.value"
                :unit="propsData.physicalDimension.diameter.unit"
              />
            </template>
          </v-col>
        </template>
        <template v-if="propsData.lifespan">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <Field
              v-for="attr in propsData.lifespan"
              :key="attr"
              :icon="callIconFinder('lifespan')"
              :label="$t('sections.characteristics.lifespan')"
              :value="attr.value"
              :unit="attr.unit"
            />
          </v-col>
        </template>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.physicalState">
            <Field
              :icon="callIconFinder('physicalState')"
              :label="$t('sections.characteristics.physicalState')"
              :value="propsData.physicalState"
            />
          </template>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "CharacteristicsComponent",
  components: {
    Field,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      propsData: this.$props.data.aspect.characteristics,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
  },
};
</script>
