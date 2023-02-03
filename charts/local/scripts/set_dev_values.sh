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

echo "Replace client credentials with actual values in consumer-ui values-dev file..."
echo
sed -i "s|<path:material-pass/data/dev/aasregistry#client.id>|${REGISTRY_CLIENT_ID}|g" ./../../consumer-ui/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/aasregistry#client.secret>|${REGISTRY_CLIENT_SECRET}|g" ./../../consumer-ui/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/oauth#api.key>|${EDC_API_AUTH_KEY}|g" ./../../consumer-ui/values-dev.yaml;
sed -i "s|tag: placeholder|tag: <LATEST_IMAGE_TAG_FROM_GIT_COMMIT_SHA>|g" ./../../consumer-ui/values-dev.yaml;


### CONSUMER CONNECTOR

echo "Replace secrets with actual values in edc-consumer values-dev file..."
echo
# Postgres DB
sed -i "s|<path:material-pass/data/dev/edc/database#user>|${POSTGRES_DB_USER}|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/database#password>|${POSTGRES_DB_PASSWORD}|g" ./../../edc-consumer/values-dev.yaml;

# Hashicorp vault
sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|${VAULT_HASHICORP_URL}|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|${VAULT_HASHICORP_SECRET_PATH}|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|${VAULT_HASHICORP_TOKEN}|g" ./../../edc-consumer/values-dev.yaml;
 
# EDC Oauth
sed -i "s|EDC_API_AUTH_KEY: <path:material-pass/data/dev/edc/oauth#api.key>|EDC_API_AUTH_KEY: ${EDC_API_AUTH_KEY}|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/oauth#client.id>|${EDC_OAUTH_CLIENT_ID}|g" ./../../edc-consumer/values-dev.yaml;

# EDC IDS Endpoints
sed -i "s|https://materialpass.dev.demo.catena-x.net/consumer/api/v1/ids|http://materialpass-edc-controlplane:8282/consumer/api/v1/ids|g" ./../../edc-consumer/values-dev.yaml;
sed -i "s|ids.webhook.address=https://materialpass.dev.demo.catena-x.net|ids.webhook.address=http://materialpass-edc-controlplane:8282|g" ./../../edc-consumer/values-dev.yaml;


### PROVIDER CONNECTOR

echo "Replace secrets with actual values in edc-provider values-dev file..."
echo
# Postgres DB
sed -i "s|<path:material-pass/data/dev/edc/database#user>|${POSTGRES_DB_USER}|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/database#password>|${POSTGRES_DB_PASSWORD}|g" ./../../edc-provider/values-dev.yaml;

# Hashicorp vault
sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|${VAULT_HASHICORP_URL}|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|${VAULT_HASHICORP_SECRET_PATH}|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|${VAULT_HASHICORP_TOKEN}|g" ./../../edc-provider/values-dev.yaml;
 
# EDC Oauth
sed -i "s|EDC_API_AUTH_KEY: <path:material-pass/data/dev/edc/oauth#api.key>|EDC_API_AUTH_KEY: ${EDC_API_AUTH_KEY}|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|<path:material-pass/data/dev/edc/oauth#client.id>|${EDC_OAUTH_CLIENT_ID}|g" ./../../edc-provider/values-dev.yaml;

# EDC IDS Endpoints
sed -i "s|https://materialpass.dev.demo.catena-x.net/provider/api/v1/ids|http://materialpass-edc-provider-controlplane:8282/provider/api/v1/ids|g" ./../../edc-provider/values-dev.yaml;
sed -i "s|ids.webhook.address=https://materialpass.dev.demo.catena-x.net|ids.webhook.address=http://materialpass-edc-provider-controlplane:8282|g" ./../../edc-provider/values-dev.yaml;

echo "Done..."
