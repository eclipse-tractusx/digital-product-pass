<!--
  Tractus-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

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

<template >
  <!-- eslint-disable-next-line vue/no-multiple-template-root -->
  <div v-for="document in field" :key="document">
    <div class="document-field">
      <div class="field-container">
        <v-icon icon="mdi-text-box-outline" class="document-icon"></v-icon>
        <a
          :href="document.documentLink"
          target="_blank"
          class="link-container"
          data-cy="link-to-the-label-element"
        >
          <span class="field-label">{{ document.documentTitle }}</span>
          <span class="field-value link-wrapper">
            <span class="link">
              {{ document.documentLink }}
            </span>
          </span>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "InstructionsField",
  props: {
    field: { type: Array, default: () => [] },
  },
};
</script>

