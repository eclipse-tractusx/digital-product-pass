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

## Certification Section

### 1° Create Certified Data Credentials (CDC)


#### Get Digital Twin Id
- Get the `<DIGITAL_TWIN_ID_BASE64_ENCODED>` from step 2.2.2 from this [Insomnia collection](./resources/explorer_payload/01%20Tractus-X%20Community%20Days.postman_collection.json) and store it in notepad editor for later use.

#### Get Submodel Id
- Search for the `digitalProductPass` keyword from the existing digital twin submodel

- Encode the submodel Id using this <a href="https://www.base64encode.org/Base64" target="_blank" rel="noopener noreferrer"> Encoder</a> and store it in notepad editor for later use

> [!TIP]
> Use Ctrl + F for searching

Navigate to the DPP-Verification directory in insomnia and follow the steps

### Step 1.1 Get the Submodel

- Substitute `<DIGITAL_TWIN_ID_BASE64_ENCODED>` with base64 encoded Digital Twin Id from [here](#get-digital-twin-id).

- Substitute `<DIGITAL_TWIN_SUBMODEL_ID_BASE64_ENCODED>` with base64 encoded Id from [here](#get-submodel-id).

- Substitute `<DIGITAL_TWIN_REGISTRY_URL>` with its value from the given paper.

- Execute the API call.

Example Response:
```json
{
	"endpoints": [
		{
			"interface": "SUBMODEL-3.0",
			"protocolInformation": {
				...
			}
		}
	],
	"idShort": "digitalProductPass",
	"id": "urn:uuid:1a6c2d40-14de-4bb3-8aee-d5f7f01f49e3",
	"semanticId": {
		"type": "ExternalReference",
		"keys": [
			{
				"type": "Submodel",
				"value": "urn:samm:io.catenax.generic.digital_product_passport:5.0.0#DigitalProductPassport"
			}
		]
	},
	"description": [
		{
			"language": "en",
			"text": "Digital Product Pass Submodel"
		}
	],
	"displayName": []
}

```

### Step 1.2 Update the Submodel

- Copy the response from [step 1.1](#step-11-get-the-submodel)

- Paste the response in step 1.2 and replace `<PASTE_RESPONSE_FROM_STEP_1.1>` in Insomnia collection

- Search for the `supplementalSemanticId` and delete the entire line

- Search for the `semanticId` and update it with the following json content:

```json
{
    "type": "ExternalReference",
    "keys": [
        {
            "type": "Submodel",
            "value": "urn:samm:io.catenax.generic.digital_product_passport:5.0.0#DigitalProductPassport"
        },
        {
            "type": "Operation",
            "value": "https://w3c.github.io/vc-jws-2020/contexts/v1/"
        },
        {
            "type": "DataElement",
            "value": "urn:samm:io.catenax.dpp_verification.cdc:1.0.0#CertifiedDataCredential"
        },
        {
            "type": "Entity",
            "value": "https://www.w3.org/ns/credentials/v2"
        }
    ]
}
```

- Substitute `<DIGITAL_TWIN_ID_BASE64_ENCODED>` with base64 encoded Digital Twin Id from [here](#get-digital-twin-id).

- Substitute `<DIGITAL_TWIN_SUBMODEL_ID_BASE64_ENCODED>` with base64 encoded Id from [here](#get-submodel-id).

- Substitute `<DIGITAL_TWIN_REGISTRY_URL>` with its value from the given paper.

- Execute the API call.

### Step 1.3 Issue the Verifiable Credentials

- Copy the DPP aspect model which you created at the beginning at the aspect model creation step (Step 2.1.1 for Explorers) and replace `<DPP_JSON_PAYLOAD_OBJECT>` with the payload that you copied.

- Substitute `<DPP_PROVIDER_WALLET_URL>` with its value given in a paper.

- Execute the API call.


### Step 1.4 Verify the Proof

- Copy the JSON response from the API Step 1.3 in insomnia collection

- Paste the response and replace `<PASTE_RESPONSE_FROM_STEP_1.4>`

- Execute the API call.

If everything works, you will get the following response: 

```json
{
	"message": "Verified Credential is Valid! The proof was verified!",
	"verified": true
}
```

- To see the proof in the application, scan the QR code again or introduce ID in the app.

## Step 1.5 Store the Certified Digital Product Passport

Now that you have successfully managed to `issue/certify a Digital Product Passport` in an own wallet as a provider role and `Verify the integrity of the Digital Product Passport` from a consumer role, also using an own consumer wallet, you shall update the JSON at the data storage:

- Copy the API response from the Step 1.3 and replace `<PASTE_RESPONSE_FROM_STEP_1.3>` with the payload that you copied.

- Substitute the URL placeholders: `<DATA_SERVICE_URL>` from the given paper and `<digitalTwinSubmodelId>` which was used in step 2.1.1 in the collection

- Execute the API call.

Congratulations! You have successfully certified your Digital Product Passport and updated it in the data storage.

Please go back to the [Phase 3 at the main README guide](../README.md#phase-3-data-certification--verification), and scan again your QR code to see the verification status at the application.
