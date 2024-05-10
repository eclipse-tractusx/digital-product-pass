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

# Data Provision
         ________EDC-Connector________         ________Registry________         ________Data Service________  
        |                             |       |                        |       |                            |
        | Controlplane <-> Dataplane  | <---> |         AAS DTR        | <---> |     A plain JSON Server    |           
        |_____________________________|       |________________________|       |____________________________|


This guide provides the information needed to setup data provisioning services as a data provider. Additionally, it enables you to register data payloads into the data service and configure digital twins in digital twin registry component. 


## Prerequisites

You must have the following components up and running: 

- A Data Service (DS) to store passport payloads in a plain JSON format
- Digital Twin Registry (DTR) to store Digital Twins as Asset Administration Shells (AAS)
- A preconfigured EDC Conenctor (Data provider)
- Familiarity with the JSON structure
- Accessibility of components over the network


## Clone a Git repository

Use the following command in your terminal to clone the digital product pass git repository

```bash
git clone https://github.com/eclipse-tractusx/digital-product-pass.git
```

> [!Note]  
> If you already cloned this repository, you can ignore this step


## Prepare DPP data

The content of specific part assigned to each group is available in [resources](./resources/) directory. Please find your content corresponding to your assigned part.


## Add Data to the Submodel Server

After preparation of the DPP data, its time to get your data registered into the data service.

The content from each part of the car is stored in JSON format and can be manipulated via the unique identifier called **Submodel ID**. This *ID* is generated using the following format:

```text
urn:<GROUP_NUMBER>:<PART_NUMBER>
```

```text
For example: urn:dpp01:0001
```

> [!IMPORTANT]  
> Please generate a unique identifier using given format prior to proceed to adding data
> Substitute the required placeholders with the provided values.

Open a new terminal and run the following command to add your data into the data service: 
```bash
curl --location '<INTERNAL_SERVER_IP>/data/urn:<GROUP_NUMBER>:<PART_NUMBER>' \
--header 'Content-Type: application/json' \
--header 'X-Api-Key: <API_KEY>' \
--data "@resources/<YOUR_JSON_FILE>.json"
```

Check your data is registerd in the service
```bash
curl --location '<INTERNAL_SERVER_IP>/data/urn:<GROUP_NUMBER>:<PART_NUMBER>' \
--header 'Content-Type: application/json' \
--header 'X-Api-Key: <API_KEY>'
```

## Create DPP Aspect Model

Use the following template and substitute the following parameters:

```bash
<GROUPNUMBER>                     ->   it is your group number e.g., dpp01
<PARTNUMBER>                      ->   the part number is written on the datasheet from a part
<PROVIDER_CONTROLPLANE>           ->   edc provider controle plane url
<PROVIDER_DATAPLANE>              ->   edc provider data plane url
<ID_SHORT>                        ->   the name is the part assigned to you
```

```json
// Submodel Descriptor template
      {
      "endpoints": [
          {
              "interface": "SUBMODEL-3.0",
              "protocolInformation": {
                  "href": "<PROVIDER_DATAPLANE>/api/public/data/urn:<GROUPNUMBER>:<PARTNUMBER>",
                  "endpointProtocol": "HTTP",
                  "endpointProtocolVersion": [
                      "1.1"
                  ],
                  "subprotocol": "DSP",
                  "subprotocolBody": "id=dpp-asset;dspEndpoint=<PROVIDER_CONTROLPLANE>",
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
      "idShort": "<ID_SHORT>",
      "id": "urn:<GROUPNUMBER>:<PARTNUMBER>",
      "semanticId": {
          "type": "ExternalReference",
          "keys": [
              {
                  "type": "Submodel",
                  "value": "urn:bamm:io.catenax.generic.digital_product_passport:2.0.0#DigitalProductPassport"
              }
          ]
      },
      "description": [
          {
              "language": "en",
              "text": "DPP Submodel Descriptor"
          }
      ]
    }
```


## Update Digital Twin

After creation of the aspect model in previous step, its time to attach this model to submodel descriptors of an existing digital twin.

> POST /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>/submodel-descriptors

```bash
curl --location --request PUT 'https://materialpass.int.demo.catena-x.net/semantics/registry/api/v3.0/shell-descriptors/DIGITAL_TWIN_ID_BASE64_ENCODED/submodel-descriptors/' \
--header 'Content-Type: application/json' \
--header 'X-Api-Key;' \
--data '@resources/DigitalTwins/<part>.json'
```

> [!Note]  
> Every physical part of vehicle is represented by a Digital Twin object. A car is manufactured with a plenty of digital twins.


Check if your aspect model is added to the Digital twin.

> GET /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>/submodel-descriptors

```bash
curl --location --request PUT 'https://materialpass.int.demo.catena-x.net/semantics/registry/api/v3.0//shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>/submodel-descriptors' \
--header 'Content-Type: application/json' \
--header 'X-Api-Key;' \
--data '@resources/DigitalTwins/<part>.json'
```

If everything works fine, then you have reached at the end of data provisioning guide.

Congratulations, you have successfully setup the data provider. It is now available and ready to exchange data in the dataspace.


