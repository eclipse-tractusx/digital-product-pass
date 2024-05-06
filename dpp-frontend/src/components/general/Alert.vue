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
  <v-alert
    :closable="closable"
    :icon="icon"
    :title="title"
    :text="description"
    :type="type"
    :color="color"
    :variant="variant"
  >
    <slot></slot>
  </v-alert>
</template>

<script>
export default {
  name: "AlertComponent",
  props: {
    title: {
      type: String,
      default: "Alert",
    },
    description: {
      type: String,
      default: "Alert Description",
    },
    type: {
      type: String,
      default: "info",
    },
    icon: {
      type: String,
      optional: true,
      default: "info",
    },
    color: {
      type: String,
      optional: true,
      default: null,
    },
    closable: {
      type: Boolean,
      optional: true,
      default: true,
    },
    variant: {
      type: String,
      optional: true,
      default: null,
    },
  },
};
</script>

<style>
.v-alert__content {
  color: black !important;
}
.v-alert--variant-outlined {
  border: 1px solid #c1c1c157 !important;
  background: white !important;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.06), 0px 7px 9px rgba(0, 0, 0, 0.12),
    0px 20px 25px -8px rgba(0, 0, 0, 0.18);
}
</style>
