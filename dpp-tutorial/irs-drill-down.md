<!--
#######################################################################

Tractus-X - Digital Product Pass Application 

Copyright (c) 2024 BMW AG
Copyright (c) 2024 CGI Deutschland B.V. & Co. KG
Copyright (c) 2024 Contributors to the Eclipse Foundation

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This work is made available under the terms of the
Creative Commons Attribution 4.0 International (CC-BY-4.0) license,
which is available at
https://creativecommons.org/licenses/by/4.0/legalcode.

SPDX-License-Identifier: CC-BY-4.0

#######################################################################
-->

# Introduction

 This guide explains the Item Relationship Service (IRS) plugin which provides a simple way to access the drill down components of digital product pass in an item tree representation, enabling data chains along the value chain in the industry. The below diagram shows the interaction of DPP consumer app and IRS plugin to fetch the component relationships. In this tutorial, it is optional to get the relationships of components.

                                                           
                 ___________________         __________________    
                |                   |       |                  |
                |  DPP CONSUMER APP | <---> |        IRS       |   
                |___________________|       |__________________|
                                            

## Component Hierarchy

To make it easily understandable, lets consider an example of a car battery. A battery consists of battery module and a module further contains a battery cell. In this way, each level of component can have its own passport.


                         ___________________
                        |                   |
                        |      Battery      |
                        |___________________|
                                |     
                                |     ___________________
                                |--->|                   |
                                     |   Battery Module  |
                                     |___________________|
                                               |
                                               |     ___________________
                                               |--->|                   |
                                                    |   Battery Cell    |
                                                    |___________________|


## Prerequisites

You must have the following components up and running: 

- A Data Service (DS) to store component relationships in a plain JSON
- Digital Twin Registry (DTR) to attach SingleLevelBomAsBuilt submodel to existing Digital Twins
- A preconfigured data consumer conenctor with IRS plugin enabled 
- Accessibility of components over the network

## 1° Generate the Ids

Please generate two UUIDs from the UUID generator https://www.uuidgenerator.net/

### BOMAsBuiltID
Generate BOMAsBuiltID using the following format 

```bash
uuid:urn:<UUID-1>
```

### SerialPartID

Generate SerialPartID using the following format 
```bash
uuid:urn:<UUID-2>
```
Store these IDs in some notepad editor for later use.

## 2° Lookup BOMAsBuilt Relationships of the Component

To find your part and be able to copy and paste the information:

1 - Go to the file [bomAsBuiltRelationships](./resources/test-data/bomAsBuiltRelationships.json)

2 - Search by uuid with CTRL + F:

> [!TIP]
> You will get the UUID in the paper or [here](./resources/test-data/carParts.json)

Example:

```json
{
    "catenaXId": "urn:uuid:48acc23b-7cb8-4288-b620-9eb3d9dce6bf",
    "childItems": [
      {
        "catenaXId": "urn:uuid:f10c0181-ce80-4139-81f0-a59226c88bfe",
        "quantity": {
          "value": 1.0,
          "unit": "unit:piece"
        },
        "hasAlternatives": false,
        "createdOn": "2022-02-03T14:48:54.709Z",
        "businessPartner": "BPNL00000003B2OM",
        "lastModifiedOn": "2022-02-03T14:48:54.709Z"
      },
      {
        "catenaXId": "urn:uuid:12e656bb-948e-44eb-9b5e-88d7deedf388",
        "quantity": {
          "value": 1.0,
          "unit": "unit:piece"
        },
        "hasAlternatives": false,
        "createdOn": "2022-02-03T14:48:54.709Z",
        "businessPartner": "BPNL00000003B2OM",
        "lastModifiedOn": "2022-02-03T14:48:54.709Z"
      }
    ]
  }
```

3 - Copy the json content and store it into the notepad editor in json format for later use.

> [!TIP]
> Copy and paste the data for creating your digital product pass faster!



## 3° Add BOMAsBuilt Payload to the Submodel Server

The BOMAsBuilt relationships, you stored temporarily in your notepad editor from [step 2](#2-lookup-bomasbuilt-relationships-of-the-component) must be added into the submodel data service.


> [!IMPORTANT]  
> Please substitute the [BOMAsBuiltID](#bomasbuiltid) that was generated in [step 1](#1-generate-the-ids)


> [!CAUTION]  
> Please dont re-use uuids.


Open a new terminal and run the following command to add your data into the data service: 
> POST /<<BOMAsBuiltID>BOMAsBuiltID>

```bash
curl --location '<DATA_SERVICE_URL>/<BOMAsBuiltID>' \
--header 'Content-Type: application/json' \
--data "@<YOUR_JSON_FILE>.json"
```

> [!TIP]  
> The placeholder <YOUR_JSON_FILE> is the json file which was stored in [step 2](#2-lookup-bomasbuilt-relationships-of-the-component)


Verify your data is registerd in the service

> GET /<<BOMAsBuiltID>BOMAsBuiltID>
```bash
curl --location '<DATA_SERVICE_URL>/<BOMAsBuiltID>' \
--header 'Content-Type: application/json' \
```

## 4° Lookup SerialPart Item of the Component


To find serial part and be able to copy and paste the information:

1 - Go to the file [serialPartItems](./resources/test-data/serialPartItems.json)

2 - Search by uuid with CTRL + F:

> [!TIP]
> You will get the UUID in the paper or [here](./resources/test-data/carParts.json)

Example:

```json
{
 "urn:uuid:c28098f6-6996-4a77-be8d-497fd9f60fc6": {
    "localIdentifiers": [
      {
        "value": "BFH-4459",
        "key": "partInstanceId"
      }
    ],
    "manufacturingInformation": {
      "date": "2022-02-04T14:48:54",
      "country": "DE"
    },
    "catenaXId": "urn:uuid:c28098f6-6996-4a77-be8d-497fd9f60fc6",
    "partTypeInformation": {
      "manufacturerPartId": "MPI0012",
      "customerPartId": "BFH-4459",
      "classification": "product",
      "nameAtManufacturer": "CHASSIS",
      "nameAtCustomer": "CHASSIS"
    }
  }
}
```

3 - Copy the json content and store it into the notepad editor in json format for later use.


## 5° Add SerialPart Item to the Submodel Server

The serial part data, you stored temporarily in your notepad editor from [step 4](#4-lookup-serialpart-item-of-the-component) must be added into the submodel data service.


> [!IMPORTANT]  
> Please substitute the [SerialPartID](#serialpartid) that was generated in [step 1](#1-generate-the-ids)

> [!CAUTION]  
> Please dont re-use uuids.

Open a new terminal and run the following command to add your data into the data service: 
> POST /<SerialPartID<SerialPartID>>

```bash
curl --location '<DATA_SERVICE_URL>/<SerialPartID>' \
--header 'Content-Type: application/json' \
--data "@<YOUR_JSON_FILE>.json"
```

> [!TIP]  
> The placeholder <YOUR_JSON_FILE> is the json file which was stored in [step 4](#4-lookup-serialpart-item-of-the-component)


Verify your data is registerd in the service

> GET /<SerialPartID<SerialPartID>>
```bash
curl --location '<DATA_SERVICE_URL>/<SerialPartID>' \
--header 'Content-Type: application/json' \
```

## 6° Attach BOMAsBuilt to Existing Digital Twin

* Get existing digital Twin by Base64 encoded digital twin Id

The digital twin registered can be checked/verified from the following command:

> [!Important]
>  The <DIGITAL_TWIN_ID_BASE64_ENCODED> should be encoded into base64. Use the following url for conversion: https://www.base64encode.org/

```bash
Example:
Digital Twin Id: urn:uuid:3f89d0d4-e11c-f83b-16fd-733c63d4e121
Base64 Encoded: dXJuOnV1aWQ6M2Y4OWQwZDQtZTExYy1mODNiLTE2ZmQtNzMzYzYzZDRlMTIx
```

> GET /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>

```bash
curl --location --request GET '<DIGITAL_TWIN_REGISTRY_URL>/shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>' \
--header 'Content-Type: application/json' \
--header 'Edc-Bpn: BPNL00000003CSGV'
```

Example JSON response:
```json
{
    "description": [],
    "displayName": [],
    "id": "urn:uuid:a530baad-77ad-4ffc-a925-f3a207839791",
    "specificAssetIds": [
        {
            "supplementalSemanticIds": [],
            "name": "manufacturerPartId",
            "value": "MPI7654",
            "externalSubjectId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "GlobalReference",
                        "value": "PUBLIC_READABLE"
                    }
                ]
            }
        }
    ],
    "submodelDescriptors": [
        {
            "endpoints": [
                { ... }
            ]
        }
    ]
}
```

Copy the actual json response and paste it into the notepad editor. You will need the values later.

### Update Digital Twin


Attach the following **SingleLevelBOMAsBuilt** and **SerialPart** aspects to the existing Digital Twin object that you retrieved and substitute the corresponding placeholders **<BOMAsBuiltID<BOMAsBuiltID>>**  and **<SerialPart<SerialPart>>** with their Ids generated in [step 1](#1-generate-the-ids).

Example to find the placeholders:

1 - Get access to the file [example-dt-irs](./resources/digital-twins/example-dt-irs.json)

2 - Get the placeholders from the below table and search with CTRL + F in [example-dt-irs](./resources/digital-twins/example-dt-irs.json):

3 - Substitute the following placeholders with their values. The values you will get them from the existing digital twin object that was retrieved.


| Placeholder                    | Path                                                            |
|-------------------------       |-----------------------------------------------------------------|
| <digitalTwinId<digitalTwinId>> | globalAssetId                                                   |
|                                | id                                                              |               
| <PART_NAME>                    | idShort                                                         |
| <YOUR PART INSTANCE ID<y>>     | specificAssetIds[1].value                                       |
| <EDC_DATAPLANE_URL>            | submodelDescriptors[0].endpoints[0].protocolInformation.href    |          
|                                | submodelDescriptors[1].endpoints[0].protocolInformation.href    |            
|                                | submodelDescriptors[2].endpoints[0].protocolInformation.href    |
| <digitalTwinSubmodelId<y>>     | submodelDescriptors[0].endpoints[0].protocolInformation.href    |
|                                | submodelDescriptors[0].id                                       | 
| <EDC_CONTROLPLANE_URL>         | submodelDescriptors[0].endpoints[0].subprotocolBody             |         
|                                | submodelDescriptors[1].endpoints[0].subprotocolBody             | 
|                                | submodelDescriptors[2].endpoints[0].subprotocolBody             | 
| <BOMAsBuiltID<BOMAsBuiltID>>   | submodelDescriptors[1].endpoints[0].protocolInformation.href    |         
|                                | submodelDescriptors[1].id                                       |
| <serialPartID<serialPartID>>   | submodelDescriptors[2].endpoints[0].protocolInformation.href    |         
|                                | submodelDescriptors[2].id                                       |       

> [!TIP]
> The table shows number of occurences of a specific property, for example <digitalTwinId<y>> has two occurences: `globalAssetId` and `id`


> [!CAUTION]
> Please make sure that you substitute all the placeholders with their values from the above table

> Please only use your assigned UUID from [here](./resources/test-data/carParts.json) that matches the UUID on the given paper

4 - Save your modified digital twin in json format.

Now, you can update the modified digital twin object using the following command:

> PUT /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>

```bash
curl --location --request PUT '<DIGITAL_TWIN_REGISTRY_URL>/shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>' \
--header 'Content-Type: application/json' --data '@resources/<YOUR_DT_JSON>.json'
```

Congratulations, you have successfully attached BOMAsBuilt relationships and serial part to the existing Digital Twin object.
