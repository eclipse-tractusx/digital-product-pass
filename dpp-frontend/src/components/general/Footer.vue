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
  <footer data-cy="footer">
    <div class="footer-content">
      <div class="links">
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/documentation/?path=docs'"
          >
            {{ $t("footer.help") }}
          </a>
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/contact'"
            >{{ $t("footer.contact") }}</a
          >
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/imprint'"
          >
            {{ $t("footer.imprint") }}
          </a>
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/privacy'"
          >
            {{ $t("footer.privacy") }}
          </a>
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/terms'"
            >
            {{ $t("footer.terms") }}
          </a>
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/cookiepolicy'"
          >
            {{ $t("footer.cookies") }}
          </a>
        </div>
        <div>
          <a
            id="lnkHelp"
            target="_blank"
            :href="portalUrl+'/thirdpartylicenses'"
          >
            {{ $t("footer.thirdParty") }}
          </a>
        </div>
        <DialogComponent>
          <div>
            <a id="legalNotice">{{ $t("footer.legal") }}</a>
          </div>
          <template v-slot:title>{{ $t("footer.legal") }}</template>
          <template v-slot:text>
            <div>
              <a id="lnkHelp" target="_blank" :href="REPO_ENDPOINT">{{
                $t("passportView.dpp")
              }}</a>
            </div>
            <div>{{ $t("footer.license") }}</div>
            <div>
              <span>{{ $t("footer.licensePath") }}</span>
              <a
                id="lnkHelp"
                target="_blank"
                :href="baseCommitUrl + '/LICENSE'"
                >{{ $t("footer.viewLicense") }}</a
              >
            </div>
            <div>
              <span>{{ $t("footer.notice") }}</span>
              <a
                id="lnkHelp"
                target="_blank"
                :href="baseCommitUrl + '/NOTICE.md'"
                >{{ $t("footer.viewNotice") }}</a
              >
            </div>
            <div>
              <span>{{ $t("footer.source") }}</span>
              <a id="lnkHelp" target="_blank" :href="REPO_ENDPOINT">{{
                REPO_ENDPOINT
              }}</a>
            </div>
            <div>
              <span>{{ $t("footer.commit") }}</span>
              <a id="lnkHelp" target="_blank" :href="baseCommitUrl">{{
                COMMIT_ID
              }}</a>
            </div>
          </template>
        </DialogComponent>
      </div>
      <div class="copyright-container">
        <div class="copyright">
          {{ $t("footer.copyright") }}
          <a target="_blank" id="version" :href="tagRepoUrl">v{{ VERSION }}</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
import { VERSION, REPO_ENDPOINT, COMMIT_ID, PORTAL_URL } from "@/services/service.const";
import DialogComponent from "../general/Dialog.vue";
export default {
  name: "FooterComponent",
  components: {
    DialogComponent,
  },
  data() {
    return {
      baseCommitUrl: REPO_ENDPOINT+'/blob/'+COMMIT_ID,
      portalUrl: PORTAL_URL
    };
  },
  computed: {
    tagRepoUrl() {
      if (VERSION == null || VERSION === "" || VERSION === "VERSION") {
        return REPO_ENDPOINT;
      }
      return REPO_ENDPOINT + "/releases/tag/v" + VERSION;
    },
  },
  setup() {
    return {
      VERSION,
      COMMIT_ID,
      REPO_ENDPOINT,
    };
  },
};
</script>
