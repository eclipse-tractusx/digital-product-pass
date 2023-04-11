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
    <!-- Composition of battery -->
    <AttributeField
      :attributes-list="propsData.compositionOfBattery"
      label="Composition of battery"
    />
    <!-- Critical raw materials -->
    <div class="sub-section-container">
      <div class="sub-title-container">
        <span class="sub-title">Critical raw materials</span>
      </div>
      <div v-if="propsData.criticalRawMaterials" class="list-container">
        <ul>
          <span class="list-label"></span>
          <li>
            <span>
              {{ propsData.criticalRawMaterials }}
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
      <div v-if="propsData.components" class="list-container">
        <ul>
          <span class="list-label">Components part number</span>
          <li>
            <span>
              {{ propsData.components.componentsPartNumber }}
            </span>
          </li>
        </ul>
      </div>
      <div
        v-if="propsData.components.componentsSupplier"
        class="list-container"
      >
        <ul>
          <span class="list-label">Address</span>
          <li
            v-for="supplierDetails in propsData.components.componentsSupplier"
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
            v-for="supplierDetails in propsData.components.componentsSupplier"
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
import AttributeField from "../AttributeField.vue";

export default {
  name: "BatteryComposition",
  components: {
    AttributeField,
  },
  props: {
    sectionTitle: {
      type: String,
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
      propsData: this.$props.data.data.passport.composition,
    };
  },
};
</script>
