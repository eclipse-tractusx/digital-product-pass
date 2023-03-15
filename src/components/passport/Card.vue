<template>
  <v-col class="card-container">
    <span>{{ title }} </span>
    <span>
      <v-icon class="icon" start md :icon="icon"></v-icon>
    </span>

    <div v-if="title === 'SUSTAINABILITY'">
      <span>test 1</span>
      <v-divider vertical inset></v-divider>
      <span>test 2</span>
    </div>

    <div v-else>
      <div>{{ label }}</div>
      <div>{{ value }}</div>
      <v-divider></v-divider>
      <div v-if="title === 'HEALTH'">
        <div>progress bar</div>
      </div>
      <div v-else>
        <div>{{ secondValue }}</div>
        <div>{{ secondLabel }}</div>
      </div>
      <div>{{ info }}</div>
    </div>
  </v-col>
</template>

<script>
export default {
  name: "CardComponent",
  props: {
    title: { type: [String, Number], default: "" },
    icon: { type: [String, Number], default: "" },
    label: { type: [String, Number], default: "" },
    value: { type: [String, Number], default: "" },
    secondLabel: { type: [String, Number], default: "" },
    secondValue: { type: [String, Number], default: "" },
    info: { type: [String, Number], default: "" },
  },
  data() {
    return {};
  },
};
</script>
