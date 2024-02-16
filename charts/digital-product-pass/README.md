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

# digital-product-pass

![Version: 2.1.2](https://img.shields.io/badge/Version-2.1.2-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 2.1.2](https://img.shields.io/badge/AppVersion-2.1.2-informational?style=flat-square)

A Helm chart for Tractus-X Digital Product Pass Kubernetes

**Homepage:** <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass>

## Source Code

* <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass>

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` |  |
| backend | object | `{"digitalTwinRegistry":{"endpoints":{"digitalTwin":"/shell-descriptors","search":"/lookup/shells","subModel":"/submodel-descriptors"},"temporaryStorage":{"enabled":true,"lifetime":12},"timeouts":{"digitalTwin":20,"negotiation":40,"search":50,"transfer":10}},"discovery":{"bpnDiscovery":{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"},"edcDiscovery":{"key":"bpn"},"hostname":""},"edc":{"apis":{"catalog":"/catalog/request","management":"/management/v2","negotiation":"/contractnegotiations","transfer":"/transferprocesses"},"delay":100,"hostname":"","participantId":"<Add participant id here>","xApiKey":"<Add API key here>"},"hostname":"localhost","image":{"pullPolicy":"Always","repository":"docker.io/tractusx/digital-product-pass-backend"},"imagePullSecrets":[],"ingress":{"enabled":false,"hosts":[{"host":"localhost","paths":[{"path":"/","pathType":"Prefix"}]}]},"irs":{"enabled":false,"hostname":""},"logging":{"level":{"root":"INFO","utils":"INFO"}},"maxRetries":5,"name":"dpp-backend","passport":{"aspects":["urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport","urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass","urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass","urn:samm:io.catenax.generic.digital_product_passport:2.0.0#DigitalProductPassport"]},"podSecurityContext":{"fsGroup":3000,"runAsGroup":3000,"runAsUser":1000,"seccompProfile":{"type":"RuntimeDefault"}},"process":{"encryptionKey":""},"securityCheck":{"bpn":false,"edc":false},"securityContext":{"allowPrivilegeEscalation":false,"capabilities":{"add":[],"drop":["ALL"]},"readOnlyRootFilesystem":true,"runAsGroup":3000,"runAsNonRoot":true,"runAsUser":1000},"serverPort":8888,"service":{"port":8888,"type":"ClusterIP"}}` | Backend configuration |
| backend.digitalTwinRegistry.temporaryStorage | object | `{"enabled":true,"lifetime":12}` | temporary storage of dDTRs for optimization |
| backend.digitalTwinRegistry.temporaryStorage.lifetime | int | `12` | lifetime of the temporaryStorage in hours |
| backend.digitalTwinRegistry.timeouts | object | `{"digitalTwin":20,"negotiation":40,"search":50,"transfer":10}` | timeouts for the digital twin registry async negotiation |
| backend.discovery | object | `{"bpnDiscovery":{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"},"edcDiscovery":{"key":"bpn"},"hostname":""}` | discovery configuration |
| backend.discovery.bpnDiscovery | object | `{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"}` | bpn discovery configuration |
| backend.discovery.edcDiscovery | object | `{"key":"bpn"}` | edc discovery configuration |
| backend.discovery.hostname | string | `""` | discovery finder configuration |
| backend.edc | object | `{"apis":{"catalog":"/catalog/request","management":"/management/v2","negotiation":"/contractnegotiations","transfer":"/transferprocesses"},"delay":100,"hostname":"","participantId":"<Add participant id here>","xApiKey":"<Add API key here>"}` | in this section we configure the values that are inserted as secrets in the backend |
| backend.edc.hostname | string | `""` | edc consumer connection configuration |
| backend.edc.participantId | string | `"<Add participant id here>"` | BPN Number |
| backend.edc.xApiKey | string | `"<Add API key here>"` | the secret for assesing the edc management API |
| backend.hostname | string | `"localhost"` | backend hostname (without protocol prefix [DEFAULT HTTPS] for security ) |
| backend.imagePullSecrets | list | `[]` | Existing image pull secret to use to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| backend.ingress | object | `{"enabled":false,"hosts":[{"host":"localhost","paths":[{"path":"/","pathType":"Prefix"}]}]}` | ingress declaration to expose the dpp-backend service |
| backend.irs | object | `{"enabled":false,"hostname":""}` | irs configuration |
| backend.logging.level.root | string | `"INFO"` | general logging level |
| backend.logging.level.utils | string | `"INFO"` | logging for the util components |
| backend.maxRetries | int | `5` | max retries for the backend services |
| backend.podSecurityContext | object | `{"fsGroup":3000,"runAsGroup":3000,"runAsUser":1000,"seccompProfile":{"type":"RuntimeDefault"}}` | The [pod security context](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-the-security-context-for-a-pod) defines privilege and access control settings for a Pod within the deployment |
| backend.podSecurityContext.fsGroup | int | `3000` | The owner for volumes and any files created within volumes will belong to this guid |
| backend.podSecurityContext.runAsGroup | int | `3000` | Processes within a pod will belong to this guid |
| backend.podSecurityContext.runAsUser | int | `1000` | Runs all processes within a pod with a special uid |
| backend.podSecurityContext.seccompProfile.type | string | `"RuntimeDefault"` | Restrict a Container's Syscalls with seccomp |
| backend.process | object | `{"encryptionKey":""}` | digital twin registry configuration |
| backend.process.encryptionKey | string | `""` | unique sha512 hash key used for the passport encryption |
| backend.securityCheck | object | `{"bpn":false,"edc":false}` | security configuration |
| backend.securityContext.allowPrivilegeEscalation | bool | `false` | Controls [Privilege Escalation](https://kubernetes.io/docs/concepts/security/pod-security-policy/#privilege-escalation) enabling setuid binaries changing the effective user ID |
| backend.securityContext.capabilities.add | list | `[]` | Specifies which capabilities to add to issue specialized syscalls |
| backend.securityContext.capabilities.drop | list | `["ALL"]` | Specifies which capabilities to drop to reduce syscall attack surface |
| backend.securityContext.readOnlyRootFilesystem | bool | `true` | Whether the root filesystem is mounted in read-only mode |
| backend.securityContext.runAsGroup | int | `3000` | The owner for volumes and any files created within volumes will belong to this guid |
| backend.securityContext.runAsNonRoot | bool | `true` | Requires the container to run without root privileges |
| backend.securityContext.runAsUser | int | `1000` | The container's process will run with the specified uid |
| backend.serverPort | int | `8888` | configuration of the spring boot server |
| backend.service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service |
| frontend.api | object | `{"delay":1000,"max_retries":30,"timeout":{"decline":20000,"negotiate":40000,"search":60000}}` | api timeouts |
| frontend.api.delay | int | `1000` | delay from getting status |
| frontend.api.max_retries | int | `30` | max retries for getting status |
| frontend.api.timeout | object | `{"decline":20000,"negotiate":40000,"search":60000}` | default timeout  - 90 seconds in milliseconds |
| frontend.backend | object | `{"hostname":""}` | url of the digital product pass backend service |
| frontend.image.pullPolicy | string | `"Always"` |  |
| frontend.image.repository | string | `"docker.io/tractusx/digital-product-pass-frontend"` |  |
| frontend.imagePullSecrets | list | `[]` | Existing image pull secret to use to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| frontend.ingress | object | `{"enabled":false,"hosts":[]}` | ingress declaration to expose the dpp-frontend service |
| frontend.irs | object | `{"maxWaitingTime":30,"requestDelay":30000}` | irs api timeouts |
| frontend.irs.maxWaitingTime | int | `30` | maximum waiting time to get the irs job status |
| frontend.irs.requestDelay | int | `30000` | request timeout delay |
| frontend.name | string | `"dpp-frontend"` |  |
| frontend.negotiation.autoSign | bool | `true` |  |
| frontend.podSecurityContext | object | `{"fsGroup":3000,"runAsGroup":3000,"runAsUser":1000,"seccompProfile":{"type":"RuntimeDefault"}}` | The [pod security context](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-the-security-context-for-a-pod) defines privilege and access control settings for a Pod within the deployment |
| frontend.podSecurityContext.fsGroup | int | `3000` | The owner for volumes and any files created within volumes will belong to this guid |
| frontend.podSecurityContext.runAsGroup | int | `3000` | Processes within a pod will belong to this guid |
| frontend.podSecurityContext.runAsUser | int | `1000` | Runs all processes within a pod with a special uid |
| frontend.podSecurityContext.seccompProfile.type | string | `"RuntimeDefault"` | Restrict a Container's Syscalls with seccomp |
| frontend.portal.hostname | string | `""` |  |
| frontend.securityContext.allowPrivilegeEscalation | bool | `false` | Controls [Privilege Escalation](https://kubernetes.io/docs/concepts/security/pod-security-policy/#privilege-escalation) enabling setuid binaries changing the effective user ID |
| frontend.securityContext.capabilities.add | list | `[]` | Specifies which capabilities to add to issue specialized syscalls |
| frontend.securityContext.capabilities.drop | list | `["ALL"]` | Specifies which capabilities to drop to reduce syscall attack surface |
| frontend.securityContext.readOnlyRootFilesystem | bool | `false` | Whether the root filesystem is mounted in read-only mode |
| frontend.securityContext.runAsGroup | int | `3000` | The owner for volumes and any files created within volumes will belong to this guid |
| frontend.securityContext.runAsNonRoot | bool | `true` | Requires the container to run without root privileges |
| frontend.securityContext.runAsUser | int | `1000` | The container's process will run with the specified uid |
| frontend.service.port | int | `8080` |  |
| frontend.service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service |
| frontend.supportContact.adminEmail | string | `"admin@example.com"` |  |
| name | string | `"digital-product-pass"` |  |
| namespace | string | `""` |  |
| nodeSelector | object | `{}` |  |
| oauth | object | `{"appId":"<app-id>","bpnCheck":{"bpn":"<Add participant id here>","enabled":false},"hostname":"","onLoad":"login-required","realm":"<realm>","roleCheck":{"enabled":false},"techUser":{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}}` | oauth configuration |
| oauth.bpnCheck | object | `{"bpn":"<Add participant id here>","enabled":false}` | configure here the bpn check for the application |
| oauth.bpnCheck.bpn | string | `"<Add participant id here>"` | this bpn needs to be included in the user login information when the check is enabled |
| oauth.hostname | string | `""` | url of the identity provider service |
| oauth.roleCheck | object | `{"enabled":false}` | the role check checks if the user has access roles for the appId |
| oauth.techUser | object | `{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}` | note: this credentials need to have access to the Discovery Finder, BPN Discovery and EDC Discovery |
| replicaCount | int | `1` |  |
| resources.limits.cpu | string | `"500m"` |  |
| resources.limits.memory | string | `"512Mi"` |  |
| resources.requests.cpu | string | `"250m"` |  |
| resources.requests.memory | string | `"512Mi"` |  |
| tolerations | list | `[]` |  |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.11.0](https://github.com/norwoodj/helm-docs/releases/v1.11.0)
