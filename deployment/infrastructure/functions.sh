#!/bin/bash
#################################################################################
# Catena-X - Product Passport Consumer Application
#
# Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License, Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
# either express or implied. See the
# License for the specific language govern in permissions and limitations
# under the License.
#
# SPDX-License-Identifier: Apache-2.0
#################################################################################

# ./upload-testdata.sh -s -e -a -k -b

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

# global variables
UUID=''
ASSET_ID=''
POLICY_ID=''
CONTRACT_DEF_ID=''
PASSPORT_ID=''
HTTP_RESPONSE=''
PREFIX='urn:uuid'

generate_UUID () {
  local uuid=$(openssl rand -hex 16)
  UUID=${PREFIX}:${uuid:0:8}-${uuid:8:4}-${uuid:12:4}-${uuid:16:4}-${uuid:20:12}
}

display_message () {

  echo
  echo "++++++++++++++++++ Passport ID: " ${PASSPORT_ID} "++++++++++++++++++++++"
  echo
}

check_status_code () {
  if [[ "$HTTP_RESPONSE" -eq 200 ]] ; then
    echo "[$HTTP_RESPONSE] - OK"
  elif [[ "$HTTP_RESPONSE" -eq 201 ]] ; then
    echo "[$HTTP_RESPONSE] - Created"
  elif [[ "$HTTP_RESPONSE" -eq 400 ]] ; then
    echo "[$HTTP_RESPONSE] - Bad Request"
  elif [[ "$HTTP_RESPONSE" -eq 409 ]] ; then
    echo "[$HTTP_RESPONSE] - Conflict: the object already exists"
  else
    echo "[$HTTP_RESPONSE] - Internal Server Error"
  fi
}

create_edc_asset () {

  generate_UUID
  ASSET_ID=${UUID}

  PAYLOAD='{
    "@context": {},
    "asset": {
        "@type": "Asset",
        "@id": "'${ASSET_ID}'", 
        "properties": {
            "description": "'${PASSPORT_TYPE}' test data"
        }
    },
    "dataAddress": {
        "@type": "DataAddress",
        "type": "HttpData",
        "proxyPath": "true",
        "proxyBody": "true",
        "proxyMethod": "true",
        "proxyQueryParams": "true",
        "baseUrl": "'${SUBMODEL_SERVER}'"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/assets)
  check_status_code
  echo "[${PASSPORT_TYPE}] - edc asset created with uuid : " ${ASSET_ID}
}

create_registry_asset () {
  PAYLOAD='{
    "@context": {},
    "asset": {
        "@type": "data.core.digitalTwinRegistry",
        "@id": "'${REGISTRY_ASSET_ID}'", 
        "properties": {
            "type": "data.core.digitalTwinRegistry",
            "description": "Digital Twin Registry for DPP",
            "contenttype": "application/json" 
        }
    },
    "dataAddress": {
        "@type": "DataAddress",
        "type": "HttpData",
        "proxyPath": "true",
        "proxyBody": "true",
        "proxyMethod": "true",
        "proxyQueryParams": "true",
        "baseUrl": "'${REGISTRY_URL}'"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/assets)
  check_status_code
  echo "registry asset created with uuid : registry-asset"
}

create_default_policy () {
  PAYLOAD='{
    "@context": {
        "odrl": "http://www.w3.org/ns/odrl/2/leftOperand"
    },
    "@type": "PolicyDefinitionRequestDto",
    "@id": "default-policy",
    "policy": {
		"@type": "Policy",
		"odrl:permission" : [{
      "odrl:action": "USE",
      "odrl:constraint": {
        "odrl:constraint": {
          "@type": "LogicalConstraint",
          "odrl:or": [
            {
              "@type": "Contraint",
              "odrl:leftOperand": "BusinessPartnerNumber",
              "odrl:operator": "EQ",
              "odrl:rightOperand": "'${BPN}'"
            }
          ]
        }
      }
    }]
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/policydefinitions)
  check_status_code
  echo "policy created with uuid : default-policy"
}

create_policy () {

  generate_UUID
  POLICY_ID=${UUID}

  PAYLOAD='{
    "@context": {
        "odrl": "http://www.w3.org/ns/odrl/2/"
    },
    "@type": "PolicyDefinitionRequestDto",
    "@id": "'${POLICY_ID}'",
    "policy": {
        "@type": "Policy",
        "odrl:permission": [
            {
                "odrl:action": "USE",
                "odrl:constraint": {
                    "@type": "AtomicConstraint",
                    "odrl:or": [
                        {
                            "@type": "Constraint",
                            "odrl:leftOperand": "PURPOSE",
                            "odrl:operator": {
                                "@id": "odrl:eq"
                            },
                            "odrl:rightOperand": "ID 3.0 Trace"
                        }
                    ]
                }
            }
        ]
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/policydefinitions)
  check_status_code
  echo "policy created with uuid : ${POLICY_ID}"
}

create_default_contractdefinition () {
  
  PAYLOAD='{
    "@context": {},
    "@id": "default-contract-definition",
    "@type": "ContractDefinition",
    "accessPolicyId": "default-policy",
    "contractPolicyId": "default-policy",
    "assetsSelector" : {
        "@type": "CriterionDto",
        "operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
        "operator": "=",
        "operandRight": "'${REGISTRY_ASSET_ID}'"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/contractdefinitions)
  check_status_code
  echo "contract created with uuid : default-contract-definition"
}

create_contractdefinition () {

  # since contract id does not support urn:uuid as prefix, generate a new one with the prerfix 
  UUID=$(openssl rand -hex 16)
  CONTRACT_DEF_ID=${UUID:0:8}-${UUID:8:4}-${UUID:12:4}-${UUID:16:4}-${UUID:20:12}

  PAYLOAD='{
    "@context": {},
    "@id": "'${CONTRACT_DEF_ID}'",
    "@type": "ContractDefinition",
    "accessPolicyId": "'${POLICY_ID}'",
    "contractPolicyId": "'${POLICY_ID}'",
    "assetsSelector" : {
        "@type": "CriterionDto",
        "operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
        "operator": "=",
        "operandRight": "'${ASSET_ID}'"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/contractdefinitions)
  check_status_code
  echo "contract created with id : ${CONTRACT_DEF_ID}"
}

create_batterypass_aas_payload (){

  BATTERY_ID=$1
  AAS_ID=$2
  PAYLOAD='{
      "description": [
          {
              "language": "en",
              "text": "Battery Passport shell descriptor"
          }
      ],
      "idShort": "Battery_'${BATTERY_ID}'",
      "globalAssetId": "'${AAS_ID}'",
      "id": "'${AAS_ID}'",
      "specificAssetIds": [
          {
              "name": "partInstanceId",
              "value": "'${BATTERY_ID}'",
              "externalSubjectId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "GlobalReference",
                          "value": "'${BPN}'"
                      }
                  ]
              }
          },
          {
              "name": "manufacturerPartId",
              "value": "'${MANUFACTURER_PART_ID}'",
              "externalSubjectId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "GlobalReference",
                          "value": "'${BPN}'"
                      },
                      {
                          "type": "GlobalReference",
                          "value": "PUBLIC_READABLE"
                      }
                  ]
              }
          }
      ],
      "submodelDescriptors": [
          {
              "endpoints": [
                  {
                      "interface": "SUBMODEL-3.0",
                      "protocolInformation": {
                          "href": "'${PROVIDER_EDC}'/api/public/data/'${SUBMODEL_ID}'",
                          "endpointProtocol": "HTTP",
                          "endpointProtocolVersion": [
                            "1.1"
                          ],
                          "subprotocol": "DSP",
                          "subprotocolBody": "id='${ASSET_ID}';dspEndpoint='${PROVIDER_EDC}'",
                          "subprotocolBodyEncoding": "plain",
                          "securityAttributes": [
                            {
                              "type": "NONE",
                              "key": "NONE",
                              "value": "NONE"
                            }
                          ]
                      }
                  }
              ],
              "idShort": "batteryPass",
              "id": "'${SUBMODEL_ID}'",
              "semanticId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "Submodel",
                          "value": "urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass"
                      }
                  ]
              },
              "description": [
                  {
                      "language": "en",
                      "text": "Battery Passport Submodel"
                  }
              ]
          }
      ]
    }'
  HTTP_RESPONSE=$(curl -X POST -s -H 'Content-Type: application/json'  --data "${PAYLOAD}" -o /dev/null -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
  check_status_code
  echo "[BatteryPass] - AAS shell created with uuid : ${AAS_ID}"
}

create_transmissionpass_aas_payload (){

  GEARBOX_ID=$1
  AAS_ID=$2

  PAYLOAD='{
    "description": [
        {
            "language": "en",
            "text": "Transmission Passport shell descriptor"
        }
    ],
    "idShort": "Transmission_'${GEARBOX_ID}'",
    "globalAssetId": "'${AAS_ID}'",
    "id": "'${AAS_ID}'",
    "specificAssetIds": [
        {
            "name": "partInstanceId",
            "value": "'${GEARBOX_ID}'",
            "externalSubjectId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "GlobalReference",
                        "value": "'${BPN}'"
                    }
                ]
            }
        },
        {
            "name": "manufacturerPartId",
            "value": "'${MANUFACTURER_PART_ID}'",
            "externalSubjectId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "GlobalReference",
                        "value": "'${BPN}'"
                    },
                    {
                        "type": "GlobalReference",
                        "value": "PUBLIC_READABLE"
                    }
                ]
            }
        }
    ],
    "submodelDescriptors": [
        {
            "endpoints": [
                {
                    "interface": "SUBMODEL-3.0",
                    "protocolInformation": {
                        "href": "'${PROVIDER_EDC}'/api/public/data/'${SUBMODEL_ID}'",
                        "endpointProtocol": "HTTP",
                        "endpointProtocolVersion": [
                          "1.1"
                        ],
                        "subprotocol": "DSP",
                        "subprotocolBody": "id='${ASSET_ID}';dspEndpoint='${PROVIDER_EDC}'",
                        "subprotocolBodyEncoding": "plain",
                        "securityAttributes": [
                          {
                            "type": "NONE",
                            "key": "NONE",
                            "value": "NONE"
                          }
                        ]
                    }
                }
            ],
            "idShort": "transmissionPass",
            "id": "'${SUBMODEL_ID}'",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"
                    }
                ]
            },
            "description": [
                {
                    "language": "en",
                    "text": "Transmission Passport Submodel"
                }
            ]
        }
    ]
  }'
  HTTP_RESPONSE=$(curl -X POST -s -H 'Content-Type: application/json'  --data "${PAYLOAD}" -o /dev/null -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
  check_status_code
  echo "AAS shell created with uuid : ${AAS_ID}"
}

create_smc_aas_payload (){

  MATERIAL_ID=$1
  AAS_ID=$2
  PAYLOAD='{
      "description": [
          {
              "language": "en",
              "text": "Secondary Material Content"
          }
      ],
      "idShort": "SMC_'${MATERIAL_ID}'",
      "globalAssetId": "'${AAS_ID}'",
      "id": "'${AAS_ID}'",
      "specificAssetIds": [
          {
              "name": "orderNumber",
              "value": "'${MATERIAL_ID}'",
              "externalSubjectId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "GlobalReference",
                          "value": "'${BPN}'"
                      },
                      {
                      "type": "GlobalReference",
                      "value": "BPNL00000003AYRE"
                      }
                  ]
              }
          },
          {
              "name": "manufacturerPartId",
              "value": "'${MANUFACTURER_PART_ID}'",
              "externalSubjectId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "GlobalReference",
                          "value": "'${BPN}'"
                      },
                      {
                      "type": "GlobalReference",
                      "value": "BPNL00000003AYRE"
                      },
                      {
                          "type": "GlobalReference",
                          "value": "PUBLIC_READABLE"
                      }
                  ]
              }
          }
      ],
      "submodelDescriptors": [
          {
              "endpoints": [
                  {
                      "interface": "SUBMODEL-3.0",
                      "protocolInformation": {
                          "href": "'${PROVIDER_EDC}'/api/public/data/'${SERIAL_PART_ASPECT_ID}'",
                          "endpointProtocol": "HTTP",
                          "endpointProtocolVersion": [
                            "1.1"
                          ],
                          "subprotocol": "DSP",
                          "subprotocolBody": "id='${ASSET_ID}';dspEndpoint='${PROVIDER_EDC}'",
                          "subprotocolBodyEncoding": "plain",
                          "securityAttributes": [
                            {
                              "type": "NONE",
                              "key": "NONE",
                              "value": "NONE"
                            }
                          ]
                      }
                  }
              ],
              "idShort": "SerialPart",
              "id": "'${SERIAL_PART_ASPECT_ID}'",
              "semanticId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "Submodel",
                          "value": "urn:bamm:io.catenax.serial_part:1.0.1#SerialPart"
                      }
                  ]
              },
              "description": [],
              "displayName": []
          },
          {
              "endpoints": [
                  {
                      "interface": "SUBMODEL-3.0",
                      "protocolInformation": {
                          "href": "'${PROVIDER_EDC}'/api/public/data/'${SUBMODEL_ID}'",
                          "endpointProtocol": "HTTP",
                          "endpointProtocolVersion": [
                            "1.1"
                          ],
                          "subprotocol": "DSP",
                          "subprotocolBody": "id='${ASSET_ID}';dspEndpoint='${PROVIDER_EDC}'",
                          "subprotocolBodyEncoding": "plain",
                          "securityAttributes": [
                            {
                              "type": "NONE",
                              "key": "NONE",
                              "value": "NONE"
                            }
                          ]
                      }
                  }
              ],
              "idShort": "secondaryMaterialContent",
              "id": "'${SUBMODEL_ID}'",
              "semanticId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "Submodel",
                          "value": "urn:samm:io.catenax.secondary_material_content:1.0.0#SecondaryMaterialContent"
                      }
                  ]
              },
              "description": [],
              "displayName": []
          }
      ]
    }'
  HTTP_RESPONSE=$(curl -X POST -s -H 'Content-Type: application/json'  --data "${PAYLOAD}" -o /dev/null -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
  check_status_code
  echo "[SecondaryMaterialContent] - AAS shell created with uuid : ${AAS_ID}"

}


create_aas3_shell () {

  PASSPORT_ID=$1
  PASSPORT_TYPE=$2
  generate_UUID
  AAS_ID=${UUID}

  if [[ $PASSPORT_TYPE = "batterypass" ]] ; then
    create_batterypass_aas_payload ${PASSPORT_ID} ${AAS_ID}
  elif [[ $PASSPORT_TYPE = "transmissionpass" ]] ; then
    create_transmissionpass_aas_payload ${PASSPORT_ID} ${AAS_ID}
  elif [[ $PASSPORT_TYPE = "secondarymaterialcontent" ]] ; then
    create_smc_aas_payload ${PASSPORT_ID} ${AAS_ID}
  fi;

}
