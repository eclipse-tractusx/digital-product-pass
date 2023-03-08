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

DIGITAL_TWIN_1='urn:uuid:1e4b62e8-dc0d-4327-9ece-fd333cea06d8'
DIGITAL_TWIN_SUBMODEL_ID_1='urn:uuid:db3fed5d-86cb-4d99-9adf-5e1c6267d293'

DIGITAL_TWIN_2='urn:uuid:bda9c9b0-0817-4dad-a279-6b141b250bae'
DIGITAL_TWIN_SUBMODEL_ID_2='urn:uuid:79a66c94-14b6-438b-b0f0-7d651243a708'

DIGITAL_TWIN_3='urn:uuid:51b1cd81-d03b-441d-a7c2-41ef9d789199'
DIGITAL_TWIN_SUBMODEL_ID_3='urn:uuid:10d094e0-aecc-4e84-b937-a1d606112cdd'

SERVER_URL='https://materialpass.dev.demo.catena-x.net'
REGISTRY_URL='https://semantics.dev.demo.catena-x.net/registry/registry/shell-descriptors'

# put access token without 'Bearer ' prefix
BEARER_TOKEN='eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ5U29MS3FlRzI3LS1OQlBETGJNUDYxel9VbEVMSXl3VHA3aUpvMFRLX0dZIn0.eyJleHAiOjE2Nzc3NzM1ODIsImlhdCI6MTY3Nzc3MzI4MiwianRpIjoiYTdjZTRiMmYtN2E1Zi00ZmFiLWJiYzgtMThhZTRhMzdkYzc2IiwiaXNzIjoiaHR0cHM6Ly9jZW50cmFsaWRwLmRldi5kZW1vLmNhdGVuYS14Lm5ldC9hdXRoL3JlYWxtcy9DWC1DZW50cmFsIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJ0ZWNobmljYWxfcm9sZXNfbWFuYWdlbWVudCIsIkNsNi1DWC1EQVBTIiwiQ2w0LUNYLURpZ2l0YWxUd2luIiwiYWNjb3VudCIsIkNsMy1DWC1TZW1hbnRpYyIsIkNsMi1DWC1Qb3J0YWwiXSwic3ViIjoiNDMxYmQzOWQtZjA0ZC00Yjk5LWExY2UtZGM5NzJiN2Y2MWIyIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoic2EtY2w2LWN4LTI5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwczovL21hdGVyaWFscGFzcy5kZXYuZGVtby5jYXRlbmEteC5uZXQiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy1jYXRlbmEteCByZWFsbSIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicmVhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJtYW5hZ2UtdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJxdWVyeS1jbGllbnRzIl19LCJ0ZWNobmljYWxfcm9sZXNfbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJBcHAgVGVjaCBVc2VyIiwiQ29ubmVjdG9yIFVzZXIiXX0sIkNsNi1DWC1EQVBTIjp7InJvbGVzIjpbImNyZWF0ZV9kYXBzX2NsaWVudCJdfSwiQ2w0LUNYLURpZ2l0YWxUd2luIjp7InJvbGVzIjpbImFkZF9kaWdpdGFsX3R3aW4iLCJ2aWV3X2RpZ2l0YWxfdHdpbiIsImRlbGV0ZV9kaWdpdGFsX3R3aW4iLCJ1cGRhdGVfZGlnaXRhbF90d2luIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX0sIkNsMy1DWC1TZW1hbnRpYyI6eyJyb2xlcyI6WyJ2aWV3X3NlbWFudGljX21vZGVsIl19LCJDbDItQ1gtUG9ydGFsIjp7InJvbGVzIjpbInZpZXdfY29ubmVjdG9ycyJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJicG4iOiJCUE5MMDAwMDAwMDAwMDAwIiwiY2xpZW50SG9zdCI6IjEwLjI0MC4wLjgiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoic2EtY2w2LWN4LTI5IiwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNhLWNsNi1jeC0yOSIsImNsaWVudEFkZHJlc3MiOiIxMC4yNDAuMC44In0.tuKp_NjV5wGhTDJYchjjKpi2l1Li1p36g6gGS0MEfWqXUiVsiam8GQ5jrLregcOrRWuKfdcIw8n3O8Btp53lQTbQ6ahoU54sX8lXQ-opomgjOCnX0nP4p0ZSM_w7v_xdgjUTtPhbVomucwKC-e8j1qgksFl2yIuAeSh9K9zpmgR37ql-diJrY-m4IEFoekMgaMuHVPAbTFm-UutfC2XZaISidIv_QgTf_OthFhM1NYDnWlc2x4nf_BtXM4u4MWqCGPDp8ycuYJRkaYSyGPbVMIGGk2dkquvIKsP9hShMo0pIFRPTDVcg3qqHcfOsK0pfl_3tFAHKgABfEDUnfuUnIA'

API_KEY='password'


echo '**************************Asset 1 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 1..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/payloads/X123456789012X12345678901234566_DEV.json"  ${SERVER_URL}/provider_backend/data/${DIGITAL_TWIN_SUBMODEL_ID_1}
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/assets/X123456789012X12345678901234566_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 1..."
curl -X POST -H 'Content-Type: application/json'  --data "@resources/contractpolicies/X123456789012X12345678901234566_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 1..."
curl -X POST -H 'Content-Type: application/json'  --data "@resources/contractdefinitions/X123456789012X12345678901234566_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 1 and register it into CX registry..."
curl -X POST --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/X123456789012X12345678901234566_DEV.json"  $REGISTRY_URL
echo
echo



echo '**************************Asset 2 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 2..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/payloads/NCR186850B_DEV.json"  ${SERVER_URL}/provider_backend/data/${DIGITAL_TWIN_SUBMODEL_ID_2}
echo

# Create a asset
echo "Create asset 2..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/assets/NCR186850B_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 2..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/contractpolicies/NCR186850B_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/policydefinitions


# Create a contract definition
echo "Create contract definition for asset 2..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/contractdefinitions/NCR186850B_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 2 and register it into CX registry..."
curl -X POST --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/NCR186850B_DEV.json" $REGISTRY_URL
echo
echo



echo '**************************Asset 3 **********************'
# Create Submodel data
echo "Create sample data for asset 3..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/payloads/IMR18650V1_DEV.json"  ${SERVER_URL}/provider_backend/data/${DIGITAL_TWIN_SUBMODEL_ID_3}
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/assets/IMR18650V1_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 3..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/contractpolicies/IMR18650V1_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 3..."
curl -X POST -H 'Content-Type: application/json' --data "@resources/contractdefinitions/IMR18650V1_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 3 and register it into CX registry..."
curl -X POST --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/IMR18650V1_DEV.json" $REGISTRY_URL
echo

echo 'Provider setup completed...'
echo 'Done'
