<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<template v-if="propsData">
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col
          sm="12"
          md="12"
          class="pa-0 ma-0"
          style="display: flex; margin-bottom: 12px"
        >
          <AttributeField
            icon="mdi-newspaper-variant-outline"
            :attributes-list="propsData.compositionOfBattery"
            label="Main Battery Composites"
            style="background: #f9f9f9; min-width: 300px"
          />
          <Field
            icon="mdi-select-all"
            label="Critical raw materials"
            :value="propsData.criticalRawMaterials"
            style="min-width: 300px"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import AttributeField from "../AttributeField.vue";
import Field from "../Field.vue";

export default {
  name: "BatteryComposition",
  components: {
    AttributeField,
    Field,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.passport.composition,
    };
  },
};
</script>
