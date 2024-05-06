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

SET_VALUES=$1
GITHUB_TOKEN=$2
VAULT_ADDRESS=$3
DPP_VALUES_FILE="./../../../charts/digital-product-pass/values.yaml"
EDC_CONSUMER_VALUES_FILE="./../../infrastructure/data-consumer/edc-consumer/values.yaml"
EDC_PROVIDER_VALUES_FILE="./../../infrastructure/data-provider/edc-provider/values.yaml"
REGISTRY_VALUES_FILE="./../../infrastructure/data-provider/registry/values.yaml"

export GH_TOKEN=${GITHUB_TOKEN} 
export VAULT_ADDR=${VAULT_ADDRESS}

VALUE=""

get_value() {

  field_name=$1
  path=$2

  VALUE=$(vault kv get -field="${field_name}" "${path}")

}

echo "Login into Hashicorp vault..."
vault login -method=github token=$GH_TOKEN
echo
echo "Retrieve secrets from Hashicorp vault..."
echo

get_value "api.key" "material-pass/dev/edc/oauth"; EDC_API_AUTH_KEY=$VALUE
get_value "client.id" "material-pass/dev/edc/oauth"; EDC_OAUTH_CLIENT_ID=$VALUE
get_value "realm" "material-pass/dev/oauth"; REALM=$VALUE
get_value "appId" "material-pass/dev/oauth"; APP_ID=$VALUE
get_value "clientId" "material-pass/dev/backend"; DPP_CLIENT_ID=$VALUE
get_value "clientSecret" "material-pass/dev/backend"; DPP_CLIENT_SECRET=$VALUE
get_value "miwUrl" "material-pass/dev/edc/ssi"; MIW_URL=$VALUE
get_value "authorityId" "material-pass/dev/edc/ssi"; AUTHORITY_ID=$VALUE
get_value "clientId" "material-pass/dev/edc/ssi"; SSI_CLIENT_ID=$VALUE
get_value "user" "material-pass/dev/edc/database"; POSTGRES_DB_USER=$VALUE
get_value "password" "material-pass/dev/edc/database"; POSTGRES_DB_PASSWORD=$VALUE
get_value "bpnNumber" "material-pass/dev/edc/participant"; PARTICIPANT_ID=$VALUE
get_value "vault.hashicorp.url" "material-pass/dev/edc/vault"; VAULT_HASHICORP_URL=$VALUE
get_value "vault.hashicorp.api.secret.path" "material-pass/dev/edc/vault"; VAULT_HASHICORP_SECRET_PATH=$VALUE
get_value "vault.hashicorp.token" "material-pass/dev/edc/vault"; VAULT_HASHICORP_TOKEN=$VALUE
get_value "user" "material-pass/dev/irs/minio"; IRS_MINIO_USER=$VALUE
get_value "password" "material-pass/dev/irs/minio"; IRS_MINIO_PASSWORD=$VALUE
get_value "clientId" "material-pass/dev/irs/keycloak/oauth2"; IRS_KEYCLOAK_CLIENT_ID=$VALUE
get_value "clientSecret" "material-pass/dev/irs/keycloak/oauth2"; IRS_KEYCLOAK_CLIENT_SECRET=$VALUE
get_value "database.user" "material-pass/dev/aasregistry"; REGISTRY_USER=$VALUE
get_value "database.password" "material-pass/dev/aasregistry"; REGISTRY_PASSWORD=$VALUE


set_values () {

  ### DIGITAL PRODUCT PASS APP

  echo "Replace client credentials with actual values in digital-product-pass values file..."
  echo
  sed -i "s|<path:material-pass/data/dev/edc/oauth#api.key>|${EDC_API_AUTH_KEY}|g" ${DPP_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/participant#bpnNumber>|${PARTICIPANT_ID}|g" ${DPP_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/oauth#realm>|${REALM}|g" ${DPP_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/oauth#appId>|${APP_ID}|g" ${DPP_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/backend#clientId>|${DPP_CLIENT_ID}|g" ${DPP_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/backend#clientSecret>|${DPP_CLIENT_SECRET}|g" ${DPP_VALUES_FILE};
  
  ### CONSUMER EDC CONNECTOR

  echo "Replace secrets with actual values in edc-consumer values file..."
  echo

  sed -i "s|<path:material-pass/data/dev/edc/participant#bpnNumber>|${PARTICIPANT_ID}|g" ${EDC_CONSUMER_VALUES_FILE};

  # Postgres DB
  sed -i "s|<path:material-pass/data/dev/edc/database#user>|${POSTGRES_DB_USER}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/database#password>|${POSTGRES_DB_PASSWORD}|g" ${EDC_CONSUMER_VALUES_FILE};

  # Hashicorp vault
  sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|${VAULT_HASHICORP_URL}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|${VAULT_HASHICORP_SECRET_PATH}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|${VAULT_HASHICORP_TOKEN}|g" ${EDC_CONSUMER_VALUES_FILE};
  
  # EDC Oauth
  sed -i "s|authKey: <path:material-pass/data/dev/edc/oauth#api.key>|authKey: ${EDC_API_AUTH_KEY}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|secret: <path:material-pass/data/dev/edc/oauth#api.key>|secret: ${EDC_API_AUTH_KEY}|g" ${EDC_CONSUMER_VALUES_FILE};

  # ssi
  sed -i "s|<path:material-pass/data/dev/edc/ssi#miwUrl>|${MIW_URL}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/ssi#authorityId>|${AUTHORITY_ID}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/ssi#clientId>|${SSI_CLIENT_ID}|g" ${EDC_CONSUMER_VALUES_FILE};

  # irs
  sed -i "s|<path:material-pass/data/dev/irs/minio#user>|${IRS_MINIO_USER}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/irs/minio#password>|${IRS_MINIO_PASSWORD}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/irs/keycloak/oauth2#clientId>|${IRS_KEYCLOAK_CLIENT_ID}|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/irs/keycloak/oauth2#clientSecret>|${IRS_KEYCLOAK_CLIENT_SECRET}|g" ${EDC_CONSUMER_VALUES_FILE};


  ### PROVIDER EDC CONNECTOR

  echo "Replace secrets with actual values in edc-provider values file..."
  echo

  sed -i "s|<path:material-pass/data/dev/edc/participant#bpnNumber>|${PARTICIPANT_ID}|g" ${EDC_PROVIDER_VALUES_FILE};

  # Postgres DB
  sed -i "s|<path:material-pass/data/dev/edc/database#user>|${POSTGRES_DB_USER}|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/database#password>|${POSTGRES_DB_PASSWORD}|g" ${EDC_PROVIDER_VALUES_FILE};

  # Hashicorp vault
  sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|${VAULT_HASHICORP_URL}|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|${VAULT_HASHICORP_SECRET_PATH}|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|${VAULT_HASHICORP_TOKEN}|g" ${EDC_PROVIDER_VALUES_FILE};
  
  # EDC Oauth
  sed -i "s|authKey: <path:material-pass/data/dev/edc/oauth#api.key>|authKey: ${EDC_API_AUTH_KEY}|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|secret: <path:material-pass/data/dev/edc/oauth#api.key>|secret: ${EDC_API_AUTH_KEY}|g" ${EDC_PROVIDER_VALUES_FILE};


  # ssi
  sed -i "s|<path:material-pass/data/dev/edc/ssi#miwUrl>|${MIW_URL}|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/ssi#authorityId>|${AUTHORITY_ID}|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/edc/ssi#clientId>|${SSI_CLIENT_ID}|g" ${EDC_PROVIDER_VALUES_FILE};

  ### PROVIDER REGISTRY

  echo "Replace secrets with actual values in registry values file..."
  echo

  # Postgres DB
  sed -i "s|<path:material-pass/data/dev/aasregistry/database.user>|${REGISTRY_USER}|g" ${REGISTRY_VALUES_FILE};
  sed -i "s|<path:material-pass/data/dev/aasregistry/database.password>|${REGISTRY_PASSWORD}|g" ${REGISTRY_VALUES_FILE};
}

unset_values() {

  ### DIGITAL PRODUCT PASS APP

  echo "Replace actual values with placholders in digital-product-pass values file..."
  echo
  sed -i "s|${EDC_API_AUTH_KEY}|<path:material-pass/data/dev/edc/oauth#api.key>|g" ${DPP_VALUES_FILE};
  sed -i "s|${PARTICIPANT_ID}|<path:material-pass/data/dev/edc/participant#bpnNumber>|g" ${DPP_VALUES_FILE};
  sed -i "s|${REALM}|<path:material-pass/data/dev/oauth#realm>|g" ${DPP_VALUES_FILE};
  sed -i "s|${APP_ID}|<path:material-pass/data/dev/oauth#appId>|g" ${DPP_VALUES_FILE};
  sed -i "s|${DPP_CLIENT_ID}|<path:material-pass/data/dev/backend#clientId>|g" ${DPP_VALUES_FILE};
  sed -i "s|${DPP_CLIENT_SECRET}|<path:material-pass/data/dev/backend#clientSecret>|g" ${DPP_VALUES_FILE};


  ### CONSUMER EDC CONNECTOR

  echo "Replace actual values with placholders in edc-consumer values file..."
  echo

  sed -i "s|${PARTICIPANT_ID}|<path:material-pass/data/dev/edc/participant#bpnNumber>|g" ${EDC_CONSUMER_VALUES_FILE};

  # Postgres DB
  sed -i "s|${POSTGRES_DB_USER}|<path:material-pass/data/dev/edc/database#user>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|password: ${POSTGRES_DB_PASSWORD}|password: <path:material-pass/data/dev/edc/database#password>|g" ${EDC_CONSUMER_VALUES_FILE};

  # Hashicorp vault
  sed -i "s|${VAULT_HASHICORP_URL}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|${VAULT_HASHICORP_SECRET_PATH}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|${VAULT_HASHICORP_TOKEN}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|g" ${EDC_CONSUMER_VALUES_FILE};
  
  # EDC Oauth
  sed -i "s|authKey: ${EDC_API_AUTH_KEY}|authKey: <path:material-pass/data/dev/edc/oauth#api.key>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|secret: ${EDC_API_AUTH_KEY}|secret: <path:material-pass/data/dev/edc/oauth#api.key>|g" ${EDC_CONSUMER_VALUES_FILE};

  # irs
  sed -i "s|${IRS_MINIO_USER}|<path:material-pass/data/dev/irs/minio#user>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|${IRS_MINIO_PASSWORD}|<path:material-pass/data/dev/irs/minio#password>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|clientId: ${IRS_KEYCLOAK_CLIENT_ID}|clientId: <path:material-pass/data/dev/irs/keycloak/oauth2#clientId>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|clientSecret: ${IRS_KEYCLOAK_CLIENT_SECRET}|clientSecret: <path:material-pass/data/dev/irs/keycloak/oauth2#clientSecret>|g" ${EDC_CONSUMER_VALUES_FILE};

  # ssi
  sed -i "s|${MIW_URL}|<path:material-pass/data/dev/edc/ssi#miwUrl>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|${AUTHORITY_ID}|<path:material-pass/data/dev/edc/ssi#authorityId>|g" ${EDC_CONSUMER_VALUES_FILE};
  sed -i "s|${SSI_CLIENT_ID}|<path:material-pass/data/dev/edc/ssi#clientId>|g" ${EDC_CONSUMER_VALUES_FILE};


  ### PROVIDER EDC CONNECTOR

  echo "Replace actual values with placholders in edc-provider values file..."
  echo

  sed -i "s|${PARTICIPANT_ID}|<path:material-pass/data/dev/edc/participant#bpnNumber>|g" ${EDC_PROVIDER_VALUES_FILE};


  # Postgres DB
  sed -i "s|${POSTGRES_DB_USER}|<path:material-pass/data/dev/edc/database#user>|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|password: ${POSTGRES_DB_PASSWORD}|password: <path:material-pass/data/dev/edc/database#password>|g" ${EDC_PROVIDER_VALUES_FILE};

  # Hashicorp vault
  sed -i "s|${VAULT_HASHICORP_URL}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.url>|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|${VAULT_HASHICORP_SECRET_PATH}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.api.secret.path>|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|${VAULT_HASHICORP_TOKEN}|<path:material-pass/data/dev/edc/vault#vault.hashicorp.token>|g"  ${EDC_PROVIDER_VALUES_FILE};
  
  # EDC Oauth
  sed -i "s|authKey: ${EDC_API_AUTH_KEY}|authKey: <path:material-pass/data/dev/edc/oauth#api.key>|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|secret: ${EDC_API_AUTH_KEY}|secret: <path:material-pass/data/dev/edc/oauth#api.key>|g" ${EDC_PROVIDER_VALUES_FILE};

  # ssi
  sed -i "s|${MIW_URL}|<path:material-pass/data/dev/edc/ssi#miwUrl>|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|${AUTHORITY_ID}|<path:material-pass/data/dev/edc/ssi#authorityId>|g" ${EDC_PROVIDER_VALUES_FILE};
  sed -i "s|${SSI_CLIENT_ID}|<path:material-pass/data/dev/edc/ssi#clientId>|g" ${EDC_PROVIDER_VALUES_FILE};


  ### PROVIDER REGISTRY

  echo "Replace secrets with actual values in registry values file..."
  echo

  # Postgres DB
  sed -i "s|${REGISTRY_USER}|<path:material-pass/data/dev/aasregistry/database.user>|g" ${REGISTRY_VALUES_FILE};
  sed -i "s|password: ${REGISTRY_PASSWORD}|password: <path:material-pass/data/dev/aasregistry/database.password>|g" ${REGISTRY_VALUES_FILE};


}


 if [[ "$1" -eq 1 ]] ; then
    echo "Setting values in the following helm values (DPP, edc-consumer, edc-provider, registry)"
    set_values
  elif [[ "$1" -eq 0 ]] ; then
   echo "Un Setting values in the following helm values (DPP, edc-consumer, edc-provider, registry)"
    unset_values
  else
    echo "invalid value provided, please provide a value 0 or 1"
  fi

echo "Done..."
