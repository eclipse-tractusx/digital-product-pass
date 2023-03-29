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
  <div class="document-field">
    <div class="field-container">
      <v-icon icon="mdi-text-box-outline" class="document-icon"></v-icon>
      <a
        v-for="document in field"
        :key="document"
        :href="document.fileLocation"
        target="_blank"
        class="link-container"
        data-cy="link-to-the-label-element"
      >
        <span class="field-label">{{ document.title }}</span>

        <span class="field-value link-wrapper">
          <span class="link">
            {{ document.fileLocation }}
          </span>
        </span>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: "DocumentField",
  props: {
    field: { type: [String, Number], default: "" },
  },
};
</script>

