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

# digital-product-pass

![Version: 1.0.0](https://img.shields.io/badge/Version-1.0.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 1.0.0-alpha](https://img.shields.io/badge/AppVersion-1.0.0--alpha-informational?style=flat-square)

A Helm chart for Kubernetes

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` |  |
| backend.application.yml | string | `"spring:\n  name: 'Catena-X Product Passport Consumer Backend'\n  main:\n    allow-bean-definition-overriding: true\n  devtools:\n    add-properties: false\n  jackson:\n    serialization:\n      indent_output: true\n\nlogging:\n  level:\n    root: INFO\n    utils: INFO\n\nconfiguration:\n  maxRetries: 5\n\n  keycloak:\n    realm: CX-Central\n    resource: Cl13-CX-Battery\n    tokenUri: 'https://centralidp.dev.demo.catena-x.net/auth/realms/CX-Central/protocol/openid-connect/token'\n    userInfoUri: 'https://centralidp.dev.demo.catena-x.net/auth/realms/CX-Central/protocol/openid-connect/userinfo'\n\n  edc:\n    endpoint: 'https://materialpass.dev.demo.catena-x.net/consumer'\n    management: '/management/v2'\n    catalog: '/catalog/request'\n    negotiation: '/contractnegotiations'\n    transfer: '/transferprocesses'\n    receiverEndpoint: 'https://materialpass.dev.demo.catena-x.net/endpoint'\n\n  process:\n    store: true\n    dir: 'process'\n    indent: true\n    signKey: ''\n\n  endpoints:\n    registryUrl: 'https://semantics.dev.demo.catena-x.net'\n\n  passport:\n    dataTransfer:\n      encrypt: true\n      indent: true\n      dir: \"data/transfer\"\n    versions:\n      - 'v3.0.1'\n\n  vault:\n    type: 'local'\n    file: 'vault.token.yml'\n    pathSep: \".\"\n    prettyPrint: true\n    indent: 2\n    defaultValue: '<Add secret value here>'\n    attributes:\n      - \"client.id\"\n      - \"client.secret\"\n      - \"edc.apiKey\"\n    - \"edc.participantId\"\n\nserver:\n  error:\n    include-message: ALWAYS\n    include-binding-errors: ALWAYS\n    include-stacktrace: ON_PARAM\n    include-exception: false\n  port: 8888\n  tomcat:\n    max-connections: 10000"` |  |
| backend.avp.helm.clientId | string | `"Add your secret here"` |  |
| backend.avp.helm.clientSecret | string | `"Add your secret here"` |  |
| backend.avp.helm.participantId | string | `"Add your secret here"` |  |
| backend.avp.helm.xApiKey | string | `"Add your secret here"` |  |
| backend.image.pullPolicy | string | `"Always"` |  |
| backend.image.repository | string | `"docker.io/tractusx/digital-product-pass-backend"` |  |
| backend.imagePullSecrets | list | `[]` |  |
| backend.ingress.enabled | bool | `false` |  |
| backend.name | string | `"dpp-backend"` |  |
| backend.service.port | int | `8888` |  |
| backend.service.type | string | `"ClusterIP"` |  |
| frontend.avp.helm.clientId | string | `"Add your secret here"` |  |
| frontend.avp.helm.clientSecret | string | `"Add your secret here"` |  |
| frontend.avp.helm.xApiKey | string | `"Add your secret here"` |  |
| frontend.image.pullPolicy | string | `"Always"` |  |
| frontend.image.repository | string | `"docker.io/tractusx/digital-product-pass-frontend"` |  |
| frontend.imagePullSecrets | list | `[]` |  |
| frontend.ingress.enabled | bool | `false` |  |
| frontend.ingress.hosts[0].host | string | `"materialpass.dev.demo.catena-x.net"` |  |
| frontend.ingress.hosts[0].paths[0].path | string | `"/passport(/|$)(.*)"` |  |
| frontend.ingress.hosts[0].paths[0].pathType | string | `"Prefix"` |  |
| frontend.name | string | `"dpp-frontend"` |  |
| frontend.productpass.api.delay | int | `1000` |  |
| frontend.productpass.api.max_retries | int | `20` |  |
| frontend.productpass.api.timeout | int | `60000` |  |
| frontend.productpass.backend_url | string | `"materialpass.dev.demo.catena-x.net"` |  |
| frontend.productpass.idp_url | string | `"centralidp.dev.demo.catena-x.net/auth/"` |  |
| frontend.productpass.keycloak.clientId | string | `"Cl13-CX-Battery"` |  |
| frontend.productpass.keycloak.onLoad | string | `"login-required"` |  |
| frontend.productpass.keycloak.realm | string | `"CX-Central"` |  |
| frontend.productpass.passport.version | string | `"v3.0.1"` |  |
| frontend.service.port | int | `8080` |  |
| frontend.service.type | string | `"ClusterIP"` |  |
| name | string | `"digital-product-pass"` |  |
| namespace | string | `"product-material-pass"` |  |
| nodeSelector | object | `{}` |  |
| replicaCount | int | `1` |  |
| resources.limits.cpu | string | `"500m"` |  |
| resources.limits.memory | string | `"512Mi"` |  |
| resources.requests.cpu | string | `"250m"` |  |
| resources.requests.memory | string | `"512Mi"` |  |
| tolerations | list | `[]` |  |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.11.0](https://github.com/norwoodj/helm-docs/releases/v1.11.0)
