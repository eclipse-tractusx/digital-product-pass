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

# ![C-X Logo](../media/catenaxLogo.svg) Catena-X Data Retrieval Guide

| Version | v1.0 |
| ------- | ---- | 

Created: *25 August 2023* 

## Table of Contents
- [ Catena-X Data Retrieval Guide](#-catena-x-data-retrieval-guide)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Starting Point](#starting-point)
    - [Available Central Services](#available-central-services)
  - [Problems Generated](#problems-generated)
  - [Data Retrieval Flow](#data-retrieval-flow)
    - [1. Discovery Phase](#1-discovery-phase)
    - [2. Digital Twin Registry Search Phase](#2-digital-twin-registry-search-phase)
    - [3. Digital Twin Search Phase](#3-digital-twin-search-phase)
    - [4. Data Negotiation and Transfer Phase](#4-data-negotiation-and-transfer-phase)
  - [1. Discovery Phase + 2. Digital Twin Registry Search Phase](#1-discovery-phase--2-digital-twin-registry-search-phase)
    - [Prerequisites](#prerequisites)
    - [Sequence Diagram](#sequence-diagram)
    - [Flow Diagram](#flow-diagram)
  - [3. Digital Twin Search Phase](#3-digital-twin-search-phase-1)
    - [Prerequisites](#prerequisites-1)
    - [Sequence Diagram](#sequence-diagram-1)
    - [Flow Diagram](#flow-diagram-1)
  - [4. Data Negotiation and Transfer Phase](#4-data-negotiation-and-transfer-phase-1)
    - [Prerequisites](#prerequisites-2)
    - [Sequence Diagram](#sequence-diagram-2)
    - [Flow Diagram](#flow-diagram-2)
      - [Negotiation and Transfer](#negotiation-and-transfer)
      - [Data Retrieval](#data-retrieval)
  - [Attachments](#attachments)
    - [AAS 3.0 Digital Twin Example](#aas-30-digital-twin-example)
    - [Contract Example](#contract-example)
  - [Authors](#authors)
  - [NOTICE](#notice)

## Introduction
The Catena-X Network Data Retrieval process can be really complex and challenging to understand. The `Digital Product Pass` application as consumer application is designed to retrieve information from the Catena-X Network in its native way. We as team proposed a solution for retrieving information in this decentralized network in a very efficient way. This solution is implemented in the [`Digital Product Pass Backend`](../../consumer-dpp-backend/digitalproductpass/readme.md) and can retrieve information in approximated `8-20s` using the algorithm and procedures documented in the [Arc42](../architecture/Arc42.md) documentation.

Therefore this guide is here to provide information about how to retrieve Data from the Catena-X Network like the `Digital Product Pass` application. Here we will describe the problem that many consumers application are facing then retrieving data in Catena-X and how we as the DPP Team propose our vision.

## Starting Point

Before we start with the diagrams is necessary to give some context and remark how things are happening and which services are available.

After the Digital Twin Registry became an decentral component provided by several Catena-X Members as Providers, there was the need of providing central services to enable searching for this digital twin registries.

Therefore some central services were created, allowing the authorized Catena-X Applications to find the EDC endpoints after calling the following services:

### Available Central Services


| Service Name | Description | Reference Implementation |
|------------- | ----------- | ------------------------ |
| Discovery Service | Responsible to give the search endpoints for a type of id | [eclipse-tractusx/sldt-discovery-finder](https://github.com/eclipse-tractusx/sldt-discovery-finder) |
| BPN Discovery	| Responsible for indicating the BPNs for the IDs registered by the providers | [eclipse-tractusx/sldt-bpn-discovery](https://github.com/eclipse-tractusx/sldt-bpn-discovery) |
| EDC Discovery	| Responsible for giving the EDC endpoints of one or more BPNs | [eclipse-tractusx/portal-backend](https://github.com/eclipse-tractusx/portal-backend) - [Code Implementation](https://github.com/eclipse-tractusx/portal-backend/blob/aca855c857aed309cbca03f4f694283629197110/src/administration/Administration.Service/Controllers/ConnectorsController.cs#L178C1-L190C63) |


The main idea was that they will be called in a sequential way when needed to find the EDC endpoints.

Here we can observe an example of how a normal exchange would work in a sequential way:

![Simplified Discovery Services Exchange](./media/discoveryServices.drawio.svg)

## Problems Generated

After the fist implementations have been released, it was analyzed that some problems were generated.

The problems observed from the application side were: 

- dDTR search guidelines are missing:
    - After we receive the list of several EDCs, there is no guideline on the most optimized way of searching which EDC has the digital twin registry asset.
- Performance problem:
   - There is a performance problem, because if you want to find the digital twin registries for each request + search in each digital twin registry for the assets (which involves contract negotiation with EDCs) takes time.
- Escalation and Maintenance problem:
   - If we scale it in to the Catena-X Overarching project, we will observe that every product implemented their own solution. From a maintenance perspective the is big problem.
   - Imagine a new update in the EDC is made or in any other central discovery service, this would mean that every single application would need to change their architecture and code because they are responsible of maintaining it.

Therefore there needs to be a easier way of querying this services and searching in different dDTRs around the Catena-X Network.

## Data Retrieval Flow

Here is a diagram of the data retrial flow necessary to retrieve any data from the Catena-X Network without any optimizations:

![Data Retrieval Flow](./media/dataRetrievalFlow.drawio.svg)


### 1. Discovery Phase

At the beginning we start calling the `Discovery Service` which is responsible for giving us the urls from the `BPN Discovery` and the `EDC Discovery` this two service give us first a `BPN or Business Partner Number` for a specific `id` and the `EDC Discovery` will give you a list of EDC registered by one company's `BPN`.

### 2. Digital Twin Registry Search Phase

Once we have a list of `EDCs` we need to find which of this EDCs contain the `Digital Twin Registry` component. We can filter which `EDCs` contain the `Digital Twin Registry` by simply calling for the catalog with the `type` condition of the contract that must have the `data.core.digitalTwinRegistry` standardized type. 

Once we have the list of DTRs we need to negotiate each contract retrieve in the catalog so that we can have the `Contract Agreement Id` which is given by the EDC once the contact is signed and agreed. This id will be used later to request the transfer for the `EDR` token for accessing the `Digital Twin Registry` through the `EDC Provider Data Plane Proxy`. 

### 3. Digital Twin Search Phase

We need to search for the `Digital Twins` inside of the `Digital Twin Registries`, and once we found it we can start the negotiation for the `submodelDescriptor` we are searching for that can be for example a: `Digital Product Pass`, `Battery Pass`, `Single Level BOM as Built` or a `Transmission Pass`.

### 4. Data Negotiation and Transfer Phase

Once we have the submodel we are going to call the [`subprotocolBody`](#L233) url of the `endpoint interface` with name `SUBMODEL-3.0`. This will provide for us the asset id to negotiate with the EDC Provider. Once this asset is negotiated we will request for the `transfer` and `EDR` token will be sent to the backend by the EDC Provider, allowing us to query the dataplane url contained in the `href` field of the endpoint interface. And in this way we will retrieve the data using the `EDC Provider Data Plane Proxy`.


## 1. Discovery Phase + 2. Digital Twin Registry Search Phase

After the discovery phase, the search for digital twin registries is one of the core components to be done when retrieving data in Catena-X.
Once the negotiation for the digital twin registries assets are done we would be able to retrieve a catalog for the user to search the serialized Id.

### Prerequisites

The following information is required to enable the decentralized search for digital twin registries:

| Name | Example | Description |
| ---- | ------- | ----------- |
| Search Id Type | *manufacturerPartId* | The search id type is required first of all to know in which `BPN Discovery` services to search. It will be introduced in the `Discovery Service` and we will obtain a list of `BPN Discovery Endpoints`. After this same id will be introduce as the *`type`* attribute in each `BPN Discovery`. |
| Search Id Value | *HV-SPORT-123* | The search id value is required for searching in the `BPN Discovery` services. One example could be the `product type id` of a company, which is owned by an unique `BPN` reducing the complexity of the search.

### Sequence Diagram 
![Digital Twin Registries Search](./media/dtrSearchSequence.drawio.svg)

As we can visualize in the following example we will request the following services and retrieve the contract agreement from the Digital Twin Registries in parallel.

### Flow Diagram
The flow diagram below allows us to see in more detail the steps required for retrieving the contract agreement id for each of the digital twin registries assets.

![Flow Digital Twin Registry Search](./media/dtrSearchFlow.drawio.svg)


## 3. Digital Twin Search Phase

The digital twin searching phase involves searching in every digital twin registry for the desired digital twin asset. In this digital twin we will find the necessary information for requesting the contract information for the "digital twin submodels".

### Prerequisites

The following information is required for enabling the digital twin search, in order to start the data transfer phase:

| Name | Example | Description |
| ---- | ------- | ----------- |
| Specific Asset Id Type | *partInstanceId* | The specific asset id type is used to search in the `digital twin registry` for an specific digital twin. It is basically the `name` of  "specificAssetId" object located at the [`digital twin`](#aas-30-digital-twin-example) `specificAssetIds` property. The `*partInstanceId*` is used as an example most of the time, since the digital twin registry implemented a hotfix that allows companies say who can access to their `partInstanceId` fields. Now allowing the *"PUBLIC_READABLE"* property.   |
| Specific Asset Id Type | *BAT-XYZ789* | The specific asset id value is added in the `digital twin lookup` when calling the `EDC Provider Proxy`. It basically points to the value of the *`Specific Asset Id Type`* property.

### Sequence Diagram 
![Data Search API](./media/dataSearchApi.drawio.svg)

### Flow Diagram 
![Data Search Flow API](./media/searchApiFlow.drawio.svg)


## 4. Data Negotiation and Transfer Phase


### Prerequisites

The following information is required for enabling the digital twin search, in order to start the data transfer phase:

| Name | Example | Description |
| ---- | ------- | ----------- |
| Contract with Policy | [Contract Example](#contract-example) | To start the contract negotiation we need to agree on a policy for the a specific contract. This needs to be selected by the one that is requesting the data.


### Sequence Diagram
![Data Retrieval](./media/dataRetrievalSequence.drawio.svg)

### Flow Diagram

#### Negotiation and Transfer
![Data Retrieval Flow 1](./media/dataRetrievalFlow1.drawio.svg)

#### Data Retrieval

![Data Retrieval Flow 1](./media/dataRetrievalFlow2.drawio.svg)


## Attachments

### AAS 3.0 Digital Twin Example

```json
{
  "description": [
      {
          "language": "en",
          "text": "Battery Digital Twin"
      }
  ],
  "displayName": [],
  "globalAssetId": "urn:uuid:efcb5f8d-f31c-4b1f-b090-9c878054554d",
  "idShort": "Battery_BAT-XYZ789",
  "id": "urn:uuid:3d050cd8-cdc7-4d65-9f37-70a65d5f53f5",
  "specificAssetIds": [
      {
          "name": "manufacturerPartId",
          "value": "XYZ78901",
          "externalSubjectId": {
              "type": "ExternalReference",
              "keys": [
                  {
                      "type": "GlobalReference",
                      "value": "BPNL000000000000"
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
          "value": "BAT-XYZ789",
          "externalSubjectId": {
              "type": "ExternalReference",
              "keys": [
                  {
                      "type": "GlobalReference",
                      "value": "BPNL000000000000"
                  }
              ]
          }
      }
  ],
  "submodelDescriptors": [
      {
          "endpoints": [
              {
                  "interface": "SUBMODEL-3.0",
                  "protocolInformation": {
                      "href": "https://materialpass.int.demo.catena-x.net/BPNL000000000000/api/public/data/urn:uuid:1ea64f49-8b2b-4cd2-818e-cf9d452c6fea",
                      "endpointProtocol": "HTTP",
                      "endpointProtocolVersion": [
                          "1.1"
                      ],
                      "subprotocol": "DSP",
                      "subprotocolBody": "id=urn:uuid:3e4a5957-f226-478a-ab18-79ced49d6195;dspEndpoint=https://materialpass.int.demo.catena-x.net/BPNL000000000000",
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
          "idShort": "SerialPart",
          "id": "urn:uuid:1ea64f49-8b2b-4cd2-818e-cf9d452c6fea",
          "semanticId": {
              "type": "ExternalReference",
              "keys": [
                  {
                      "type": "Submodel",
                      "value": "urn:bamm:io.catenax.serial_part:1.0.1#SerialPart"
                  }
              ]
          },
          "description": [],
          "displayName": []
      },
      {
          "endpoints": [
              {
                  "interface": "SUBMODEL-3.0",
                  "protocolInformation": {
                      "href": "https://materialpass.int.demo.catena-x.net/BPNL000000000000/api/public/data/urn:uuid:09d5d8a9-9073-47b6-93c6-80caff176dca",
                      "endpointProtocol": "HTTP",
                      "endpointProtocolVersion": [
                          "1.1"
                      ],
                      "subprotocol": "DSP",
                      "subprotocolBody": "id=urn:uuid:3e4a5957-f226-478a-ab18-79ced49d6195;dspEndpoint=https://materialpass.int.demo.catena-x.net/BPNL000000000000",
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
          "idShort": "singleLevelBomAsBuilt",
          "id": "urn:uuid:09d5d8a9-9073-47b6-93c6-80caff176dca",
          "semanticId": {
              "type": "ExternalReference",
              "keys": [
                  {
                      "type": "Submodel",
                      "value": "urn:bamm:io.catenax.single_level_bom_as_built:1.0.0#SingleLevelBomAsBuilt"
                  }
              ]
          },
          "description": [],
          "displayName": []
      },
      {
          "endpoints": [
              {
                  "interface": "SUBMODEL-3.0",
                  "protocolInformation": {
                      "href": "https://materialpass.int.demo.catena-x.net/BPNL000000000000/api/public/data/urn:uuid:777a3f0a-6d29-4fcd-81ea-1c27c1b870cc",
                      "endpointProtocol": "HTTP",
                      "endpointProtocolVersion": [
                          "1.1"
                      ],
                      "subprotocol": "DSP",
                      "subprotocolBody": "id=urn:uuid:3e4a5957-f226-478a-ab18-79ced49d6195;dspEndpoint=https://materialpass.int.demo.catena-x.net/BPNL000000000000",
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
                      "value": "urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"
                  }
              ]
          },
          "description": [
              {
                  "language": "en",
                  "text": "Digital Product Passport Submodel"
              }
          ],
          "displayName": []
      }
  ]
}

```

### Contract Example
```json
{
    "@id": "registry-asset",
    "@type": "dcat:Dataset",
    "odrl:hasPolicy": {
        "@id": "ZGVmYXVsdC1jb250cmFjdC1kZWZpbml0aW9u:cmVnaXN0cnktYXNzZXQ=:MTIxMjYzMzgtYzhkMC00MGQ4LTkxYWMtZmY2ZTY0ZTQ5ZmM0",
        "@type": "odrl:Set",
        "odrl:permission": [],
        "odrl:prohibition": [],
        "odrl:obligation": [],
        "odrl:target": "registry-asset"
    },
    "dcat:distribution": [
        {
            "@type": "dcat:Distribution",
            "dct:format": {
                "@id": "HttpProxy"
            },
            "dcat:accessService": "bc491229-1b41-49a9-9101-a430a4907e6e"
        },
        {
            "@type": "dcat:Distribution",
            "dct:format": {
                "@id": "AmazonS3"
            },
            "dcat:accessService": "bc491229-1b41-49a9-9101-a430a4907e6e"
        }
    ],
    "edc:type": "data.core.digitalTwinRegistry",
    "edc:description": "Digital Twin Registry for DPP",
    "edc:id": "registry-asset",
    "edc:contenttype": "application/json"
}

```

## Authors
Here are the main authors and reviewers of this documentation:

| Name | GitHub |
| ---- | ------ |
| Mathias Brunkow Moser | [@matbmoser](https://github.com/matbmoser) |
| Muhammed Saud Khan | [@saudkhan116](https://github.com/saudkhan116) |

> NOTE: Find all the repo authors in the [authors.md](../../AUTHORS.md) file!

## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass

