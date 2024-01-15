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
        <template v-if="propsData.importer">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <Field
              :icon="callIconFinder('importer')"
              label="Importer Id"
              :value="propsData.importer.left.id"
            />
          </v-col>
        </template>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <template v-if="propsData.manufacturer">
            <Field
              :icon="callIconFinder('manufacturer')"
              label="Manufacturer Id"
              :value="propsData.manufacturer.manufacturer"
            />
            <Field
              :icon="callIconFinder('facility')"
              label="Facility Id"
              :value="propsData.manufacturer.facility"
            />
            <Field
              :icon="callIconFinder('manufacturingDate')"
              label="Manufacturing date"
              :value="propsData.manufacturer.manufacturingDate"
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
  name: "OperationComponent",
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
      propsData: this.$props.data.aspect.operation,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
  },
};
</script>
