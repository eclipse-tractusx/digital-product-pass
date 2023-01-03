<template>
  <footer>
    <div class="links"></div>
    <div class="copy">Copyright © Catena-X Automotive Network.</div>
  </footer>
</template>

<script>
import CatenaLogo from "../assets/logo.png";

export default {
  name: "FooterComponent",
  components: {},
  setup() {
    return {
      CatenaLogo,
    };
  },
};
</script>

<style scoped>
footer {
  position: relative;
  left: 0;
  right: 0;
  bottom: 0;
  font-family: Arial, Helvetica, sans-serif;
  text-align: center;
  padding-top: 16vh;
  padding-bottom: 20px;
  /* background-image: url(data:image/svg+xml;base64,PHN2ZyB2ZXJzaW9uPSIxLjIiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgdmlld0JveD0iMCAwIDE0NDAgMzYwIj4KPGRlZnM+PGxpbmVhckdyYWRpZW50IGlkPSJnMSIgeDE9IjExMzAuOSIgeTE9Ii00NjEiIHgyPSIxNjYzLjQiIHkyPSI0MjYuNCIgZ3JhZGllbnRVbml0cz0idXNlclNwYWNlT25Vc2UiPjxzdG9wIG9mZnNldD0iMCIgc3RvcC1jb2xvcj0iI2ZmYjMyNiIvPjxzdG9wIG9mZnNldD0iMSIgc3RvcC1jb2xvcj0iI2ZmNzgyYyIvPjwvbGluZWFyR3JhZGllbnQ+PC9kZWZzPgo8cGF0aCBzdHlsZT0iZmlsbDp1cmwoI2cxKSIgZD0ibTEyNzAuNSAzMS44bDAuMy0wLjFjNTYuNC0xMC43IDExMi44LTIxLjQgMTY5LjItMzEuN3YzNjBoLTE0NDB2LTIzNy40cTM1OS44IDM3LjcgNzE5LjUtMS4yYzE4My43LTE5LjkgMzY3LjMtNTQuOCA1NTEtODkuNnoiLz4KPC9zdmc+); */
  background-repeat: no-repeat;
  background-size: cover;
}

.copy {
  font-size: 12px;
}

.links {
  padding-top: 20px;
}

.links,
.copy {
  line-height: 60px;
  min-height: 60px;
}
</style>
