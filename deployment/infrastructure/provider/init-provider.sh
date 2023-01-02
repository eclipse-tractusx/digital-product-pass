#!/bin/bash

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

SERVER_URL='https://materialpass.int.demo.catena-x.net'


# put access token without 'Bearer ' prefix
BEARER_TOKEN=''

API_KEY=''


echo '**************************Asset 1 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/X123456789012X12345678901234566.json"  $SERVER_URL/provider_backend/data/$DIGITAL_TWIN_SUBMODEL_ID_1
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/X123456789012X12345678901234566.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 1 and register it into CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/X123456789012X12345678901234566.json"  https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors
echo
echo



echo '**************************Asset 2 **********************'

echo 
# Create Submodel data
echo "Create sample data for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/NCR186850B.json"  $SERVER_URL/provider_backend/data/$DIGITAL_TWIN_SUBMODEL_ID_2
echo

# Create a asset
echo "Create asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/NCR186850B.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/contractdefinitions
echo


# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 2 and register it into CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/NCR186850B.json" https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors
echo
echo



echo '**************************Asset 3 **********************'
# Create Submodel data
echo "Create sample data for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/IMR18650V1.json"   $SERVER_URL/provider_backend/data/$DIGITAL_TWIN_SUBMODEL_ID_3
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/IMR18650V1.json" --header 'X-Api-Key: '${API_KEY} $SERVER_URL/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 3 and register it into CX registry..."

curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/IMR18650V1.json" https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors
echo

echo 'Provider setup completed...'
echo 'Done'
