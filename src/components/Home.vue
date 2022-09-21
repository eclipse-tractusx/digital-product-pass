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
