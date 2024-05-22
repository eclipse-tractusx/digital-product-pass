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

# DPP Application Installation

This guide provides the information needed to setup the Digital product Passport (DPP) application as a data consumer. It describes how to run a local setup to leverage the DPP frontend and backend components using a container platform.

                         ________________________Kubernetes Cluster________________________
                        |                                                                  |
                        |     _______________________Namespace________________________     |
                        |    |     ___________________         __________________     |    |
                        |    |    |                   |       |                  |    |    |
                        |    |    |   DPP Frontend    | <---> |    DPP Backend   |    |    |
                        |    |    |___________________|       |__________________|    |    |
                        |    |________________________________________________________|    |
                        |                                                                  |
                        |__________________________________________________________________|
            



## Prerequisites

You must have the following tools installed and configured:

- A container platform preferrably [Docker](https://docs.docker.com/engine/install/)
- A local Kubernetes cluster. It could be a [Minikube](https://minikube.sigs.k8s.io/docs/start/) or other cluster runtimes such as [Kind](https://kind.sigs.k8s.io/docs/user/quick-start/). In this tutorial we would use a single node minikube cluster.
- A [Helm](https://helm.sh/) package manager for Kubernetes cluster to run kubernetes resources. 
- MXD Setup (2 EDCS + Wallet + IAM)
- BPN Discovery Service + EDC Discovery Service + Discovery Finder

> [!CAUTION]
> For the worksession in 16, 17 May Second Tractus-X Community Days the deployement is already provided for you! So just data consumption and provision are going to be the center of the attention. You can skip this guide!

## Clone a Git repository

Use the following command in your terminal to clone the digital product pass git repository

```bash
git clone https://github.com/eclipse-tractusx/digital-product-pass.git
```

> [!Note]  
> If you already cloned this repository, you can ignore this step



## Setup a cluster

This step is optional for those who already have a running cluster.


```bash
# start minikube cluster
minikube start -p $USER

# enable minikube ingress addon
minikube addons enable ingress -p $USER

# create your namespace
kubectl create namespace $USER
```

> [!TIP]
> $USER is a current user account from Operating System
> The current usr can be checked using command: `echo $USER` or `whoami`


## Check cluster availability


```bash
# check is cluster is accessible using the forrowing command
kubectl config current-context

# output - you must see your minikube as current context
# minikube

# check your namespace is accessible
kubectl get pods

# output
# No resources found in $USER namespace
```

> [!CAUTION]
> Each user must follow the guide inside its user namespace

<!-- #### Start Minikube Cluster
```bash
# start minikube cluster
minikube start --cpus 4 --memory 8096

# enable minikube ingress addon
minikube addons enable ingress
``` 
Install helm tool

If Kind Cluster is used:
Deploy ingress controller explicitly


helm install dpp ...

Open new terminal and write minikube tunnel

Access the app in browser
-->


## Configure the helm values

Go to the [values.yaml](../../charts/digital-product-pass/values.yaml) file

### Backend Configuration
The backend has the following settings to be configured:

* Add the *HOSTNAME* to the **backend** settings
```yaml
backend:
    hostname: "<HOSTNAME>"
```

* Add the *HOSTNAME* to the **ingress** settings

```yaml
ingress:
	hosts:
        - host: "<HOSTNAME>"
    tls:
        - secretName: tls-secret
        hosts:
            - "<HOSTNAME>"
```

* Add the *X_API_KEY*, *PARTICIPANT_ID* and *EDC_HOSTNAME* to the **edc** settings

```yaml
edc:
    xApiKey: "<X_API_KEY>"
    participantId: "<PARTICIPANT_ID>"
    hostname: "<EDC_HOSTNAME>"
```

* Add the *HOSTNAME* and set *enabled* flag to *TRUE* to the **irs**
```yaml
irs:
    enabled: true
    hostname: "<HOSTNAME>"
```

* Add the *ENCRYPTION_KEY* to the **process**
```yaml
process:
    encryptionKey:"<ENCRYPTION_KEY>"
```

* Add the *DISCOVERY_FINDER_HOST* to the **discovery** settings
```yaml
discovery:
    hostname: "<DISCOVERY_FINDER_HOST>"
```

### Frontend Configuration
The backend has the following settings to be configured:

* Add the *HOSTNAME* to the **ingress** settings
```yaml
ingress:
    hosts:
        - host: "<HOSTNAME>"
    tls:
        - secretName: tls-secret
        hosts:
            - "<HOSTNAME>"
```

* Add the *HOSTNAME* to the **backend**
```yaml
backend:
    hostname: "<HOSTNAME>"
```

* Add the *PORTAL_HOSTNAME* to the **portal** settings
```yaml
portal:
    hostname: "<PORTAL_HOSTNAME>"
```

* Add the following values to the **oauth** configuration:
*HOSTNAME*, *CLIENT_ID*,*CLIENT_SECRET*, *REALM*, *APP_ID*, *BPN* and *API_KEY*
```yaml
oauth:
    hostname: "<HOSTNAME>"
    techUser:
        clientId: "<CLIENT_ID>"
        clientSecret: "<CLIENT_SECRET>"
    realm: "<REALM>"
    appId: "<APP_ID>"
    bpnCheck:
        enabled: true
        bpn: "<BPN>"
    roleCheck:
        enabled: false
    apiKey:
        header: "X-Api-Key"
        secret: "<API_KEY>"
```

## Deploy the Application

Use the following command to install the application as configured helm deployment: 

```bash
# install helm charts -DPP
helm install digital-product-pass ./charts/digital-product-pass -f ./charts/digital-product-pass/values.yaml --namespace $USER

# you would see the similar output once the application is deployed
NAME: digital-product-pass
LAST DEPLOYED: THU May 9 16:24:00 2024
NAMESPACE: dpp01
STATUS: deployed
REVISION: 1

# wait until all the pods are ready
kubectl wait --namespace $USER \
  --for=condition=ready pod all \
  --timeout=90s
```

> **NOTE**: This command will deploy the complete application. However, it would take some minutes until all the pods are up and in running state. You can check the pod state repeatedly by the following command.


## Expose the application
```bash
# enable port forwarding to expose the frontend as a service to outside world
kubectl port-forward service/dpp-frontend --namespace $USER <EXTERNAL_PORT>:8080 --address 0.0.0.0

# enable port forwarding to expose the backend application as a service to outside world
kubectl port-forward service/dpp-backend --namespace $USER <EXTERNAL_PORT>:8888 --address 0.0.0.0

# access the application through your browser and your mobile device
http://<PUBLIC_IP>:<EXTERNAL_PORT>
```


## Undeploy the Application

```bash
# in case of any issue, redeploy the application
# uninstall the existing helm chart
helm uninstall digital-product-pass --namespace $USER

# output of the above command
release "digital-product-pass" uninstalled
```

Install the helm charts again

```bash
# install helm charts - DPP
helm install digital-product-pass ./charts/digital-product-pass -f ./charts/digital-product-pass/values.yaml --namespace $USER
```

If everything works fine, then you have reached at the end of deployment guide.

Congratulations, you have successfully setup the data consumer Digital Product Passport application. It is now available and ready to exchange data in the dataspace.

## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2023, 2024 BMW AG
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
