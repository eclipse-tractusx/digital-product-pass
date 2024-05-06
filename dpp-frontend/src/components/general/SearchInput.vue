<!--
  Tractus-X - Digital Product Passport Application

  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
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
        :placeholder="$t('searchInput.passportId')"
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
      id="search-btn"
      rounded="pill"
      append-icon="mdi-arrow-right"
      color="#0F71CB"
      size="x-large"
      class="submit-btn text-none"
      @click="onClick"
      type="submit"
      :disabled="!isContent"
    >
      {{ $t("searchInput.search") }}
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
  computed: {
    isContent() {
      return this.typedCode !== "" && this.typedCode !== null;
    },
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
