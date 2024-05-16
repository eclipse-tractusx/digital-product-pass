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
  <div>
    <HeaderComponent>
      <template v-if="!data">
        <span class="header-title">{{ $t("passportView.dpp") }}</span>
      </template>
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
        "
      >
        <span class="header-title">{{ $t("passportView.bpp") }}</span>
      </template>
      <template
        v-else-if="
          data.semanticId ===
          'urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass'
        "
      >
        <span class="header-title">{{ $t("passportView.tpp") }}</span>
      </template>
      <template v-else>
        <span class="header-title">{{ $t("passportView.dpp") }}</span>
      </template>
    </HeaderComponent>
    <v-container v-if="loading && !error">
      <LoadingComponent :id="id" />
    </v-container>
    <v-container
      class="container-policy-selection"
      v-else-if="showOverlay && !error"
    >
      <div class="loading-container">
        <v-col class="v-col-auto dpp-id-container contract-modal">
          <v-card class="contract-container">
            <div class="title-container">
              {{ $t("passportView.policyAgreement.title") }}
            </div>
            <v-radio-group class="content-container" v-model="radios">
              <!-- Loop over the grouped policies -->
              <!-- eslint-disable vue/no-v-for-template-key -->
              <template
                v-for="(group, contractId, contractIndex) in groupedPolicies"
                :key="contractId"
              >
                <v-row class="policy-group-label">
                  <v-col cols="auto"
                    >{{ contractIndex + 1 }}.
                    {{ $t("passportView.policyAgreement.contractId") }}
                  </v-col>
                  <v-col>
                    <span class="contractid-title"> {{ contractId }}</span>
                  </v-col>
                </v-row>
                <v-radio
                  v-for="(item, index) in group"
                  :key="`${contractId}_${index}`"
                  color="#0F71CB"
                  @click="chooseContract(contractId, item['@id'])"
                  :value="`${contractIndex}.${index}`"
                >
                  <template v-slot:label>
                    <div class="radio-label">
                      <v-container class="policy-container">
                        <div class="policy-container fill-height">
                          <span class="policy-title"
                            ><strong
                              >Policy {{ contractIndex + 1 }}.{{
                                index + 1
                              }}</strong
                            ></span
                          >
                          <div>
                            <v-row>
                              <v-col
                                cols="auto"
                                justify-content="center"
                                align-content="center"
                              >
                                <div class="policy-label">ID</div>
                              </v-col>
                              <v-col>
                                <div class="policy-value">
                                  {{ item["@id"] }}
                                </div>
                              </v-col>
                            </v-row>
                            <v-divider></v-divider>
                            <div>
                              <v-row
                                v-for="(
                                  attributes, policyKey, policyIndex
                                ) in parsedPolicyConstraints[item['@id']]"
                                :key="`${policyIndex}`"
                              >
                                <template v-if="attributes.length != 0">
                                  <v-col
                                    cols="auto"
                                    justify-content="center"
                                    align-content="center"
                                  >
                                    <div class="policy-second-label">
                                      {{ policyKey }}
                                    </div>
                                  </v-col>
                                  <v-col>
                                    <div class="policy-second-value">
                                      <v-row
                                        class="field-container"
                                        v-for="(
                                          attribute, attrIndex
                                        ) in attributes"
                                        :key="`${attrIndex}`"
                                      >
                                        <p class="policy-second-value">
                                          Action Type:
                                          {{ attribute.actionType }}
                                        </p>
                                        <div>
                                          <ul>
                                            <li
                                              v-for="(
                                                constraint, constraintIndex
                                              ) in attribute.constraints
                                                .constraint"
                                              :key="`${constraintIndex}`"
                                            >
                                              <span
                                                v-if="
                                                  attribute.constraints.operator
                                                "
                                                class="attribute-operator"
                                                >{{
                                                  attribute.constraints.operator
                                                }}</span
                                              >
                                              <span class="attribute-constraint"
                                                >- [{{
                                                  constraint.leftOperand
                                                }}] [{{ constraint.operator }}]
                                                [{{
                                                  constraint.rightOperand
                                                }}]</span
                                              >
                                            </li>
                                          </ul>
                                        </div>
                                      </v-row>
                                    </div>
                                  </v-col>
                                </template>
                              </v-row>
                            </div>
                          </div>
                        </div>
                      </v-container>
                    </div>
                  </template>
                </v-radio>
              </template>
            </v-radio-group>
            <div class="btn-background">
              <v-row class="pt-8 justify-center">
                <v-btn
                  rounded="xl"
                  size="x-large"
                  color="#0F71CB"
                  class="text-none"
                  variant="outlined"
                  style="border: 2px solid"
                  @click="declineContract()"
                  >{{ $t("passportView.policyAgreement.decline") }}</v-btn
                >
                <v-btn
                  rounded="xl"
                  size="x-large"
                  class="text-none ms-4 text-white"
                  color="#0F71CB"
                  @click="
                    resumeNegotiation(
                      searchResponse,
                      contractToSign.contract,
                      contractToSign.policy
                    )
                  "
                  >{{ $t("passportView.policyAgreement.agree") }}</v-btn
                >
              </v-row>
              <v-row class="justify-center">
                <v-btn
                  rounded="xl"
                  size="x-large"
                  variant="text"
                  :prepend-icon="
                    !details
                      ? 'mdi-plus-circle-outline'
                      : 'mdi-minus-circle-outline'
                  "
                  @click="toggleDetails"
                  class="details-btn text-none"
                >
                  {{ detailsTitle }}
                  <template v-slot:prepend>
                    <v-icon color="#0F71CB"></v-icon>
                  </template>
                </v-btn>
              </v-row>
              <v-row v-if="details" class="justify-center">
                <div class="json-viewer-container">
                  <JsonViewer
                    class="json-viewer"
                    :value="contractItems"
                    sort
                    theme="jv-light"
                  />
                </div>
              </v-row>
            </div>
          </v-card>
          <v-overlay class="contract-modal" v-model="declineContractModal">
            <v-card class="contract-container">
              <div class="title-container pt-8 decliner">
                {{ $t("passportView.policyAgreement.declineModal.question") }}
              </div>
              <div class="policy-group-label decliner">
                <div class="back-to-homepage">
                  {{ $t("passportView.policyAgreement.declineModal.homepage") }}
                </div>
              </div>
              <div class="btn-background decliner">
                <v-row class="pt-8 pb-8 justify-center">
                  <v-btn
                    rounded="xl"
                    size="large"
                    color="#0F71CB"
                    class="text-none"
                    variant="outlined"
                    style="border: 2px solid"
                    @click="cancelDeclineContract()"
                    >{{
                      $t("passportView.policyAgreement.declineModal.cancel")
                    }}</v-btn
                  >
                  <v-btn
                    rounded="xl"
                    size="large"
                    class="text-none ms-4 text-white"
                    color="red-darken-4"
                    @click="confirmDeclineContract()"
                    ><template v-if="declineLoading"
                      ><v-progress-circular
                        indeterminate
                      ></v-progress-circular></template
                    ><template v-else>{{
                      $t("passportView.policyAgreement.declineModal.confirm")
                    }}</template></v-btn
                  >
                </v-row>
              </div>
            </v-card>
          </v-overlay>
        </v-col>
      </div>
    </v-container>
    <div v-else-if="data && !error">
      <template
        v-if="
          data.semanticId ===
          'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
        "
      >
        <PassportHeader
          :id="data.aspect.batteryIdentification.batteryIDDMCCode"
          type="Battery ID"
        />
      </template>
      <template v-else>
        <PassportHeader :id="id ? id : '-'" type="ID" />
      </template>
      <div class="pass-container">
        <template
          v-if="
            data.semanticId ===
            'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
          "
        >
          <BatteryCards :data="data" />
        </template>
        <template
          v-else-if="
            data.semanticId ==
            'urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass'
          "
        >
          <TransmissionCards :data="data" />
        </template>
        <template v-else>
          <GeneralCards :data="data" />
        </template>
      </div>
      <div class="pass-container footer-spacer">
        <template
          v-if="
            data.semanticId ===
            'urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass'
          "
        >
          <TabsComponent
            :componentsNames="batteryComponentsNames"
            :componentsData="data"
            :semanticId="data.semanticId"
          />
        </template>
        <template v-else>
          <TabsComponent
            :componentsNames="filteredComponentsNames"
            :componentsData="data"
            :semanticId="data.semanticId"
          />
        </template>
      </div>
      <FooterComponent />
    </div>
    <v-container v-else class="h-100 w-100">
      <div class="d-flex align-items-center w-100 h-100">
        <ErrorComponent
          :title="errorObj.status + ' ' + errorObj.statusText"
          :subTitle="errorObj.title"
          :description="errorObj.description"
          reloadLabel="Return"
          reloadIcon="mdi-arrow-left"
          :reload="errorObj.reload"
        />
      </div>
    </v-container>
  </div>
</template>

<script>
// @ is an alias to /src

import LoadingComponent from "../components/general/LoadingComponent.vue";
import TabsComponent from "../components/general/TabsComponent.vue";
import HeaderComponent from "@/components/general/Header.vue";
import PassportHeader from "@/components/passport/PassportHeader.vue";
import BatteryCards from "@/components/passport/BatteryCards.vue";
import TransmissionCards from "@/components/passport/TransmissionCards.vue";
import GeneralCards from "@/components/passport/GeneralCards.vue";
import FooterComponent from "@/components/general/Footer.vue";
import ErrorComponent from "@/components/general/ErrorComponent.vue";
import {
  AUTO_SIGN,
  SEARCH_TIMEOUT,
  NEGOTIATE_TIMEOUT,
  DECLINE_TIMEOUT,
} from "@/services/service.const";
import threadUtil from "@/utils/threadUtil.js";
import jsonUtil from "@/utils/jsonUtil.js";
import configUtil from "@/utils/configUtil.js";
import edcUtil from "@/utils/edcUtil.js";
import passportUtil from "@/utils/passportUtil.js";
import BackendService from "@/services/BackendService";
import { inject } from "vue";
import { mapState } from "vuex";
import store from "../store/index";
import { JsonViewer } from "vue3-json-viewer";
import "vue3-json-viewer/dist/index.css";
import { reactive } from "vue";
import passports from "@/config/templates/passports.json";

export default {
  name: "PassportView",
  components: {
    JsonViewer,
    HeaderComponent,
    FooterComponent,
    PassportHeader,
    BatteryCards,
    LoadingComponent,
    ErrorComponent,
    TabsComponent,
    GeneralCards,
    TransmissionCards,
  },
  data() {
    return {
      showOverlay: true,
      contractItems: reactive([]),
      radios: "0.0",
      details: false,
      detailsTitle: this.$t("passportView.policyAgreement.details.moreDetails"),
      policies: [],
      declineContractModal: false,
      showContractModal: true,
      batteryComponentsNames: [
        {
          label: "passportView.batteryComponentsNames.generalInformation",
          icon: "mdi-information-outline",
          component: "GeneralInformation",
        },
        {
          label: "passportView.batteryComponentsNames.stateOfBattery",
          icon: "mdi-battery-charging",
          component: "StateOfBattery",
        },
        {
          label: "passportView.batteryComponentsNames.components",
          icon: "mdi-battery-unknown",
          component: "Components",
        },
        {
          label: "passportView.batteryComponentsNames.batteryComposition",
          icon: "mdi-battery-unknown",
          component: "BatteryComposition",
        },
        {
          label: "passportView.batteryComponentsNames.cellChemistry",
          icon: "mdi-flask-empty-outline",
          component: "CellChemistry",
        },
        {
          label:
            "passportView.batteryComponentsNames.electrochemicalProperties",
          icon: "mdi-microscope",
          component: "ElectrochemicalProperties",
        },
        {
          label: "passportView.batteryComponentsNames.documents",
          icon: "mdi-text-box-multiple-outline",
          component: "Documents",
        },
        {
          label: "passportView.batteryComponentsNames.exchange",
          icon: "mdi-file-swap-outline",
          component: "Exchange",
        },
      ],
      auth: inject("authentication"),
      data: null,
      loading: true,
      searchResponse: null,
      declineLoading: false,
      errors: [],
      id: this.$route.params.id,
      irsData: [],
      processId: null,
      backendService: null,
      parsedPolicyConstraints: {},
      error: false,
      errorObj: {
        title: "Something went wrong while returning the passport!",
        description: "We are sorry for that, you can retry or try again later",
        type: "error",
        status: 500,
        statusText: "Internal Server Error",
        reload: true,
      },
    };
  },
  computed: {
    filteredComponentsNames() {
      let dataKeys = Object.keys(this.data.aspect);
      // Check if data exists and is not empty
      if (this.data.aspect && dataKeys.length > 0) {
        // Filter out keys with empty objects or arrays
        dataKeys = dataKeys.filter((key) => {
          const value = this.data.aspect[key];
          if (typeof value === "object" && value !== null) {
            // Check if it's an array or an object and ensure it's not empty
            return Array.isArray(value)
              ? value.length > 0
              : Object.keys(value).length > 0;
          } else if (Array.isArray(value) && value !== null) {
            return value.length > 0;
          }
          return true; // Include if it's not an object/array or if it's a non-empty primitive value
        });

        dataKeys.splice(3, 0, "components");
        dataKeys.push("exchange");
        // Generate component names dynamically from the JSON keys
        return dataKeys.map((key) => ({
          label: key,
          icon: passportUtil.iconFinder(key),
          component: key,
        }));
      } else {
        return [];
      }
    },
    ...mapState(["searchData", "contractToSign"]),
    groupedPolicies() {
      return this.policies.reduce((groups, policy) => {
        const contractId = Object.keys(policy)[0];
        if (!groups[contractId]) {
          groups[contractId] = [];
        }
        groups[contractId].push(policy[contractId]);
        return groups;
      }, {});
    },
  },

  async created() {
    this.backendService = new BackendService();
    this.searchContracts();
  },
  methods: {
    extractPolicies(contracts) {
      let contractPolicies = [];

      for (let key in contracts) {
        // eslint-disable-next-line no-prototype-builtins
        if (contracts.hasOwnProperty(key)) {
          const contract = contracts[key];

          if (Array.isArray(contract["odrl:hasPolicy"])) {
            contract["odrl:hasPolicy"].forEach((policy) => {
              let policyEntry = {};
              let policyId = policy["@id"];
              policyEntry[key] = policy;
              contractPolicies.push(policyEntry);
              try {
                this.parsedPolicyConstraints[policyId] =
                  edcUtil.parsePolicyConstraints(policy);
              } catch (e) {
                console.error(e);
              }
            });
          } else {
            // Create an entry with the contract key and the policy object
            let policyEntry = {};
            let policy = contract["odrl:hasPolicy"];
            let policyId = policy["@id"];
            policyEntry[key] = policy;
            contractPolicies.push(policyEntry);
            try {
              this.parsedPolicyConstraints[policyId] =
                edcUtil.parsePolicyConstraints(policy);
            } catch (e) {
              console.error(e);
              return false;
            }
          }
        }
      }
      return (this.policies = contractPolicies);
    },
    toggleDetails() {
      this.details = !this.details;
      if (this.details) {
        this.detailsTitle = this.$t(
          "passportView.policyAgreement.details.lessDetails"
        );
      } else {
        this.detailsTitle = this.$t(
          "passportView.policyAgreement.details.moreDetails"
        );
      }
    },
    chooseContract(contract, policy) {
      return (this.contractToSign = store.commit("setContractToSign", {
        contract: contract,
        policy: policy,
      }));
    },
    shouldShowOverlay() {
      if (this.policies.length > 0) {
        return (this.showOverlay = true);
      }
    },
    declineContract() {
      this.declineContractModal = true;
      this.showContractModal = false;
    },
    confirmDeclineContract() {
      this.declineLoading = true;
      this.triggerDecline(this.searchResponse);
      if (!this.error) {
        this.$router.push("/");
      }
    },
    cancelDeclineContract() {
      this.declineContractModal = false;
      this.showContractModal = true;
    },
    async searchContracts() {
      let result = null;
      try {
        // Setup aspect promise
        let passportPromise = this.searchAsset(this.id);
        // Execute promisse with a Timeout
        result = await threadUtil.execWithTimeout(
          passportPromise,
          SEARCH_TIMEOUT,
          null
        );
        if (!result || result == null) {
          this.errorObj.title =
            "Timeout! Failed to search for the Digital Twin Registry and the Digital Twin!";
          this.errorObj.description =
            "The request took too long... Please retry or try again later.";
          this.status = 408;
          this.statusText = "Request Timeout";
          this.error = true;
        }
        this.searchResponse = result;
      } catch (e) {
        console.log("searchContracts -> " + e);
      } finally {
        if (!this.searchResponse || this.searchResponse["status"] != 200) {
          this.errorObj.title = jsonUtil.exists("message", this.searchResponse)
            ? this.searchResponse["message"]
            : "The backend is not accessible!";
          this.errorObj.description =
            "It was not possible to search for contracts in backend!.";

          this.errorObj.status = jsonUtil.exists("status", this.searchResponse)
            ? this.searchResponse["status"]
            : 503;

          this.errorObj.statusText = jsonUtil.exists(
            "statusText",
            this.searchResponse
          )
            ? this.searchResponse["statusText"]
            : "Not Available";
          this.error = true;
        } else if (
          this.searchResponse &&
          jsonUtil.exists("status", this.searchResponse) &&
          this.searchResponse["status"] == 200 &&
          jsonUtil.exists("data", this.searchResponse) &&
          jsonUtil.exists("contracts", this.searchResponse["data"]) &&
          jsonUtil.exists("token", this.searchResponse["data"]) &&
          jsonUtil.exists("id", this.searchResponse["data"])
        ) {
          this.error = false;
          if (AUTO_SIGN) {
            this.resumeNegotiation(this.searchResponse);
          } else {
            // Initialize contractItems from searchData
            this.contractItems = jsonUtil.get(
              "data.contracts",
              this.searchResponse
            );
            if (!this.contractItems) {
              this.errorObj.title = "No contract items found!";
              this.errorObj.description =
                "It was not possible to display the policies and contracts.";
              this.status = 500;
              this.statusText = "Internal Server Error";
              this.error = true;
            }

            // Extract policies
            let res = this.extractPolicies(this.contractItems);
            if (!res) {
              this.errorObj.title = "It was not possible to parse policies!";
              this.errorObj.description =
                "It was not possible to display the policies and contracts.";
              this.status = 500;
              this.statusText = "Internal Server Error";
              this.error = true;
            } else {
              // Check if policies array has elements and then access the @id of the first element
              const firstPolicyObj = this.policies[0];
              const initialContractToSign = Object.keys(firstPolicyObj)[0];
              const initialPolicyToSign =
                firstPolicyObj[initialContractToSign]["@id"];

              if (
                !this.parsedPolicyConstraints ||
                Object.keys(this.parsedPolicyConstraints).length == 0
              ) {
                this.errorObj.title = "No contract policies found!";
                this.errorObj.description =
                  "It was not possible to display the policies and contracts.";
                this.status = 500;
                this.statusText = "Internal Server Error";
                this.error = true;
              } else {
                // Commit the contract ID to the store
                this.$store.commit("setContractToSign", {
                  contract: initialContractToSign,
                  policy: initialPolicyToSign,
                });

                this.shouldShowOverlay();
              }
            }
          }
        }
        if (this.error || !AUTO_SIGN) {
          // Stop loading
          this.loading = false;
        }
      }
    },
    async triggerDecline(searchResponse) {
      let result = null;
      let token = jsonUtil.get("data.token", searchResponse);
      let processId = jsonUtil.get("data.id", searchResponse);
      try {
        // Setup aspect promise
        let passportPromise = this.declineNegotiation(token, processId);
        // Execute promisse with a Timeout
        result = await threadUtil.execWithTimeout(
          passportPromise,
          DECLINE_TIMEOUT,
          null
        );
        if (!result || result == null) {
          this.errorObj.title = "Timeout! Failed to decline negotiation!";
          this.errorObj.description =
            "The request took too long... Please retry or try again later.";
          this.status = 408;
          this.statusText = "Request Timeout";
          this.error = true;
        }
        this.data = result;
      } catch (e) {
        console.log("passportView -> " + e);
      } finally {
        if (!this.data || this.data["status"] != 200) {
          this.errorObj.title = jsonUtil.exists("message", this.data)
            ? this.data["message"]
            : "The backend is not accessible!";
          this.errorObj.description =
            "It was not possible to search for contracts in backend!.";

          this.errorObj.status = jsonUtil.exists("status", this.data)
            ? this.data["status"]
            : 503;

          this.errorObj.statusText = jsonUtil.exists("statusText", this.data)
            ? this.data["statusText"]
            : "Not Available";
          this.error = true;
        } else if (
          this.data &&
          jsonUtil.exists("status", this.data) &&
          this.data["status"] == 200
        ) {
          this.error = false;
        }
        // Stop loading
        this.$store.commit("resetLoadingState");
        this.loading = false;
        this.declineLoading = false;
      }
    },
    async resumeNegotiation(
      searchResponse,
      contractId = null,
      policyId = null
    ) {
      this.showOverlay = false;
      this.loading = true;
      let result = null;
      let contracts = jsonUtil.get("data.contracts", searchResponse);
      let token = jsonUtil.get("data.token", searchResponse);
      let processId = jsonUtil.get("data.id", searchResponse);

      try {
        // Setup aspect promise
        let passportPromise = this.negotiatePassport(
          contracts,
          token,
          processId,
          contractId,
          policyId
        );
        // Execute promisse with a Timeout
        result = await threadUtil.execWithTimeout(
          passportPromise,
          NEGOTIATE_TIMEOUT,
          null
        );
        if (!result || result == null) {
          this.errorObj.title =
            "Timeout! Failed to negotiate and return the passport!";
          this.errorObj.description =
            "The request took too long... Please retry or try again later.";
          this.errorObj.status = 408;
          this.errorObj.statusText = "Request Timeout";
          this.error = true;
        }
        this.data = result;
      } catch (e) {
        console.log("passportView -> " + e);
      } finally {
        if (!this.data || this.data["status"] != 200) {
          this.errorObj.title = jsonUtil.exists("message", this.data)
            ? this.data["message"]
            : "The backend is not accessible!";
          this.errorObj.description =
            "It was not possible to search for contracts in backend!.";

          this.errorObj.status = jsonUtil.exists("status", this.data)
            ? this.data["status"]
            : 503;

          this.errorObj.statusText = jsonUtil.exists("statusText", this.data)
            ? this.data["statusText"]
            : "Not Available";
          this.error = true;
        } else if (
          this.data &&
          jsonUtil.exists("status", this.data) &&
          this.data["status"] == 200 &&
          jsonUtil.exists("data", this.data) &&
          jsonUtil.exists("metadata", this.data["data"]) &&
          jsonUtil.exists("aspect", this.data["data"]) &&
          jsonUtil.exists("semanticId", this.data["data"])
        ) {
          let passportSemanticId = jsonUtil.get("data.semanticId", this.data);
          if (!jsonUtil.exists(passportSemanticId, passports)) {
            this.errorObj.title =
              "This application version does not support this passport aspect model version!";
            this.errorObj.description =
              "Unfortunatly, this aspect model with semantic id  [" +
              passportSemanticId +
              "] is not supported in this application at the moment.";
            this.errorObj.status = 422;
            this.errorObj.statusText = "Not Supported Aspect Model";
            this.errorObj.reload = false;
            this.error = true;
          } else {
            let additionalData = [];
            let sources = [];
            // In order to have the additional data available we need to copy it in deep
            if (
              jsonUtil.exists("additionalData", this.data["data"]["aspect"])
            ) {
              additionalData = jsonUtil.copy(
                this.data["data"]["aspect"]["additionalData"]
              );
            }
            // When extend deep is called this property will be replaced
            if (jsonUtil.exists("sources", this.data["data"]["aspect"])) {
              sources = jsonUtil.copy(this.data["data"]["aspect"]["sources"]);
            }
            this.data = configUtil.normalizePassport(
              jsonUtil.get("data.aspect", this.data),
              jsonUtil.get("data.metadata", this.data),
              jsonUtil.get("data.semanticId", this.data)
            );
            // Re-add the additionalData
            if (jsonUtil.exists("additionalData", this.data["aspect"])) {
              this.data["aspect"]["additionalData"] = additionalData;
            }
            // Re-add the sources
            if (jsonUtil.exists("sources", this.data["aspect"])) {
              this.data["aspect"]["sources"] = sources;
            }
            this.error = false;
            this.processId = this.$store.getters.getProcessId; // Get process id from the store
            this.irsData = this.backendService.getIrsData(
              this.processId,
              this.auth
            ); // Return the IRS data
            this.$store.commit("setIrsData", this.irsData); // Save IRS Data
            this.$store.commit(
              "setIrsState",
              this.backendService.getIrsState(this.processId, this.auth)
            );
          }
        }
        // Stop loading
        this.$store.commit("resetLoadingState");
        this.loading = false;
        this.contractItems = [];
      }
    },
    async searchAsset(id) {
      let response = null;
      // Get Passport in Backend
      try {
        // Init backendService
        // Get access token from IDP
        // Get the aspect for the selected version

        response = await this.backendService.searchAsset(id, this.auth);
      } catch (e) {
        console.log("passportView.getPassport() -> " + e);
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "Failed to return passport";
        this.errorObj.description =
          "It was not possible to transfer the passport.";

        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 500;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Internal Server Error";
        this.error = true;
        return response;
      }
      if (!response || response["status"] != 200) {
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "The backend is not accessible!";
        this.errorObj.description =
          "It was not possible to search for contracts in backend!.";

        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 503;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Not Available";
        this.error = true;
      } 
      // Check if reponse content was successfull and if not print error comming message from backend
      if (jsonUtil.exists("status", response) && response["status"] != 200) {
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "An error occured when searching for the passport!";
        this.errorObj.description =
          "An error occured when searching for the passport!";
        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 404;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Not found";
        this.error = true;
      }

      return response;
    },
    async negotiatePassport(
      contracts,
      token,
      processId,
      contractId = null,
      policyId = null
    ) {
      let response = null;
      // Get Passport in Backend
      try {
        // Init backendService
        // Get access token from IDP
        // Get the aspect for the selected version

        response = await this.backendService.negotiateAsset(
          contracts,
          token,
          processId,
          this.auth,
          contractId,
          policyId
        );
      } catch (e) {
        console.log("passportView.getPassport() -> " + e);
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "Failed to return passport";
        this.errorObj.description =
          "It was not possible to transfer the passport.";

        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 500;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Internal Server Error";
        this.error = true;
        return response;
      }

      // Check if the response is empty and give an error
      if (!response) {
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to complete the passport transfer.";
        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 503;
        this.errorObj.statusText = "Bad Request";
        this.error = true;
        return null;
      }

      // Check if reponse content was successfull and if not print error comming message from backend
      if (jsonUtil.exists("status", response) && response["status"] != 200) {
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "An error occured when searching for the passport!";
        this.errorObj.description =
          "An error occured when searching for the passport!";
        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 404;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Not found";
        this.error = true;
      }

      return response;
    },
    async declineNegotiation(token, processId) {
      let response = null;
      // Get Passport in Backend
      try {
        // Init backendService
        // Get access token from IDP
        // Get the aspect for the selected version

        response = await this.backendService.declineNegotiation(
          token,
          processId,
          this.auth
        );
      } catch (e) {
        console.log("passportView.declineNegotiation() -> " + e);
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "Failed to return passport";
        this.errorObj.description =
          "It was not possible to transfer the passport.";

        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 500;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Internal Server Error";
        this.error = true;
        return response;
      }

      // Check if the response is empty and give an error
      if (!response || response.status != 200) {
        this.errorObj.title = "Failed to return passport";
        this.errorObj.description =
          "It was not possible to complete the passport transfer.";
        this.errorObj.status = 400;
        this.errorObj.statusText = "Bad Request";
        this.error = true;
        return null;
      }

      // Check if reponse content was successfull and if not print error comming message from backend
      if (jsonUtil.exists("status", response) && response["status"] != 200) {
        this.errorObj.title = jsonUtil.exists("message", response)
          ? response["message"]
          : "An error occured when searching for the passport!";
        this.errorObj.description =
          "An error occured when searching for the passport!";
        this.errorObj.status = jsonUtil.exists("status", response)
          ? response["status"]
          : 404;

        this.errorObj.statusText = jsonUtil.exists("statusText", response)
          ? response["statusText"]
          : "Not found";
        this.error = true;
      }

      return response;
    },
  },
};
</script>

