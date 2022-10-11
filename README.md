# Product-Battery-Passport Consumer-App

This battery pass consumer application is used with the **Asset Administration Shell (AAS)**.

## Local Setup

### Tools:

- Git
- Code editor (VS Code/ IntelliJ recommended)
- Nodejs (Node Package Manager - npm)
- Vuejs
- Docker
- Git Bash (for windows operating system only)

### Cloning repository

```bash
# Project repository
git clone https://github.com/catenax-ng/product-battery-passport-consumer-app.git

# AAS repository
git clone https://github.com/catenax-ng/catenax-at-home.git
```

### Launch docker containers for the Asset Administration Shell (AAS)

```bash
cd catenax-at-home/getting-started-guide/
docker compose up -d
```

### Init Data Provider

```bash
cd ../../product-battery-passport-consumer-app/aas-int-provider/
chmod ug+x ./init-provider.sh

# Executing this script will create a sample battery passport data, create EDC asset, policies, contract definitions and register it as a digital twin inside registry.
./init-provider.sh
```

**Note:** To execute the shell-script with windows, the [Git Bash](https://gitforwindows.org/) is required.

### Install dependencies

```bash
cd ..
npm install --legacy-peer-deps
```

## Run Project

### Compiles and minifies for production

```bash
npm run build
```
#### Method 1: Without Docker

#### Compiles and hot-reloads for development

```bash
npm run serve
```

#### Method 2: Run with Docker

The Dockerfile is located in root directory of the project.

```bash
# build docker image
docker build -t consumer-ui:latest .

# run docker image
docker run -p 8080:80 --name consumer-ui -d consumer-ui:latest

# check logs
docker logs consumer-ui

# stop and remove docker container
docker stop consumer-ui; docker rm consumer-ui;
```
You can run the application in docker container with existing image from gitHub packages.

See consumer frontend docker image in registry: [consumer-ui](https://github.com/catenax-ng/product-battery-passport-consumer-app/pkgs/container/product-battery-passport-consumer-app%2Fconsumer-ui)

```bash
# pull the image 
# Replace LATEST_TAG with the most recent tag in registry

docker pull ghcr.io/catenax-ng/product-battery-passport-consumer-app/consumer-ui:LATEST_TAG

# run docker image
docker run -p 8080:80 --name consumer-ui -d ghcr.io/catenax-ng/product-battery-passport-consumer-app/consumer-ui:LATEST_TAG
```
The consumer frontend is available in browser at [http://localhost:8080](http://localhost:8080)


### How to set up a code editor

See [VSCode configuration](https://confluence.catena-x.net/pages/viewpage.action?pageId=55009683).

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).
