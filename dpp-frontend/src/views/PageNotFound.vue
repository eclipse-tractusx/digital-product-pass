<!--
  Tractus-X -  Digital Product Pass Application
  
  Copyright (c) 2022, 2024 BMW AG
  Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

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
  <div class="PageNotFound">
    <router-link to="/">
      <img :src="Error" alt="error" class="error" />
    </router-link>
  </div>
</template>

<script>
import Error from "../media/500error.png";
import authUtil from "@/utils/authUtil.js";
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "PageNotFound",
  components: {},
  setup() {
    return {
      Error,
    };
  },
  async created() {
    authUtil.cleanUrl(this)
  }
};
</script>
<style scoped>
.error {
  width: 110%;
}
</style>
