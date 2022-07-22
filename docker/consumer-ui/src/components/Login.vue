<template>
  <div class="sign-in-page-container">
    <img :src="LogoBG" alt="logo" class="bg-logo" />
    <div class="logo-container">
      <img :src="CatenaLogo" alt="logo" class="logo" />
    </div>
    <div class="sign-in-wrapper">
      <div class="sign-in-container">
        <h1>Sign In</h1>
        <h3>
          New user?<span
            ><router-link to="/sign-up" class="sign-up"
              >Sign Up</router-link
            ></span
          >
        </h3>

        <div class="col-md-4">
          <input
            class="form-control input"
            v-model="email"
            type="text"
            placeholder="Username or email"
          />
        </div>

        <div class="col-md-4">
          <input
            class="form-control input"
            v-model="password"
            type="password"
            placeholder="Password"
          />
        </div>

        <div class="col-md-4">
          <button class="btn btn-success btn-login" v-on:click="login">
            Sign In
          </button>
        </div>
        <div class="col-md-4">
          <span>
            <router-link to="#" class="public-data"
              >See public data</router-link
            ></span
          >

          <!-- <span>
              <router-link to="/api/scanpassport"
                >Scan QR Code</router-link
              ></span
            > -->
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/jsx">
import axios from "axios";
import CatenaLogo from "../assets/logotype.png";
import LogoBG from "../assets/logo.png";



export default {
  name: "LoginPage",
  data() {
    return {
      email: "",
      password: "",
    };
  },
  components: {
CatenaLogo,
LogoBG
  },
  setup() {
    return {
      CatenaLogo, LogoBG

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
.sign-in-page-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}
.sign-in-wrapper {
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
}
.sign-in-container {
  width: 280px;
  justify-content: center;
  align-items: center;
}
.btn-login {
  width: 280px;
  height: 48px;
  margin: 30px 0 30px 0;
  background: #b3cb2d;
  color: white;
  font-weight: bolder;
  font-size: 16px;
  border: solid 1px #b3cb2d;
  border-radius: 4px;
  cursor: pointer;
}
.bg-logo {
  width: 46%;
  z-index: 0;
  position: absolute;
  right: -4%;
  bottom: -4%;
  opacity: 0.2;
}
.sign-up {
  color: #ffa600;
  margin-left: 10px;
  text-decoration: none;
}
h3 {
  margin: 20px 0 20px 0px;
}
.public-data {
  margin: 30px 0 0 78px;
  text-decoration: none;
  color: #7a7a7a;
  font-weight: bold;
}

.input {
  width: 280px;
  height: 48px;
  margin-top: 10px;
  padding: 12px;
  border: solid 1px #b3cb2d;
  border-radius: 4px;
  font-weight: bold;
  font-size: 16px;
  color: #545d64;
}
::placeholder {
  color: #cccccc;
  font-size: 16px;
}
.logo-container {
  margin: 48px;
  display: block;
}
.logo {
  display: block;
  width: 209px;
  height: 49px;
  margin: 0 0 7% 64px;
}
</style>
