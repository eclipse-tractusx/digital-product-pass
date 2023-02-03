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



echo "****Start script*****"

PROVIDER_DATAMGMT_URL=http://localhost:31571;
PROVIDER_IDS_URL==http://materialpass-edc-provider-controlplane:8282;
CONSUMER_DATAMGMT_URL=http://localhost:30295;
CONSUMER_BACKEND_URL=http://localhost:8080;

ASSET_ID=15;
POLICY_ID=125;
CONTRACT_DEF_ID=125;
BASE_URL="https://materialpass.int.demo.catena-x.net/provider_backend/data/urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97918";

sleep 1;
# Getting contract offer catalog
echo "******Get contract offer catalog******"
 curl -G -X GET "$CONSUMER_DATAMGMT_URL/consumer/data/catalog"     --data-urlencode "providerUrl=http://materialpass-edc-provider-controlplane:8282/provider/api/v1/ids/data"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json'     -s | jq;

echo "*****Start Contract Negotiation process*****"
NEGOTIATION_ID=$( \
    curl -X POST "$CONSUMER_DATAMGMT_URL/consumer/data/contractnegotiations" \
        --header "X-Api-Key: password" \
        --header "Content-Type: application/json" \
        --data "{
                    \"connectorId\": \"foo\",
                    \"connectorAddress\": \"http://materialpass-edc-provider-controlplane:8282/provider/api/v1/ids/data\",
                    \"offer\": {
                        \"offerId\": \"1:foo\",
                        \"assetId\": \"${ASSET_ID}\",
                        \"policy\": {
                            \"uid\": \"$POLICY_ID\",
                            \"prohibitions\": [],
                            \"obligations\": [],
                            \"permissions\": [
                                {
                                    \"edctype\": \"dataspaceconnector:permission\",
                                    \"action\": { \"type\": \"USE\" },
                                    \"target\": \"$ASSET_ID\",
                                    \"constraints\": []
                                }
                            ]
                        }
                    }
                }" \
    -s | jq -r '.id');
	
sleep 5;
	
echo "Negotiation Id: ${NEGOTIATION_ID}"	

sleep 1;

echo "*****Get contract negotiation*****"
curl -X GET "$CONSUMER_DATAMGMT_URL/consumer/data/contractnegotiations/${NEGOTIATION_ID}" \
    --header 'X-Api-Key: password' \
    --header 'Content-Type: application/json' \
    -s | jq;

CONTRACT_AGREEMENT_ID=$( \
    curl -X GET "$CONSUMER_DATAMGMT_URL/consumer/data/contractnegotiations/$NEGOTIATION_ID" \
    --header 'X-Api-Key: password' \
    --header 'Content-Type: application/json' \
    -s | jq -r '.contractAgreementId');

echo "Contract agreement Id: ${CONTRACT_AGREEMENT_ID}"

sleep 5;

echo "*****Generate transfer process Id *****"
TRANSFER_PROCESS_ID=$(tr -dc '[:alnum:]' < /dev/urandom | head -c20);

sleep 1;
echo "Transfer Process Id: ${TRANSFER_PROCESS_ID}";


echo "*****Start data transfer process*****"
TRANSFER_ID=$( \
    curl -X POST "$CONSUMER_DATAMGMT_URL/consumer/data/transferprocess" \
    --header "X-Api-Key: password" \
    --header "Content-Type: application/json" \
    --data "{
                \"id\": \"${TRANSFER_PROCESS_ID}\", 
                \"connectorId\": \"foo\", 
                \"connectorAddress\": \"http://materialpass-edc-provider-controlplane:8282/provider/api/v1/ids/data\", 
                \"contractId\": \"$CONTRACT_AGREEMENT_ID\", 
                \"assetId\": \"${ASSET_ID}\", 
                \"managedResources\": \"false\", 
                \"dataDestination\": { \"type\": \"HttpProxy\" }
            }" \
     -s | jq -r '.id');
	 
sleep 10;

echo "Transfer Id: ${TRANSFER_ID}";

sleep 5;

echo "*****Verify the data transfer process from consumer end*****"
curl -X GET "$CONSUMER_DATAMGMT_URL/consumer/data/transferprocess/$TRANSFER_ID" \
    --header 'X-Api-Key: password' \
    --header 'Content-Type: application/json' \
    -s | jq

sleep 5;
echo "*****Retrieve transferred data from consumer backend*****"
curl -X GET "$CONSUMER_BACKEND_URL/${TRANSFER_PROCESS_ID}" \
--header 'Accept: application/octet-stream' \
-s | jq

echo "****Script executed successfully...*****"
echo "****Done...*****"