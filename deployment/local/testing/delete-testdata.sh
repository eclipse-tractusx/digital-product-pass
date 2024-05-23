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

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

#ARGS=[]

# ./delete-testdata.sh -e -a -k

while getopts e:a:k:h flag
do
    case "${flag}" in
      e) edc=${OPTARG};;
      a) registry=${OPTARG};;
      k) api_key=${OPTARG};;
      h) echo "Usage: ./upload-testdata.sh <submodel_server> <edc-url> <aas-url> <api-key> <bpn>"
          echo "-e,      Provider edc controlplane url"
          echo "-a,      AAS registry url"
          echo "-k,      API Key"
         exit 1
    esac
done

EDC_URL=$edc
REGISTRY_URL=$registry
API_KEY=$api_key
MANAGEMENT_API="management/v2"

echo "Delete EDC contracts..."
JSON_RESPONSE=$(curl --location --silent --request POST "${EDC_URL}/${MANAGEMENT_API}/contractdefinitions/request" --header "Content-Type: application/json" --header "X-Api-Key: ${API_KEY}")
items=$(echo "$JSON_RESPONSE" |  jq '.[]["@id"]')
for id in ${items[@]}; do
    echo "Delete EDC contract id: $id"
    # remove quotes around the id
    id=$(echo "${id}" | sed 's/"//g')
    curl --location --silent --request DELETE "${EDC_URL}/${MANAGEMENT_API}/contractdefinitions/${id}" --header "Content-Type: application/json" --header "X-Api-Key: ${API_KEY}"
    echo
done

echo "Delete EDC policies..."
JSON_RESPONSE=$(curl --location --silent --request POST "${EDC_URL}/${MANAGEMENT_API}/policydefinitions/request" --header "Content-Type: application/json" --header "X-Api-Key: ${API_KEY}")
items=$(echo "$JSON_RESPONSE" |  jq '.[]["@id"]')
for id in ${items[@]}; do
    echo "Delete EDC policy id: $id"
    # remove quotes around the id
    id=$(echo "${id}" | sed 's/"//g')
    curl --location --silent --request DELETE "${EDC_URL}/${MANAGEMENT_API}/policydefinitions/${id}" --header "Content-Type: application/json" --header "X-Api-Key: ${API_KEY}"
    echo
done


echo "Delete EDC assets..."
JSON_RESPONSE=$(curl --location --silent --request POST "${EDC_URL}/${MANAGEMENT_API}/assets/request" --header "Content-Type: application/json" --header "X-Api-Key: ${API_KEY}")
items=$(echo "$JSON_RESPONSE" |  jq '.[]["@id"]')
for id in ${items[@]}; do
    echo "Delete EDC asset id: $id"
    # remove quotes around the id
    id=$(echo "${id}" | sed 's/"//g')
    curl --location --silent --request DELETE "${EDC_URL}/${MANAGEMENT_API}/assets/${id}" --header "Content-Type: application/json" --header "X-Api-Key: ${API_KEY}"
    echo
done

echo "Delete aas shells..."
JSON_RESPONSE=$(curl --location --silent --request GET "${REGISTRY_URL}/shell-descriptors" --header "Content-Type: application/json")
items=$(echo "$JSON_RESPONSE" | jq -r ".result[].id")
for id in ${items[@]}; do
    echo "Delete shell id: $id"
    # remove quotes around the id
    id=$(echo "${id}" | sed 's/"//g')
    base64_id=$(echo -n $id | base64)
    curl --location --silent --request DELETE "${REGISTRY_URL}/shell-descriptors/${base64_id}" --header "Content-Type: application/json"
    echo
done

echo 'Delete test data script complete...'
echo 'Done'
