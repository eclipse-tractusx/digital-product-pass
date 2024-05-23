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
  <div class="field-container" :class="{ 'info-dialog': info }">
    <a v-if="link" :href="link" target="_blank">
      <v-icon start md :icon="icon" class="icon link"> </v-icon>
    </a>
    <v-icon v-else start md :icon="icon" class="icon"> </v-icon>
    <span class="field-label">{{ label }}</span>
    <span v-if="value" class="field-value"
      >{{ value }} {{ callUnitRemover(unit) }}
      <div v-if="subText" class="field-label">{{ subText }}</div>
    </span>

    <span v-else-if="length"></span>
    <span v-else-if="tempRangeMin" class="field-value">
      {{ tempRangeMin }} {{ tempUnit }} to {{ tempRangeMax }}
      {{ tempUnit }}
      <span>
        <span class="temp-range-value temp-range-min"
          >{{ tempRangeMin }} {{ tempUnit }}</span
        >
        <span class="temp-range-value temp-range-max"
          >{{ tempRangeMax }} {{ tempUnit }}</span
        >
        <img :src="img" alt="tempRange" class="temp-range" />
      </span>
    </span>

    <span v-else-if="day"></span>
    <span v-else-if="faxNumber" class="field-value"
      >{{ faxNumber ? faxNumber : "—" }}
    </span>
    <span v-else class="field-value">—</span>
    <!-- This block of code is for section 1 General information Importer information, STREET goes as a value and than the rest below -->
    <span v-if="postal" class="field-value">{{ postal ? postal : "—" }} </span>
    <span v-if="city" class="field-value">{{ city ? city : "—" }} </span>
    <span v-if="country" class="field-value"
      >{{ country ? country : "—" }}
    </span>
    <span v-if="phone" class="field-value">{{ phone ? phone : "—" }} </span>
    <span v-if="website" class="field-value"
      >{{ website ? website : "—" }}
    </span>
    <span v-if="email" class="field-value">{{ email ? email : "—" }} </span>

    <!-- This block of code is for section 4 Parameters of the battery where we have tests and temp ranges -->
    <div v-if="tempMin || test" class="test-container">
      <span v-if="tempMin" class="test"
        >{{ $t("field.tempRange") }}
        <b
          >{{ tempMin }} {{ tempUnit }} {{ $t("field.to") }} {{ tempMax }}
          {{ tempUnit }}</b
        >
      </span>
      <span v-if="test" class="test"
        >{{ $t("field.referenceTest") }} <b>{{ test }}</b>
      </span>
    </div>

    <!-- This block of code is for section 1 General information. Dimensions of the battery -->
    <span class="field-value">
      <div v-if="length">L: {{ length }} {{ callUnitRemover(unit) }}</div>
      <div v-if="height">H: {{ height }} {{ callUnitRemover(unit) }}</div>
      <div v-if="width">W: {{ width }} {{ callUnitRemover(unit) }}</div>
    </span>
    <!-- This block of code is for dates -->
    <span v-if="day" class="field-value">{{ day }}{{ month }}{{ year }}</span>
  </div>
</template>

<script>
import passportUtil from "@/utils/passportUtil.js";

export default {
  name: "FieldComponent",
  methods: {
    callUnitRemover(unit) {
      return passportUtil.unitRemover(unit);
    },
  },
  props: {
    icon: { type: [String, Number], default: "mdi-information-outline" },
    label: { type: [String, Number], default: "" },
    img: { type: [String, Number], default: "" },
    value: { type: [String, Number], default: "" },
    subText: { type: [String, Number], default: "" },
    unit: { type: [String, Number], default: "" },
    test: { type: [String, Number], default: "" },
    city: { type: [String, Number], default: "" },
    postal: { type: [String, Number], default: "" },
    country: { type: [String, Number], default: "" },
    phone: { type: [String, Number], default: "" },
    faxNumber: { type: [String, Number], default: "" },
    email: { type: [String, Number], default: "" },
    website: { type: [String, Number], default: "" },
    length: { type: [String, Number], default: "" },
    height: { type: [String, Number], default: "" },
    width: { type: [String, Number], default: "" },
    link: { type: [String, Number], default: "" },
    weight: { type: [String, Number], default: "" },
    day: { type: [String, Number], default: "" },
    month: { type: [String, Number], default: "" },
    year: { type: [String, Number], default: "" },
    tempMin: { type: [String, Number], default: "" },
    tempMax: { type: [String, Number], default: "" },
    tempUnit: { type: [String, Number], default: "" },
    tempRangeMin: { type: [String, Number], default: "" },
    tempRangeMax: { type: [String, Number], default: "" },
    info: { type: Boolean, default: false },
  },
};
</script>

