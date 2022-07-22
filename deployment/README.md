# Deployment in ArgoCD Hotel Budapest

This document describes the material pass application deployment steps in hotel budapest using helm charts. In order to deploy the app components, the following artifacts are required. 

- Link to the Integration environment: [ArgoCD Hotel Budapest INT - Product Material Passport](https://argo.int.demo.catena-x.net) 
- Currently, the docker images are published in docker hub registry and can be accessible publicly for testing purpose. In future, the images should be stored in a private registry.
    - [edc-consumer](https://hub.docker.com/repository/docker/muhammadsaudkhan/edc-consumer): muhammadsaudkhan/edc-consumer:latest
    - [edc-provider](https://hub.docker.com/repository/docker/muhammadsaudkhan/edc-provider): muhammadsaudkhan/edc-provider:latest
    - [consumer-ui](https://hub.docker.com/repository/docker/muhammadsaudkhan/consumer-ui): muhammadsaudkhan/consumer-ui:latest


#### Sign in via the GitHub account

After signing in into the account, you can see the allocated space inside the namespace 'product-material-pass' and project 'project-material-pass' for the material pass team. The new app will be created inside this space.

#### Provisioning Persistence

Is is a shared storage to share files between edc-provider and edc-consumer connectors. The steps below are in general to deploy any application in the environment.\
##### NOTE: the storage must be created first, otherwise the later components cannot find the persistence and may be failed the sync process.

#### Creating New Application

Create new app from the top-left corner button.
Fill out the following required fields.
- Application Name: <APP_NAME> (e.g., edc-storage, edc-consumer, edc-provider, consumer-ui)
- Project: project-material-pass
- Source: Git repository where the application artifacts are stored (https://github.com/saudkhan116/DataSpaceConnector)
- Path: The path to the deployment (possible values: deployment/helm/edc-consumer, deployment/helm/edc-provider, deployment/helm/consumer-ui)
- Cluster URL: https://kubernetes.default.svc
- Namespace: product-material-pass

Click on 'Create' button

![Create New App](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/create-app.png)

- Go inside the application and sync it. It would take some time to get synced.

![Sync App](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/syncapp.png)

![Sync App](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/syncapp2.png)

- Navigate inside the pod

![Consumer Pod](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/consumer-pod.png)
- Go to the logs tab

![Consumer connector logs](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/logs.png)

If everything works fine then the application is deployed...

The same steps could be followed to deploy the edc-provider connector.

#### Consumer-UI:

The consumer frontend app for the material passport that interacts with the end-user. The above same steps could be followed to deploy the consumer-ui component.

In the end, the app should be accessible at https://materialpass.int.demo.catena-x.net/

##### Login credentails:
- User 1:- Role: Recycler, Email: mustermann@test-recycler.de, Password: mustermann
- User 2:- Role: OEM, Email: mustermann@test-oem.de, Password: mustermann

#### Example Screenshots:

![Login Page](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/login.png)

![Battery Passport](https://raw.githubusercontent.com/saudkhan116/DataSpaceConnector/main/deployment/images/batterypassport.png)

