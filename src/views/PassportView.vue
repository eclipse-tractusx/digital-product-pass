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
  <div>
    <HeaderComponent>
      <template v-if="!data">
        <span class="header-title">{{ $t("passportView.dpp") }}</span>
      </template>
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
        "
      >
        <span class="header-title">{{ $t("passportView.bpp") }}</span>
      </template>
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass'
        "
      >
        <span class="header-title">{{ $t("passportView.tpp") }}</span>
      </template>
      <template v-else>
        <span class="header-title">{{ $t("passportView.dpp") }}</span>
      </template>
    </HeaderComponent>
    <v-container v-if="loading">
      <LoadingComponent :id="id" />
    </v-container>
    <v-container v-else-if="error" class="h-100 w-100">
      <div class="d-flex align-items-center w-100 h-100">
        <ErrorComponent
          :title="errorObj.status + ' ' + errorObj.statusText"
          :subTitle="errorObj.title"
          :description="errorObj.description"
          reloadLabel="Return"
          reloadIcon="mdi-arrow-left"
          :reload="errorObj.reload"
        />
      </div>
    </v-container>
    <div v-else>
      <template
        v-if="
          data.semanticId ===
          'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
        "
      >
        <PassportHeader
          :id="data.aspect.batteryIdentification.batteryIDDMCCode"
          type="Battery ID"
        />
      </template>
      <template v-else>
        <PassportHeader :id="id ? id : '-'" type="ID" />
      </template>
      <div class="pass-container">
        <template
          v-if="
            data.semanticId ===
            'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
          "
        >
          <BatteryCards :data="data" />
        </template>
        <template
          v-else-if="
            data.semanticId ==
            'urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass'
          "
        >
          <TransmissionCards :data="data" />
        </template>
        <template v-else>
          <GeneralCards :data="data" />
        </template>
      </div>
      <div class="pass-container footer-spacer">
        <template
          v-if="
            data.semanticId ===
            'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
          "
        >
          <TabsComponent
            :componentsNames="batteryComponentsNames"
            :componentsData="data"
          />
        </template>
        <template v-else>
          <TabsComponent
            :componentsNames="filteredComponentsNames"
            :componentsData="data"
          />
        </template>
      </div>
      <FooterComponent />
    </div>
  </div>
</template>

<script>
// @ is an alias to /src

import LoadingComponent from "../components/general/LoadingComponent.vue";
import TabsComponent from "../components/general/TabsComponent.vue";
import HeaderComponent from "@/components/general/Header.vue";
import PassportHeader from "@/components/passport/PassportHeader.vue";
import BatteryCards from "@/components/passport/BatteryCards.vue";
import TransmissionCards from "@/components/passport/TransmissionCards.vue";
import GeneralCards from "@/components/passport/GeneralCards.vue";
import FooterComponent from "@/components/general/Footer.vue";
import ErrorComponent from "@/components/general/ErrorComponent.vue";
import { AUTO_SIGN, SEARCH_TIMEOUT, NEGOTIATE_TIMEOUT } from "@/services/service.const";
import threadUtil from "@/utils/threadUtil.js";
import jsonUtil from "@/utils/jsonUtil.js";
import configUtil from "@/utils/configUtil.js";
import passportUtil from "@/utils/passportUtil.js";
import BackendService from "@/services/BackendService";
import { inject } from "vue";

export default {
  name: "PassportView",
  components: {
    HeaderComponent,
    FooterComponent,
    PassportHeader,
    BatteryCards,
    LoadingComponent,
    ErrorComponent,
    TabsComponent,
    GeneralCards,
    TransmissionCards,
  },
  data() {
    return {
      batteryComponentsNames: [
        {
          label: "passportView.batteryComponentsNames.generalInformation",
          icon: "mdi-information-outline",
          component: "GeneralInformation",
        },
        {
          label: "passportView.batteryComponentsNames.stateOfBattery",
          icon: "mdi-battery-charging",
          component: "StateOfBattery",
        },
        {
          label: "passportView.batteryComponentsNames.components",
          icon: "mdi-battery-unknown",
          component: "Components",
        },
        {
          label: "passportView.batteryComponentsNames.batteryComposition",
          icon: "mdi-battery-unknown",
          component: "BatteryComposition",
        },
        {
          label: "passportView.batteryComponentsNames.cellChemistry",
          icon: "mdi-flask-empty-outline",
          component: "CellChemistry",
        },
        {
          label:
            "passportView.batteryComponentsNames.electrochemicalProperties",
          icon: "mdi-microscope",
          component: "ElectrochemicalProperties",
        },
        {
          label: "passportView.batteryComponentsNames.documents",
          icon: "mdi-text-box-multiple-outline",
          component: "Documents",
        },
        {
          label: "passportView.batteryComponentsNames.exchange",
          icon: "mdi-file-swap-outline",
          component: "Exchange",
        },
      ],
      auth: inject("authentication"),
      data: null,
      loading: true,
      searchResponse: null,
      errors: [],
      id: this.$route.params.id,
      irsData: [],
      processId: null,
      backendService: null,
      error: true,
      errorObj: {
        title: "Something went wrong while returning the passport!",
        description: "We are sorry for that, you can retry or try again later",
        type: "error",
        status: 500,
        statusText: "Internal Server Error",
        reload: true
      },
    };
  },

  computed: {
    filteredComponentsNames() {
      let dataKeys = Object.keys(this.data.aspect);
      // Check if data exists and is not empty
      if (this.data.aspect && dataKeys.length > 0) {
        dataKeys.splice(3, 0, "components");
        dataKeys.push("exchange");
        // Generate component names dynamically from the JSON keys
        return dataKeys.map((key) => ({
          label: passportUtil.toSentenceCase(
            key[0].toUpperCase() + key.slice(1)
          ),
          icon: passportUtil.iconFinder(key),
          component: key,
        }));
      } else {
        return [];
      }
    }
  },

  async created() {
    this.backendService = new BackendService();
    this.searchContracts();
  },
  methods: {
    async searchContracts() {
      let result = null;
      try {
        // Setup aspect promise
        let passportPromise = this.searchAsset(this.id);
        // Execute promisse with a Timeout
        result = await threadUtil.execWithTimeout(
          passportPromise,
          SEARCH_TIMEOUT,
          null
        );
        if (!result || result == null) {
          this.errorObj.title = "Timeout! Failed to search for the Digital Twin Registry and the Digital Twin!";
          this.errorObj.description =
            "The request took too long... Please retry or try again later.";
          this.status = 408;
          this.statusText = "Request Timeout";
        }
        this.searchResponse = result;
      } catch (e) {
        console.log("searchContracts -> " + e);
      } finally {
        if (
          this.searchResponse &&
          jsonUtil.exists("status", this.searchResponse) &&
          this.searchResponse["status"] == 200 &&
          jsonUtil.exists("data", this.searchResponse) &&
          jsonUtil.exists("contracts", this.searchResponse["data"]) &&
          jsonUtil.exists("token", this.searchResponse["data"]) &&
          jsonUtil.exists("id", this.searchResponse["data"])
        ) {
          this.error = false;
          if(AUTO_SIGN){
            this.resumeNegotiation(this.searchResponse);
          }
        }
        if(this.error || !AUTO_SIGN){
          // Stop loading
          this.loading = false;
        }
      }
    },
    async resumeNegotiation(
      searchResponse,
      contractId = null,
      policyId = null
    ) {
      let result = null;
      let contracts = jsonUtil.get("data.contracts", searchResponse);
      let token = jsonUtil.get("data.token", searchResponse);
      let processId =  jsonUtil.get("data.id", searchResponse);
      // [TODO] Get Contract Information
      try {
        // Setup aspect promise
        let passportPromise = this.negotiatePassport(
          contracts,
          token,
          processId,
          contractId,
          policyId
        );
        // Execute promisse with a Timeout
        result = await threadUtil.execWithTimeout(
          passportPromise,
          NEGOTIATE_TIMEOUT,
          null
        );
        if (!result || result == null) {
          this.errorObj.title =
            "Timeout! Failed to negotiate and return the passport!";
          this.errorObj.description =
            "The request took too long... Please retry or try again later.";
          this.status = 408;
          this.statusText = "Request Timeout";
        }
        this.data = result;
      } catch (e) {
        console.log("passportView -> " + e);
      } finally {
        console.log(this.data)
        if (
          this.data &&
          jsonUtil.exists("status", this.data) &&
          this.data["status"] == 200 &&
          jsonUtil.exists("data", this.data) &&
          jsonUtil.exists("metadata", this.data["data"]) &&
          jsonUtil.exists("aspect", this.data["data"]) &&
          jsonUtil.exists("semanticId", this.data["data"])
        ) {
          this.data = configUtil.normalizePassport(
            jsonUtil.get("data.aspect", this.data),
            jsonUtil.get("data.metadata", this.data),
            jsonUtil.get("data.semanticId", this.data)
          );
          this.error = false;
          this.processId = this.$store.getters.getProcessId; // Get process id from the store
          this.irsData = this.backendService.getIrsData(
            this.processId,
            this.auth
          ); // Return the IRS data
          this.$store.commit("setIrsData", this.irsData); // Save IRS Data
          this.$store.commit(
            "setIrsState",
            this.backendService.getIrsState(this.processId, this.auth)
          );
        }
        // Stop loading
        this.loading = false;
      }
    },
    async searchAsset(id) {
      let response = null;
      // Get Passport in Backend
      try {
        // Init backendService
        // Get access token from IDP
        // Get the aspect for the selected version

        response = await this.backendService.searchAsset(id, this.auth);
      } catch (e) {
        console.log("passportView.getPassport() -> " + e);
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "Failed to return passport";
        this.errorObj.description =
          "It was not possible to transfer the passport.";

        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 500;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Internal Server Error";
        return response;
      }

      //     response = jsonUtil.copy(response, true);

      // Check if the response is empty and give an error
      if (!response) {
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to complete the passport transfer.";
        this.errorObj.status = 400;
        this.errorObj.statusText = "Bad Request";
        return null;
      }
      // Check if reponse content was successfull and if not print error comming message from backend
      if (jsonUtil.exists("status", response) && response["status"] != 200) {
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "An error occured when searching for the passport!";
        this.errorObj.description =
          "An error occured when searching for the passport!";
        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 404;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Not found";
      }

      return response;
    },
    async negotiatePassport(
      contracts,
      token,
      processId,
      contractId = null,
      policyId = null
    ) {
      let response = null;
      // Get Passport in Backend
      try {
        // Init backendService
        // Get access token from IDP
        // Get the aspect for the selected version

        response = await this.backendService.negotiateAsset(
          contracts,
          token,
          processId,
          this.auth,
          contractId,
          policyId
        );
      } catch (e) {
        console.log("passportView.getPassport() -> " + e);
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "Failed to return passport";
        this.errorObj.description =
          "It was not possible to transfer the passport.";

        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 500;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Internal Server Error";
        return response;
      }

      //     response = jsonUtil.copy(response, true);

      // Check if the response is empty and give an error
      if (!response) {
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to complete the passport transfer.";
        this.errorObj.status = 400;
        this.errorObj.statusText = "Bad Request";
        return null;
      }

      // Check if reponse content was successfull and if not print error comming message from backend
      if (jsonUtil.exists("status", response) && response["status"] != 200) {
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "An error occured when searching for the passport!";
        this.errorObj.description =
          "An error occured when searching for the passport!";
        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 404;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Not found";
      }

      return response;
    },
  },
};
</script>

