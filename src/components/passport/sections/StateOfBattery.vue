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
    <v-container class="ma-0">
      <v-row class="section">
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <Field
            icon="mdi-battery-plus"
            data-cy="state-of-charge"
            label="State of health"
            :value="propsData.stateOfBattery.stateOfHealth"
            style="margin-bottom: 12px"
            unit="%"
          />
          <Field
            icon="mdi-recycle-variant"
            label="Cycle life test c rate"
            :value="propsData.batteryCycleLife.cycleLifeTestCRate"
            style="margin-bottom: 12px"
            unit="cycles"
          />
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <Field
            icon="mdi-flash-outline"
            label="State of charge"
            :value="propsData.stateOfBattery.stateOfCharge"
            style="margin-bottom: 12px"
            unit="%"
          />

          <Field
            icon="mdi-flash-outline"
            label="Cycle life test depth of discharge"
            :value="propsData.batteryCycleLife.cycleLifeTestDepthOfDischarge"
            style="margin-bottom: 12px"
            unit="cycles"
          />
        </v-col>
        <v-col sm="12" md="5" class="pa-0 ma-0">
          <Field
            icon="mdi-battery-charging"
            label="Expected lifetime"
            style="background: #f9f9f9; margin-bottom: 12px"
            :value="propsData.batteryCycleLife.expectedLifetime"
            unit="cycles"
          />
          <Field
            icon="mdi-thermometer-low"
            label="Temperature range (idle state)"
            style="background: #f9f9f9; margin-bottom: 12px"
            :tempRangeMin="
              propsData.temperatureRangeIdleState
                .temperatureRangeIdleStateLowerLimit
            "
            tempUnit="°C"
            :tempRangeMax="
              propsData.temperatureRangeIdleState
                .temperatureRangeIdleStateUpperLimit
            "
            :img="TempRange"
          />
        </v-col>
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <Field
            icon="mdi-numeric-1-circle-outline"
            label="Status battery"
            :value="propsData.stateOfBattery.statusBattery"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Field from "../Field.vue";
import TempRange from "../../../media/tempRange.svg";

export default {
  name: "StateOfBattery",
  components: {
    Field,
  },
  setup() {
    return {
      TempRange,
    };
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
