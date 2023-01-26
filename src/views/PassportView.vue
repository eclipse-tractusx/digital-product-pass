
<template>
  <v-container v-if="loading">
    <v-alert
      v-if="data.status !== 200"
      closable
      type="error"
      title="Error"
      :text="data.message"
    ></v-alert>
    <v-alert
      v-if="data.status === 200"
      type="info"
      variant="outlined"
      class="ma-15 pa-10"
    >
      <span>
        Contract ID: {{ data.data.metadata.transferRequest.contractId }}</span
      >
    </v-alert>
    <v-alert
      v-if="negotiationId"
      type="info"
      variant="outlined"
      class="ma-15 pa-10"
    >
      <span>Negotiation ID: {{ data.data.metadata.negotiation.id }}</span>
    </v-alert>
    <div class="loading-container">
      <Spinner class="spinner-container" />
    </div>
  </v-container>
  <div v-else>
    <Header :battery-id="data.data.passport" />
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
import GeneralInformation from "@/components/GeneralInformation.vue";
import CellChemistry from "@/components/CellChemistry.vue";
import ElectrochemicalProperties from "@/components/ElectrochemicalProperties.vue";
import BatteryComposition from "@/components/BatteryComposition.vue";
import StateOfBattery from "@/components/StateOfBattery.vue";
import Documents from "@/components/Documents.vue";
import ContractInformation from "@/components/ContractInformation.vue";
import Spinner from "@/components/Spinner.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import { API_KEY } from "@/services/service.const";
import apiWrapper from "@/services/Wrapper";
import AAS from "@/services/AasServices";
import { inject } from "vue";
export default {
  name: "PassportView",
  components: {
    Header,
    GeneralInformation,
    CellChemistry,
    StateOfBattery,
    ElectrochemicalProperties,
    BatteryComposition,
    Documents,
    ContractInformation,
    Footer,
    Spinner,
  },
  data() {
    return {
      auth: inject("authentication"),
      data: null,
      loading: true,
      errors: [],
      passId: this.$route.params.id,
    };
  },
  async created() {
    this.data = await this.getPassport(this.passId);
    this.loading = false;
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

      const shellId = await aas.getAasShellId(
        JSON.stringify(assetIdJson),
        AASRequestHeader
      );
      const shellDescriptor = await aas.getShellDescriptor(
        shellId[0],
        AASRequestHeader
      );
      const subModel = await aas.getSubmodelDescriptor(
        shellDescriptor,
        AASRequestHeader
      );
      if (subModel.endpoints.length > 0) {
        let providerConnector = {
          connectorAddress:
            subModel.endpoints[0].protocolInformation.endpointAddress,
          idShort: subModel.idShort,
        };
        let APIWrapperRequestHeader = {
          "x-api-key": API_KEY,
        };

        console.info("Selected asset Id: " + assetId);
        const response = await wrapper.performEDCDataTransfer(
          assetId,
          providerConnector,
          APIWrapperRequestHeader
        );
        this.contractInformation = providerConnector;
        return response;
      } else
        alert(
          "There is no connector endpoint defined in submodel.. Could not proceed further!"
        );
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
