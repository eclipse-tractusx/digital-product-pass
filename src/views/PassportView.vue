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
      <Alert class="w-100" :description="errorObj.description" :title="errorObj.title" :type="errorObj.type" icon="mdi-alert-circle-outline" :closable="false" variant="outlined">
        
          <v-row class="justify-space-between mt-3">
            <v-col class="v-col-auto">
              Click in the <strong>"return"</strong> button to go back to the search field
            </v-col>
            <v-col class="v-col-auto">
                <v-btn
                    style="color:white!important"
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
    <Header />
    <PassportHeader :id="data.data.passport.batteryIdentification.batteryIDDMCCode" type="BatteryID" />
    <div class="pass-container">
      <GeneralInformation
        section-title="General information"
        :general-information="data.data.passport"
      />
      <CellChemistry
        section-title="Cell chemistry"
        :cell-chemistry="data.data.passport.cellChemistry"
      />
      <ElectrochemicalProperties
        section-title="State of Health"
        :electrochemical-properties="
          data.data.passport.electrochemicalProperties
        "
      />
      <BatteryComposition
        section-title="Parameters of The Battery"
        :battery-composition="data.data.passport.composition"
      />
      <StateOfBattery
        section-title="State of Battery"
        :state-of-battery="data.data.passport"
      />

      <Documents
        section-title="Documents"
        :documents="data.data.passport.document"
      />
      <ContractInformation
        section-title="Contract Information"
        :contract-information="data.data.metadata"
      />
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
import Header from "@/components/general/Header.vue";
import PassportHeader from "@/components/passport/PassportHeader.vue";
import Alert from "@/components/general/Alert.vue";
import Footer from "@/components/general/Footer.vue";
import { API_KEY, API_TIMEOUT } from "@/services/service.const";
import threadUtil from "@/utils/threadUtil.js";
import apiWrapper from "@/services/Wrapper";
import AAS from "@/services/AasServices";
import { inject } from "vue";
export default {
  name: "PassportView",
  components: {
    Header,
    GeneralInformation,
    PassportHeader,
    CellChemistry,
    StateOfBattery,
    ElectrochemicalProperties,
    BatteryComposition,
    Documents,
    ContractInformation,
    Footer,
    Spinner,
    Alert,
  },
  data() {
    return {
      auth: inject("authentication"),
      data: null,
      loading: true,
      errors: [],
      passId: this.$route.params.id,
      error:false,
      errorObj: {
        "title": "",
        "description": "",
        "type": "error"
      }
    };
  },
  async created() {
    try{
      let passportPromise = this.getPassport(this.passId);
      const result = await threadUtil.execWithTimeout(passportPromise, API_TIMEOUT, null);
      if(result && result != null){
        this.data = result;
      }else{
        this.loading = false;
        this.error = true;
        this.errorObj.title = "Timeout! Failed to return passport!";
        this.errorObj.description = "We are sorry, it took too long to retrieve the passport.";
      }
    }catch(e){
      this.loading = false;
      this.error = true;
      this.errorObj.title = "Failed to return passport!";
      this.errorObj.description = "We are sorry, it was not posible to retrieve the passport.";
    }finally{
      this.loading = false;
    }
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
      var shellId,shellDescriptor,subModel = null;

      try{
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
      }catch(e){
        this.loading = false;
        this.error = true;
        this.errorObj.title = "We are sorry, the searched ID was not found!";
        this.errorObj.description = "It was not possible to find the searched ID ["+this.passId+"] in the Digital Twin Registry";
        return null;
      }
      if (subModel.endpoints.length < 0){
        this.loading = false;
        this.error = true;
        this.errorObj.title = "We are sorry, the searched ID was not found!";
        this.errorObj.description = "It was not possible to find the searched ID ["+this.passId+"] in the Digital Twin Registry, it might not be registered";
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
      try{
        response = await wrapper.performEDCDataTransfer(
          assetId,
          providerConnector,
          APIWrapperRequestHeader
        );
      }catch(e){
        this.loading = false;
        this.error = true;
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description = "It was not possible to transfer the passport.";
        return null;
      };

      if(response == null || typeof response == "string" || typeof response.data.passport != "object" || response.data.passport==null || response.data.passport.errors != null){
        this.loading = false;
        this.error = true;
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description = "It was not possible to complete the passport transfer.";
        return null;
      }
      this.contractInformation = providerConnector;
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
