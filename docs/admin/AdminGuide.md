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

# Product Passport Administrator Guide Documentation

![C-X Logo](./media/CXLogo.png)  

Version: v2.1 </br>
Latest Revision 04 Jan, 2024

## Table of Contents

- [Product Passport Administrator Guide Documentation](#product-passport-administrator-guide-documentation)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Getting Started Guide](#getting-started-guide)
  - [Deployment Configuration](#deployment-configuration)
  - [Local IAM Configuration](#local-iam-configuration)
  - [Consumer Backend Configuration](#consumer-backend-configuration)
    - [Backend Application Configuration](#backend-application-configuration)
    - [Spring Boot Configuration](#spring-boot-configuration)
    - [Spring Boot Logging Configuration](#spring-boot-logging-configuration)
  - [Postman Collection](#postman-collection)
  - [Secrets Management](#secrets-management)
  - [EDC Provider Configuration](#edc-provider-configuration)
    - [Documentation Description](#documentation-description)
    - [Asset Configuration](#asset-configuration)
      - [Variables:](#variables)
      - [Format and Fields:](#format-and-fields)
    - [Policies Configuration](#policies-configuration)
      - [Usage Policies](#usage-policies)
      - [Variables:](#variables-1)
      - [Format and Fields:](#format-and-fields-1)
    - [Contract Definition Configuration](#contract-definition-configuration)
      - [Variables:](#variables-2)
      - [Format and Fields:](#format-and-fields-2)
    - [Digital Twin Registration](#digital-twin-registration)
      - [Variables:](#variables-3)
      - [Format and Fields:](#format-and-fields-3)
    - [Digital Twin Registry Configuration](#digital-twin-registry-configuration)
      - [Variables:](#variables-4)
      - [Format and Fields:](#format-and-fields-4)
  - [Item Relationship Service Integration](#item-relationship-service-integration)
  - [NOTICE](#notice)

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

## Local IAM Configuration

All the authentication and authorization is managed by Catena-X IAM, however there is a possibility to configure a local Keycloak Instance for testing and development purposes.  

Therefore in order to configure the users and roles for the application the administration needs to import the Realm Configuration File into their local Keycloak instance hosted in a docker container.  

Additionally two test users shall be created and the correct roles shall be assigned:  

**User 1:** "company 1 user" (OEM, Dismantler)  
**User 2:** "company 2 user" (Recycler)  

Follow the [Local IAM Setup Guide](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/local/iam/README.md) in order to set up the users, and their passwords correctly:

| Name | Location | Link |
| ---- | -------- | ---- |
| Local IAM Setup Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/local/iam/README.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/local/iam/README.md) |
| Realm Configuration File | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/local/iam/realm.json](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/local/iam/realm.json) |


All the values for the helm charts are configured for each environment and set up in the Product Passport Application source code:  

| Name | Location | Link |
| ---- | -------- | ---- |
| Helm Charts Main Directory | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts) |
| Digital Product Pass | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass) |
| EDC Consumer Helm Charts | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/infrastructure/data-consumer/edc-consumer](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/infrastructure/data-consumer/edc-consumer) |
| MOCK EDC Provider Helm Charts | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/infrastructure/data-provider/edc-provider](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/infrastructure/data-provider/edc-provider) |
| MOCK Provider Backend Helm Charts | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/infrastructure/data-provider/data-service](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/deployment/infrastructure/data-provider/data-service) |

## Consumer Backend Configuration

In order to communicate with the Catena-X Services there is a Consumer Backend that manages user sessions, provide APIs to access information, etc.  

All the information about the backend services is described in this documentation:

| Name | Location | Link |
| ---- | -------- | ---- |
| Consumer Backend Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-backend/digitalproductpass/readme.md](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-backend/digitalproductpass/readme.md) |
| Open API - Swagger | GitHub | [https://dpp.int.demo.catena-x.net/swagger-ui/index.html](https://dpp.int.demo.catena-x.net/swagger-ui/index.html) |

### Backend Application Configuration
The configurations of log levels and other variables can be set in the following file:

| Name                              | Location | Link                                                                                                                                                                                                                                         |
|-----------------------------------| -------- |----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Backend Application Configuration | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/src/main/resources/application.yml](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/src/main/resources/application.yml) |

### Spring Boot Configuration

The Consumer Backend is running over a Spring Boot server, therefore a application configuration file was created to set up mandatory parameters like the Keycloak host, and the security constrains for accessing each API and Services:

| Name | Location | Link |
| ---- | -------- | ---- |
| Spring Boot Server Configuration | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/src/main/resources/application.yml](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/src/main/resources/application.yml) |

All the application utilizes these variables to configure the utilities (tools) and other controllers/services.


### Spring Boot Logging Configuration

In order to manage the logs from the application a XML file was set, it contains the configuration from the log format and output, as well as the role back and file configuration:  

| Name | Location | Link |
| ---- | -------- | ---- |
| Spring Boot Logging Configuration | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/src/main/resources/logback-spring.xml](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/src/main/resources/logback-spring.xml) |

## Postman Collection

In order to document and test easily the API that are set up and used by the Product Passport Application, there were set a series of Postman Collections that can be found here:

| Name | Location | Link |
| ---- | -------- | ---- |
| Postman Collection Directory | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/postman](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/postman) |
| Postman Getting Started Guide | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/deployment/local/postman/README.md](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/deployment/local/postman/README.md) |

## Secrets Management

In order to set up the secret management please follow this guide:

| Name | Location | Link |
| ---- | -------- | ---- |
| Secrets Management Documentation | GitHub | [https://github.com/eclipse-tractusx/digital-product-pass/blob/main/docs/security/secrets-management/SecretsManagement.md](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/docs/security/secrets-management/SecretsManagement.md) |


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

#### Variables:

| Name                    | Description                                                                                                                      | Example Value                                                                                                                                                                                                                                                                                                                                                                                                              |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| AssetId                 | A unique identifier (UUID) of the asset                                                                                  |urn:uuid:0ec8cf2b-f58e-3f13-b5ef-e7dd01d15b19                                                                                                                                                                                                                                                          |
| AssetType               | The type of the Asset                                                                                                            | Asset                                                                                                                                                                                                                                                                                                                          |
| Description             | Simple description of the asset                                                                                                  | Battery Passport Test asset                                                                                                                                                                                                                                                                                                                                                                                                 |
| submodel.server.endpoint | URL to the endpoint which stores and serves the data, basically a Database that retrieves plain text/json data for a certain API | [https://materialpass.int.demo.catena-x.net/provider_backend](https://materialpass.int.demo.catena-x.net/provider_backend) |


#### Format and Fields:

```json
{
    "@context": {},
    "asset": {
        "@type": "{{AssetType}}",
        "@id": "{{AssetId}}", 
        "properties": {
            "description": "{{Description}}",
            "contenttype": "application/json"
        }
    },
    "dataAddress": {
        "@type": "DataAddress",
        "type": "HttpData",
        "proxyPath": "true",
        "baseUrl": "{{submodel.server.endpoint}}"
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

#### Variables:

| Name | Description | Example Value |
| ---- | -------- | ---- |
| PolicyId | UUID that identifies the policy in the EDC Connector | ad8d2c57-cf32-409c-96a8-be59675b6ae5 |
| PermissionType | DID Permission Type | PolicyDefinitionRequestDto |
| PermissionActionType | Defines the action allowed when the permission is assigned to an asset. In case of the usage policy the value "USE" is necessary | "USE" |
| BPN                  | Consumer's Business Partner Number                                                                                               | BPNL000000000000 |

#### Format and Fields:

To allow partners to access information use this policy with the BPN number included:

```json
{
    "@context": {
        "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
        "odrl": "http://www.w3.org/ns/odrl/2/"
    },
    "@type": "{{PermissionType}}",
    "@id": "{{PolicyId}}",
    "policy": {
		"@type": "Policy",
		"odrl:permission" : [{
          "odrl:action": "{{PermissionActionType}}",
          "odrl:constraint": {
            "odrl:constraint": {
              "@type": "LogicalConstraint",
              "odrl:or": [
                {
                  "@type": "Contraint",
                  "odrl:leftOperand": "BusinessPartnerNumber",
                  "odrl:operator": {
                    "@id": "odrl:eq"
                 },
                  "odrl:rightOperand": "{{BPN}}"
                }
              ]
            }
          }
        }]
    }
}
```

The minimum set of **membership** and the **circular economy frameworkagreement** **MUST** to be added to the asset:

```json
{
  "@context": {
    "@vocab": "https://w3id.org/edc/v0.0.1/ns/",
    "odrl": "http://www.w3.org/ns/odrl/2/"
  },
  "@type": "{{PermissionType}}",
  "@id": "{{PolicyId}}",
  "policy": {
    "@type": "Policy",
    "odrl:permission" : [
      {
        "odrl:action":"{{PermissionActionType}}",
        "odrl:constraint": {
          "@type": "LogicalConstraint",
          "odrl:and": [
            {
              "@type": "Constraint",
              "odrl:leftOperand": "Membership",
              "odrl:operator": {
                "@id": "odrl:eq"
              },
              "odrl:rightOperand": "active"
            },
            {
              "@type": "Constraint",
              "odrl:leftOperand": "FrameworkAgreement.sustainability",
              "odrl:operator": {
                "@id": "odrl:eq"
              },
              "odrl:rightOperand": "active"
            }
          ]
        }
      }
    ]
  }
}
```

> *NOTE*: If your SSI credentials do not include both membership and framework agreement, you can use the `odrl:or` keyword instead of the default `odrl:and` keyword. Make sure you credentials are correctly configured in order to access the resources in the EDC from your providers.


### Contract Definition Configuration

Contract definitions allow us to expose the assets and link them to a contract policy and a access policy.

> **_INFO:_** *Remember that all **policies and assets** you bind to a contract **must be defined in the same EDC Connector** and linked though their ID in the configuration from the contract.*

#### Variables:

| Name | Description | Example Value                                                                                                                                 |
| ---- | -------- |-----------------------------------------------------------------------------------------------------------------------------------------------|
| ContractDefinitionId | UUID that identifies the policy in the EDC Connector | 76b50bfc-ec19-457f-9911-a283f0d6d0df                                                                                                          |
| AssetId | UUID of the asset | **Example value for asset**: urn:uuid:0ec8cf2b-f58e-3f13-b5ef-e7dd01d15b19 <br/> **Example value for registry**: registry-asset |
| AccessPolicyId | Policy that allows/restricts/enforces asset access constrains | ad8d2c57-cf32-409c-96a8-be59675b6ae5                                                                                                          |
| ContractPolicyId | Policy that allows/restricts/enforces contract constrains | ad8d2c57-cf32-409c-96a8-be59675b6ae5                                                                                                          |


#### Format and Fields:

> **_INFO:_** *For testing proposes and in order to ease the access to your assets we are going to define the **same policy as accessPolicy and as contractPolicy**. However, you are recommended to configure two separated policies and specify them adapting each one of them to your specific needs.*

```json
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



#### Variables:

| Name | Description | Example Value |
| ---- | -------- | ---- |
| DigitalTwinId | Manually generated DID that contains a UUID | urn:uuid:de98db6e-8e05-5d8e-8ae8-9f702cf5c396 |
| DigitalTwinSubmodelId | Sub Model Id registered in the Digital Twin Registry | urn:uuid:555c5513-5e52-2d7d-0904-fe90829252de|
| PartInstanceId | Battery passport attribute - part instance Id | X123456789012X12345678901234566 |
| ManufacturerPartId | Battery passport attribute - manufacturer part Id | XYZ78901 |
| edc.data.plane | The edc data plane endpoint of the provider | [https://materialpass.int.demo.catena-x.net/BPNL000000000000](https://materialpass.int.demo.catena-x.net/BPNL000000000000) |
| edc.control.plane | The edc control plane endpoint of the provider | [https://materialpass.int.demo.catena-x.net/BPNL000000000000](https://materialpass.int.demo.catena-x.net/BPNL000000000000) |
| Path | The edc data plane public endpoint of the provider  | [/api/public/data](/api/public/data) |
| AssetId | The UUID of the edc data asset | urn:uuid:0ec8cf2b-f58e-3f13-b5ef-e7dd01d15b19 |
| BPN | OPTIONAL: The endpoint address can include a BPN number, which shall lead to the EDC Provider, and return the contracts when called from an EDC Consumer | BPNL000000000000 |
| SubmodelIdShort | EXACT STRING REQUIRED: The submodel id of the battery passports needs to be exactly the string: "batteryPass" | **batteryPass** |
| BammModelVersionId | The semantic version from the submodel aspect, consult the CX-0096 for more options of the semantic version | urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass |

> **_INFO:_** *It is important that the "SubmodelIdShort" is set in the correct format and that the edc.data.plane points to the valid EDC Provider, that providers valid contracts configured in the structure defined here.*

#### Format and Fields:

```json
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
            "name": "manufacturerPartId",
            "value": "{{ManufacturerPartId}}",
            "externalSubjectId": {
            "type": "ExternalReference",
            "keys": [
                {
                "type": "GlobalReference",
                "value": "{{BPN}}"
                },
                {
                "type": "GlobalReference",
                "value": "PUBLIC_READABLE"
                }
              ]
            }
        },
        {
            "name": "partInstanceId",
            "value": "{{PartInstanceId}}",
            "externalSubjectId": {
            "type": "ExternalReference",
            "keys": [
                {
                "type": "GlobalReference",
                "value": "{{BPN}}"
                }
              ]
            }
        },
        {
            "key" : "assetLifecyclePhase",
            "value": "AsBuild"
        }
    ],
   "submodelDescriptors":[
      {
        "endpoints": [
          {
            "interface": "SUBMODEL-3.0",
            "protocolInformation": {
              "href": "https://{{edc.data.plane}}/{{Path}}/{{DigitalTwinSubmodelId}}",
              "endpointProtocol": "HTTP",
              "endpointProtocolVersion": [
                "1.1"
              ],
              "subprotocol": "DSP",
              "subprotocolBody": "{{AssetId}};dspEndpoint=https://{{edc.control.plane}}",
              "subprotocolBodyEncoding": "plain",
              "securityAttributes": [
                {
                  "type": "NONE",
                  "key": "NONE",
                  "value": "NONE"
                }
              ]
            }
          }
        ],
        "idShort": "batteryPass",
        "id": "{{DigitalTwinSubmodelId}}",
        "semanticId": {
          "type": "ExternalReference",
          "keys": [
            {
              "type": "Submodel",
              "value": "urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass"
            }
          ]
        },
        "description": [
          {
            "language": "en",
            "text": "Battery Passport Submodel"
          }
        ],
        {
          "endpoints": [
            {
              "interface": "SUBMODEL-3.0",
              "protocolInformation": {
                "href": "https://{{edc.data.plane}}/{{path}}/urn:uuid:777a3f0a-6d29-4fcd-81ea-1c27c1b870cc",
                "endpointProtocol": "HTTP",
                "endpointProtocolVersion": [
                  "1.1"
                ],
                "subprotocol": "DSP",
                "subprotocolBody": "id={{AssetId}};dspEndpoint=https://{{edc.control.plane}}",
                "subprotocolBodyEncoding": "plain",
                "securityAttributes": [
                  {
                    "type": "NONE",
                    "key": "NONE",
                    "value": "NONE"
                  }
                ]
              }
            }
          ],
          "idShort": "digitalProductPass",
          "id": "urn:uuid:777a3f0a-6d29-4fcd-81ea-1c27c1b870cc",
          "semanticId": {
            "type": "ExternalReference",
            "keys": [
              {
                "type": "Submodel",
                "value": "urn:samm:io.catenax.generic.digital_product_passport:3.0.0#DigitalProductPassport"
              }
            ]
          },
          "description": [
            {
              "language": "en",
              "text": "Digital Product Passport Submodel"
            }
          ]
        }
      }
    ]
    }
```
> **_NOTE:_** 
*The BPN number is not required for the configuration of the endpoint, just **make sure that the host is pointing to the EDC Provider**.*


### Digital Twin Registry Asset Configuration

When configuring the digital twin registry behind the EDC Provider you should follow this EDC Registration guidelines:

#### Variables:

| Name         | Description                                 | Example Value                                                 |
|--------------|---------------------------------------------|---------------------------------------------------------------|
| registryUrl  | The base url from the digital twin registry | [https://<registry-hostname>/semantics/registry/api/v3.0](https://<registry-hostname>/semantics/registry/api/v3.0) |
| registryName | The name from the asset for the registry    | registry-asset                                         |

> **IMPORTANT**: Is mandatory by the *Catena-X Standard CX-0002* from the Digital Twin Registry, the asset.properties.type should be `data.core.digitalTwinRegistry` in order to the digital product pass to find the asset in the EDC.

#### Format and Fields:

```json
{
    "@context": {},
    "asset": {
        "@type": "Asset",
        "@id": "{{registryName}}", 
        "properties": {
            "type": "data.core.digitalTwinRegistry",
            "description": "Digital Twin Registry",
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
        "baseUrl": "{{registryUrl}}"
    }
}
```


## Item Relationship Service Integration

For deploying and integrating the IRS (Item Relationship Service)[https://github.com/eclipse-tractusx/item-relationship-service] with the digital product pass application first deploy the [reference helm chart values](../../deployment/infrastructure/irs) helm charts.

For creating relationships between the digital twins register "singleLevelBomAsBuilt" and "singleLevelBomAsUsage" aspects which can be found here: [SingleLevelBomAsBuilt](https://github.com/eclipse-tractusx/sldt-semantic-models/tree/main/io.catenax.single_level_bom_as_built) and [SingleLevelUsageAsBuilt](https://github.com/eclipse-tractusx/sldt-semantic-models/tree/main/io.catenax.single_level_usage_as_built)



> **IMPORTANT**!: The proxy configuration needs to be enabled exactly like it is configured in the dataAdress property above.

The rest of the assets can be configured in the same way as the normal assets.

## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023, 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass

