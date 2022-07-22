#!/bin/bash
# Provider
[ -z "$providerDataMgmtUrl" ] && providerDataMgmtUrl=http://localhost:8182
# APIs
[ -z "$dataMgmtPath" ] && dataMgmtPath=api/v1/data

# API Key
[ -z "$apiKey" ] && apiKey=X-Api-Key
[ -z "$apiKeyValue" ] && apiKeyValue=password

# Asset
[ -z "$defaultAssetId" ] && defaultAssetId=1
[ -z "$defaultAssetDescription" ] && defaultAssetDescription='Demo Asset'
[ -z "$defaultAssetDataEndpoint" ] && defaultAssetDataEndpoint=https://jsonplaceholder.typicode.com/todos/1
echo "Enter Asset Id (default '$defaultAssetId')"
read -r assetId
[ -z "$assetId" ] && assetId=$defaultAssetId

echo "Enter Asset Description (default '$defaultAssetDescription')"
read assetDescription
[ -z "$assetDescription" ] && assetDescription=$defaultAssetDescription

echo "Enter Asset Data Endpoint (default '$defaultAssetDataEndpoint')"
read assetDataEndpoint
[ -z "$assetDataEndpoint" ] && assetDataEndpoint=$defaultAssetDataEndpoint

__payload="
        {
        \"asset\": {
            \"properties\": {
                \"asset:prop:id\": \"$assetId\",
                \"asset:prop:description\": \"$assetDescription\"
            }
        },
        \"dataAddress\": {
            \"properties\": {
                \"type\": \"HttpProxy\",
                \"endpoint\": \"$assetDataEndpoint\"
            }
        }
    }"
echo ""
echo -e "\033[1mCreating Asset\033[0m"
echo "---------"
#echo $__payload | jq
echo $__payload
echo "---------"
curl -X POST "$providerDataMgmtUrl/$dataMgmtPath/assets" --header "$apiKey: $apiKeyValue" --header "Content-Type: application/json" --data "$__payload" -w 'Response Code: %{http_code}\n'
echo ""
echo -e "\033[1mCreated Asset\033[0m"
echo "---------"
# curl -X GET "$providerDataMgmtUrl/$dataMgmtPath/assets/$assetId" --header "$apiKey: $apiKeyValue" --header "Content-Type: application/json" | jq
curl -X GET "$providerDataMgmtUrl/$dataMgmtPath/assets/$assetId" --header "$apiKey: $apiKeyValue" --header "Content-Type: application/json"
echo "---------"