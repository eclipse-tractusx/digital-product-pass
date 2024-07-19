<!--
#######################################################################

Tractus-X - Digital Product Pass Application 

Copyright (c) 2024 BMW AG
Copyright (c) 2024 CGI Deutschland B.V. & Co. KG
Copyright (c) 2024 Contributors to the Eclipse Foundation

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This work is made available under the terms of the
Creative Commons Attribution 4.0 International (CC-BY-4.0) license,
which is available at
https://creativecommons.org/licenses/by/4.0/legalcode.

SPDX-License-Identifier: CC-BY-4.0

#######################################################################
-->

# How to generate and print a QR code

This guide will provide you with the information you need to generate a QR code for each car part, as well as how to print the generated QR code in order to be able to attach it to the desired car part. 


## Prerequisites

You must have access to the following components: 

- Sheet with a specific car part from the Arena
- Access to the browser of your choice
- Printer from the Arena
- Vinyl paper for the printer


## Generate the QR code

Follow the steps below to generate and download a QR code in .svg format:


- Visit the [QR Code Generator website](https://goqr.me)
- Make sure that **"Text"** is displayed below the "2. Contents" section
  - If "Text" is not displayed, click the document icon on the left side under the "1. Type" section
- Enter the desired text in the empty field/box. In this case we need the following information: **CX:\<manufacturerPartId>:\<partInstanceId>**
    - This is the \<manufacturerPartId>: 
     ```
     MPI0012
    ```
    - You can find the \<partInstanceId> value on the car part information sheet
- Click **"Download"** on the right side below the QR code
- In the pop-up window, click on **"SVG"** and your QR code will be downloaded

## Print the QR code

-   Open the downloaded .svg file 
-   Select **"Print"**:
    -   If you are using a Windows computer press ``` Ctrl + P ```
    -   If you are using a Mac computer press ```command + P```
-   In the Print Settings window: 
    -   in the Layout section, select **“Landscape”**
    -   click **“more settings”**
    -   select **"4”** in the Pages per sheet section
    -   click **"Print"** at the bottom left
-   Your QR will be printed on the printer


## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2024 BMW AG
- SPDX-FileCopyrightText: 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass

