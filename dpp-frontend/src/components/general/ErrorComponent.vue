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
  <v-container
    class="page-not-found fill-height align-self-center justify-center"
  >
    <v-row class="justify-center">
      <v-col class="v-col-auto justify-center" style="height: fit-content">
        <img src="@/media/404.svg" class="h-100" />
      </v-col>
      <v-col class="v-col-sm-12 v-col-md-6 v-col-lg-6">
        <v-container fluid>
          <template v-if="title">
            <v-row>
              <v-col
                ><span class="title">{{ $t(title) }}</span></v-col
              >
            </v-row>
          </template>
          <template v-if="subTitle">
            <v-row>
              <v-col
                ><span class="subTitle">{{ $t(subTitle) }}</span></v-col
              >
            </v-row>
          </template>
          <template v-if="explanation || description || adminLabel">
            <v-row>
              <v-col
                ><span class="description"
                  ><template v-if="description">{{ $t(description) }}</template> 
                  <template v-if="explanation">{{ $t(explanation) }}</template> 
                  <template v-if="adminLabel"
                    > <a :href="'mailto:' + adminEmail">{{
                      $t(adminLabel)
                    }}</a></template
                  ></span
                ></v-col
              >
            </v-row>
          </template>
          <template v-if="buttonsVisible">
            <v-row class="justify-center">
              <v-col class="v-col-auto">
                <v-btn
                  v-if="reload"
                  size="large"
                  class="btn"
                  rounded="pill"
                  color="primary"
                  @click="$router.go()"
                  prepend-icon="mdi-refresh"
                  >Try Again</v-btn
                >
                <v-btn
                  v-else
                  size="large"
                  class="btn"
                  rounded="pill"
                  color="primary"
                  :prepend-icon="reloadIcon"
                  @click="$router.go(back)"
                  >{{ $t(reloadLabel) }}</v-btn
                >
              </v-col>
              <v-col class="v-col-auto"
                ><v-btn
                  size="large"
                  class="btn"
                  rounded="pill"
                  variant="outlined"
                  :href="homepage"
                  >{{ $t("Home") }}</v-btn
                ></v-col
              >
            </v-row>
          </template>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { ADMIN_EMAIL } from "@/services/service.const";
export default {
  name: "ErrorComponent",
  props: {
    title: {
      type: [String, Number],
      default: "404 Not Found",
    },
    subTitle: {
      type: [String, Number],
      default: "Oops, Something went wrong...",
    },
    description: {
      type: Number,
      default: null,
    },
    explanation: {
      type: String,
      default:
        "The server encountered an internal error or misconfiguration and was unable to complete your request.",
    },
    adminLabel: {
      type: String,
      default: "Please contact your admin",
    },
    adminEmail: {
      type: String,
      default: ADMIN_EMAIL,
    },
    buttonsVisible: {
      type: Boolean,
      default: true,
    },
    back: {
      type: [String, Number],
      default: -1,
    },
    homepage: {
      type: String,
      default: "/",
    },
    reloadLabel: {
      type: String,
      default: "Reload Page",
    },
    reloadIcon: {
      type: String,
      default: null,
    },
    reload: {
      type: Boolean,
      default: false,
    },
  },
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>

