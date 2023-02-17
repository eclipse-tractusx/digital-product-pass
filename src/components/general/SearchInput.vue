<template>
  <v-form
    ref="form"
    class="form"
    v-model="valid"
    lazy-validation
    @submit.prevent
  >
    <div class="input-form">
      <v-text-field
        v-model="typedCode"
        class="input"
        type="text"
        placeholder="Enter your company name"
      ></v-text-field>

      <v-btn
        v-if="typedCode !== '' && typedCode !== null"
        @click="reset"
        class="clear"
        :ripple="{ class: 'ripple-background' }"
      >
        <v-icon icon="mdi-close-thick" size="large"></v-icon>
      </v-btn>
    </div>
    <v-btn
      rounded="pill"
      color="#0F71CB"
      size="small"
      class="submit-btn"
      @click="onClick"
      type="submit"
    >
      Search
      <v-icon class="icon" start md icon="mdi-arrow-right"></v-icon>
    </v-btn>
  </v-form>
</template>

<script>
export default {
  data() {
    return {
      valid: true,
      typedCode: "",
    };
  },
  methods: {
    onClick() {
      this.$router.push({
        path: `/${this.typedCode}`,
      });
    },
    reset() {
      this.$refs.form.reset();
    },
  },
};
</script>
