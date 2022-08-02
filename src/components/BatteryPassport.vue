<template id="battery-passport-root">
  <Header/>
  <div class="container">
    <label class="label" for="Provider">Battery Provider:</label>

    <select
        id="selectProvider"
        v-model="selectedProvider"
        class="select"
        placeholder="Select Battery Provider"
        @change="getBatteriesByProvider()"
    >
      <option disabled selected value="">Select Battery Provider...</option>
      <option
          v-for="provider in listProviders"
          v-bind:key="provider.id"
          :value="provider.name"
      >
        {{ provider.name }}
      </option>
    </select>

    <label class="label" for="Battery">Battery:</label>
    <select
        id="selectBattery"
        v-model="selectedBattery"
        :disabled="selectedProvider === ''"
        class="form-select select"
        placeholder="Select Battery"
        required
        @change="getAssetIdsByBattery()"
    >
      <option disabled selected value="">Select Battery...</option>
      <option
          v-for="(battery, id) in provider.batteries"
          v-bind:key="id"
          :value="battery.id"
      >
        {{ battery.name }}
      </option>
    </select>
    <br/>
    <div v-if="assetIdsVisible">
      <label class="label" for="Search criteria">Search Criteria:</label><br/><br/>
      <textarea v-model="assetIds" disabled style="height: 120px; width:340px;"></textarea>
    </div>

    <button
        :disabled="!validateFields(selectedProvider, selectedBattery)"
        class="btn btn-success center success-btn"
        type="button"
        v-on:click="getProductPassport"
    >
      Get Battery Passport
    </button>
  </div>
</template>

<script type="text/jsx">
import Header from '@/components/Header.vue'

let listBatteryProviders = require('../assets/providers.json');

export default {
  name: 'BatteryPassport',
  created() {
  },
  components: {
    Header
  },
  mounted() {
    let user = localStorage.getItem("user-info");
    if (!user) {
      if (this.$route.query.provider !== undefined && this.$route.query.battery !== undefined) {
        let provider = this.$route.query.provider
        let battery = this.$route.query.battery
        let QRCodeAccessInfo = {"provider": provider.toUpperCase(), "battery": battery}
        localStorage.setItem("QRCode-info", JSON.stringify(QRCodeAccessInfo))
      } else {
        this.$router.push({name: 'Login'});
      }
    } else {
      let user = localStorage.getItem("user-info")
      let role = JSON.parse(user).role;
      console.log("CurrentUser: ", user, role);
      // check query params for QR code scanning
      this.selectedProvider = this.$route.query.provider
      this.selectedBattery = this.$route.query.battery
      this.selectedContract = this.$route.query.battery + '_' + role.toLowerCase()

      if (this.$route.query.provider === undefined && this.$route.query.battery === undefined) {
        // do manual selection of fields
        console.log('INFO: provider and battery are not defined')
        this.resetFields()
      } else if (this.validateFields(this.$route.query.provider, this.$route.query.battery)) {
        // Get BatteryData from qr code
        this.getBatteryDataUsingQRCode()
      } else {
        alert('Battery provider and battery name are required...!')
      }
    }
  },
  data() {
    return {
      listProviders: listBatteryProviders,
      provider: {},
      selectedProvider: '',
      selectedBattery: '',
      assetIds: {},
      assetIdsVisible: false,
    }
  },
  methods: {
    getContractOfferByLoggedInRole: function () {
      let user = localStorage.getItem("user-info")
      let role = JSON.parse(user).role
      this.provider.contractOffers = this.provider.contractOffers.filter(h => h.includes(role.toLowerCase()))
      // to handle filling the battery provider dropdown here because this.provider is loaded before provider dropdown and get empty value.
      this.selectedProvider = this.provider.name
    },
    resetFields: function () {
      this.selectedProvider = ''
      this.selectedBattery = ''
    },
    validateFields: function (provider, battery) {
      return !(provider === '' || battery === '');
    },
    getBatteriesByProvider: function () {
      this.assetIds = '';
      this.assetIdsVisible = false;
      this.listProviders.forEach((arrObj) => {
        arrObj.name === this.selectedProvider ? this.provider = arrObj : null;
      });
    },
    getAssetIdsByBattery: function () {
      this.provider.batteries.forEach((arrObj) => {
        arrObj.id === this.selectedBattery ? this.assetIds = JSON.stringify(arrObj.AssetIds) : null;
      });
      this.assetIdsVisible = true;

    },
    async getBatteryDataUsingQRCode() {
      // To get the provider and batteries
      this.getBatteriesByProvider();
      this.getAssetIdsByBattery();
      await this.getProductPassport();
    },
    getProductPassport: function () {
      if (this.validateFields(this.selectedProvider, this.selectedBattery)) {
        this.$router.replace({
          name: "Passport",
          params: {assetIds: this.assetIds},
          query: {provider: this.selectedProvider, battery: this.selectedBattery}
        });
      } else {
        alert('Battery provider and battery name are required...!')
      }
    }
  }
}
</script>

<style scoped>
#battery-passport-root {
  min-height: 100vh;
}

.container {
  display: flex;
  flex-direction: column;

  width: 22%;
  margin: 0 39% 0 39%;
}

.success-btn {
  width: 340px;
  height: 48px;
  margin: 12px 0 60px 0;
  background: #b3cb2d;
  color: white;
  font-size: 16px;
  font-weight: bolder;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.success-btn:disabled {
  cursor: not-allowed;
  /* TODO add disabled color #b7c567*/
}

.label {
  padding: 12px 0 12px 0;
  font-weight: bold;
}

.select {
  width: 340px;
  height: 48px;
  border: solid 1px #b3cb2c;
  border-radius: 4px;
}
</style>
