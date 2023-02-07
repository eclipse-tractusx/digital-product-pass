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
  <SectionHeader title="6. Additional information" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <div class="sub-section-container">
      <div v-if="documents.responsibleSourcing" class="field-container">
        <a
          v-for="document in documents.responsibleSourcing"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.packagingInstructions" class="field-container">
        <a
          v-for="document in documents.packagingInstructions"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.transportationInstructions" class="field-container">
        <a
          v-for="document in documents.transportationInstructions"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.vehicleDismantlingProcedure" class="field-container">
        <a
          v-for="document in documents.vehicleDismantlingProcedure"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.testReportsResults" class="field-container">
        <a
          v-for="document in documents.testReportsResults"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.batteryDismantlingProcedure" class="field-container">
        <a
          v-for="document in documents.batteryDismantlingProcedure"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.safetyMeasures" class="field-container">
        <a
          v-for="document in documents.safetyMeasures"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
      </div>
      <div v-if="documents.declarationOfConformity" class="field-container">
        <a
          v-for="document in documents.declarationOfConformity"
          :key="document"
          :href="document.fileLocation"
          target="_blank"
          class="field-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.title }}</span>
          <span class="field-value">
            {{ document.fileLocation }}
          </span>
        </a>
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

.field-container {
  display: flex;
  flex-direction: column;
  width: 33%;
  min-height: 120px;
  text-decoration: none;
}

.field-label {
  padding: 30px 0 10px 40px;
  font-size: 14px;
  color: #777777;
}

.field-value {
  padding-left: 40px;
  font-size: 14px;
  line-height: 20px;
  font-weight: bold;
  text-decoration: underline;
}

.longer {
  padding-bottom: 0;
}

.icon {
  width: 37px;
  height: 39px;
  margin-left: 30px;
}

.list-container {
  width: 33%;
  padding-left: 40px;
  padding-bottom: 40px;
}

.list-label {
  padding: 30px 0 10px 0;
  font-size: 12px;
  color: #777777;
}

ul {
  display: flex;
  flex-direction: column;
}

li {
  margin-left: 20px;
  font-weight: bold;
}

.hidden {
  display: none;
}

.two-third-width {
  width: 66% !important;
}

.short-label {
  max-width: 53%;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
  }

  .section-content {
    margin-bottom: 0;
  }

  .longer {
    padding-bottom: 22px;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }

  .list-container {
    width: 100%;
    padding-left: 40px;
    padding-bottom: 40px;
  }

  .field-container {
    border-bottom: solid 1px #edefe5;
    width: 100%;
    min-height: 120px;
  }

  .two-third-width {
    width: 100% !important;
  }
}
</style>
