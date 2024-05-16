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

<h1 style="display:flex; align-items: center;"><img src="./media/catenaxLogo.svg"/>&nbsp;&nbsp;Digital Product Pass Documentation</h1>

Welcome to the documentation section, below you will find all the necesary docs of interest for undestanding the DPP application.

<h2> Table of Contents</h2>

<!-- TOC -->
- [Business Statement and Application Use Case](#business-statement-and-application-use-case)
- [Application User Interface Preview](#application-user-interface-preview)
  - [Passport Search View](#passport-search-view)
  - [Passport General Information View](#passport-general-information-view)
  - [User Interface Documentation](#user-interface-documentation)
- [Architecture Documentation](#architecture-documentation)
  - [Scope](#scope)
  - [Documents of Interest](#documents-of-interest)
- [User and Technical User Changelogs](#user-and-technical-user-changelogs)
- [Technical Documentation](#technical-documentation)
  - [Backend Documentation](#backend-documentation)
  - [Infrastructure Documentation](#infrastructure-documentation)
- [Security Documentation](#security-documentation)
- [Testing Documentation](#testing-documentation)
- [Release Guidelines Documentation](#release-guidelines-documentation)
- [API Documentation](#api-documentation)
  - [Postman Collection](#postman-collection)
  - [Swagger Documetation](#swagger-documetation)
  - [NOTICE](#notice)
<!-- TOC -->

# Business Statement and Application Use Case

> **Note**: *IMPORTANT!* Before you start with the architecture docs **please take a look at the business context** from the application use case. In this way you will be able to understand much better the further documentation.

| Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Business Statement](./interoperability/InteroperabilityGuide.md)         | Business Context from the Application                                                                                                                             |

# Application User Interface Preview

Here you can find some application screenshots that will help you to visualize and get to know the UI.

## Passport Search View

![Search View](./architecture/media/GraphicQRCodeView.png)

## Passport General Information View

![General Info View](./architecture/media/GraphicBatteryPassportViewGeneralInfo.png)

## User Interface Documentation

If you need an extra guideline to understand the application user interface please check the user manual:

| Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [User Manual](./user/UserManual.md)                              | User Manual explaining the user interface                                                                                                                                    |

# Architecture Documentation

## Scope

For understating the achitecture first take a look at the following diagram:

![Application Scope](./architecture/media/dataRetrieval/digitalProductPassContext.drawio.svg)

Here you can see that the Digital Product Pass Application is located between the consumer and the data provider. It should be provided by the Consumer and conected to a EDC. At the moment the DPP is hosted by Catena-X and is connected to a [test EDC Consumer](../deployment/infrastructure/data-consumer/edc-consumer/), which is maintained by the Eclipse Foundation and can be found in the [Eclipse Tractus-X EDC Repository](https://github.com/eclipse-tractusx/tractusx-edc).

The [Arc42](./architecture/Arc42.md) documentation is the main architecture guide you can read in order to have a overview from the application and get to know more about the system.

## Documents of Interest

Here are all the docs you need to understand the architecture and arhitecture configurations:

| Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Arc42](./architecture/Arc42.md)                                             | Main architecture document  of the Digital Product Pass Application                                                                                                                                     |
| [Administration Guide](./admin/AdminGuide.md)                  | Administration Guide explaining the infrastructure and how to configure the application  

# User and Technical User Changelogs

| Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Technical User Changelog](../CHANGELOG.md)                                                | Documentation containing all the release changes at a technical level.  
| [User Changelog](./RELEASE_USER.md)                                                | Resumed released changes from the application without getting into so much technical detail.

# Technical Documentation

## Backend Documentation

You can find the backend documentation at the following location:

| Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Backend Documentation](./dpp-backend/digitalproductpass/readme.md)          | Backend documentation Product Passport App

## Infrastructure Documentation

Here you will find the Infrastructure documentation for the Digital Product Pass Application:

Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Getting Started Introduction](./GETTING-STARTED.md)           | Digital Product Pass application infrastructure, installation guide, technical usage guide
| [Docker Overview](../deployment/local/iam/README.md)                                      | Overview on general docker commands                                                                                                                                |

# Security Documentation

Here you can find the main security documentatin for the Digital Product Pass Application

Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Keycloak Overview](../deployment/local/iam/README.md)                     | This guide describes how to setup a keycloak instance in local docker container and import existing realm.json file.  |
[Code Scaning with Kics and Trivy](./security/infrastructure-as-code/IaC.md)  | Infrastructure As Code (IaC) with KICS intends to find security vulnerabilities by scanning the code and upload results to the security dashboard in github
| [Secret Management](./security/secrets-management/SecretsManagement.md)                          | Secrets management to store client credentials, database passwords, access tokens                                   |

# Testing Documentation

Here you can find the test (End to End, etc.) documentation for the Digital Product Pass Application

Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
 [Cypress Overview](./cypress/CYPRESS.md)                              | Documentation for Battery Passport App E2E Cypress test

# Release Guidelines Documentation

Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
[Release Guidelines](./RELEASE.md)                                     | Product Battery Pass Consumer App Release Guide

# API Documentation

Here you can find the API documentation, there is a postman collection with the workflow that our application is following to retrive the data:

## Postman Collection

Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------                                                                                                                                     |
| [Postman Overview](../deployment/local/postman/README.md)                                    | Technical guide depicts the battery pass end-to-end API calls through the postman REST client                                                                      |

## Swagger Documetation

Additionally we have a open swagger documentation at the following URL:

<pre>
<a href="https://dpp.int.demo.catena-x.net/swagger-ui/index.html">https://dpp.int.demo.catena-x.net/swagger-ui/index.html</a>
</pre>

You can also deploy the application using the [deployment guidelines](./GETTING-STARTED.md), which will be deployed at:

<pre>
<a href="https://localhost:8888/swagger-ui/index.html">https://localhost:8888/swagger-ui/index.html</a>
</pre>

> **Note**: The port can vary if you change the port at the configuration.

## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023, 2024 Contributors to the Eclipse Foundation
- Source URL: <https://github.com/eclipse-tractusx/digital-product-pass>
