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
        <template v-if="callCurrentData.physicalDimension">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template
              v-if="
                callCurrentData.physicalDimension.width ||
                callCurrentData.physicalDimension.length ||
                callCurrentData.physicalDimension.height
              "
            >
              <Field
                icon="mdi-image-size-select-small"
                :label="
                  $t('sections.characteristics.physicalDimensionsProperty')
                "
                :width="callCurrentData.physicalDimension?.width?.value"
                :length="callCurrentData.physicalDimension?.length?.value"
                :height="callCurrentData.physicalDimension?.height?.value"
                :unit="callCurrentData.physicalDimension?.width?.unit"
              />
            </template>
            <template v-if="callCurrentData.physicalDimension.grossWeight">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.grossWeight')"
                :value="callCurrentData.physicalDimension.grossWeight.value"
                :unit="callCurrentData.physicalDimension.grossWeight.unit"
              />
            </template>
            <template v-if="callCurrentData.physicalDimension.weight">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.weight')"
                :value="callCurrentData.physicalDimension.weight.value"
                :unit="callCurrentData.physicalDimension.weight.unit"
              />
            </template>
            <template v-if="callCurrentData.physicalDimension.weightOrVolume">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.weightOrVolume')"
                :value="
                  callCurrentData.physicalDimension.weightOrVolume.left.value
                "
                :unit="
                  callCurrentData.physicalDimension.weightOrVolume.left.unit
                "
              />
            </template>
            <template v-if="callCurrentData.physicalDimension.grossVolume">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.grossVolume')"
                :value="callCurrentData.physicalDimension.grossVolume.value"
                :unit="callCurrentData.physicalDimension.grossVolume.unit"
              />
            </template>
            <template v-if="callCurrentData.physicalDimension.diameter">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.diameter')"
                :value="callCurrentData.physicalDimension.diameter.value"
                :unit="callCurrentData.physicalDimension.diameter.unit"
              />
            </template>
            <template v-if="callCurrentData.physicalDimension.volume">
              <Field
                icon="mdi-arrow-down-circle-outline"
                :label="$t('sections.characteristics.volume')"
                :value="callCurrentData.physicalDimension.volume.value"
                :unit="callCurrentData.physicalDimension.volume.unit"
              />
            </template>
          </v-col>
        </template>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              callCurrentData?.lifespan ||
                callCurrentData?.generalPerformanceClass ||
                callCurrentData.warranty ||
                callCurrentData.lifeTime
            )
          "
        >
          <template v-if="callCurrentData.lifespan">
            <Field
              v-for="attr in callCurrentData.lifespan"
              :key="attr"
              :icon="callIconFinder('lifespan')"
              :label="$t('sections.characteristics.lifespan')"
              :value="attr.value"
              :unit="attr.unit"
            />
          </template>
          <template v-if="callCurrentData.generalPerformanceClass">
            <Field
              :icon="callIconFinder('generalPerformanceClass')"
              :label="$t('sections.characteristics.generalPerformanceClass')"
              :value="callCurrentData.generalPerformanceClass"
            />
          </template>
          <template v-if="callCurrentData.warranty">
            <template v-if="callIsObject(callCurrentData.warranty)">
              <Field
                :icon="callIconFinder('warranty')"
                :label="$t('sections.characteristics.warranty')"
                :value="callCurrentData.warranty.lifeValue"
                :unit="callCurrentData.warranty.lifeUnit"
              />
            </template>
            <template v-else>
              <Field
                :icon="callIconFinder('warranty')"
                :label="$t('sections.characteristics.warranty')"
                :value="callCurrentData.warranty"
                unit="days"
              />
            </template>
          </template>
          <template v-if="callCurrentData.lifeTime">
            <Field
              :icon="callIconFinder('lifeTime')"
              :label="$t('sections.characteristics.lifeTime')"
              :value="callCurrentData.lifeTime"
            />
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="callHasContent(callCurrentData?.physicalState)"
        >
          <template v-if="callCurrentData?.physicalState">
            <Field
              :icon="callIconFinder('physicalState')"
              :label="$t('sections.characteristics.physicalState')"
              :value="callCurrentData.physicalState"
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
      propsData: this.$props.data.aspect?.characteristics || {},
      tppData:
        this.$props.data.aspect?.productUnspecificParameters?.characteristics ||
        {},
    };
  },
  computed: {
    callCurrentData() {
      return passportUtil.currentData(this.tppData, this.propsData);
    },
  },
  methods: {
    callIsObject(value) {
      return passportUtil.isObject(value);
    },
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    callHasContent(data) {
      return passportUtil.hasContent(data);
    },
    callIsString(value) {
      return passportUtil.isString(value);
    },
  },
};
</script>
