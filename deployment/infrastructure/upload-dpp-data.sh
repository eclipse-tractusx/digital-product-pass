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

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

# script parameters
SUBMODEL_SERVER=$1
PROVIDER_EDC=$2
REGISTRY_URL=$3
API_KEY=$4
BPN=$5

# global variables
UUID=''
ASSET_ID=''
POLICY_ID=''
CONTRACT_DEF_ID=''
DT_ID=''
SUBMODEL_ID=''
PASSPORT_ID=''
HTTP_RESPONSE=''
BAT_ASPECT_ID=''
BAT_SINGLE_LEVEL_BOM_AS_BUILT_ID=''
BAT_MODULE_ID=''
BAT_MODULE_SINGLE_LEVEL_BOM_AS_BUILT_ID=''
BAT_MODULE_SINGLE_LEVEL_USAGE_AS_BUILT_ID=''
BAT_CELL_ASPECT_ID=''
BAT_CELL_SINGLE_LEVEL_BOM_AS_BUILT_ID=''
BAT_CELL_SINGLE_LEVEL_USAGE_AS_BUILT_ID=''


PREFIX='urn:uuid'
REGISTRY_ASSET_ID='registry-asset'
MANUFACTURER_PART_ID='XYZ78901'

# declare an array variable
declare -a passports=("BAT-XYZ789" "EVMODULE-TRJ712" "CTA-13123")
# decare fixed uuids for the reltionships
declare -a globalAssetIds=("urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d" "urn:uuid:d8ec6acc-1ad7-47b4-bc7e-612122d9d552" "urn:uuid:63b95496-86ed-4762-b248-491d5c1242e1")

display_message () {

  echo
  echo "++++++++++++++++++ Passport ID: " ${PASSPORT_ID} "++++++++++++++++++++++"
  echo
}

generate_submodel_id () {

  generate_UUID
  BAT_ASPECT_ID=${UUID} # digital product pass aspect
  generate_UUID
  BAT_SINGLE_LEVEL_BOM_AS_BUILT_ID=${UUID}

  generate_UUID
  BAT_MODULE_ID=${UUID}  # digital product pass aspect
  generate_UUID
  BAT_MODULE_SINGLE_LEVEL_BOM_AS_BUILT_ID=${UUID}
  generate_UUID
  BAT_MODULE_SINGLE_LEVEL_USAGE_AS_BUILT_ID=${UUID}

  generate_UUID
  BAT_CELL_ASPECT_ID=${UUID}  # digital product pass aspect
  generate_UUID
  BAT_CELL_SINGLE_LEVEL_BOM_AS_BUILT_ID=${UUID}
  generate_UUID
  BAT_CELL_SINGLE_LEVEL_USAGE_AS_BUILT_ID=${UUID}
}

generate_UUID () {
  local uuid=$(openssl rand -hex 16)
  UUID=${PREFIX}:${uuid:0:8}-${uuid:8:4}-${uuid:12:4}-${uuid:16:4}-${uuid:20:12}
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

create_submodel_payload () {

  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/${passports[0]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_ASPECT_ID})
  check_status_code
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/singleLevelBomAsBuilt-${passports[0]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_SINGLE_LEVEL_BOM_AS_BUILT_ID})
  check_status_code

  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/${passports[1]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_MODULE_ID})
  check_status_code
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/singleLevelBomAsBuilt-${passports[1]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_MODULE_SINGLE_LEVEL_BOM_AS_BUILT_ID})
  check_status_code
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/singleLevelUsageAsBuilt-${passports[1]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_MODULE_SINGLE_LEVEL_USAGE_AS_BUILT_ID})
  check_status_code

  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/${passports[2]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_CELL_ASPECT_ID})
  check_status_code
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/singleLevelBomAsBuilt-${passports[2]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_CELL_SINGLE_LEVEL_BOM_AS_BUILT_ID})
  check_status_code
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/singleLevelUsageAsBuilt-${passports[2]}.json" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${BAT_CELL_SINGLE_LEVEL_USAGE_AS_BUILT_ID})
  check_status_code
  
  echo "[DPP] - Submodel data upload complete"
}

create_edc_asset () {
  
  generate_UUID
  ASSET_ID=${UUID}

  PAYLOAD='{
    "@context": {
      "dct": "https://purl.org/dc/terms/",
      "tx": "https://w3id.org/tractusx/v0.0.1/ns/",
      "edc": "https://w3id.org/edc/v0.0.1/ns/",
      "dcat": "https://www.w3.org/ns/dcat/",
      "odrl": "http://www.w3.org/ns/odrl/2/",
      "dspace": "https://w3id.org/dspace/v0.8/"
    },
    "asset": {
        "@type": "Asset",
        "@id": "'${ASSET_ID}'", 
        "properties": {
            "description": "DPP EDC Test Asset"
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
  echo "[DPP] - edc asset creation complete with uuid : " ${ASSET_ID}
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
  echo "[DPP] - registry asset creation complete with uuid : registry-asset"
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
  echo "[DPP] - policy creation complete with uuid : default-policy"
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
  echo "[DPP] - policy creation complete with uuid : ${POLICY_ID}"
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
  echo "[DPP] - Contract creation complete with uuid : default-contract-definition"
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
  echo "[DPP] - Contract creation complete with id : ${CONTRACT_DEF_ID}"
}

create_battery_payload() {

  display_message
  PAYLOAD='{
      "description": [
          {
              "language": "en",
              "text": "Battery Digital Twin"
          }
      ],
      "idShort": "Battery_'${PASSPORT_ID}'",
      "globalAssetId": "'${globalAssetIds[0]}'",
      "id": "'${AAS_ID}'",
      "specificAssetIds": [
          {
              "name": "partInstanceId",
              "value": "'${PASSPORT_ID}'",
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
                          "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_ASPECT_ID}'",
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
              "idShort": "digitalProductPass",
              "id": "'${BAT_ASPECT_ID}'",
              "semanticId": {
                  "type": "ExternalReference",
                  "keys": [
                      {
                          "type": "Submodel",
                          "value": "urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"
                      }
                  ]
              },
              "description": [
                  {
                      "language": "en",
                      "text": "Digital Product Passport Submodel"
                  }
              ]
          },
          {
              "endpoints": [
                {
                    "interface": "SUBMODEL-3.0",
                    "protocolInformation": {
                        "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_SINGLE_LEVEL_BOM_AS_BUILT_ID}'",
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
            "idShort": "singleLevelBomAsBuilt",
            "id": "'${BAT_SINGLE_LEVEL_BOM_AS_BUILT_ID}'",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:bamm:io.catenax.single_level_bom_as_built:1.0.0#SingleLevelBomAsBuilt"
                    }
                ]
            },
            "description": [],
            "displayName": []
          }
      ]
    }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" -o /dev/null -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
  check_status_code
  echo "Battery digital twin created with uuid : ${AAS_ID}"

}

create_batteryModule_payload() {

  display_message
  PAYLOAD='{
    "description": [
        {
            "language": "en",
            "text": "Battery Module Digital Twin"
        }
    ],
    "idShort": "Battery_'${PASSPORT_ID}'",
    "globalAssetId": "'${globalAssetIds[1]}'",
    "id": "'${AAS_ID}'",
    "specificAssetIds": [
        {
            "name": "partInstanceId",
            "value": "'${PASSPORT_ID}'",
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
                        "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_MODULE_ID}'",
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
            "idShort": "digitalProductPass",
            "id": "'${BAT_MODULE_ID}'",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"
                    }
                ]
            },
            "description": [
                {
                    "language": "en",
                    "text": "Digital Product Passport Submodel"
                }
            ]
        },
        {
            "endpoints": [
              {
                  "interface": "SUBMODEL-3.0",
                  "protocolInformation": {
                      "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_MODULE_SINGLE_LEVEL_BOM_AS_BUILT_ID}'",
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
          "idShort": "singleLevelBomAsBuilt",
          "id": "'${BAT_MODULE_SINGLE_LEVEL_BOM_AS_BUILT_ID}'",
          "semanticId": {
              "type": "ExternalReference",
              "keys": [
                  {
                      "type": "Submodel",
                      "value": "urn:bamm:io.catenax.single_level_bom_as_built:1.0.0#SingleLevelBomAsBuilt"
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
                        "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_MODULE_SINGLE_LEVEL_USAGE_AS_BUILT_ID}'",
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
            "idShort": "SingleLevelUsageAsBuilt",
            "id": "'${BAT_MODULE_SINGLE_LEVEL_USAGE_AS_BUILT_ID}'",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:bamm:io.catenax.single_level_usage_as_built:1.0.1#SingleLevelUsageAsBuilt"
                    }
                ]
            },
            "description": [
                {
                    "language": "en",
                    "text": "Digital Product Passport Submodel"
                }
            ],
            "displayName": []
        }
    ]
  }'
  HTTP_RESPONSE=$(curl -X POST  -H 'Content-Type: application/json' -s --data "${PAYLOAD}" -o /dev/null  -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
  check_status_code
  echo "Battery module digital twin created with uuid : ${AAS_ID}"
}

create_batteryCell_payload() {

  display_message
  PAYLOAD='{
    "description": [
        {
            "language": "en",
            "text": "Battery Cell Digital Twin"
        }
    ],
    "idShort": "Battery_'${PASSPORT_ID}'",
    "globalAssetId": "'${globalAssetIds[2]}'",
    "id": "'${AAS_ID}'",
    "specificAssetIds": [
        {
            "name": "partInstanceId",
            "value": "'${PASSPORT_ID}'",
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
                        "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_CELL_ASPECT_ID}'",
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
            "idShort": "digitalProductPass",
            "id": "'${BAT_CELL_ASPECT_ID}'",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"
                    }
                ]
            },
            "description": [
                {
                    "language": "en",
                    "text": "Digital Product Passport Submodel"
                }
            ]
        },
        {
            "endpoints": [
              {
                  "interface": "SUBMODEL-3.0",
                  "protocolInformation": {
                      "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_CELL_SINGLE_LEVEL_BOM_AS_BUILT_ID}'",
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
          "idShort": "singleLevelBomAsBuilt",
          "id": "'${BAT_CELL_SINGLE_LEVEL_BOM_AS_BUILT_ID}'",
          "semanticId": {
              "type": "ExternalReference",
              "keys": [
                  {
                      "type": "Submodel",
                      "value": "urn:bamm:io.catenax.single_level_bom_as_built:1.0.0#SingleLevelBomAsBuilt"
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
                        "href": "'${PROVIDER_EDC}'/api/public/data/'${BAT_CELL_SINGLE_LEVEL_USAGE_AS_BUILT_ID}'",
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
            "idShort": "SingleLevelUsageAsBuilt",
            "id": "'${BAT_CELL_SINGLE_LEVEL_USAGE_AS_BUILT_ID}'",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:bamm:io.catenax.single_level_usage_as_built:1.0.1#SingleLevelUsageAsBuilt"
                    }
                ]
            },
            "description": [
                {
                    "language": "en",
                    "text": "Digital Product Passport Submodel"
                }
            ],
            "displayName": []
        }
    ]
  }'
  HTTP_RESPONSE=$(curl -X POST  -H 'Content-Type: application/json' -s --data "${PAYLOAD}" -o /dev/null -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
  check_status_code
  echo "Battery cell digital twin created with uuid : ${AAS_ID}"
}

create_aas3_shell () {

  # create and register battery digital twin
  generate_UUID
  AAS_ID=${UUID}
  PASSPORT_ID=${passports[0]}
  create_battery_payload

  # create and register battery module digital twin
  generate_UUID
  AAS_ID=${UUID}
  PASSPORT_ID=${passports[1]}
  create_batteryModule_payload

  # create and register battery cell digital twin
  generate_UUID
  AAS_ID=${UUID}
  PASSPORT_ID=${passports[2]}
  create_batteryCell_payload
}

# generate submodel uuids to be used in digital twin aspect models and to create submodel payloads
generate_submodel_id

# create edc assets, policies and contracts for the registry (DTR)
create_registry_asset
create_default_policy
create_default_contractdefinition

#create dpp asset, policy and contract
create_edc_asset
create_policy
create_contractdefinition
create_aas3_shell
create_submodel_payload
  
echo 'DPP test data upload complete...'
echo 'Done'
