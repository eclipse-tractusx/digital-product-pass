<!--
  Catena-X - Product Passport Consumer Frontend
 
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

<h1 style="display:flex; align-items: center;"><img src="./docs/catena-x-logo.svg"/>&nbsp;&nbsp;Digital Product Pass Application</h1>

[![Contributors][contributors-shield]][contributors-url]
[![Stargazers][stars-shield]][stars-url]
[![Apache 2.0 License][license-shield]][license-url]
[![Latest Release][release-shield]][release-url]

## Description

The digital product passport  application provides a consumer user interface to request a battery passport from a battery manufacturer using the standardized components and technologies in a Catena-X network. The passport will be displayed in a human-readable from any browser. The data exchange standards given by Catena-X are used to provide the battery passport to different personas (roles) in the network.

In particular, the appliction is used to access the battery passport data provided by battery manufacturer. By scanning QR-code or knowing the manufacturer and battery-ID, a user can request the passport  through **Eclipse Dataspace Connectors (EDCs)** over the Catena-X network. The passport provider will provide data attributes that is only visible to a permitted signed-in user. 

### Software Version
#### Helm Chart Version
<pre id="helm-version"><a href="https://github.com/eclipse-tractusx/digital-product-pass/releases/tag/digital-product-pass-1.0.1">1.0.1</a></pre>
#### Application Version
<pre id="app-version"><a href="https://github.com/eclipse-tractusx/digital-product-pass/releases/tag/v1.0.1">v1.0.1</a></pre>


## Application Preview

Here is a preview from the DPP App UI, where we visualize a test battery passport in this case.

![General Info View](./docs/arc42/GraphicBatteryPassportViewGeneralInfo.png)

> **Note**: For more information check the [documentation section](./docs/)

## Getting Started

To get started you can have a look into our documentation:

| Name                                                                      | Description                                                                                                                                                        |
| ----------------------------------------------------------------          | -----------------------------------------------------------------------------------------------------------------------------------------------------------        |
| [Arc42](./docs/arc42/Arc42.md)                                             | Arc42 of Digital Product Pass                                                                                                                                      |
| [Administration Guide](./docs/admin%20guide/Admin_Guide.md)                  | Administration Guide explaining the infrastructure and how to configure the application                                                                          |
| [Backend Documentation](./consumer-backend/productpass/readme.md)          | Backend documentation Product Passport App                                                                                                                         |
| [Deployment in Hotel Budapest](./deployment/README.md)                     | Technical Guide - Deployment in ArgoCD Hotel Budapest (integration environment)                                                                                    |
| [Docker Overview](./docker/README.md)                                      | Overview on general docker commands                                                                                                                                |
| [Keycloak Overview](./docker/local/Keycloak/README.md)                     | This guide describes how to setup a keycloak instance in local docker container and import existing realm.json file.                                               |
| [Short Introduction into the project](./docs/GETTING-STARTED.md)           | Battery Pass Allpication infrastructure, installation guide, technical usage guide                                                                                 |
| [Code Scaning with Kics and Trivy](./docs/IaC.md)                          | Infrastructure As Code (IaC) with KICS intends to find security vulnerabilities by scanning the code and upload results to the security dashboard in github        |
| [Release Guidelines](./docs/RELEASE.md)                                     | Product Battery Pass Consumer App Release Guide                                                                                                                    |
| [Secret Management](./docs/SECRETS_MANAGEMENT.md)                          | Secrets management with CX HashiCorp Vault and ArgoCD Vault Plugin (AVP) - client credentials, database passwords, access tokens                                   |
| [Cypress Overview](./docs/cypress/CYPRESS.md)                              | Documentation for Battery Passport App E2E Cypress test                                                                                                            |
| [End User Manual](./docs/user%20manual/User%20Manual%20Product%20Viewer%20App.md)             | End User Manual Product Viewer App                                                                                                                                  |
| [Postman Overview](./postman/README.md)                                    | Technical guide depicts the battery pass end-to-end API calls through the postman REST client                                                                      |
| [Changelog](./CHANGELOG.md)                                                | Changelog                                                                                                                                                          |
| [Helm Charts](https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass)                                                | Project's Helm Charts                                                                                                                                                          |



## Base Images
| Language | Container Base Image |
| :------- | :------------------- |
| Java / JVM based   | [Eclipse Temurin](https://hub.docker.com/_/eclipse-temurin) |
| JS frontends       | [Node.JS](https://hub.docker.com/_/node)  <br/> [Nginx](https://hub.docker.com/r/nginxinc/nginx-unprivileged) |
      

## Installation
[INSTALL](./INSTALL.md)

## License

[Apache-2.0](https://raw.githubusercontent.com/eclipse-tractusx/digital-product-pass/main/LICENSE)

## Notice for Docker images
DockerHub:

- https://hub.docker.com/r/tractusx/digital-product-pass-frontend
- https://hub.docker.com/r/tractusx/digital-product-pass-backend

Eclipse Tractus-X product(s) installed within the image:

- GitHub: https://github.com/eclipse-tractusx/digital-product-pass
- Project home: https://projects.eclipse.org/projects/automotive.tractusx
- Dockerfiles: 
    - Frontend: https://github.com/eclipse-tractusx/digital-product-pass/blob/main/Dockerfile
    - Backend: https://github.com/eclipse-tractusx/digital-product-pass/blob/main/consumer-backend/productpass/Dockerfile
- Project License: [Apache License, Version 2.0](https://raw.githubusercontent.com/eclipse-tractusx/digital-product-pass/main/LICENSE)


**Used base image**
- [node:lts-alpine](https://github.com/nodejs/docker-node)
- [nginxinc/nginx-unprivileged:stable-alpine](https://github.com/nginxinc/docker-nginx-unprivileged/blob/main/Dockerfile-alpine.template)
- [eclipse-temurin:19-alpine](https://github.com/adoptium/containers)
- Official DockerHub pages:
    - Node: https://hub.docker.com/_/node
    - Nginxinc/nginx-unprivileged: https://hub.docker.com/r/nginxinc/nginx-unprivileged
    - Eclipse Temurin: https://hub.docker.com/_/eclipse-temurin  
- Eclipse Temurin Project: https://projects.eclipse.org/projects/adoptium.temurin  
- Additional information about images:
    - Node: https://github.com/docker-library/repo-info/tree/master/repos/node
    - Nginxinc/nginx-unprivileged: https://github.com/nginxinc/docker-nginx-unprivileged
    - Eclipse Temurin: https://github.com/docker-library/repo-info/tree/master/repos/eclipse-temurin

As with all Docker images, these likely also contain other software which may be under other licenses 
(such as Bash, etc. from the base distribution, along with any direct or indirect dependencies of the primary software being contained).

As for any pre-built image usage, it is the image user's responsibility to ensure that any use of this image complies with any relevant licenses for all software contained within.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/eclipse-tractusx/digital-product-pass.svg?style=for-the-badge

[contributors-url]: https://github.com/eclipse-tractusx/digital-product-pass/graphs/contributors

[stars-shield]: https://img.shields.io/github/stars/eclipse-tractusx/digital-product-pass.svg?style=for-the-badge

[stars-url]: https://github.com/eclipse-tractusx/digital-product-pass/stargazers

[license-shield]: https://img.shields.io/github/license/eclipse-tractusx/digital-product-pass.svg?style=for-the-badge

[license-url]: https://github.com/eclipse-tractusx/digital-product-pass/blob/main/LICENSE

[release-shield]: https://img.shields.io/github/v/release/eclipse-tractusx/digital-product-pass.svg?style=for-the-badge

[release-url]: https://github.com/eclipse-tractusx/digital-product-pass/releases