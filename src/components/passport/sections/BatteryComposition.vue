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

<template v-if="batteryComposition">
  <SectionHeader title="4. Battery composition" @click="toggle = !toggle" />
  <div class="section-content" :class="[toggle ? 'hidden' : '']">
    <!-- Composition of battery -->

    <AttributeField
      :attributes-list="batteryComposition.compositionOfBattery"
      label="Composition of battery"
    />
    <!-- Critical raw materials -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Critical raw materials</span>
      </div>
      <div
        v-if="batteryComposition.criticalRawMaterials"
        class="list-container"
      >
        <ul>
          <span class="list-label"></span>
          <li>
            <span>
              {{ batteryComposition.criticalRawMaterials }}
            </span>
          </li>
        </ul>
      </div>
    </div>
    <!-- Components -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Components</span>
      </div>
      <div v-if="batteryComposition.components" class="list-container">
        <ul>
          <span class="list-label">Components part number</span>
          <li>
            <span>
              {{ batteryComposition.components.componentsPartNumber }}
            </span>
          </li>
        </ul>
      </div>
    </div>
    <!-- Components supplier -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Components supplier</span>
      </div>
      <div
        v-if="batteryComposition.components.componentsSupplier"
        class="list-container"
      >
        <ul>
          <span class="list-label">Address</span>
          <li
            v-for="supplierDetails in batteryComposition.components
              .componentsSupplier"
            :key="supplierDetails"
          >
            <p>{{ supplierDetails.address.locality.value }}</p>
            <p>{{ supplierDetails.address.country.shortName }}</p>
            <p>{{ supplierDetails.address.postCode.value }}</p>
            <p>
              {{ supplierDetails.address.thoroughfare.value }}
              {{ supplierDetails.address.thoroughfare.number }}
            </p>
            <p>{{ supplierDetails.address.premise.value }}</p>
            <p>{{ supplierDetails.address.postalDeliveryPoint.value }}</p>
          </li>
        </ul>
        <ul>
          <span class="list-label">Contact</span>
          <li
            v-for="supplierDetails in batteryComposition.components
              .componentsSupplier"
            :key="supplierDetails"
          >
            <p>fax: {{ supplierDetails.contact.faxNumber }}</p>
            <p>www: {{ supplierDetails.contact.website }}</p>
            <p>tel: {{ supplierDetails.contact.phoneNumber }}</p>
            <p>
              email:
              {{ supplierDetails.contact.email }}
            </p>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import SectionHeader from "../../general/SectionHeader.vue";
import AttributeField from "../AttributeField.vue";

export default {
  name: "BatteryComposition",
  components: {
    SectionHeader,
    AttributeField,
  },
  props: {
    sectionTitle: {
      type: String,
      default: "",
    },
    batteryComposition: {
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
