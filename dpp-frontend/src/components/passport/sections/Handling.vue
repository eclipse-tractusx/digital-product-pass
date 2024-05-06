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
        <template v-if="propsData.spareParts">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <!-- eslint-disable-next-line vue/no-v-for-template-key -->
            <template
              v-for="attr in propsData.spareParts.left.producer"
              :key="attr"
            >
              <Field
                :icon="callIconFinder('producer')"
                :value="attr.id"
                :label="$t('sections.handling.producerId')"
              />
            </template>
            <template
              v-for="attr in propsData.spareParts.left.part"
              :key="attr"
            >
              <Field
                :icon="callIconFinder('part')"
                :value="attr.name"
                :label="$t('sections.handling.partName')"
              />
              <Field
                :icon="callIconFinder('part')"
                :value="attr.gtin"
                :label="$t('sections.handling.partGtin')"
              />
            </template>
          </v-col>
        </template>
        <template v-if="propsData.substanceOfConcern">
          <!-- eslint-disable-next-line vue/no-v-for-template-key -->
          <template
            v-for="attr in propsData.substanceOfConcern.left"
            :key="attr"
          >
            <v-col sm="12" md="4" class="pa-0 ma-0">
              <Field
                :icon="callIconFinder('substanceOfConcern')"
                :value="attr.name.name"
                :label="$t('sections.handling.substanceOfConcern')"
              />
              <Field
                :icon="callIconFinder('type')"
                :value="attr.name.type"
                :label="$t('sections.handling.type')"
              />
              <Field
                :icon="callIconFinder('location')"
                :value="attr.location"
                :label="$t('sections.handling.location')"
              />
              <Field
                :icon="callIconFinder('unit')"
                :value="callUnitRemover(attr.unit)"
                :label="$t('sections.handling.unit')"
              />
            </v-col>
            <v-col sm="12" md="4" class="pa-0 ma-0">
              <!-- eslint-disable-next-line vue/no-v-for-template-key -->
              <template v-for="attr in attr.concentration.left" :key="attr">
                <Field
                  :icon="callIconFinder('unit')"
                  :value="attr.max"
                  :label="$t('sections.handling.concentrationMax')"
                />
                <Field
                  :icon="callIconFinder('unit')"
                  :value="attr.min"
                  :label="$t('sections.handling.concentrationMin')"
                />
              </template>
              <Field
                :icon="callIconFinder('exemption')"
                :value="attr.exemption"
                :label="$t('sections.handling.exemption')"
              />
              <!-- eslint-disable-next-line vue/no-v-for-template-key -->
              <template v-for="attr in attr.id" :key="attr">
                <Field
                  :icon="callIconFinder('type')"
                  :value="attr.type"
                  :label="$t('sections.handling.idType')"
                />
                <Field
                  :icon="callIconFinder('type')"
                  :value="attr.id"
                  :label="$t('sections.handling.id')"
                />
              </template>
            </v-col>
          </template>
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
      propsData: this.$props.data.aspect.handling,
    };
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
