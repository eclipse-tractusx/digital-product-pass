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

<template v-if="propsData">
  <div class="section">
    <div class="sub-section-container">
      <Field
        data-cy="state-of-charge"
        label="State of health"
        :value="propsData.stateOfBattery.stateOfHealth"
      />
      <Field
        label="Status battery"
        :value="propsData.stateOfBattery.statusBattery"
      />
      <Field
        label="State of charge"
        :value="propsData.stateOfBattery.stateOfCharge"
      />
      <Field
        label="Cycle life test c rate"
        :value="propsData.batteryCycleLife.cycleLifeTestCRate"
      />
      <Field
        label="Cycle life test depth of discharge"
        :value="propsData.batteryCycleLife.cycleLifeTestDepthOfDischarge"
      />
      <Field
        label="Expected lifetime"
        :value="propsData.batteryCycleLife.expectedLifetime"
      />
      <Field
        label="Temperature range idle state upper limit"
        :value="
          propsData.temperatureRangeIdleState
            .temperatureRangeIdleStateUpperLimit
        "
      />
      <Field
        label="Temperature range idle state lower limit"
        :value="
          propsData.temperatureRangeIdleState
            .temperatureRangeIdleStateLowerLimit
        "
      />
    </div>
  </div>
</template>

<script>
import Field from "../Field.vue";

export default {
  name: "StateOfBattery",
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
      propsData: this.$props.data.data.passport,
    };
  },
};
</script>
