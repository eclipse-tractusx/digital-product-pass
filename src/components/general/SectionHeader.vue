<!--
  Catena-X - Product Passport Consumer Frontend
 
  Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->

<template>
  <div className="section-header" @click="toggle = !toggle">
    <span className="section-title">{{ title }}</span>
    <span className="toggle-container">
      <img
        :src="Toggle"
        alt="toggle"
        class="toggle-btn"
        :class="[toggle ? 'rotate' : '']"
      />
    </span>
  </div>
</template>

<script>
import Toggle from "../../media/toggle.svg";

export default {
  name: "SectionHeader",

  props: {
    title: {
      type: String,
      default: "",
    },
  },
  setup() {
    return {
      Toggle,
    };
  },
  data() {
    return {
      toggle: false,
    };
  },
};
</script>

<style scoped>
.section-header {
  display: flex;
  border: solid 1px #b3cb2d;
  border-radius: 4px 4px 0 0;
  background-color: #fff;
}

.section-title {
  font-size: 28px;
  color: #b3cb2d;
  margin: 30px 30px 30px 40px;
  font-weight: bold;
  width: 90%;
}

.toggle-container {
  display: flex;
}

.toggle-btn {
  width: 70px;
  cursor: pointer;
}

.rotate {
  transform: rotate(180deg);
}

@media (max-width: 750px) {
  .section-header {
    border-radius: 0;
  }

  .section-title {
    font-size: 20px;
    margin: 22px 0px 22px 30px;
  }

  .section-header {
    border: none;
    border-top: solid 1px #b3cb2d;
    border-bottom: solid 1px #b3cb2d;
  }
}
</style>
