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
  <div class="id-container">
    <v-btn
      rounded="pill"
      color="#0F71CB"
      size="large"
      class="back-btn"
      @click="$router.go(-1)"
      variant="outlined"
      style="border: 2px solid; text-transform: initial"
    >
      <v-icon class="icon" start md icon="mdi-arrow-left"></v-icon>
      Back
    </v-btn>

    <div class="id-wrapper">
      <p class="id">
        {{ type }}:
        {{ id ? id : "—" }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "PassportHeader",
  props: {
    data: {
      type: String,
      default: null,
    },
    type: {
      type: String,
      default: "BatteryID",
    },
  },
  data() {
    return {
      id: this.$props.data.batteryIdentification
        ? this.$props.data.batteryIdentification.batteryIDDMCCode
        : "-",
    };
  },
};
</script>

<style>
.id-wrapper {
  display: flex;
  justify-content: center;
  width: 100%;
  padding-right: 115px;
  line-break: anywhere;
}

.id {
  font-size: 14px;
  line-height: 36px;
  font-weight: bold;
}
.id-container {
  display: flex;
  align-items: center;
  width: 100%;
  margin: 6em 40px 0 40px;
  padding: 15px 0 10px 0;
}
@media (max-width: 750px) {
  .id-container {
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
  }
  .id-wrapper {
    margin-top: 15px;
  }
}
</style>
