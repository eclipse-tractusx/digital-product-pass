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
  <h1> Digital Product Pass Verification Add-on </h1>
</div>

| Version | v1.0 |
|---------|------|

|                      |       Date        |
|----------------------|-------------------|
| **Created**          | December 29, 2023 |
| **Lastest Revision** | April 22, 2024    |


## Tags

> [!NOTE]
> #Cybersecurity #DataVerification #DataCertification #Catena-X #DigitalProductPassVerification #DPP #SignedDocuments #DataCredentials
> #DigitalProductPass #VerifiableCredentials #Wallets #DecentralIdenties #SSI #ProductDataExchangeTrust #Verification #Innovation #Ed25519 #JWS #Web3.0

## Authors

|         Name          |                   GitHub                   |                  Role                   |
|-----------------------|--------------------------------------------|-----------------------------------------|
| Mathias Brunkow Moser | [@matbmoser](https://github.com/matbmoser) | Digital Product Pass Software Architect |
|                       |                                            |                                         |
|                       |                                            |                                         |

## Abstract

When talking about increasing trust in data ecosystems there are multiple possible ways to be followed. Contractual and Policy solutions can be taken into consideration to ensure data sovereignty based on analog framework agreement contracts. Blockchain solutions can be implemented to assure that transactions and ownership is mathematically proofed, creating a assertive level of trust in the complete chain. Artificial Intelligence can be used as a neutral party for doing moderation and certification of data of partners and member of the network. However if you want to maintain your data and identify under your control assuring data sovereignty and keeping it decentral the best option to choose are Decentral Identities from the W3C.

Decentralized Identities are already used in the Catena-X Network to digitally identify parties and authorizations across all data exchanges done through an EDC from a peer to peer perspective. This technology is implemented in the current SSI concept used in the network and has been proofed to work and also to be successful when bringing trust to all the data exchanges done which take place in the network.

The data exchanged during the peer to peer connections between EDCs can have different formats, shapes and content. It varies from use case to use case and its up to the owner of the data to choose which data will be provided to who and which one not. However once this data is exchanged there is no assertive way to determine if the data provided is really true or false. Framework agreements cover the legal part of the transaction and participation in the use cases however do not cover the specific product information assertion and confirmation of veracity.

Product Information Certification is the way to go when it comes to creating trust over complete or partial data provided in peer to peer connections between two partners in a network. Once the consumer is allowed to visualize the data he can verify if it was certified by its data issuer or by an external auditor party. This is relevant when we start to talk about bringing the Catena-X Automotive Network to a productive environment, specially where human lives are at stake and mistakes can cause huge monetary and image losses.

This Digital Product Pass Verification and Certification concept aims to create an assertive second layer of trust over the actual peer to peer data exchanges of Product Information. Basing itself in the SSI technology already in place in Catena-X, this concept sets the first steps for data verification statements creation starting with the CX Generic Digital Product Pass Aspect Model. Giving the data providers the possibility of creating self signed documents confirming the information placed into the aspect models and gives data auditors the possibility to certify one or more specific attributes from Aspect Model documents that are relevant to the data provider business cases. It allows the data consumer to base its processes and decisions based on actual production data which has been assertive verified by external auditors, giving safety that not just the data issuer by also a third party has certified that specific data is true or compliant to standards.

The technology concept consists of creating Signed Documents (Verification Statements) using the Verifiable Credentials 2.0 Technology. Which is in resume a JSON-LD structure standardized by the W3C Consortium for Web 3.0 for data trust and identity assurance. Using JSON Web Signatures (JWS) and a wallet component which is connected to Catena-X and identified by the unique company Business Partner Number (BPN), the data issuer and auditor can sign using their Ed25519 private key and the data consumer can access their public key by resolving the DID contained in the signature proof at the certified document credential. The certified data will be stored in the Data Provider infrastructure sub-model server, in order to assure the data sovereignty. Data consumer can access this data if they are allowed by the data provider simply by looking for the Digital Twin from the specific asset type or instance depending on the specific use case. This data will be retrieved using the EDC connector proxy which is protected by Policies and require data consumers to sign "odrl" contracts to maintain data sovereignty.

In this way decentralize data exchange trust is assertive assured. Making possible and easing the transition from the Catena-X network product data exchange from Pre-Production to Production environments. Enabling better decision taking, saving possible human lives, boosting the circular economy use case, creating justification as form of digital proof for possible framework contracts trust breaks or frauds, assuring product quality and increasing employee safety when hazard materials/products are handled.

This concept has been proved to be of high interest from the Certification and Verification roles in the Catena-X Community, generating value for multiple use cases and bringing the Catena-X Network Data Exchange Trust Level to a totally new level. Enabling the different network parties to exchange data with electronic proof of an external party certification revision, reducing the risk of failure and error. Allowing data consumers to comfortably invest and deposit their trust in bring their data into a Catena-X Network Production Data Ecosystem Environment.

## Table of Contents

<!-- TOC -->
- [Introduction](#introduction)
  - [Value Proposition Motivators](#value-proposition-motivators)
  - [Objectives](#objectives)
  - [Use Cases](#use-cases)

<!-- TOC -->

# Introduction

This Documentation contains the first concept of Data Verification in Catena-X. This verification aims to create a second layer of trust over the EDC data exchanges between consumers and data providers. Allowing auditors to verify specific attributes or complete aspect models for data providers and allowing consumers to retrieve and verify the "validity" of the verification done. 

## Value Proposition Motivators

When talking about certification and verification of data there are a series of motivators that create value for data exchanges done in a production environment.

|   |       Motivator       |                                                       Description                                                       |
|---|-----------------------|-------------------------------------------------------------------------------------------------------------------------|
| 1 | Trustworthiness       | Important for **Accurate Decision Taking** and **Money Loss Avoidance**                                                 |
| 2 | Quality Assurance     | Proof the quality of the products and be able to **audit product quality standards**                                    |
| 3 | Regulatory Compliance | For regulatory reasons in some specific products a **rigorous verification process is mandatory to protect consumers.** |

## Objectives

When defining for the first time the way of doing certification and verification of data aspect models and digital product passports in Catena-X some objectives must be set.

In case of the use case we are aiming our objectives are the following:

1. **Create Value and Draft a Verification Process**
   - Get external verifiers to check the data and generate verifiable credentials or certificates.
   - Define a process for the business verification of data
   - Investigate on the existing market solutions
   - Talk with Catena-X Partners for defining a solid Verification Process draft

2. **Implement the Verification Concept in Catena-X**
   - Design a technical concept for verifying passports in Catena-X
   - **“Be able to verify the data without changing the existent architecture”.**
   - Investigate on existent verification solutions in Catena-X
   - Research and talk with other Catena-X Components/Use Cases about how to integrate the verification in the network.
   - Implement a Technical PoC in the Digital Product Pass application

3. **Define how to create Verification Statements in Catena-X**
   - Set the first steps for other use cases like PCF Verification which need a Verification Statement to be defined.
   - Create a technical solution that can be scaled to other aspect models rather than the Digital Product Pass Use Case
   - Talk with different use cases/components in Catena-X to find a common way of using the network to Certify and Verify data.
   - Following the CX Standards and ideas like Data Sovereignty, Decentralization and Self Sovereign Identities (SSI)

Once this objectives are achieved we will be able to scale the solution and implemented in real life so the benefits of the technology and process defined here can contribute to the automotive industry and increase trust in data exchanges using Catena-X.

## Use Cases

When talking about the certification and verification of data we can find several use cases. Here we have some examples:

| Name                              | Description                                                                                                                                                                                                                |
|:----------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Assuring Product Quality**      | The final serialized product data manufactured may have flaws/imperfections that can be detected with external type level verified aspects.                                                                                |
| **Digital Proof of Trust Breaks** | If contractual clauses are broken, having external verified proofs of the actual data provided at a specific time **can be used as legal assets.** This increases the trust of exchanging real production data in Catena-X |
| **Employee Safety Assurance**     |When handling/maintaining a product which contains **critical raw materials**, confirming that the materials present on the product can prevent accidents and fatal human loses.|
|**Production Inefficiency Detection**          | When assets are not performing as they were “design” for, external verified attributes **can certify inefficiency** of the product performance in use. Leading to future changes in manufacturing and design.|
|**Human Life Handling Products** |Products which handle human lives like Cars, Airplanes and Trains have a strict regulation when it comes to Data Quality requiring the **critical specification data to be “certified/verified** **”** before production for safety reasons |
|**Easing Decision Taking** |  When companies need to take important decisions, having external verified attributes/aspect can make a huge difference in which way to go or which product to choose.|
| **Secure Data Against Fraud** | The data providers by verifying and signing digitately their data when issued, are **transparently being protected against fraud or false accusations**, because they can demonstrate the data was verified by an external auditors or their internal quality management. |

