<!--
#######################################################################

Tractus-X - Digital Product Passport Application 

Copyright (c) 2022 BMW AG
Copyright (c) 2022 Henkel AG & Co. KGaA
Copyright (c) 2023 CGI Deutschland B.V. & Co. KG
Copyright (c) 2023 Contributors to the Eclipse Foundation

See the NOTICE file(s) distributed with this work for additional
information regarding copyright ownership.

This work is made available under the terms of the
Creative Commons Attribution 4.0 International (CC-BY-4.0) license,
which is available at
https://creativecommons.org/licenses/by/4.0/legalcode.

SPDX-License-Identifier: CC-BY-4.0

#######################################################################
-->

## Deployment Guide

### Reference Links

* [Getting Started Documentation](../docs/GETTING-STARTED.md)
* [Install.md](../INSTALL.md)
* Helm Charts
    * [edc-consumer](./infrastructure/data-consumer/edc-consumer)
    * [edc-provider](./infrastructure/data-provider/edc-provider)
    * [digital-product-pass](../charts/digital-product-pass)
* OpenAPI Documentation
    * [Open API documentation in Swagger](https://dpp.int.demo.catena-x.net/swagger-ui/index.html)
    * [Central Swagger Hub](https://app.swaggerhub.com/apis/eclipse-tractusx-bot/digital-product-pass)


<br />

## Helm to manage Kubernetes

### Basic Helm tricks

<details><summary>show</summary>
<p>

```bash
# Creating basic helm chart
helm create <CHART_NAME>

# Building chart dependencies
 helm dependency build <SOURCE>

# Updating chart dependencies
 helm dependency update <SOURCE>

# Installing helm release
helm install <CHART_NAME> -f myvalues.yaml ./SOURCE

# Uninstalling helm release
helm uninstall <CHART_NAME>

# Listing helm releases
helm list
```
<p>
</details>

### Using Helm Repository
<details><summary>show</summary>
<p>

```bash
helm repo add [NAME] [URL]  [flags]

helm repo list / helm repo ls

helm repo remove [REPO1] [flags]

helm repo update / helm repo up

helm repo update [REPO1] [flags]

helm repo index [DIR] [flags]
```
<p>
</details>

### Download a Helm chart from a repository 

<details><summary>show</summary>
<p>

```bash
helm pull [chart URL | repo/chartname] [...] [flags] ## this would download a helm, not install 
helm pull --untar [rep/chartname] # untar the chart after downloading it 
```

</p>
</details>
