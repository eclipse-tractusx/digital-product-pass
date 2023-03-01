<template>
  <div class="document-field">
    <div class="document-field-container">
      <a
        v-for="document in field"
        :key="document"
        :href="document.fileLocation"
        target="_blank"
        class="link-container"
        data-cy="link-to-the-label-element"
      >
        <span class="field-label">{{ document.title }}</span>

        <span class="field-value link-wrapper">
          <v-icon
            v-if="document.fileLocation && document.fileLocation != null"
            icon="mdi-text-box-outline"
            class="document-icon"
          ></v-icon>
          <span class="link">
            {{ document.fileLocation }}
          </span>
        </span>
      </a>
    </div>
  </div>
</template>

<script>
export default {
  name: "DocumentField",
  props: {
    field: { type: [String, Number], default: "" },
  },
};
</script>

