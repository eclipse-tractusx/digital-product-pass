<!--
  Tractus-X - Digital Product Passport Application
 
  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
  Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

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

<h1 style="display:flex; align-items: center;"><img src="./docs/media/catenaxLogo.svg"/>&nbsp;&nbsp;Digital Product Pass Application</h1>

# Application Installation

## Pre-requisites

You must have [Helm](https://helm.sh/),  [Minikube](https://minikube.sigs.k8s.io/docs/start/) and [Maven](https://maven.apache.org/) to follow this steps.

#### Start Minikube Cluster
```bash
# start minikube cluster
minikube start --cpus 4 --memory 8096

# enable minikube ingress addon
minikube addons enable ingress
```


## Install

### Configuration

First configure the [`values.yaml`](./charts/digital-product-pass/values.yaml) file with the secrets and the necessary configuration for starting the application correctly.

The documentation of the backend configuration is available here [README.md](./dpp-backend/digitalproductpass/README.md)

> **TIP**: For a correct Catena-X integration, get the appropriate credentials from the Portal! You can also place secrets in a Vault so that the credentials are safe!

### Deployment

Before the application is deployed, the persistance is necessary and must be existed. 

```bash
# Launch persistent volume
kubectl apply -f ./deployment/local/storage/pv-data.yaml
``` 
Use the following command to install the application as configured helm deployment:

```bash
helm install digital-product-pass ./charts/digital-product-pass -f charts/digital-product-pass/values.yaml
``` 

> **NOTE**: This command will deploy the complete application. However, it would take some minutes until all the pods are up and in running state. You can check the pod state repeatedly by the following command.

```bash
# To check the pod status
kubectl get pods
``` 

#### Expose necessary ports

Once the application is running, in order for you to access it, we need to expose the ports. Following this commands we will be able to access it.

#### Get pod name
Search for the application name:
The default value of <YOUR-NAMESPACE> is **default**.

```bash
kubectl get pods -n <YOUR-NAMESPACE> --no-headers |  awk '{if ($1 ~ "dpp-backend-") print $1}'
```
**Example**:

![img4.png](./dpp-backend/digitalproductpass/docs/media/podname.png)


Copy the pod name with the prefix `dpp-backend`

### Port forward

Paste the pod name after the `port-forward` parameter. 
```bash
kubectl -port-forward dpp-backend-95b5d4989-xvbkq 8888:8888 -n default
```

> **NOTE**: The default port set is `8888` however it can be changed in the configuration.

### Check if the application is running

Go to the following url to check the health status:
```
localhost:8888/health
```

# Frequently asked questions

## How to install the application and run it locally without docker and helm charts?

Use the following commands to install/compile the application:

### Compile Backend
```bash
cd dpp-backend/digitalproductpass 
mvn clean install test
```

### Run the JAR file

Substitute the `<version>` variable with the current version of the Digital Product Pass Backend and run the jar:

```bash

./target/digitalproductpass-<version>-SNAPSHOT.jar

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

The following environment variables must be set in [build and deploy](./dpp-frontend/buildAndDeploy.sh) script:

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
../buildAndDeploy.sh consumer-frontend
```

You can run the application in docker container with existing image `ghcr.io/catenax-ng/tx-digital-product-pass/digital-product-pass-frontend:latest` from GitHub packages. You need to update the [build and deploy](./buildAndDeploy.sh) script.

The consumer frontend is available in browser at [http://localhost:8080](http://localhost:8080)


## What must you do if you encounter problems like "the docker service isn't running. Try restarting the docker service."?

You need to start/restart your docker daemon, and then run the command (mentioned in pre-requisites) to the start minikube cluster.

## Local Setup

If you would like to run the entire application locally, please have a look at the [GETTING-STARTED.md](./docs/GETTING-STARTED.md)


## Coding styles

### How to set up a code editor

See [VSCode configuration](https://code.visualstudio.com/docs/getstarted/settings).
