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
  <div class="container">
    <div class="main">
      <h5 class="center">Step # 1: Load contract offers from the battery provider</h5><br />
      <div class="container">
        <label class="center" for="Provider"><strong>Battery Provider:</strong></label> <br />
        <select
id="selectProvider" v-model="selectedProvider" class="form-select center ddl"
          placeholder="Select Battery Provider">
          <option disabled selected value="">Select Battery Provider...</option>
          <option v-for="provider in listProviders" :key="provider.id" :value="provider.name">{{
             provider.name 
            }}
          </option>
        </select>
      </div>
      <br />
      <div class="container">
        <button
:disabled="isDisabled" class="btn btn-success center success-btn" type="button"
          @click="GetProviderInfo">Load Contract Offers
        </button>
        <span id="loadContracts" class="container" style="margin-left: 20px"></span>
      </div>
      <br />
      <h5 class="center">Step # 2: Negotiate the edc contract</h5><br />
      <div class="container">
        <label class="center" for="contractOffer"><strong>Contract Offers:</strong></label><br />
        <select
id="selectOffer" v-model="selectedContract" class="form-select center ddl" placeholder="Select Offer"
          required @change="setSelectedContract($event)">
          <option disabled selected value="">Select an Offer...</option>
          <option v-for="(offer, index) in provider.contractOffers" :key="index">{{  offer  }}
          </option>
        </select>
      </div>
      <br />
      <div class="container">
        <label class="center" for="Battery"><strong>Battery:</strong></label><br />
        <select
id="selectBattery" v-model="selectedBattery" class="form-select center ddl" placeholder="Select Battery"
          required @change="setSelectedBattery($event)">
          <option disabled selected value="">Select Battery...</option>
          <option v-for="(battery, id) in provider.batteries" :key="id" :value="battery.id">{{  battery.name  }}
          </option>
        </select>
      </div>
      <br />
      <div class="container">
        <button
:disabled="isDisabled" class="btn btn-success center success-btn" type="button"
          @click="doNegotiation">Start Negotiation
        </button>
        <span id="negotiateContract" class="container" style="margin-left: 20px"></span>
      </div>
      <br />
      <h5 class="center">Step # 3: Get battery passport from the provider</h5><br />
      <div class="container">
        <button
:disabled="isDisabled" class="btn btn-success center success-btn" type="button"
          @click="initiateTransfer">Get Battery Passport
        </button>
      </div>
      <br />
    </div>
    <br />
  </div>
  <div v-if="isLoading" style="margin-left:49%">
    <div class="spinner-border" role="status" style="width: 3rem; height: 3rem;">
      <span class="sr-only"></span>
    </div>
    <br />
    <div class="h5">{{  currentStatus  }}</div>
  </div>
</template>


<!-- change the script below -->
<script>
// @ is an alias to /src

export default {
  name: 'PassportView',
  components: {},
  methods: {
  }
};
</script>

<style scoped>
.container {
  max-width: 88%;
  padding-left: 130px;
}
</style>
