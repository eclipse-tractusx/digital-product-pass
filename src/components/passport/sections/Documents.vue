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

<template v-if="propsData">
  <div>
    <div class="sub-section-container">
      <DocumentField :field="propsData.responsibleSourcing" />
      <DocumentField :field="propsData.packagingInstructions" />
      <DocumentField :field="propsData.transportationInstructions" />
      <DocumentField :field="propsData.vehicleDismantlingProcedure" />
      <DocumentField :field="propsData.testReportsResults" />
      <DocumentField :field="propsData.batteryDismantlingProcedure" />
      <DocumentField :field="propsData.safetyMeasures" />
      <DocumentField :field="propsData.declarationOfConformity" />
    </div>
  </div>
</template>
 
<script>
import DocumentField from "../DocumentField.vue";

export default {
  name: "AdditionalDocuments",
  components: {
    DocumentField,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    data: {
      type: Object,
      required: false,
      default: Object,
    },
  },

  data() {
    return {
      toggle: false,
      propsData: this.$props.data.data.passport,
    };
  },
};
</script>
