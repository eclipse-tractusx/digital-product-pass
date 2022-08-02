<template>
  <BatteryPassport/>
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
    let user = localStorage.getItem("user-info");
    if (!user) {
      this.$router.push({name: 'Login'});
    }
  }
})
</script>

<style scoped>
</style>
