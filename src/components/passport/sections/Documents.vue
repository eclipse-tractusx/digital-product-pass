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

<template v-if="documents">
  <div class="documents">
    <SectionHeader
      title="6. Additional information"
      @click="toggle = !toggle"
    />
    <div class="section-content" :class="[toggle ? 'hidden' : '']">
      <div class="sub-section-container">
        <DocumentField :field="documents.responsibleSourcing" />
        <DocumentField :field="documents.packagingInstructions" />
        <DocumentField :field="documents.transportationInstructions" />
        <DocumentField :field="documents.vehicleDismantlingProcedure" />
        <DocumentField :field="documents.testReportsResults" />
        <DocumentField :field="documents.batteryDismantlingProcedure" />
        <DocumentField :field="documents.safetyMeasures" />
        <DocumentField :field="documents.declarationOfConformity" />
      </div>
    </div>
  </div>
</template>
 
<script>
import SectionHeader from "../../general/SectionHeader.vue";
import DocumentField from "../DocumentField.vue";

export default {
  name: "AdditionalDocuments",
  components: {
    SectionHeader,
    DocumentField,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    documents: {
      type: Object,
      required: false,
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
