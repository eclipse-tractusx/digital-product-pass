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


CONTAINER_NAME=$1
IMAGE_NAME="consumer-ui"
IMAGE_TAG="latest"
VERSION=""
API_TIMEOUT=""
API_DELAY=""
API_MAX_RETRIES=""
BACKEND_URL=""
SERVER_URL=""
IDP_URL=""
KEYCLOAK_CLIENTID=""
KEYCLOAK_REALM=""
KEYCLOAK_ONLOAD=""

docker rm -f ${CONTAINER_NAME}

echo "Build docker image..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

echo "Run docker container..."
docker run --name ${CONTAINER_NAME} -p 8080:80 -d -e APP_VERSION=${VERSION} -e APP_API_TIMEOUT=${API_TIMEOUT} -e APP_API_MAX_RETRIES=${API_MAX_RETRIES} -e APP_API_DELAY=${API_DELAY} -e IDENTITY_PROVIDER_URL=${IDP_URL} -e HOST_URL=${SERVER_URL} -e DATA_URL=${BACKEND_URL} -e KEYCLOAK_CLIENTID=${KEYCLOAK_CLIENTID} -e KEYCLOAK_REALM=${KEYCLOAK_REALM} -e  KEYCLOAK_ONLOAD=${KEYCLOAK_ONLOAD} ${IMAGE_NAME}:${IMAGE_TAG}

echo "Done"
