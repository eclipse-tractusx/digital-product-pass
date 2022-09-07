<template id="battery-passport-root">
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <Header />
    <div class="container" data-cy="battery-pass-container">
      <label class="label" for="Provider">Battery Provider:</label>
      <select
        id="selectProvider"
        v-model="selectedProvider"
        class="select"
        placeholder="Select Battery Provider"
        data-cy="provider-select"
        @change="getBatteriesByProvider()"
      >
        <option value="" disabled selected>Select Battery Provider...</option>
        <option
          v-for="provider in listProviders"
          :key="provider.id"
          :value="provider.name"
        >
          {{ provider.name }}
        </option>
      </select>
      <label class="label" for="Battery">Battery:</label>
      <select
        id="selectBattery"
        v-model="selectedBattery"
        required
        class="form-select select"
        :disabled="selectedProvider === ''"
        placeholder="Select Battery"
        data-cy="battery-select"
        @change="getAssetIdsByBattery()"
      >
        <option value="" disabled selected>Select Battery...</option>
        <option
          v-for="(battery, id) in provider.batteries"
          :key="id"
          :value="battery.id"
        >
          {{ battery.name }}
        </option>
      </select>
      <br />
      <div v-if="assetIdsVisible">
        <label class="label" for="Search criteria">Search Criteria:</label
        ><br /><br />
        <textarea
          v-model="assetIds"
          disabled
          style="height: 120px; width: 340px"
        ></textarea>
      </div>
      <button
        :disabled="!validateFields(selectedProvider, selectedBattery)"
        class="btn btn-success center success-btn"
        type="button"
        data-cy="passport-btn"
        @click="getProductPassport"
      >
        Get Battery Passport
      </button>
    </div>
    <!-- <div class="dashboard-container">
      <div class="titles-container">
        <div class="title">Welcome back {{ name }}!</div>
        <div class="sub-title">See batteries scanned today</div>
        <div class="sub-title orange">See full history</div>
      </div>
      <b-table borderless striped :fields="fields" sort-icon-left :items="batteriesList" />
    </div> -->
  </div>
</template>

<script type="text/jsx">
import Header from "@/components/Header.vue";
import Spinner from "@/components/Spinner.vue";
import { inject } from "vue";

let listBatteryProviders = require("../assets/providers.json");

export default {
  name: "BatteryPassport",
  components: {
    Spinner,
    Header,
  },
  data() {
    return {
      auth: inject("authentication"),
      // fields: [
      //   {
      //     key: 'serial_number',
      //     label: 'Serial number',
      //     sortable: true
      //   },
      //   {
      //     key: 'car_producer',
      //     label: 'Car producer',
      //     sortable: true
      //   },
      //   {
      //     key: 'date_of_admission',
      //     label: 'Date of admission',
      //     sortable: true,
      //   },
      //   {
      //     key: 'status',
      //     label: 'Status',
      //     sortable: true,
      //   }
      // ],
      // batteriesList: [
      //   {
      //     serial_number: "11194511/45",
      //     car_producer: "BMW",
      //     date_of_admission: "21.07.2022",
      //     status: "1",
      //   },
      //   {
      //     serial_number: "22294511/45",
      //     car_producer: "Volkswagen",
      //     date_of_admission: "21.07.2022",
      //     status: "2",
      //   },
      //   {
      //     serial_number: "33394511/45",
      //     car_producer: "Volvo",
      //     date_of_admission: "21.07.2022",
      //     status: "3",
      //   },
      //   {
      //     serial_number: "44494511/45",
      //     car_producer: "Tesla",
      //     date_of_admission: "21.07.2022",
      //     status: "1",
      //   },
      //   {
      //     serial_number: "55594511/45",
      //     car_producer: "Lada",
      //     date_of_admission: "21.07.2022",
      //     status: "2",
      //   },
      // ],
      loading: true,
      listProviders: listBatteryProviders,
      provider: {},
      selectedProvider: "",
      selectedBattery: "",
      assetIds: {},
      assetIdsVisible: false,
      name: "",
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
      // to handle filling the battery provider dropdown here because this.provider is loaded before provider dropdown and get emplty value.
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
      this.listProviders.forEach((arrObj) => {
        arrObj.name == this.selectedProvider ? (this.provider = arrObj) : null;
      });
    },
    getAssetIdsByBattery: function () {
      this.provider.batteries.forEach((arrObj) => {
        arrObj.id == this.selectedBattery
          ? (this.assetIds = JSON.stringify(arrObj.AssetIds))
          : null;
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

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 22%;
  margin: 15% 39% 0 39%;
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

.dashboard-container {
  width: 54%;
  margin: 0 23% 0 23%;
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

.orange {
  color: #ffa600;
}

@media (max-width: 750px) {
  .container {
    width: 80%;
    margin: 25% 10% 0 10%;
  }

  .success-btn {
    width: 90%;
    margin: 0;
  }

  .select {
    width: 90%;
  }
}
</style>
