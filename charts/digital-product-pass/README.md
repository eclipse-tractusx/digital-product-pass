# digital-product-pass

![Version: 1.5.0](https://img.shields.io/badge/Version-1.5.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 1.5.0](https://img.shields.io/badge/AppVersion-1.5.0-informational?style=flat-square)

A Helm chart for Tractus-X Digital Product Pass Kubernetes

**Homepage:** <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass>

## Source Code

* <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/charts/digital-product-pass>

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` |  |
| backend | object | `{"digitalTwinRegistry":{"endpoints":{"digitalTwin":"/shell-descriptors","search":"/lookup/shells","subModel":"/submodel-descriptors"},"temporaryStorage":{"enabled":true},"timeouts":{"digitalTwin":20,"negotiation":40,"search":10,"transfer":10}},"discovery":{"bpnDiscovery":{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"},"edcDiscovery":{"key":"bpn"},"hostname":""},"edc":{"apis":{"catalog":"/catalog/request","management":"/management/v2","negotiation":"/contractnegotiations","transfer":"/transferprocesses"},"delay":100,"endpoint":"","participantId":"<Add participant id here>","xApiKey":"<Add API key here>"},"hostname":"localhost","image":{"pullPolicy":"Always","repository":"docker.io/tractusx/digital-product-pass-backend"},"imagePullSecrets":[],"ingress":{"enabled":false,"hosts":[{"host":"localhost","paths":[{"path":"/","pathType":"Prefix"}]}]},"irs":{"enabled":false,"hostname":""},"logging":{"level":{"root":"INFO","utils":"INFO"}},"maxRetries":5,"name":"dpp-backend","passport":{"aspects":["urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport","urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass","urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"]},"process":{"encryptionKey":""},"securityCheck":{"bpn":false,"edc":false,"enabled":false},"serverPort":8888,"service":{"port":8888,"type":"ClusterIP"}}` | Backend configuration |
| backend.digitalTwinRegistry.temporaryStorage | object | `{"enabled":true}` | temporary storage of dDTRs for optimization |
| backend.digitalTwinRegistry.timeouts | object | `{"digitalTwin":20,"negotiation":40,"search":10,"transfer":10}` | timeouts for the digital twin registry async negotiation |
| backend.discovery | object | `{"bpnDiscovery":{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"},"edcDiscovery":{"key":"bpn"},"hostname":""}` | discovery configuration |
| backend.discovery.bpnDiscovery | object | `{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"}` | bpn discovery configuration |
| backend.discovery.edcDiscovery | object | `{"key":"bpn"}` | edc discovery configuration |
| backend.discovery.hostname | string | `""` | discovery finder configuration |
| backend.edc | object | `{"apis":{"catalog":"/catalog/request","management":"/management/v2","negotiation":"/contractnegotiations","transfer":"/transferprocesses"},"delay":100,"endpoint":"","participantId":"<Add participant id here>","xApiKey":"<Add API key here>"}` | in this section we configure the values that are inserted as secrets in the backend |
| backend.edc.endpoint | string | `""` | edc consumer connection configuration |
| backend.edc.participantId | string | `"<Add participant id here>"` | BPN Number |
| backend.edc.xApiKey | string | `"<Add API key here>"` | the secret for assesing the edc management API |
| backend.hostname | string | `"localhost"` | backend hostname (without protocol prefix [DEFAULT HTTPS] for security ) |
| backend.imagePullSecrets | list | `[]` | Existing image pull secret to use to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| backend.ingress | object | `{"enabled":false,"hosts":[{"host":"localhost","paths":[{"path":"/","pathType":"Prefix"}]}]}` | ingress declaration to expose the dpp-backend service |
| backend.irs | object | `{"enabled":false,"hostname":""}` | irs configuration |
| backend.logging.level.root | string | `"INFO"` | general logging level |
| backend.logging.level.utils | string | `"INFO"` | logging for the util components |
| backend.maxRetries | int | `5` | max retries for the backend services |
| backend.passport | object | `{"aspects":["urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport","urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass","urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"]}` | passport data transfer configuration |
| backend.passport.aspects | list | `["urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport","urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass","urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"]` | passport versions and aspects allowed |
| backend.process | object | `{"encryptionKey":""}` | digital twin registry configuration |
| backend.process.encryptionKey | string | `""` | unique sha512 hash key used for the passport encryption |
| backend.securityCheck | object | `{"bpn":false,"edc":false,"enabled":false}` | security configuration |
| backend.serverPort | int | `8888` | configuration of the spring boot server |
| backend.service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service |
| frontend.api | object | `{"delay":1000,"max_retries":30,"timeout":90000}` | api timeouts |
| frontend.api.delay | int | `1000` | delay from getting status |
| frontend.api.max_retries | int | `30` | max retries for getting status |
| frontend.api.timeout | int | `90000` | default timeout  - 90 seconds in milliseconds |
| frontend.backend | object | `{"hostname":""}` | url of the digital product pass backend service |
| frontend.image.pullPolicy | string | `"Always"` |  |
| frontend.image.repository | string | `"docker.io/tractusx/digital-product-pass-frontend"` |  |
| frontend.imagePullSecrets | list | `[]` | Existing image pull secret to use to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| frontend.ingress | object | `{"enabled":false,"hosts":[]}` | ingress declaration to expose the dpp-frontend service |
| frontend.irs | object | `{"maxWaitingTime":30,"requestDelay":30000}` | irs api timeouts |
| frontend.irs.maxWaitingTime | int | `30` | maximum waiting time to get the irs job status |
| frontend.irs.requestDelay | int | `30000` | request timeout delay |
| frontend.name | string | `"dpp-frontend"` |  |
| frontend.portal.hostname | string | `""` |  |
| frontend.service.port | int | `8080` |  |
| frontend.service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service |
| frontend.supportContact.adminEmail | string | `"admin@example.com"` |  |
| name | string | `"digital-product-pass"` |  |
| namespace | string | `""` |  |
| nodeSelector | object | `{}` |  |
| oauth | object | `{"appId":"","bpnCheck":{"bpn":"<Add participant id here>","enabled":false},"hostname":"","onLoad":"login-required","realm":"","roleCheck":{"enabled":false},"techUser":{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}}` | oauth configuration |
| oauth.bpnCheck | object | `{"bpn":"<Add participant id here>","enabled":false}` | configure here the bpn check for the application |
| oauth.bpnCheck.bpn | string | `"<Add participant id here>"` | this bpn needs to be included in the user login information when the check is enabled |
| oauth.hostname | string | `""` | url of the identity provider service |
| oauth.roleCheck | object | `{"enabled":false}` | the role check checks if the user has access roles for the appId   |
| oauth.techUser | object | `{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}` | note: this credentials need to have access to the Discovery Finder, BPN Discovery and EDC Discovery |
| replicaCount | int | `1` |  |
| resources.limits.cpu | string | `"500m"` |  |
| resources.limits.memory | string | `"512Mi"` |  |
| resources.requests.cpu | string | `"250m"` |  |
| resources.requests.memory | string | `"512Mi"` |  |
| tolerations | list | `[]` |  |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.11.0](https://github.com/norwoodj/helm-docs/releases/v1.11.0)
