<template>
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <Header :battery-id="data" />
    <div class="pass-container">
      <GeneralInformation
        section-title="General information"
        :general-information="data"
      />
      <CellChemistry
        section-title="Cell chemistry"
        :cell-chemistry="data.cellChemistry"
      />
      <ElectrochemicalProperties
        section-title="State of Health"
        :electrochemical-properties="data.electrochemicalProperties"
      />
      <BatteryComposition
        section-title="Parameters of The Battery"
        :battery-composition="data.composition"
      />
      <StateOfBattery
        section-title="State of Battery"
        :state-of-battery="data"
      />

      <Documents section-title="Documents" :documents="data.document" />
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
import Spinner from "@/components/Spinner.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import passportData from "../assets/MOCK/passportExample01.json";
// import axios from "axios";
// import { AAS_PROXY_URL } from "@/services/service.const";
// import { BASE_URL } from "@/services/service.const";
import api_wrapper from "@/services/wrapper";
import AAS from "@/services/aasServices";
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
    let jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    console.log(jwtToken);
    //   let assetIds = this.$route.params.assetIds;
    this.data = passportData;
    this.loading = false;
  },
  // methods: {
  //   getDigitalTwinId: function (assetIds) {
  //     return new Promise((resolve) => {
  //       let encodedAssetIds = encodeURIComponent(assetIds);
  //       axios
  //         .get(`${AAS_PROXY_URL}/lookup/shells?assetIds=${encodedAssetIds}`)
  //         .then((response) => {
  //           console.log("PassportView (Digital Twin):", response.data);
  //           resolve(response.data);
  //         })
  //         .catch((e) => {
  //           this.errors.push(e);
  //           resolve("rejected");
  //         });
  //     });
  //   },
  //   getDigitalTwinObjectById: function (digitalTwinId) {
  //     //const res =  axios.get("http://localhost:4243/registry/shell-descriptors/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001"); // Without AAS Proxy
  //     return new Promise((resolve) => {
  //       axios
  //         .get(`${AAS_PROXY_URL}/registry/shell-descriptors/${digitalTwinId}`)
  //         .then((response) => {
  //           console.log("PassportView (Digital Twin Object):", response.data);
  //           resolve(response.data);
  //         })
  //         .catch((e) => {
  //           this.errors.push(e);
  //           resolve("rejected");
  //         });
  //     });
  //   },
  //   getSubmodelData: function (digitalTwin) {
  //     //const res =  axios.get("http://localhost:8193/api/service/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001-urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97919/submodel?provider-connector-url=http://provider-control-plane:8282"); // Without AAS Proxy
  //     //Calling with AAS Proxy
  //     return new Promise((resolve) => {
  //       axios
  //         .get(
  //           `${AAS_PROXY_URL}/shells/${digitalTwin.identification}/aas/${digitalTwin.submodelDescriptors[0].identification}/submodel?content=value&extent=withBlobValue`,
  //           {
  //             auth: {
  //               username: "someuser",
  //               password: "somepassword",
  //             },
  //           }
  //         )
  //         .then((response) => {
  //           console.log("PassportView (SubModel):", response.data);
  //           resolve(response.data);
  //         })
  //         .catch((e) => {
  //           this.errors.push(e);
  //           resolve("rejected");
  //         });
  //     });
  //   },
  //   async getPassport(assetIds) {
  //    const digitalTwinId = await this.getDigitalTwinId(assetIds);
  //    const digitalTwin = await this.getDigitalTwinObjectById(digitalTwinId);
  //    const response = await this.getSubmodelData(digitalTwin);
  //    let requestHeaders ={
  //    };
  //    let aas = new AAS();
  //    let wrapper = new api_wrapper();
  //    const shellId = await aas.getAasShellId(assetIds, requestHeaders);
  //    const shellDescriptor = await aas.getShellDescriptor(shellId[0], requestHeaders);
  //    const subModel = await aas.getSubmodelDescriptor(shellDescriptor, requestHeaders);

  //  const providerEndpoint = subModel.endpoints[0].protocolInformation.endpointAddress;
  //  console.log(providerEndpoint);
  //  const response = await wrapper.performEDCDataTransfer("101",providerEndpoint);
  //  return response;
  //   },
  // },
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
