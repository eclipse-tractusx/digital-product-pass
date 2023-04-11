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

## General Docker commands

### Build Image
```bash
docker build -t <IMAGE_NAME>:<IMAGE_TAG> .
```

### Run Container
```bash
docker run -p <HOST_PORT>:<CONTAINER_PORT> --name <CONTAINER_NAME> -d <IMAGE_NAME>:<IMAGE_TAG>
```

### Tag Image
```bash
docker tag <IMAGE_NAME>:<IMAGE_TAG> <REGISTRY>/<IMAGE_NAME>:<IMAGE_TAG>
```

### Push Image
```bash
docker push <REGISTRY>/<IMAGE_NAME>:<IMAGE_TAG>
```

### Stop Container
```bash
docker stop <CONTAINER_NAME>;
```

### Remove Container
```bash
docker rm <CONTAINER_NAME>;
```

### Java Remote Debugging:
Add this parameter when running docker run:

```bash
-e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000" -p 8000:8000
```
