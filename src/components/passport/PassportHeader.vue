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
  <div class="id-container">
    <div class="back-wrapper">
      <v-btn
        rounded="pill"
        color="#0F71CB"
        size="large"
        class="back-btn"
        @click="$router.go(-1)"
        variant="outlined"
        style="border: 2px solid; text-transform: initial"
      >
        <v-icon class="icon" start md icon="mdi-arrow-left"></v-icon>
        Back
      </v-btn>
    </div>
    <div class="id-wrapper">
      <p class="id">
        {{ type }}:
        {{ id ? id : "—" }}
      </p>
    </div>
    <div class="share-wrapper"></div>
  </div>
</template>

<script>
import IMR18650V1 from "../../media/IMR18650V1.svg";
import NCR186850B from "../../media/NCR186850B.svg";
export default {
  name: "PassportHeader",
  props: {
    id: {
      type: String,
      default: null,
    },
    type: {
      type: String,
      default: "BatteryID",
    },
  },
  setup() {
    return {
      NCR186850B,
      IMR18650V1,
    };
  },
};
</script>

<style>
.back-wrapper {
  position: absolute;
}
.share {
  cursor: not-allowed;
}

.share-wrapper {
  position: absolute;
  right: 0;
}

.id-wrapper {
  display: flex;
  justify-content: center;
  width: 100%;
  line-break: anywhere;
}
.code {
  padding: 0;
  margin: 0;
}
.id {
  font-size: 14px;
  line-height: 36px;
  font-weight: bold;
}
.id-container {
  position: relative;
  display: flex;
  align-items: center;
  width: 100%;
  margin: 6em 40px 0 40px;
  padding: 20px 0;
}
.code-container {
  width: 40%;
  display: flex;
  justify-content: flex-end;
}
@media (max-width: 750px) {
  .code-container {
    display: none;
  }
  .id-container {
    margin: 12em 0 2em 3em;
    padding: 20px 0;
    width: 85%;
  }
  .id-wrapper {
    width: 100%;
  }
  .id {
    font-size: 1.5em;
  }
}
</style>
