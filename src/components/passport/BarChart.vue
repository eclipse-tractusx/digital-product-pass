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
  <div class="charging-cycles">
    <div class="bar-chart" />
    <div
      class="bar-chart"
      :style="`width: ${barChart(currentValue, maxValue)}; background: #0f71cb`"
    />
    <div class="chart-value current">
      {{ isNumeric(currentValue) ? currentValue : "No data" }}
    </div>
    <div class="chart-value max">
      {{ isNumeric(maxValue) ? maxValue : "No data" }}
    </div>
  </div>
</template>


<script>
export default {
  name: "BarChart",

  props: {
    currentValue: { type: [String, Number], default: "" },
    maxValue: { type: [String, Number], default: "" },
  },

  methods: {
    barChart(currentValue, maxValue) {
      const bar = (currentValue * 100) / maxValue;
      try {
        if (currentValue > maxValue) return 100 + "%";
        if (bar < 3) return 3 + "%";
        return bar + "%";
      } catch (e) {
        if (!currentValue || !maxValue) return 0;
      }
    },
    isNumeric(n) {
      return !isNaN(parseFloat(n)) && isFinite(n);
    },
  },
};
</script>
