<!-- 
  Tractus-X - Digital Product Passport Application 
 
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

## Getting Started Guide

This technical guide depicts the digital product pass end-to-end API calls through the postman REST client.

### Components in Digital Product Pass:
- Provider
  - Submodel Server
  - EDC
  - Registry
- Discovery Finder
- BPN Discovery
- EDC Discovery
- Consumer EDC APIs
    - Registry Asset
    - Data Asset
- DPP Backend


### Steps to  Proceed
- Setup up [Postman client](https://www.postman.com/downloads)

- Import the [Digital Product Pass collection](./Digital-Product-Pass-collection.json)

- Configure the following environment variables from the variables tab inside the root directory.

- ***APIKey***
- ***clientId***
- ***clientSecret***
- ***bpnNumber***

For more technical documentation, please refer to the [catenax-at-home-getting-started-guide](https://catenax-ng.github.io/docs/guides/catenax-at-home)
