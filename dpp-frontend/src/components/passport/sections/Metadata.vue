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
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <Field
            :icon="callIconFinder('predecessor')"
            :label="$t('sections.metadata.predecessor')"
            :value="propsData.predecessor"
          />
          <Field
            :icon="callIconFinder('issueDate')"
            :label="$t('sections.metadata.issueDate')"
            :value="propsData.issueDate"
          />
          <Field
            :icon="callIconFinder('version')"
            :label="$t('sections.metadata.version')"
            :value="propsData.version"
          />
        </v-col>
        <template v-if="propsData.economicOperator">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <Field
              :icon="callIconFinder('legitimization')"
              :label="$t('sections.metadata.legitimization')"
              :value="propsData.economicOperator.legitimization"
            />
            <Field
              :icon="callIconFinder('identification')"
              :label="$t('sections.metadata.identification')"
              :value="propsData.economicOperator.identification"
            />
          </v-col>
        </template>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <Field
            :icon="callIconFinder('status')"
            :label="$t('sections.metadata.status')"
            :value="propsData.status"
          />
          <Field
            :icon="callIconFinder('expirationDate')"
            :label="$t('sections.metadata.expirationDate')"
            :value="propsData.expirationDate"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "MetadataComponent",
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
      propsData: this.$props.data.aspect.metadata,
    };
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
  },
};
</script>
