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

<template id="battery-passport-root">
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <div data-cy="history-container" class="dashboard-container">
      <div class="titles-container">
        <div class="title">Welcome back {{ name }}!</div>
        <div class="sub-title">Batteries scanned today:</div>
        <!-- TODO: History of scanned batteries -->
        <!-- <div class="sub-title orange">See full history</div> -->
      </div>
      <DashboardTable />
    </div>
  </div>
</template>

<script type="text/jsx">
import Spinner from "@/components/general/Spinner.vue";
import DashboardTable from "@/components/landing/DashboardTable.vue";
import { inject } from "vue";

export default {
  name: "WelcomeView",
  components: {
    Spinner,
    DashboardTable,
  },
  data() {
    return {
      auth: inject("authentication"),
      loading: true,
      provider: {},
      selectedProvider: "",
      selectedBattery: "",
      assetIds: {},
      assetIdsVisible: false,
      name: "",
      MATERIAL_URL: process.env.VUE_APP_MATERIAL_URL,
    };
  },
  created() {
    this.loading = false;
  },

  mounted() {
    if (this.auth.isUserAuthenticated) {
      // User has an active session and using QR code feature
      let user = this.auth.getUserName();
      let role = this.auth.getRole();
      console.log("CurrentUser: ", user, " role: ", role);
      // check query params for QR code scanning
      this.selectedProvider = this.$route.query.provider;
      this.selectedBattery = this.$route.query.battery;
      this.selectedContract =
        this.$route.query.battery + "_" + role.toLowerCase();
      if (
        this.$route.query.provider === undefined ||
        this.$route.query.battery === undefined
      ) {
        // do manual selection of fields
        console.log("INFO: provider and battery are not defined");
        this.resetFields();
      } else if (
        this.validateFields(
          this.$route.query.provider,
          this.$route.query.battery
        )
      ) {
        // Get BatteryData from qr code
        this.getBatteryDataUsingQRCode();
      } else {
        alert("Battery provider and battery name are required...!");
      }
    }
  },
  methods: {
    getContractOfferByLoggedInRole: function () {
      let role = this.auth.getRole();
      const offer = this.provider.contractOffers.filter((h) =>
        h.includes(role.toLowerCase())
      );
      this.provider.contractOffers = offer;
      // to handle filling the battery provider dropdown here because this.provider is loaded before provider dropdown and get empty value.
      this.selectedProvider = this.provider.name;
    },
    resetFields: function () {
      this.selectedProvider = "";
      this.selectedBattery = "";
    },

    validateFields: function (provider, battery) {
      return !(provider === "" || battery === "");
    },
    getBatteriesByProvider: function () {
      this.assetIds = "";
      this.assetIdsVisible = false;
      this.listProviders.forEach(this.findProvider);
    },
    findProvider: function (item) {
      if (item.name == this.selectedProvider) this.provider = item;
      else return null;
    },
    getAssetIdsByBattery: function () {
      this.provider.batteries.forEach(this.findAssetId);
      this.assetIdsVisible = true;
      console.log(this.assetIds);
    },
    findAssetId: function (item) {
      let assetId = JSON.stringify(item.AssetIds);
      if (item.id == this.selectedBattery) this.assetIds = assetId;
      else return null;
    },
    async getBatteryDataUsingQRCode() {
      // To get the provider and batteries
      this.getBatteriesByProvider();
      this.getAssetIdsByBattery();
      await this.getProductPassport();
    },
    getProductPassport: function () {
      if (this.validateFields(this.selectedProvider, this.selectedBattery))
        this.$router.replace({
          name: "Passport",
          params: { assetIds: this.assetIds },
          query: {
            provider: this.selectedProvider,
            battery: this.selectedBattery,
          },
        });
      else alert("Battery provider and battery name are required...!");
    },
  },
};
</script>

<style scoped>
#battery-passport-root {
  min-height: 100vh;
}

.dashboard-container {
  width: 70%;
  margin: 14em 15% 70px 15%;
}

.titles-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-bottom: 24px;
}

.title {
  font-size: 32px;
  font-weight: bold;
}

.sub-title {
  font-size: 16px;
  font-weight: bold;
}
</style>
