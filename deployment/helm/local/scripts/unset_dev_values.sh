#!/bin/bash

set -o errexit
set -o errtrace
set -o pipefail
set -o nounset

export GH_TOKEN="<GITHUB_TOKEN>" 
export VAULT_ADDR="<VAULT_URL>"

echo "Login into Hashicorp vault..."
vault login -method=github token=$GH_TOKEN
echo
echo "Retrieve secrets from Hashicorp vault..."
echo

REGISTRY_CLIENT_ID=$(vault kv get -field=client.id material-pass/dev/aasregistry)
REGISTRY_CLIENT_SECRET=$(vault kv get -field=client.secret material-pass/dev/aasregistry)
EDC_API_AUTH_KEY=$(vault kv get -field=api.key material-pass/dev/edc/oauth)
POSTGRES_DB_USER=$(vault kv get -field=user material-pass/dev/edc/database)
POSTGRES_DB_PASSWORD=$(vault kv get -field=password material-pass/dev/edc/database)

VAULT_HASHICORP_URL=$(vault kv get -field=vault.hashicorp.url material-pass/dev/edc/vault)
VAULT_HASHICORP_SECRET_PATH=$(vault kv get -field=vault.hashicorp.api.secret.path material-pass/dev/edc/vault)
VAULT_HASHICORP_TOKEN=$(vault kv get -field=vault.hashicorp.token material-pass/dev/edc/vault)

EDC_OAUTH_CLIENT_ID=$(vault kv get -field=client.id material-pass/dev/edc/oauth)

### CONSUMER FRONTEND APP

echo "Replace actual values with placholders in consumer-ui values-dev file..."
echo
sed -i "s|${REGISTRY_CLIENT_ID}|<path:material-pass/data/dev/aasregistry#client.id>|g" ./../../consumer-ui/values-dev.yaml;
sed -i "s|${REGISTRY_CLIENT_SECRET}|<path:material-pass/data/dev/aasregistry#client.secret>|g" ./../../consumer-ui/values-dev.yaml;
sed -i "s|${EDC_API_AUTH_KEY}|<path:material-pass/data/dev/edc/oauth#api.key>|g" ./../../consumer-ui/values-dev.yaml;
sed -i "s|tag: <LATEST_IMAGE_TAG_FROM_GIT_COMMIT_SHA>|tag: placeholder|g" ./../../consumer-ui/values-dev.yaml;

### CONSUMER CONNECTOR

echo "Replace actual values with placholders in edc-consumer values-dev file..."
echo
# Postgres DB
sed -i "s|${POSTGRES_DB_USER}|<path:material-pass/data/dev/edc/database#user>|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|password: ${POSTGRES_DB_PASSWORD}|password: <path:material-pass/data/dev/edc/database#password>|g" ./../../edc-consumer/values-dev.yaml;

# Hashicorp vault
sed -i "s|${VAULT_HASHICORP_URL}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|${VAULT_HASHICORP_SECRET_PATH}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|${VAULT_HASHICORP_TOKEN}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|g" ./../../edc-consumer/values-dev.yaml;
 
# EDC Oauth
sed -i "s|EDC_API_AUTH_KEY: ${EDC_API_AUTH_KEY}|EDC_API_AUTH_KEY: <path:material-pass/data/dev/edc/oauth#api.key>|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|${EDC_OAUTH_CLIENT_ID}|<path:material-pass/data/dev/edc/oauth#client.id>|g" ./../../edc-consumer/values-dev.yaml;

# EDC IDS Endpoints
sed -i "s|http://materialpass-edc-controlplane:8282/consumer/api/v1/ids|https://materialpass.dev.demo.catena-x.net/consumer/api/v1/ids|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|ids.webhook.address=http://materialpass-edc-controlplane:8282|ids.webhook.address=https://materialpass.dev.demo.catena-x.net|g" ./../../edc-consumer/values-dev.yaml;


### PROVIDER CONNECTOR

echo "Replace actual values with placholders in edc-provider values-dev file..."
echo
# Postgres DB
sed -i "s|${POSTGRES_DB_USER}|<path:material-pass/data/dev/edc/database#user>|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|password: ${POSTGRES_DB_PASSWORD}|password: <path:material-pass/data/dev/edc/database#password>|g" ./../../edc-provider/values-dev.yaml;

# Hashicorp vault
sed -i "s|${VAULT_HASHICORP_URL}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|${VAULT_HASHICORP_SECRET_PATH}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|${VAULT_HASHICORP_TOKEN}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|g" ./../../edc-provider/values-dev.yaml;
 
# EDC Oauth
sed -i "s|EDC_API_AUTH_KEY: ${EDC_API_AUTH_KEY}|EDC_API_AUTH_KEY: <path:material-pass/data/dev/edc/oauth#api.key>|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|${EDC_OAUTH_CLIENT_ID}|<path:material-pass/data/dev/edc/oauth#client.id>|g" ./../../edc-provider/values-dev.yaml;

# EDC IDS Endpoints
sed -i "s|http://materialpass-edc-provider-controlplane:8282/provider/api/v1/ids|https://materialpass.dev.demo.catena-x.net/provider/api/v1/ids|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|ids.webhook.address=http://materialpass-edc-provider-controlplane:8282|ids.webhook.address=https://materialpass.dev.demo.catena-x.net|g" ./../../edc-provider/values-dev.yaml;


echo "Done..."




