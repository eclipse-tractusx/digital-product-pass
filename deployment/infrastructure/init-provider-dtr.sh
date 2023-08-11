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

DIGITAL_TWIN_1='32aa72de-297a-4405-9148-13e12744028a'
DIGITAL_TWIN_SUBMODEL_ID_1='699f1245-f57e-4d6b-acdb-ab763665554a'

DIGITAL_TWIN_2='1f4a64f0-aba9-498a-917c-4936c24c50cd'
DIGITAL_TWIN_SUBMODEL_ID_2='49a06ad2-64b7-46c8-9f3b-a718c462ca23'

DIGITAL_TWIN_3='365e6fbe-bb34-11ec-8422-0242ac120002'
DIGITAL_TWIN_SUBMODEL_ID_3='61125dc3-5e6f-4f4b-838d-447432b97918'

DIGITAL_TWIN_4='1f0ef836-40b7-4f31-a9bd-cb6a8960779e'
DIGITAL_TWIN_SUBMODEL_ID_4='26bf39c5-68a5-43a1-8db7-d33e116a6f61'

SERVER_URL='<data-provider-url>'
REGISTRY_URL='<registry-url/api/v3.0/shell-descriptors>'


# put access token without 'Bearer ' prefix
BEARER_TOKEN=''

API_KEY=''

ASSET_ID_1=${DIGITAL_TWIN_1}-${DIGITAL_TWIN_SUBMODEL_ID_1}
ASSET_ID_2=${DIGITAL_TWIN_2}-${DIGITAL_TWIN_SUBMODEL_ID_2}
ASSET_ID_3=${DIGITAL_TWIN_3}-${DIGITAL_TWIN_SUBMODEL_ID_3}
ASSET_ID_4=${DIGITAL_TWIN_4}-${DIGITAL_TWIN_SUBMODEL_ID_4}

echo '**************************Asset 1 - Digital Twin Registry **********************'
echo

# Create a asset
echo "Create asset 1 - DTR..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 1 - DTR..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 1 - DTR..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo
echo


echo '**************************Asset 2 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/X123456789012X12345678901234566.json"  $SERVER_URL/provider_backend/data/${ASSET_ID_1}
echo

# Create a asset
echo "Create asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 2 and register it devo CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/X123456789012X12345678901234566.json"  $REGISTRY_URL
echo
echo



echo '**************************Asset 3 **********************'

echo 
# Create Submodel data
echo "Create sample data for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/NCR186850B.json"  $SERVER_URL/provider_backend/data/${ASSET_ID_2}
echo

# Create a asset
echo "Create asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo


# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 3 and register it devo CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/NCR186850B.json" $REGISTRY_URL
echo
echo



echo '**************************Asset 4 **********************'
# Create Submodel data
echo "Create sample data for asset 4..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/IMR18650V1.json"   $SERVER_URL/provider_backend/data/${ASSET_ID_3}
echo

# Create a asset
echo "Create asset 4..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 4..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 4..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 4 and register it devo CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/IMR18650V1.json" $REGISTRY_URL
echo


echo '**************************Asset 5 **********************'
# Create Submodel data
echo "Create sample data for asset 5..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/Y792927456954B81677903848654570.json"   $SERVER_URL/provider_backend/data/${ASSET_ID_4}
echo

# Create a asset
echo "Create asset 5..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/Y792927456954B81677903848654570.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 5..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/Y792927456954B81677903848654570.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 5..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/Y792927456954B81677903848654570.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo


# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 5 and register it devo CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/Y792927456954B81677903848654570.json" $REGISTRY_URL
echo

echo 'Provider setup completed...'
echo 'Done'
