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
          <v-row>
            <v-col><span class="title">Something went wrong</span></v-col>
          </v-row>
          <v-row>
            <v-col
              ><span class="subTitle"
                >We are sorry but this application is not available.</span
              ></v-col
            >
          </v-row>
          <v-row>
            <v-col>
              <span class="description">
                This Digital Product Pass application is experiencing complications to initialize, probably the authentication service is not available or some core configuration is missing.
                If you are experiencing problems try again later or contact with the admin from this application: <a target="_blank" :href="'mailto:'+adminEmail">{{adminEmail}}</a>
              </span>
            </v-col>
          </v-row>
          <v-row class="justify-center">
            <v-col class="v-col-auto"
              ><v-btn
                size="large"
                class="btn"
                rounded="pill"
                variant="outlined"
                @click="$router.go()"
                prepend-icon="mdi-refresh"
                >Reload Page</v-btn
              ></v-col
            >
          </v-row>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

import { ADMIN_EMAIL } from "@/services/service.const";
export default {
  name: "NotAuthorizedComponent",
  data() {
    return {
      adminEmail: ADMIN_EMAIL
    };
  },
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>
