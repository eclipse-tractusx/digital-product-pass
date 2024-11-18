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

# Explorer Path

In this Path, you, as a provider, will create a Digital Product Passport (DPP) for a specific Part of the Arena-X Car. For this purpose, you will use <a href="https://insomnia.rest/" target="_blank">Insomnia</a>, a user-friendly tool for sending and receiving HTTP requests.

> [!Tip]
> If the installation still has to be done, follow this <a href="https://docs.insomnia.rest/insomnia/get-started" target="_blank">Get Started Guide</a> from Insomnia.

## Pre-Step - Setting Up the Environment

To begin the work in the Explorer Path, you first need to set up the environment. For this tutorial, we will use **Insomnia** as our web-based HTTP communication tool to send and receive data in a visual and user-friendly way. 

To set up the environment follow this steps:

- Open Insomnia
- If necessary: create a new project
- click on **"Import"**
- choose **"URL"**
- copy the following url
```bash
https://raw.githubusercontent.com/ELebedkin/digital-product-pass/refs/heads/main/dpp-tutorial/resources/httpie_payloads/01%20Tractus-X%20Community%20Days.postman_collection.json
```
- paste it into the URL-placeholder box
- click on **"Scan"**
- if done correctly there has to be "Postman Resources to be imported: 5 Requests"
- click on import


Congratulations! You have now imported all the necessary API calls for this tutorial.

---

## Step 1 - Create a Digital Product Passport (DPP) 

In this step, you'll create the Digital Product Passport (DPP) by utilizing data from the Product Carbon Footprint and specifications of a specific car part from the Arena. 
   
  * For a more technical explanation, refer to: <a href="./aspect-model.md" target="_blank">How to create Aspect Model</a>.

### Aspect Model Creation

Follow this steps to create a new Digital Product Passport serialized model:

### Step 1.1: Find test data before generating the model

In the worksession you will receive a paper with the test data, you can find the same information <a href="./resources/test-data/carParts.json" target="_blank">here</a> in a test JSON file.

To find your part and be able to copy and paste the information:

- Search by UUID with `CTRL + F` (or `CMD + F` on Mac):

![search id](./resources/screenshots/idsearch_gitrepo.png)

You will get your information in a paper:

Example:

```json
{
 "f10c0181-ce80-4139-81f0-a59226c88bfe": {
      "Name":"TRUNK LID HINGE (LEFT)",
      "PCF (Product Carbon Footprint)": "189 kgCO2e",
      "Height": "24 cm",
      "Width": "2 cm",
      "Length": "38 cm",
      "Weight": "1.4 kg",
      "id": "f10c0181-ce80-4139-81f0-a59226c88bfe",
      "Part Instance ID": "DLH-5159",
      "Manufacturing Date": "01.12.2023",
      "Placed on Market Date": "15.01.2024",
      "List of Materials": "Aluminum",
      "Hazard Materials": "Lead, Butyl, Cyanoacrylates, Polyurethane",
      "Guarantee": "24 months"
  }
}

```
---
### Step 1.2: Substitute data in the template

In Insomnia, locate the request labeled `Step 2.1.1 Create Aspect Model` and switch to the **Body** tab.

Replace the placeholders in the provided template with the data from your part. For example, to add the Product Carbon Footprint (PCF) value, use the following path:

```text
sustainability.productFootprint.carbon[0].value
```

Example:

![PCF](./resources/screenshots/pcf_search_insomnia.png)

---

### Additional: Data Mapping Table

Use the following table to identify where to place your part's information in the template:

| Property | Path |
| -------- | ----- |
| Name (Really short) | identification.type.nameAtManufacturer |
| Class/Type of Part | identification.classification.classificationDescription |
| PCF | sustainability.productFootprint.carbon[0].value |
|Height| characteristics.physicalDimension.height.value |
|Width| characteristics.physicalDimension.width.value|
|Length| characteristics.physicalDimension.length.value|
|Weight| characteristics.physicalDimension.grossWeight.value|
|Part Instance Id | identification.serial[0].value |
|Manufacturing Date |operation.manufacturer.manufacturingDate |
| Guarantee | lifespan[0].value (Add value) |
| Guarantee | lifespan[0].unit (Add unit:months) |

Congratulations! You have successfully created your own digital product pass!

> [!TIP]
> You can add more relavant data and personalized information at the digital product pass, follow the template and modify the data as you wish!

---

## Step 2 - Create a Digital Twin integrating the generated DPP as a submodel

In this step, you will create a Digital Twin of your provided Car part. The data generated in previous step can be stored into the submodel data service. 

> [!Caution]
>  The UUID should be written in the format: 6fb9a71b-aee6-4063-a82e-957022aeaa7a

---

### Step 2.1: Register the Aspect Model

1. In the Insomnia App, locate the request labeled `Step 2.1.1 Create Aspect Model`.
2. Replace `<UUID-1>` with the UUID provided on your datasheet, as shown in the example

Example:

```text
https://tx-dpp.int.catena-x.net/urn:uuid:f10c0181-ce80-4139-81f0-a59226c88bfe
```

3. Send the **POST** request

- If successful, a 200 OK response will appear next to the `Send`-Button, confirming the Aspect Model has been registered in the service.

4. To verify the registration:
- Use the Insomnia request labeled `Step 2.1.2 Verify the Creation`.
- Replace <UUID-1> with your actual UUID from the datasheet.
- Send the request. A 200 OK response confirms that the data has been registered successfully.

---

### Step 2.2 

Now we actually will create the digitil Twin.

1. Open the Insomnia request labeled `Step 2.2.1 Create Digital Twin"`.

> [!Note]
> This request uses the template provided in <a href="./resources/digital-twins/example-dt.json" target="_blank">resources/digital-twins/example-dt.json</a>.

2. Switch to the Body tab and replace the following placeholders:

```bash
<PART_INSTANCE_ID>                     ->  the value of part instance written on datasheet
<PART_NAME>                            ->   the part number is written on the datasheet from a part
<UUID-1>                               ->   the UUID written on datasheet
```

3. Generate a new UUID:

- Visit  <a href="https://www.uuidgenerator.net/version4" target="_blank">this UUID Generator</a> to generate an additional UIID
- Replace `<UUID-2>` with this new UUID

> [!Important]
> There are **two instances** of `UUID-2` in the example. Please replace **both** of them:
> - One is used as `"id"`
> - The other is used as `"href"`

4. Send the POST request to add the Digital Twin to the Digital Twin Registry (DTR).
- A successful request will return a `200 OK` response.

> [!Note]  
> Every physical part of vehicle is represented by a Digital Twin object. A car is manufactured with plenty of digital twins.

---

### Step 2.2.2: Verify the Digital Twin Registration

1. Use the Insomnia request labeled `"Step 2.2.2 Verify the Creation"`.
2. Replace `<DIGITAL_TWIN_ID_BASE64_ENCODED>` `with` the Base64-encoded version of the Digital Twin ID.
- You can encode your Digital Twin ID using this <a href="https://www.base64encode.org/Base64" target="_blank"> Encoder</a>

Example:

```bash
Digital Twin Id (UUID): 3f89d0d4-e11c-f83b-16fd-733c63d4e121
Base64 Encoded: dXJuOnV1aWQ6M2Y4OWQwZDQtZTExYy1mODNiLTE2ZmQtNzMzYzYzZDRlMTIx
```

3. Replace <UUID-1_BASE64_ENCODED> in the following URL:

```bash
https://dpp-registry.int.catena-x.net/semantics/registry/api/v3/shell-descriptors/<UUID-1_BASE64_ENCODED>
```

4. Send the request. A `200 OK` response confirms that the Digital Twin has been successfully registered.

---

### Step 2.3

If you encounter an error or need to update the Digital Twin, you can use the Insomnia request labeled `Step 2.3 Modify Digital Twin` to make changes.

> [!Note]  
> Copy the **Body** from `Step 2.2.1 Create Digitale Twin` into the new **Body** in `Step 2.3 Modify Digital Twin` and afterwards modify data/attributes.

If everything works fine, then you have reached the end Explorer Path.

Congratulations, you have successfully setup the data provider. It is now available and ready to exchange data in the dataspace.

You can now process further with the original DPP-Tutorial at Step 3 - Generate the QR-Code. Click <a href= "/dpp-tutorial/README.md#step-3---generate-the-qr-code" target="_blank">here</a> to aaccess the next steps.


## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2024 BMW AG
- SPDX-FileCopyrightText: 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass

