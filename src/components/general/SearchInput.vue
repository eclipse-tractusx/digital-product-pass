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
        placeholder="Type ID"
      ></v-text-field>

      <v-btn
        v-if="isContent"
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
      :disabled="!isContent"
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
  computed:{
    isContent() {
      return this.typedCode !== '' && this.typedCode !== null;
    }
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
