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
## [1.0.0] - 16-08-2023

# What is new?
  
## Added
- Added new structure to communicate with:
    - EDC discovery
    - Discovery Finder
    - BPN Discovery
    - New digital twin registry decentralized version
    - Integration of AAS 3.0 APIs
- Added new `/create` API to start a new process
- Added BPN number checks on startup to verify
    - Added a check if the EDC Consumer Connector is accesible
    - Added a check if the EDC Connector BPN is the same as the Backend BPN
    - Added a check if the technical user configured is valid
    - Added a check if the technical user configured has the same BPN number as the EDC consumer
- Added EDC `v0.5.0` support
    - Added new models related with the `EDC v0.5.0` upgrade
    - Added backend compatibility with `EDC v0.4.1`
- Added persistent storage to store contract negotiation details for the data sovereignty
- Added the mechanism to split the Id into 3 parts `CX:<manufacturerPartId>:<serializedId>` and check the validity of the Id used
- Added support for the following Contribution guidelines (TRG) requirements:
    - **TRG-1.02:** Added instructions to the install.md to set up and install the application
    - **TRG-4.02:** Added base images information to the main README.md file
    - **TRG-4.03:** Added user and file permissions to run as a non root user
    - **TRG-4.06:** Added contents for notice for docker images
    - **TRG-5.01:** Added helm chart requirements, remove hostnames from the ingress and environment-specific values
    - **TRG-5.04:** Added resource management settings to limit CPU and memory utilization
    - **TRG-5.08:** Added comments in the helm values.yaml file
    - **TRG-5.09:** Added helm-test workflow to verify that a released helm chart works as expected
    - **TRG-5.10:** Added support of various Kubernetes versions particularly `v1.27`, `v1.26`, `v1.25` and `v1.24` for helm tests
    - **TRG-5.11:** Added helm-upgrade workflow to achieve pre-release upgradeability of the helm charts

## Updated
- Updated frontend helm charts templates for fixing severe merge configuration bug
- Updated the models to match the new Digital Twin Registry exchange
- Optimized the processing time by adding temporary configurable processing storage
- Updated README.md file of the digital product pass components
- Updated pom.xml resources configuration
- Updated ingress annotation to support various Kubernetes versions for helm tests
- Updated the EDC `v0.5.0` configuration
    - Updated SSI configuration in the EDC helm charts
    - Updated Passport storage and how the contract Id key is obtained from the EDR.
    - Updated transfer models
    - Updated helm chart configuration to match the EDC version `v0.5.0`
- Updated the DPP postman collection to test the registry APIs in decentralized fashion
- Updated CONTRIBUTING.md readme file
- Updated .helmignore to ignore environment-specific values files
- Updated Chart.yaml by adding new variables home and sources
- Updated the `Backend.js` service to call the `/create` backend api, with the `manufacturerPartId`,  before the actual search
- Updated DPP documentation to align with current implemention:
    - GETTING-STARTED.md
    - SECRETS_MANAGEMENT.md
    - Arc42.md
    - Admin_Guide.md
    - deployment/readme
    - Fix navigation links in root readme
    - User Manual Product Viewer App.md
    - Consumer-backend readme

## Issues Fixed
- Fixed pom.xml resources and logging configurations to resolve the security vulnerability
- Fixed indentation issue in a helm default values.yaml


## Security Issues
- Updated the sprint boot version from `3.1.0` to `3.1.2` to fix a high security vulnerability


## [released]
## [1.0.0-rc4] - 16-08-2023

# What is new?

## Updated

- Changed exception from Discovery Service startup to a Critical Error message

## [released]
## [1.0.0-rc3] - 15-08-2023

# What is new?

## Added
- Set security checks in values file as disabled by default
- Updated helm docs in charts folder
- Added comments for documentation in swagger

## [pre-released]
## [1.0.0-alpha] - 2023-07-03

## Deleted
- Deleted the cx-backend-service from the EDC Consumer and Provider deployments 
- Removed inrelevant infrastructure files 
- Remove not necesarry logs that affected the performance
  
## Added
- Added new `/endpoint` api to store the payload incomming from the EDC data plane
- Added the encryption and decryption in AES from passport payload.
- Added AES unit tests
- Added the DataPlane service in the backend to comunicate with the data plane.
- Added process manager to manage the asyncronous processes executing in parallel.
- Added process dataModel in session.
- Added new passport util. 
- Added new models to negotiate  and transfer with the new EDC `v0.4.1`
- Added new utils methods like to delete files.
- Added contract controller apis
   - Added contract search  `/api/contract/search`
   - Added contract decline `/api/contract/decline`
   - Added contract sign `/api/contract/sign`
   - Added contract cancel `/api/contract/cancel`
   - Added contract status `/api/contract/status`
- Added new Backend configuration
- Integrated the EDC Data Plane retrieval logic
- Added `.tractusx` metafile
- Align chart version with app version.
- Added file system logging of the negotiation and transfer.
- Added new contract attributes from response.
- Optimized the retrieval time to ~4 seconds.
- Refactored git workflows to add the dpp frontend and backend container images onto Docker Hub registry in order to have public access to the images.
- Added docker.io in digital-product-pass helm chart.
- Commit ID and Repo URL added in frontend image
- Frontend component to display legal information
- Added components to display more contract information.
- Fixed bug related to backend get status, where it looped over the status received.
- Added the AUTHORS.md


## Updated
- Updated charts configurations related to the backend.
- Updated the EDC test charts to remote the cx-backend-service configurations
- Updated payloads
- Update the backend chart configuration
- Refactor secrets structure
- Updated postman collection
- Updated veracode workflow
- Updated the backend service in the frontend to call the async backend apis.
- Updated footer of the application to add the legal information dialog. 
- Updated the Mock payloads in the frontend component.
- Updated pom.xml file adding a resource tag to include files into /META-INF folder inside JAR.
  
## Security Improvements
- Added logic to create and authenticate with  unique session tokens the sign and other methods.
- Added Encryption of passport payload when coming from Data Plane endpoint, until it is retrieved to the  user which is authenticated and is using the unique  session token as decryption key.
- Added unique signKey to backend, which is used to the unique session key.

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
