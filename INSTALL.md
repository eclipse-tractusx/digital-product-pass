<!--
  Catena-X - Product Passport Consumer Frontend
 
  Copyright (c) 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
  See the NOTICE file(s) distributed with this work for additional
  information regarding copyright ownership.
 
  This program and the accompanying materials are made available under the
  terms of the Apache License, Version 2.0 which is available at
  https://www.apache.org/licenses/LICENSE-2.0.
 
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
  either express or implied. See the
  License for the specific language govern in permissions and limitations
  under the License.
 
  SPDX-License-Identifier: Apache-2.0
-->

<h1 style="display:flex; align-items: center;"><img src="./docs/catena-x-logo.svg"/>&nbsp;&nbsp;Digital Product Pass Application</h1>

# Application Installation

## Pre-requisites

You must have [Helm](https://helm.sh/),  [Minikube](https://minikube.sigs.k8s.io/docs/start/) and [Maven](https://maven.apache.org/) to follow this steps.


## Install

To install the application using the configured helm charts use the following command from the project root directory:

```bash
helm install digital-product-pass ./charts/digital-product-pass -f charts/digital-product-pass/values.yaml -f charts/digital-product-pass/values-int.yaml 
``` 

> **NOTE**: This command will deploy the complete application.

## Expose the ports

Once the application is running, in order for you to access it, we need to expose the ports. Following this commands we will be able to access it.

### Get pod name
Search for the application name:

```bash
kubectl get pods -n product-material-pass --no-headers |  awk '{if ($1 ~ "consumer-backend-") print $1}'
```
**Example**:

![img4.png](./consumer-backend/productpass/docs/media/img4.png)


Copy the pod name with the prefix `consumer-backend`

### Port forward

Paste the pod name after the `port-forward` parameter. 
```bash
kubectl -port-forward consumer-backend-67c4c9678-nqg7p 8888:8888 -n product-material-pass
```

> **NOTE**: The default port set is `8888` however it can be changed in the configuration.

### Check if the application is running

Go to the following url to check the health status:
```
localhost:8888/health
```

# Frequently asked questions

## How to install the application and run it locally?

Use the following commands to install/compile the application:

### Compile Backend
```bash
cd consumer-backend/productpass 
mvn clean install test
```

### Run the JAR file

Substitute the `<version>` variable with the current version of the Digital Product Pass Backend and run the jar:

```bash

./target/productpass-<version>-SNAPSHOT.jar

```
### Configure the secrets

Once the application is running a tmp file will be created in the following directory: `data/VaultConfig/vault.token.yml`

```yml
client:
  id: <Add the Keycloak client.id here>
  secret: <Add the Keycloak client.secret here>
edc: 
  apiKey: <Add the Keycloak edc.apiKey here>
  participantId: <Add the Keycloak edc.participantId here>
```

## How to start the application locally using Spring Boot?

Use the following command using [maven](https://maven.apache.org/) to start the application

```bash 
mvn clean spring-boot:run
```


# Compile Frontend Installation

### Prerequisites:

- Git
- Code editor (VS Code/ IntelliJ recommended)
- Nodejs 16 (Node Package Manager - npm)
- Vuejs
- Docker
- Git Bash (for windows operating system only)

## Installation
### Clone project repository

```bash
git clone https://github.com/eclipse-tractusx/digital-product-pass.git
```

### Install dependencies

```bash
cd digital-product-pass/
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

#### Method 2: With Docker

The following environment variables must be set in [build and deploy](./buildAndDeploy.sh) script:

- PASS_VERSION
- APP_VERSION
- APP_API_TIMEOUT
- APP_API_MAX_RETRIES
- APP_API_DELAY
- IDENTITY_PROVIDER_URL
- HOST_URL
- DATA_URL
- KEYCLOAK_CLIENTID
- KEYCLOAK_REALM
- KEYCLOAK_ONLOAD



```bash
# run script
../buildAndDeploy.sh consumer-ui
```

You can run the application in docker container with existing image `ghcr.io/eclipse-tractusx/digital-product-pass/consumer-ui:latest` from GitHub packages. You need to update the [build and deploy](./buildAndDeploy.sh) script.

The consumer frontend is available in browser at [http://localhost:8080](http://localhost:8080)

## Coding styles

### How to set up a code editor

See [VSCode configuration](https://code.visualstudio.com/docs/getstarted/settings).