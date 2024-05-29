# digital-product-pass-backend

![Version: 3.0.0](https://img.shields.io/badge/Version-3.0.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 3.0.0](https://img.shields.io/badge/AppVersion-3.0.0-informational?style=flat-square)

A Helm chart for Tractus-X Digital Product Pass Backend Kubernetes

**Homepage:** <https://github.com/eclipse-tractusx/digital-product-pass/tree/main/dpp-backend/charts/digital-product-pass-backend>


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
```

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
| digitalTwinRegistry.policyCheck | object | `{"enabled":true,"policies":[{"obligation":[],"permission":[{"action":"USE","constraints":[{"leftOperand":"cx-policy:Membership","operator":"odrl:eq","rightOperand":"active"},{"leftOperand":"cx-policy:UsagePurpose","operator":"odrl:eq","rightOperand":"cx.core.digitalTwinRegistry:1"}],"logicalConstraint":"odrl:and"}],"prohibition":[]}],"strictMode":false}` | policy configuration for the digital twin assets in the edc catalog |
| digitalTwinRegistry.policyCheck.enabled | bool | `true` | condition to enable and disable the policy check |
| digitalTwinRegistry.policyCheck.policies | list | `[{"obligation":[],"permission":[{"action":"USE","constraints":[{"leftOperand":"cx-policy:Membership","operator":"odrl:eq","rightOperand":"active"},{"leftOperand":"cx-policy:UsagePurpose","operator":"odrl:eq","rightOperand":"cx.core.digitalTwinRegistry:1"}],"logicalConstraint":"odrl:and"}],"prohibition":[]}]` | list of allowed policies that can be selected from the edc catalog in negotiations |
| digitalTwinRegistry.policyCheck.strictMode | bool | `false` | the strict mode is quicker (uses hashes) and requires less computation complexity, the default mode is comparing against every single object value |
| digitalTwinRegistry.temporaryStorage | object | `{"enabled":true,"lifetime":12}` | temporary storage of dDTRs for optimization |
| digitalTwinRegistry.timeouts | object | `{"digitalTwin":40,"negotiation":60,"search":50,"transfer":20}` | timeouts for the digital twin registry async negotiation |
| discovery | object | `{"bpnDiscovery":{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"},"edcDiscovery":{"key":"bpn"},"hostname":""}` | discovery configuration |
| discovery.bpnDiscovery | object | `{"key":"manufacturerPartId","path":"/api/v1.0/administration/connectors/bpnDiscovery/search"}` | bpn discovery configuration |
| discovery.edcDiscovery | object | `{"key":"bpn"}` | edc discovery configuration |
| discovery.hostname | string | `""` | discovery finder configuration |
| edc | object | `{"apis":{"catalog":"/catalog/request","management":"/management/v2","negotiation":"/contractnegotiations","readiness":"/api/check/readiness","transfer":"/transferprocesses"},"delay":100,"hostname":"","participantId":"<Add participant id here>","xApiKey":"<Add API key here>"}` | in this section we configure the values that are inserted as secrets in the backend |
| edc.hostname | string | `""` | edc consumer connection configuration |
| edc.participantId | string | `"<Add participant id here>"` | BPN Number |
| edc.xApiKey | string | `"<Add API key here>"` | the secret for assesing the edc management API |
| fullnameOverride | string | `""` |  |
| hostname | string | `""` | backend hostname (without protocol prefix [DEFAULT HTTPS] for security ) |
| image.pullPolicy | string | `"IfNotPresent"` |  |
| image.repository | string | `"docker.io/tractusx/digital-product-pass-backend"` |  |
| imagePullSecrets | list | `[]` | Existing image pull secret to use to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| ingress | object | `{"enabled":false,"hosts":[{"host":"","paths":[{"path":"/","pathType":"Prefix"}]}]}` | ingress declaration to expose the dpp-backend service |
| irs | object | `{"enabled":false,"hostname":""}` | irs configuration |
| logging.level.root | string | `"INFO"` | general logging level |
| logging.level.utils | string | `"INFO"` | logging for the util components |
| maxRetries | int | `5` | max retries for the backend services |
| name | string | `"dpp-backend"` |  |
| nameOverride | string | `""` |  |
| namespace | string | `""` |  |
| nodeSelector | object | `{}` |  |
| oauth | object | `{"apiKey":{"header":"X-Api-Key","secret":"<api-key>"},"appId":"<app-id>","bpnCheck":{"bpn":"<Add participant id here>","enabled":false},"hostname":"","onLoad":"login-required","realm":"<realm>","roleCheck":{"enabled":false},"techUser":{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}}` | oauth configuration |
| oauth.apiKey | object | `{"header":"X-Api-Key","secret":"<api-key>"}` | to authenticate against single API |
| oauth.bpnCheck | object | `{"bpn":"<Add participant id here>","enabled":false}` | configure here the bpn check for the application |
| oauth.bpnCheck.bpn | string | `"<Add participant id here>"` | this bpn needs to be included in the user login information when the check is enabled |
| oauth.hostname | string | `""` | url of the identity provider service |
| oauth.roleCheck | object | `{"enabled":false}` | the role check checks if the user has access roles for the appId |
| oauth.techUser | object | `{"clientId":"<Add client id here>","clientSecret":"<Add client secret here>"}` | note: this credentials need to have access to the Discovery Finder, BPN Discovery and EDC Discovery |
| passport.aspects[0] | string | `"urn:bamm:io.catenax.generic.digital_product_passport:1.0.0#DigitalProductPassport"` |  |
| passport.aspects[1] | string | `"urn:bamm:io.catenax.battery.battery_pass:3.0.1#BatteryPass"` |  |
| passport.aspects[2] | string | `"urn:bamm:io.catenax.transmission.transmission_pass:1.0.0#TransmissionPass"` |  |
| passport.aspects[3] | string | `"urn:samm:io.catenax.generic.digital_product_passport:2.0.0#DigitalProductPassport"` |  |
| passport.policyCheck | object | `{"enabled":true,"policies":[{"obligation":[],"permission":[{"action":"USE","constraints":[{"leftOperand":"cx-policy:Membership","operator":"odrl:eq","rightOperand":"active"},{"leftOperand":"cx-policy:FrameworkAgreement","operator":"odrl:eq","rightOperand":"circulareconomy:1.0"},{"leftOperand":"cx-policy:UsagePurpose","operator":"odrl:eq","rightOperand":"cx.circular.dpp:1"}],"logicalConstraint":"odrl:and"}],"prohibition":[]}],"strictMode":false}` | configuration for policies to filter in the digital product pass asset negotiation |
| passport.policyCheck.enabled | bool | `true` | condition to enable and disable the policy check |
| passport.policyCheck.policies | list | `[{"obligation":[],"permission":[{"action":"USE","constraints":[{"leftOperand":"cx-policy:Membership","operator":"odrl:eq","rightOperand":"active"},{"leftOperand":"cx-policy:FrameworkAgreement","operator":"odrl:eq","rightOperand":"circulareconomy:1.0"},{"leftOperand":"cx-policy:UsagePurpose","operator":"odrl:eq","rightOperand":"cx.circular.dpp:1"}],"logicalConstraint":"odrl:and"}],"prohibition":[]}]` | list of allowed policies that can be selected from the edc catalog in negotiations |
| passport.policyCheck.strictMode | bool | `false` | the strict mode is quicker (uses hashes) and requires less computation complexity, the default mode is comparing against every single object value |
| podAnnotations | object | `{}` |  |
| podSecurityContext | object | `{"fsGroup":3000,"runAsGroup":3000,"runAsUser":1000,"seccompProfile":{"type":"RuntimeDefault"}}` | The [pod security context](https://kubernetes.io/docs/tasks/configure-pod-container/security-context/#set-the-security-context-for-a-pod) defines privilege and access control settings for a Pod within the deployment |
| podSecurityContext.fsGroup | int | `3000` | The owner for volumes and any files created within volumes will belong to this guid |
| podSecurityContext.runAsGroup | int | `3000` | Processes within a pod will belong to this guid |
| podSecurityContext.runAsUser | int | `1000` | Runs all processes within a pod with a special uid |
| podSecurityContext.seccompProfile.type | string | `"RuntimeDefault"` | Restrict a Container's Syscalls with seccomp |
| process | object | `{"encryptionKey":""}` | digital twin registry configuration |
| process.encryptionKey | string | `""` | unique sha512 hash key used for the passport encryption |
| replicaCount | int | `1` |  |
| resources.limits.cpu | string | `"500m"` |  |
| resources.limits.memory | string | `"512Mi"` |  |
| resources.requests.cpu | string | `"250m"` |  |
| resources.requests.memory | string | `"512Mi"` |  |
| securityCheck | object | `{"bpn":false,"edc":false}` | security configuration |
| securityContext.allowPrivilegeEscalation | bool | `false` | Controls [Privilege Escalation](https://kubernetes.io/docs/concepts/security/pod-security-policy/#privilege-escalation) enabling setuid binaries changing the effective user ID |
| securityContext.capabilities.add | list | `[]` | Specifies which capabilities to add to issue specialized syscalls |
| securityContext.capabilities.drop | list | `["ALL"]` | Specifies which capabilities to drop to reduce syscall attack surface |
| securityContext.readOnlyRootFilesystem | bool | `true` | Whether the root filesystem is mounted in read-only mode |
| securityContext.runAsGroup | int | `3000` | The owner for volumes and any files created within volumes will belong to this guid |
| securityContext.runAsNonRoot | bool | `true` | Requires the container to run without root privileges |
| securityContext.runAsUser | int | `1000` | The container's process will run with the specified uid |
| serverPort | int | `8888` | configuration of the spring boot server |
| service.port | int | `8888` |  |
| service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service |
| serviceAccount.annotations | object | `{}` |  |
| serviceAccount.create | bool | `true` |  |
| serviceAccount.name | string | `""` |  |
| single-api | object | `{"delay":1000,"maxRetries":30}` | configuration to the single API endpoint |
| tolerations | list | `[]` |  |
| volumeMounts | list | `[{"mountPath":"/app/config","name":"backend-config"},{"mountPath":"/app/data/process","name":"pvc-backend","subPath":"data/process"},{"mountPath":"/app/log","name":"tmpfs","subPath":"log"},{"mountPath":"/tmp","name":"tmpfs"},{"mountPath":"/app/data/VaultConfig","name":"tmpfs","subPath":"VaultConfig/vault.token.yml"},{"mountPath":"/app/tmp","name":"tmpfs"}]` | specifies the volume mounts for the backend deployment |
| volumeMounts[0] | object | `{"mountPath":"/app/config","name":"backend-config"}` | mounted path for the backend configuration added in the config maps |
| volumeMounts[1] | object | `{"mountPath":"/app/data/process","name":"pvc-backend","subPath":"data/process"}` | contains the location for the process data directory |
| volumeMounts[2] | object | `{"mountPath":"/app/log","name":"tmpfs","subPath":"log"}` | contains the log directory uses by the backend |
| volumeMounts[3] | object | `{"mountPath":"/tmp","name":"tmpfs"}` | container tmp directory |
| volumeMounts[4] | object | `{"mountPath":"/app/data/VaultConfig","name":"tmpfs","subPath":"VaultConfig/vault.token.yml"}` | contains the vault configuration for the backend |
| volumeMounts[5] | object | `{"mountPath":"/app/tmp","name":"tmpfs"}` | contains the temporary directory used by the backend |
| volumes | list | `[{"configMap":{"name":"{{ .Release.Name }}-backend-config"},"name":"backend-config"},{"name":"pvc-backend","persistentVolumeClaim":{"claimName":"{{ .Release.Name }}-pvc-data"}},{"emptyDir":{},"name":"tmpfs"}]` | volume claims for the containers |
| volumes[0] | object | `{"configMap":{"name":"{{ .Release.Name }}-backend-config"},"name":"backend-config"}` | persist the backend configuration |
| volumes[1] | object | `{"name":"pvc-backend","persistentVolumeClaim":{"claimName":"{{ .Release.Name }}-pvc-data"}}` | persist the backend data directories |
| volumes[2] | object | `{"emptyDir":{},"name":"tmpfs"}` | temporary file system mount |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.11.0](https://github.com/norwoodj/helm-docs/releases/v1.11.0)
