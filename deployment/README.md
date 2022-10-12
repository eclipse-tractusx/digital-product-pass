## Technical Guide - Deployment in ArgoCD Hotel Budapest

This document describes the battery pass application deployment steps in hotel budapest using helm charts. In order to deploy the app components, the following artifacts are required. 

- Link to the Integration environment: [ArgoCD Hotel Budapest INT - Product Material Passport](https://argo.int.demo.catena-x.net)

- [edc-consumer](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/main/deployment/helm/edc-consumer)

- [edc-provider](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/main/deployment/helm/edc-provider)

- [consumer-ui](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/main/deployment/helm/consumer-ui)


#### Sign in via the GitHub account

After signing in into the account, you can see the allocated space inside the namespace 'product-material-pass' and project 'project-material-pass' for the material pass team. The new app should be created inside this space.

#### Creating New Application

Create new app from the top-left corner button.
Fill out the following required fields.
- **Application Name:** <APP_NAME> (e.g., materialpass-edc)
- **Project:** project-material-pass
- **Source:** Git repository where the application artifacts are stored (https://github.com/catenax-ng/product-battery-passport-consumer-app)
- **Revision:** select branch or a tag
- **Path:** The path to the deployment (possible values: deployment/helm/edc-consumer, deployment/helm/edc-provider, deployment/helm/consumer-ui)
- **Cluster URL:** https://kubernetes.default.svc
- **Namespace:** product-material-pass

Click on 'Create' button

![Create New App](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/create_application.png)

- Go inside the application and sync it. It would take some time to get synced.

![Sync App](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/app_sync.png)

![Sync App](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/pod_sync.png)

- Navigate inside the pod

![Consumer Pod](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/consumer-ui_pod.png)
- Go to the logs tab

![Consumer connector logs](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/logs.png)

If everything works fine then the application is deployed...

#### Consumer-UI:

The consumer frontend app for the material passport that interacts with the end-user. The steps above will be followed to deploy the consumer frontend component.

In the end, the frontend should be accessible at https://materialpass.int.demo.catena-x.net. You would be navigated to the CatenaX central IDP and can see the keycloak login page after company selection.

##### Login credentails:
- **Company Selection:** CX-Test-Access
- **User 1:** Role: OEM, user: company 1 user, Password: changeme
- **User 2:** Role: Recycler, user: company 2 user, Password: changeme

#### Example Screenshots:

![Login Page](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/cx_login_page.png)

![Dashboard](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/batterypass_dashboard.png)

![Battery Passport](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/feature/cmp-266/deployment/images/battery_passport_data.png)

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
