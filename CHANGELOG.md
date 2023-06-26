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
## [0.9.0] - 2023-06-20

# What is new?
 
## Added
 - Added new welcome screen with basic description of the application
 - Added new search bar.
 - Added new QR code advance searching mechanism

## Updated
- Optimized the responsiveness of the welcome screen and the search field.
- Optimized UX to match the view of the passport.

## Security Issues
- Upgraded Spring Boot version to  `v0.3.1`


## [in  preparation]
## [0.9.0] - xxxx-xx-xx

## Deleted

- Deleted the cx-backend-service from the EDC Consumer and Provider deployments 

## Added
- Added new `/endpoint` api to store the payload incomming from the EDC data plane
- Added the encryption and decryption in AES from passport payload.
- Added AES unit tests
- Added the DataPlane service in the backend to comunicate with the data plane.

## Updated
- Updated charts configurations related to the backend.
- Updated the EDC test charts to remote the cx-backend-service configurations

## [released]
## [0.8.1] - 2023-06-09

## Updated
- Updated the NGINX configuration
- Updated ingress templates in product helm charts

## Added
- Added the base image recommended by the TRGs: `nginxinc/nginx-unprivileged:stable-alpine` in the frontend image generation.

## Security Issues:
- Updated version from vite to `v4.1.5` to fix critical vulnerability raised by the dependapot: https://github.com/eclipse-tractusx/digital-product-pass/security/dependabot/2


## [released]
## [0.8.0] - 2023-05-19

## Updated
- Updated configuration charts
- Updated backend utilities (added spring boot to the classes)
- Updated tests to use spring boot to autowire the components.


## Added
- Added new configuration classes
- Added new methods to get the configurations at the start of the services

## Deleted
- Deleted the configuration util (old alternative to spring boot configuration)
- Deleted the tests for configuration util.


## [released]

## [0.7.0] - 2023-05-10

## Updated
- Adapted springboot application yaml as a single source of configuration
- Externalized the consumer-backend configuration through k8s configmaps and helm values
-  Updated DPP documentation to align with current implemention:
    - GETTING-STARTED.md
    - RELEASE.md
    - SECRETS_MANAGEMENT.md
    - Arc42.md
    - Admin_Guide.md
    - deployment/readme
    - Fix navigation links in root readme
    - User Manual Product Viewer App.md
    - Consumer-backend readme

## Deleted
- Removed environment-specific configurations from consumer-backend
- Removed standalone consumer-ui and backend helm charts

## Added
- Added an umbrella helm chart digital-product-pass, containing consumer-frontend and consumer-backend helm deployments together
- Prepare environment-specific values.yaml
- Prepare values.yaml for default mode
- Added Tooltip Component
- Added Bar Component Fix
- Added new Readme to guide the developer/user thought the documentation
  
## Security Fixes
- Blocked accessExcalation in the frontend helm templates.

## [released]

## [0.6.0] - 2023-04-25

## Updated
- Updated pull request (PR) template description
- Upgrade battery pass test data for Passport Semantic version `v3.0.1`
- Adjusted footer and search components in consumer frontend
- Updated documentation for the digital product pass (Arc42 + Admin Guide)

## Deleted
- Removed history table from Dashboard page 

## Added
- Implemented new User Interface (UI) design for battery passport page
- Added digital product passport (DPP) logo and favicon icon for DPP frontend component 
- 404 and Error message in consumer backend handler
- Added null and none checks in the passport (improved stability)
  
## Issues Fixed
- Reference to the back button in consumer frontend
- Updated battery QR code styles
- Security vulnerabilities in veracode scanning tool
- Fixed swagger ui display api documentation
- Improved responsiveness from the application 

## Security Issues
- Fixed security vulnerabilities related to Spring Boot `v3.0.5` by upgrating to `v3.0.6`

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
