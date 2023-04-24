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

<template v-if="propsData">
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col
          sm="12"
          md="3"
          class="pa-0 ma-0"
          style="padding-right: 24px !important"
        >
          <v-row class="section">
            <v-col sm="12" md="12" class="pa-0 ma-0">
              <DocumentField :field="propsData.responsibleSourcing" />
            </v-col>
          </v-row>
          <v-row class="section">
            <v-col sm="12" md="12" class="pa-0 ma-0">
              <DocumentField :field="propsData.packagingInstructions" />
            </v-col>
          </v-row>

          <v-row class="section">
            <v-col sm="12" md="12" class="pa-0 ma-0">
              <DocumentField :field="propsData.declarationOfConformity" />
            </v-col>
          </v-row>
        </v-col>
        <v-col sm="12" md="9" class="pa-0 ma-0">
          <v-row class="section">
            <v-col sm="12" md="6" class="pa-0 ma-0">
              <DocumentField :field="propsData.transportationInstructions" />
            </v-col>
            <v-col sm="12" md="6" class="pa-0 ma-0">
              <DocumentField :field="propsData.vehicleDismantlingProcedure" />
            </v-col>
          </v-row>
          <v-row class="section">
            <v-col sm="12" md="6" class="pa-0 ma-0">
              <DocumentField :field="propsData.vehicleDismantlingProcedure" />
            </v-col>
            <v-col sm="12" md="6" class="pa-0 ma-0">
              <DocumentField :field="propsData.testReportsResults" />
            </v-col>
          </v-row>
          <v-row class="section">
            <v-col sm="12" md="3" class="pa-0 ma-0">
              <DocumentField :field="propsData.batteryDismantlingProcedure" />
            </v-col>
            <v-col sm="12" md="9" class="pa-0 ma-0">
              <DocumentField :field="propsData.safetyMeasures" />
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>
 
<script>
import DocumentField from "../DocumentField.vue";

export default {
  name: "AdditionalDocuments",
  components: {
    DocumentField,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    data: {
      type: Object,
      required: false,
      default: Object,
    },
  },

  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.passport.document,
    };
  },
};
</script>
