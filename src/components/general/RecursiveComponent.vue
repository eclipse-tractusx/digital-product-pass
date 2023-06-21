<template>
  <div>
    <ul>
      <li v-for="(value, key) in jsonData" :key="key">
        <template v-if="Array.isArray(jsonData)">
          {{ parseInt(key) + 1 }}:
        </template>
        <template v-else-if="!Array.isArray(jsonData)"> {{ key }}: </template>
        <template v-if="typeof value === 'string'">
          <span v-if="typeof key === number"> {{ key }}: </span>
          <strong> {{ value }}</strong>
        </template>
        <template v-else-if="Array.isArray(value)">
          <recursive-component :jsonData="value" style="padding-left: 10px" />
        </template>
        <template v-else-if="typeof value === 'object'">
          <recursive-component :jsonData="value" style="padding-left: 10px" />
        </template>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "RecursiveComponent",
  props: {
    jsonData: {
      type: [Object, Array],
      required: true,
    },
  },
};
</script>
