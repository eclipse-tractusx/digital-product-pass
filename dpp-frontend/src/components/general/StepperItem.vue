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
  <v-timeline-item
    :icon="condition ? 'mdi-check' : null"
    :dot-color="[condition ? 'green' : 'grey']"
    :size="condition ? 'large' : 'small'"
    :class="{ completed: condition }"
  >
    <div class="timeline-item">
      <div>
        <strong>
          <div class="step-title" :class="{ 'completed-step': condition }">
            {{ stepTitle }}
          </div></strong
        >
        <div class="text-caption-container">
          <p>
            {{ condition ? successStepSubTitle : initialStepSubTitle }}
          </p>
          <div v-if="displayId">
            <p class="text-caption">
              {{ condition ? idLabel : "" }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </v-timeline-item>
</template>

<script>
export default {
  name: "StepperItem",
  props: {
    condition: { type: [String, Number], default: "" },
    stepTitle: { type: [String, Number], default: "" },
    successStepSubTitle: { type: [String, Number], default: "" },
    initialStepSubTitle: { type: [String, Number], default: "" },
    displayId: { type: Boolean, default: false },
    idLabel: { type: [String, Number], default: "" },
  },
};
</script>

