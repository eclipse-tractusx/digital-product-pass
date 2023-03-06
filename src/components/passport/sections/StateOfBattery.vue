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
