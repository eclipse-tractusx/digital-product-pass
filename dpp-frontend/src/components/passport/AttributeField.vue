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
  <div v-if="attributesList" class="field-container">
    <ul v-for="attribute in attributesList" :key="attribute">
      <template v-if="attribute.document">
        <a :href="attribute.document.content" target="_blank">
          <v-icon start md :icon="icon" class="link"> </v-icon>
        </a>
      </template>
      <template v-else><v-icon start md :icon="icon"> </v-icon></template>
      <span class="field-label">{{ label }}</span>
      <li>
        <span>
          {{ attribute.materialName }}
        </span>
        <span v-if="attribute.materialPercentageMassFraction">
          - {{ attribute.materialPercentageMassFraction }}%
        </span>
        <span v-if="attribute.materialWeight">
          - {{ attribute.materialWeight }}kg
        </span>
        <span v-if="attribute.value"> {{ attribute.value }} </span>
        <template v-if="attribute.document">
          <span v-if="attribute.document.header">
            {{ attribute.document.header }}
          </span>
        </template>
        <template v-if="attribute.name">
          <span>
            {{ attribute.name }}
          </span>
        </template>
        <template v-if="attribute.description">
          <span>
            {{ attribute.description }}
          </span>
        </template>
        <template v-if="attribute.unit">
          <span> {{ attribute.unit }} </span>
        </template>
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
