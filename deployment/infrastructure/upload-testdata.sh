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

while getopts s:e:a:k:b:h flag
do
    case "${flag}" in
      s) submodel_server=${OPTARG};;
      e) edc=${OPTARG};;
      a) registry=${OPTARG};;
      k) api_key=${OPTARG};;
      b) bpn=${OPTARG};;
      h) echo "Usage: ./upload-testdata.sh <submodel_server> <edc-url> <aas-url> <api-key> <bpn>"
          echo "-s,      Submodel server url"
          echo "-e,      Provider edc controlplane url"
          echo "-a,      AAS registry url"
          echo "-k,      API Key"
          echo "-b,      BPN number"
         exit 1
    esac
done

# script parameters
export SUBMODEL_SERVER=${submodel_server}
export PROVIDER_EDC=${edc}
export REGISTRY_URL=${registry}
export API_KEY=${api_key}
export BPN=${bpn}

export REGISTRY_ASSET_ID='registry-asset'
export MANUFACTURER_PART_ID='XYZ78901'

echo "****************Start upload battery test data*************"
./upload-batterypass-data.sh ${submodel_server} ${edc} ${registry} ${api_key} ${bpn}
echo "*****************End upload battery test data**************"
echo

echo "****************Start upload dpp test data****************"
./upload-dpp-data.sh ${submodel_server} ${edc} ${registry} ${api_key} ${bpn}
echo "***************End upload dpp test data*******************"
echo

echo "****************Start upload Secondary Material Content SMC test data****************"
./upload-smc-data.sh ${submodel_server} ${edc} ${registry} ${api_key} ${bpn}
echo "***************End upload  Secondary Material Content SMC test data*******************"
echo

echo "****************Start upload transmission pass test data****************"
./upload-transmissionpass-data.sh ${submodel_server} ${edc} ${registry} ${api_key} ${bpn}
echo "***************End upload transmission pass test data*******************"
echo

echo 'Test data upload complete...'
echo 'Done'
