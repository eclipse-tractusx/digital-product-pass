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
import { AAS_PROXY_URL } from "@/services/service.const";
import api_wrapper from "@/services/wrapper";
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
    getDigitalTwinId: function (assetIds) {
      return new Promise((resolve) => {
        let encodedAssetIds = encodeURIComponent(assetIds);
        axios
          .get(`${AAS_PROXY_URL}/lookup/shells?assetIds=${encodedAssetIds}`)
          .then((response) => {
            console.log("PassportView (Digital Twin):", response.data);
            resolve(response.data);
          })
          .catch((e) => {
            this.errors.push(e);
            resolve("rejected");
          });
      });
    },
    getDigitalTwinObjectById: function (digitalTwinId) {
      //const res =  axios.get("http://localhost:4243/registry/shell-descriptors/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001"); // Without AAS Proxy
      return new Promise((resolve) => {
        axios
          .get(`${AAS_PROXY_URL}/registry/shell-descriptors/${digitalTwinId}`)
          .then((response) => {
            console.log("PassportView (Digital Twin Object):", response.data);
            resolve(response.data);
          })
          .catch((e) => {
            this.errors.push(e);
            resolve("rejected");
          });
      });
    },
    getSubmodelData: function (digitalTwin) {
      //const res =  axios.get("http://localhost:8193/api/service/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001-urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97919/submodel?provider-connector-url=http://provider-control-plane:8282"); // Without AAS Proxy
      //Calling with AAS Proxy
      return new Promise((resolve) => {
        axios
          .get(
            `${AAS_PROXY_URL}/shells/${digitalTwin.identification}/aas/${digitalTwin.submodelDescriptors[0].identification}/submodel?content=value&extent=withBlobValue`,
            {
              auth: {
                username: "someuser",
                password: "somepassword",
              },
            }
          )
          .then((response) => {
            console.log("PassportView (SubModel):", response.data);
            resolve(response.data);
          })
          .catch((e) => {
            this.errors.push(e);
            resolve("rejected");
          });
      });
    },
    async getPassport(assetIds) {
      // const digitalTwinId = await this.getDigitalTwinId(assetIds);
      // const digitalTwin = await this.getDigitalTwinObjectById(digitalTwinId);
      // const response = await this.getSubmodelData(digitalTwin);
      let requestHeaders ={
        "Authorization" : "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ5dlp4ejhyLVZvYndEd0dqMW1TUHlRMWFTX1loYWJFZlQwakpsX2ZEZk4wIn0.eyJleHAiOjE2NjYzNjQ3NzYsImlhdCI6MTY2NjM2NDQ3NiwianRpIjoiZWZiYTAyNGUtMTY3My00ZWZlLTgxMGEtYTdlN2ZlMjgzNTIxIiwiaXNzIjoiaHR0cHM6Ly9jZW50cmFsaWRwLmRlbW8uY2F0ZW5hLXgubmV0L2F1dGgvcmVhbG1zL0NYLUNlbnRyYWwiLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsIkNsNC1DWC1EaWdpdGFsVHdpbiIsImFjY291bnQiXSwic3ViIjoiZjEwNjViOWYtNDEwZC00M2NjLTg4YTQtODAxNzAzYzYxMDZhIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2EtY2w2LWN4LTI5IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtY2F0ZW5hLXggcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsibWFuYWdlLXVzZXJzIiwidmlldy1jbGllbnRzIiwicXVlcnktY2xpZW50cyJdfSwiQ2w0LUNYLURpZ2l0YWxUd2luIjp7InJvbGVzIjpbImFkZF9kaWdpdGFsX3R3aW4iLCJ2aWV3X2RpZ2l0YWxfdHdpbiIsImRlbGV0ZV9kaWdpdGFsX3R3aW4iLCJ1cGRhdGVfZGlnaXRhbF90d2luIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwiYnBuIjoiQlBOTDAwMDAwMDAwMDAwMCIsImNsaWVudEhvc3QiOiIxMC4yNDAuMC42IiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJjbGllbnRJZCI6InNhLWNsNi1jeC0yOSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1zYS1jbDYtY3gtMjkiLCJjbGllbnRBZGRyZXNzIjoiMTAuMjQwLjAuNiJ9.CRGobWPlAy-0mPFHutuIUeMi0LbKoV9Xak23yczShPdsRTkQyVWfjuW79Vbdwg3RS_cdyLPc0nlVnJ8cF_noDL6PnRIIhEk0VdqvJqrtUP-Ur7dANA64VU5gY1Re3OHh16Gu3fZIKgbOyWehdR6zXc3cjuigfQeCcf6N9nQAIrMY1TZWr06ilnCZ-DdTvWSZv_ZZd0bkPvfuYVgVobqmy2jshAYJ_kZKJpsb2MUBC842aAlj0MnY3tzeeXVVFfwx3ou18KiR7wWPY6UZIq_9YcvcV4hwfANbPBf1DQG-ubgmS5COW5-xlUpVtgeAdcusPYMO6LRcEgB82qmk-pgj1g"
      };
      let aas = new AAS();
      let wrapper = new api_wrapper();
      const shellId = await aas.getAasShellId(assetIds, requestHeaders);
      const shellDescriptor = await aas.getShellDescriptor(shellId[0], requestHeaders);
      const subModel = await aas.getSubmodelDescriptor(shellDescriptor, requestHeaders);

      const providerEndpoint = subModel.endpoints[0].protocolInformation.endpointAddress;
      console.log(providerEndpoint);
      const response = await wrapper.performEDCDataTransfer("101",providerEndpoint);
      return response;
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
