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

DIGITAL_TWIN_1='urn:uuid:3d050cd8-cdc7-4d65-9f37-70a65d5f53f5'
DT1_SUBMODEL_ID_1='urn:uuid:777a3f0a-6d29-4fcd-81ea-1c27c1b870cc'
DT1_SUBMODEL_ID_2='urn:uuid:09d5d8a9-9073-47b6-93c6-80caff176dca'

DIGITAL_TWIN_2='urn:uuid:ace301f6-92c5-4623-a022-c2a30dfee0e2'
DT2_SUBMODEL_ID_1='urn:uuid:754b6c6c-d74a-4dd0-a62c-f07959f15332'
DT2_SUBMODEL_ID_2='urn:uuid:25ea2646-d57f-4b31-97a0-d0d7b3b35d37'
DT2_SUBMODEL_ID_3='urn:uuid:c216bece-b17f-4679-8b62-ec25810ca1c4'

DIGITAL_TWIN_3='urn:uuid:d3e7cc6c-0e9b-49db-8d0d-25c6a1e68690'
DT3_SUBMODEL_ID_1='urn:uuid:0f8eb434-32af-48cb-8dc2-6391fb3d8aa8'
DT3_SUBMODEL_ID_2='urn:uuid:7e3f0673-fa92-43c3-af0d-e86485a97bda'
DT3_SUBMODEL_ID_3='urn:uuid:d7f23694-6d38-44ca-8ba5-3256e3b4b219'

SERVER_URL='<data-provider-url>'
REGISTRY_URL='<registry-url>/api/v3.0/shell-descriptors'
SUBMODEL_SERVER='<data-server-url>'

API_KEY=''

DPP_ASSET='urn:uuid:3e4a5957-f226-478a-ab18-79ced49d6195'
REGISTRY_ASSET='registry-asset'

echo '**************************Asset 1 - Digital Twin Registry **********************'
echo

# Create a asset
echo "Create asset 1 - DTR..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/assets/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 1 - DTR..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/contractpolicies/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 1 - DTR..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/contractdefinitions/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo
echo


echo '**************************Battery**********************'
echo
# Create Submodel data
echo "Create battery sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/BAT-XYZ789.json"  $SUBMODEL_SERVER/provider_backend/data/${DT1_SUBMODEL_ID_1}
echo

echo "Create Battery BomAsBuilt sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/singleLevelBomAsBuilt-BAT-XYZ789.json"  $SUBMODEL_SERVER/provider_backend/data/${DT1_SUBMODEL_ID_2}
echo

# Create a asset
echo "Create asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/assets/DT-BAT-XYZ789.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/assets
echo

# Create a general policy
echo "Create policy for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/contractpolicies/DT-BAT-XYZ789.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/contractdefinitions/DT-BAT-XYZ789.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/management/v2/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 2 and register it devo CX registry..."

curl -X POST -s -H 'Content-Type: application/json' --data "@resources/irs/digitaltwins/DT-BAT-XYZ789+BOMAsBuilt.json"  $REGISTRY_URL
echo
echo



echo '**************************Battery Pack**********************'

echo 
# Create Submodel data
echo "Create Battery pack sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/EVPACK-TRJ712.json"  $SUBMODEL_SERVER/provider_backend/data/${DT2_SUBMODEL_ID_1}
echo

echo "Create Battery pack BomAsBuilt sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/singleLevelUsageAsBuilt-EVPACK-TRJ712.json"  $SUBMODEL_SERVER/provider_backend/data/${DT2_SUBMODEL_ID_2}
echo

echo "Create Battery pack BomAsUsage sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/singleLevelBomAsBuilt-EVPACK-TRJ712.json"  $SUBMODEL_SERVER/provider_backend/data/${DT2_SUBMODEL_ID_3}
echo


# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 3 and register it devo CX registry..."

curl -X POST -s -H 'Content-Type: application/json' --data "@resources/irs/digitaltwins/DT-EVPACK-TRJ712+BOMAsBuilt.json" $REGISTRY_URL
echo
echo



echo '**************************Battery Cell**********************'
# Create Submodel data
echo "Create Battery Cell sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/singleLevelBOMAsBuilt-CTA-13123.json"   $SUBMODEL_SERVER/provider_backend/data/${DT3_SUBMODEL_ID_1}
echo

echo "Create Battery Cell BomAsBuilt sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/CTA-13123.json"   $SUBMODEL_SERVER/provider_backend/data/${DT3_SUBMODEL_ID_2}
echo

echo "Create Battery Cell BomAsUsage sample data..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/payloads/singleLevelUsageAsBuilt-CTA-13123.json"   $SUBMODEL_SERVER/provider_backend/data/${DT2_SUBMODEL_ID_3}
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 4 and register it devo CX registry..."

curl -X POST -s -H 'Content-Type: application/json' --data "@resources/irs/digitaltwins/DT-CTA-13123+BOMAsBuilt.json" $REGISTRY_URL
echo

echo 'Provider setup completed...'
echo 'Done'
