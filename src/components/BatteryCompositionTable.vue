<template>
  <EasyDataTable
    table-class-name="customize-table"
    hide-footer
    :headers="headers"
    :items="items"
    :search-field="searchField"
    :search-value="searchValue"
    alternating
  >
    <template #header-part_name="header">
      <div class="customize-header">
        {{ header.text }}
      </div>
    </template>
    <template #item-part_name="{ part_name, page }">
      <div class="part_name-wrapper">
        <a :href="page">
          <span class="link-text">
            {{ part_name }}
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
  name: "BatteryCompositionTable",
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
        { text: "Part name", value: "part_name", sortable: true },
        {
          text: "Serial number",
          value: "serial_number",
          sortable: true,
        },
        { text: "Phone number", value: "phone_number", sortable: true },
        { text: "Email", value: "email", sortable: true },
      ],
      items: [
        {
          part_name: "Battery modules",
          serial_number: null,
          phone_number: "+49 345879349",
          email: "john@spp01.de",
        },
        {
          part_name: "BMS",
          serial_number: "23494511/45",
          phone_number: "+49 345879349",
          email: "john@spp01.de",
        },
        {
          part_name: "Voltage cables",
          serial_number: "23494511/45",
          phone_number: "+49 345879349",
          email: "john@spp01.de",
        },
        {
          part_name: "Thermal control",
          serial_number: null,
          phone_number: "+49 345879349",
          email: "john@spp01.de",
        },
        {
          part_name: "Casing",
          serial_number: "23494511/45",
          phone_number: "+49 345879349",
          email: "john@spp01.de",
        },
      ],
    };
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
  margin: 40px 0;
  --easy-table-header-font-color: #cccccc;
  --easy-table-body-row-height: 56px;
  --easy-table-body-row-font-size: 14px;
  --easy-table-body-row-font-color: #3d3d3d;
  --easy-table-body-row-background-color: #f4f7df;
  --easy-table-body-even-row-background-color: #fbfdf4;
  --easy-table-body-row-hover-background-color: #ffd280;
  --easy-table-header-background-color: white;
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

.part_name-wrapper {
  font-size: 14px;
  padding-left: 20px;
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
  padding-left: 30px;
}
</style>
