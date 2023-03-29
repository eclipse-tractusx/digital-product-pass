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
  <v-col cols="7" class="materials-container">
    <v-row class="yeah">
      <div
        class="material-container"
        v-for="(material, index) in data"
        :key="index"
      >
        <span class="material-label">{{ material.materialName }}</span>
        <span class="material-value">
          {{ material.materialPercentageMassFraction }}%
        </span>
      </div>
    </v-row>
  </v-col>
</template>


<script>
export default {
  name: "ElementChart",
  props: {
    data: {
      type: Object,
      default: Object,
    },
  },
};
</script>

<style scoped>
.materials-container {
  width: 400px;
}
.material-container {
  position: relative;
  width: 114px;
  height: 114px;
  background-color: #eaf1fe;
  border-radius: 10px;
  padding-top: 20px;
  margin: 7px;
}
.material-label {
  position: absolute;
  top: 5px;
  left: 5px;
  font-size: 14px;
  font-weight: 500;
}
.material-value {
  position: absolute;
  font-size: 24px;
  font-weight: 600;
  top: 43px;
  left: 50%;
  transform: translate(-50%, 0);
}
</style>

<style scoped>
</style>
