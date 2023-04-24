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
