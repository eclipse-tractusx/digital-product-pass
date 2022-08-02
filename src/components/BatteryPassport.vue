<template>
  <Spinner v-if="loading" class="spinner-container" />
  <div v-else>
    <Header />
    <div class="container" data-cy="battery-pass-container">
      <label class="label" for="Provider">Battery Provider:</label>

      <select
        class="select"
        id="selectProvider"
        v-model="selectedProvider"
        placeholder="Select Battery Provider"
        @change="getBatteriesbyProvider()"
        data-cy="provider-select"
      >
        <option value="" disabled selected>Select Battery Provider...</option>
        <option
          v-for="provider in listProviders"
          :value="provider.name"
          v-bind:key="provider.id"
        >
          {{ provider.name }}
        </option>
      </select>

      <label class="label" for="Battery">Battery:</label>
      <select
        required
        class="form-select select"
        id="selectBattery"
        v-model="selectedBattery"
        placeholder="Select Battery"
        :disabled="selectedProvider != '' ? disabled : ''"
        @change="getAssetIdsByBattery()"
        data-cy="battery-select"
      >
        <option value="" disabled selected>Select Battery...</option>
        <option
          v-for="(battery, id) in provider.batteries"
          :value="battery.id"
          v-bind:key="id"
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
        type="button"
        class="btn btn-success center success-btn"
        :disabled="isDisabled"
        v-on:click="getProductPassport"
        data-cy="passport-btn"
      >
        Get Battery Passport
      </button>
    </div>
    <div class="dashboard-container">
      <div class="titles-container">
        <div class="title">Welcome back {{ name }}!</div>
        <div class="sub-title">See batteries scanned today</div>
        <div class="sub-title orange">See full history</div>
      </div>
      <b-col lg="6" class="my-1">
        <b-form-group
          label="Search"
          label-for="filter-input"
          label-cols-sm="3"
          label-align-sm="right"
          label-size="sm"
          class="mb-0"
        >
          <b-input-group size="sm">
            <b-form-input
              id="filter-input"
              v-model="filter"
              type="search"
              placeholder="Type to Search"
            ></b-form-input>

            <b-input-group-append>
              <b-button :disabled="!filter" @click="filter = ''"
                >Clear</b-button
              >
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
      </b-col>
      <b-table
        borderless
        striped
        :filter="filter"
        :fields="fields"
        sort-icon-left
        :items="batteriesList"
      />
    </div>
  </div>
</template>

<script type="text/jsx">
import axios from 'axios';
import Header from '@/components/Header.vue'
import Spinner from "@/components/Spinner.vue";

let listBatteryProviders = require('../assets/providers.json');

export default {
  name: 'batteryPassport',
  created(){
    this.loading = false;
  },
  components: {
     Spinner,
Header
  },

  mounted(){

    let user = localStorage.getItem("user-info");
        if (!user){
          if (this.$route.query.provider != undefined && this.$route.query.battery != undefined){
            let provider = this.$route.query.provider
            let battery =  this.$route.query.battery
            let QRCodeAccessInfo = {"provider": provider.toUpperCase(), "battery": battery}
            localStorage.setItem("QRCode-info", JSON.stringify(QRCodeAccessInfo))
          }
          else
            this.$router.push({name:'Login'});
        }
        else{
          let user = localStorage.getItem("user-info")
          let role = JSON.parse(user).role;
          let name = JSON.parse(user).name;
          console.log("CurrentUser: ",user, role);

          // check query params for QR code scanning
          this.selectedProvider = this.$route.query.provider
          this.selectedBattery = this.$route.query.battery
          this.selectedContract = this.$route.query.battery + '_' + role.toLowerCase()
          this.name = name
          if (this.$route.query.provider === undefined && this.$route.query.battery === undefined) {
            // do manual selection of fields
            console.log('INFO: provider and battery are not defined')
            this.resetFields()
          }
          else if (this.validateFields(this.$route.query.provider, this.$route.query.battery))
            {
              // Get BatteryData from qr code
              this.GetBatteryDataUsingQRCode()
            }
          else
            alert('Battery provider and battery name are required...!')
        }
  },
  data() {
    return {
       fields: [
          {
            key: 'serial_number',
            label: 'Serial number',
            sortable: true
          },
          {
            key: 'car_producer',
            label: 'Car producer',
            sortable: true
          },
          {
            key: 'date_of_admission',
            label: 'Date of admission',
            sortable: true,

          },
           {
            key: 'status',
            label: 'Status',
            sortable: true,

          }
        ],
      batteriesList: [
        {
          serial_number: "11194511/45",
          car_producer: "BMW",
          date_of_admission: "21.07.2022",
          status: "1",
        },
        {
          serial_number: "22294511/45",
          car_producer: "Volkswagen",
          date_of_admission: "21.07.2022",
          status: "2",
        },
        {
          serial_number: "33394511/45",
          car_producer: "Volvo",
          date_of_admission: "21.07.2022",
          status: "3",
        },
        {
          serial_number: "44494511/45",
          car_producer: "Tesla",
          date_of_admission: "21.07.2022",
          status: "1",
        },
        {
          serial_number: "55594511/45",
          car_producer: "Lada",
          date_of_admission: "21.07.2022",
          status: "2",
        },
      ],
      loading: true,
      listProviders: listBatteryProviders,
      provider: {},
      selectedProvider:'',
      selectedBattery:'',
      assetIds: {},
      isDisabled: false,
      assetIdsVisible: false,
      name: ""
    }
  },
  methods:{

    getContractOfferByLoggedInRole: function(){
      let user = localStorage.getItem("user-info")
      let role = JSON.parse(user).role
      const offer = this.provider.contractOffers.filter( h => h.includes(role.toLowerCase()) );
      this.provider.contractOffers = offer
      // to handle filling the battery provider dropdown here because this.provider is loaded before provider dropdown and get emplty value.
      this.selectedProvider = this.provider.name
    },
    resetFields: function () {

      this.selectedProvider = ''
      this.selectedBattery = ''
    },
    validateFields: function(provider, battery){
      if (provider === '' && battery === '')
        return false;
      else
        return true;
    },
    getBatteriesbyProvider: function(){
      this.assetIds = '';
      this.assetIdsVisible = false;
      this.listProviders.forEach((arrObj) => {
      arrObj.name == this.selectedProvider ? this.provider = arrObj : null;
    });
    },
    getAssetIdsByBattery: function(){
      this.provider.batteries.forEach((arrObj) => {
      arrObj.id == this.selectedBattery ? this.assetIds = JSON.stringify(arrObj.AssetIds) : null;
    });
    this.assetIdsVisible = true;

    },
    async GetBatteryDataUsingQRCode(){

      // To get the provider and batteries
      this.getBatteriesbyProvider();
      this.getAssetIdsByBattery();
      await this.getProductPassport();
    },
    getProductPassport: function(){
      if (this.validateFields(this.selectedProvider, this.selectedBattery))
        this.$router.replace({ name: "Passport", params:{ assetIds: this.assetIds }, query:{ provider: this.selectedProvider, battery: this.selectedBattery,  },  });

    else
      alert('Battery provider and battery name are required...!')
    }
  }
}
</script>

<style scoped>
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
</style>
