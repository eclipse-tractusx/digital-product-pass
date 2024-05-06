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
