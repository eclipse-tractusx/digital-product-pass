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

## Notice for Docker images
DockerHub:
- https://hub.docker.com/r/tractusx/digital-product-pass-frontend
- https://hub.docker.com/r/tractusx/digital-product-pass-backend
- https://hub.docker.com/r/tractusx/digital-product-pass-verification-wallet
  
Eclipse Tractus-X product(s) installed within the image:

- GitHub: https://github.com/eclipse-tractusx/digital-product-pass
- Project home: https://projects.eclipse.org/projects/automotive.tractusx
- Dockerfiles: 
    - Frontend: https://github.com/eclipse-tractusx/digital-product-pass/blob/main/Dockerfile
    - Backend: https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-backend/digitalproductpass/Dockerfile
    - DPP Verification Simple Wallet: https://github.com/eclipse-tractusx/digital-product-pass/blob/main/dpp-verification/simple-wallet/Dockerfile
- Project License: [Apache License, Version 2.0](https://raw.githubusercontent.com/eclipse-tractusx/digital-product-pass/main/LICENSE)


**Used base image**
- [alpine:3.19](https://github.com/alpinelinux/docker-alpine)
- [node:lts-alpine](https://github.com/nodejs/docker-node)
- [nginxinc/nginx-unprivileged:stable-alpine](https://github.com/nginxinc/docker-nginx-unprivileged/blob/main/Dockerfile-alpine.template)
- [eclipse-temurin:19-alpine](https://github.com/adoptium/containers)
- Official DockerHub pages:
    - Alpine: https://hub.docker.com/_/alpine
    - Node: https://hub.docker.com/_/node
    - Nginxinc/nginx-unprivileged: https://hub.docker.com/r/nginxinc/nginx-unprivileged
    - Eclipse Temurin: https://hub.docker.com/_/eclipse-temurin  
- Eclipse Temurin Project: https://projects.eclipse.org/projects/adoptium.temurin  
- Additional information about images:
    - alpinelinux/docker-alpine:3.19: https://github.com/alpinelinux/docker-alpine/blob/v3.19/x86_64/Dockerfile
    - Node: https://github.com/docker-library/repo-info/tree/master/repos/node
    - Nginxinc/nginx-unprivileged: https://github.com/nginxinc/docker-nginx-unprivileged
    - Eclipse Temurin: https://github.com/docker-library/repo-info/tree/master/repos/eclipse-temurin

As with all Docker images, these likely also contain other software which may be under other licenses
(such as Bash, etc. from the base distribution, along with any direct or indirect dependencies of the primary software being contained).

As for any pre-built image usage, it is the image user's responsibility to ensure that any use of this image complies with any relevant licenses for all software contained within.
