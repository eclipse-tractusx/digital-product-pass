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

# Release Notes Digital Product Pass Application
User friendly relase notes without especific technical details.


**May 13 2024 (Version 3.0.0)**
*13.05.2024*

### Added

#### Data Sovereighty Configuration enabled

Now in the backend configuration it is possible to add the policies required per contract.
The policy configuration guide can be found here: [Policy Configuration Guide](https://github.com/eclipse-tractusx/digital-product-pass/blob/main/docs/data-sovereignty/PolicyConfigGuide.md)

#### Single API functionality enabled

Now the backend allows users to access and retrieve information without pending of a frontend keyclock authentication. With a single api call `/api/data/request` and the API Key configured in the helm charts values, the backend is allowed to be accessed.

#### EDC 0.7.0 integrated in the application

The Digital Product Pass application now supports the latest edc version available. The new models and payloads have been integrated into the application.

**May 06 2024 (Version 2.3.0)**
*06.05.2024*

### Updated

#### Integration of Industry Core Changes

There has been some new changes from Industry Core adjusted to the asset policy and digital twin. The new attributes are `manufacturerId` and `digitalTwinType` to the specific asset Ids of the digital twin and the asset policy is now contrained to be compliant with the standardization.

#### License and License Header correction

As request by `BASF` in the last interation (PI12) the company wanted to be removed from the copyright for the further development of the application in Tractus-X. Instead `CGI` which has been doing the work in name of BASF is added to the copyright headers and license together with the other companies `BMW` and `HENKEL`. Reference in the Pull Request: [#304](https://github.com/eclipse-tractusx/digital-product-pass/pull/304)


**March 26 2024 (Version 2.2.0)**
*26.03.2024*

### Updated

#### Updated backend to support EDC version v0.6.0
The backend is now supporting EDC version v0.6.0 and is being prepared to be compatible with EDC version v0.7.0.

#### Improved quality of the frontend User Interface
The mocked graphs were removed from the user interface and the graphs are now dynamically renderd based on data. The overall quality of the frontend is improved, missing units were added and separate collection symbol is now available for the battery pass. 

#### Supported new Digital Twin Registry version v0.3.31
The dpp backend is able to comminucate with the latest interface for lookup shells of the digital twin registry using base64 encoding. 


**February 19 2024 (Version 2.1.3)**
*19.02.2024*

### Updated

#### Updated copyright header to Tractus-X instead of Catena-X
In the copyright headers the Digital Product Pass still had the name "Catena-X". It was removed to comply with the TRGs for legal licenses.




**February 16 2024 (Version 2.1.2)**
*16.02.2024*

### Updated

#### Visualization of a policy
A mechanism is created to  visualize the contract policy in an improved manner. This makes it easier to understand and selection of a  right policy.


**February 05 2024 (Version 2.1.1)**
*05.02.2024*

### Added
#### Added missing german translations for transmission passport
The missing translations related with the view to show the transmission passport was added in order to have both english and german languages on the application.

#### Integrated dependabot in repository
Integrated dependabot to automate the dependencies updates and comply to the tractus-x release guidelines.

### Updated
#### Updated License and copyright header
The License and copyright header was changed and the latest version was updated to every files needed in the application folder to the year 2024.

#### Fixed security vulnerabilies related to axios
Updated the axios library to the latest version `v1.6.7` in order to solve the library vulnerability.

**February 02 2024 (Version 2.1.0)**
*02.02.2024*

### Added
#### Added Support for more than one Policy and Contract in the Backend
The backend is now able to handle multiple policies and multiple contracts, allowing the user to select one specific policy if the configuration.

#### Added frontend decline/agree functionality
Now the frotend is able to show the user the option of "agreeing" or "declining" a contract based on the policy. They are able to select a contract and policy.

### Updated
#### Refactored Data Retrieval Script
Refactored the script that retrieves passports to be able to interacturate with the new backend implementation. Upgraded the script documentation to make it more understandable and clear for the user that is using it

#### Updated user manual explanation
Added explanation for agree and decline functionality in the user manual that explains how the user can choose a policy and interactuate with the new frontend implementation.

**February 02 2024 (Version 2.0.3)**
*02.02.2024*

### Updated
#### Updated documentation and business statement
Updated the user manual to reflect the latests changes
The business statement was also updated to reflect the latest functionalities from the application like to display the digital product pass aspect.


**February 02 2024 (Version 2.0.2)**
*02.02.2024*
### Added
#### English and German translations enabled in the application
Now the application is able to visualize the texts in more than one language.
The german language selection is added to the application.

#### Digital Product Pass version v2.0.0
The application now supports the version v2.0.0 of the Digital Product Pass Aspect.
This deprecates the version v1.0.0 of the passport aspect.

**January 04 2024 (Version 2.0.1)**
*03.01.2024*

### Added
#### Added check for invalid and valid Digital Twin Registries in Temporary Storage
The temporary storage mechanism was fixed to add the invalid and valid Digital Twin Registries that were found.
Making the storage more precise and effient than the version `v2.0.0`. 
This fixes a bug from the previous version which more than one BPN numbers were found in the BPN Discovery, it always have done the search without relying on the cache.

#### Added internacionalization library vue-i18n `v9.2.2`
The library has passed all the IP checks and was added in order to support the next release `DPP v2.1.0` with the internacionalizaiton files.

### Security Issues
#### Updated Spring Boot to version `v3.2.1`
In order to fix the security issues the Spring Boot library was updated to the latest version.

#### Updated Axios library to version `v1.6.0`
In order to fix the security issues the Spring Boot library was updated to the latest version.

**December 22 2023 (Version 2.0.0)**
*22.12.2023*

### Added
#### Enabled backend as Standalone Application 
Now there is a seperate helm chart, script and backend packege as digital product pass in the `/dpp-backend` directory.
This marks the backend as standalone application that can be used and deployed without the frontend. The backend packege was also updated to match the latest naming of the application. It was renamed to `org.eclipse.tractusx.digitalproductpass`.

#### Added transmision pass v1.0.0 visualization in frontend
Now the frotend component is able to visualize the transmission pass v1.0.0. It searches for the semanticId of the aspect and visualizes it in the frontend so the user can interactuate with it.


#### Script to retrieve any passport type using the dpp-backend
The script contains the following features:

  - Retrieve DPP information using the backend in json format and prints it to the standard output
  - Prints data retrieval status to console output on each step
  - Perform authentication from the centrally managed authorization server (keycloak) based on company and user credentials provided by the user
  - Export enabled/disabled option to export the requested aspect data to a json file
  - Logging enabled/disabled option to log intermediate retrieval status to a file for further backtracking/debugging/troubleshooting
  - The backend API and authorization server settings are configurable
  - Capable to handle exception and error messages

#### Added security improvement by allowing authorization by BPN and Portal IAM Roles
The backend and the frontend components from the Digital Product Pass Application are now able to be configured for blocking the access to not authorized end users. 
The BPN contained in the configuration will be check if enabled and if enabled also the IAM roles asigned to the end user in the Portal will also be checked for the specific `clientId`

#### Added new Data Retrieval Guide
The data retrieval guide is now available and is a document for explaning in detail how the data retrieval process is performed by applicactions like the digital product pass. It explains how to retrieve data in Catena-X

### Updated
#### Updated the Arc42 documentation (drill down documentation added)
The Arc42 is updated and will be used as a base for the EcoPassKIT. It has detailed explanation about the new functionalities of the backend using the Item Relationship Service (IRS), and also how the application is doing the drill down in components.

#### Updated Admin Guide
The admin guide was updated to match the newest `v1.1.0` of the CX-0096-TriangleForDigitalProductPass standard.

#### Updated QR Scanner
Updated the QR Scanner to be more resilient and scan the QR codes in a more optimized way improving the UI functionality.


**November 14 2023 (Version 1.4.0)**
*14.12.2023*

### Added
#### DPP test data uploader
A script is refactored to upload/remove testdata set from the data provider setup. This speeds up the automatic uploading of various passes types into the provider's digital twin registry, data service and EDC connector.

### Updated
#### Optimize contract negotiation time
There was a long waiting time during the contract negotiation. This time is now reduced and the negotiation is perfomred faster.

**November 08 2023 (Version 1.3.1)**
*08.11.2023*

### Added
#### Added functionality to timeout when `BPN` and `EDC` Discovery API are taking more than the configured time
Now when the application is creating a process and searching to the BPN discovery and the EDC discovery there is a timeout which can be configured to skip the api call if the timeout expires, in this way when APIs that not exist are added to the `Discovery Service` they will be ignored redusing the waiting time for retrieving the passport.


**November 03 2023 (Version 1.3.0)**
*03.11.2023*

### Added
#### Added drill down functionality with `IRS` for the Digital Product Pass Aspect
Now the application is able to drill down into its components one level down.
The backend application is communicating with the IRS and managing the job.
Once the IRS completes its job the backend is able to parse it and inform the frontend that the job has completed.

#### Enabled tree of components drill down in the frontend
Now in the frontend `components` section there will be displayed when available and after loading the
tree of components of the searched passport. It will tell you the status if found, if failed or if not found the children components.

#### Prepare Secondary Material Content (SMC) test use case
There was added the Secondary Material Content (SMC) payload in the configuration from the edc assets to test the SMC use case.

**October 31 2023 (Version 1.2.1)**
*31.10.2023*

### Security Issues
#### Fix the security issues related to 3 library dependencies
The spring boot version was updated to `3.1.5` to fix the vulnerabilities with the `netty-codec-http2` library.
In addition two frontend libraries were updated, the `semver` library was overrided for the latest version and the cypress reference was removed from the dependency list
because of problems with the IP checks: https://gitlab.eclipse.org/eclipsefdn/emo-team/iplab/-/issues/11346, which required IP Team Review.
The `crypto-js` library was also updated to the latest available version.



**October 30 2023 (Version 1.2.0)**
*30.10.2023*

### Added
#### Availability of the DPP aspect model
There is now a possibility to retrieve any type of passport aspect from a submodel endpoint searched by the semanticId of the aspect.

#### Visualization of the "Digital Product Passport" aspect in the frontend
It is now possible to visualize the digital product passports aspect model in the DPP frontend component.

### Updated
#### Updated Decentral lookup for the Digital Twin Registry
The decentral lookup is changed from searching by registry type e.g., data.core.digitalTwinRegistry instead of the registry id/name.

**October 19 2023 (Version 1.1.0)**
*19.10.2023*

### Added
#### Added user friendly loading screen
Now the frontend will be able to visualize the loading process done in the backend.
Making more transparent what is going on in the passport search.

#### QR Code scanner button moved to Welcome Screen search
The quick search for passports using the QR Code was facilitated by including a new QR button
where the search bar in the welcome screen is displayed.

#### Support new dDTR version `v0.3.15-M1` with AAS 3.0
Now we are able to query the Decentral Digital Twin Registry component by using the
`/lookup/shells` API which was once deprecated and now is again in place because of the AAS 3.0 standard.

**August 31 2023 (Version 1.0.1)**
*31.08.2023*

### Updated

#### Fixed the parsing of new attributes by ignoring any extra attributes incoming from the DTR.
Due to the latest hotfix released `v0.3.15-M1` of the `Digital Twin Registry`, was necesarry
to implement a hotfix that ignored all the extra attributes incoming from the DTR that were not relevant
to the Digital Product Pass Application core functionality.

**August 15 2023 (Version 1.0.0)**
*15.08.2023*

### Added

#### Added EDC v0.5.0 version support

As of now, there is a possibility to run EDC `v0.5.0` version of consumer and provider connectors contained in `deployment/infrastructure/edc-provider` and `deployment/infrastructure/edc-consumer`. Additionally, the last EDC version `v0.4.1` is also compatible with the backend application.

#### Added Decentralized Digital Twin Registry integration

The backend component is upgraded to call the decentral Digital Twin Regitry using Asset Administration Shell (AAS) `v3.0` API syntax. Also, integrated the usage of the Discovery Finder service to retrieve the endpoints from the EDC Discovery and the BPN Discovery services.

#### Added search parameter

The search Id is now based on 3 parts separated by colon `CX:<manufacturerPartId>:<serializedId>` and followed by the `CX` prefix. The purpose is to search the asset through these parameters using a decentral registry approach. The manufacturer part Id and serialized Id are also validated.

#### Added persistent storage

The data preservation layer is implemented to ensure that the contract details are stored in logs in order to be complaint with Data Sovereignty requirements.

#### Added TRG requirements

The main following TRG requirements that were added were:

##### TRG-4.06 Added notice for docker images

The released container images need to be annotated to provide good quality images. This has to be defined in a dedicated Notice for docker image section and on the respective image page on DockerHub.

##### TRG-5.04 Added resource management

There needs to be the resource management configuration added along with the application settings. This enables the ability to utilize the CPU and memory consumption as best practices and to match minimum requirements when running an application.

##### TRG-1.02 Added instructions to install the application

There is getting started guide `INSTALL.md` included to the documentation to setup the application from the scratch.

### Updated

#### Updated the EDC v0.5.0 configuration

The Self-Sovereign Identity (SSI) are configured to use the latest EDC version `v0.5.0` in helm configuration.

#### Updated documentation and prepared for release

Updated all the documentation in `docs` folder to match the latest version of the code.

### Security Improvements

#### Added user and file permissions

The user and file permissions are added in the container file system to run as a non-root user for the better security and to restrict the user from unnecessary access.

#### Validate the application against the foreign BPN number

The user authentication is checked against the specified BPN number, EDC consumer, the backend application in order to restrict access the application for security purposes.


**July 03 2023 (Pre-release 1.0.0-alpha)**
*03.07.2023*

### Added

#### Added legal notice in frontend UI
Now the user is able to see the legal notice inclusing the license, the notice and the Commit Id from the source repository.

#### Added legal files into the backend compiled JAR
When the images are generated, and the backend is compiled the LICENSE, NOTICE and DEPENCENCIES_BACKEND are moved inside the JAR file,
into the META-INF folder.

#### Added Official Container Images to Docker Hub
Now the container images are available publicly on the Docker Hub Registry Platform through automated workflows.
They are released in the following URLs:
  - https://hub.docker.com/r/tractusx/digital-product-pass-frontend
  - https://hub.docker.com/r/tractusx/digital-product-pass-backend


#### Integration from frontend and new asynchronous backend
The frontend is now making the negotiation with the backend component in a asynchronous way.

#### Made backend asynchronous.
By creating a asynchronous backend we are improving the control that the user  has over the contract negotiation.
Now the user can decline, cancel and sign the contract requests and visualize the status of the negotiation.
Now the backend  is also negotiating faster with the EDC `v0.4.1` so that is quicker and optimized

#### Added file system negotiation logs.
Each process stores in the container file system (non persistent) the contract negotiation files as well the information for the transfer process.

### Security Improvements

#### Added a new layer of security in the contract negotiation
Allow only the user to sign, decline or cancel the contract negotiation by using a session token generated uniquely in the backend and asigned to the user.
That means that only the user can access its own data. And the backend will make sure that everything is correct otherwise no action is taken.

#### Added cryptography to the passport transfer process
As defined in the documentation of the EDC the passport must be store in the backend until the user  requests for its retrieval.
We are now encrypting the passport  when it arrives from the EDC consumer data plane and we alse asure that the user  will be the only  one  that  can decrypt it. Once the user requests the  passport it is destroyed and no longer available.


### Updated

#### Updated EDC Provider and Consumer Versions to `v0.4.1`
Now the test EDC consusumer and provider contained in `deployment/infrastructure/edc-provider` and `deployment/infrastructure/edc-consumer` are updated to the latest version at the moment available the `v0.4.1` 


## Removed cx-backend-service support
The backend application fully substituted the cx-backend-service by unwrapping the token and storing the information encrypted (an improvement in comparation with the cx-backend-service)
The API that should be used is `/endpoint`


**June 20 2023 (Version 0.9.0)**
*20.06.2023*

### Added

#### Added new welcome screen with basic description of the application and new search bar with QR code.
Now the welcome screen has a description of the application and the search mechanism was improved.

### Updated

#### Optimized the responsiveness of the welcome screen and the search field and optimized UX to match the view of the passport.
Now is more intuitive to navigate through the welcome screen and access the search field.


### Security Issue
#### The Spring Boot version was upgraded to `v0.3.1`
To fix the security issues raised in veracode we needed to increase a version from Spring Boot.

**June 09 2023 (Version 0.8.1)**  
*09.06.2023*

### Added

#### Fixed frontend loading bug
Now the frontend is able to load all the application without giving 502 errors.

### Security Issues

#### Updated version from Vite Library
The vite library version was updated to version `4.1.5` to fix a vulnerability.

**May 18 2023 (Version 0.8.0)**  
*18.05.2023*

#### Fixed bug with the backend external configuration.
The backend configuration is not able to be deployed in different environments without bugs.

**May 10 2023 (Version 0.7.0)**  
*10.05.2023*

#### Enabled the external backend configuration
Now is posible to configure all the backend parameters of configuration in the helm charts.

#### Combined the helm charts from consumer-ui and consumer-backend into one single chart.
in order to ease the deployment the frontend and backend helm charts were merged into one single chart.

#### Fixed bugs related with the frontend
A new tool tip interation was introduced and minor bugs were fixed.

#### Updated the main documentation and increased quality
Created a new readme to guide the user into the digital product pass documentation, updated the quality of the docs and
included preview images at the main readme file.

**April 24 2023 (Version 0.6.0)**  
*24.04.2023*

### Added
#### Implementation of new User Interface (UI) design for the Battery Passport Viewer Page
A new responsive design for the Battery Passport page is implemented which is more attractive and allows users to navigate easily through the various battery modules defined in respective tabs. 

### Updated
#### Updated Quality of Documentation from DPP
Updated documentation to match the new design for the Digital Product Passport

### Security Fixes
Fixed security issues related with Spring Boot version `v3.0.5`, upgrading to `v3.0.6`.

**April 13 2023 (Version 0.5.2)**  
*13.04.2023*

### Deleted
#### Deletion of history table from Digital Product Passport (DPP) Dashboard
The history table was removed from the dashboard.

### Security Issues

Updated Spring Boot Version from `v3.0.2` to `v3.0.5` in order to fix the security issues found in two libraries.


**March 31 2023 (Version 0.5.1)**  
*31.03.2023*

### Updated

Updated license headers to include the `SPDX` attribute


**March 30 2023 (Version 0.5.0)**  
*30.03.2023*

### Updated
#### Redefinition of search parameter in Digital Twin Registry
In order to complete the case study successfuly, the Digital Product Pass application aligned with the CatenaX standardized search parameters and configuration. The following parameters were updated:
- Search `idShort` of submodel with name `batteryPass`
- Search for asset Id in a provider catalog by concatenating a Digital Twin Identification and its submodel Identification property in the following schema:
    
    ```yaml
    {DigitalTwinId}-{DigitalTwinSubmodelId}
    ```

#### Upgraded to Latest (v3.0.1) semantic version of Battery Passport
Following the new standard BAMM model version 3.0.1, the battery passport application is able to display all fields defined in the latest data model of semantic hub.  

#### Upgraded EDC version to 0.1.6
The EDC version was upgraded to comply with the external company.

### Added
#### BETA Environment Configuration
 A BETA environment was configured to complete the case study with an external company. The case study was performed successfully.



**March 01 2023 (Version 0.4.6)**  
*01.03.2023*

### Updated
Updated documentation issues, fixing incorrect structure.

**February 28 2023 (Version 0.4.5)**  
*28.02.2023*

### Security Issues
Updated security issues related with a library.

**February 27 2023 (Initial Release, Version 0.4.4)**  
*27.02.2023*

Welcome to the inital release of the **Product Pass App**. Here is an overview on the applications features.

Here is what's new in **Version 0.4.4:**

### Added

#### Request product information

The Search Passport page allows you request the technical information of products. Requests can be submitted by:

1. Scanning the product QR Code
   - When opened on a device with camera access the products QR code can be scanned and a request for information will automatically be submitted.
2. Entering the Product ID
    - When not opened on a device with camera acces or no when QR Code is available, the Product ID can be entered manually into a search bar.

#### Insight into product information

For a fast and efficient overview, a table containing the technical product information will be returned and displayed after sucessfully submitting an information request.

#### Additional insights

A deep dive on the application's operation and functionalities can be found in the [End User Manual](docs\user%20manual\User%20Manual%20Product%20Viewer%20App.md).


## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
