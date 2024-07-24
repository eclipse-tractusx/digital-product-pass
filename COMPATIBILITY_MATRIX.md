<!--
#######################################################################

Tractus-X - Digital Product Pass Application 

Copyright (c) 2022, 2024 BMW AG
Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This work is made available under the terms of the
Creative Commons Attribution 4.0 International (CC-BY-4.0) license,
which is available at
https://creativecommons.org/licenses/by/4.0/legalcode.

SPDX-License-Identifier: CC-BY-4.0

#######################################################################
-->

# Digital Product Pass Compatibility Matrix

# v4.0.0 - R24.08

## dpp-backend-v4.0.1

| Dependency                                                                                                                               | Version                      | Helm  | Comments        |
|------------------------------------------------------------------------------------------------------------------------------------------|------------------------------|-------|-----------------|
| [EDC](https://github.com/eclipse-tractusx/tractusx-edc)                                                                                                                                      | 0.7.3                        | [0.7.3](https://github.com/eclipse-tractusx/tractusx-edc/releases/tag/0.7.3) | Management API V3                |
| [Digital Twin Registry](https://github.com/eclipse-tractusx/sldt-digital-twin-registry)                                                                                                                    | 0.5.0                        | [0.5.2](https://github.com/eclipse-tractusx/sldt-digital-twin-registry/releases/tag/digital-twin-registry-0.5.2) |                 |
| [Data Service](https://github.com/eclipse-tractusx/tractus-x-umbrella/tree/main/simple-data-backend)                                                                                                                             | 0.0.1 |                                  [0.1.0](https://github.com/eclipse-tractusx/tractus-x-umbrella/blob/main/charts/simple-data-backend/Chart.yaml) | Available at TX Umbrella |
| [Item Relationship Service](https://github.com/eclipse-tractusx/item-relationship-service) | 5.4.0 | [7.4.0](https://github.com/eclipse-tractusx/item-relationship-service/releases/tag/item-relationship-service-7.4.0) | Component/Part Drill Down Add-on |
| [Simple Wallet](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-verification/simple-wallet) | 1.0.0 | [1.0.0](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-verification/charts/simple-wallet/Chart.yaml) | DPP Verification Add-on |
| [Certified Data Credential](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-verification/semantics/io.catenax.dpp_verification.cdc/) | 1.0.0 | - | DPP Verification Add-on |

Can retrieve any model version thought the EDC proxy. Semantic Ids **MUST** be specified in priority order at the chart values.yaml.

## dpp-frontend-v4.0.1

The frontend can visualize the following models only:

| Model | Version | SemanticId | Comments |
| -- | -- | -- | -- |
| [Digital Product Passport](https://github.com/eclipse-tractusx/sldt-semantic-models/tree/main/io.catenax.generic.digital_product_passport) | 5.0.0 | `urn:samm:io.catenax.generic.digital_product_passport:5.0.0#DigitalProductPassport` | |
| [Battery Pass](https://github.com/eclipse-tractusx/sldt-semantic-models/tree/main/io.catenax.battery.battery_pass) | 6.0.0 | `urn:samm:io.catenax.battery.battery_pass:6.0.0#BatteryPass` | |
| [Transmission Pass](https://github.com/eclipse-tractusx/sldt-semantic-models/tree/main/io.catenax.transmission.transmission_pass) | 3.0.0 | `urn:samm:io.catenax.transmission.transmission_pass:3.0.0#TransmissionPass` | |
| [Certified Data Credential](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-verification/semantics/io.catenax.dpp_verification.cdc/) | 1.0.0 | `urn:samm:io.catenax.dpp_verification.cdc:1.0.0#CertifiedDataCredential`| DPP Verification Add-on |

## dpp-verification/simple-wallet-v1.0.0

| Model | Version | SemanticId | Comments |
| -- | -- | -- | -- |
| [Verifiable Credentials](https://www.w3.org/TR/vc-data-model-2.0/) | 2.0.0 | https://www.w3.org/ns/credentials/v2 | W3C Data Model |
| [Certified Data Credential](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-verification/semantics/io.catenax.dpp_verification.cdc/) | 1.0.0 | `urn:samm:io.catenax.dpp_verification.cdc:1.0.0#CertifiedDataCredential`| DPP Verification Add-on with Catena-X semantic Models |
| [JsonWebSignature2020 & JsonWebKey2020 Proofs](https://www.w3.org/TR/vc-jws-2020/) | 1.0.0 | https://w3c.github.io/vc-jws-2020/contexts/v1/ | DPP Verification Signature & Keys types, used by Gaia-X |

## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG
- SPDX-FileCopyrightText: 2022, 2024 Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023, 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
