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

while getopts s:e:a:k:f:h flag
do
    case "${flag}" in
      s) submodel_server=${OPTARG};;
      e) edc=${OPTARG};;
      a) registry=${OPTARG};;
      k) api_key=${OPTARG};;
      f) datapath=${OPTARG};;
      h) echo "Usage: ./upload-testdata.sh <submodel_server> <edc-url> <aas-url> <api-key> <bpn>"
          echo "-s,      Submodel server url"
          echo "-e,      Provider edc controlplane url"
          echo "-a,      AAS registry url"
          echo "-k,      API Key"
          echo "-f,      input JSON file containing AAS shells configuration and submodel data" 
         exit 1
    esac
done

# script parameters
export SUBMODEL_SERVER=${submodel_server}
export PROVIDER_EDC=${edc}
export REGISTRY_URL=${registry}
export API_KEY=${api_key}

export REGISTRY_ASSET_ID='registry-asset'
export SUBMODEL_ID=''

source ./functions.sh

DATA_POLICY=''
REGISTRY_POLICY=''

REGISTRY_POLICY=$(jq '.policies[0]' "${datapath}")
DATA_POLICY=$(jq '.policies[1]' "${datapath}")

# create edc assets, policies and contracts for the registry (DTR)
echo "Creating default edc assets for the registry asset"
create_registry_asset
create_registry_policy "${REGISTRY_POLICY}"
create_registry_contractdefinition
create_default_policy
create_default_contractdefinition
echo

# create assets for passes
echo "Creating edc assets for the passport"
create_edc_asset
create_edc_policy "${DATA_POLICY}"
create_contractdefinition
echo

jq -c '.shells[]' "${datapath}" | while read indexx; do create_aas3_shell "${indexx}"; echo "------------------------------------------------------------------------------------" ;
 done

echo 'Test data upload complete...'
echo 'Done'
