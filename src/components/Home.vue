<template>
  <BatteryPassport class="battery-page-container" />
</template>

<script type="text/jsx">

import BatteryPassport from './BatteryPassport.vue'
export default ({
  name: 'Home',
  components:{

    BatteryPassport
  },
  props: {
    provider: String,
    battery: String,
  },
  mounted(){
        let user = localStorage.getItem("user-info");
        if (!user){
            this.$router.push({name:'Login'});
        }
    }
})
</script>
<style scoped>
.battery-page-container {
  min-height: 100vh;
}
</style>
