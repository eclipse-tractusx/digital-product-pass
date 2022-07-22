<template>
<div class="bg">
  <img style="margin-left: 45%" src="../assets/catenaX-logo.png" class="logo" />
  <br />
  <div class="margin-top">
    <p class="h1" style="margin-left: 49%; font-weight: bolder; font-size: x-large;">Sign Up</p>
  </div>
  <div class="margin-top">
    <div class="container">
      <div class="col-md-4 center">
        <select class="form-select textbox" v-model="role">
          <option disabled selected value="">Select Role..</option>
          <option value="oem">OEM</option>
          <option value="rawMaterialSupplier">Raw Material Supplier</option>
          <option value="recycler">Recycler</option>
          <option value="manufacturer">Manufacturer</option>
          <option value="producer">Producer</option>
           <option value="dismentler">Dismentler</option>
        </select>
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          class="form-control textbox"
          v-model="name"
          type="text"
          placeholder="Enter Name"
        />
      </div>
      <br />
      <div class="col-md-4 center">
        <input
          class="form-control textbox"
          v-model="email"
          type="text"
          placeholder="Enter Email"
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
        <button class="btn btn-success btn-signup" v-on:click="signUp">
          Sign Up
        </button>
      </div>
      <br />
      <div class="col-md-4 center">
        <p>
          <span> <router-link to="/login" style="margin-left:13%">Already a User?</router-link></span>
        </p>
      </div>
    </div>
  </div>
</div>
</template>

<script type="text/jsx">
import axios from "axios";
export default {
  name: "SignUp",
  data() {
    return {
      name: "",
      email: "",
      role: "",
      password: "",
    };
  },
  methods: {
    async signUp() {
      let result = await axios.post("http://localhost:3000/users", {
        email: this.email,
        password: this.password,
        name: this.name,
      });
      console.warn(result);
      if (result.status == 201) {
        //alert("sign-up successful..!")
        localStorage.setItem("user-info", JSON.stringify(result.data));
        this.$router.push({ name: "Home" });
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
  margin-top:5%;
}
.btn-signup {
  width: 35%;
  padding: 6px 6px 6px 6px;
  background: #b3cb2d;
  color: white;
  font-weight: bolder;
}
.register input {
  width: 300px !important;
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