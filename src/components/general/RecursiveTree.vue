<!--
  Catena-X - Product Passport Consumer Frontend
 
  Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
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
  <div class="recursive-tree">
    <ul>
      <li>
        <template v-if="typeof treeData === 'object'">
          <div class="tile-container" @click="toggle">
            <v-icon
              class="icon-bg"
              :class="{
                'mdi-plus': treeData.nodes && !treeData.open,
                'mdi-minus': !treeData.nodes || treeData.open,
                'blue-bg': !treeData.open,
                'gray-bg': !treeData.nodes || treeData.open,
              }"
            >
              [{{
                treeData.nodes && treeData.nodes.length > 0
                  ? treeData.open
                    ? "mdi-minus"
                    : "mdi-plus"
                  : "mdi-minus"
              }}]
            </v-icon>
            <div
              class="tile"
              :class="{
                'linked-tile': treeData.link,
                'not-available': !treeData.id,
              }"
            >
              <span class="label">
                {{ treeData.label }}
              </span>
              <span
                class="counter"
                v-if="treeData.nodes && treeData.nodes.length > 0"
              >
                {{ treeData.nodes.length }}
              </span>
              <p class="tile-id">
                {{ treeData.id ? treeData.id : "Not available" }}
              </p>
              <a
                :href="treeData.link"
                target="_blank"
                rel="noopener noreferrer"
              >
                <v-icon v-if="treeData.link" class="external-link">{{
                  "mdi-open-in-new"
                }}</v-icon>
              </a>
            </div>
          </div>
          <template v-if="treeData.nodes && treeData.open">
            <li
              transition="fade-transition"
              v-for="childTreeData in treeData.nodes"
              :key="childTreeData.id"
            >
              <recursive-tree :treeData="childTreeData" />
            </li>
          </template>
        </template>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "RecursiveTree",
  props: {
    treeData: {
      open: Boolean,
      type: [Object, Array],
      required: true,
    },
  },

  methods: {
    toggle() {
      if (this.treeData.nodes && this.treeData.nodes.length > 0) {
        this.treeData.open = !this.treeData.open;
      }
    },
  },
};
</script>
