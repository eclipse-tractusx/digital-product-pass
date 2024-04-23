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
  <img alt="Version:  v1.0" src="https://img.shields.io/badge/Version-v1.0-blue?style=for-the-badge">
  <img alt="STATUS: IN PROGRESS" src="https://img.shields.io/badge/Status-In%20Progress-8A2BE2?style=for-the-badge">
  <h1> Digital Product Pass Verification Add-on </h1>
</div>

> [!CAUTION]
> This documentation is still in progress... Not all the content is yet available. It is being migrated and published here whenever it is ready. It will be finished and added to main in the R24.05

# Metadata

|                      | Date              | Authors & Reviewers                                   |
| -------------------- | ----------------- | ----------------------------------------------------- |
| **Created**          | December 29, 2023 | [Mathias Brunkow Moser](https://github.com/matbmoser) |
| **Lastest Revision** | April 23, 2024    | [Mathias Brunkow Moser](https://github.com/matbmoser) |

## Authors


| Name                  | GitHub                                     | Role                                    |
| --------------------- | ------------------------------------------ | --------------------------------------- |
| Mathias Brunkow Moser | [@matbmoser](https://github.com/matbmoser) | Digital Product Pass Software Architect |
|                       |                                            |                                         |
|                       |                                            |                                         |

## Tags

> [!NOTE]
> #Cybersecurity #DataVerification #DataCertification #Catena-X #DigitalProductPassVerification #DPP #SignedDocuments #DataCredentials
> #DigitalProductPass #VerifiableCredentials #Wallets #DecentralIdenties #SSI #ProductDataExchangeTrust #Verification #Innovation #Ed25519 #JWS #Web3.0



# Abstract

When talking about increasing trust in data ecosystems there are multiple possible ways to be followed. Contractual and Policy solutions can be taken into consideration to ensure data sovereignty based on analog framework agreement contracts. Blockchain solutions can be implemented to assure that transactions and ownership is mathematically proofed, creating a assertive level of trust in the complete chain. Artificial Intelligence can be used as a neutral party for doing moderation and certification of data of partners and member of the network. However if you want to maintain your data and identify under your control assuring data sovereignty and keeping it decentral the best option to choose are Decentral Identities from the W3C.

Decentralized Identities are already used in the Catena-X Network to digitally identify parties and authorizations across all data exchanges done through an EDC from a peer to peer perspective. This technology is implemented in the current SSI concept used in the network and has been proofed to work and also to be successful when bringing trust to all the data exchanges done which take place in the network.

The data exchanged during the peer to peer connections between EDCs can have different formats, shapes and content. It varies from use case to use case and its up to the owner of the data to choose which data will be provided to who and which one not. However once this data is exchanged there is no assertive way to determine if the data provided is really true or false. Framework agreements cover the legal part of the transaction and participation in the use cases however do not cover the specific product information assertion and confirmation of veracity.

Product Information Certification is the way to go when it comes to creating trust over complete or partial data provided in peer to peer connections between two partners in a network. Once the consumer is allowed to visualize the data he can verify if it was certified by its data issuer or by an external auditor party. This is relevant when we start to talk about bringing the Catena-X Automotive Network to a productive environment, specially where human lives are at stake and mistakes can cause huge monetary and image losses.

This Digital Product Pass Verification and Certification concept aims to create an assertive second layer of trust over the actual peer to peer data exchanges of Product Information. Basing itself in the SSI technology already in place in Catena-X, this concept sets the first steps for data verification statements creation starting with the CX Generic Digital Product Pass Aspect Model. Giving the data providers the possibility of creating self signed documents confirming the information placed into the aspect models and gives data auditors the possibility to certify one or more specific attributes from Aspect Model documents that are relevant to the data provider business cases. It allows the data consumer to base its processes and decisions based on actual production data which has been assertive verified by external auditors, giving safety that not just the data issuer by also a third party has certified that specific data is true or compliant to standards.

> The technology concept consists of creating Signed Documents (Verification Statements) using the Verifiable Credentials 2.0 Technology. Which is in resume a JSON-LD structure standardized by the W3C Consortium for Web 3.0 for data trust and identity assurance. Using JSON Web Signatures (JWS) and a wallet component which is connected to Catena-X and identified by the unique company Business Partner Number (BPN), the data issuer and auditor can sign using their Ed25519 private key and the data consumer can access their public key by resolving the DID contained in the signature proof at the certified document credential. The certified data will be stored in the Data Provider infrastructure sub-model server, in order to assure the data sovereignty. Data consumer can access this data if they are allowed by the data provider simply by looking for the Digital Twin from the specific asset type or instance depending on the specific use case. This data will be retrieved using the EDC connector proxy which is protected by Policies and require data consumers to sign "odrl" contracts to maintain data sovereignty.

In this way decentralize data exchange trust is assertive assured. Making possible and easing the transition from the Catena-X network product data exchange from Pre-Production to Production environments. Enabling better decision taking, saving possible human lives, boosting the circular economy use case, creating justification as form of digital proof for possible framework contracts trust breaks or frauds, assuring product quality and increasing employee safety when hazard materials/products are handled.

This concept has been proved to be of high interest from the Certification and Verification roles in the Catena-X Community, generating value for multiple use cases and bringing the Catena-X Network Data Exchange Trust Level to a totally new level. Enabling the different network parties to exchange data with electronic proof of an external party certification revision, reducing the risk of failure and error. Allowing data consumers to comfortably invest and deposit their trust in bring their data into a Catena-X Network Production Data Ecosystem Environment.

## Table of Contents

<!-- TOC -->
- [Introduction](#introduction)
  - [Value Proposition Motivators](#value-proposition-motivators)
  - [Objectives](#objectives)
  - [Use Cases](#use-cases)
- [References](#references)
- [Special Thanks](#special-thanks)
- [Glossary](#glossary)

<!-- TOC -->

# Introduction

This Documentation contains the first concept of Data Verification in Catena-X. This verification aims to create a second layer of trust over the EDC data exchanges between consumers and data providers. Allowing auditors to verify specific attributes or complete aspect models for data providers and allowing consumers to retrieve and verify the "validity" of the verification done.

## Value Proposition Motivators

When talking about certification and verification of data there are a series of motivators that create value for data exchanges done in a production environment.

|     | Motivator             | Description                                                                                                             |
| --- | --------------------- | ----------------------------------------------------------------------------------------------------------------------- |
| 1   | Trustworthiness       | Important for **Accurate Decision Taking** and **Money Loss Avoidance**                                                 |
| 2   | Quality Assurance     | Proof the quality of the products and be able to **audit product quality standards**                                    |
| 3   | Regulatory Compliance | For regulatory reasons in some specific products a **rigorous verification process is mandatory to protect consumers.** |

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

| Name                                  | Description                                                                                                                                                                                                                                                               |
| :------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Assuring Product Quality**          | The final serialized product data manufactured may have flaws/imperfections that can be detected with external type level verified aspects.                                                                                                                               |
| **Digital Proof of Trust Breaks**     | If contractual clauses are broken, having external verified proofs of the actual data provided at a specific time **can be used as legal assets.** This increases the trust of exchanging real production data in Catena-X                                                |
| **Employee Safety Assurance**         | When handling/maintaining a product which contains **critical raw materials**, confirming that the materials present on the product can prevent accidents and fatal human loses.                                                                                          |
| **Production Inefficiency Detection** | When assets are not performing as they were “design” for, external verified attributes **can certify inefficiency** of the product performance in use. Leading to future changes in manufacturing and design.                                                             |
| **Human Life Handling Products**      | Products which handle human lives like Cars, Airplanes and Trains have a strict regulation when it comes to Data Quality requiring the **critical specification data to be “certified/verified** **”** before production for safety reasons                               |
| **Easing Decision Taking**            | When companies need to take important decisions, having external verified attributes/aspect can make a huge difference in which way to go or which product to choose.                                                                                                     |
| **Secure Data Against Fraud**         | The data providers by verifying and signing digitately their data when issued, are **transparently being protected against fraud or false accusations**, because they can demonstrate the data was verified by an external auditors or their internal quality management. |


# Certification Processes

For easing the understanding from the certification process and the interaction between the Data Provider and the Data Auditor, some diagrams are provided where the different interactions and artifacts generated are mapped.

> [!NOTE] 
>
> The Certification Processes of data are valid equally for `Type` level digital twins (Aspect Model in Type Level) or `Instance` digital twins (Aspect Model in Serialized Level). The difference relies on the configuration of the digital twin, and in which level the certification wants to be done.
> Is important to know that the certification **MUST** be at the same level always. If we talk about a Digital Twin in Type Level, then the Digital Product Pass or any aspect model will contain Type level data, as well as the verified attributes.

## Certification Types




##  Attribute Certification Process
The attribute certification is based on a plain JSON Aspect Model Payload that contains the information from a digital product pass. It starts with the `data provider` that creates the `digital product passport` with the available information from and storing it in the `data service`.
Once that is done the data will be linked in a `digital twin`, so in this way by receiving the digital twin and searching for the passport submodel it can be found. After that it will be stored in the `digital twin registry`. Now if any attribute level certification is required to be done by an auditor, a `request` will be triggered from the data provider side, so a `EDC Push Notification` will be sent to the `data auditor` with the EDC Provider URL, the Digital Twin Id and the DPP Aspect Submodel Id (unique identification)

> [!TIP]
>
> A possible optimization to be done is to send directly the digital product pass data and the path to the attributes to be verified. However, for maintaining data sovereignty and the data not being transmitted without a contact exchange, the best way would be to send the IDs and then the `data auditor` will retrieve the data using the EDC.

Once the EDC Push Notification is received by the `data auditor` the Digital Twin and the Digital Product Pass (JSON aspect model payload to be audited) will be retrieved using the `EDC Connector` and through the `EDC Data Plane proxy`. When the passport aspect is available the data auditor can certify the `specific attributes requested` from the product against the different catena-x standards and regulations. The `data auditor` will create a new document (a certified snapshot credential) which contains the proof of compliance of the specific attributes audited in the passport using selective disclosure, there the data is not copied it is hashed so it can be signed and stored in the wallet from the `data auditor` for tracking reasons.

The `CSC Document` (the certificate) will then be sent to the `data provider` using the EDC Push Notification functionality. When the data arrives in the data provider it will be then added to the `Attribute Certification Record (ACR)` which contains all the attribute certifications for an specific aspect model payload submodel. It contains a list of credentials provided by one or more auditors for this aspect. It will be linked in the digital twin where the aspect is and if additional certification is required it will be triggered and the process repeats again.

![](./resources/processes/csc-workflow.svg)

## Self-Testify Certification Process

The self-testify certification process consist in the data provided singing its own data which is being provided. Basically giving proof that he was the one that aggregated and created this data.

![](./resources/processes/cdc-workflow.svg)


## Total Certification Process

The total certification process is the same as the attribute verification process however the complete process is not starting with a plain json file. In this case the data provider can `self testify` its own data. The rest of the process is same and will result in the verification from the specific attributes from the aspect.

![](./resources/processes/cdc-csc-workflow.svg)


# References

The following references were used as inspiration for understanding more how product credentials are done in the market. Is also included references to components in Tractus-X that were used to understand on how the different components behave in the network.

No content with copyright was copied. All the information used as reference in this documentation is open source, is available for the public or released under creative commons license.

| Name                                                                                   | Author                                                                                                                                                                                                                  | Date                                                                                                                 | Link                                                                                                                                                                               |
|:---------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Data Integrity Demonstrator (TRS) in Supply Chain                                      | [Matthias Binzer](https://github.com/matgnt) - Bosch                                                                                                                                                                  | 2023                                                                                                                 |  https://github.com/boschresearch/cx-data-integrity-demonstrator                                                                                                                                                |
| ID Union Data Integrity Demonstrator                                                   | [Matthias Binzer](https://github.com/matgnt) - Bosch                                                                                                                                                                                    | 2023                                                                                                        | https://github.com/IDunion/i40-examples/tree/main/nameplate-vc                                                                                                                     |
| Digital Product Passport Verifiable Credential Demo                                    | Spherity                                                                                                                                                                                                                | 2023                                                                                                                 | https://acme.dpp.spherity.com/                                                                                                                                                     |
| Verifiable Credentials Data Model v1.1/v2.0                                            | W3C                                                                                                                                                                                                                     | 2022 - 2024                                                                                                          | https://www.w3.org/TR/vc-data-model/ https://www.w3.org/TR/vc-data-model-2.0/                                                                                                      |
| Tractus-X SSI Documentation                                                            | Catena-X Core-ART Architects & Eclipse Tractus-X Contributors                                                                                                                                                           | 2023                                                                                                                 | https://github.com/eclipse-tractusx/ssi-docu/tree/main/docs/architecture/cx-3-2                                                                                                    |
| Ed25519: high-speed high-security signatures                                           | Daniel J. Bernstein, University of Illinois at Chicago  Niels Duif, Technische Universiteit Eindhoven Tanja Lange, TechnischeUniversiteit Eindhoven Peter Schwabe,National Taiwan UniversityBo-Yin Yang,Academia Sinica | 2017                                                                                                                 | https://ed25519.cr.yp.to/                                                                                                                                                          |
| Digital Product Pass Documentation and Arc42                                           | [Mathias Brunkow Moser](https://github.com/matbmoser) & [Muhammad Saud Khan](https://github.com/saudkhan116) - CGI - Tractus-X Contributors                                                                             | 2021-2024                                                                                                            | https://github.com/eclipse-tractusx/digital-product-pass/ https://github.com/eclipse-tractusx/digital-product-pass/blob/main/docs/arc42/Arc42.md                                   |
| Managed Identity Wallets                                                               | Tractus-X Contributors                                                                                           | 2022-2024                                                                                                            | https://github.com/eclipse-tractusx/managed-identity-wallet                                                                                                                        |
| Digital Twin Registry                                                                  | Bosch - Tractus-X Contributors                                                                                                                                                                     | 2021-2024                                                                                                            | https://github.com/eclipse-tractusx/sldt-digital-twin-registry                                                                                                                     |
| Tractus-X EDC                                                                          | Tractus-X Contributors                                                                                                                                     | 2021-2024                                                                                                            | https://github.com/eclipse-tractusx/tractusx-edc                                                                                                                                   |
| Eclipse Connector                                                                      | Eclipse Foundation Contributors                                                                                                                                                                                         | 2021-2024                                                                                                            | https://github.com/eclipse-edc/Connector                                                                                                                                           |
| Universal Resolver for DIDs                                                            | Universal Resolver                                                                                                                                                                                                      | 2017-2024                                                                                                            | https://dev.uniresolver.io/ https://github.com/decentralized-identity/universal-resolver                                                                                           |
| Decentralized Identifiers (DIDs) v1.0                                                  | W3C                                                                                                                                                                                                                     | 2022                                                                                                                 | https://www.w3.org/TR/did-core/                                                                                                                                                    |
| Decentralized Identifier Resolution (DID Resolution) v0.3                              | W3C                                                                                                                                                                                                                     | 2023                                                                                                                 | https://w3c-ccg.github.io/did-resolution/                                                                                                                                          |
| Self-Sovereign Identity - Decentralized Digital Identity and Verifiable Credentials v2 | Manning Publications: manning.com                                                                                                                                                                                       | 2020                                                                                                                 | https://livebook.manning.com/book/self-sovereign-identity/chapter-8/v-2/7                                                                                                          |
| EECC Verifier for Verifiable Credentials                                               | Free Software Foundation, Inc (https://fsf.org)                                                                                                                                                                         | 2022-2024                                                                                                            | https://github.com/european-epc-competence-center/vc-verifier [ssi.eecc.de/verifier](ssi.eecc.de/verifier/)                                                                        |
| Identity Resolution Verification                                                       | European EPC Competence Center GmbHhttps://eecc.info/                                                                                                                                                                   | 2022-2024                                                                                                            | https://id.eecc.de/                                                                                                                                                                |
| SuplyTree - The Inter-company Tamper-evidence Protocol for Supply Chain Traceability   | Matthias Guenther, Robert Bosch GmbH, Economy of Things Dominie Woerner, Robert Bosch Switzerland, Economy of Things | 2023                                                                                                                                                                                                                    |                                                                                                                                                                                    |
| A Beginners Guide to Decentralized Identifiers (DIDs)                                  | Amarachi Johnson-Ubah - Medium                                                                                                                                                                                          | 2022                                                                                                                 | https://medium.com/veramo/a-beginners-guide-to-decentralized-identifiers-dids-5e842398e82c#:~:text=A%20decentralized%20identifier%20is%20an,the%20signatures%20of%20that%20subject |
| Schema Organization for JSON-LD                                                        | W3C                                                                                                                                                                                                                     | 2021-2024                                                                                                            | https://schema.org/                                                                                                                                                                |


# Special Thanks
We would like to thank [Matthias Binzer](https://github.com/matgnt) for contributing in the refactoring of the initial concept by giving some insights on how he has done the Supply Chain data integrity concept using Verifiable Credentials (TRS) Data Integrity Demonstrator. He supported us on finding a way and giving the hints for maintaining selective disclosure when it comes to verify specific attributes from a aspect.
We also thank for all the Platform Capability Architects for their disposition for reviewing and supporting the concept from an architecture perspective. We thank the Wallet Catena-X Experts for the time they took review the concept and for the feedback that was given.
We thank the managed identify wallets product owner for the support and availability for answering questions which were relevant to the adaptation of the concept to the architecture.
Last but not least a special thanks for all the Tractus-X and Catena-X Stakeholders that participated in the elaboration and review of this concept.


# Glossary

Here are the abbreviations and complete terms used during the
explanation of this Certification and Verification Concept.

| **Abbreviation**      | **Complete Term**                                          |
| --------------------- | ---------------------------------------------------------- |
| AAS                   | Asset Administration Shell                                 |
| API                   | Application Programming Interfaces                         |
| BPN(BPNL, BPNA, BPNS) | Business Partner Number (Legal Entities, Addresses, Sites) |
| CSC                   | Certified Snapshot Credential                              |
| CDC                   | Certified Data Credential                                  |
| DTR                   | Digital Twin Registry                                      |
| dDTR                  | Decentralized Digital Twin Registry                        |
| DID                   | Decentral Identifier                                       |
| DPP                   | Digital Product Passport                                   |
| DT                    | Digital Twin                                               |
| EDC                   | Eclipse Data Space Connector                               |
| IRS                   | Item Relationship Service                                  |
| JSON                  | JavaScript Object Notation                                 |
| JWT                   | JSON Web Token                                             |
| JWS                   | JSON Web Signature                                         |
| JSON-LD               | JavaScript Object Notation for Linked Data                 |
| MIW                   | Managed Identity Wallet                                    |
| PCF                   | Product Carbon Footprint                                   |
| SSI                   | Self-Sovereign Identity                                    |
| TRG                   | Tractus-X Release Guideline                                |
| TTL                   | Terse RDF Triple Language                                  |
| VC                    | Verifiable Credential                                      |
| VP                    | Verifiable Presentation                                    |
| W3C                   | World Wide Web Consortium                                  |


## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).
See the [License](./CC-BY-4.0) for more information about usage of content and author attribution.
Author: [Mathias Brunkow Moser](https://github.com/matbmoser)

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2023, 2024 BMW AG
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
