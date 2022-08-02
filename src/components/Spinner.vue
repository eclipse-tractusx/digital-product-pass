<template>
  <div class="spinner-container">
    <img class="spinner" src="../assets/spinner.svg" />
  </div>
</template>

<script>
export default {
  name: "Spinner",
};
</script>

<style>
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
