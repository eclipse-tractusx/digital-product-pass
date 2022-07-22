<template>
<div class="bg">
  <img style="margin-left: 45%" src="../assets/catenaX-logo.png" />
  <br />
  <div class="margin-top">
    <p class="h1" style="margin-left: 49%; font-weight: bolder; font-size: x-large;">Login</p>
  </div>
  <div class="margin-top">
    <div class="container">
      <div class="col-md-4 center">
        <input
          class="form-control textbox"
          v-model="email"
          type="text"
          placeholder="Enter email"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          class="form-control textbox"
          v-model="password"
          type="password"
          placeholder="Enter Password"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <button class="btn btn-success btn-login" v-on:click="login">
          Login
        </button>
      </div>
    </div>
    <div class="col-md-4 center margin-top" style="margin-left: 45%">
      <p>
        <span> <router-link to="/sign-up">Sign Up</router-link></span>
        <span> | </span>
        <span> <router-link to="#">Forgot Password</router-link></span>
        <!-- <span> | </span> -->
        <!-- <span> <router-link to="/api/scanpassport">Scan QR Code</router-link></span> -->
      </p>
    </div>
  </div>
</div>
</template>

<script type="text/jsx">
import axios from "axios";
export default {
  name: "LoginPage",
  data() {
    return {
      email: "",
      password: "",
    };
  },
  methods: {
    async login() {
      let result = await axios.get(
        `https://mock--server.herokuapp.com/users?email=${this.email}&password=${this.password}`
      );
      if (result.status == 200 && result.data.length > 0) {
        //alert("login successful..!")
        localStorage.setItem("user-info", JSON.stringify(result.data[0]));
        if (localStorage.getItem("QRCode-info")){
          const isAccessUsingQRCode = localStorage.getItem("QRCode-info")
            
            let query = { "provider": JSON.parse(isAccessUsingQRCode).provider, "battery": JSON.parse(isAccessUsingQRCode).battery}
            this.$router.push({ name: "Home", query: query }); 
        }
        else
           this.$router.push({ name: "Home" });
      }
      else {
          alert("user is not registered or invalid credentails..!")
      }
    },
  },
  mounted() {
    let user = localStorage.getItem("user-info");
    if (user) {
      this.$router.push({ name: "Home" });
    }
  },
};
</script>

<style scoped>

.bg{
  background: white;
  margin-top:8%;
}
.btn-login{
  width: 35%;
  padding: 6px 6px 6px 6px;
  background: #b3cb2d;
  color: white;
  font-weight: bolder;
}

.center {
  margin-left: 40%;
  margin-top: inherit;
}

.margin-top {
  margin-top: 30px;
}

.textbox{
  width: 35%;
  margin-top:10px;
  padding: 6px 6px 6px 6px;
}
</style>