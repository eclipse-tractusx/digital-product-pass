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
  <v-card>
    <v-tabs v-model="tab" center-active show-arrows class="menu">
      <v-tab
        v-for="(section, index) in componentsNames"
        :key="index"
        :value="section.component"
      >
        <v-icon start md :icon="section.icon"> </v-icon>
        {{
          section.label.includes("passportView")
            ? $t(`${section.label}`)
            : semanticId ===
              "urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"
            ? $t(`passportView.tppComponentsNames.${section.label}`)
            : $t(`passportView.dppComponentsNames.${section.label}`)
        }}
      </v-tab>
    </v-tabs>
    <v-card-text>
      <v-window v-model="tab">
        <v-window-item
          v-for="(section, index) in componentsNames"
          :key="index"
          :value="section.component"
        >
          <component :is="section.component" :data="componentsData" />
        </v-window-item>
      </v-window>
    </v-card-text>
  </v-card>
</template>

<script>
import GeneralInformation from "../passport/sections/GeneralInformation.vue";
import CellChemistry from "../passport/sections/CellChemistry.vue";
import ElectrochemicalProperties from "../passport/sections/ElectrochemicalProperties.vue";
import BatteryComposition from "@/components/passport/sections/BatteryComposition.vue";
import StateOfBattery from "@/components/passport/sections/StateOfBattery.vue";
import Documents from "@/components/passport/sections/Documents.vue";
import Exchange from "@/components/passport/sections/Exchange.vue";
import Serialization from "../passport/sections/Serialization.vue";
import Typology from "../passport/sections/Typology.vue";
import Metadata from "../passport/sections/Metadata.vue";
import Characteristics from "../passport/sections/Characteristics.vue";
import Components from "../passport/sections/Components.vue";
import Commercial from "../passport/sections/Commercial.vue";
import Identification from "../passport/sections/Identification.vue";
import Sources from "../passport/sections/Sources.vue";
import Handling from "../passport/sections/Handling.vue";
import AdditionalData from "../passport/sections/AdditionalData.vue";
import Sustainability from "../passport/sections/Sustainability.vue";
import Operation from "../passport/sections/Operation.vue";
import ProductSpecificParameters from "../passport/sections/ProductSpecificParameters.vue";
import Instructions from "../passport/sections/Instructions.vue";
import SparePartSupplier from "../passport/sections/SparePartSupplier.vue";
import StateOfHealth from "../passport/sections/StateOfHealth.vue";

export default {
  name: "TabsComponent",
  components: {
    GeneralInformation,
    CellChemistry,
    ElectrochemicalProperties,
    BatteryComposition,
    StateOfBattery,
    Documents,
    Exchange,
    Serialization,
    Typology,
    Components,
    // eslint-disable-next-line vue/no-reserved-component-names
    Metadata,
    Characteristics,
    Commercial,
    Identification,
    Sources,
    Handling,
    AdditionalData,
    Sustainability,
    Operation,
    ProductSpecificParameters,
    Instructions,
    SparePartSupplier,
    StateOfHealth,
  },
  data() {
    return {
      tab: null,
    };
  },
  props: {
    componentsNames: { type: Object, default: Object },
    componentsData: { type: Object, default: Object },
    semanticId: { type: String, default: "" },
  },
};
</script>