<!--
#######################################################################

Tractus-X - Digital Product Pass Application 

Copyright (c) 2022, 2024 BMW AG
Copyright (c) 2022, 2024 Henkel AG & Co. KGaA
Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This work is made available under the terms of the
Creative Commons Attribution 4.0 International (CC-BY-4.0) license,
which is available at
https://creativecommons.org/licenses/by/4.0/legalcode.

SPDX-License-Identifier: CC-BY-4.0

#######################################################################
-->

# Identity Access Management (IAM) Setup

This guide describes how to setup a IAM instance locally using docker container and import existing realm.json file.

## Launch keycloak docker container

- Keycloak official image: [jboss/keycloak](https://registry.hub.docker.com/r/jboss/keycloak)

```
docker run --name keycloak -p 8088:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -d jboss/keycloak
```

## Import realm

Import the [realm.json](./realm.json) located in current directory

## Create users

After importing the realm, the users need to be created manually. 

***Example users:***
- **User 1:** company 1 user  \
  **Password:** changeme \
  **Role:** OEM, Dismantler

- **User 2:** company 2 user \
  **Password:** changeme \
  **Role:** Recycler

## Integration with vuejs app

### Enable keycloak configuration

Install the keycloak plugin for vuejs app from ```npm install keycloak-js```

The keycloak configurations are defined in [dpp-frontend/src/services/service.const.js](../../../dpp-frontend/src/services/service.const.js).

### Build and run the app

```
npm install --legacy-peer-deps
npm run serve
```


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
