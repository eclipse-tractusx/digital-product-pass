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
  <v-container v-if="loading">
    <div class="loading-container">
      <Spinner class="spinner-container" />
    </div>
  </v-container>
  <v-container v-else-if="error" class="h-100 w-100">
    <div class="loading-container d-flex align-items-center w-100 h-100">
      <Alert
        class="w-100"
        :description="errorObj.description"
        :title="errorObj.title"
        :type="errorObj.type"
        icon="mdi-alert-circle-outline"
        :closable="false"
        variant="outlined"
      >
        <v-row class="justify-space-between mt-3">
          <v-col class="v-col-auto">
            Click in the <strong>"return"</strong> button to go back to the
            search field
          </v-col>
          <v-col class="v-col-auto">
            <v-btn
              style="color: white !important"
              rounded="pill"
              color="#0F71CB"
              size="large"
              class="submit-btn"
              @click="$router.go(-1)"
            >
              <v-icon class="icon" start md icon="mdi-arrow-left"></v-icon>
              Return
            </v-btn>
          </v-col>
        </v-row>
      </Alert>
    </div>
  </v-container>
  <div v-else>
    <HeaderComponent />
    <PassportHeader
      :id="data.data.passport.batteryIdentification.batteryIDDMCCode"
      type="BatteryID"
    />
    <div class="pass-container">
      <div v-for="(section, index) in componentsNames" :key="index">
        <SectionComponent :title="`${index + 1}. ${section.label}`">
          <component :is="section.component" :data="data" />
        </SectionComponent>
      </div>
    </div>
    <FooterComponent />
  </div>
</template>



<script>
// @ is an alias to /src
import GeneralInformation from "@/components/passport/sections/GeneralInformation.vue";
import CellChemistry from "@/components/passport/sections/CellChemistry.vue";
import ElectrochemicalProperties from "@/components/passport/sections/ElectrochemicalProperties.vue";
import BatteryComposition from "@/components/passport/sections/BatteryComposition.vue";
import StateOfBattery from "@/components/passport/sections/StateOfBattery.vue";
import Documents from "@/components/passport/sections/Documents.vue";
import ContractInformation from "@/components/passport/sections/ContractInformation.vue";
import Spinner from "@/components/general/Spinner.vue";
import HeaderComponent from "@/components/general/Header.vue";
import PassportHeader from "@/components/passport/PassportHeader.vue";
import Alert from "@/components/general/Alert.vue";
import FooterComponent from "@/components/general/Footer.vue";
import { API_TIMEOUT, PASSPORT_VERSION } from "@/services/service.const";
import threadUtil from "@/utils/threadUtil.js";
import jsonUtil from "@/utils/jsonUtil.js";
import BackendService from "@/services/BackendService";
import { inject } from "vue";
import SectionComponent from "@/components/passport/Section.vue";

export default {
  name: "PassportView",
  components: {
    HeaderComponent,
    GeneralInformation,
    PassportHeader,
    CellChemistry,
    StateOfBattery,
    ElectrochemicalProperties,
    BatteryComposition,
    Documents,
    ContractInformation,
    FooterComponent,
    Spinner,
    Alert,
    SectionComponent,
  },
  data() {
    return {
      componentsNames: [
        {
          label: "General information",
          component: "GeneralInformation",
        },
        {
          label: "Cell chemistry",
          component: "CellChemistry",
        },
        {
          label: "Electrochemical properties",
          component: "ElectrochemicalProperties",
        },
        {
          label: "Battery composition",
          component: "BatteryComposition",
        },
        {
          label: "State of battery",
          component: "StateOfBattery",
        },
        {
          label: "Additional information",
          component: "Documents",
        },
        {
          label: "Contract information",
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
      },
      version: PASSPORT_VERSION,
    };
  },
  async created() {
    let result = null;
    try {
      // Setup passport promise
      let passportPromise = this.getPassport(this.id);
      // Execute promisse with a Timeout
      result = await threadUtil.execWithTimeout(
        passportPromise,
        API_TIMEOUT,
        null
      );
      if(!result || result == null){
        this.errorObj.title = "Timeout! Failed to return passport!";
        this.errorObj.description = "The request took too long... Please retry or try again later."
      }
      this.data = result;
    } catch (e) {
      console.log("passportView -> " + e);
    } finally {
      if (
        this.data && jsonUtil.exists("status", this.data) && this.data["status"] == 200
      ) {
        this.error = false;
      }
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
        let jwtToken = await this.auth.getAccessToken();
        // Get the passport for the selected version
        response = await backendService.getPassport(this.version, id, jwtToken);
      } catch (e) {
        console.log("passportView.getPassport() -> " + e);
        this.errorObj.title = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Failed to return passport";
        this.errorObj.description = jsonUtil.exists("message", response)
          ? response["message"]
          : "It was not possible to transfer the passport.";
        return response;
      }

      response = jsonUtil.copy(response, true);

      // Check if the response is empty and give an error
      if (!response) {
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to complete the passport transfer.";
        return null;
      }

      // Check if reponse content was successfull and if not print error comming message from backend
      if (
        jsonUtil.exists("status", response) && 
        response["status"] != 200
      ) {
        this.errorObj.title = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "An error occured when searching for the passport!";
        this.errorObj.description = jsonUtil.exists("message", response)
          ? response["message"]
          : "It was not possible to retrieve the passport";
      }

      return response;
    },
  },
};
</script>

<style>
.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
}
.pass-container {
  width: 76%;
  margin: 0 12% 0 12%;
}
.spinner-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.spinner {
  margin: auto;
  width: 8vh;
  animation: rotate 3s infinite;
}
@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}
@media (max-width: 750px) {
  .pass-container {
    width: 100%;
    margin: 0;
  }
}
</style>
