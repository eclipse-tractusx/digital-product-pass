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

# DPP Instance Aspect Model Creation Guide

This guide provides a simple explanation on how to create a digital product pass serialized aspect model payload.

## Prerequisites

You must fullfill the following pre-requisites:

- A Catena-X Standarized Aspect Model (in this case we use the [Digital Product Pass v2.0.0 Model](https://github.com/eclipse-tractusx/sldt-semantic-models/tree/main/io.catenax.generic.digital_product_passport/2.0.0))
- JSON Editor like [VS Code](https://code.visualstudio.com/) or [Notepad++](https://notepad-plus-plus.org/downloads/)
- Car Parts JSON Test Data [found here](./resources/test-data/carParts.json)

>[!WARNING]
>
> The model v2.0.0 of the Digital Product Pass is **DEPRECATED** and its used here only as demo purposes, please check the latests models in: [Digital Product Pass Models](https://github.com/eclipse-tractusx/sldt-semantic-modelstree/main/io.catenax.generic.digital_product_passport)


## Aspect Model Creation

Follow this steps to create a new Digital Product Passport serialized model:

### 1º - Paste the example payload in your VS Code/Notepad++ application

Paste this [Digital Product Passport v2.0.0 Payload](./resources/payloads/example.json) in the VS Code/Notepad++:

![DPP Example](dpp-example.png)



## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2023, 2024 BMW AG
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass