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
      if (!currentValue || !maxValue) return 0;
      if (this.isNumeric(!currentValue) || this.isNumeric(!maxValue)) return 0;

      const bar = (currentValue * 100) / maxValue;

      return bar + "%";
    },
    isNumeric(n) {
      return !isNaN(parseFloat(n)) && isFinite(n);
    },
  },
};
</script>
