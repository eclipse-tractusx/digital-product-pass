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

<template v-if="propsDate">
  <div class="section">
    <div class="sub-section-container">
      <Field label="Contract ID" :value="propsData.negotiation.id" />
      <Field
        label="Contract Agreement ID"
        :value="propsData.transferRequest.contractId"
      />
      <Field
        label="Transfer Process ID"
        :value="propsData.transferRequest.id"
      />
    </div>
  </div>
</template>

<script>
import Field from "../Field.vue";
export default {
  name: "ContractInformation",
  components: {
    Field,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.metadata,
    };
  },
};
</script>
