<template>
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <Header :battery-id="data.generalInformation" />
    <div class="pass-container">
      <GeneralInformation
        section-title="General information"
        :general-information="data.generalInformation"
      />
      <BatteryComposition
        section-title="Battery Composition"
        :battery-composition="data.batteryComposition"
      />
      <StateOfHealth
        section-title="State of Health"
        :state-of-health="data.stateOfHealth"
      />
      <ParametersOfTheBattery
        section-title="Parameters of The Battery"
        :parameters-of-the-battery="data.parametersOfTheBattery"
      />
      <DismantlingProcedures
        section-title="Dismantling procedures"
        :dismantling-procedures="data.dismantlingProcedures"
      />
      <SafetyInformation
        section-title="Safety information"
        :safety-measures="data.safetyMeasures"
      />
      <InformationResponsibleSourcing
        section-title="Information responsible sourcing"
        :information-responsible-sourcing="data.informationResponsibleSourcing"
      />
      <AdditionalInformation
        section-title="Additional information"
        :additional-information="data.additionalInformation"
      />
    </div>
    <Footer />
  </div>
</template>

<script>
// @ is an alias to /src
import GeneralInformation from "@/components/GeneralInformation.vue";
import BatteryComposition from "@/components/BatteryComposition.vue";
import StateOfHealth from "@/components/StateOfHealth.vue";
import ParametersOfTheBattery from "@/components/ParametersOfTheBattery.vue";
import DismantlingProcedures from "@/components/DismantlingProcedures.vue";
import SafetyInformation from "@/components/SafetyInformation.vue";
import InformationResponsibleSourcing from "@/components/InformationResponsibleSourcing.vue";
import AdditionalInformation from "@/components/AdditionalInformation.vue";
import Spinner from "@/components/Spinner.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";
import { AAS_PROXY_URL, API_KEY } from "@/services/service.const";
import apiWrapper from "@/services/wrapper";
import AAS from "@/services/aasServices";
import { inject } from "vue";

export default {
  name: "PassportView",
  components: {
    Header,
    GeneralInformation,
    BatteryComposition,
    StateOfHealth,
    ParametersOfTheBattery,
    DismantlingProcedures,
    SafetyInformation,
    InformationResponsibleSourcing,
    AdditionalInformation,
    Footer,
    Spinner,
  },

  data() {
    return {
      auth: inject("authentication"),
      data: null,
      loading: true,
      errors: [],
    };
  },
  async created() {
    let assetIds = this.$route.params.assetIds;
    this.data = await this.getPassport(assetIds);
    this.loading = false;
  },
  methods: {
    async getPassport(assetIds) {
      let aas = new AAS();
      let wrapper = new apiWrapper();
      console.log("API Key: "+ API_KEY);
      let accessToken = await this.auth.getAuthTokenForTechnicalUser();
      let AASRequestHeader ={
        "Authorization" : "Bearer " + accessToken
      };
    
      const shellId = await aas.getAasShellId(assetIds, AASRequestHeader);
      const shellDescriptor = await aas.getShellDescriptor(shellId[0], AASRequestHeader);
      const subModel = await aas.getSubmodelDescriptor(shellDescriptor, AASRequestHeader);
      if (subModel.endpoints.length > 0){
        let providerConnector={
          "connectorAddress": subModel.endpoints[0].protocolInformation.endpointAddress,
          "idShort": subModel.idShort
        };
        let APIWrapperRequestHeader={
          'x-api-key': API_KEY
        };

        let assetId = JSON.parse(assetIds)[1].value; // Two elements in json array [batteryIDDMCode, assetId], get the last element and it wll always be the asset id i.e., [1]
        console.info('Selected asset Id: ' + assetId);
        const response = await wrapper.performEDCDataTransfer(assetId, providerConnector,APIWrapperRequestHeader);
        return response;
      }
      else
        alert("There is no connector endpoint defined in submodel.. Could not proceed further!");
    },
  },
};
</script>

<style>
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
