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

# ./upload-testdata.sh -s -edc -a -api -bpn -d
# args: SUBMODEL_SERVER_URL, PROVIDER_CONTROLPLANE, REGISTRY_URL, API_KEY, ALLOWED_BPNS, RESOURCE_DIR
# ./upload-testdata.sh -s https://materialpass.dev.demo.catena-x.net/provider_backend -e https://materialpass.dev.demo.catena-x.net/BPNL000000000000 -a https://materialpass.dev.demo.catena-x.net/semantics/registry/api/v3.0 -k password -b BPNL00000003CRHL -f @resources

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

#ARGS=[]

while getopts s:e:a:k:b:f flag
do
    case "${flag}" in
      s) submodel_server=${OPTARG};;
      e) edc=${OPTARG};;
      a) registry=${OPTARG};;
      k) api_key=${OPTARG};;
      b) bpn=${OPTARG};;
      #f) resource_dir=${OPTARG};;
    esac
done
echo "submodel_server: $submodel_server";
echo "edc: $edc";
echo "registry: $registry";
echo "api_key: $api_key";
echo "bpn: $bpn";


./upload-batterypass-data.sh ${submodel_server} ${edc} ${registry} ${api_key} ${bpn}
./upload-dpp-data.sh ${submodel_server} ${edc} ${registry} ${api_key} ${bpn}

echo 'Test data upload complete...'
echo 'Done'
