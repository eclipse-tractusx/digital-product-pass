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

DIGITAL_TWIN='urn:uuid:479ceed8-0eef-4c28-a74f-4eac9e01335b'
DT_SUBMODEL_ID_1='urn:uuid:d1707d00-d714-40a6-8229-11b14086029f'
DT_SUBMODEL_ID_2='urn:uuid:88d6d37e-4bac-4770-8583-f9a50a8b4a87'

SERVER_URL='<data-provider-url>'
REGISTRY_URL='<registry-url>/api/v3.0/shell-descriptors'
SUBMODEL_SERVER='<data-server-url>'

API_KEY=''

DPP_ASSET='urn:uuid:d0724e94-6fa5-4551-9e07-abba7a1ecd8b'
REGISTRY_ASSET='registry-asset'

echo '************************** Digital Twin Registry (DTR) EDC Asset **********************'
echo

# Create a asset
echo "Create DTR asset..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/assets/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} ${SUBMODEL_SERVER}/management/v2/assets
echo

# Create a general policy
echo "Create DTR policy..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/contractpolicies/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} ${SUBMODEL_SERVER}/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create DTR contract definition..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/irs/contractdefinitions/digital-twin-registry.json" --header 'X-Api-Key: '${API_KEY} ${SUBMODEL_SERVER}/management/v2/contractdefinitions
echo
echo

echo '************************** SMC EDC Asset **********************'
echo

# Create a asset
echo "Create SMC EDC asset..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/smc/smc_asset.json" --header 'X-Api-Key: '${API_KEY} ${SUBMODEL_SERVER}/management/v2/assets
echo

# Create a general policy
echo "Create SMC EDC policy..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/smc/smc_policiy.json" --header 'X-Api-Key: '${API_KEY} ${SUBMODEL_SERVER}/management/v2/policydefinitions
echo

# Create a contract definition
echo "Create SMC EDC contract definition..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/smc/smc_contract-definition.json" --header 'X-Api-Key: '${API_KEY} ${SUBMODEL_SERVER}/management/v2/contractdefinitions
echo
echo


echo '************************** Secondary Material Content - SMC_KLZ-90-8564-96 **********************'
echo

# Create Submodel data
echo "Create sample data for SMC KLZ-90-8564-96..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/smc/smc_manual_payload.json"  ${SUBMODEL_SERVER}/${DT_SUBMODEL_ID_1}
echo

echo "Create BomAsBuilt sample data of SMC serialized part..."
curl -X POST -s -H 'Content-Type: application/json' --data "@resources/smc/smc_payload_partAsPlanned.json"  ${SUBMODEL_SERVER}/${DT_SUBMODEL_ID_2}
echo

# Create a digital twin
echo "Create a Digital Twin of SMC KLZ-90-8564-96..."

curl -X POST -s -H 'Content-Type: application/json' --data "@resources/smc/smc_digital-twin.json"  ${REGISTRY_URL}
echo
echo

echo 'DPP test data upload completed...'
echo 'Done'
