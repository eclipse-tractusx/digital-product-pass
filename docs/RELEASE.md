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

<div style="display: flex; align-items: center;justify-content: center;align-content: center;">
   <img src="./catena-x-logo.svg" alt="Product Battery Pass Consumer App Release Guide" style="width:50px;"/>
   <h1 style="margin: 10px 0 0 10px">Product Battery Pass Consumer App Release Guide</h1>
</div>

## Release an application

The application is released through [GitHub Releases page](https://github.com/eclipse-tractusx/digital-product-pass/releases) by creating a new release and tag. Follow the [Semantic Versioning Scheme](https://semver.org/spec/v2.0.0.html) while creating a new tag.


The changelog must also be updated from [CHANGELOG.md](../CHANGELOG.md) file, showing the changes in release using [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format.

## Release Helm charts

Helm charts released:
- [consumer-ui](../charts/consumer-ui/Chart.yaml)
- [consumer-backend](../charts/consumer-backend/Chart.yaml)


Aditional Helm charts of below components can be found in *deployment/helm* folder.
- [edc-consumer](../deployment/helm/edc-consumer/Chart.yaml)
- [edc-provider](../deployment/helm/edc-provider/Chart.yaml)

In order to update helm charts, please update helm chart version and related dependencies from *version* property in *Chart.yaml file* for the above components. In addition, if there are changes to application version, the *appVersion* property also needs to be changed.

Navigate to the [Deploymment Guide](/deployment/README.md) and follow the steps. The deployment guide illustrates the manual steps to install helm deployments into ArgoCD INT environment.
