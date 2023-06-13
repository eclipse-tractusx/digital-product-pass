<!-- eslint-disable vue/no-v-for-template-key -->
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

<template >
  <div class="section">
    <v-row class="section">
      <div>dawdasdasd</div>
      <h2>daw</h2>
    </v-row>
  </div>
</template>

<script>
import Field from "../Field.vue";
import FieldBox from "../../general/FieldBox.vue";
export default {
  name: "ContractInformation",
  components: {
    Field,
    FieldBox,
  },
  props: {
    sectionTitle: {
      type: String,
      required: false,
      default: "",
    },
    data: {
      type: Object,
      default: Object,
    },
  },
  data() {
    return {
      toggle: false,
      propsData: {
        negotiation: {
          createdAt: 1679579334111,
          updatedAt: 1679579340178,
          state: "CONFIRMED",
          type: "CONSUMER",
          id: "8f1f59e4-490f-4aad-83fa-fface6e849b0",
          contractAgreementId: "2:4ede1eb4-81d3-475f-98e9-b2db2d429556",
          counterPartyAddress:
            "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data",
          protocol: "ids-multipart",
        },
        contractOffer: {
          connectorId: "2937009c-d5c0-4c7c-8a18-7e2cb831b739",
          id: "2:2937009c-d5c0-4c7c-8a18-7e2cb831b739",
          policy: {
            permissions: [
              {
                edctype: "dataspaceconnector:permission",
                uid: null,
                target: "NCR186850B",
                action: {
                  type: "USE",
                  includedIn: null,
                  constraint: null,
                },
                assignee: null,
                assigner: null,
                constraints: [],
                duties: [],
              },
            ],
            prohibitions: [],
            obligations: [],
            extensibleProperties: {},
            target: "NCR186850B",
            "@type": {
              "@policytype": "set",
            },
          },
          asset: {
            id: "NCR186850B",
            createdAt: "1679579334105",
            properties: {
              "asset:prop:byteSize": null,
              "asset:prop:description": "Battery Passport test data",
              "asset:prop:id": "NCR186850B",
              "asset:prop:fileName": null,
            },
          },
          assetId: "2",
          provider: "urn:connector:provider",
          consumer: "urn:connector:consumer",
          offerId: "2:2937009c-d5c0-4c7c-8a18-7e2cb831b739",
        },
        transfer: {
          id: "2dbb60d8-8c56-4175-95c5-4c1355206e21",
          createdAt: "1679579340194",
          updatedAt: "1679579350085",
          type: "CONSUMER",
          state: "COMPLETED",
          stateTimestamp: 1679579350085,
          dataRequest: {
            assetId: "NCR186850B",
            contractId: "2:4ede1eb4-81d3-475f-98e9-b2db2d429556",
            connectorId: "MaterialPass",
          },
          dataDestination: {
            properties: {
              type: "HttpProxy",
            },
          },
        },
        transferRequest: {
          id: "70e85af6e338e1cfa0c1fdac785dfdda1290a434a95e7c8046f15df466fc97f0",
          connectorId: "MaterialPass",
          connectorAddress:
            "https://materialpass.int.demo.catena-x.net/provider/api/v1/ids/data",
          contractId: "2:4ede1eb4-81d3-475f-98e9-b2db2d429556",
          assetId: "NCR186850B",
          managedResources: false,
          dataDestination: {
            type: "HttpProxy",
          },
        },
      },
    };
  },
};
</script>
