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
  <div class="id-container">
    <v-btn
      rounded="pill"
      color="#0F71CB"
      size="large"
      class="back-btn"
      href="/"
      variant="outlined"
      style="border: 2px solid; text-transform: initial"
    >
      <v-icon class="icon" start md icon="mdi-arrow-left"></v-icon>
      {{ $t("passportHeader.backBtn") }}
    </v-btn>

    <div class="id-wrapper">
      <p class="id">
        {{ type }}:
        {{ id ? id : "—" }}
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "PassportHeader",
  props: {
    id: {
      type: String,
      default: null,
    },
    type: {
      type: String,
      default: "Passport Id",
    },
  },
};
</script>

<style>
.id-wrapper {
  display: flex;
  justify-content: center;
  width: 100%;
  padding-right: 115px;
  line-break: anywhere;
}

.id {
  font-size: 14px;
  line-height: 36px;
  font-weight: bold;
}
.id-container {
  display: flex;
  align-items: center;
  width: 100%;
  margin: 6em 40px 0 40px;
  padding: 15px 0 10px 0;
}
@media (max-width: 750px) {
  .id-container {
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
  }
  .id-wrapper {
    margin-top: 15px;
  }
}
</style>
