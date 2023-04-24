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

<template>
  <div class="field-container">
    <span class="field-label">{{ label }}</span>
    <span v-if="value" class="field-value">{{ value }} {{ unit }}</span>
    <span v-else-if="length"></span>
    <span v-else-if="tempRangeMin" class="field-value">
      {{ tempRangeMin }} {{ tempUnit }}—{{ tempRangeMax }} {{ tempUnit }}</span
    >

    <span v-else-if="day"></span>
    <span v-else class="field-value">—</span>
    <!-- This block of code is for section 1 General information Importer information, STREET goes as a value and than the rest below -->
    <span v-if="city" class="field-value">{{ city ? city : "—" }} </span>
    <span v-if="postal" class="field-value">{{ postal ? postal : "—" }} </span>
    <span v-if="country" class="field-value"
      >{{ country ? country : "—" }}
    </span>
    <!-- This block of code is for section 4 Parameters of the battery where we have tests and temp ranges -->
    <div v-if="tempMin || test" class="test-container">
      <span v-if="tempMin" class="test"
        >Temp ranges:
        <b>{{ tempMin }} {{ tempUnit }} — {{ tempMax }} {{ tempUnit }}</b>
      </span>
      <span v-if="test" class="test"
        >Reference test: <b>{{ test }}</b>
      </span>
    </div>

    <!-- This block of code is for section 1 General information. Dimensions of the battery -->
    <span v-if="length" class="field-value"
      >L: {{ length }}{{ unit }}, H: {{ height }}{{ unit }}, W: {{ width
      }}{{ unit }}</span
    >
    <!-- This block of code is for dates -->
    <span v-if="day" class="field-value">{{ day }}{{ month }}{{ year }}</span>
  </div>
</template>

<script>
export default {
  name: "FieldComponent",
  props: {
    label: { type: [String, Number], default: "" },
    value: { type: [String, Number], default: "" },
    unit: { type: [String, Number], default: "" },
    test: { type: [String, Number], default: "" },
    city: { type: [String, Number], default: "" },
    postal: { type: [String, Number], default: "" },
    country: { type: [String, Number], default: "" },
    length: { type: [String, Number], default: "" },
    height: { type: [String, Number], default: "" },
    width: { type: [String, Number], default: "" },
    day: { type: [String, Number], default: "" },
    month: { type: [String, Number], default: "" },
    year: { type: [String, Number], default: "" },
    tempMin: { type: [String, Number], default: "" },
    tempMax: { type: [String, Number], default: "" },
    tempUnit: { type: [String, Number], default: "" },
    tempRangeMin: { type: [String, Number], default: "" },
    tempRangeMax: { type: [String, Number], default: "" },
  },
};
</script>

<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}

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

.test {
  font-size: 12px;
  padding-left: 40px;
}

.field-container {
  display: flex;
  flex-direction: column;
  width: 33%;
  min-height: 120px;
}

.field-label {
  padding: 30px 0 10px 40px;
  font-size: 12px;
  color: #777777;
}

.field-value {
  padding-left: 40px;
  font-size: 16px;
  line-height: 20px;
  font-weight: bold;
  line-break: anywhere;
}

.test-container {
  display: flex;
  flex-direction: column;
  padding: 12px 0 22px 0;
}

@media (max-width: 750px) {
  .section-content {
    width: 100%;
    border: none;
    border-radius: 0;
    background-color: #fff;
    margin-bottom: 50px;
  }

  .sub-section-container {
    display: flex;
    flex-wrap: wrap;
    border-bottom: solid 1px #edefe5;
  }

  .test {
    font-size: 12px;
    padding: 4px 0 4px 40px;
  }

  .test-container {
    padding: 0 0 22px 0;
    margin-top: -18px;
  }

  .field-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    min-height: 50px;
    border-bottom: solid 1px #edefe5;
  }

  .field-label {
    padding: 22px 40px 8px 50px;
    font-size: 14px;
    color: #777777;
    overflow: auto;
  }

  .field-value {
    padding: 0 0 22px 50px;
    font-size: 16px;
    line-height: 16px;
    font-weight: bold;
  }

  .test {
    padding: 0 0 0 50px;
  }
}
</style>