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

# Data Retrieval Script
A command line python script to request for any aspect model data using the Digital Product Passport (DPP) backend reusable component.

## Pre-requisites:
- The provider components setup:
    - Decentralized Digital Twin Registry (dDTR)
    - Backend data service (submodel server)
    - EDC setup
- The consumer components setup:
    - DPP backend service
    - EDC and IRS services setup

## TL;DR 
- The default script configuration is provided in [Constants.py](./utilities/constants.py) and can be changed based on the authentication provider.
- Configure the following required script parameters (shown in below table) in [test.sh](./test.sh)
- Execute the script `test.sh` in a terminal
```bash
./test.sh
```

#### Script Parameters:
| Parameter  | Description                                                    | Default value                                                                       | Required/Optionl |
| :---:      | :---                                                           | :---                                                                                | :---:            |
| -s         | Semantic ID of the aspect model                                | urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport   | Optional         | 
| -it        | Product type attribute to lookup into digital twin registry    | partInstanceId                                                                      | Optional         |
| -id        |Product type value to lookup into the digital twin registry     |       -                                                                             | Required         |
| -dt        |  Discovery type attribute to lookup into the discovery service | manufacturerPartId                                                                  | Optional         |
| -di        | Discovery type value to lookup into the discovery service      |       -                                                                             | Required         |
| -c         | Boolean value to check if children are retrieved               |     False                                                                           | Optional         |
|            |                                                                |                                                                                     |                  |
