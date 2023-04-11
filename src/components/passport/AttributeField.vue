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
  <div v-if="attributesList" class="sub-section-container">
    <div class="sub-title-container">
      <span class="sub-title">{{ label }}</span>
    </div>
    <div class="list-container">
      <ul>
        <span class="list-label"></span>
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
  </div>
</template>

<script>
export default {
  name: "AttributeField",
  props: {
    attributesList: { type: Array, default: () => [] },
    label: { type: [String, Number], default: "" },
  },
};
</script>

<style scoped>
ul {
  display: flex;
  flex-direction: column;
  padding: 0;
}

li {
  margin-left: 20px;
  font-weight: bold;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 22px 40px 0 40px;
  width: 100%;
}

.list-container {
  width: 33%;
  padding: 0 0 22px 40px;
}

.list-label {
  padding: 22px 0 10px 0;
  font-size: 12px;
  color: #777777;
}

@media (max-width: 750px) {
  .list-container {
    width: 100%;
    padding-left: 50px;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
