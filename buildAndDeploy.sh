#!/bin/bash

CONTAINER_NAME=$1
IMAGE_NAME="consumer-ui"
IMAGE_TAG="latest"
VUE_APP_CLIENT_ID=""
VUE_APP_CLIENT_SECRET=""
X_API_KEY=""

docker rm -f ${CONTAINER_NAME}

echo "Build docker image..."
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .

echo "Run docker container..."
docker run --name ${CONTAINER_NAME} -p 8080:80 -d -e VUE_APP_CLIENT_ID=${VUE_APP_CLIENT_ID} -e VUE_APP_CLIENT_SECRET=${VUE_APP_CLIENT_SECRET} -e X_API_KEY=${X_API_KEY} ${IMAGE_NAME}:${IMAGE_TAG}

echo "Done"