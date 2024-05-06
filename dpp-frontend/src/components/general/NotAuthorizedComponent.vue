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
            <v-col><span class="title">Not Authorized</span></v-col>
          </v-row>
          <v-row>
            <v-col
              ><span class="subTitle"
                >You are not authorized to access this Digital Product Pass
                Application!</span
              ></v-col
            >
          </v-row>
          <v-row>
            <v-col>
              <span class="description">
                This application is restricted for users from a specific
                company. If you have not the permission to login may be because
                your user is not from the specific company or does not contain
                the necessary credentials to access the app. We apologize for
                the inconveniences.
              </span>
            </v-col>
          </v-row>
          <v-row class="justify-center">
            <v-col class="v-col-auto"
              ><v-btn
                size="large"
                class="btn"
                rounded="pill"
                color="primary"
                prepend-icon="mdi-logout"
                @click="auth.logout()"
                >Logout</v-btn
              ></v-col
            >
            <v-col class="v-col-auto"
              ><v-btn
                size="large"
                class="btn"
                rounded="pill"
                variant="outlined"
                target="_blank"
                :href="portalUrl"
                prepend-icon="mdi-home"
                >Go back to Portal</v-btn
              ></v-col
            >
          </v-row>
        </v-container>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { inject } from "vue";

import { PORTAL_URL } from "@/services/service.const";
export default {
  name: "NotAuthorizedComponent",
  data() {
    return {
      auth: inject("authentication"),
      portalUrl: PORTAL_URL
    };
  },
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>
