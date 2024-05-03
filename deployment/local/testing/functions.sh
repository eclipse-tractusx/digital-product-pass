#!/bin/bash
#################################################################################
# Tractus-X - Digital Product Passport Application
#
# Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
# Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
# Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
declare -a ARRAY_SUBMODELS=[]
declare -a BATCH_IDS=[]

generate_UUID () {
  local uuid=$(openssl rand -hex 16)
  UUID=${PREFIX}:${uuid:0:8}-${uuid:8:4}-${uuid:12:4}-${uuid:16:4}-${uuid:20:12}
}

check_status_code () {
  success_message=$1

  if [[ "$HTTP_RESPONSE" -eq 200 ]] ; then
    echo "RESPONSE CODE: [$HTTP_RESPONSE] - OK"
    echo "${success_message}"
  elif [[ "$HTTP_RESPONSE" -eq 201 ]] ; then
    echo "RESPONSE CODE: [$HTTP_RESPONSE] - Created"
    echo "${success_message}"
  elif [[ "$HTTP_RESPONSE" -eq 400 ]] ; then
    echo "RESPONSE CODE: [$HTTP_RESPONSE] - Bad Request"
  elif [[ "$HTTP_RESPONSE" -eq 409 ]] ; then
    echo "RESPONSE CODE: [$HTTP_RESPONSE] - Conflict: the object already exists"
  else
    echo "RESPONSE CODE: [$HTTP_RESPONSE] - Internal Server Error"
  fi
}

create_edc_asset () {

  generate_UUID
  ASSET_ID=${UUID}
  
  payload='{
    "@context": {
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "cx-common": "https://w3id.org/catenax/ontology/common#",
        "cx-taxo": "https://w3id.org/catenax/taxonomy#",
        "dct": "https://purl.org/dc/terms/"
    },
    "@id": "'${ASSET_ID}'",
    "properties": {
        "type": {
            "@id": "Asset"
        }
    },
    "dataAddress": {
        "@type": "DataAddress",
        "type": "HttpData",
        "baseUrl": "'${SUBMODEL_SERVER}'",
        "proxyQueryParams": "true",
        "proxyPath": "true",
        "proxyMethod": "true",
        "proxyBody": "true"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -s -H 'Content-Type: application/json' --data "${payload}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v3/assets)
  check_status_code "[DPP] - edc asset created with uuid : ${ASSET_ID}"
}

create_registry_asset () {

  PAYLOAD='{
    "@context": {
        "edc": "https://w3id.org/edc/v0.0.1/ns/",
        "cx-common": "https://w3id.org/catenax/ontology/common#",
        "cx-taxo": "https://w3id.org/catenax/taxonomy#",
        "dct": "https://purl.org/dc/terms/"
    },
    "@id": "'${REGISTRY_ASSET_ID}'",
    "properties": {
        "type": {
            "@id": "DigitalTwinRegistry"
        },
        "version": "3.0",
        "asset:prop:type": "data.core.digitalTwinRegistry"
    },
    "dataAddress": {
        "@type": "DataAddress",
        "type": "HttpData",
        "baseUrl": "'${REGISTRY_URL}'",
        "proxyQueryParams": "true",
        "proxyPath": "true",
        "proxyMethod": "true",
        "proxyBody": "true"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v3/assets)
  check_status_code "registry asset created with uuid : registry-asset"
}

create_default_policy () {
  payload='{
    "@context": {
        "odrl": "http://www.w3.org/ns/odrl/2/leftOperand"
    },
    "@type": "PolicyDefinitionRequestDto",
    "@id": "default-policy",
    "policy": {
		"@type": "Policy",
		"odrl:permission": []
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${payload}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/policydefinitions)
  check_status_code "policy created with uuid : default-policy"
}

create_registry_policy () {
  policy=$1
  POLICY_ID=$(echo "${policy}" | jq -r '.["@id"]')


  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${policy}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/policydefinitions)
  check_status_code "policy created with uuid : registry-policy"
}

create_edc_policy () {

  policy=$1
  POLICY_ID=$(echo "${policy}" | jq -r '.["@id"]')

  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${policy}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/policydefinitions)
  check_status_code "policy created with uuid : ${POLICY_ID}"
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
  check_status_code "contract created with uuid : default-contract-definition"
}

create_registry_contractdefinition () {
  
  PAYLOAD='{
    "@context": {},
    "@id": "registry-contract-definition",
    "@type": "ContractDefinition",
    "accessPolicyId": "registry-policy",
    "contractPolicyId": "registry-policy",
    "assetsSelector" : {
        "@type": "CriterionDto",
        "operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
        "operator": "=",
        "operandRight": "'${REGISTRY_ASSET_ID}'"
    }
  }'
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${PAYLOAD}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n" ${PROVIDER_EDC}/management/v2/contractdefinitions)
  check_status_code "contract created with uuid : registry-contract-definition"
}

create_contractdefinition () {

  # since contract id does not support urn:uuid as prefix, generate a new one with the prerfix 
  UUID=$(openssl rand -hex 16)
  CONTRACT_DEF_ID=${UUID:0:8}-${UUID:8:4}-${UUID:12:4}-${UUID:16:4}-${UUID:20:12}

  payload='{
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

  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${payload}" --header 'X-Api-Key: '${API_KEY} -o /dev/null -w "%{http_code}\n"  ${PROVIDER_EDC}/management/v2/contractdefinitions)
  check_status_code "contract created with id : ${CONTRACT_DEF_ID}"
}

create_submodel_payload (){

  submodels=$1
  type=$2
  noOfSubmodels=$(echo ${submodels}  | jq -r '. | length')
 
  echo "***********************Create Submodel data*****************************************"
  for ((i = 0; i < $noOfSubmodels; i++)); do

    generate_UUID
    SUBMODEL_ID=${UUID}

    # -- prepare the submodel data with the generated SUBMODEL_ID
    data=$(echo "${submodels}" | jq -r ".[$i].data")
    HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${data}" -o /dev/null -w "%{http_code}\n" $SUBMODEL_SERVER/data/${SUBMODEL_ID})
    check_status_code "[${type}] - Created submodel data with uuid: ${SUBMODEL_ID}"

    submodelIdShort=$(echo "${submodels}" | jq -r ".[$i].name")
    semanticId=$(echo "${submodels}" | jq -r ".[$i].semanticId")
    description=$(echo "${submodels}" | jq -r ".[$i].description")
    
    submodelDescriptor=$(cat <<EOF
      {
      "endpoints": [
          {
              "interface": "SUBMODEL-3.0",
              "protocolInformation": {
                  "href": "${PROVIDER_EDC}/api/public/data/${SUBMODEL_ID}",
                  "endpointProtocol": "HTTP",
                  "endpointProtocolVersion": [
                      "1.1"
                  ],
                  "subprotocol": "DSP",
                  "subprotocolBody": "id=${ASSET_ID};dspEndpoint=${PROVIDER_EDC}",
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
      "idShort": "${submodelIdShort}",
      "id": "${SUBMODEL_ID}",
      "semanticId": {
          "type": "ExternalReference",
          "keys": [
              {
                  "type": "Submodel",
                  "value": "${semanticId}"
              }
          ]
      },
      "description": [
          {
              "language": "en",
              "text": "${description}"
          }
      ]
    }
EOF
)
  # -- adding a submodel to the array of submodels 
  ARRAY_SUBMODELS=$(echo "${ARRAY_SUBMODELS}" | jq ". + [${submodelDescriptor}]")
  done
} 

prepare_specific_asset_ids (){

  lookupIds=$1

  length=$(echo ${lookupIds} | jq -r '. | length')
  # -- create specific asset for each search id
  for ((i = 0; i < $length; i++)); do

    declare -a keys=()
    name=$(echo "${lookupIds}" | jq -r ".[$i].name")
    value=$(echo "${lookupIds}" | jq -r ".[$i].value")
    allowedBpns=$(echo "${lookupIds}" | jq -r ".[$i].allowedBpns")
    lengthOfBpns=$(echo ${allowedBpns} | jq -r '. | length')

    # -- create payload for each bpn
    for ((j = 0; j < $lengthOfBpns; j++)); do
      bpn=$(echo ${allowedBpns} | jq -r ".[$j]")
      key=$(cat <<EOF
      {
        "type": "GlobalReference",
        "value": "${bpn}"
      }
EOF
)
    
      if (( ${#keys[@]} == 0 )); then
        keys+=${key}
      else
        keys+=", ${key}"
      fi;
    done

    # -- append PUBLIC_READABLE json object to the existing specificAssetIds array
    # -- this only applies to the manufacturerPartId object
    if [[ "$name" == "manufacturerPartId" ]]; then
      keys+=',
      {
        "type": "GlobalReference",
        "value": "PUBLIC_READABLE"
      }'
    fi;

    specificAssetIds=$(cat <<EOF
    {
      "name": "${name}",
      "value": "${value}",
      "externalSubjectId": {
        "type": "ExternalReference",
        "keys": [
         ${keys}
       ]
     }
    }
EOF
)
  # -- adding processed specificAssetIds to the array of specificAssetIds 
  BATCH_IDS=$(echo "${BATCH_IDS}" | jq ". + [${specificAssetIds}]")
  done
}

create_aas3_shell (){

  shell=$1
  declare -a specificAssetIds=()
  allowedBpns=()
  idShort=''

  local submodels=$(echo ${shell} | jq -r ".submodels")
  local specificAssetIds=$(echo ${shell} | jq -r ".specificAssetIds")
  local globalAssetId=$(echo ${shell} | jq -r ".catenaXId")
  local type=$(echo ${shell} | jq -r ".type")
  local shellDescription=$(echo ${shell} | jq -r ".description")
  
  create_submodel_payload "${submodels}" "${type}"
  submodelDescriptors=$ARRAY_SUBMODELS
  generate_UUID
  aasId=${UUID}
  # -- check if partInstanceId exists or not, if not then print error message and exit
  isExists=false

  # -- these are two mandatory search fields required for the dpp
  searchIds=$(echo ${specificAssetIds}  | jq '.')
  length=$(echo ${searchIds}  | jq '. | length')
  for ((i = 0; i < $length; i++)); do
    name=$(echo ${searchIds}  | jq -r ".[$i].name")
    if [[ ${name} == "partInstanceId" ]]; then
      isExists=true
      break;
    fi;
  done

  if [[ $isExists != true ]]; then
    echo "The attribute partInstanceId must not be null or empty. Please set the partInstanceId in ${globalAssetId}.specificAssetIds[]"
    exit 1
  fi;
  partInstanceId=$(echo ${searchIds}  | jq -r ".[$i].value")
  idShort="${type}_${partInstanceId}"

  prepare_specific_asset_ids "${specificAssetIds}"

  payload=$(cat <<EOF
    {
    "description": ${shellDescription},
    "idShort": "${idShort}",
    "globalAssetId": "${globalAssetId}",
    "id": "${aasId}",
    "specificAssetIds": ${BATCH_IDS},
    "submodelDescriptors": ${submodelDescriptors}
  }
EOF
)
    echo "Adding Digital Twin to the aas registry: ${aasId}"
    # printf "Digital Twin %s: ${payload} "
    HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' -s --data "${payload}" -o /dev/null -w "%{http_code}\n" $REGISTRY_URL/shell-descriptors)
    check_status_code "[${type}] - AAS shell created with uuid : ${aasId}"
    BATCH_IDS=[]
    ARRAY_SUBMODELS=[]
    echo
}
