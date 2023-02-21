# Product Passport Administrator Guide Documentation

![C-X Logo](./CXLogo.png)  

Version: v1.1 </br>
Latest Revision Jan 18, 2023

>Some links might not be accesible as they lead to a private confluence. If you need access please reach out to the dev Team and request what information you need for what reason.

## Table of Contents

1. [Table of contents](#table-of-contents)  
2. [Introduction](#introduction)
3. [Getting Started Guide](#getting-started-guide)
4. [Deployment Configuration](#deployment-configuration)
5. [Local Keycloak Configuration](#local-keycloak-configuration)
6. [Helm Charts Configuration](#helm-charts-configuration)
7. [Consumer Backend Configuration](#consumer-backend-configuration)  
    7.1 [Backend Application Configuration](#backend-application-configuration)  
    7.2  [Spring Boot Configuration](#spring-boot-configuration)  
    7.3  [Spring Boot Logging Configuration](#spring-boot-logging-configuration)  
8. [Postman Collection](#postman-collection)
9. [Secrets Management](#secrets-management)  

## Introduction

This guide contains all the available information for an administrator to configure, operate and deploy the Product Passport Application.  

At the moment the Application does not offers any type of administration interface within the Frontend UI, however a series of configurations can be performed in order to administrate it.

Here you will find all the guides related with the configuration of the Product Passport Application Infrastructure/Environment as well as the Frontend and Backend Systems.

## Getting Started Guide

To start the configuration of the Product Passport Application please follow this getting started guide:

| Name | Location | Link |
| ---- | -------- | ---- |
| Getting Started Guide | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docs/GETTING-STARTED.md](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docs/GETTING-STARTED.md) |  

## Deployment Configuration

In order to deploy the application in an environment we use Helm Charts to configure the Kubernetes pods and containers.  

All the information about deploying you can find in this resource:

| Name | Location | Link |
| ---- | -------- | ---- |
| Technical Guide for Development | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/deployment/README.md](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/deployment/README.md) |

## Local Keycloak Configuration

All the authentication and authorization is managed by Catena-X IAM, however there is a possibility to configure a local Keycloak Instance for testing and development purposes.  

Therefore in order to configure the users and roles for the application the administration needs to import the Realm Configuration File into their local Keycloak instance hosted in a docker container.  

Additionally two test users shall be created and the correct roles shall be assigned:  

**User 1:** "company 1 user" (OEM, Dismantler)  
**User 2:** "company 2 user" (Recycler)  

Follow the [Local Keycloak Setup Guide](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docker/local/Keycloak/README.md) in order to set up the users, and their passwords correctly:

| Name | Location | Link |
| ---- | -------- | ---- |
| Local Keycloak Setup Guide | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docker/local/Keycloak/README.md](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docker/local/Keycloak/README.md) |
| Realm Configuration File | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docker/local/Keycloak/realm.json](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docker/local/Keycloak/realm.json) |

## Helm Charts Configuration

At the moment, the Product Passport Application is hosted in two environments:

|  | Materialpass | ArgoCD Development |
| - | -------- | ---- |
| **Development Environment** | [https://materialpass.dev.demo.catena-x.net/](https://materialpass.dev.demo.catena-x.net/) | [https://argo.dev.demo.catena-x.net/](https://argo.dev.demo.catena-x.net/) |
| **Integration Environment** | [https://materialpass.int.demo.catena-x.net/](https://materialpass.int.demo.catena-x.net/) | [https://argo.int.demo.catena-x.net/](https://argo.int.demo.catena-x.net/) |

All the values for the helm charts are configured for each environment and set up in the Product Passport Application source code:  

| Name | Location | Link |
| ---- | -------- | ---- |
| Helm Charts Main Directory | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm) |
| Consumer UI Helm Directory | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm/consumer-ui](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm/consumer-ui) |
| EDC Consumer Helm Charts | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm/edc-consumer](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm/edc-consumer) |
| MOCK EDC Provider Helm Charts | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm/edc-provider](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/deployment/helm/edc-provider) |

## Consumer Backend Configuration

In order to communicate with the Catena-X Services there is a Consumer Backend that manages user sessions, provide APIs to access information, etc.  

All the information about the backend services is described in this documentation:

| Name | Location | Link |
| ---- | -------- | ---- |
| Consumer Backend Guide | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/consumer-backend/materialpass/readme.md](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/consumer-backend/materialpass/readme.md) |
| Open API - swagger **(in progress)** | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/product-passport-consumer-backend.json](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/QG4-open-api/document-api_cmp-392/consumer-backend/productpass/openapi/product-passport-consumer-backend.json) |

### Backend Application Configuration

The configurations of log levels and other variables can be set in the following file:

| Name | Location | Link |
| ---- | -------- | ---- |
| Backend Application Configuration | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/consumer-backend/productpass/src/main/resources/config/configuration.yml](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/consumer-backend/productpass/src/main/resources/config/configuration.yml) |

All the application utilizes these variables to configure the utilities (tools) and other controllers/services.

### Spring Boot Configuration

The Consumer Backend is running over a Spring Boot server, therefore a application configuration file was created to set up mandatory parameters like the Keycloak host, and the security constrains for accessing each API and Services:

| Name | Location | Link |
| ---- | -------- | ---- |
| Spring Boot Server Configuration | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/consumer-backend/productpass/src/main/resources/application.yml](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/consumer-backend/productpass/src/main/resources/application.yml) |

### Spring Boot Logging Configuration

In order to manage the logs from the application a XML file was set, it contains the configuration from the log format and output, as well as the role back and file configuration:  

| Name | Location | Link |
| ---- | -------- | ---- |
| Spring Boot Logging Configuration | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/feature/backend-api-develop/consumer-backend/productpass/src/main/resources/logback-spring.xml](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/feature/backend-api-develop/consumer-backend/productpass/src/main/resources/logback-spring.xml) |

## Postman Collection

In order to document and test easily the API that are set up and used by the Product Passport Application, there were set a series of Postman Collections that can be found here:

| Name | Location | Link |
| ---- | -------- | ---- |
| Postman Collection Directory | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/postman](https://github.com/catenax-ng/product-battery-passport-consumer-app/tree/develop/postman) |
| Postman Getting Started Guide | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/postman/README.md](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/postman/README.md) |

## Secrets Management

In order to set up the secret management please follow this guide:

| Name | Location | Link |
| ---- | -------- | ---- |
| Secrets Management Documentation | GitHub | [https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docs/SECRETS_MANAGEMENT.md](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/develop/docs/SECRETS_MANAGEMENT.md) |
