<!--
  Tractus-X -  Digital Product Pass Application
  
  Copyright (c) 2022, 2024 BMW AG
  Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

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
              callCurrentData?.predecessor ||
                callCurrentData?.issueDate ||
                callCurrentData.version ||
                callCurrentData.lastModification
            )
          "
        >
          <template v-if="callCurrentData.predecessor">
            <Field
              :icon="callIconFinder('predecessor')"
              :label="$t('sections.metadata.predecessor')"
              :value="callCurrentData.predecessor"
            />
          </template>
          <template v-if="callCurrentData.lastModification">
            <Field
              :icon="callIconFinder('lastModification')"
              :label="$t('sections.metadata.lastModification')"
              :value="callCurrentData.lastModification"
            />
          </template>
          <template v-if="callCurrentData.issueDate">
            <Field
              :icon="callIconFinder('issueDate')"
              :label="$t('sections.metadata.issueDate')"
              :value="callCurrentData.issueDate"
            />
          </template>
          <template v-if="callCurrentData.version">
            <Field
              :icon="callIconFinder('version')"
              :label="$t('sections.metadata.version')"
              :value="callCurrentData.version"
            />
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              callCurrentData.backupReference ||
                callCurrentData.registrationIdentifier ||
                callCurrentData.economicOperatorId ||
                callCurrentData.passportIdentifier ||
                callCurrentData.economicOperator
            )
          "
        >
          <template v-if="callCurrentData.backupReference">
            <Field
              :icon="callIconFinder('backupReference')"
              :label="$t('sections.metadata.backupReference')"
              :value="callCurrentData.backupReference"
            />
          </template>
          <template v-if="callCurrentData.registrationIdentifier">
            <Field
              :icon="callIconFinder('registrationIdentifier')"
              :label="$t('sections.metadata.registrationIdentifier')"
              :value="callCurrentData.registrationIdentifier"
            />
          </template>
          <template v-if="callCurrentData.economicOperatorId">
            <Field
              :icon="callIconFinder('economicOperatorId')"
              :label="$t('sections.metadata.economicOperatorId')"
              :value="callCurrentData.economicOperatorId"
            />
          </template>
          <template v-if="callCurrentData.passportIdentifier">
            <Field
              :icon="callIconFinder('passportIdentifier')"
              :label="$t('sections.metadata.passportIdentifier')"
              :value="callCurrentData.passportIdentifier"
            />
          </template>
          <template v-if="callCurrentData.economicOperator">
            <Field
              :icon="callIconFinder('legitimization')"
              :label="$t('sections.metadata.legitimization')"
              :value="callCurrentData.economicOperator.legitimization"
            />
            <Field
              :icon="callIconFinder('identification')"
              :label="$t('sections.metadata.identification')"
              :value="callCurrentData.economicOperator.identification"
            />
          </template>
        </v-col>
        <v-col
          sm="12"
          md="4"
          class="pa-0 ma-0"
          v-if="
            callHasContent(
              callCurrentData?.status || callCurrentData?.expirationDate
            )
          "
        >
          <template v-if="callCurrentData.status">
            <Field
              :icon="callIconFinder('status')"
              :label="$t('sections.metadata.status')"
              :value="callCurrentData.status"
            />
          </template>
          <template v-if="callCurrentData.expirationDate">
            <Field
              :icon="callIconFinder('expirationDate')"
              :label="$t('sections.metadata.expirationDate')"
              :value="callCurrentData.expirationDate"
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
      propsData: this.$props.data.aspect?.metadata || {},
      tppData: this.$props.data.aspect?.generic?.metadata || {},
    };
  },
  computed: {
    callCurrentData() {
      return passportUtil.currentData(this.tppData, this.propsData);
    },
  },
  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    callHasContent(data) {
      return passportUtil.hasContent(data);
    },
  },
};
</script>
