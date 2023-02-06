<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<template>
  <footer data-cy="footer">
    <div class="links">
      <div>
        <router-link id="lnkHelp" to="/PageNotFound">Help</router-link>
      </div>
      <div>
        <router-link id="lnkHelp" to="/PageNotFound">Contact</router-link>
      </div>
      <div>
        <router-link id="lnkHelp" to="/PageNotFound">Imprint</router-link>
      </div>
      <div>
        <router-link id="lnkHelp" to="/PageNotFound">Privacy</router-link>
      </div>
      <div>
        <router-link id="lnkHelp" to="/PageNotFound"
          >Terms of Service</router-link
        >
      </div>
      <div>
        <router-link id="lnkHelp" to="/PageNotFound">Cookie Policy</router-link>
      </div>
      <div>
        <router-link id="lnkHelp" to="/PageNotFound"
          >Third Party Licenses</router-link
        >
      </div>
    </div>
    <div class="copy">Copyright © Catena-X Automotive Network. {{ VERSION }}</div>
  </footer>
</template>

<script>
import { VERSION } from "@/services/service.const";

export default {
  name: "FooterComponent",
  components: {},
  setup() {
    return {
      VERSION
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
  display: flex;
  align-items: center;
  justify-content: center;
}

a {
  margin: 0 1rem;
  color: #000;
  font-size: 12px;
}

.links,
.copy {
  line-height: 60px;
  min-height: 60px;
}

@media(max-width: 740px){
  
.links {
  padding-top: 20px;
  display: initial;
}
}

</style>
