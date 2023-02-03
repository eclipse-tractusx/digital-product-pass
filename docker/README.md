<!--
 Copyright 2023 BASF SE, BMW AG, Henkel AG & Co. KGaA
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
     http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
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
