#!/bin/bash

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

# put access token without 'Bearer ' prefix
BEARER_TOKEN=''

API_KEY=''


echo '**************************Asset 1 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/X123456789012X12345678901234566_DEV.json"  ${SERVER_URL}/provider_backend/data/${DIGITAL_TWIN_SUBMODEL_ID_1}
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/X123456789012X12345678901234566_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s  --data "@resources/contractpolicies/X123456789012X12345678901234566_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 1..."
curl -X POST -H 'Content-Type: application/json' -s  --data "@resources/contractdefinitions/X123456789012X12345678901234566_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 1 and register it into CX registry..."
curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/X123456789012X12345678901234566_DEV.json"  https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors
echo
echo



echo '**************************Asset 2 **********************'
echo
# Create Submodel data
echo "Create sample data for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/NCR186850B_DEV.json"  ${SERVER_URL}/provider_backend/data/${DIGITAL_TWIN_SUBMODEL_ID_2}
echo

# Create a asset
echo "Create asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/NCR186850B_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/NCR186850B_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/policydefinitions


# Create a contract definition
echo "Create contract definition for asset 2..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/NCR186850B_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 2 and register it into CX registry..."
curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/NCR186850B_DEV.json" https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors
echo
echo



echo '**************************Asset 3 **********************'
# Create Submodel data
echo "Create sample data for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/payloads/IMR18650V1_DEV.json"  ${SERVER_URL}/provider_backend/data/${DIGITAL_TWIN_SUBMODEL_ID_3}
echo

# Create a asset
echo "Create asset 1..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/assets/IMR18650V1_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/assets
echo

# Create a general policy
echo "Create policy for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractpolicies/IMR18650V1_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/policydefinitions
echo

# Create a contract definition
echo "Create contract definition for asset 3..."
curl -X POST -H 'Content-Type: application/json' -s --data "@resources/contractdefinitions/IMR18650V1_DEV.json" --header 'X-Api-Key: '${API_KEY} ${SERVER_URL}/provider/data/contractdefinitions
echo

# Create a digital twin and register inside CX registry
# To authenticate against CX registry, one needs a valid bearer token which can be issued through postman given the clientId and clientSecret
echo "Create a DT for asset 3 and register it into CX registry..."
curl -X POST -s --header 'Content-Type: application/json' --header "Authorization: Bearer ${BEARER_TOKEN//[$'\t\r\n ']}"  --data "@resources/digitaltwins/IMR18650V1_DEV.json" https://semantics.int.demo.catena-x.net/registry/registry/shell-descriptors
echo

echo 'Provider setup completed...'
echo 'Done'
