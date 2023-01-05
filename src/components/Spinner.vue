<template>
  <div class="spinner-container">
    <img class="spinner" :src="Spinner" />
  </div>
</template>

<script>
import Spinner from "../assets/spinner.gif";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Spinner",
  setup() {
    return {
      Spinner,
    };
  },
};
</script>

<style scoped>
.spinner-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.spinner {
  margin: auto;
  width: 8vh;
  animation: rotate 3s infinite;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}
</style>
