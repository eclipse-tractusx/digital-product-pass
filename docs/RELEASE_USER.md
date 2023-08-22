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

# Release Notes Digital Product Pass Application
User friendly relase notes without especific technical details.

**August 15 2023 (Version 1.0.0)**
*15.08.2023*

### Added

#### Added EDC v0.5.0 version support

As of now, there is a possibility to run EDC `v0.5.0` version of consumer and provider connectors contained in `deployment/helm/edc-provider` and `deployment/helm/edc-consumer`. Additionally, the last EDC version `v0.4.1` is also compatible with the backend application.

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
Now the test EDC consusumer and provider contained in `deployment/helm/edc-provider` and `deployment/helm/edc-consumer` are updated to the latest version at the moment available the `v0.4.1` 


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
- SPDX-FileCopyrightText: 2022, 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
