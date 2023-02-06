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

<template v-if="contractInformation">
  <SectionHeader title="7. Contract information" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div v-if="contractInformation" class="sub-section-container">
      <Field
        label="Contract ID"
        :value="contractInformation.negotiation.id"
      />
      <Field
        label="Contract Agreement ID"
        :value="contractInformation.transferRequest.contractId"
      />
      <Field
        label="Transfer Process ID"
        :value="contractInformation.transferRequest.id"
      />
    </div>
  </div>
</template>

<script>
import SectionHeader from "../../general/SectionHeader.vue";
import Field from "../Field.vue";
export default {
  name: "ContractInformation",
  components: {
    Field,
    SectionHeader,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    contractInformation: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
    };
  },
};
</script>

<style scoped>
.full-width {
  width: 100% !important;
}
.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}
.two-third-width {
  width: 66% !important;
}
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}
.hidden {
  display: none;
}
.longer {
  padding-bottom: 50px;
}
@media (max-width: 750px) {
  .section-content {
    border: none;
  }
  .section-content {
    margin-bottom: 0;
  }
  .longer {
    padding-bottom: 0;
  }
}
</style>
