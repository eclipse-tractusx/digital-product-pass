<template>
  <div class="PageNotFound">
    <router-link to="/">
      <img :src="Error" alt="error" class="error" />
    </router-link>
  </div>
</template>

<script>
import Error from "../assets/500error.png";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "PageNotFound",
  components: {},
  setup() {
    return {
      Error,
    };
  },
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>
