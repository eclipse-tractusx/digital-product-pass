<!-- 
  Tractus-X - Digital Product Passport Application 
 
  Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

<h1 style="display:flex; align-items: center;"><img src="../docs/media/catenaxLogo.svg"/>&nbsp;&nbsp;Catena-X​ Digital Product Passport​ Workstream​</h1>

## Description

The digital product passport  application provides a consumer user interface to request a battery passport from a battery manufacturer using the standardized components and technologies in a Catena-X network. The passport will be displayed in a human-readable from any browser. The data exchange standards given by Catena-X are used to provide the battery passport to different personas (roles) in the network.

In particular, the appliction is used to access the battery passport data provided by battery manufacturer. By scanning QR-code or knowing the manufacturer and battery-ID, a user can request the passport  through **Eclipse Dataspace Connectors (EDCs)** over the Catena-X network. The passport provider will provide data attributes that is only visible to a permitted signed-in user. 

## Problem Statement

Company-X has manufactured cars for the market in Germany. The cars were built using components from different companies. A surprise inspection is scheduled by a governmental environment auditor in 5 months. This generates a problem for Company-X, because all the data from the parts is not available. Company-X sends an email to their supplier companies, however their send incomplete data in different formats and structures, which confuses Company-X engineers when calculating the carbon footprint and makes the compliance to the regulations really complicated. It is hard to keep track from the latest regulations and convince supplier companies to give all their data without loosing control over it. So, the engineers decide to use Catena-X for the data exchange. Per email Company-X request its suppliers to provide the data while still maintaining data sovereignty, simply by following the Catena-X standards for the Digital Product Passport which comply with the regulations of the inspection. ​

You are one of the suppliers from Company-X. To maintain your client, you are required to create a Digital Product Pass for your Part, so that Company-X can  obtain the total PCF value from its car. Creating a Car Digital Product Pass and fulfilling the sustainability regulations imposed in the inspection.

## Preparation

Split into groups of three individuals, ensuring each group receives a sheet of paper containing a distinct Part from the Arena-X Car.

## Phase 1: Create the puzzle with DPPs (Provider)​

In this Phase, you as a provider will create a Digital Product Passport of a spectific Part of the Arena-X Car.

##### Duration: 45 mins

### Step 1 - Creating DPP and integrate in Digital Twin

Generate the Digital Product Passport (DPP) using the data from the Product Carbon Footprint and the specification from the Part. Create a Digital Twin integrating the genenrated DPP as a submodel.

### Step 2 - Digital Twin Registry

Provide the Digital Twin and its relations (in template) in the Digital Twin Registry with a specific ID in the Virtual Machine.

### Step 3 - QR Code

Print the QR-Code and paste it in the specific Part in the Arena -X. Ensure that the QR code is securely attached and easily scannable. Test the scanning functionality to verify that the encoded information can be retrieved accurately.


## Phase 2: Consume the digital product pass with app (Consumer + Provider)

### Step 1 - 

Follow the tutorial on how to deploy the Digital Product Pass application in a specific port from a Virtual Machine​ (Connect to EDC, Discovery Services, etc..)​

### Step 2 - 

Scan the QR codes located around the car that belong to the other team.






















