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
export PASSPORT_TYPE="batterypass"

source ./functions.sh

# script global variables
UUID=''
BATTERY_ID=''


# declare an array variable
declare -a batteries=("IMR18650V1" "NCR186850B" "X123456789012X12345678901234566" "Y792927456954B81677903848654570")

create_submodel_payload () {

  generate_UUID
  SUBMODEL_ID=${UUID}

  HTTP_RESPONSE==$(curl -X POST -H 'Content-Type: application/json' -s --data "@testing/testdata/dpp/${BATTERY_ID}.json" $SUBMODEL_SERVER/data/${SUBMODEL_ID})
  echo "[BatteryPass] - Created submodel data with uuid: " ${SUBMODEL_ID}
}

# create edc assets, policies and contracts for the registry (DTR)
create_registry_asset
create_default_policy
create_default_contractdefinition


## now loop through the above array
for battery in "${batteries[@]}"
do
  BATTERY_ID=$battery
  echo
  echo "++++++++++++++++++ Battery ID: " ${BATTERY_ID} "++++++++++++++++++++++"
  echo
  create_submodel_payload 
  create_edc_asset
  create_policy
  create_contractdefinition
  create_aas3_shell ${BATTERY_ID} ${PASSPORT_TYPE}
  echo
done

# You can access them using echo "${arr[0]}", "${arr[1]}" also
  
echo 'Battery test data upload complete...'
echo 'Done'
