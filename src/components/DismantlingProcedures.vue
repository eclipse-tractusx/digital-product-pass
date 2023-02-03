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
  <SectionHeader title="5. Dismantling Procedures" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <a
        data-cy="dismantling-procedures"
        :href="dismantlingProcedures.vehicleDismantlingProcedure.url"
        class="field-container"
        target="_blank"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">
          {{ dismantlingProcedures.vehicleDismantlingProcedure.value }}
        </span>
      </a>
    </div>
    <div class="sub-section-container">
      <a
        :href="dismantlingProcedures.batteryDismantlingProcedure.url"
        class="field-container"
        target="_blank"
      >
        <img :src="Pdf" alt="settings" class="icon" />
        <span class="field-value">
          {{ dismantlingProcedures.batteryDismantlingProcedure.value }}
        </span>
      </a>
    </div>
  </div>
</template>

<script>
import SectionHeader from "./SectionHeader.vue";
import Pdf from "../assets/pdf.svg";

export default {
  name: "DismantlingProcedures",
  components: {
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    dismantlingProcedures: {
      type: Object,
      required: false,
      default: Object,
    },
  },

  setup() {
    return {
      Pdf,
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
.section-content {
  display: flex;
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}

.sub-section-container {
  width: 33%;
  border-bottom: solid 1px #edefe5;
}

.field-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  text-align: center;
  align-items: center;
  min-height: 120px;
  cursor: pointer;
  padding: 20px 0 60px 0;
}

.icon {
  width: 27px;
  height: 36px;
  margin: 20px;
}

.field-value {
  font-size: 14px;
  line-height: 20px;
  font-weight: bold;
}

.hidden {
  display: none;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
    margin-bottom: 0;
    flex-direction: column;
  }

  .longer {
    padding-bottom: 0;
  }

  .sub-section-container {
    width: 100%;

    border-bottom: solid 1px #edefe5;
  }

  .field-container {
    width: 100%;
  }
}
</style>
