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
  <div>
    <div @click="dialog = true">
      <slot></slot>
    </div>
    <v-dialog v-model="dialog">
      <v-card class="dialog-container">
        <v-card-title class="title-container">
          <v-icon start size="small" icon="mdi-information-outline"> </v-icon>
          <slot name="title"> {{ $t("dialog.description") }} </slot>
          <v-card-actions class="close-btn-container">
            <v-btn class="close-btn-container" block @click="dialog = false">
              <v-icon start md icon="mdi-close"></v-icon
            ></v-btn>
          </v-card-actions>
        </v-card-title>
        <v-divider></v-divider>
        <v-card-text class="dialog-text">
          <slot name="text">
            <h2>404</h2>
            <p>{{ $t("dialog.noDescription") }}</p>
          </slot>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: "DialogComponent",
  data() {
    return {
      dialog: false,
    };
  },
};
</script>
