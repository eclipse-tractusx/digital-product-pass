<template>
  <v-table hover class="table">
    <thead>
      <tr class="header">
        <th class="text-left">Car model</th>
        <th class="text-left">Car producer</th>
        <th class="text-left">Battery capacity</th>
        <th class="text-left">Date of admission</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="item in items" :key="item.model">
        <td>
          {{ item.model }}
        </td>
        <td>{{ item.producer }}</td>
        <td>{{ item.capacity }}</td>
        <td>{{ item.date }}</td>
      </tr>
    </tbody>
  </v-table>
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
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-2",
        },
        {
          model: "ID.4",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "11.01.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-2",
        },
        {
          model: "ID.15",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "11.01.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-2",
        },
        {
          model: "i7",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "i11",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "i16",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "Model 3",
          producer: "Tesla",
          capacity: "250 kWh",
          date: "21.06.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "Niva",
          producer: "Lada",
          capacity: "600 kWh",
          date: "24.07.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "XC-40",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "XC-60",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
        },
        {
          model: "XC-90",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          page: "https://materialpass.int.demo.catena-x.net/?provider=BMW&battery=test-battery-1",
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

.table {
  border: 1px solid #dcdcdc;
  border-radius: 24px;
}

a {
  padding: 12px 0 12px 12px;
  color: #3d3d3d;
  text-decoration: none;
  white-space: nowrap;
}

tr {
  height: 74px;
}

.header {
  background: #e9e9e9;
  height: 76px;
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
