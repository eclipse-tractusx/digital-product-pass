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
      <v-row class="section">
        <template v-if="propsData.physicalDimension">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <div class="element-chart-label">Physical dimension</div>
            <template v-if="propsData.physicalDimension.grossWeight">
              <Field
                :icon="callIconFinder('grossWeight')"
                label="Gross weight"
                :value="propsData.physicalDimension.grossWeight.value"
                :unit="propsData.physicalDimension.grossWeight.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.weightOrVolume.left">
              <Field
                :icon="callIconFinder('weightOrVolume')"
                label="Weight or volume"
                :value="propsData.physicalDimension.weightOrVolume.left.value"
                :unit="propsData.physicalDimension.weightOrVolume.left.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.diameter">
              <Field
                :icon="callIconFinder('diameter')"
                label="Diameter"
                :value="propsData.physicalDimension.diameter.value"
                :unit="propsData.physicalDimension.diameter.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.grossVolume">
              <Field
                :icon="callIconFinder('grossVolume')"
                label="Gross volume"
                :value="propsData.physicalDimension.grossVolume.value"
                :unit="propsData.physicalDimension.grossVolume.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.width">
              <Field
                :icon="callIconFinder('width')"
                label="Width"
                :value="propsData.physicalDimension.width.value"
                :unit="propsData.physicalDimension.width.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.length">
              <Field
                :icon="callIconFinder('length')"
                label="Length"
                :value="propsData.physicalDimension.length.value"
                :unit="propsData.physicalDimension.length.unit"
              />
            </template>
            <template v-if="propsData.physicalDimension.height">
              <Field
                :icon="callIconFinder('height')"
                label="Height"
                :value="propsData.physicalDimension.height.value"
                :unit="propsData.physicalDimension.height.unit"
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
              label="Lifespan"
              :value="attr.value"
              :unit="attr.unit"
            />
          </v-col>
        </template>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.physicalState">
            <Field
              :icon="callIconFinder('physicalState')"
              label="Physical state"
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
