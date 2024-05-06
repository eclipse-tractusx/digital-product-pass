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

<template v-if="propsData">
  <div class="section">
    <v-container class="ma-0">
      <v-row class="section">
        <v-col sm="12" md="2" class="pa-0 ma-0">
          <template v-if="propsData.stateOfBattery">
            <Field
              icon="mdi-battery-plus"
              data-cy="state-of-charge"
              :label="$t('sections.stateOfBattery.stateOfHealth')"
              :value="propsData.stateOfBattery.stateOfHealth"
              style="margin-bottom: 12px"
              unit="%"
            />
          </template>
          <Field
            icon="mdi-recycle-variant"
            :label="$t('sections.stateOfBattery.cycleLifeTestCRate')"
            :value="propsData.batteryCycleLife.cycleLifeTestCRate"
            style="margin-bottom: 12px"
            unit="C"
          />
        </v-col>
        <v-col sm="12" md="3" class="pa-0 ma-0">
          <template v-if="propsData.stateOfBattery">
            <Field
              icon="mdi-flash-outline"
              :label="$t('sections.stateOfBattery.stateOfCharge')"
              :value="propsData.stateOfBattery.stateOfCharge"
              style="margin-bottom: 12px"
              unit="%"
            />
          </template>
          <Field
            icon="mdi-flash-outline"
            :label="$t('sections.stateOfBattery.cycleLifeTestDepthOfDischarge')"
            :value="propsData.batteryCycleLife.cycleLifeTestDepthOfDischarge"
            style="margin-bottom: 12px"
            unit="%"
          />
        </v-col>
        <v-col sm="12" md="5" class="pa-0 ma-0">
          <!-- componet belowe needs to be repalced with charging cycle bar chart -->
          <Field
            icon="mdi-battery-charging"
            :label="$t('sections.stateOfBattery.expectedLifetime')"
            style="background: #f9f9f9; margin-bottom: 12px"
            :value="propsData.batteryCycleLife.expectedLifetime"
            unit="cycles"
          />
          <Field
            icon="mdi-thermometer-low"
            :label="$t('sections.stateOfBattery.temperatureRange')"
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
        <template v-if="propsData.stateOfBattery">
          <v-col sm="12" md="2" class="pa-0 ma-0">
            <Field
              icon="mdi-numeric-1-circle-outline"
              :label="$t('sections.stateOfBattery.statusBattery')"
              :value="propsData.stateOfBattery.statusBattery"
            />
          </v-col>
        </template>
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
    data: {
      type: Object,
      default: Object,
    },
  },

  data() {
    return {
      propsData: this.$props.data.aspect,
    };
  },
};
</script>
