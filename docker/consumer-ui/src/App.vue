<template>
  <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
  <!-- <div id="nav">
      <router-link to="/">Home</router-link>
      <router-link to="/about">About</router-link>
  </div>
  <router-view /> -->
  <!-- <ProviderDropdown msg="Welcome to Your Vue.js App"/> -->
  <!-- <SignUp /> -->
  
  <router-view />

</template>

<script>
// import ProviderDropdown from './components/ProviderDropdown.vue'
// import Login from './components/Login.vue'


export default {
  name: "App",
  // components: {
  //   Login
  // }
};
</script>

<style>
#app {
  font-family: "Libre Franklin", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;

  color: #2c3e50;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

<!--<style>
body{
  margin: 0px;
  padding: 0px;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  /* margin-top: 60px; */
}
#nav{
  padding: 30px;
}

#nav a{
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active{
  color: #42b983;
}

/* .center {
  text-align: center;
} */

.margin-top {
  margin-top: 30px;
}

.register input {
  width: 300px !important;
}

.center {
  margin-left: 33%;
  margin-top: inherit;
}

.btn-signup {
  width: -webkit-fill-available;
}

.logo {
  width: 150px;
}

.btn-login{
    width: -webkit-fill-available;
}

@import './assets/styles/style.css';
</style>!-->
