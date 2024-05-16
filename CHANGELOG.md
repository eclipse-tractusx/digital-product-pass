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

# Changelog

The changelog format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [Unreleased]


## [released]
## [v3.0.0] - 13-05-2024
### Added
- Added security assessment report in /docs for the threat modeling
- Added SingleApiRequest class for the requested data for the single API.
- Added SingleApiConfig class to had configurations related to the single API.
- Added Policy Check configuration in helm charts
- EDC Util methods for asserting the policy configuration against the actual constraints
- Methods for parsing the policy contraints
- Unit tests for testing the policy evaluation feature
- Policy configuration guide added
- EDC v0.7.0 models
- Optimization in the catalog query using `bpn`
- Updated EDR structure
- Added `lombok` dependency to speed the development
- Added security assessment documentation
- Added Digital Product Pass Verification Concept initial documentation


### Updated
- Updated postman collection and tested end-to-end data exchange journey with EDC v0.7.0
- Updated policy in testdata file
- Updated and renamed the following readme files in /docs
    - docs/admin guide/ -> docs/admin/
    - docs/arch42/ -> docs/architecture/
    - docs/userManual/ -> docs/user/
    - docs/infrastcuture as code/ -> docs/security/infrastructure-as-code/
    - docs/secrets management/SECRET_MANAGEMENT.md -> docs/security/secrets-management/SecretsManagement.md
    - docs/data retrieval guide/data-retrieval -> docs/data-retrieval/README.md
    - docs/business statement/ -> docs/interoperability/Interoperability.md
- Updated all relevant references from the above files
- Updated dpp helm values
- Updated ApiController with the singleApi POST method.
- Updated ContractController by creating call methods (create, search, agree and status) without the authentication step to call in the Single API.
- Updated AuthenticationService by adding the isSingleApiAuthenticated method to authenticate the single API key.
- Updated application.yaml with the single api configurations.
- Updated deployment-backend.yaml with the oauth.apiKey.
- Updated values-int/beta/dev.yaml files with the oauth.apiKey.
- Updated spring boot to version `v3.2.5` from `v3.2.4`
- Updated EDR structure to match new EDC 0.7.0 one


## Deleted
- Deleted values-dev.yaml and values-beta.yaml from helm charts


## Issues Fixed
- Fixed issue with multiple contract and policies parsing
- Optimized data sovereignty checks removing spotted bugs
- Fixed issue when policy is selected in the frontend
- Fixed issue when backend is not available in the fronted


## [released]
## [v2.3.0] - 06-05-2024
### Added
- SingleApiRequest class for the requested data for the single API.
- SingleApiConfig class to had configurations related to the single API.
- ContractService class to move all the processing logic from the API and Contract controllers to this service.
- Added the following Industry Core changes to the policy and digital twin:
    - Added `manufacturerId` and `digitalTwinType` to the specificAssetIds in digital twin registry
    - Added localIdentifiers to the SerialPart aspect model
- Updated ApiController with the singleApi POST method.
- Updated ContractController by creating call methods (create, search, agree and status) without the authentication step to call in the Single API.
- Updated AuthenticationService by adding the isSingleApiAuthenticated method to authenticate the single API key.
- Updated application.yaml with the single api configurations.
- Updated deployment-backend.yaml with the oauth.apiKey.
- Updated values-int/beta/dev.yaml files with the oauth.apiKey.

### Updated
- Refactored workflows where required
- Moved frontend source files into dpp-frontend directory
- Made data-service chart independent from the edc-provider chart
- Renamed Keycloak to IAM
- Re-organized data-consumer and data-provider artifacts
- Re-organized directory strcuture for docs and deployment folders
- Updated documentation references where required
- Updated infrastructure guide
- Updated testdata script to allow EDC constrained policy for the registry
- Updated ApiController with the singleApi POST method.
- Updated ContractController by creating call methods (create, search, agree and status) without the authentication step to call in the Single API.
- Updated AuthenticationService by adding the isSingleApiAuthenticated method to authenticate the single API key.
- Updated application.yaml with the single api configurations.
- Updated deployment-backend.yaml with the oauth.apiKey.
- Updated values-int/beta/dev.yaml files with the oauth.apiKey.
- Refactored the swagger workflow
- Updated: as agreed removed BASF from the license and license headers and added CGI instead


### Deleted
- Deleted unused files/directories/docs/images
    - Removed environment-specific values files from helm charts
    - Removed MOCKed json payloads
    - Removed docker directory
- Deleted veracode-pipeline workflow (replaced by CodeQL and dependabot)

### Issues Fixed
- Fixed tagging issue in swagger workflow
- Fixed table formatting in main `README.md`

### Dependencies Fixed by Dependabot
* chore(deps): bump actions/upload-artifact from 3 to 4 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/214
* chore(deps): bump helm/chart-testing-action from 2.3.1 to 2.6.1 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/216
* chore(deps): bump actions/setup-python from 4 to 5 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/220
* chore(deps): bump veracode/veracode-uploadandscan-action from 0.2.1 to 0.2.6 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/218
* chore(deps): bump actions/checkout from 3 to 4 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/284
* chore(deps): bump docker/build-push-action from 3 to 5 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/283
* chore(deps): bump container-tools/kind-action from 1 to 2 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/285
* chore(deps): bump github/codeql-action from 2 to 3 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/281
* chore(deps): bump follow-redirects from 1.15.4 to 1.15.6 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/258
* chore(deps): bump azure/setup-helm from 3 to 4 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/292
* chore(deps): bump helm/chart-releaser-action from 1.4.1 to 1.6.0 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/291
* chore(deps): bump actions/setup-java from 3 to 4 by @dependabot in https://github.com/eclipse-tractusx/digital-product-pass/pull/290


## [released]
## [v2.2.0] - 26-03-2024

### Updated
- Refactored dpp-script by adding AppId as a script parameter
- Updated the encoding scheme to base64 to the lookup/shells?assetIds... in dpp-backend
- Updated the digital twin registry version to v0.3.31
- Updated postman collection to adjust the APIs from EDC `v0.6.0`
- Updated IRS collection to change authentication process from OAuth2 to the API-Key
- Updated models to support the new EDC 0.6.0 semantics (alias for retro-compatibility enabled)
- Updated edc-consumer and edc-provider helm charts version to `v0.6.0`
- Updated the catalog request semantics
- Optimized/refactored the contract negotiation and transfer flow.
- Updated Data Plane Service data parsing
- Updated the following readme files:
    - Postman readme
    - dpp-script readme
-   Updated translations
-   Updated battery graph

## Added
- Added timeout in negotiation and transfer requests for avoiding infinite loops. When status from transfer does not changes from STARTED.
- Added missing static units
- Added missing data fields

## [released]
## [v2.1.3] - 19-02-2024

## Updated
- Renamed "Catena-X" to "Tractus-X" in header license prefix

## [released]
## [v2.1.2] - 16-02-2024

## Updated
- Updated the following changes in the frontend:
    - Updated visualization and selection of policy
    - Changed styles of buttons and radio button in the policy dialog box to the standard styles guidelines
    - Updated "Help" link to point the end user manual
- Updated end user manual readme
- Increased timeouts to API calls in the backend

## Added
- Added the mechanism to parse the `ODRL` policy structure in frontend

## [released]
## [v2.1.1] - 05-02-2024

## Added
- Added missing german translations to the transmission pass
- Added missing german translations to the policy selection feature
- Integrated dependabot to automate the dependencies updates

## Updated
- Updated axios library from `v1.6.0` to `v1.6.7` to fix vulnerability
- Updated the license headers in all the files to the latest "license and copyright header" of 2024

## Issues Fixed
- Fixed tabs bugs related to translations
- Fixed translations of battery pass and digital product pass
- Fixed missing additional data visualization 
- Fixed missing total sources visualization

## [released]
## [v2.1.0] - 02-02-2024
## Added
- Added policyId parameter to the `/agree` api.
- Added mechanism to get a policy by id from a catalog contract
- Added frontend logic to call the `/decline` and `/cancel` backend APIs
- Added reload option in case of error
- Added/fixed logging of the Digital Twin Registry Contracts and which policy was selected (Data Souverenighty improvement)
- Added util method to move files from one path to another
- Added functionality to treat more than one policy and more than one contract, allowing user to decide
- Added safety functionalities to make sure that the contract selected is correct
- Added Policy selector when autosign is not enabled
- Added Contract JSON details display
- Added Agree/Decline button for contract/policy
- Added fixes in backend and frontend integration
- Added pydoc comments to the getPassport.py
- Added access-token parameter to authorize the APIs without providing username/password and company

## Updated
- Updated versions from container base images
- Adjusted the non root user ids in Dockerfiles and dpp helm chart configurations
- Updated diagrams in Arc42, Data Retrieval Guide to editable drawio svgs
- [BRAKING CHANGE] Renamed API from **sign** to negotiate  **/agree**
- Bumped Vite to version `>=4.5.2`
- Updated the frontend to allow the **AutoSigning** of contracts.
- Optimized temporary directory logging of the DTR negotiation
- Updated logging and readme
- Updated license header
- Updated user manual readme by adding the policy selection screenshots
 
## Issues Fixed
- Fixed issue related to `sleepy` EDCs, allowing the backend to re-request the contract negotiation when the EDC does not respond.

## [released]
## [v2.0.3] - 02-02-2024
## Added
- Added Frameagreement conditions to the provider configuration
- Added Unit Tests for Managers and for Services
- Added changes from version `v1.0.0` to version `v2.0.0` for every component involved
- Added iconMapping for all components involved in DPP `v2.0.0`
- Added multi-language feature that supports currently `EN` and `DE`
- Added back button in the welcome page
- Added `timeToLive` attribute to discovery service model in the dpp-backed
- Added `readOnlyRootFilesystem` to the container security context in helm charts
- Added Serial Part aspect in the drill down components
- Added `notice.md` to include **Notice for docker images** section to be only part of DockerHub description

## Updated
- Cleaned up necessary scripts
-Refactored the DPP and IRS postman collections
- Updated the deployment and testing directory structure and their references in relevant documentation
- Updated license header and deployment directory references in the following readme files:
    - Admin guide
    - Arc42
    - Getting-Started guide
- Updated test directory stricture in dpp-backend
- Updated the app url from [https://materialpass.int.demo.catena-x.net](https://materialpass.int.demo.catena-x.net) to [https://dpp.int.demo.catena-x.net](https://dpp.int.demo.catena-x.net)
- Updated the payloads of asset, policies, contract definition, digital twin and its aspects to align with the DPPTriangle document v1.1.0
- Updated the following frontend content:
    - Condition for "commercial.warranty" in General Cards
    - Mocked passports
    - Loading page translation
    - Translation files
    - Characteristics component
    - Identification component
    - Sustainability component
    - Typology component
- Updated helm template to provide security context values from helm vaules file
- Updated kics workflow
- Updated user manual, deployment guide
- Updated year 2024 to the license headers
- Updated diagrams to an editable version in `.svg` in business statement readme
- Updated `README.md` and `UNIT_TESTS.md` for the dpp-backend
- Updated versions in docker workflows and setup-java action
- Refactor docker workflows
- Updated **Notice for docker images** section in a main `README.md`


## [released]
## [v2.0.2] - 02-02-2024
## Added
- Added Frameagreement conditions to the provider configuration
- Added Unit Tests for Managers and for Services
- Added changes from version `v1.0.0` to version `v2.0.0` for every component involved
- Added iconMapping for all components involved in DPP `v2.0.0`
- Added multi-language feature that supports currently `EN` and `DE`
- Added back button in the welcome page
- Added `timeToLive` attribute to discovery service model in the dpp-backed
- Added `readOnlyRootFilesystem` to the container security context in helm charts


## Updated
- Cleaned up necessary scripts
-Refactored the DPP and IRS postman collections
- Updated the deployment and testing directory structure and their references in relevant documentation
- Updated license header and deployment directory references in the following readme files:
    - Admin guide
    - Arc42
    - Getting-Started guide
- Updated test directory stricture in dpp-backend
- Updated the app url from [https://materialpass.int.demo.catena-x.net](https://materialpass.int.demo.catena-x.net) to [https://dpp.int.demo.catena-x.net](https://dpp.int.demo.catena-x.net)
- Updated the payloads of asset, policies, contract definition, digital twin and its aspects to align with the DPPTriangle document `v1.1.0`
- Updated the following frontend content:
    - Condition for "commercial.warranty" in General Cards
    - Mocked passports
    - Loading page translation
    - Translation files
    - Characteristics component
    - Identification component
    - Sustainability component
    - Typology component
- Updated helm template to provide security context values from helm vaules file


## Deleted
- Filtered out unnecessary nnecessary/unused files


## [released]
## [v2.0.1] - 03-01-2024
## Added
- Added function to check for duplicated DTRs in the temporaryStorage
- Added check for skipping the check of all BPNs when the DTRs are not available for security and optimization
- Added `vue-i18n v9.2.2` library that will be used in the release `v2.1.0` with the translations
- Added check to fix bug related to invalid BPN endpoints in cache

## Updated
- Updated header license of modified files to match the new year 2024

## Security Issues
- Updated Axios from version `v0.8.1` -> `v1.6.0`
- Updated Spring Boot from version `v3.1.5` -> `v3.2.1`
- Logback from Log4j got updated with the Spring Boot `v1.4.11` ->  `v1.4.14` 

## Issues Fixed
- Fixed the backend IRS exception handling, for detecting failure when job does not start
- Fixed misconfiguration of config maps related to the temporaryStorage
- Fixed incorrect authors names

## [released]
## [v2.0.0] - 22-12-2023

## Added
 
- Added components to display a Transmission Pass in frontend
- Added MOCK file with the transmission pass in frontend
- Added permission listener & reloader in frontend
- Added camera toggle component in frontend
- Added Decentral Digital Twin Registry configuration payloads
- Added IRS configuration guide
- New Data Retrieval Guide added with detailed information how to retrieve data in Catena-X like the DPP.
- Added new structure of files for the Arc42
- Added new Authentication & Authorization chapter in the Arc42
- Added detailed description next to the API diagrams at the Arc42
- Added IRS Integration documentation at the Arc42
- Added diagrams in xml form for further edition
- Added Check of BPN Number in Frontend (With Trigger in Configuration) 
- Added Check of BPN Number in Backend (With Trigger in Configuration) 
- Added Check for appId Roles in Frontend (With Trigger in Configuration) 
- Added Check for appId Roles in Backend (With Trigger in Configuration) 
- Added new security checks in the backend and frontend
- Added new non authorized view in the frontend
- Added script to retrieve any type of aspect data using the dpp backend component
- Added [README.md](./deployment/infrastructure/README.md) on how to execute the script with certain parameters

## Updated

- Refactor frontend code to be compliant with transmission pass 2.0.0.
- Refactor Responsiveness of frontend
- Updated URL check in frontend
- Corrected the payloads ids and the configurations
- Updated the Install.md file with a section to install the prerequisites and configure values.yaml files
- Updated the file structure from the Arc42
- Updated data retrieval api diagrams with better quality
- Updated context diagram with a new easy context
- Updated Arc42 Descriptions in the Technical Integration chapter
- Updated data uploading script by adding physical dimensions aspect
- Refactored helm chart values configuration.
- Refactored backend values chart configuration
- Update the IRS version to 4.2.0
- Adapted the springboot configuration structure
- Update the default policies
- [BREAKING CHANGES]: Updated the path from the backend from `/consumer-backend` to just `/dpp-backend` to declare the to standalone application


## [released]
## [v1.4.0] - 14-12-2023

## Added
- Added script to automate the uploading of various passport types
- Added script to delete data from the data provider
- Added check for empty or null contractIds with retry attempts
- Added descriptive logs to search and create methods

## Updated
- Updated ingress settings and backend configuration in the helm chart
- Refactored helm values to show only user relevant settings

## Issued Fixed
- Fixed the timeout time for each negotiation
- Fixed the long waiting time by implementing timeout when doing the negotiation
- Fixed the null contract ids creation

## Deleted
- Remove the legacy style to register/delete the testdata from the data provider
 

## [released]
## [v1.3.1] - 08-11-2023

## Added
- Added timeout for the `BPN Discovery` and `EDC Discovery`  APIs returned in the Discovery Service API.
- Created a new `doPost` function in the `HttpUtils`that allows the usage of timeouts in API calls.
- Create a new `timeout` function in `ThreadUtils` to be applied to the call of functions.

## [released]
## [v1.3.0] - 03-11-2023

 
## Added 
- New IRS Service which is able to communicate with the IRS component api.
- Added condition to deactivate the IRS
- Added tree manager to manage the tree of components used by each irs process
- Added structure to manage the information coming from the IRS and jobs initiated
- Enabled callback mechanism with the IRS component
- Created `/api/irs/{processId}/tree` and  `/api/irs/{processId}/components` APIs
- Added process to refresh the cache when the transfer has failed
- Added timestamp to every known DTR in the cache for refreshing the contract id every time it is reached.
- Added a mechanism to parse/update file system json files by specific properties, avoiding conflicts
- Enabled irs search in frontend
- Added info bar in component search
- Create visual tree of components
- Enabled drill down in tree of components
- Add IRS configuration to the helm values
- Added Secondary Material Content (SMC) json payloads, edc assets to test the SMC use case

## Updated
- Update dpp/irs test data edc assets and script to register them

## Issues Fixed
- Fix IRS tree component bugs related to the Digital Twin parsing
- Fix IRS job tracker to one single job.
- Fix bug related to the broadcast search of digital twin registry
- Fix minor bugs related to the digital twin search and the caching mechanism
- Fix bug related to the passport retrieval by implementing check for transfer complete
- Fix database credentials in the edc postgres configuration
- Fix the condition to the publish dpp backend workflow


## [released]
## [1.2.1] - 31-10-2023

## Deleted
- Removed cypress from `package.json` dependencies
- Removed unused devDependencies of `@babel`

## Security Issues 
- Fixed vulnerabilities related to `crypto-js`, `semver`, `netty-codec`.
  - Updated `Spring Boot` to version `v3.1.5`
  - Updated `crypto-js` to version `v4.2.0`
  - Overrided `semver` to versions over `^v7.5.3`


## [released]
## [1.2.0] - 30-10-2023

## Added
- Added comments at every classes explaining their purpose
- Added comments at every methods explaining what its done, each parameter, what it returns and if it throws exceptions
- Added SupressionWarnings("Unused") annotation in every Class and/or method that is unused, to decide if it is to delete or let it be for future use
- Added New class for the new model of Digital Product Passport
- Added to retrieve any type of bamm aspect from a submodel endpoint (searching by the semanticId included in the digital twin)
- Added new structure to parse the passport payload and display it dynamically
- Added new components to display the passport
- Added visualization of the "Digital Product Pass" aspect in the frontend
- Added a second check for "transfer-completed" in history when passport status is checked in the frontend

### Updated
- The Aspect Submodels are searching in the Digital Twin by their `semanticId` instead of `idShort` parameter
- Updated DTR search as type instead of ID
- Updated the Apis that communicate with the backend
- Updated DTR configuration to support the new DTR API `v1.0`

### Deleted
- Removed the passport's version requirement
- Removed version from the Search Api call
- Deleted configuration variables related with central search from application.yaml
- Deleted old version of DigitalTwin, Submodel and Endpoint, no longer used and renamed the most recent versions.
- Deleted all code related to the central search.
- Removed the central `Digital Twin Registry` support


### Issues Fixed
- Fixed a bug related to the discovery service when more than one search endpoint would be available
- Fixed bug related to the passport search and the transfer data not being available sometimes

## [released]
## [1.1.0] - 19-10-2023

## Added
- Added loading screen functionality in the frontend
- Create IRS component configuration for the integration with the backend application in next versions
- Created IRS postman collection for testing the APIs.
- Added the QR code scanning button in the main search view (welcome screen)
- Added support for the new dDTR lookup shells version which is (AAS 3.0) compliant.

## Updated
- Updated the digital twin test payloads to match AAS 3.0 payloads which should be used for version `>v1.0.0` of the DPP
- Updated the main readme fixing notable mistakes.

## [released]
## [1.0.1] - 31-08-2023

## Updated
- Fixed model parsing from payloads incoming from the `Digital Twin Registry` due to their latest hotfix.
  - Ignored extra attributes not relevant for the Digital Product Pass application.
  
- Added footer License notes in the all the documents inside `/docs` to comply with the TRGs.

- Updated the Trivy workflow to fix a bug related with the configuration

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
