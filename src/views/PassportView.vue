<template>
  <div v-if="loading" style="padding: 18% 42% 18% 42%">
    <span>
      <img height="200" src="../assets/loading.gif" width="250"/>
    </span>
  </div>
  <div v-else>
    <Header :batteryId="data.generalInformation"/>
    <div class="container">
      <GeneralInformation
          :generalInformation="data.generalInformation"
          sectionTitle="General information"
      />
      <BatteryComposition
          :batteryComposition="data.batteryComposition"
          sectionTitle="Battery Composition"
      />
      <StateOfHealth
          :stateOfHealth="data.stateOfHealth"
          sectionTitle="State of Health"
      />
      <ParametersOfTheBattery
          :parametersOfTheBattery="data.parametersOfTheBattery"
          sectionTitle="Parameters of The Battery"
      />
      <DismantlingProcedures
          :dismantlingProcedures="data.dismantlingProcedures"
          sectionTitle="Dismantling procedures"
      />
      <!-- <SafetyInformation
        sectionTitle="Safety information"
        :safetyInformation="data.safetyInformation"
      /> -->
      <InformationResponsibleSourcing
          :informationResponsibleSourcing="data.informationResponsibleSourcing"
          sectionTitle="Information responsible sourcing"
      />

      <!-- <AdditionalInformation
        sectionTitle="Additional information"
        :additionalInformation="data.additionalInformation"
      /> -->
    </div>
    <Footer/>
  </div>
</template>

<script>
// @ is an alias to /src
import GeneralInformation from "@/components/GeneralInformation.vue";
import BatteryComposition from "@/components/BatteryComposition.vue";
import StateOfHealth from "@/components/StateOfHealth.vue";
import ParametersOfTheBattery from "@/components/ParametersOfTheBattery.vue";
import DismantlingProcedures from "@/components/DismantlingProcedures.vue";
import InformationResponsibleSourcing from "@/components/InformationResponsibleSourcing.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";
import {reactive} from "vue";
import {AAS_PROXY_URL} from "@/services/service.const";

export default {
  name: "PassportView",
  components: {
    Header,
    GeneralInformation,
    BatteryComposition,
    StateOfHealth,
    ParametersOfTheBattery,
    DismantlingProcedures,
    InformationResponsibleSourcing,
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
      errors: []
    };
  },
  methods: {
    getDigitalTwinId: function (assetIds) {
      return new Promise((resolve) => {
        let encodedAssetIds = encodeURIComponent(assetIds);
        axios.get(`${AAS_PROXY_URL}/lookup/shells?assetIds=${encodedAssetIds}`).then((response) => {
          console.log("PassportView (Digital Twin):", response.data);
          resolve(response.data);
        }).catch((e) => {
          this.errors.push(e);
          resolve("rejected");
        });
      });
    },
    getDigitalTwinObjectById: function (digitalTwinId) {
      //const res =  axios.get("http://localhost:4243/registry/shell-descriptors/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001"); // Without AAS Proxy
      return new Promise((resolve) => {
        axios.get(`${AAS_PROXY_URL}/registry/shell-descriptors/${digitalTwinId}`).then((response) => {
          console.log("PassportView (Digital Twin Object):", response.data);
          resolve(response.data);
        }).catch((e) => {
          this.errors.push(e);
          resolve("rejected");
        });
      });
    },
    getSubModelData: function (digitalTwin) {
      //const res =  axios.get("http://localhost:8193/api/service/urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120001-urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97919/submodel?provider-connector-url=http://provider-control-plane:8282"); // Without AAS Proxy
      //Calling with AAS Proxy 
      return new Promise((resolve) => {
        axios.get(`${AAS_PROXY_URL}/shells/${digitalTwin.identification}/aas/${digitalTwin.submodelDescriptors[0].identification}/submodel?content=value&extent=withBlobValue`, {
          auth: {
            username: 'someuser',
            password: 'somepassword'
          }
        }).then((response) => {
          console.log("PassportView (SubModel):", response.data);
          resolve(response.data);
        }).catch((e) => {
          this.errors.push(e);
          resolve("rejected");
        });
      });
    },
    async getPassport(assetIds) {
      const digitalTwinId = await this.getDigitalTwinId(assetIds);
      const digitalTwin = await this.getDigitalTwinObjectById(digitalTwinId);
      return await this.getSubModelData(digitalTwin);
    },
  },
  async created() {
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
