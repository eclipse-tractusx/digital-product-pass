## Secrets management with CX HashiCorp Vault and ArgoCD Vault Plugin (AVP)

The client credentials, database passwords, access tokens are considered as secrets and they are usually kept in a vault. CatenaX have a central Hashicorp vault component to store these types of secrets and credentails to prevant from revealing them in a public source code repository to ensure security. These secrets are then utilized by Kubernetes resources through helm charts in a safe and secure manner.

ArgoCD access these secrets through vault plugins **(avp)** which solves the secret management issues with GitOps. Vault plugin internally uses a special vault token for authentication,  retrieve actual secret values against their keys and subsitute them in a deployment.

Further read about ArgoCD valut plugin here: [ArgoCD vault plugin](https://argocd-vault-plugin.readthedocs.io/en/stable/)

MaterialPass team is allocated a vault space at [CX HashiCorp Vault - Material Pass ](https://vault.demo.catena-x.net/ui/vault/secrets/material-pass/list).

To access a material pass vault space, a vault token is required

 Vault contents:
- **ids-daps_key**
- **ids_daps_crt**
- **int/** (directory used for integration environment)

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

If consumer frontend is run using docker, one needs to pass secret values through the variables: ***API_KEY, VUE_APP_CLIENT_ID, VUE_APP_CLIENT_SECRET*** in docker environment.

```bash
# example
docker run --name consumer-ui -p 8080:8080 -d -e VUE_APP_CLIENT_ID=<YOUR_REGISTRY_CLIENT_ID_HERE> -e VUE_APP_CLIENT_SECRET=<YOUR_REGISTRY_CLIENT_SECRET_HERE> -e X_API_KEY=<YOUR_EDC_API_KEY_HERE> consumer-ui:<LATEST_TAG>
```


### Using Helm

The variables ***API_KEY, VUE_APP_CLIENT_ID, VUE_APP_CLIENT_SECRET*** must be set in values.yaml file manually.


Further info about vault plugin for helm charts: [argocd-vault-plugin-helm](https://catenax-ng.github.io/docs/guides/ArgoCD/howto-use-vault-secrets-with-argocd#argocd-vault-plugin-helm)


## Secrets Scanning

### Veracode
Veracode upload-and-scan is used for secret scanning and scan results are then published in veracode dashboard once job is completed [Static Application Serucity Testing](./IaC.md)

### Git Guardian

Git Guardian tool is used to scan secrets within application. It is currently active in the repository and managed by SEC team.

#### Pre-commit Hook:

A client side git hook that runs prior to commit code changes.

#### Pre-push Hook:

A client-side git hook similar to pre-commit hook that runs right before code changes are pushed to a remote origin.

GitGuardian hooks are performed through ggshield utility. ggshield is a wrapper around GitGuardian API for secrets detection that requires an API key to work. Please refer the official documentation for more information [here](https://docs.gitguardian.com/ggshield-docs/integrations/git-hooks/pre-commit)

The git hooks for this repository are set up in [yaml file](../pre-commit-config.yaml)



Setting up Git Guradian for the project [gitguardian-shield](https://docs.gitguardian.com/ggshield-docs/getting-started)
