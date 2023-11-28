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
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData['PEF']">
            <template v-if="propsData['PEF'].carbon">
              <template v-for="attr in propsData['PEF'].carbon" :key="attr">
                <Field
                  :icon="callIconFinder('carbon')"
                  :label="attr.type"
                  :value="attr.value"
                  :unit="attr.unit"
                />
              </template>
            </template>
            <template v-if="propsData['PEF'].carbon">
              <template
                v-for="attr in propsData['PEF'].environmental"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('carbon')"
                  :label="attr.type"
                  :value="attr.value"
                  :unit="attr.unit"
                />
              </template>
            </template>
          </template>
          <template v-if="propsData.state">
            <Field
              :icon="callIconFinder('Sustainability')"
              label="State"
              :value="propsData.state"
            />
          </template>
        </v-col>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.material">
            <template v-for="attr in propsData.material.left" :key="attr">
              <Field
                :icon="callIconFinder('material')"
                :label="attr.name.type"
                :value="attr.name.name"
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
          <template v-if="propsData.critical">
            <template v-for="attr in propsData.critical.left" :key="attr">
              <Field
                :icon="callIconFinder('material')"
                label="Critical"
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
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "SustainabilityComponent",
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
      propsData: this.$props.data.aspect.sustainability,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
  },
};
</script>
