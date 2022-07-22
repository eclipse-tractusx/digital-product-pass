<template>
<Navigation />
<h2 style="margin: 2% 0% 0% 42%; font-weight: bolder; font-size: x-large;">Battery Passport Portal</h2>
<br />
<BatteryPassport />
</template>

<script type="text/jsx">
import Navigation from './Navigation.vue'
import BatteryPassport from './BatteryPassport.vue'
export default ({
  name: 'Home',
  components:{
    Navigation,
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

