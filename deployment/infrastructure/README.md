<!--
  Catena-X - Product Passport Consumer Application
 
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

# Test data Uploader
## TL;DR 

Use the following script to upload test data to the provider edc and registry:
```bash
./upload-testdata.sh -s <submodel-server-url> -e <provider-edc-url> -a <aas-registry-url> -k <edc-api-key> -b <bpn-number>
```

#### Script Parameters:
| Parameter  | Description                  | Example value                                                           | Required/Optionl |
| :---:      | :---                         | :---                                                                    | :---:            |
| -s         | Submodel server URL          | https://materialpass.int.demo.catena-x.net/provider_backend             | Required         | 
| -e         | Provider control plane URL   | https://materialpass.int.demo.catena-x.net/BPNL000000000000             | Required         |
| -a         | AAS registry URL             | https://materialpass.int.demo.catena-x.net/semantics/registry/api/v3.0  | Required         |
| -k         | EDC API Key                  | xxxxxxxx                                                                | Required         |
| -b         | BPN number                   | BPNL00000000CBA5                                                        | Required         |
|            |                              |                                                                         |                  |


Use the following script to upload specific test data file

### Digital Product Pass (DPP)

```bash
./upload-dpp-data.sh <submodel-server-url> <provider-edc-url> <aas-registry-url> <edc-api-key> <bpn-number>
```

### Battery Pass

```bash
./upload-batterypass-data.sh <submodel-server-url> <provider-edc-url> <aas-registry-url> <edc-api-key> <bpn-number>
```

### Transmission Pass

```bash
./upload-transmissionpass-data.sh <submodel-server-url> <provider-edc-url> <aas-registry-url> <edc-api-key> <bpn-number>
```

### Secondary Material Content (SMC)

```bash
./upload-smc-data.sh <submodel-server-url> <provider-edc-url> <aas-registry-url> <edc-api-key> <bpn-number>
```

### Delete test data

Use the following script to remove all test data from the provider edc and registry:
```bash
./delete-testdata.sh -e <provider-edc-url> -a <aas-registry-url> -k <edc-api-key>
```


