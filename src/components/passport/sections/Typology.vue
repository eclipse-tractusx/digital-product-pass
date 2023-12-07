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
          <DialogComponent class="field-dialog">
            <Field
              info
              icon="mdi-image-size-select-small"
              :label="$t('sections.typology.name')"
              :value="propsData.name"
            />
            <template v-slot:text>
              {{ propsData.description }}
            </template>
          </DialogComponent>
        </v-col>
        <template v-if="propsData.class">
          <v-col sm="12" md="4" class="pa-0 ma-0">
            <Field
              icon="mdi-lightning-bolt-outline"
              :label="$t('sections.typology.definition')"
              :value="propsData.class.definition"
            />
            <Field
              icon="mdi-arrow-bottom-right-thin-circle-outline"
              :label="$t('sections.typology.code')"
              :value="propsData.class.code"
            />
          </v-col>
        </template>
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <Field
            icon="mdi-lightning-bolt-outline"
            :label="$t('sections.typology.nameAtManufacturer')"
            :value="propsData.nameAtManufacturer"
          />
          <Field
            icon="mdi-arrow-bottom-right-thin-circle-outline"
            :label="$t('sections.typology.nameAtCustomer')"
            :value="propsData.nameAtCustomer"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import DialogComponent from "../../general/Dialog.vue";

export default {
  name: "TypologyComponent",
  components: {
    DialogComponent,
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
      propsData: this.$props.data.aspect["typology"],
    };
  },
};
</script>
