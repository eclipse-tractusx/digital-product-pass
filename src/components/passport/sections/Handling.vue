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
        <template v-if="propsData.maintenanceHistory">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <DialogComponent class="field-dialog">
              <AttributeField
                icon="mdi-image-size-select-small"
                label="Maintenance history"
                :attributes-list="propsData.maintenanceHistory"
              >
                <template v-slot:text>
                  {{ propsData.maintenanceHistory.description }}
                </template>
              </AttributeField>
            </DialogComponent>
          </v-col>
        </template>
        <template v-if="propsData.spareParts">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <AttributeField
              icon="mdi-image-size-select-small"
              label="Spare parts"
              :attributes-list="propsData.spareParts"
            />
          </v-col>
        </template>
        <template v-if="propsData.substanceOfConcern">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <AttributeField
              icon="mdi-image-size-select-small"
              label="Substance of concern"
              :attributes-list="propsData.substanceOfConcern"
            />
          </v-col>
        </template>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import AttributeField from "../AttributeField.vue";
import DialogComponent from "../../general/Dialog.vue";

export default {
  name: "HandlingComponent",
  components: {
    AttributeField,
    DialogComponent,
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
};
</script>
