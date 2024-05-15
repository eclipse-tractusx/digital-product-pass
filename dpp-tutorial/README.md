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

<div align=right><img height=200 src="../docs/media/dpp-tx-logo.png"/>&nbsp;&nbsp;<h1>Tractus-X​ Digital Product Passport​ Workstream​</h1><h4>Second Tractus-X Community Days - 16, 17 May 2024 - Stuttgart</h4></div>


## Description

The digital product passport application provides a consumer user interface to request a battery passport from a battery manufacturer using the standardized components and technologies in a Catena-X network. The passport will be displayed in a human-readable from any browser. The data exchange standards given by Catena-X are used to provide the battery passport to different personas (roles) in the network.

In particular, the appliction is used to access the battery passport data provided by battery manufacturer. By scanning QR-code or knowing the manufacturer and battery-ID, a user can request the passport  through **Eclipse Dataspace Connectors (EDCs)** over the Catena-X network. The passport provider will provide data attributes that is only visible to a permitted signed-in user. 

## Problem Statement

Company-X has manufactured cars for the market in Germany. The cars were built using components from different companies. A surprise inspection is scheduled by a governmental environment auditor in 5 months. This generates a problem for Company-X, because all the data from the parts is not available. Company-X sends an email to their supplier companies, however their send incomplete data in different formats and structures, which confuses Company-X engineers when calculating the carbon footprint and makes the compliance to the regulations really complicated. It is hard to keep track from the latest regulations and convince supplier companies to give all their data without loosing control over it. So, the engineers decide to use Catena-X for the data exchange. Per email Company-X request its suppliers to provide the data while still maintaining data sovereignty, simply by following the Catena-X standards for the Digital Product Passport which comply with the regulations of the inspection. ​

You are one of the suppliers from Company-X. To maintain your client, you are required to create a Digital Product Pass for your Part, so that Company-X can  obtain the total PCF value from its car. Creating a Car Digital Product Pass and fulfilling the sustainability regulations imposed in the inspection.

## Pre-Requisites

  * Kubernetes 1.19+
  * Helm 3.2.0+
  * PV provisioner support in the underlying infrastructure
  * MXD Components:
        2 EDCs (Provider + Consumer)
        1 Keycloak
        1 Vault
  * BPN Discovery Service
  * EDC Discovery Service
  * Discovery Service
  * Printer (for printing QR codes)
  * Mobile Phone with Browser (for scanning QR codes) 


## Preparation

Split into groups of five individuals, ensuring each group receives a sheet of paper containing a distinct Part from the test data [test-data](./resources/test-data/carParts.json).

## Phase 1: Data Provision 

In this Phase, you as a provider will create a Digital Product Passport of a spectific Part of the Arena-X Car. For this phase, you can use your own computer through Visual Studio Code / Notepad ++ or a given Virtual Machine.

##### Duration: 45 mins

#### Step 1 - Create a Digital Product Pass 

* In this step, you'll create the Digital Product Passport (DPP) by utilizing data from the Product Carbon Footprint and specifications of a specific car part from the Arena. 
   
  * Follow the tutorial on "How to create Aspect Model Guide"

#### Step 2 - Hosting JSON Using Postman

* Create a new POST request in Postman. Paste the JSON content into the body section, and send the request to the given URL to host the JSON file.

#### Step 3 - Create a Digital Twin integrating the generated DPP as a submodel

In this step, you will create a Digital Twin of your provided Car part. 

  * Follow the tutorial on "How to create a Digital Twin"

#### Step 4 - Upload the Digital Twin in the Digital Twin Registry
   
Provide the Digital Twin and its relations in the Digital Twin Registry.

* UUID
* Part Instance ID
   * Manufacturer Part ID
   * Submodel Reference

#### Step 5 - Generate the QR Code

  * Follow the tutorial on "How to generate a QR code"
   
#### Step 6 - Print the QR-Code 

Print the QR Code from the nearby printer and paste it in the specific Part in the Arena -X. Ensure that the QR code is securely attached and easily scannable. Test the scanning functionality to verify that the encoded information can be retrieved accurately.


## Phase 2: Data Consumption

##### Duration: 45 mins

#### Step 1 - Digital Product Pass Application Deployment

   * Access the virual Machine (VM) and follow the instructions from the workstream.

   * Namespace creation - Each group will be allocated a Namespace.

   * Deploy the application using Helm Charts

   * Verify and access the deployment using your computer

#### Step 2 - Consumption - Access application using Smartphone using IP address and Ports

   * Access the application using the provided credentials

   * Scan the QR code of your respective part.

   * Upon scanning, you will obtain the passport.

   * Access the UI of the passport to view its details.

####  Step 3 - Creating the Final Digital Product Passport (DPP) for the Whole Car

   * Scan the QR codes from various parts of the car and record the Product Carbon Footprint (PCF) value of each specific part.

* #### Compile the following information to create the DPP:

   * Name: Provide a name for the car model.
   * Description: Include a brief description of the car, its features, and specifications.
   * ID: Assign a unique identifier for the car.
   * Aggregate Value: Calculate and add up the PCF values of all individual parts to obtain the aggregate carbon footprint value for the entire car.



