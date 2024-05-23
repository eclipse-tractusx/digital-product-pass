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


  <template >
  <div v-if="treeData" class="recursive-tree">
    <!-- eslint-disable-next-line vue/require-v-for-key -->
    <ul>
      <!-- eslint-disable-next-line vue/no-v-for-template-key  -->
      <li v-for="(treeChild, index) in treeData" :key="index">
        <div class="tile-container" @click="toggle">
          <template
            v-if="treeChild.children.length > 0 || loading || status != 200"
          >
            <v-icon v-if="status == 204" class="loading">
              {{ "mdi-loading" }}
            </v-icon>
            <v-icon
              class="icon-bg"
              :style="`background-color: ${infoColor}`"
              v-else-if="status == 404"
            >
              {{ "mdi-exclamation-thick" }}
            </v-icon>
            <v-icon
              v-else-if="status == 200"
              class="icon-bg"
              :class="{
                'mdi-plus':
                  treeChild.children &&
                  treeChild.children.length > 0 &&
                  !treeChild.open,
                'mdi-minus':
                  !treeChild.children ||
                  treeChild.open ||
                  treeChild.children.length == 0,
                'blue-bg': !treeChild.open,
                'gray-bg':
                  !treeChild.children ||
                  treeChild.open ||
                  treeChild.children.length == 0,
              }"
            >
              [{{
                treeChild.children && treeChild.children.length > 0
                  ? treeChild.open
                    ? "mdi-minus"
                    : "mdi-plus"
                  : "mdi-minus"
              }}]
            </v-icon>
            <v-icon
              v-else
              class="icon-bg"
              :style="`background-color: ${infoColor}`"
            >
              {{ "mdi-close-octagon-outline" }}
            </v-icon>
          </template>
          <template v-else>
            <v-icon></v-icon>
          </template>
          <div
            v-if="treeChild != null"
            class="tile"
            :class="{
              'linked-tile': treeChild.link,
              'not-available': !treeChild.id,
            }"
          >
            <span class="label">
              {{ treeChild.name }}
            </span>
            <span
              class="counter"
              v-if="treeChild.children && treeChild.children.length > 0"
            >
              {{ treeChild.children.length }}
            </span>
            <p class="tile-id">
              {{ treeChild.id ? treeChild.id : "Not available" }}
            </p>
            <a
              v-if="'/' + treeChild.searchId !== this.$route.path"
              @click="getLink(treeChild.searchId)"
              target="_blank"
              rel="noopener noreferrer"
            >
              <v-icon v-if="treeChild.searchId" class="external-link">{{
                "mdi-open-in-new"
              }}</v-icon>
            </a>
          </div>
        </div>
        <template v-if="treeChild.children && treeChild.open">
          <recursive-tree :treeData="treeChild.children" />
        </template>
      </li>
    </ul>
  </div>
  <div v-else>
    <div class="recursive-tree">
      <div class="tile-container">
        <v-icon class="icon-bg mdi-minus gray-bg"> [{{ "mdi-minus" }}] </v-icon>
        <div class="tile not-available">
          <p class="tile-id">{{ $t("recursiveTree.notAvailable") }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RecursiveTree",
  props: {
    loading: { type: Boolean, default: false },
    status: { type: Number, default: 200 },
    infoColor: { type: String, default: "" },
    treeData: {
      open: Boolean,
      type: [Object, Array],
      required: true,
    },
  },

  methods: {
    toggle() {
      this.treeData.forEach((element) => {
        if (element.children && element.children.length > 0) {
          element.open = !element.open;
        }
      });
    },
    getLink(searchId) {
      const routeData = this.$router.resolve({
        path: `/${searchId}`,
      });
      window.open(routeData.href, "_blank");
    },
  },
};
</script>


