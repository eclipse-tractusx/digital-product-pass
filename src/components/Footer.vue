<template>
  <div class="footer-container">
    <div class="footer-wrapper">
      <div class="logo-wrapper">
        <img :src="CatenaLogo" alt="logo" class="logo" />
      </div>
      <div class="links-wrapper">
        <h3 class="buttons">Contact</h3>
        <h3 class="buttons">Legal Notice</h3>
        <h3 class="buttons">Privacy Policy</h3>
      </div>
    </div>
  </div>
</template>

<script>
import CatenaLogo from "../assets/logo.png";

export default {
  name: "Footer",

  components: {
    CatenaLogo,
  },
  setup() {
    return {
      CatenaLogo,
    };
  },
};
</script>

<style scoped>
.footer-container {
  background-color: #e7e7e7;
  height: 165px;
  padding-top: 30px;
}
.footer-wrapper {
  display: flex;
  width: 76%;
  margin: 0 12% 0 12%;
}
.logo-wrapper {
  width: 30%;
}

.links-wrapper {
  width: 70%;
  display: flex;

  justify-content: flex-end;
}
.logo {
  display: block;
  height: 49px;
}
.buttons {
  color: #3d3d3d;
  margin: 15px 0px 15px 30px;
}
</style>
