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
    <div class="id-wrapper">
      <p class="id">
        {{ type }}:
        {{ id ? id : "—" }}
      </p>
    </div>
    <div v-if="id === 'NCR186850B'" class="code-container">
      <img
        :src="NCR186850B"
        alt="profile"
        class="code"
        width="170"
        height="170"
      />
    </div>
    <div v-if="id === 'IMR18650V1'" class="code-container">
      <img
        :src="IMR18650V1"
        alt="profile"
        class="code"
        width="170"
        height="170"
      />
    </div>
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
.id-wrapper {
  width: 60%;
  line-break: anywhere;
}
.code {
  padding: 0;
  margin: 0;
}
.id{
    font-size: 2em;
    line-height: 36px;
    font-weight: bold;
}
.id-container {
  display: flex;
  align-items: center;
  width: 76%;
  margin: 12em 12% 6% 12%;
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