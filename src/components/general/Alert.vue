<template>
  <v-alert
    :closable="closable"
    :icon="icon"
    :title="title"
    :text="description"
    :type="type"
    :color="color"
    :variant="variant"
  >
    <slot></slot>
  </v-alert>
</template>

<script>
export default {
  name: "Alert",
  props: {
    title: {
      type: String,
      default: "Alert",
    },
    description: {
      type: String,
      default: "Alert Description",
    },
    type: {
      type: String,
      default: "info",
    },
    icon: {
      type: String,
      optional: true,
      default: "info",
    },
    color: {
      type: String,
      optional: true,
      default: null,
    },
    closable: {
      type: Boolean,
      optional: true,
      default: true,
    },
    variant: {
      type: String,
      optional: true,
      default: null,
    },
  },
};
</script>

<style>
.v-alert__content {
  color: black !important;
}
.v-alert--variant-outlined {
  border: 1px solid #c1c1c157 !important;
  background: white !important;
  box-shadow: 0px 3px 5px rgba(0, 0, 0, 0.06), 0px 7px 9px rgba(0, 0, 0, 0.12),
    0px 20px 25px -8px rgba(0, 0, 0, 0.18);
}
</style>