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
  <footer data-cy="footer">
    <div class="footer-content">
      <div class="links">
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/documentation/?path=docs"
            >Help</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/contact"
            >Contact</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/imprint"
            >Imprint</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/privacy"
            >Privacy</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/terms"
            >Terms of Service</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/cookiepolicy"
            >Cookies</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            href="https://portal.int.demo.catena-x.net/thirdpartylicenses"
            >Third Party Licenses</a
          >
        </div>
        <DialogComponent>
          <div>
            <a id="legalNotice">Legal notice</a>
          </div>
          <template v-slot:title> Legal notice </template>
          <template v-slot:text>
            <div>
              <a
                id="lnkHelp"
                target="_blank"
                :href="REPO_ENDPOINT"
                >Digital Product Pass</a
              >
            </div>
            <div>License: Apache-2.0</div>
            <div>
              <span>Licence Path: </span>
              <a
                id="lnkHelp"
                target="_blank"
                :href="baseCommitUrl + '/LICENSE'"
                >View License File</a
              >
            </div>
            <div>
              <span>NOTICE: </span>
              <a
                id="lnkHelp"
                target="_blank"
                :href="baseCommitUrl + '/NOTICE.md'"
                >View Notice File</a
              >
            </div>
            <div>
              <span>Source URL: </span>
              <a
                id="lnkHelp"
                target="_blank"
                :href="REPO_ENDPOINT"
                >{{REPO_ENDPOINT}}</a
              >
            </div>
            <div>
              <span>Commit ID: </span>
              <a
                id="lnkHelp"
                target="_blank"
                :href="baseCommitUrl"
                >{{ COMMIT_ID }}</a
              >
            </div>
          </template>
        </DialogComponent>
      </div>
      <div class="copyright-container">
        <div class="copyright">
          Copyright © Catena-X Automotive Network.
          <a target="_blank" id="version" :href="tagRepoUrl">v{{ VERSION }}</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import { VERSION, REPO_ENDPOINT, COMMIT_ID } from "@/services/service.const";
import DialogComponent from "../general/Dialog.vue";
export default {
  name: "FooterComponent",
  components: {
    DialogComponent,
  },
  data() {
    return {
      baseCommitUrl: REPO_ENDPOINT+'/blob/'+COMMIT_ID,
    };
  },
  computed: {
    tagRepoUrl() {
      if (VERSION == null || VERSION === "" || VERSION === "VERSION") {
        return this.repoUrl;
      }
      return this.repoUrl + "/releases/tag/v" + VERSION;
    },
  },
  setup() {
    return {
      VERSION,
      COMMIT_ID,
      REPO_ENDPOINT
    };
  },
};
</script>
