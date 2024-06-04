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
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              callCurrentData.importer || callCurrentData.intoServiceDate
            )
          "
        >
          <template v-if="callCurrentData.importer">
            <Field
              :icon="callIconFinder('importer')"
              :label="$t('sections.operation.importer')"
              :value="callCurrentData.importer.left.id"
            />
            <Field
              :icon="callIconFinder('importer')"
              :label="$t('sections.operation.importerEori')"
              :value="callCurrentData.importer.left.eori"
            />
          </template>
          <template v-if="callCurrentData.intoServiceDate">
            <Field
              :icon="callIconFinder('intoServiceDate')"
              :label="$t('sections.operation.intoServiceDate')"
              :value="callCurrentData.intoServiceDate"
            />
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              callCurrentData.import || callCurrentData.intoServiceDate
            )
          "
        >
          <template v-if="callCurrentData.import?.content">
            <Field
              :icon="callIconFinder('importer')"
              :label="$t('sections.operation.importerID')"
              :value="callCurrentData.import.content.id"
            />
            <Field
              :icon="callIconFinder('importer')"
              :label="$t('sections.operation.importerEori')"
              :value="callCurrentData.import.content.eori"
            />
          </template>
          <template v-if="callCurrentData.intoServiceDate">
            <Field
              :icon="callIconFinder('intoServiceDate')"
              :label="$t('sections.operation.intoServiceDate')"
              :value="callCurrentData.intoServiceDate"
            />
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="callHasContent(callCurrentData.manufacturer)"
        >
          <template v-if="callCurrentData.manufacturer">
            <Field
              :icon="callIconFinder('manufacturer')"
              :label="$t('sections.operation.manufacturerId')"
              :value="callCurrentData.manufacturer?.manufacturer"
            />
            <template
              v-if="
                callCurrentData.manufacturer.facility &&
                typeof callCurrentData.manufacturer.facility === 'object'
              "
            >
              <template
                v-for="attr in callCurrentData.manufacturer.facility"
                :key="attr"
              >
                <Field
                  :icon="callIconFinder('facility')"
                  :label="$t('sections.operation.facilityId')"
                  :value="attr.facility"
                />
              </template>
            </template>
            <template v-else>
              <Field
                :icon="callIconFinder('facility')"
                :label="$t('sections.operation.facilityId')"
                :value="callCurrentData.manufacturer.facility"
              />
            </template>
            <Field
              :icon="callIconFinder('manufacturingDate')"
              :label="$t('sections.operation.manufacturingDate')"
              :value="
                callProcessDateTime(
                  callCurrentData.manufacturer?.manufacturingDate
                )
              "
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
      propsData: this.$props.data.aspect.operation || {},
      tppData:
        this.$props.data.aspect.productUnspecificParameters?.operation || {},
    };
  },
  computed: {
    callCurrentData() {
      return passportUtil.currentData(this.tppData, this.propsData);
    },
  },
  methods: {
    callHasContent(...args) {
      return passportUtil.hasContent(...args);
    },
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    callProcessDateTime(dateTimeString) {
      return passportUtil.processDateTime(dateTimeString);
    },
  },
};
</script>
