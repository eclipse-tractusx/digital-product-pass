<!--
#######################################################################

Tractus-X - Digital Product Passport Application 

Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This work is made available under the terms of the
Creative Commons Attribution 4.0 International (CC-BY-4.0) license,
which is available at
https://creativecommons.org/licenses/by/4.0/legalcode.

SPDX-License-Identifier: CC-BY-4.0

#######################################################################
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

Split into groups of five individuals, ensuring each group receives a sheet of paper containing a distinct Part from the  [test-data](./resources/test-data/carParts.json).


> [!Caution]
> Please dont change the given templates for digital twin, only change the provided placeholders 

## Phase 1: Data Provision

In this Phase, you as a provider will create a Digital Product Passport of a spectific Part of the Arena-X Car. For this phase, you can use your own computer through Visual Studio Code / Notepad ++ or a given Virtual Machine.

##### Duration: 45 mins

#### Step 1 - Create a Digital Product Passport (DPP) 

In this step, you'll create the Digital Product Passport (DPP) by utilizing data from the Product Carbon Footprint and specifications of a specific car part from the Arena. 
   
  * Follow the tutorial on [How to create Aspect Model](./aspect-model.md) 

#### Step 2 - Create a Digital Twin integrating the generated DPP as a submodel

In this step, you will create a Digital Twin of your provided Car part. 

  * Follow the tutorial on [How to create a Digital Twin](./digital-twin-provision.md)

#### Step 3 - Generate the QR Code

  * Follow the tutorial on [How to generate a QR code](./qr-code.md)
   
#### Step 4 - Print the QR-Code 

Print the QR Code from the nearby printer and start searching your part in the Car!

#### Step 5 - Paste QR Code in the Car Part of the Picture

Paste it in the specific Part in the Car. Ensure that the QR code is securely attached and easily scannable. Test the scanning functionality to verify that the encoded information can be retrieved accurately.

Great! The data provisioning process has been successfully set up. It is now ready for data exchange within the dataspace. This means that all necessary configurations and integrations have been completed, ensuring efficient data sharing.

## Phase 2: Data Consumption

In this phase, you as a consumer, will access the Digital Product Pass (DPP) consumer application and retrieve data from the network by either scanning a QR code or entering an ID.

##### Duration: 45 mins

#### Consumption - Access DPP Application

  * Follow the tutorial on [DPP Data Consumption](./data-consumption.md)

    * You can access the application by using URL and the credentials. 

    * Scan the available QR Codes on the Car parts or provide the ID. 

    * Upon scanning, you will obtain the passport of the part. 

    * Access the UI of the passport to view its details.

    * Look for the Product Carbon Footprint (PCF) information for each of the specific car part
   
    * Sum up the PCF values of all individual parts to obtain the aggregate Carbon Footprint value for the entire car.
  
## Final Challenge: Car Passport

Group name is defined like this:

```
 dpp-<your number=page number right bit)-<sessionNumber>-partName
```

> [!NOTE]
> Session number will be provided by the coaches! There are three sessions, values from [1-3].
### Creating the Final Digital Product Passport (DPP) for the Whole Car

Scan the available QR codes from various parts of the car and record the Product Carbon Footprint (PCF) value of each specific part.


### Compile the following information to create the DPP:

   * Name: Provide a name for the car model.
   * Description: Include a brief description of the car, its features, and specifications.
   * ID: Assign a unique identifier for the car.
   * Total PCF Aggregate Value: Calculate and add up the PCF values of all individual parts to obtain the aggregate carbon footprint value for the entire car.
   * Any other information you may find relevant

For creating the Car passport digital twin use [this template](./resources/digital-twins/car-dt-template.json)

 * After generating the aspect model you need to generate the car digital twin. You can look in the previous [phase 2](#phase-2:-data-consumption)
   
 * Generate a new UUID4 in this [uuid generator page](https://www.uuidgenerator.net/version4)
 
 * As digital twin "id" add with this schema: `urn:uuid:<your group name>`

 * Create Part Instance Id of your choice and add to `specificAssetIds["name=partInstanceId"].value

>[!IMPORTANT]
> Remember the part instance id you created for later creating the QR code -> `CX:<manufacturerPartId>:<partInstanceId>`

 * For the `manufacturerPartId` use `MPI0012`

 * Add the submodel as specified in the [digital-twin-provision](./digital-twin-provision.md) guide to the twin.

 * Upload your payload to the data service as specified in the [digital-twin-provision](./digital-twin-provision.md) guide.
 
 * Generate a QR code as specified in the [qr code guide](./qr-code.md)

 * Scan it and access it with the app.

 * Add your QR code to the board!


Congratulations! You have completed the E2E Journey for Digital Product Pass

## Links of interest
 
| How to Guides | Link |
| -------- | ----- |
| How to deploy DPP Application | [deployment.md](./deployment.md) |
| How to create Aspect Model | [aspect-model.md](./aspect-model.md) |
| How to create a Digital Twin | [digital-twin-provision](.digital-twin-provision.md) |
| How to generate a QR code | [qr-code.md](./qr-code.md) |
| DPP Data Consumption | [data-consumption.md](./data-consumption.md) |

## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
