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
        <span class="header-title">Digital Product Passport</span>
      </template>
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
        "
      >
        <span class="header-title">Battery Product Passport</span>
      </template>
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.transmission:3.0.1#Transmission'
        "
      >
        <span class="header-title">Transmission Product Passport</span>
      </template>
      <template v-else>
        <span class="header-title">Digital Product Passport</span>
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
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.transmission:3.0.1#Transmission'
        "
      >
        <PassportHeader
          :id="data.aspect.batteryIdentification.batteryIDDMCCode"
          type="Transmission ID"
        />
      </template>
      <template v-else>
        <PassportHeader
          :id="
            data.aspect &&
            data.aspect.identification &&
            data.aspect.identification.gtin
              ? data.aspect.identification.gtin
              : '-'
          "
          type="Passport ID"
        />
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
            data.semanticId ===
            'urn:bamm:io.catenax.transmission:3.0.1#Transmission'
          "
        >
          <BatteryCards :data="data" />
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
        <template
          v-else-if="
            data.semanticId ===
            'urn:bamm:io.catenax.transmission:3.0.1#Transmission'
          "
        >
          <TabsComponent
            :componentsNames="batteryComponentsNames"
            :componentsData="data"
          />
        </template>
        <template v-else>
          <TabsComponent
            :componentsNames="generalComponentsNames"
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
import GeneralCards from "@/components/passport/GeneralCards.vue";
import FooterComponent from "@/components/general/Footer.vue";
import ErrorComponent from "@/components/general/ErrorComponent.vue";
import { API_TIMEOUT, PASSPORT_VERSION } from "@/services/service.const";
import threadUtil from "@/utils/threadUtil.js";
import jsonUtil from "@/utils/jsonUtil.js";
import configUtil from "@/utils/configUtil.js";
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
  },
  data() {
    return {
      generalComponentsNames: [
        {
          label: "Serialization",
          icon: "mdi-information-outline",
          component: "Serialization",
        },
        {
          label: "Typology",
          icon: "mdi-information-outline",
          component: "Typology",
        },
        {
          label: "Metadata",
          icon: "mdi-information-outline",
          component: "MetadataComponent",
        },
        {
          label: "Characteristics",
          icon: "mdi-information-outline",
          component: "Characteristics",
        },
        {
          label: "Commercial",
          icon: "mdi-information-outline",
          component: "Commercial",
        },
        {
          label: "Identification",
          icon: "mdi-information-outline",
          component: "Identification",
        },
        {
          label: "Sources",
          icon: "mdi-information-outline",
          component: "Sources",
        },
        {
          label: "Handling",
          icon: "mdi-information-outline",
          component: "Handling",
        },
        {
          label: "Additional data",
          icon: "mdi-information-outline",
          component: "AdditionalData",
        },
        {
          label: "Sustainability",
          icon: "mdi-information-outline",
          component: "Sustainability",
        },
        {
          label: "Operation",
          icon: "mdi-information-outline",
          component: "Operation",
        },
      ],
      batteryComponentsNames: [
        {
          label: "General Information",
          icon: "mdi-information-outline",
          component: "GeneralInformation",
        },
        {
          label: "Product Condition",
          icon: "mdi-battery-charging",
          component: "StateOfBattery",
        },
        {
          label: "Composition",
          icon: "mdi-battery-unknown",
          component: "BatteryComposition",
        },
        {
          label: "Cell chemistry",
          icon: "mdi-flask-empty-outline",
          component: "CellChemistry",
        },
        {
          label: "Electrochemical properties",
          icon: "mdi-microscope",
          component: "ElectrochemicalProperties",
        },
        {
          label: "Additional information",
          icon: "mdi-text-box-multiple-outline",
          component: "Documents",
        },
        {
          label: "Data exchange information",
          icon: "mdi-file-swap-outline",
          component: "ContractInformation",
        },
      ],
      auth: inject("authentication"),
      data: null,
      loading: true,
      errors: [],
      id: this.$route.params.id,
      error: true,
      errorObj: {
        title: "Something went wrong while returning the passport!",
        description: "We are sorry for that, you can retry or try again later",
        type: "error",
        status: 500,
        statusText: "Internal Server Error",
      },
      version: PASSPORT_VERSION,
    };
  },

  async created() {
    let result = null;
    try {
      // Setup aspect promise
      let passportPromise = this.getPassport(this.id);
      // Execute promisse with a Timeout
      result = await threadUtil.execWithTimeout(
        passportPromise,
        API_TIMEOUT,
        null
      );
      if (!result || result == null) {
        this.errorObj.title = "Timeout! Failed to return passport!";
        this.errorObj.description =
          "The request took too long... Please retry or try again later.";
        this.status = 408;
        this.statusText = "Request Timeout";
      }
      this.data = result;
      console.log(this.data);
    } catch (e) {
      console.log("passportView -> " + e);
    } finally {
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
      }
      console.log(this.data);
      // Stop loading
      this.loading = false;
    }
  },
  methods: {
    async getPassport(id) {
      let response = null;
      // Get Passport in Backend
      try {
        // Init backendService
        let backendService = new BackendService();
        // Get access token from IDP
        // Get the aspect for the selected version
        response = await backendService.getPassport(
          this.version,
          id,
          this.auth
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

