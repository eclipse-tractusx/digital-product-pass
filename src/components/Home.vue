<template>
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <BatteryPassport class="battery-page-container" />
  </div>
</template>

<script type="text/jsx">
import BatteryPassport from './BatteryPassport.vue'
import Spinner from "@/components/Spinner.vue";
export default ({
  name: 'HomeComponent',
  components: {
    Spinner,
    BatteryPassport
  },
  data() {
    return {
      loading: true
    }
  },
  props: {
    provider: String,
    battery: String,
  },
  mounted() {
    let user = localStorage.getItem("user-info");
    if (!user) {
      this.$router.push({ name: 'Login' });
    }

  },
  created() {
    this.loading = false;

  }
})
</script>

<style scoped>
.battery-page-container {
  min-height: 100vh;
}
</style>
