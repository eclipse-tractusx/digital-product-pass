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

<template>
  <div v-if="attributesList" class="field-container">
    <v-icon start md :icon="icon"> </v-icon>
    <ul>
      <span class="field-label">{{ label }}</span>
      <li v-for="attribute in attributesList" :key="attribute">
        <span>
          {{ attribute.materialName }}
        </span>
        <span v-if="attribute.materialPercentageMassFraction">
          - {{ attribute.materialPercentageMassFraction }}%
        </span>
        <span v-if="attribute.materialWeight">
          - {{ attribute.materialWeight }}kg
        </span>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "AttributeField",
  props: {
    attributesList: { type: Array, default: () => [] },
    label: { type: [String, Number], default: "" },
    icon: { type: [String, String], default: "" },
  },
};
</script>
