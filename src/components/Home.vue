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
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <BatteryPassport class="battery-page-container" />
  </div>
</template>

<script type="text/jsx">
import BatteryPassport from "./BatteryPassport.vue";
import Spinner from "@/components/Spinner.vue";
export default {
  name: "HomeComponent",
  components: {
    Spinner,
    BatteryPassport,
  },
  props: {
    provider: {
      type: String,
      default: "",
    },
    battery: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      loading: true,
    };
  },
  created() {
    this.loading = false;
  },
};
</script>

<style scoped>
.battery-page-container {
  min-height: 100vh;
}
</style>
