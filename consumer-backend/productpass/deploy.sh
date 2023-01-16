echo "[INFO] Starting build..."
echo "[INFO] Checking for docker daemon and opened containers..."
if (! docker stats --no-stream );
    then
        printf "\n******* [BUILD FAILED] ******************\n"
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

