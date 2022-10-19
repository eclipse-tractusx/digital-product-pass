# backend-service

![Version: 0.0.6](https://img.shields.io/badge/Version-0.0.6-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 0.0.6](https://img.shields.io/badge/AppVersion-0.0.6-informational?style=flat-square)

Small CX Backend Service Implementation for Testing Purposes

**Homepage:** <https://github.com/denisneuling/cx-backend-service>

## TL;DR
```shell
$ helm repo add dn https://denisneuling.github.io/cx-backend-service
$ helm install cx-backend-service dn/cx-backend-service --version 0.0.6
```

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` | [Affinity](https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/#affinity-and-anti-affinity) constrains which nodes the Pod can be scheduled on based on node labels. |
| automountServiceAccountToken | bool | `false` | Whether to [automount kubernetes API credentials](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/#use-the-default-service-account-to-access-the-api-server) into the pod |
| autoscaling.enabled | bool | `false` | Enables [horizontal pod autoscaling](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale/) |
| autoscaling.maxReplicas | int | `100` | Maximum replicas if resource consumption exceeds resource threshholds |
| autoscaling.minReplicas | int | `1` | Minimal replicas if resource consumption falls below resource threshholds |
| autoscaling.targetCPUUtilizationPercentage | int | `80` | targetAverageUtilization of cpu provided to a pod |
| autoscaling.targetMemoryUtilizationPercentage | int | `80` | targetAverageUtilization of memory provided to a pod |
| fullnameOverride | string | `""` | Overrides the releases full name |
| image.pullPolicy | string | `"IfNotPresent"` | [Kubernetes image pull policy](https://kubernetes.io/docs/concepts/containers/images/#image-pull-policy) to use |
| image.repository | string | `"ghcr.io/denisneuling/cx-backend-service"` | Which container image to use |
| image.tag | string | `""` | Overrides the image tag whose default is the chart appVersion |
| imagePullSecrets | list | `[]` | Image pull secret to create to [obtain the container image from private registries](https://kubernetes.io/docs/concepts/containers/images/#using-a-private-registry) |
| livenessProbe | object | `{"exec":{"command":["/bin/bash","-c","/bin/ps -ef | grep backend-service | grep -v grep"]},"initialDelaySeconds":10,"periodSeconds":10}` | [Liveness-Probe](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/#define-a-liveness-command) to detect and remedy broken applications |
| livenessProbe.exec | object | `{"command":["/bin/bash","-c","/bin/ps -ef | grep backend-service | grep -v grep"]}` | exec command for liveness check |
| livenessProbe.initialDelaySeconds | int | `10` | initialDelaySeconds before performing the first probe |
| livenessProbe.periodSeconds | int | `10` | periodSeconds between each probe |
| nameOverride | string | `""` | Overrides the charts name |
| nodeSelector | object | `{}` | [Node-Selector](https://kubernetes.io/docs/concepts/scheduling-eviction/assign-pod-node/#nodeselector) to constrain the Pod to nodes with specific labels. |
| persistence.accessMode | string | `nil` | [PersistentVolume Access Modes](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#access-modes) Access mode to use. One of (ReadOnlyMany, ReadWriteOnce, ReadWriteMany, ReadWriteOncePod) |
| persistence.capacity | string | `"100M"` | Capacity given to the claimed [PersistentVolume](https://kubernetes.io/docs/concepts/storage/persistent-volumes/) |
| persistence.enabled | bool | `false` | Whether to enable persistence via [PersistentVolumeClaim](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#reserving-a-persistentvolume) |
| persistence.storageClassName | string | `nil` | Storage class to use together with the claimed [PersistentVolume](https://kubernetes.io/docs/concepts/storage/persistent-volumes/) |
| podAnnotations | object | `{}` | [Annotations](https://kubernetes.io/docs/concepts/overview/working-with-objects/annotations/) added to deployed [pods](https://kubernetes.io/docs/concepts/workloads/pods/) |
| podSecurityContext | object | `{}` |  |
| readinessProbe | object | `{"exec":{"command":["/bin/bash","-c","/bin/ps -ef | grep backend-service | grep -v grep"]},"initialDelaySeconds":10,"periodSeconds":10}` | [Readiness-Probe](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/#define-readiness-probes) to detect ready applications to receive traffic |
| readinessProbe.exec | object | `{"command":["/bin/bash","-c","/bin/ps -ef | grep backend-service | grep -v grep"]}` | exec command for readiness check |
| readinessProbe.initialDelaySeconds | int | `10` | initialDelaySeconds before performing the first probe |
| readinessProbe.periodSeconds | int | `10` | periodSeconds between each probe |
| replicaCount | int | `1` |  |
| resources | object | `{}` | [Resource management](https://kubernetes.io/docs/concepts/configuration/manage-resources-containers/) applied to the deployed pod |
| securityContext | object | `{}` |  |
| service.backend.port | int | `8081` | Port on which to run the "backend" api |
| service.frontend.port | int | `8080` | Port on which to run the "frontend" api |
| service.type | string | `"ClusterIP"` | [Service type](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types) to expose the running application on a set of Pods as a network service. |
| serviceAccount.annotations | object | `{}` | [Annotations](https://kubernetes.io/docs/concepts/overview/working-with-objects/annotations/) to add to the service account |
| serviceAccount.create | bool | `true` | Specifies whether a [service account](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/) should be created per release |
| serviceAccount.name | string | `""` | The name of the service account to use. If not set and create is true, a name is generated using the release's fullname template |
| tolerations | list | `[]` | [Tolerations](https://kubernetes.io/docs/concepts/scheduling-eviction/taint-and-toleration/) are applied to Pods to schedule onto nodes with matching taints. |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.10.0](https://github.com/norwoodj/helm-docs/releases/v1.10.0)
