<template>
  <BatteryPassport class="battery-page-container" />
</template>

<script type="text/jsx">
import BatteryPassport from './BatteryPassport.vue'
export default ({
  name: 'HomeComponent',
  components: {
    BatteryPassport
  },
  props: {
    provider: String,
    battery: String,
  },
  mounted() {
  }
})
</script>

<style scoped>
.battery-page-container {
  min-height: 100vh;
}
</style>
