#!/bin/bash
# Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#     http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

DIGITAL_TWIN_1='urn:uuid:32aa72de-297a-4405-9148-13e12744028a'
DIGITAL_TWIN_SUBMODEL_ID_1='urn:uuid:699f1245-f57e-4d6b-acdb-ab763665554a'

DIGITAL_TWIN_2='urn:uuid:1f4a64f0-aba9-498a-917c-4936c24c50cd'
DIGITAL_TWIN_SUBMODEL_ID_2='urn:uuid:49a06ad2-64b7-46c8-9f3b-a718c462ca23'

DIGITAL_TWIN_3='urn:uuid:365e6fbe-bb34-11ec-8422-0242ac120002'
DIGITAL_TWIN_SUBMODEL_ID_3='urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97918'
BPN='BPNL000000000000'

SERVER_URL='https://materialpass.int.demo.catena-x.net'
REGISTRY_URL='https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors'


# put access token without 'Bearer ' prefix
BEARER_TOKEN='eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIVUgzYjZrMzZvbFNQVTRDRTRaMVUxUjhVeHg4eFQwS3p4QXdLb3NkVk1VIn0.eyJleHAiOjE2Nzg4OTY3MDgsImlhdCI6MTY3ODg5NjQwOCwianRpIjoiNmU5ZDk4MjUtNDRlOC00YmRhLTk0MDQtZmU3NzMzYWY4NmMwIiwiaXNzIjoiaHR0cHM6Ly9jZW50cmFsaWRwLmludC5kZW1vLmNhdGVuYS14Lm5ldC9hdXRoL3JlYWxtcy9DWC1DZW50cmFsIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJ0ZWNobmljYWxfcm9sZXNfbWFuYWdlbWVudCIsIkNsNi1DWC1EQVBTIiwiQ2w0LUNYLURpZ2l0YWxUd2luIiwiYWNjb3VudCIsIkNsMy1DWC1TZW1hbnRpYyIsIkNsMi1DWC1Qb3J0YWwiXSwic3ViIjoiNDMxYmQzOWQtZjA0ZC00Yjk5LWExY2UtZGM5NzJiN2Y2MWIyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2EtY2w2LWN4LTI5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwczovL21hdGVyaWFscGFzcy5pbnQuZGVtby5jYXRlbmEteC5uZXQiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1jYXRlbmEteCByZWFsbSIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJtYW5hZ2UtdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJxdWVyeS1jbGllbnRzIl19LCJ0ZWNobmljYWxfcm9sZXNfbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJBcHAgVGVjaCBVc2VyIiwiQ29ubmVjdG9yIFVzZXIiXX0sIkNsNi1DWC1EQVBTIjp7InJvbGVzIjpbImNyZWF0ZV9kYXBzX2NsaWVudCJdfSwiQ2w0LUNYLURpZ2l0YWxUd2luIjp7InJvbGVzIjpbImFkZF9kaWdpdGFsX3R3aW4iLCJ2aWV3X2RpZ2l0YWxfdHdpbiIsImRlbGV0ZV9kaWdpdGFsX3R3aW4iLCJ1cGRhdGVfZGlnaXRhbF90d2luIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sIkNsMy1DWC1TZW1hbnRpYyI6eyJyb2xlcyI6WyJ2aWV3X3NlbWFudGljX21vZGVsIl19LCJDbDItQ1gtUG9ydGFsIjp7InJvbGVzIjpbInZpZXdfY29ubmVjdG9ycyJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJicG4iOiJCUE5MMDAwMDAwMDAwMDAwIiwiY2xpZW50SG9zdCI6IjEwLjI0MC4wLjQiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoic2EtY2w2LWN4LTI5IiwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNhLWNsNi1jeC0yOSIsImNsaWVudEFkZHJlc3MiOiIxMC4yNDAuMC40In0.K2x6xGilulKC4p9xNBsodJzgh3dOJ2qDmp2tHb0rQbpN539zNLGud5umKRDR6XVFaSnemjYmrI7aXgYo79OnE976gwyEoNAVVHmbvXjykI3HjpL4IiaT5z3l3QbefjpIe30G-3SLea5xrwlwH3hiImRgO6JnjGhLZMqMobz1SI9JoAoP0x0BdxfvL8BOtpdOxB27f9M9QZrh2mlroK0_-BEQ4UJErF2QThNZKtj6-pGqNoV4K8jgzaOgC0N9ZVlsBeXpD8CQradV-A7LG2QZKYy4cKMxKpXatHn6QWiFLAgHOReds11vfYweV4ws_rnm6E8DeABA-4y5gbqxLxYuuQ'

API_KEY=''
ASSET_ID_1=${DIGITAL_TWIN_1}-${DIGITAL_TWIN_SUBMODEL_ID_1}
ASSET_ID_2=${DIGITAL_TWIN_2}-${DIGITAL_TWIN_SUBMODEL_ID_2}
ASSET_ID_3=${DIGITAL_TWIN_3}-${DIGITAL_TWIN_SUBMODEL_ID_3}


echo '**************************Asset 1 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/X123456789012X12345678901234566.json"  $SERVER_URL/provider_backend/data/${ASSET_ID_1}
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/assets
echo

# Create a general policy
echo "Create policy for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 1 and register it into CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/X123456789012X12345678901234566.json"  $REGISTRY_URL
echo
echo



echo '**************************Asset 2 **********************'

echo 
# Create Submodel data
echo "Create sample data for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/NCR186850B.json"  $SERVER_URL/provider_backend/data/${ASSET_ID_2}
echo

# Create a asset
echo "Create asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/assets
echo

# Create a general policy
echo "Create policy for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/contractdefinitions
echo


# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 2 and register it into CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/NCR186850B.json" $REGISTRY_URL
echo
echo



echo '**************************Asset 3 **********************'
# Create Submodel data
echo "Create sample data for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/IMR18650V1.json"   $SERVER_URL/provider_backend/data/${ASSET_ID_3}
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/assets
echo

# Create a general policy
echo "Create policy for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/${BPN}/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 3 and register it into CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/IMR18650V1.json" $REGISTRY_URL
echo

echo 'Provider setup completed...'
echo 'Done'
