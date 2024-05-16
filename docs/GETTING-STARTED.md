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

# Digital Product Passport Guide

> [CatenaX Introduction](https://catena-x.net)

## Application Infrastructure

```          
                    |----Consumer-Frontend----|       |---Consumer-Backend---|       |--------EDC-Consumer--------|  
        Consumer    |     DPP frontend        | <---> |     DPP Backend      | <---> | Controlplane <-> Dataplane |             
                    |-------------------------|       |----------------------|       |----------------------------|


                            ---------   ---------------------   --------------------    -------------------    -------------------        
        CatenaX Services    |  IAM  |   |  HashiCorp Vault  |   | Discovery Finder |    |  BPN Discovery  |    |  EDC Discovery  |          
                            ---------   ---------------------   --------------------    -------------------    -------------------


                    |-------Registry-------|       |--------EDC-Provider--------|  
        Provider    |         DTR          | <---> | Controlplane <-> Dataplane |<---> Backend Service           
                    |----------------------|       |----------------------------|

```

The `EDC-Provider` holds data that is managed via an __Data Management API__ and owned by product owners. To use the
data offer, the `EDC-Consumer` needs to __negotiate a contract for the data transfer__. The `CatenaX-Services` gives the
platform for the services to basically find each other (provides the meta-data).

The consumer frontend `DPP frontend` provides a User Interface to request battery/product passports through edc connectors and the
final __representation of the data__.

### Application Installation

> Prerequisite: 
- [Docker](https://www.docker.com/products/docker-desktop/)
- [MiniKube](https://minikube.sigs.k8s.io/docs/start/)
- [Helm](https://helm.sh/docs/intro/install/) 

#### Start Minikube Cluster
```bash
# start minikube cluster
minikube start --cpus 4 --memory 8096

# enable minikube ingress addon
minikube addons enable ingress
```

The secrets/credentials for all components are stored in a centralized vault service (HashiCorp Vault). The secrets in configurations must be populated with their values, and security must also be ensured during the substitution process. To achieve this, a shell script is available to set/unset [init-values.sh](../deployment/local/testing/init-values.sh) in required components as needed. 

> Prerequisite: Prior to run the scripts, the values for the follwoing environment variables should be placed in the script.

__Script Environment Variables:__
* _GH_TOKEN_ (Generate the token from GitHub)
* _VAULT_ADDR_ (Request for vault address)
* _LATEST_IMAGE_TAG_FROM_GIT_COMMIT_SHA_ (Get the latest image tag from GitHub registry)

```bash
# Navigate to working directory
cd ../deployment/local/testing

# set values for local run
# ./init-values.sh <0 or 1> <GH_TOKEN> <VAULT_ADDRESS>
# 0: unset values back to the placeholders
# 1: set actual values from the vault storage

# set values
./init-values.sh 1 <GH_TOKEN> <VAULT_ADDRESS>

# unset values
./init-values.sh 0 <GH_TOKEN> <VAULT_ADDRESS>
```



* __CatenaX-Services__
    * Description: The following components are centrally managed by CatenaX:
      * Discovery Finder to search for the discovery type (e.g., manufacturerPartId, bpn)
      * BPN Discovery to search for the BPN
      * EDC Discovery to search for an appriopriate EDC connector endpoint

* __EDC-Consumer__
    * Description: This component consists of different services which are described in
      the [Connector Setup](https://github.com/eclipse-tractusx/tractusx-edc/tree/main/charts/tractusx-connector).
    * __Controlplane__ & __Dataplane__
        * [Helm Chart](../deployment/infrastructure/data-consumer/edc-consumer) hosted locally
```bash
# Navigate to the working directory
cd ../deployment/infrastructure/data-consumer/edc-consumer

# Update chart dependencies
helm dependency update .

# install helm chart named edc-consumer
helm install edc-consumer . -f ./values.yaml

# optional: remove/uninstall helm chart 
helm uninstall edc-consumer
```
The edc-consumer chart deploys all consumer components e.g., controlplane, dataplane, postgresql and other components.

```bash
# status check
$ kubectl get pods

# output
NAME                                             READY   STATUS    RESTARTS      AGE
consumer-postgresql-0                            1/1     Running   0             49s
edc-consumer-controlplane-787bdd7b74-hznz2       1/1     Running   2             49s
edc-consumer-dataplane-5f9988589f-98kc6          1/1     Running   0             49s
```

In order to access the data managemnt APIs locally from these charts, kubectl port forwarding is required.

#### Ports Mapping:

```bash
kubectl get services
kubectl port-forward services/edc-consumer-controlplane 31639:8181
kubectl port-forward services/edc-consumer-controlplane 31216:8282
```

Integration (INT) deployment available through postman: [https://dpp.int.demo.catena-x.net/consumer](https://dpp.int.demo.catena-x.net/consumer/)
  
 __Hashicorp-Vault__ & __DAPS__ are centralized components and managed by central CatenaX services.
* __EDC-Provider__
    * Description: This component consists of different services which are described in
      the [Connector Setup](https://github.com/eclipse-tractusx/tractusx-edc/tree/main/charts/tractusx-connector).
    * __Controlplane__ & __Dataplane__
        * [Helm Chart](../deployment/infrastructure/data-provider/edc-provider) hosted locally

```bash
# Navigate to the working directory
cd ../deployment/infrastructure/data-provider/edc-provider

# Update chart dependencies
helm dependency update .

# install helm chart named edc-provider
helm install edc-provider . -f ./values.yaml

# optional: remove/uninstall helm chart 
helm uninstall edc-provider
```
The edc-provider chart deploys all provider components e.g., provider backend, controlplane, dataplane and postgres pods.

```bash
# status check
$ kubectl get pods

# output
NAME                                         READY   STATUS    RESTARTS      AGE
edc-provider-backend-5f9fb86f8d-qmjqg        1/1     Running   0             46s
edc-provider-controlplane-6799fc4675-7b9tz   1/1     Running   2             46s
edc-provider-dataplane-6449bcd495-rnz8p      1/1     Running   0             46s
provider-postgresql-0                        1/1     Running   0             46s
```

In order to access the data managemnt APIs locally from these charts, kubectl port forwarding is required.

#### Ports Mapping:
```bash
kubectl get services
kubectl port-forward services/edc-provider-controlplane 31495:8181
kubectl port-forward services/edc-provider-controlplane 8282:8282
kubectl port-forward services/edc-provider-backend 8081:8081
```
Link to the Integration (INT) environment: [https://dpp.int.demo.catena-x.net/provider](https://dpp.int.demo.catena-x.net/provider/)
  
 __Hashicorp-Vault__ is centralized components and managed by CatenaX shared services.
        
* __Digital Product Passport (DPP) Components__
    - Consumer Frontend
    - Consumer Backend
    
    [Helm Chart](../charts/digital-product-pass/) hosted locally

    - Reference docs: 
        - [INSTALL.md](../INSTALL.md)
        - [Productpass backend](../dpp-backend/digitalproductpass/README.md)

Link to the Integration (INT) environment: [https://dpp.int.demo.catena-x.net](https://dpp.int.demo.catena-x.net/)

```bash
# Navigate to the working directory
cd ../charts/digital-product-pass

# install helm chart named digital-product-pass in namespace product-material-pass
helm install digital-product-pass . -f ./values.yaml --namespace product-material-pass --create-namespace

# optional: remove/uninstall helm chart 
helm uninstall digital-product-pass
```

```bash
# status check
$ kubectl get pods -n product-material-pass

# output
NAME                            READY   STATUS    RESTARTS   AGE
dpp-backend-74c6c6854c-lhw27    1/1     Running      0       36s
dpp-frontend-6fb95f466-t2m4h    1/1     Running      0       36s
```

#### Port Mapping:

```bash
kubectl get services -n product-material-pass
kubectl port-forward services/consumer-ui 8080:8080 -n product-material-pass
kubectl port-forward services/consumer-backend 8888:8888 -n product-material-pass
```

__Notes:__

* Port mappings are `kubectl port-forward services/<SERVICE_NAME> host-port:service-port`

### Application Usage

To use the application, data needs to be prepared in the __EDC-Provider__ using the provider setup script [init-provider-dev.sh](../deployment/local/testing/upload-testdata.sh)

__Optional:__ One can also use the postman collection
in [deployment/local/postman/Digital-Product-Pass](../deployment/local/postman/Digital-Product-Pass-collection.json) to access APIs, used among DPP components. As a prerequisite, [Postman](https://www.postman.com/downloads/) agent is required.


| Who            | Action/Events                                                                                                |
| :---:          | :---                                                                                                         |
| _PROVIDER_     | Prepare the __Provider__ with `sample data`, `assets`, `policies` and `contract definition`                  |
| _PROVIDER_     | Setup the __Registry__                                                                                       | 
| _PROVIDER_     | Register the __Provider__ as `digital twin` into the __Registry__                                            |
| _CONSUMER_     | Lookup for the __Registry__ to find the right `digital twin`                                                 |
| _CONSUMER_     | Lookup for the the `digital twin` from the __Registry__ (searched in previous step)                          |
| _CONSUMER_     | Get the `shell descriptor` and the `submodels` from the __Registry__ for the __Consumer__                    |
| _CONSUMER_     | Get the `specific submodel` from the __Registry__ (more than one could be delivered in the previous step)    |
| _CONSUMER_     | Get the contract offer catalog for the __Consumer__                                                          |
| _CONSUMER_     | Negotiate the contract between the __Consumer__ and __Provider__ with the `submodel` for the `digital twin`  |
| _CONSUMER_     | Perform the `data transfer` between the __Consumer and Provider__                                            |
|                |                                                                                                              |

The __Digital Product Passport Uer Interface__ will make this process accessible to users.

__Note:__ Adjust the URLs according to the _local_ (`http://localhost:port/`) or
_integration_ (`https://dpp.int.demo.catena-x.net/`) environments.

- [Documentation of EDC Data Transfer](https://github.com/eclipse-tractusx/tractusx-edc/blob/main/docs/usage/management-api-walkthrough/06_transferprocesses.md)
- [End-to-End Use Case](https://catenax-ng.github.io/docs/guides/catenax-at-home#end-to-end-use-case)


## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023, 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
