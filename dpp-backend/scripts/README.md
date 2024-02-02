<!--
  Catena-X - Digital Product Passport Application
 
  Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

## Features
- Retrieve DPP information using the backend in json format and prints it to the standard output
-  Prints data retrieval status to console output on each step
- Perform authentication from the centrally managed authorization server (keycloak) based on company and user credentials provided by the user
- Export enabled/disabled option to export the requested aspect data to a json file
- Logging to log intermediate retrieval status to a file for further backtracking/debugging/troubleshooting
- The backend API and authorization server settings are configurable
- Capable to handle exception and error messages
- When doing contract negotiation the first contract is always used

## TL;DR 
- The default script configuration is provided in [Constants.py](./utilities/constants.py) and can be changed based on the authentication provider.
- Configure the username and password and following required script parameters (shown in below table) in [get-data.sh](./get-data.sh)
- Execute the script `get-data.sh` in a terminal
```bash
./get-data.sh
```

The following parameters can be added in the [test.sh](./test.sh)

#### Script Parameters:
| Parameter         |  Description                                                    | Default value                                                                      | Required/Optionl |
| :---:             | :---                                                           | :---                                                                                | :---:            |
| --company         | Company name                                                   |         CX-Test-Access                                                              | Optional         |
| --username        | username                                                       |         your username                                                               | Optional         |
| --password        | password                                                       |         your password                                                               | Optional         |
| --semanticId      | Semantic ID of the aspect model                                |         *Managed by the Backend*                                                    | Optional         | 
| -idType           | Product type attribute to lookup into digital twin registry    |         *Managed by the Backend*: "partInstanceId"                                  | Optional         |
| --id              |Product type value to lookup into the digital twin registry     |         BAT-XYZ789                                                                  | Required         |
| --discoveryType   |  Discovery type attribute to lookup into the discovery service |         *Managed by the Backend*: "manufacturerPartId"                              | Optional         |
| --discoveryId     | Discovery type value to lookup into the discovery service      |         XYZ78901                                                                    | Required         |
| -getChildren      | Boolean value to check if children are retrieved               |         False                                                                       | Optional         |
| --access_token    | Access token to be used instead of username / password         |         -                                                                       | Optional         |
|                   |                                                                |                                                                                     |                  |


## Export feature
The export feature can be enabled/disabled from the configuration [Constants.py](./utilities/constants.py)
- EXPORT_TO_FILE
