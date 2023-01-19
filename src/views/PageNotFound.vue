<template>
  <div class="PageNotFound">
    <h1>This is an about page</h1>
    <img :src="Error" alt="error" class="error" />
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
  width: 100%;
}
</style>
