<!--
Tractus-X - Digital Product Passport Verification Add-on

Copyright (c) 2023, 2024 BMW AG, Henkel AG & Co. KGaA
Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

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


<div align="center">
  <img alt="DPP Verificaion Logo" src="./resources/verification-logo.png" width="350" height="350">
  <br><br>
  <img alt="Version:  v1.0.0" src="https://img.shields.io/badge/Version-v1.0.0-blue?style=for-the-badge">
  <h3> Digital Product Pass Verification Add-on</h3>
  <h1> Simple Wallet </h1>
  
</div>


# What is the simple wallet?



# Docker Local Deployment

To deploy the digital product pass simple wallet follow this steps:

Asure you are in the right folder:

```
cd dpp-verification/simple-wallet
```

## 1º- Image creation

```
docker build -t simple-wallet:v1 .
```

## 2º- Run container

```
docker run --rm -p 7777:7777 simple-wallet:v1
```

## 3º - Start sending requests

One it is deployed you will see this starting message:

![Starting Message](./media/startingMessage.png)
