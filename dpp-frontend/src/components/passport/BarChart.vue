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
  <div class="charging-cycles">
    <div class="bar-chart" />
    <div
      class="bar-chart"
      :style="`width: ${barChart(currentValue, maxValue)}; background: #0f71cb`"
    />
    <div class="chart-value current">
      {{ isNumeric(currentValue) ? currentValue : $t("barChart.noData") }}
    </div>
    <div class="chart-value max">
      {{ isNumeric(maxValue) ? maxValue : $t("barChart.noData") }}
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
