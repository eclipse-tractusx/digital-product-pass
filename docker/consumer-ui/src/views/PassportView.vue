<template>
  <div>
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
      <SafetyInformation
        sectionTitle="Safety information"
        :safetyInformation="data.safetyInformation"
      />
      <InformationResponsibleSourcing
        sectionTitle="Information responsible sourcing"
        :informationResponsibleSourcing="data.informationResponsibleSourcing"
      />

      <AdditionalInformation
        sectionTitle="Additional information"
        :additionalInformation="data.additionalInformation"
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

import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from "axios";

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
  data() {
    return {
      data: {},
    };
  },
  methods: {
    async fetchData() {
      const res = await fetch("http://localhost:3000/334593247");
      const data = await res.json();
      console.log(data);
      return data;
    },
    getProductPassport(asset, destinationPath, contractId) {
      return new Promise((resolve) => {
        var jsonData = {
          protocol: "ids-multipart",
          assetId: asset,
          contractId: contractId,
          dataDestination: {
            properties: {
              path: destinationPath + "/" + asset + ".json",
              keyName: "keyName",
              type: "File",
            },
          },
          transferType: {
            contentType: "application/octet-stream",
            isFinite: true,
          },
          managedResources: false,
          connectorAddress: "http://edc-provider:8282/api/v1/ids/data",
          connectorId: "consumer",
        };

        // "connectorAddress": "http://edc-provider:8282/api/v1/ids/data",

        //axios.post('/consumer/data/transferprocess', jsonData, {
        axios
          .post("/consumer/data/transferprocess", jsonData, {
            headers: {
              "X-Api-Key": "password",
            },
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
    displayProductPassport(filename) {
      return new Promise((resolve) => {
        //axios.get('/consumer/data/contractnegotiations/passport/display/' + filename, {
        axios
          .get("/consumer/data/contractnegotiations/passport/display/" + filename, {
            headers: {
              "X-Api-Key": "password",
            },
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
  },
  async created() {
    //this.data = await this.fetchData();
    //console.log(data);
    let contractId = this.$route.params.contractId;
    const destinationPath = "/app/samples/04.0-file-transfer/data"; // set different path for containers
    let asset = "";
    let user = localStorage.getItem("user-info");
    let role = JSON.parse(user).role;
    if (role.toLowerCase() == "dismantler") asset = "test-document_dismantler";
    else if (role.toLowerCase() == "oem") asset = "test-document_oem";
    else if (role.toLowerCase() == "recycler") asset = "test-document_recycler";
    else if (role.toLowerCase() == "Battery Producer")
      asset = "test-document_battery_producer";
    let uuid = await this.getProductPassport(
      asset,
      destinationPath,
      contractId
    );
    if (uuid == null)
      alert("Something went wrong in finalizing product process");
    else {
      // Display the product passport //
      let response = await this.displayProductPassport(asset + ".json");
      this.data = response;
    }
  },
};
</script>

<style scoped>
.container {
  width: 76%;
  margin: 0 12% 0 12%;
}
</style>
