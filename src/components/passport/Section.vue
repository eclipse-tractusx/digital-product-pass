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
  <div>
    <SectionHeader :title="title" @click="toggle = !toggle" />
    <div class="section-content" :class="[toggle ? 'hidden' : '']">
      <slot>Sorry, no content</slot>
    </div>
  </div>
</template>

<script>
import SectionHeader from "../general/SectionHeader.vue";

export default {
  name: "SectionComponent",
  components: {
    SectionHeader,
  },
  props: {
    title: { type: String, default: "" },
  },
  data() {
    return {
      toggle: false,
    };
  },
};
</script>


<style scoped>
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 22px 40px 0 40px;
  width: 100%;
}

.list-container {
  width: 33%;
  padding: 0 0 22px 40px;
}

.list-label {
  padding: 22px 0 10px 0;
  font-size: 12px;
  color: #777777;
}

ul {
  display: flex;
  flex-direction: column;
  padding: 0;
}

li {
  margin-left: 20px;
  font-weight: bold;
}

.hidden {
  display: none;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
  }

  .section-content {
    margin-bottom: 0;
  }

  .list-container {
    width: 100%;
    padding-left: 50px;
  }

  .section-content {
    border: none;
    margin-bottom: 0;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
