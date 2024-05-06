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

<template>
  <div class="additional-data">
    <!-- eslint-disable-next-line vue/no-v-for-template-key -->
    <template v-for="attribute in jsonData.children" :key="attribute">
      <template v-if="attribute.type.dataType !== 'object'">
        <DialogComponent class="field-dialog">
          <Field
            info
            :icon="callIconFinder(attribute.label)"
            :label="attribute.label"
            :value="processValue(attribute)"
            :unit="attribute.type.typeUnit ? attribute.type.typeUnit : ''"
          />
          <template v-slot:text>
            {{ attribute.description }}
          </template>
        </DialogComponent>
      </template>
      <template v-else>
        <DialogComponent class="field-dialog">
          <div class="column">
            <div class="container-label">{{ attribute.label }}</div>
          </div>
          <template v-slot:text>
            {{ attribute.description }}
          </template>
        </DialogComponent>
        <template v-if="attribute.children">
          <recursive-additional-data :jsonData="attribute" />
        </template>
      </template>
    </template>
  </div>
</template>

<script>
import passportUtil from "@/utils/passportUtil.js";
import Field from "../passport/Field.vue";
import DialogComponent from "../general/Dialog.vue";
export default {
  name: "RecursiveAdditionalData",
  components: {
    Field,
    DialogComponent,
  },
  props: {
    jsonData: {
      type: [Object, Array],
    },
  },

  methods: {
    callIconFinder(unit) {
      return passportUtil.iconFinder(unit);
    },
    processValue(attribute) {
      if (attribute.type.dataType == "array" && Array.isArray(attribute.data)) {
        return attribute.data.join(", ");
      } else {
        return attribute.data;
      }
    },
  },
};
</script>

