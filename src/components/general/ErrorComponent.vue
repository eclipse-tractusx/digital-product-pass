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
    <v-container class="page-not-found fill-height align-self-center justify-center">
      <v-row class="justify-center">
        <v-col class="v-col-auto justify-center" style="height: fit-content">
          <img src="@/media/404.svg" class="h-100"> 
        </v-col>
        <v-col class="v-col-sm-12 v-col-md-6 v-col-lg-6">
          <v-container fluid>
            <template v-if="title">
              <v-row>
                <v-col><span class="title">{{ title }}</span></v-col>
              </v-row>
            </template>
            <template v-if="subTitle">
              <v-row>
                <v-col><span class="subTitle">{{ subTitle }}</span></v-col>
              </v-row>
            </template>
            <template v-if="explanation || description || adminLabel">
              <v-row>
                <v-col><span class="description"><template v-if="description">{{ description }}</template> <template v-if="explanation">{{ explanation }}</template> <template v-if="adminLabel"><a :href="'mailto:'+adminEmail">{{adminLabel}}</a></template></span></v-col>
              </v-row>
            </template>
            <template v-if="buttonsVisible">
              <v-row class="justify-center">
                <v-col class="v-col-auto"><v-btn size="large" class="btn" rounded="pill" color="primary" :prepend-icon="reloadIcon" @click="$router.go(back)">{{reloadLabel}}</v-btn></v-col>
                <v-col class="v-col-auto"><v-btn size="large" class="btn" rounded="pill" variant="outlined" :href="homepage">Homepage</v-btn></v-col>
              </v-row>
            </template>
          </v-container>
        </v-col>
      </v-row>
    </v-container>
</template>

<script>

export default {
  name: "ErrorComponent",
  props: {
    title: {
      type: [String,Number],
      default: "404 Not Found",
    },
    subTitle:{
      type: [String,Number],
      default: "Oops, Something went wrong..."
    },
    description: {
      type: Number,
      default: null
    },
    explanation: {
      type: String,
      default: "The server encountered an internal error or misconfiguration and was unable to complete your request."
    },
    adminLabel:{
      type: String,
      default: "Please contact your admin"
    },
    adminEmail:{
      type: String,
      default: "admin@example.com",
    },
    buttonsVisible:{
      type: Boolean,
      default: true
    },
    back:{
      type: [String, Number],
      default: -1
    },
    homepage:{
      type: String,
      default: "/"
    },
    reloadLabel:{
      type: String, 
      default: "Reload Page"
    },
    reloadIcon:{
      type: String,
      default: null
    }
  }
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>
