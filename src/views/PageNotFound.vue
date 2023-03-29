<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

<template>
  <div class="PageNotFound">
    <v-container>
      <v-row>
        <v-col>
        </v-col>
        <v-col>
          <v-container>
            <v-row>
              <v-col></v-col>
            </v-row>
            <v-row>
              <v-col></v-col>
            </v-row>
            <v-row>
              <v-col></v-col>
            </v-row>
          </v-container>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "PageNotFound",
  props: {
    title: {
      type: [String,Number],
      default: "404 Not Found",
    },
    subTitle:{
      type: [String,Number],
      default: "Oops, Something went wrong"
    },
    description: {
      type: [String,Number],
      default: "The server encountered an internal error or misconfiguration and was unable to complete your request."
    },
    adminLink:{
      type: String,
      default: "m"
    }
  }
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>
