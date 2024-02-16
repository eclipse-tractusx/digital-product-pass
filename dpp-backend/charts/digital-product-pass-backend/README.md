<!-- 
  Catena-X - Digital Product Passport Application 
 
  Copyright (c) 2022, 2024 BASF SE, BMW AG, Henkel AG & Co. KGaA
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

# digital-product-pass-backend

![Version: 2.1.2](https://img.shields.io/badge/Version-2.1.2-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 2.1.2](https://img.shields.io/badge/AppVersion-2.1.2-informational?style=flat-square)

A Helm chart for Tractus-X Digital Product Pass Backend Kubernetes

## TL;DR 

### Install

```bash
cd backend/charts/digital-product-pass-backend
helm install digital-product-pass-backend -f ./values.yaml -f ./values-int.yaml
```

> **NOTE**: This command will deploy the backend application.

### Exposing ports

Once the application is running, the certain ports need to be exposed to access the backend outside the Kubernetes cluster.

### Get pod name
Search for the application name:

```bash
kubectl get pods --no-headers |  awk '{if ($1 ~ "dpp-backend-*") print $1}'
```
Copy the pod name with the prefix `dpp-backend-*`

### Port forwarding

```bash
kubectl port-forward dpp-backend-* 8888:8888
```

> **NOTE**: The default port set is `8888` however it can be changed in the configuration.

### Check if the application is running

Open the web browser with the following url to check the health status:
```
localhost:8888/health

**Homepage:** <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-backend/charts/digital-product-pass-backend>

## Source Code

* <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-backend/charts/digital-product-pass-backend>

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` |  |
| autoscaling.enabled | bool | `false` |  |
| autoscaling.maxReplicas | int | `100` |  |
| autoscaling.minReplicas | int | `1` |  |
| autoscaling.targetCPUUtilizationPercentage | int | `80` |  |
| digitalTwinRegistry.endpoints.digitalTwin | string | `"/shell-descriptors"` |  |
| digitalTwinRegistry.endpoints.search | string | `"/lookup/shells"` |  |
| digitalTwinRegistry.endpoints.subModel | string | `"/submodel-descriptors"` |  |
| digitalTwinRegistry.temporaryStorage | object | `{"enabled":true,"lifetime":12}` | temporary storage of dDTRs for optimization |
| digitalTwinRegistry.timeouts | object | `{"digitalTwin":20,"negotiation":40,"search":50,"transfer":10}` | timeouts for the digital twin registry async negotiation |
| discovery | object | `{"bpnDiscovery":{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"},"edcDiscovery":{"key":"bpn"},"hostname":""}` | discovery configuration |
| discovery.bpnDiscovery | object | `{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"}` | bpn discovery configuration |
| discovery.edcDiscovery | object | `{"key":"bpn"}` | edc discovery configuration |
| discovery.hostname | string | `""` | discovery finder configuration |
| edc | object | `{"apis":{"catalog":"/catalog/request","management":"/management/v2","negotiation":"/contractnegotiations","transfer":"/transferprocesses"},"delay":100,"endpoint":"","participantId":"<Add participant id here>","xApiKey":"<Add API key here>"}` | in this section we configure the values that are inserted as secrets in the backend |
| edc.endpoint | string | `""` | edc consumer connection configuration |
| edc.participantId | string | `"<Add participant id here>"` | BPN Number |
| edc.xApiKey | string | `"<Add API key here>"` | the secret for assesing the edc management API |
| hostname | string | `"localhost"` | backend hostname (without protocol prefix [DEFAULT HTTPS] for security ) |
| image.pullPolicy | string | `"Always"` |  |
| image.repository | string | `"docker.io/tractusx/digital-product-pass-backend"` |  |
| imagePullSecrets | list | `[]` | Existing image pull secret to use to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| ingress | object | `{"enabled":false,"hosts":[{"host":"localhost","paths":[{"path":"/","pathType":"Prefix"}]}]}` | ingress declaration to expose the dpp-backend service |
| irs | object | `{"enabled":false,"hostname":""}` | irs configuration |
| logging.level.root | string | `"INFO"` | general logging level |
| logging.level.utils | string | `"INFO"` | logging for the util components |
| maxRetries | int | `5` | max retries for the backend services |
| name | string | `"dpp-backend"` |  |
| namespace | string | `""` |  |
| nodeSelector | object | `{}` |  |
| oauth | object | `{"appId":"<app-id>","bpnCheck":{"bpn":"<Add participant id here>","enabled":false},"hostname":"","onLoad":"login-required","realm":"<realm>","roleCheck":{"enabled":false},"techUser":{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}}` | oauth configuration |
| oauth.bpnCheck | object | `{"bpn":"<Add participant id here>","enabled":false}` | configure here the bpn check for the application |
| oauth.bpnCheck.bpn | string | `"<Add participant id here>"` | this bpn needs to be included in the user login information when the check is enabled |
| oauth.hostname | string | `""` | url of the identity provider service |
| oauth.roleCheck | object | `{"enabled":false}` | the role check checks if the user has access roles for the appId |
| oauth.techUser | object | `{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}` | note: this credentials need to have access to the Discovery Finder, BPN Discovery and EDC Discovery |
| passport.aspects[0] | string | `"urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"` |  |
| passport.aspects[1] | string | `"urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass"` |  |
| passport.aspects[2] | string | `"urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"` |  |
| passport.aspects[3] | string | `"urn:samm:io.catenax.generic.digital_product_passport:2.0.0#DigitalProductPassport"` |  |
| podAnnotations | object | `{}` |  |
| podSecurityContext.fsGroup | int | `3000` |  |
| podSecurityContext.runAsUser | int | `1000` |  |
| process | object | `{"encryptionKey":""}` | digital twin registry configuration |
| process.encryptionKey | string | `""` | unique sha512 hash key used for the passport encryption |
| replicaCount | int | `1` |  |
| resources.limits.cpu | string | `"500m"` |  |
| resources.limits.memory | string | `"512Mi"` |  |
| resources.requests.cpu | string | `"250m"` |  |
| resources.requests.memory | string | `"512Mi"` |  |
| securityCheck | object | `{"bpn":false,"edc":false}` | security configuration |
| securityContext.allowPrivilegeEscalation | bool | `false` |  |
| securityContext.readOnlyRootFilesystem | bool | `true` |  |
| securityContext.runAsGroup | int | `3000` |  |
| securityContext.runAsNonRoot | bool | `true` |  |
| securityContext.runAsUser | int | `1000` |  |
| serverPort | int | `8888` | configuration of the spring boot server |
| service.port | int | `8888` |  |
| service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service |
| serviceAccount.annotations | object | `{}` |  |
| serviceAccount.create | bool | `true` |  |
| serviceAccount.name | string | `""` |  |
| tolerations | list | `[]` |  |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.11.0](https://github.com/norwoodj/helm-docs/releases/v1.11.0)
