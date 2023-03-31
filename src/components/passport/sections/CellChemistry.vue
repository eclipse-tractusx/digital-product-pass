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
    <!-- Composition of Electrolyte -->
    <AttributeField
      :attributes-list="propsData.electrolyteComposition"
      label="Composition of electrolyte"
      data-cy="electrolyte-composition"
    />
    <!-- Composition of Anode -->
    <AttributeField
      :attributes-list="propsData.anodeActiveMaterials"
      label="Composition of Anode"
    />
    <!-- Composition of Anode other -->
    <AttributeField
      :attributes-list="propsData.anodeCompositionOther"
      label="Composition of other Anode materials"
    />
    <!-- Composition of Cathode -->
    <AttributeField
      :attributes-list="propsData.cathodeActiveMaterials"
      label="Composition of Cathode"
    />
    <!-- Composition of Cathode other -->
    <AttributeField
      :attributes-list="propsData.cathodeCompositionOther"
      label="Composition of other Cathode materials"
    />
    <!-- Recyclate content active materials -->
    <AttributeField
      :attributes-list="propsData.recyclateContentActiveMaterials"
      label="Recyclate content active materials"
    />
  </div>
</template>

<script>
import AttributeField from "../AttributeField.vue";

export default {
  name: "CellChemistry",
  components: {
    AttributeField,
  },
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.passport.cellChemistry,
    };
  },
};
</script>
