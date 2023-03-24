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
  <div class="pass-container-bg" v-else>
    <HeaderComponent>
      <span class="header-title">Battery passport</span>
    </HeaderComponent>
    <PassportHeader
      :id="data.data.passport.batteryIdentification.batteryIDDMCCode"
      type="BatteryID"
    />
    <div class="pass-container">
      <CardsComponent :data="data" />
    </div>

    <div class="pass-container">
      <v-card>
        <v-tabs v-model="tab" center-active show-arrows class="menu">
          <v-tab
            v-for="(section, index) in componentsNames"
            :key="index"
            :value="section.component"
          >
            <v-icon start md :icon="section.icon"> </v-icon>
            {{ section.label }}</v-tab
          >
        </v-tabs>
        <v-card-text>
          <v-window v-model="tab">
            <v-window-item
              v-for="(section, index) in componentsNames"
              :key="index"
              :value="section.component"
            >
              <component :is="section.component" :data="data" />
            </v-window-item>
          </v-window>
        </v-card-text>
      </v-card>
    </div>
    <Footer />
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
import HeaderComponent from "../components/general/Header.vue";
import PassportHeader from "@/components/passport/PassportHeader.vue";
import CardsComponent from "@/components/passport/Cards.vue";
import Alert from "@/components/general/Alert.vue";
import Footer from "@/components/general/Footer.vue";
import { API_KEY, API_TIMEOUT, BACKEND } from "@/services/service.const";
import threadUtil from "@/utils/threadUtil.js";
import apiWrapper from "@/services/Wrapper";
import AAS from "@/services/AasServices";
import BackendService from "@/services/BackendService";
import { inject } from "vue";
import SectionComponent from "@/components/passport/Section.vue";
import MOCK_DATA from "../assets/MOCK/passportExample02.json";

export default {
  name: "PassportView",
  components: {
    HeaderComponent,
    GeneralInformation,
    PassportHeader,
    CardsComponent,
    CellChemistry,
    StateOfBattery,
    ElectrochemicalProperties,
    BatteryComposition,
    Documents,
    ContractInformation,
    Footer,
    Spinner,
    Alert,
    SectionComponent,
  },
  data() {
    return {
      tab: null,

      componentsNames: [
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
      passId: this.$route.params.id,
      error: false,
      errorObj: {
        title: "",
        description: "",
        type: "error",
      },
      backend: BACKEND,
    };
  },
  async created() {
    this.loading = false;
    this.data = MOCK_DATA;

    // try {
    //   let passportPromise = this.getPassport(this.passId);
    //   const result = await threadUtil.execWithTimeout(
    //     passportPromise,
    //     API_TIMEOUT,
    //     null
    //   );
    //   if (result && result != null) {
    //     this.data = result;
    //   } else {
    //     this.error = true;
    //     if (this.errorObj.title == null) {
    //       this.errorObj.title = "Timeout! Failed to return passport!";
    //     }
    //     if (this.errorObj.description == null) {
    //       this.errorObj.description =
    //         "We are sorry, it took too long to retrieve the passport.";
    //     }
    //   }
    // } catch (e) {
    //   this.error = true;
    //   this.errorObj.title = "Failed to return passport!";
    //   this.errorObj.description =
    //     "We are sorry, it was not posible to retrieve the passport.";
    // } finally {
    //   this.loading = false;
    // }
  },
  methods: {
    async getPassport(assetId) {
      let assetIdJson = [{ key: "Battery_ID_DMC_Code", value: assetId }];
      let aas = new AAS();
      let wrapper = new apiWrapper();
      let accessToken = await this.auth.getAuthTokenForTechnicalUser();
      let AASRequestHeader = {
        Authorization: "Bearer " + accessToken,
      };
      var shellId,
        shellDescriptor,
        subModel = null;

      try {
        shellId = await aas.getAasShellId(
          JSON.stringify(assetIdJson),
          AASRequestHeader
        );
        shellDescriptor = await aas.getShellDescriptor(
          shellId[0],
          AASRequestHeader
        );
        subModel = await aas.getSubmodelDescriptor(
          shellDescriptor,
          AASRequestHeader
        );
      } catch (e) {
        this.loading = false;
        this.error = true;
        this.errorObj.title = "We are sorry, the searched ID was not found!";
        this.errorObj.description =
          "It was not possible to find the searched ID [" +
          this.passId +
          "] in the Digital Twin Registry";
        return null;
      }
      if (subModel.endpoints.length < 0) {
        this.loading = false;
        this.error = true;
        this.errorObj.title = "We are sorry, the searched ID was not found!";
        this.errorObj.description =
          "It was not possible to find the searched ID [" +
          this.passId +
          "] in the Digital Twin Registry, it might not be registered";
        return null;
      }
      let providerConnector = {
        connectorAddress:
          subModel.endpoints[0].protocolInformation.endpointAddress,
        idShort: subModel.idShort,
      };
      let APIWrapperRequestHeader = {
        "x-api-key": API_KEY,
      };
      console.info("Selected asset Id: " + assetId);
      var response = null;
      try {
        if (this.backend === "true" || this.backend == true) {
          let backendService = new BackendService();
          let jwtToken = await this.auth.getAccessToken();
          response = await backendService.getPassportV1(assetId, jwtToken);
        } else {
          response = await wrapper.performEDCDataTransfer(
            assetId,
            providerConnector,
            APIWrapperRequestHeader
          );
        }
      } catch (e) {
        this.loading = false;
        this.error = true;
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to transfer the passport.";
        return null;
      }

      if (
        response == null ||
        typeof response == "string" ||
        typeof response.data.passport != "object" ||
        response.data.passport == null ||
        response.data.passport.errors != null
      ) {
        this.loading = false;
        this.error = true;
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to complete the passport transfer.";
        return null;
      }
      this.contractInformation = providerConnector;
      return response;
    },
  },
};
</script>
