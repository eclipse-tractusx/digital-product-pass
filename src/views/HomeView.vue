<template>
<div class="container">
  
  <div class="main">
    <h5 class="center">Step # 1: Load contract offers from the battery provider</h5><br />
  <div class="container">
    <label class="center" for="Provider"><strong>Battery Provider:</strong></label> <br />
    <select class="form-select center ddl" id="selectProvider" v-model="selectedProvider" placeholder="Select Battery Provider">
      <option value="" disabled selected>Select Battery Provider...</option>
      <option v-for="provider in listProviders" :value="provider.name" v-bind:key="provider.id">{{ provider.name }}</option>
    </select>

  </div>
  <br />
    <div class="container">
        <button type="button" class="btn btn-success center success-btn"  :disabled="isDisabled"  v-on:click="GetProviderInfo">Load Contract Offers</button>
    <span class="container" style="margin-left: 20px" id="loadContracts"></span>
    </div>
   
  <br />
  <h5 class="center">Step # 2: Negotiate the edc contract</h5><br />
   <div class="container">
      <label class="center" for="contractOffer"><strong>Contract Offers:</strong></label><br />
      <select required class="form-select center ddl" id="selectOffer" v-model="selectedContract" placeholder="Select Offer" @change="setSelectedContract($event)">
        <option value="" disabled selected>Select an Offer...</option>
        <option v-for="(offer, index) in provider.contractOffers"
                v-bind:key="index">{{ offer }}
        </option>
      </select>
    </div>
    <br />
    <div class="container">
      <label class="center" for="Battery"><strong>Battery:</strong></label><br />
    <select required class="form-select center ddl"  id="selectBattery" v-model="selectedBattery" placeholder="Select Battery" @change="setSelectedBattery($event)">
      <option value="" disabled selected >Select Battery...</option>
      <option v-for="(battery, id) in provider.batteries" :value="battery.id"
              v-bind:key="id">{{ battery.name }}
      </option>
    </select>
  </div>
    <br />

    <div class="container">
        <button type="button" class="btn btn-success center success-btn" :disabled="isDisabled"  v-on:click="doNegotiation">Start Negotiation</button>
        <span class="container" style="margin-left: 20px" id="negotiateContract"></span>
    </div>
    <br />
    <h5 class="center">Step # 3: Get battery passport from the provider</h5><br />
    <div class="container">
        <button type="button" class="btn btn-success center success-btn" :disabled="isDisabled"  v-on:click="initiateTransfer">Get Battery Passport</button>
    </div>
    <br />
  </div>
  <br />
</div>
<div v-if="isLoading" style="margin-left:49%">
  <div class="spinner-border" style="width: 3rem; height: 3rem;" role="status" >
    <span class="sr-only"></span>
  </div>
  <br />
  <div class="h5">{{currentStatus}}</div>
</div>
</template>


<!-- change the script below -->
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

      return data;
    },
  },
  async created() {
    this.data = await this.fetchData();
    console.log(data);
  },
};
</script>

<style scoped>
.container {
  max-width: 88%;
  padding-left: 130px;
}
</style>
