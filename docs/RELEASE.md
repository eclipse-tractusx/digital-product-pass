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

<div style="display: flex; align-items: center;justify-content: center;align-content: center;">
   <img src="../docs/media/catenaxLogo.svg" alt="Product Battery Pass Consumer App Release Guide" style="width:50px;"/>
   <h1 style="margin: 10px 0 0 10px">Product Battery Pass Consumer App Release Guide</h1>
</div>

## Release an application

The application is released through [GitHub Releases page](https://github.com/eclipse-tractusx/digital-product-pass/releases) by creating a new release and tag. Follow the [Semantic Versioning Scheme](https://semver.org/spec/v2.0.0.html) while creating a new tag.


The changelog must also be updated from [CHANGELOG.md](../CHANGELOG.md) file, showing the changes in release using [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) format.

## Release Helm charts

Helm chart released:
- [digital-product-pass](../charts/digital-product-pass/Chart.yaml)

Aditional Helm charts of below components can be found in *deployment/infrastructure* folder.
- [edc-consumer](../deployment/infrastructure/data-consumer/edc-consumer/Chart.yaml)
- [edc-provider](../deployment/infrastructure/data-provider/edc-provider/Chart.yaml)

In order to update helm charts, please update helm chart version and related dependencies from *version* property in *Chart.yaml file* for the above components. In addition, if there are changes to application version, the *appVersion* property also needs to be changed.

Navigate to the [Deploymment Guide](/deployment/README.md) and have a look into the reference links.


## NOTICE

This work is licensed under the [CC-BY-4.0](https://creativecommons.org/licenses/by/4.0/legalcode).

- SPDX-License-Identifier: CC-BY-4.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG
- SPDX-FileCopyrightText: 2022, 2024 Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023, 2024 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
