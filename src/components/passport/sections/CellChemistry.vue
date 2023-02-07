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

<template v-if="cellChemistry">
  <SectionHeader title="2. Cell chemistry" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <!-- Composition of Electrolyte -->
    <AttributeField
      :attributes-list="cellChemistry.electrolyteComposition"
      label="Composition of electrolyte"
      data-cy="electrolyte-composition"
    />
    <!-- Composition of Anode -->
    <AttributeField
      :attributes-list="cellChemistry.anodeActiveMaterials"
      label="Composition of Anode"
    />
    <!-- Composition of Anode other -->
    <AttributeField
      :attributes-list="cellChemistry.anodeCompositionOther"
      label="Composition of other Anode materials"
    />
    <!-- Composition of Cathode -->
    <AttributeField
      :attributes-list="cellChemistry.cathodeActiveMaterials"
      label="Composition of Cathode"
    />
    <!-- Composition of Cathode other -->
    <AttributeField
      :attributes-list="cellChemistry.cathodeCompositionOther"
      label="Composition of other Cathode materials"
    />
    <!-- Recyclate content active materials -->
    <AttributeField
      :attributes-list="cellChemistry.recyclateContentActiveMaterials"
      label="Recyclate content active materials"
    />
  </div>
</template>

<script>
import SectionHeader from "../../general/SectionHeader.vue";
import AttributeField from "../AttributeField.vue";

export default {
  name: "CellChemistry",
  components: {
    SectionHeader,
    AttributeField,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    cellChemistry: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
    };
  },
};
</script>

<style scoped>
.section-content {
  width: 100%;
  border: solid 1px #b3cb2d;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
  margin-bottom: 50px;
}

.sub-section-container {
  display: flex;
  flex-wrap: wrap;
  border-bottom: solid 1px #edefe5;
}

.field-container {
  display: flex;
  flex-direction: column;
  width: 33%;
}

.sub-title {
  font-weight: bold;
  font-size: 20px;
  color: #c6cca3;
}

.sub-title-container {
  padding: 22px 40px 0 40px;
  width: 100%;
}

.list-container {
  width: 33%;
  padding: 0 0 22px 40px;
}

.list-label {
  padding: 22px 0 10px 0;
  font-size: 12px;
  color: #777777;
}

ul {
  display: flex;
  flex-direction: column;
  padding: 0;
}

li {
  margin-left: 20px;
  font-weight: bold;
}

.hidden {
  display: none;
}

@media (max-width: 750px) {
  .section-content {
    border: none;
  }

  .section-content {
    margin-bottom: 0;
  }

  .field-container {
    width: 100%;
  }

  .list-container {
    width: 100%;
    padding-left: 50px;
  }

  .section-content {
    border: none;
    margin-bottom: 0;
  }

  .sub-title-container {
    padding: 22px 40px 0 30px;
  }
}
</style>
