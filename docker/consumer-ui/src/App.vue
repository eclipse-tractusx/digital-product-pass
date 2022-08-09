<template>
  <router-view />
</template>

<script>
// import ProviderDropdown from './components/ProviderDropdown.vue'
// import Login from './components/Login.vue'

export default {
  name: "App",
};
</script>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
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
.table-borderless > :not(caption) > * > * {
  border-bottom-width: 0;
  padding-left: 40px !important;
}
.table-striped tbody tr:nth-of-type(odd) {
  background-color: rgba(179, 203, 44, 0.15);
}
.table-striped tbody tr:nth-of-type(even) {
  background-color: rgba(179, 203, 44, 0.05);
}

.table > thead {
  vertical-align: bottom;
  color: gray;
  font-size: 12px;
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
