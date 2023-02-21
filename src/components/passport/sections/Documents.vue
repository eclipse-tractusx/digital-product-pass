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
        <div v-if="documents.responsibleSourcing" class="field-container">
          <a
            v-for="document in documents.responsibleSourcing"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div v-if="documents.packagingInstructions" class="field-container">
          <a
            v-for="document in documents.packagingInstructions"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div
          v-if="documents.transportationInstructions"
          class="field-container"
        >
          <a
            v-for="document in documents.transportationInstructions"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div
          v-if="documents.vehicleDismantlingProcedure"
          class="field-container"
        >
          <a
            v-for="document in documents.vehicleDismantlingProcedure"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div v-if="documents.testReportsResults" class="field-container">
          <a
            v-for="document in documents.testReportsResults"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div
          v-if="documents.batteryDismantlingProcedure"
          class="field-container"
        >
          <a
            v-for="document in documents.batteryDismantlingProcedure"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div v-if="documents.safetyMeasures" class="field-container">
          <a
            v-for="document in documents.safetyMeasures"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
        <div v-if="documents.declarationOfConformity" class="field-container">
          <a
            v-for="document in documents.declarationOfConformity"
            :key="document"
            :href="document.fileLocation"
            target="_blank"
            class="link-container"
            data-cy="link-to-the-label-element"
          >
            <span class="field-label">{{ document.title }}</span>

            <span class="field-value link-wrapper">
              <v-icon
                icon="mdi-text-box-outline"
                class="document-icon"
              ></v-icon>
              <span class="link">
                {{ document.fileLocation }}
              </span>
            </span>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>
 
<script>
import SectionHeader from "../../general/SectionHeader.vue";

export default {
  name: "AdditionalDocuments",
  components: {
    SectionHeader,
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
