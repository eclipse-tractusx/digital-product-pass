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
