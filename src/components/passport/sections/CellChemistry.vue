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
        <v-col sm="12" md="4" class="pa-0 ma-0">
          <AttributeField
            icon="mdi-battery-plus"
            :attributes-list="electrolyteComposition"
            label="Composition of electrolyte"
            data-cy="electrolyte-composition"
            style="margin-bottom: 12px; amx"
          />
          <div>
            <img
              :src="BatteryComposition"
              alt="BatteryComposition"
              style="margin-left: 6px"
            />
          </div>
        </v-col>
      </v-row>
    </v-container>
    <v-container class="ma-0">
      <v-row class="section">
        <v-col sm="12" md="12" class="pa-0 ma-0">
          <div class="element-chart-label">
            Recyclate Content Active Materials
          </div>
          <ElementChart :data="propsData" />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import AttributeField from "../AttributeField.vue";
import BatteryComposition from "../../../media/BatteryComposition.svg";
import ElementChart from "../../passport/ElementChart.vue";

export default {
  name: "CellChemistry",
  components: {
    AttributeField,
    ElementChart,
  },
  setup() {
    return {
      BatteryComposition,
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
      toggle: false,
      propsData: [
        {
          materialPercentageMassFraction: 47,
          materialName: "Ni",
          materialWeight: 2.5,
        },
        {
          materialPercentageMassFraction: 9,
          materialName: "Co",
          materialWeight: 2.5,
        },
        {
          materialPercentageMassFraction: 19,
          materialName: "Li",
          materialWeight: 2.5,
        },
        {
          materialPercentageMassFraction: 0,
          materialName: "Pb",
          materialWeight: 2.5,
        },
      ],
      cathodeCompositionOther: [
        {
          materialPercentageMassFraction: 19,
          materialName: "Pb",
          materialWeight: 2.5,
        },
      ],
      electrolyteComposition: [
        {
          materialPercentageMassFraction: 19,
          materialName: "Pb",
          materialWeight: 2.5,
        },
      ],
    };
  },
};
</script>
