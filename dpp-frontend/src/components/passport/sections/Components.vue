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

<template v-if="propsData">
  <div class="section">
    <v-container class="ma-0">
      <template v-if="loading || infoBar">
        <div class="info-bar" :style="`background-color: ${infoColor}`">
          {{ $t(infoBarMessage) }}
        </div>
      </template>
      <RecursiveTree
        :treeData="irsData.data"
        :loading="loading"
        :status="status"
        :infoColor="infoColor"
      />
    </v-container>
  </div>
</template>

<script>
import RecursiveTree from "@/components/general/RecursiveTree.vue";
import { mapState } from "vuex";
import BackendService from "@/services/BackendService";
import { inject } from "vue";
import store from "@/store/index";
import threadUtil from "@/utils/threadUtil";
import { IRS_DELAY, IRS_MAX_WAITING_TIME } from "@/services/service.const";

export default {
  name: "BatteryComposition",
  components: {
    RecursiveTree,
  },
  data() {
    return {
      auth: inject("authentication"),
      loading: false,
      infoBar: false,
      infoBarMessage: "sections.components.searchForChild",
      infoColor: "#0F71CB",
      backendService: null,
      status: 204,
    };
  },
  computed: {
    ...mapState(["irsData", "processId"]),
  },
  async created() {
    this.backendService = new BackendService();
    this.loading = true;
    this.invokeIrsData();
    this.invokeIrsState();
  },
  methods: {
    async invokeIrsData() {
      try {
        let response = await this.backendService.getIrsData(
          this.processId,
          this.auth
        );

        store.commit("setIrsData", response);
      } catch (error) {
        console.error("API call failed:", error);
      }
    },
    async invokeIrsState() {
      try {
        let response = await this.backendService.getIrsState(
          this.processId,
          this.auth
        );

        const created = new Date();
        let currentTime = new Date();
        let timediff = Math.floor(
          (currentTime.getTime() - created.getTime()) / 1000 / 60
        );
        this.status = response.status;
        this.infoBar = true;
        while (response.status == 204 && timediff < IRS_MAX_WAITING_TIME) {
          await threadUtil.sleep(IRS_DELAY);
          response = await this.backendService.getIrsState(
            this.processId,
            this.auth
          );
          this.status = response.status;
          if (response.status != 204) {
            break;
          }
          currentTime = new Date();
          timediff = Math.floor(
            (currentTime.getTime() - created.getTime()) / 1000 / 60
          );
        }
        this.loading = false;
        if (response.status == 200) {
          this.invokeIrsData();
          this.infoBar = false;
        } else if (response.status == 404) {
          this.infoBarMessage = "sections.components.noChild";
          this.infoColor = "#FFA000";
        } else {
          this.infoBarMessage = "sections.components.somethingWentWrong";
          this.infoColor = "#d32f2f";
          this.status = 500;
        }
      } catch (error) {
        console.error("API call failed:", error);
        this.infoBarMessage = "sections.components.somethingWentWrong";
        this.infoColor = "#d32f2f";
        this.status = 500;
      }
    },
  },
};
</script>

