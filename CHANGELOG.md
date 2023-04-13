<!--
  Catena-X - Product Passport Consumer Frontend
 
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

# Changelog

The changelog format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [released]

## [0.5.2] - 2023-04-13

## Security Issues

- Updated Spring Boot Version from `v3.0.2` to `v3.0.5` fixing the following vulnerable libraries:
  - Spring Expression *v6.0.4* -> `v6.0.7`
  - JSON Smart *v2.4.8* -> `v2.4.10`

## [released]

## [0.5.1] - 2023-03-31

## Updated

- Aligned header structure with the current `License.md` file in all the project documents headers.

## [released]

## [0.5.0] - 2023-03-30

## Updated
- Updated documentation
- Upgraded EDC version to 0.1.6
- Refactor passport view
- Support passport semantic version 3.0.1
- Upgraded frontend and backend integration to permanent
- Updated Exception handling and application robustness
- Updated Swagger documentation
    - Removed login/ logout auth APIs
- Updated postman collection for INT and DEV to retrieve passport version 3.0.1
- Refactor assets registration scripts for the mock provider
- Change the mock provider endpoint configuration from `/provider` to `/BPNL000000000000`
- Update all environment configuration files to adapt semantic version 3.0.1
- Upgraded frontend mockups to support semantic version 3.0.1


## Added
- Externalized the frontend configuration using helm charts
- Added tooltip component in frontend
- Added veracode workflow_dispatch
- Added search in backend for submodel with `idShort` as `batteryPass` in the Digital Twin Registry (DTR)
- Added `endpointAddress` parsing in backend with and without `BPN`
- Added search for `assetId` in a provider catalog using the scheme `{DigitalTwinId}-{DigitalTwinSubmodelId}`
- Enabled "OPTIONS" method in backend CORS configuration
    - Allowing local browser calls to the backend
- Support end-to-end integration with external companies


## Issues Fixed
- EsLint configuration
- Veracode secret names for tractusx

## Deleted
- Removed Wrapper.js from frontend component
- Removed additional mocks in frontend configuration


## [released]

## [0.4.6] - 2023-03-01

## Updated
- Updated documentation, corrected links.

## [released]

## [0.4.5] - 2023-02-28

# Security Issues
- Insecure SnakeYaml library version 1.33 updated to Secure version 2.0

## [released]

## [0.4.4] - 2023-02-27 

## Updated
- Reworked header component and styles to match portal
- Reworked footer component and styles to match portal
- Reworked search input and styles to match portal
- Updated image repository for consumer-ui and consumer-backend helm deployments
- Reworked table component and styles to match portal

## Added
- Refactored SCSS styles structure, allowing a optimized scalability from the application
- Updated frontend dependencies file.
- Matched portal styles to pass quality gate 5
- Refactored responsiveness from components
- Important Documentation related with the product and repository


## [0.4.3] - 2023-02-17 

## Updated
- Fixed Swagger UI documentation (updated to correct one)
- Fixed Enter Key Search Bug (Adding submit property)
- Fixed Deployment issues.
- Fixed Incorrect image building
- Fixed Import Bugs related with integration

## Security Issues:
- Removed Keycloak Library vulnerability 


## [0.4.2] - 2023-02-05
### Added
- Deployment refactored to fit and follow new guidelines from DevSecOps team.
- Data soverenity printing the contract id
- Tractus-X Ready code 

### Updated
- Optimized the reponsive from the application
- Optimized the passport display
- Updated components with Vuetify 3, making the application more stable. 
