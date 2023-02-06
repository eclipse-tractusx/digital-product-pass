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

echo "[INFO] Starting build..."
echo "[INFO] Checking for docker daemon and opened containers..."
if (! docker stats --no-stream );
    then
        printf "\n******* [DEPLOY FAILED] ******************\n"
        printf "[ERROR] Your Docker Daemon is not up, please open your docker app!"
        printf "\n*****************************************"
        exit -1
fi
printf "\n[INFO] Docker Daemon is up... "

version=${1}

if [ -z "$version" ];
then
    echo "[ERROR] Please indicate the deployment version: deploy.sh <version>"
    exit -1
fi

echo "[INFO] Building docker image for version: $version..."
docker build -t ghcr.io/catenax-ng/product-battery-passport-consumer-app/product-pass-consumer-backend:$version .

echo "[INFO] Tagging docker image version: $version..."
docker tag ghcr.io/catenax-ng/product-battery-passport-consumer-app/product-pass-consumer-backend:$version ghcr.io/catenax-ng/product-battery-passport-consumer-app/product-pass-consumer-backend:$version

echo "[INFO] Pushing docker image to repo: $version..."
docker push ghcr.io/catenax-ng/product-battery-passport-consumer-app/product-pass-consumer-backend:$version

