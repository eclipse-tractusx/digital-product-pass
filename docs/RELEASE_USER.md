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

**April 13 2023 (Version 0.5.2)**  
*13.04.2023*

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
