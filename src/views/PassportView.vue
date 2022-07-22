<template>
  <div v-if="loading" style="padding: 18% 42% 18% 42%">
    <span>
      <img src="../assets/loading.gif" height="200" width="250"/>
    </span>
  </div>
  <div v-else>
    <Header :batteryId="data.generalInformation" />
    <div class="container">
      <GeneralInformation
        sectionTitle="General information"
        :generalInformation="data.generalInformation"
      />
      <BatteryComposition
        sectionTitle="Battery Composition"
        :batteryComposition="data.batteryComposition"
      />
      <StateOfHealth
        sectionTitle="State of Health"
        :stateOfHealth="data.stateOfHealth"
      />
      <ParametersOfTheBattery
        sectionTitle="Parameters of The Battery"
        :parametersOfTheBattery="data.parametersOfTheBattery"
      />
      <DismantlingProcedures
        sectionTitle="Dismantling procedures"
        :dismantlingProcedures="data.dismantlingProcedures"
      />
      <!-- <SafetyInformation
        sectionTitle="Safety information"
        :safetyInformation="data.safetyInformation"
      /> -->
      <InformationResponsibleSourcing
        sectionTitle="Information responsible sourcing"
        :informationResponsibleSourcing="data.informationResponsibleSourcing"
      />

      <!-- <AdditionalInformation
        sectionTitle="Additional information"
        :additionalInformation="data.additionalInformation"
      /> -->
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

import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";
import { reactive } from "vue";

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
  },
  provide() {
    return {
      loadingState: new reactive(() => this.loadingState),
    };
  },
  data() {
    return {
      data: null,
      loading: true,
      errors: [],
      twinRegistryUrl: "http://localhost:4243",
      aasProxyUrl: "http://localhost:4245",
    };
  },
  methods: {
    async fetchData() {
      const res = await fetch("http://localhost:3000/334593247");
      const data = await res.json();
      console.log(data);
      return data;
    },
    getDigitalTwinId: function(assetIds){
      return new Promise((resolve) => {
        let encodedAssetIds = encodeURIComponent(assetIds);
        axios
          .get( this.aasProxyUrl + '/lookup/shells?assetIds=' + encodedAssetIds )
          .then((response) => {
            console.log(response.data);
            resolve(response.data);
          })
          .catch((e) => {
            this.errors.push(e);
            resolve("rejected");
          });
      });
    },
    getDigitalTwinObjectById: function(digitalTwinId){
      //const res =  axios.get("http://localhost:4243/registry/shell-descriptors/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001"); // Without AAS Proxy
      return new Promise((resolve) => {
        axios
          .get( this.aasProxyUrl + '/registry/shell-descriptors/' + digitalTwinId )   //Calling with AAS Proxy
          .then((response) => {
            console.log(response.data);
            resolve(response.data);
          })
          .catch((e) => {
            this.errors.push(e);
            resolve("rejected");
          });
      });
    },
    getSubmodelData: function(digitalTwin){
      //const res =  axios.get("http://localhost:8193/api/service/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001-urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97919/submodel?provider-connector-url=http://provider-control-plane:8282"); // Without AAS Proxy
      //Calling with AAS Proxy 
      return new Promise((resolve) => {
        axios.get( this.aasProxyUrl + '/shells/' + digitalTwin.identification + '/aas/' + digitalTwin.submodelDescriptors[0].identification + '/submodel?content=value&extent=withBlobValue',{
          auth: {
              username: 'someuser',
              password: 'somepassword'
            }
        })
          .then((response) => {
            console.log(response.data);
            resolve(response.data);
          })
          .catch((e) => {
            this.errors.push(e);
            resolve("rejected");
          });
      });
    },
    async getPassport(assetIds){
      const digitalTwinId = await this.getDigitalTwinId(assetIds);
      const digitalTwin = await this.getDigitalTwinObjectById(digitalTwinId);
      const response = await this.getSubmodelData(digitalTwin);
      return response;
    },
  },
  async created() {
    //this.data = await this.fetchData();
    //console.log(data);
    let assetIds = this.$route.params.assetIds;
    this.data = await this.getPassport(assetIds);
    this.loading = false;
  },
};
</script>

<style scoped>
.container {
  width: 76%;
  margin: 0 12% 0 12%;
}
</style>
