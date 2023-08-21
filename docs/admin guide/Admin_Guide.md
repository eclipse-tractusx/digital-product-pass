<!--
  Catena-X - Product Passport Consumer Application
 
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

# Product Passport Administrator Guide Documentation

![C-X Logo](./CXLogo.png)  

Version: v1.5 </br>
Latest Revision Mar 30, 2023

## Table of Contents

1. [Table of contents](#table-of-contents)  
2. [Introduction](#introduction)
3. [Getting Started Guide](#getting-started-guide)
4. [Deployment Configuration](#deployment-configuration)
5. [Local Keycloak Configuration](#local-keycloak-configuration)
6. [Helm Charts Configuration](#helm-charts-configuration)
7. [Consumer Backend Configuration](#consumer-backend-configuration)  
    7.1  [Backend Application Configuration](#backend-application-configuration)  
    7.2  [Spring Boot Configuration](#spring-boot-configuration)

    7.3  [Spring Boot Logging Configuration](#spring-boot-logging-configuration)
8. [Postman Collection](#postman-collection)
9. [Secrets Management](#secrets-management)
10. [EDC Provider Configuration](#edc-provider-configuration)  
    10.1 [Documentation Description](#documentation-description)    
    10.2 [Asset Configuration](#asset-configuration)   
    10.3 [Policies Configuration](#policies-configuration)    
    10.4 [Contract Definition Configuration](#contract-definition-configuration)        
    10.5 [Digital Twin Registration](#digital-twin-registration)
11. [NOTICE](#notice)

## Introduction

This guide contains all the available information for an administrator to configure, operate and deploy the Product Passport Application.  

At the moment the Application does not offers any type of administration interface within the Frontend UI, however a series of configurations can be performed in order to administrate it.

Here you will find all the guides related with the configuration of the Product Passport Application Infrastructure/Environment as well as the Frontend and Backend Systems.

## Getting Started Guide

To start the configuration of the Product Passport Application please follow this getting started guide for configuring a local stand-alone enviroment (for all components) and install guide to install the application with frontend and backend:

| Name                  | Location | Link                                                                                                                                                                                     |
|-----------------------| -------- |------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Getting Started Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docs/GETTING-STARTED.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docs/GETTING-STARTED.md) |  
| Install Guide         | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/INSTALL.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/INSTALL.md)                           |  

## Deployment Configuration

In order to deploy the application in an environment we use Helm Charts to configure the Kubernetes pods and containers.  

All the information about deploying you can find in this resource:

| Name | Location | Link |
| ---- | -------- | ---- |
| Technical Guide for Development | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/README.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/README.md) |

## Local Keycloak Configuration

All the authentication and authorization is managed by Catena-X IAM, however there is a possibility to configure a local Keycloak Instance for testing and development purposes.  

Therefore in order to configure the users and roles for the application the administration needs to import the Realm Configuration File into their local Keycloak instance hosted in a docker container.  

Additionally two test users shall be created and the correct roles shall be assigned:  

**User 1:** "company 1 user" (OEM, Dismantler)  
**User 2:** "company 2 user" (Recycler)  

Follow the [Local Keycloak Setup Guide](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docker/local/Keycloak/README.md) in order to set up the users, and their passwords correctly:

| Name | Location | Link |
| ---- | -------- | ---- |
| Local Keycloak Setup Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docker/local/Keycloak/README.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docker/local/Keycloak/README.md) |
| Realm Configuration File | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docker/local/Keycloak/realm.json](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/docker/local/Keycloak/realm.json) |

## Helm Charts Configuration

At the moment, the Product Passport Application is hosted in two environments:

|  | Application Runtime Environment | ArgoCD - Deployment Platform |
| - | -------- | ---- |
| **Development** | [https://materialpass.dev.demo.catena-x.net/](https://materialpass.dev.demo.catena-x.net/) | [https://argo.dev.demo.catena-x.net/](https://argo.dev.demo.catena-x.net/) |
| **Integration** | [https://materialpass.int.demo.catena-x.net/](https://materialpass.int.demo.catena-x.net/) | [https://argo.int.demo.catena-x.net/](https://argo.int.demo.catena-x.net/) |
| **Beta** | [https://materialpass.beta.demo.catena-x.net/](https://materialpass.beta.demo.catena-x.net/) | [https://argo.beta.demo.catena-x.net/](https://argo.beta.demo.catena-x.net/) |

All the values for the helm charts are configured for each environment and set up in the Product Passport Application source code:  

| Name | Location | Link |
| ---- | -------- | ---- |
| Helm Charts Main Directory | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts) |
| Digital Product Pass | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass) |
| EDC Consumer Helm Charts | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/helm/edc-consumer](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/helm/edc-consumer) |
| MOCK EDC Provider Helm Charts | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/helm/edc-provider](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/helm/edc-provider) |

## Consumer Backend Configuration

In order to communicate with the Catena-X Services there is a Consumer Backend that manages user sessions, provide APIs to access information, etc.  

All the information about the backend services is described in this documentation:

| Name | Location | Link |
| ---- | -------- | ---- |
| Consumer Backend Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/consumer-backend/productpass/readme.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/consumer-backend/productpass/readme.md) |
| Open API - Swagger | GitHub | [https://materialpass.int.demo.catena-x.net/swagger-ui/index.html](https://materialpass.int.demo.catena-x.net/swagger-ui/index.html) |

### Backend Application Configuration
The configurations of log levels and other variables can be set in the following file:

| Name                              | Location | Link                                                                                                                                                                                                                                         |
|-----------------------------------| -------- |----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Backend Application Configuration | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/src/main/resources/application.yml](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/src/main/resources/application.yml) |

### Spring Boot Configuration

The Consumer Backend is running over a Spring Boot server, therefore a application configuration file was created to set up mandatory parameters like the Keycloak host, and the security constrains for accessing each API and Services:

| Name | Location | Link |
| ---- | -------- | ---- |
| Spring Boot Server Configuration | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/src/main/resources/application.yml](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/src/main/resources/application.yml) |

All the application utilizes these variables to configure the utilities (tools) and other controllers/services.


### Spring Boot Logging Configuration

In order to manage the logs from the application a XML file was set, it contains the configuration from the log format and output, as well as the role back and file configuration:  

| Name | Location | Link |
| ---- | -------- | ---- |
| Spring Boot Logging Configuration | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/src/main/resources/logback-spring.xml](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/src/main/resources/logback-spring.xml) |

## Postman Collection

In order to document and test easily the API that are set up and used by the Product Passport Application, there were set a series of Postman Collections that can be found here:

| Name | Location | Link |
| ---- | -------- | ---- |
| Postman Collection Directory | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/postman](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/postman) |
| Postman Getting Started Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/postman/README.md](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/postman/README.md) |

## Secrets Management

In order to set up the secret management please follow this guide:

| Name | Location | Link |
| ---- | -------- | ---- |
| Secrets Management Documentation | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/docs/SECRETS_MANAGEMENT.md](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/docs/SECRETS_MANAGEMENT.md) |


## EDC Provider Configuration

When configuring your EDC Provider you need to take info consideration the following guidelines and formats:

> **_NOTE:_**
*Please take into consideration following our Postman Collection while setting your EDC Provider*

### Documentation Description

**All variables are written in the following notation: ***{{ VARIABLE_NAME }}*****

All the configurations are in JSON notation and follow the [EDC Configuration from Catena-X](https://github.com/eclipse-tractusx/tractusx-edc) and the [Eclipse Foundation](https://github.com/eclipse-edc/Connector).

### Asset Configuration

When configurating you EDC provider you will be able to set some assets which reference to a certain endpoint.

> **_INFO:_** *All public assets must be registered in a SubModel from a Digital Twin in the Digital Twin Registry.*

#### **Variables:**

| Name                    | Description                                                                                                                      | Example Value                                                                                                                                                                                                                                                                                                                                                                                                              |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| AssetId                 | Combination of Digital Twin and Sub Model UUIDs                                                                                  | **Example value for asset**: 32aa72de-297a-4405-9148-13e12744028a-699f1245-f57e-4d6b-acdb-ab763665554a <br/>**Example value for registry**: digital-twin-registry                                                                                                                                                                                                                                                          |
| AssetType               | The type of the Asset                                                                                                            | **Example value for asset**: Asset <br/>**Example value for registry**: data.core.digitalTwinRegistry                                                                                                                                                                                                                                                                                                                          |
| Description             | Simple description of the asset                                                                                                  | Battery Passport Test Data                                                                                                                                                                                                                                                                                                                                                                                                 |
| DataProviderEndpointUrl | URL to the endpoint which stores and serves the data, basically a Database that retrieves plain text/json data for a certain API | **Example value for asset**: [https://materialpass.int.demo.catena-x.net/provider_backend/data/{{DigitalTwinId}}-{{DigitalTwinSubmodelId}}](https://materialpass.int.demo.catena-x.net/provider_backend/data/{{DigitalTwinId}}-{{DigitalTwinSubmodelId}}) <br/> **Example value for registry**: [https://materialpass.int.demo.catena-x.net/semantics/registry](https://materialpass.int.demo.catena-x.net/semantics/registry) |
| DigitalTwinId           | Id from the Digital Twin	                                                                                                        | 32aa72de-297a-4405-9148-13e12744028a                                                                                                                                                                                                                                                                                                                                                                                       |
| DigitalTwinSubmodelId   | Sub Model Id registered in the Digital Twin Registry                                                                             | 699f1245-f57e-4d6b-acdb-ab763665554a                                                                                                                                                                                                                                                                                                                                                                                       |


#### **Format and Fields:**

```
{
    "@context": {},
    "asset": {
        "@type": "{{AssetType}}",
        "@id": "{{AssetId}}", 
        "properties": {
            "description": "{{Description}}"
            "contenttype": "application/json"
        }
    },
    "dataAddress": {
        "@type": "DataAddress",
        "type": "HttpData",
        "proxyPath": "true",
        "proxyBody": "true",
        "proxyMethod": "true",
        "proxyQueryParams": "true",
        "baseUrl": "{{DataProviderEndpointUrl}}"
    }
}
```
When configurating your EDC provider you will be able to set some assets which reference to a certain endpoint.


### Policies Configuration
Policies are important for configuration the **access, prohibitions, obligations and permissions to certain assets.**

A policy can have more and less configurations, depending of the restrictions you want to give to each asset.

Here we specify a simple policy with just the USAGE permission, so we are able to retrieve the whole asset without obligations and prohibitions.

#### Usage Policies

| Policy Name | Description |
| ---- | -------- |
| Usage Permission Policy | In order to use/access the assets from the EDC Provider the Usage Policy is required |

> **_NOTE:_**
*At the moment only Usage Permission Policies are assigned to assets, however restriction policies could be also configured if it is required for a specific use case.*

#### **Variables:**

| Name | Description | Example Value |
| ---- | -------- | ---- |
| PolicyId | UUID that identifies the policy in the EDC Connector | ad8d2c57-cf32-409c-96a8-be59675b6ae5 |
| PermissionType | DID Permission Type | PolicyDefinitionRequestDto |
| PermissionActionType | Defines the action allowed when the permission is assigned to an asset. In case of the usage policy the value "USE" is necessary | "USE" |
| BPN                  | Consumer's Business Partner Number                                                                                               | BPNL000000000000 |

#### **Format and Fields:**

```
{
    "@context": {
        "odrl": "http://www.w3.org/ns/odrl/2/leftOperand"
    },
    "@type": "{{PermissionType}}",
    "@id": "{{PolicyId}}",
    "policy": {
		"@type": "Policy",
		"odrl:permission" : [{
          "odrl:action": "{{PermissionActionType}}",
          "odrl:constraint": {
            "odrl:constraint": {
              "@type": "LogicalConstradev",
              "odrl:or": [
                {
                  "@type": "Contraint",
                  "odrl:leftOperand": "BusinessPartnerNumber",
                  "odrl:operator": "EQ",
                  "odrl:rightOperand": "{{BPN}}"
                }
              ]
            }
          }
        }]
    }
}
```


### Contract Definition Configuration

Contract definitions allow us to expose the assets and link them to a contract policy and a access policy.

> **_INFO:_** *Remember that all **policies and assets** you bind to a contract **must be defined in the same EDC Connector** and linked though their ID in the configuration from the contract.*

#### **Variables:**

| Name | Description | Example Value                                                                                                                                 |
| ---- | -------- |-----------------------------------------------------------------------------------------------------------------------------------------------|
| ContractDefinitionId | UUID that identifies the policy in the EDC Connector | 76b50bfc-ec19-457f-9911-a283f0d6d0df                                                                                                          |
| AssetId | Combination of Digital Twin and Sub Model UUIDs | **Example value for asset**: 32aa72de-297a-4405-9148-13e12744028a-699f1245-f57e-4d6b-acdb-ab763665554a <br/> **Example value for registry**: digital-twin-registry |
| AccessPolicyId | Policy that allows/restricts/enforces asset access constrains | ad8d2c57-cf32-409c-96a8-be59675b6ae5                                                                                                          |
| ContractPolicyId | Policy that allows/restricts/enforces contract constrains | ad8d2c57-cf32-409c-96a8-be59675b6ae5                                                                                                          |


#### **Format and Fields:**

> **_INFO:_** *For testing proposes and in order to ease the access to your assets we are going to define the **same policy as accessPolicy and as contractPolicy**. However, you are recommended to configure two separated policies and specify them adapting each one of them to your specific needs.*

```
{
    "@context": {},
    "@id": "{{ContractDefinitionId}}",
    "@type": "ContractDefinition",
    "accessPolicyId": "{{AccessPolicyId}}",
    "contractPolicyId": "{{ContractPolicyId}}",
    "assetsSelector" : {
        "@type" : "CriterionDto",
        "operandLeft": "https://w3id.org/edc/v0.0.1/ns/id",
        "operator": "=",
        "operandRight": "{{AssetId}}"
    }
}
```

### Digital Twin Registration

Once you finish the configuration, to make the endpoint public configure in the following way your Digital Twin:

 > **_INFO:_** *You need to be able to request tokens for the **Catena-X Central IAM** in order to **configure Digital Twins** in the Registry.*



#### **Variables:**

| Name | Description | Example Value |
| ---- | -------- | ---- |
| DigitalTwinId | Manually generated DID that contains a UUID | 32aa72de-297a-4405-9148-13e12744028a |
| DigitalTwinSubmodelId | Sub Model Id registered in the Digital Twin Registry | 699f1245-f57e-4d6b-acdb-ab763665554a |
| PartInstanceId | Battery passport attribute - part instance Id | X123456789012X12345678901234566 |
| EDCProviderUrl | URL to the endpoint which contains the EDC Provider | [https://materialpass.int.demo.catena-x.net](https://materialpass.int.demo.catena-x.net) |
| BPN | OPTIONAL: The endpoint address can include a BPN number, which shall lead to the EDC Provider, and return the contracts when called from an EDC Consumer | BPNL000000000000 |
| SubmodelIdShort | EXACT STRING REQUIRED: The submodel id of the battery passports needs to be exactly the string: "batteryPass" | **batteryPass** |
| BammModelVersionId | The semantic version of the asset passport model, currently the latest version v3.0.1 is used | urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass |

> **_INFO:_** *It is important that the "SubmodelIdShort" is set in the correct format and that the EDCProviderUrl points to an valid EDC Provider, that providers valid contracts configured in the structure defined here.*


#### **Format and Fields:**

```
{
    "description": [
        {
            "language": "en",
            "text": "Battery Passport shell descriptor"
        }
    ],
    "idShort": "Battery_{{PartInstanceId}}",
    "id": "{{DigitalTwinId}}",
    "specificAssetIds": [
        {
            "name": "partInstanceId",
            "value": "{{PartInstanceId}}"
        }
    ],
    "submodelDescriptors": [
        {
            "endpoints": [
                {
                    "interface": "SUBMODEL-3.0",
                    "protocolInformation": {
                        "href": "{{EDCProviderUrl}}/{{BPN}}/{{DigitalTwinId}}-{{DigitalTwinSubmodelId}}/submodel",
                        "endpointProtocol": "HTTP",
                        "endpointProtocolVersion": [ 
                            "1.1" 
                        ],
                        "subprotocol": "DSP",
                        "subprotocolBody": "id={{DigitalTwinId}}-{{DigitalTwinSubmodelId}}",dspEndpoint={{EDCProviderUrl}}/{{BPN}}",
                        "subprotocolBodyEncoding": "plain"
                    }
                }
            ],
            "idShort": "{{SubmodelIdShort}}",
            "id": "{{DigitalTwinSubmodelId}}",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "{{BammModelVersionId}}"
                    }
                ]
            },
            "description": [
                {
                    "language": "en",
                    "text": "Battery Passport Submodel"
                }
            ]
        }
    ]
}
```



> **_NOTE:_** 
*The BPN number is not required for the configuration of the endpoint, just **make sure that the host is pointing to the EDC Provider**.*


## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass

