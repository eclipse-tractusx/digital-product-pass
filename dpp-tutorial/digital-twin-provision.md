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


This guide provides the information needed to setup digital twin provisioning services as a data provider. Additionally, it enables you to create and register aspect models into the data service.


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


## 1º Prepare Digital Product Pass Model

In order to prepare aspect models, please follow the [aspect model guide](./aspect-model.md) 

## Add Aspect Model to the Submodel Server

The data generated in previous step can be stored into the submode data service.

> [!Caution]
>  The UUID should be written in the format: 6fb9a71b-aee6-4063-a82e-957022aeaa7a

Open a new terminal and run the following command to add your data into the data service: 

Substitute the UUID for the actual one in the paper

```bash
curl --location '<DATA_SERVICE_URL>/data/<UUID>' \
--header 'Content-Type: application/json' \
--data "@<YOUR_JSON_FILE>.json"
```

Verify your data is registerd in the service

```bash
curl --location '<DATA_SERVICE_URL>/data/<UUID>' \
--header 'Content-Type: application/json' \
```

## 2º Create Digital Twin

After preparing and registering aspect models, create a digital twin of a part assigned. 
Create a new json and use the template in [resources/digital-twins/example-dt.json](./resources/digital-twins/example-dt.json)

Replace the following placeholders:

```bash
<PART_INSTANCE_ID>                     ->  the value of part instance written on datasheet
<PART_NAME>                            ->   the part number is written on the datasheet from a part
<UUID>                              ->   the UUID written on datasheet
```

## 3º Add Digital Twin into Digital Twin Registry (DTR)

After creation of the digital twin in previous step, add the twin into Digital Twin Registry (DTR).


```bash
curl --location --request POST '<DIGITAL_TWIN_REGISTRY_URL>/shell-descriptors' \
--header 'Content-Type: application/json' \
--data '@resources/<YOUR_DT_JSON>.json'
```

> [!Note]  
> Every physical part of vehicle is represented by a Digital Twin object. A car is manufactured with plenty of digital twins.

The digital twin registered can be checked/verified from the following command:

> [!Important]
>  The <DIGITAL_TWIN_ID_BASE64_ENCODED> should be encoded into base64. Use the following url for conversion: https://www.base64encode.org/

```bash
Example:
Digital Twin Id: 3f89d0d4-e11c-f83b-16fd-733c63d4e121
Base64 Encoded: dXJuOnV1aWQ6M2Y4OWQwZDQtZTExYy1mODNiLTE2ZmQtNzMzYzYzZDRlMTIx
```

> GET /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>

```bash
curl --location --request GET '<DIGITAL_TWIN_REGISTRY_URL>/shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>' \
--header 'Content-Type: application/json'
```

In case of error, you can always modify your digital twin using the following commands:

> PUT /shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>

```bash
curl --location --request PUT '<DIGITAL_TWIN_REGISTRY_URL>/shell-descriptors/<DIGITAL_TWIN_ID_BASE64_ENCODED>' \
--header 'Content-Type: application/json' --data '@resources/<YOUR_DT_JSON>.json'
```

If everything works fine, then you have reached the end of data provisioning guide.

Congratulations, you have successfully setup the data provider. It is now available and ready to exchange data in the dataspace.


## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2023, 2024 BMW AG
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
