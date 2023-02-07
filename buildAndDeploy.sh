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


CONTAINER_NAME=$1
IMAGE_NAME="consumer-ui"
IMAGE_TAG="latest"
VUE_APP_CLIENT_ID=""
VUE_APP_CLIENT_SECRET=""
X_API_KEY=""
BACKEND=""
VERSION=""
API_TIMEOUT=""
API_DELAY=""
API_MAX_RETRIES=""

docker rm -f ${CONTAINER_NAME}

echo "Build docker image..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

echo "Run docker container..."
docker run --name ${CONTAINER_NAME} -p 8080:80 -d -e VUE_APP_CLIENT_ID=${VUE_APP_CLIENT_ID} -e VUE_APP_CLIENT_SECRET=${VUE_APP_CLIENT_SECRET} -e X_API_KEY=${X_API_KEY} -e APP_BACK=${BACKEND} -e APP_VER=${VERSION} -e APP_API_TIMEOUT=${API_TIMEOUT} -e APP_API_MAX_RETRIES=${API_MAX_RETRIES} -e APP_API_DELAY=${API_DELAY} ${IMAGE_NAME}:${IMAGE_TAG}

echo "Done"