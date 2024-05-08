<!-- 
  Tractus-X - Digital Product Passport Application 
 
  Copyright (c) 2022, 2024 BMW AG, Henkel AG & Co. KGaA
  Copyright (c) 2023, 2024 CGI Deutschland B.V. & Co. KG
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

## Secrets management with HashiCorp Vault Service

The client credentials, database passwords, access tokens are considered as secrets and they are usually kept in a vault. CatenaX have a central Hashicorp vault component to store these types of secrets and credentails to prevant from revealing them in a public source code repository to ensure security. These secrets are then utilized by Kubernetes resources through helm charts in a safe and secure manner.

At the time of writing this guide, ArgoCD was used to deploy the application. It accesses the secrets through its built-in vault plugins which solves the secret management issues with GitOps. Vault plugin internally uses a special vault token for authentication,  retrieve actual secret values against their keys and subsitute them in a deployment.

Further read about ArgoCD valut plugin here: [ArgoCD vault plugin](https://argocd-vault-plugin.readthedocs.io/en/stable/)

MaterialPass team is allocated a vault space at [HashiCorp Vault - Material Pass ](https://vault.demo.catena-x.net/ui/vault/secrets/material-pass/list).

To access a material pass vault space, a vault token is required

 Vault contents:
- **edc/oauth#api.key** 
- **edc/participant#bpnNumber**
- **backend/#signKey**
- **backend#clientId**
- **backend#clientSecret**
- **oauth#realm**
- **oauth#appId**
- **oauth#xApiKey**

To put a secret from vault, a special pattern is followed in helm values files:

```bash
<path:some/path#secret-key#version>
```

Some examples below:
```bash
<path:material-pass/data/int/edc/vault#vault.hashicorp.url>
<path:material-pass/data/int/edc/vault#vault.hashicorp.url#1>
```

**Note:** ***/data/*** path is always be placed after ***material-pass***, though it is not defined in a vault directory structure. AVP uses this data path itself internally.

To use a vault and create new secret, please look here: [how to-use vault create a secret](https://catenax-ng.github.io/docs/guides/how-to-use-vault#create-a-secret)

## Local Run

### Using Docker

If dpp frontend is run using docker, the following environment variables must be set in [build and deploy script](../../../dpp-frontend/buildAndDeploy.sh):

- APP_VERSION
- APP_API_MAX_RETRIES
- API_SEARCH_TIMEOUT
- API_NEGOTIATE_TIMEOUT
- API_DECLINE_TIMEOUT
- APP_API_DELAY
- REPO_COMMIT_ID
- REPO_ENDPOINT_URL
- AUTH_ROLE_CHECK
- AUTH_BPN_CHECK
- AUTH_BPN_NUMBER
- APP_PORTAL_URL
- APP_ADMIN_EMAIL
- APP_AUTO_SIGN
- IDENTITY_PROVIDER_URL
- HOST_URL
- DATA_URL
- KEYCLOAK_CLIENTID
- KEYCLOAK_REALM
- KEYCLOAK_ONLOAD



```bash
# run script
../buildAndDeploy.sh consumer-ui
```
### Using Helm

The required variables must be set in values-*.yaml file manually.

Further info about vault plugin for helm charts: [argocd-vault-plugin-helm](https://catenax-ng.github.io/docs/guides/ArgoCD/howto-use-vault-secrets-with-argocd#argocd-vault-plugin-helm)


## Secrets Scanning

### Veracode
Veracode upload-and-scan is used for secret scanning and scan results are then published in veracode dashboard once job is completed [Static Application Serucity Testing](../infrastructure-as-code/IaC.md)

### Git Guardian

Git Guardian tool is used to scan secrets within application. It is currently active in the repository and managed by SEC team.

#### Pre-commit Hook:

A client side git hook that runs prior to commit code changes.

#### Pre-push Hook:

A client-side git hook similar to pre-commit hook that runs right before code changes are pushed to a remote origin.

GitGuardian hooks are performed through ggshield utility. ggshield is a wrapper around GitGuardian API for secrets detection that requires an API key to work. Please refer the official documentation for more information [here](https://docs.gitguardian.com/ggshield-docs/integrations/git-hooks/pre-commit)


#### Links
- Setting up Git Guradian for the project: [gitguardian-shield](https://docs.gitguardian.com/ggshield-docs/getting-started)
- The git hooks for the repository: [yaml file](../../../pre-commit-config.yaml)
- Release Guidelines requirements: [here](https://eclipse-tractusx.github.io/docs/release/trg-8/trg-8-03)

## NOTICE

This work is licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0).

- SPDX-License-Identifier: Apache-2.0
- SPDX-FileCopyrightText: 2022, 2024 BMW AG, Henkel AG & Co. KGaA
- SPDX-FileCopyrightText: 2023, 2024 CGI Deutschland B.V. & Co. KG
- SPDX-FileCopyrightText: 2023 Contributors to the Eclipse Foundation
- Source URL: https://github.com/eclipse-tractusx/digital-product-pass
