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

# Test data Uploader
## TL;DR 

- Configure the following script parameters (shown in below table) in `upload-testdata.sh` script.
- The `testdata-payload.json` file contains structured data for the edc policy and digital twins.
- Run the following script to upload test data to the provider setup:
```bash
./upload-testdata.sh
```

#### Script Parameters:
| Parameter  | Description                  | Example value                                                           | Required/Optionl |
| :---:      | :---                         | :---                                                                    | :---:            |
| -s         | Submodel server URL          | https://materialpass.dev.demo.catena-x.net/provider_backend             | Required         | 
| -e         | Provider control plane URL   | https://materialpass.dev.demo.catena-x.net/BPNL000000000000             | Required         |
| -a         | AAS registry URL             | https://materialpass.dev.demo.catena-x.net/semantics/registry/api/v3.0  | Required         |
| -k         | EDC API Key                  | xxxxxxxx                                                                | Required         |
| -f         | input JSON testdata file     | ./testdata/testdata-payload.json                                        | Required         |
|            |                              |                                                                         |                  |


### Delete test data

Use the following script to remove all test data from the provider edc and registry:
```bash
./delete-testdata.sh -e <provider-edc-url> -a <aas-registry-url> -k <edc-api-key>
```


> **_NOTE:_**
*It might be the case that some of the assets could not be deleted through the deletion script, because they were already used in the past contract negotiations and may still exist in contract agreement references. In that case, the persistent storage of the provider EDC must to be killed and EDC deployment must be restarted.*
