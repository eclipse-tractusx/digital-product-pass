<!--
  Catena-X - Digital Product Pass Application
 
  Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
 
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

# User Manual

This manual provides a step by step introduction on how to use the Product Pass Viewer app and gives an overview on its functionalities.

## Content

1. [Content](#content)
2. [Getting Started](#getting-started)
3. [Main Menu](#main-menu)
    1. [Search for a Product Passport](#search-for-a-product-passport)
    2. [Profile Information and Settings](#settings-and-profile-information)
    3. [Catena-X Helpdesk](#catena-x-helpdesk)
4. [Digital Product Passport](#digital-product-passport)
    1. [Drilling Down Child Components](#drilling-down-child-components)
5. [NOTICE](#notice)

## Getting Started

After opening the Digital Product Pass Viewer application on your computer or phone, login with your company's login credentials.
</br></br>  

## Main Menu

After logging in, you will be forwarded to the application's main menu. Here you can access three functions:

1. [Search for a Product Passport](#search-for-a-product-passport)
2. [Adjust Settings and see Profile Information](#settings-and-profile-information)
3. [Access the Catena-X Helpdesk](#catena-x-helpdesk)

![Main Menu](./images/MainMenu.png)
</br></br>  

### Search for a Product Passport

The [Main Menu](#main-menu) provides two methods for searching for Product Passports. You can request information by:

1. By entering a product ID manually or
2. By scanning the product's QR code

Both options can be found in the center of the [Main Menu's](#main-menu) screen (1). Whenever you access the [Main Menu](#main-menu), the manual product ID search will be displayed by default (1a). The search text contains the pattern separated by the colon(:) `CX:<manufacturerPartId>:<serializedId>` where CX is a prefix, `<manufacturerPartId>` is the part Id of the manufacturer, and `<serializedId>` is the Id of the product Example: `CX:XYZ78901:X123456789012X12345678901234566`. If the search format is not followed, an error would be displayed.

 By clicking on the switch the back arrow button (1b), you activate the QR code scanner and the application will access your devices camera, which you can then capture the QR code with.


![Scan Passport](./images/ScanPassport.png)  
</br></br>  

### Settings and Profile Information

Clicking on the blue avatar icon in the upper right corner of the [Main Menu](#main-menu) (2) opens a drop-down menu containing your profile information and language settings. In this tab you find your specific user information and assigned role (is any) within Catena-X (4), you can sign out from your account (5) or change the applications's language (6) as shown in the below screenshot. Currently. the application supports two different languages: English (EN) as a default language and German (DE) as a second language. 

![Profile Information](./images/UserProfile.png)
</br></br>  

### Catena-X Helpdesk

If questions arise, you can access the Catena-X Helpdesk through clicking on "Help" in the upper right corner of the [Main Menu](#main-menu) (3).  
</br></br>

## Digital Product Passport

 After requesting data via one of the [product search functions](#search-for-a-product-passport), the information will start loading against the asset Id (7) as illustrated in the screenshot , showing the steps to retrieve the passport (9) and displayed on the screen once progress bar is loaded (8). An exemplary product passport of a high voltage battery can be seen below. It provides an overview on the product's history, technical specifications, its child components using the Item Relationship Service (IRS) and data exchange information:

</br></br>
![Loading Product Pass](./images/LoadingPass.png)
</br></br>


</br></br>
![Product Pass](./images/ProductPassport.png)
</br></br>

Hereby, the information is divided into the following sections:

1. Serialization Information
2. Typology Information
3. Metadata Information
4. Components
5. Characteristics
6. Commercial
7. Identification
8. Sources
9. Sustainability
10. Operation
11. Data Exchange Information

Each category can be accessed by clicking on its heading in the selection bar towards the middle of the product passport screen (10).


### Drilling Down Child Components

The application is capable to retrieve the information from the product's child components (if any) that can be seen in the Components section of the passport (11). Once the passport is loaded and displayed to the screen, the drilling down process of the requested product gets started (12) and retrieves the available information from the first level (depth: 1) of the child components. Ths process might take a while to complete (around 4-5 minutes) as it searches the relevant components from the IRS tool.

</br></br>
![IRS Job](./images/IRSJob.png)
</br></br>


In the below figure, the product battery contains one child component (13) named as battery module and its passport can be accessed by clicking the small external arrow (14).

</br></br>
![Drill Down Components](./images/IRS.png)
</br></br>



## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2023 BASF SE, BMW AG, Henkel AG & Co KGaA
- SPDX-FileCopyrightText: 2023, 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
