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
        <template
          v-if="
            propsData.localIdentifiers &&
            Array.isArray(propsData.localIdentifiers)
          "
        >
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template v-if="propsData.manufacturerId" :key="attr">
              <Field
                :icon="callIconFinder('additionalCode')"
                :value="propsData.manufacturerId"
                :label="$t('sections.identification.manufacturerId')"
              />
            </template>
            <template v-for="attr in propsData.localIdentifiers" :key="attr">
              <Field
                :icon="callIconFinder('additionalCode')"
                :value="attr.value"
                :label="attr.key"
              />
            </template>
            <template v-if="propsData.dataMatrixCode" :key="attr">
              <Field
                :icon="callIconFinder('additionalCode')"
                :value="propsData.dataMatrixCode"
                :label="$t('sections.identification.dataMatrixCode')"
              />
            </template>
          </v-col>
        </template>
        <template v-else>
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template v-if="propsData.localIdentifier">
              <Field
                :icon="callIconFinder('localIdentifiers')"
                :value="propsData.localIdentifier.value"
                :label="propsData.localIdentifier.key"
              />
            </template>
          </v-col>
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template v-if="propsData.additionalCode">
              <template v-for="attr in propsData.additionalCode" :key="attr">
                <Field
                  :icon="callIconFinder('additionalCode')"
                  :value="attr.value"
                  :label="attr.key"
                />
              </template>
            </template>
            <template v-if="propsData.dataCarrier">
              <Field
                :icon="callIconFinder('dataCarrier')"
                :value="propsData.dataCarrier.carrierType"
                :label="propsData.dataCarrier.carrierLayout"
              />
            </template>
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
</template>
<script>
import Field from "../Field.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "IdentificationComponent",
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
      propsData: this.$props.data.aspect.identification,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
  },
};
</script>
