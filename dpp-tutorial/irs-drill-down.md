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


## 1° Lookup BOMAsBuilt Relationships of the Component


* Search for the <<UUID>UUID> of the component which was used to from the given sheet of paper. You can also find the same information [here](./resources/test-data/carParts.json) in a test JSON file.

To find your part and be able to copy and paste the information:

1 - Search by uuid with CTRL + F:

![search id](./resources/screenshots/idsearch.png)

You will get your information in a paper:

Example:

```json
{
 "f10c0181-ce80-4139-81f0-a59226c88bfe": {
      "Name":"TRUNK LID HINGE (LEFT)",
      "PCF (Product Carbon Footprint)": "189 kgCO2e",
      "Height": "24 cm",
      "Width": "2 cm",
      "Length": "38 cm",
      "Weight": "1.4 kg",
      "id": "f10c0181-ce80-4139-81f0-a59226c88bfe",
      "Part Instance ID": "DLH-5159",
      "Manufacturing Date": "01.12.2023",
      "Placed on Market Date": "15.01.2024",
      "List of Materials": "Aluminum",
      "Hazard Materials": "Lead, Butyl, Cyanoacrylates, Polyurethane",
      "Guarantee": "24 months"
  }
}
```

2 - Copy the json content and paste it into the notepad editor for latter use.

> [!TIP]
> Copy and paste the data for creating your digital product pass faster!



## 2° Add BOMAsBuilt Relationships to the Submodel Server

The BOMAsBuilt data retrieved in [previous step](#1-lookup-bomasbuilt-relationships-of-the-component) can be stored into the submodel data service.

* Generate a new UUID from here: https://www.uuidgenerator.net/

```bash
Example: uuid:urn:6fb9a71b-aee6-4063-a82e-957022aeaa7a
```

> [!IMPORTANT]  
> Please substitute the <<UUID>UUID> with the one generated above.


Open a new terminal and run the following command to add your data into the data service: 
> POST /uuid:urn:<<UUID>UUID>

```bash
curl --location '<DATA_SERVICE_URL>/uuid:urn:<UUID>' \
--header 'Content-Type: application/json' \
--data "@<YOUR_JSON_FILE>.json"
```

Verify your data is registerd in the service

> GET /data/uuid:urn:<<UUID>UUID>
```bash
curl --location '<DATA_SERVICE_URL>/uuid:urn:<UUID>' \
--header 'Content-Type: application/json' \
```

## 3° Lookup SerialPart Item of the Component

Once the BOMAsBuilt relationship is added the submodel server, similarly, search for the Id of serial part component with the same UUID as used to search for BOMAsBuilt relationship.

1 - Search by uuid with CTRL + F:

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

2 - Copy the json content and paste it into the notepad editor for latter use.


## 4° Add SerialPart Item to the Submodel Server

The serial part data retrieved in [previous step](#3-lookup-serialpart-item-of-the-component) can be stored into the submodel data service.

* Generate a new UUID from here: https://www.uuidgenerator.net/

```bash
Example: uuid:urn:6fb9a71b-aee6-4063-a82e-957022aeaa7a
```

> [!IMPORTANT]  
> Please substitute the <<UUID>UUID> with the one generated above.


Open a new terminal and run the following command to add your data into the data service: 
> POST /uuid:urn:<<UUID>UUID>

```bash
curl --location '<DATA_SERVICE_URL>/uuid:urn:<UUID>' \
--header 'Content-Type: application/json' \
--data "@<YOUR_JSON_FILE>.json"
```

Verify your data is registerd in the service

> GET /data/uuid:urn:<<UUID>UUID>
```bash
curl --location '<DATA_SERVICE_URL>/uuid:urn:<UUID>' \
--header 'Content-Type: application/json' \
```

## 5° Attach BOMAsBuilt to Existing Digital Twin

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

Copy the json response and paste it into the notepad editor for modification.

### Update Digital Twin


Attach the following **SingleLevelBOMAsBuilt** and **SerialPart** aspects to the existing Digital Twin object and substitute the corresponding placeholders **<BOMAsBuiltID<BOMAsBuiltID>>**  and **<SerialPart<SerialPart>>**.

* **<BOMAsBuiltID<BOMAsBuiltID>>**   -->  BOMAsBuilt UUID generated [here](#2-add-bomasbuilt-relationships-to-the-submodel-server)
* **<SerialPart<SerialPart>>**       -->  SerialPart UUID generated [here](#4-add-serialpart-item-to-the-submodel-server)

Example to find the placeholders:

```json
{
    "href": "https://dpp-provider-dataplane.a3fb75c369e540489a65.germanywestcentral.aksapp.io/api/public/<BOMAsBuiltID>",
}
```
```json
{
       "idShort": "singleLevelBomAsBuilt",
        "id": "<BOMAsBuiltID>",
}
```
```json
{
       "href": "https://dpp-provider-dataplane.a3fb75c369e540489a65.germanywestcentral.aksapp.io/api/public/<serialPartID>",
}
```
```json
{
       "idShort": "serialPart",
        "id": "<serialPartID>",
}
```

Find the above placeholders in the following json and substitute the values accordingly.

```json
{
    ...
    "submodelDescriptors": [
        {
            "endpoints": [
                { ...}
            ]
        },
        {
            "endpoints": [
                {
                    "interface": "SUBMODEL-3.0",
                    "protocolInformation": {
                        "href": "https://dpp-provider-dataplane.a3fb75c369e540489a65.germanywestcentral.aksapp.io/api/public/<BOMAsBuiltID>",
                        "endpointProtocol": "HTTP",
                        "endpointProtocolVersion": [
                            "1.1"
                        ],
                        "subprotocol": "DSP",
                        "subprotocolBody": "id=urn:uuid:0c3d2db0-e5c6-27f9-5875-15a9a00e7a27;dspEndpoint=https://dpp-provider-dataplane.a3fb75c369e540489a65.germanywestcentral.aksapp.io",
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
            "id": "<BOMAsBuiltID>",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:samm:io.catenax.single_level_bom_as_built:3.0.0#SingleLevelBomAsBuilt"
                    }
                ]
            },
            "supplementalSemanticId": [],
            "description": [
                {
                    "language": "en",
                    "text": "DPP singleLevelBOMAsBuilt Submodel"
                }
            ],
            "displayName": []
        },
        {
            "endpoints": [
                {
                    "interface": "SUBMODEL-3.0",
                    "protocolInformation": {
                        "href": "https://dpp-provider-dataplane.a3fb75c369e540489a65.germanywestcentral.aksapp.io/api/public/<serialPartID>",
                        "endpointProtocol": "HTTP",
                        "endpointProtocolVersion": [
                            "1.1"
                        ],
                        "subprotocol": "DSP",
                        "subprotocolBody": "id=urn:uuid:0c3d2db0-e5c6-27f9-5875-15a9a00e7a27;dspEndpoint=https://dpp-provider-dataplane.a3fb75c369e540489a65.germanywestcentral.aksapp.io",
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
            "idShort": "serialPart",
            "id": "<serialPartID>",
            "semanticId": {
                "type": "ExternalReference",
                "keys": [
                    {
                        "type": "Submodel",
                        "value": "urn:samm:io.catenax.serial_part:3.0.0#SerialPart"
                    }
                ]
            },
            "supplementalSemanticId": [],
            "description": [
                {
                    "language": "en",
                    "text": "DPP serial part Submodel"
                }
            ],
            "displayName": []
        }
    ]
}
```


Now, you can update the modified digital twin object using the following command:

> PUT /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>

```bash
curl --location --request PUT '<DIGITAL_TWIN_REGISTRY_URL>/shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>' \
--header 'Content-Type: application/json' --data '@resources/<YOUR_DT_JSON>.json'
```

Congratulations, you have successfully attached BOMAsBuilt relationships and serial part to the existing Digital Twin object.
