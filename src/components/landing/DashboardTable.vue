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
          <router-link
            :to="'/' + item.id"
            class="table-link"
            data-cy="passport-link"
          >
          </router-link>
          {{ item.model }}
        </td>
        <td>
          <router-link
            :to="'/' + item.id"
            class="table-link"
            data-cy="passport-link"
          >
          </router-link>
          {{ item.producer }}
        </td>
        <td>
          <router-link
            :to="'/' + item.id"
            class="table-link"
            data-cy="passport-link"
          >
          </router-link>
          {{ item.capacity }}
        </td>
        <td>
          <router-link
            :to="'/' + item.id"
            class="table-link"
            data-cy="passport-link"
          >
          </router-link>
          {{ item.date }}
        </td>
      </tr>
    </tbody>
  </v-table>
</template>


<script type="text/jsx">
import { ref } from "vue";
import Search from "../../media/search.svg";

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
          id: "NCR186850B",
        },
        {
          model: "ID.4",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "11.01.2022",
          id: "NCR186850B",
        },
        {
          model: "ID.15",
          producer: "Volkswagen",
          capacity: "98 kWh",
          date: "11.01.2022",
          id: "NCR186850B",
        },
        {
          model: "i7",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          id: "NCR186850B",
        },
        {
          model: "i11",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          id: "NCR186850B",
        },
        {
          model: "i16",
          producer: "BMW",
          capacity: "150 kWh",
          date: "17.04.2022",
          id: "NCR186850B",
        },
        {
          model: "Model 3",
          producer: "Tesla",
          capacity: "250 kWh",
          date: "21.06.2022",
          id: "NCR186850B",
        },
        {
          model: "Niva",
          producer: "Lada",
          capacity: "600 kWh",
          date: "24.07.2022",
          id: "NCR186850B",
        },
        {
          model: "XC-40",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          id: "NCR186850B",
        },
        {
          model: "XC-60",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          id: "NCR186850B",
        },
        {
          model: "XC-90",
          producer: "Volvo",
          capacity: "120 kWh",
          date: "28.08.2022",
          id: "NCR186850B",
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
.table {
  border: 1px solid #dcdcdc;
  border-radius: 24px;
}

tr {
  height: 74px;
}

.header {
  background: #e9e9e9;
  height: 76px;
}
a {
  color: #3d3d3d;
  text-decoration: none;
  white-space: nowrap;
}
</style>
