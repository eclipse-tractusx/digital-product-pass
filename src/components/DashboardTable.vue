<template>
  <div class="search-container">
    <div class="search-sub-container">
      <span v-if="searchMenu" class="input-label">search field:</span>
      <select v-if="searchMenu" v-model="searchField" class="select">
        <option>model</option>
        <option>producer</option>
        <option>capacity</option>
      </select>
      <span v-if="searchMenu" class="input-label">search value: </span>
      <input
        v-model="searchValue"
        type="text"
        class="input"
        :class="{ extended: searchMenu }"
      />
    </div>
    <img
      :src="Search"
      alt="search"
      class="search-button"
      title="search"
      @click="
        resetFields();
        searchMenu = !searchMenu;
      "
    />
  </div>
  <EasyDataTable
    table-class-name="customize-table"
    hide-footer
    :headers="headers"
    :items="items"
    :search-field="searchField"
    :search-value="searchValue"
    alternating
  >
    <template #header-model="header">
      <div class="customize-header">
        {{ header.text }}
      </div>
    </template>
    <template #item-model="{ model, page }">
      <div class="model-wrapper">
        <a target="_blank" :href="page">
          <span class="link-text">
            {{ model }}
          </span>
        </a>
      </div>
    </template>
  </EasyDataTable>
</template>


<script type="text/jsx">
import { ref } from "vue";
import Search from "../assets/search.svg";

export default {
  name: "DashboardTable",
  setup() {
    const searchField = ref("producer");

    return {
      Search,
      searchField,
    };
  },
  data() {
    return {
      searchValue: "",
      searchMenu: false,
      headers: [
        { text: "Car model", value: "model", sortable: true },
        {
          text: "Car producer",
          value: "producer",
          sortable: true,
        },
        { text: "Battery capacity", value: "capacity", sortable: true },
        { text: "Date of admission", value: "date", sortable: true },
      ],
      items: [
        {
          model: "ID.3",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "15.02.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "ID.4",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "11.01.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "ID.15",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "11.01.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "i7",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "i11",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "i16",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "Model 3",
          producer: "Tesla",
          capacity: "250 kWh",
          date: "21.06.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "Niva",
          producer: "Lada",
          capacity: "600 kWh",
          date: "24.07.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "XC-40",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "XC-60",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "XC-90",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          page: "http://localhost:8080/?provider=BMW&battery=test-battery-1",
        },
      ],
    };
  },

  methods: {
    resetFields: function () {
      this.searchValue = "";
    },
  },
};
</script>

<style scoped>
.search-container {
  position: relative;
  min-width: 60%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 20px 0;
}

input:focus-visible {
  outline-color: #b3cb2c;
}

.input {
  border: 1px solid #b3cb2c;
  border-radius: 4px;
  margin: 0 0 0 10px;
  height: 39px;
  width: 39px;
  padding: 0 8px;
}

select:focus-visible {
  outline-color: #b3cb2c;
}

.select {
  border: 1px solid #b3cb2c;
  border-radius: 4px;
  margin: 0 0 0 10px;
  width: 100px;
  height: 39px;
  padding: 0 8px;
  color: #3d3d3d;
  background: white;
}

.input-label {
  padding: 0 10px;
}

.customize-table {
  min-width: 60%;
  --easy-table-header-font-color: #cccccc;
  --easy-table-header-background-color: white;
  --easy-table-body-row-height: 56px;
  --easy-table-body-row-font-size: 14px;
  --easy-table-body-row-font-color: #3d3d3d;
  --easy-table-body-row-background-color: #f4f7df;
  --easy-table-body-even-row-background-color: #fbfdf4;
  --easy-table-body-row-hover-background-color: #ffd280;
  --easy-table-header-background-color: #f8f9fa;
  --easy-table-header-font-color: #7a7a7a;
  --easy-table-header-height: 49px;
  --easy-table-border: none;
  --easy-table-row-border: none;
}
a {
  position: absolute;
  bottom: 6px;
  padding: 12px 50vw 12px 12px;
  color: #3d3d3d;
  text-decoration: none;
  white-space: nowrap;
  z-index: 12;
}

.model-wrapper {
  font-size: 14px;
  padding-left: 8px;
  bottom: 5px;
}

.search-button {
  position: absolute;
  cursor: pointer;
  margin: 20px 0;
}

.extended {
  width: 12vw;
}

.customize-header {
  padding-left: 16px;
}
</style>
