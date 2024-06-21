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
        <template v-if="callCurrentData.content">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <template v-if="callCurrentData.content.producer">
              <template
                v-for="attr in callCurrentData.content.producer"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('producer')"
                  :value="attr.id"
                  :label="$t('sections.handling.producerId')"
                />
              </template>
              <template v-if="callCurrentData.content.sparePart">
                <template
                  v-for="attr in callCurrentData.content.sparePart"
                  :key="attr"
                >
                  <Field
                    :icon="callIconFinder('part')"
                    :value="attr.manufacturerPartId"
                    :label="$t('sections.handling.manufacturerPartId')"
                  />
                  <Field
                    :icon="callIconFinder('part')"
                    :value="attr.nameAtManufacturer"
                    :label="$t('sections.handling.nameAtManufacturer')"
                  />
                </template>
              </template>
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
  name: "HandlingComponent",
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
      propsData: this.$props.data.aspect.handling || {},
      tppData: this.$props.data.aspect?.generic?.handling || {},
    };
  },
  computed: {
    callCurrentData() {
      return passportUtil.currentData(this.tppData, this.propsData);
    },
  },
  methods: {
    callIconFinder(icon) {
      return passportUtil.iconFinder(icon);
    },
    callUnitRemover(unit) {
      return passportUtil.unitRemover(unit);
    },
  },
};
</script>
