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
export SUBMODEL_SERVER=$1
export PROVIDER_EDC=$2
export REGISTRY_URL=$3
export API_KEY=$4
export BPN=$5
export REGISTRY_ASSET_ID='registry-asset'
export MANUFACTURER_PART_ID='XYZ78901'
export SUBMODEL_ID=''
export SERIAL_PART_ASPECT_ID=''
export PASSPORT_TYPE="secondarymaterialcontent"

source ./functions.sh

# script global variables
UUID=''
MATERIAL_ID=''


# declare an array variable
declare -a smc_orders=("KLZ-90-8564-96")

create_submodel_payload () {

  generate_UUID
  SUBMODEL_ID=${UUID}
  echo "Create sample data for SMC KLZ-90-8564-96..."
  HTTP_RESPONSE=$(curl -X POST -H 'Content-Type: application/json' --data "@testing/testdata/smc/smc_manual_payload.json"  ${SUBMODEL_SERVER}/data/${SUBMODEL_ID})
  check_status_code

  generate_UUID
  SERIAL_PART_ASPECT_ID=${UUID}
  echo "Create BomAsBuilt sample data of SMC serialized part..."
  HTTP_RESPONSE=$(curl -X POST  -H 'Content-Type: application/json' --data "@testing/testdata/smc/smc_payload_partAsPlanned.json"  ${SUBMODEL_SERVER}/data/${SERIAL_PART_ASPECT_ID})
  check_status_code

  echo "[SecondaryMaterialContent] - Created submodel data"
}

# create edc assets, policies and contracts for the registry (DTR)
create_registry_asset
create_default_policy
create_default_contractdefinition


## loop through the above array
for item in "${smc_orders[@]}"
do
  ORDER_ID=$item
  echo
  echo "++++++++++++++++++ SMC Order ID: " ${ORDER_ID} "++++++++++++++++++++++"
  echo
  create_submodel_payload
  create_edc_asset
  create_policy
  create_contractdefinition
  create_aas3_shell ${ORDER_ID} ${PASSPORT_TYPE}
  echo
done
  
echo 'Secondary Material Content SMC test data upload complete...'
echo 'Done'
