<template>
  <span>search field:</span>
  <select v-model="searchField">
    <option>model</option>
    <option>producer</option>
    <option>capacity</option></select
  ><br />
  <span>search value: </span>
  <input type="text" v-model="searchValue" />
  <EasyDataTable
    table-class-name="customize-table"
    :headers="headers"
    :items="items"
    :search-field="searchField"
    :search-value="searchValue"
    alternating
  >
    <template #item-model="{ model, page }">
      <div class="model-wrapper">
        <a target="_blank" :href="page">{{ model }}</a>
      </div>
    </template>
  </EasyDataTable>
</template>


<script type="text/jsx">
import { ref } from "vue";
import { Header, Item } from "vue3-easy-data-table";

export default {
  name: "DashboardTable",
  setup() {
    const searchField = ref("producer");
    const searchValue = ref("");
    return {
      searchField,
      searchValue,
    };
  },
  data() {
    return {
      headers: [
        { text: "Car model", value: "model", sortable: true },
        {
          text: "Car producer",
          value: "producer",
          sortable: true,
          // , width: 200
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
};
</script>

<style scoped>
.customize-table {
  --easy-table-body-row-font-size: 16px;

  --easy-table-body-even-row-background-color: #edefe5;
}
a {
  text-decoration: none;
  color: black;
}
.model-wrapper {
  font-size: 16px;
}
</style>
