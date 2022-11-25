#!/bin/bash

# Please do port forwarding before running this script
# Kubernetes port forwarding
# edc provider
# kubectl port-forward pod/materialpass-edc-provider-controlplane-68d6c8ffb-vrkbw 31571:818
# kubectl port-forward pod/materialpass-edc-provider-controlplane-68d6c8ffb-vrkbw 8282:8282

# edc consumer
# kubectl port-forward pod/materialpass-edc-controlplane-79d8f4f65-qv2qw 30295:8181
# kubectl port-forward pod/materialpass-edc-backend-6487c79b9d-s9lmt 8080:8081

echo "****Start script*****"

PROVIDER_DATAMGMT_URL=http://localhost:31571;
PROVIDER_IDS_URL==http://materialpass-edc-provider-controlplane:8282;
CONSUMER_DATAMGMT_URL=http://localhost:30295;
CONSUMER_BACKEND_URL=http://localhost:8080;

ASSET_ID=15;
POLICY_ID=125;
CONTRACT_DEF_ID=125;
BASE_URL="https://materialpass.int.demo.catena-x.net/provider_backend/data/urn:uuid:61125dc3-5e6f-4f4b-838d-447432b97918";

# Register assets
echo "******Register assets******"
curl -X POST "$PROVIDER_DATAMGMT_URL/provider/data/assets"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json'     --data "{
             \"asset\": {
                \"properties\": {
                        \"asset:prop:id\": \"${ASSET_ID}\",
                        \"asset:prop:description\": \"Product EDC Demo Asset\"
                    }
                },
                \"dataAddress\": {
                    \"properties\": {
                        \"type\": \"HttpData\",
                        \"baseUrl\": \"${BASE_URL}\"
                    }
                }}"


# Register policy definitions
echo "******Register policy definitions*****"
curl -X POST "$PROVIDER_DATAMGMT_URL/provider/data/policydefinitions"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json'     --data "{
               \"id\": \"${CONTRACT_DEF_ID}\",
                \"policy\": {
                    \"prohibitions\": [],
                    \"obligations\": [],
                    \"permissions\": [
                        {
                            \"edctype\": \"dataspaceconnector:permission\",
                            \"action\": { \"type\": \"USE\" },
                            \"constraints\": []
                        }
                    ]
                }
            }";
			
#Register contract definitions
echo "******Register contract definitions******"
curl -X POST "$PROVIDER_DATAMGMT_URL/provider/data/contractdefinitions"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json'     --data "{
			\"id\": \"${POLICY_ID}\",
			\"criteria\": [
				{
					\"operandLeft\": \"asset:prop:id\",
					\"operator\": \"=\",
					\"operandRight\": \"${ASSET_ID}\"
				}
			],
			\"accessPolicyId\": \"${POLICY_ID}\",
			\"contractPolicyId\": \"${POLICY_ID}\"
		}";


sleep 1;
echo "******Get assets******"
curl -X GET "$PROVIDER_DATAMGMT_URL/provider/data/assets"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json' -s | jq;

sleep 1;
echo "******Get policy definitions******"
curl -X GET "$PROVIDER_DATAMGMT_URL/provider/data/policydefinitions"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json' -s | jq;

sleep 1;
echo "******Get contract definitions******"
curl -X GET "$PROVIDER_DATAMGMT_URL/provider/data/contractdefinitions"     --header 'X-Api-Key: password'     --header 'Content-Type: application/json' -s | jq;

echo "****Script executed successfully...*****"
echo "****Done...*****"
