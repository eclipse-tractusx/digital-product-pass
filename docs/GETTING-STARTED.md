# Battery Passport Guide

> [CatenaX Introduction](https://catena-x.net/en/angebote/edc-die-zentrale-komponente-fuer-die)

## Application Infrastructure

```
                    |-------Project-------|                |-----CatenaX------|
                    | Battery-Passport-UI |                | Service-Registry |
                    |---------------------|                |------------------|


                    |--------EDC-Consumer--------|         |--------EDC-Provider--------|
Backend-Service <---> Controlplane <-> Dataplane |         | Controlplane <-> Dataplane <---> Backend-Service
                    |----------------------------|         |----------------------------|
                                 |                                      |    
                        _________|____________               ___________|____________
                       |         |            |             |           |            |
                  Postgres-DB   DAPS   Hashicorp-Vault   Postgres-DB   DAPS   Hashicorp-Vault     
```

> [Demo for EDC-Deployment](https://github.com/catenax-ng/product-edc/tree/develop/edc-tests/src/main/resources/deployment/helm/all-in-one)

The `EDC-Provider` holds data that is managed via an __Data Management API__ and owned by product owners. To use the
data offer, the `EDC-Consumer` needs to __negotiate a contract for the data transfer__. The `CatenaX-Registry` gives the
platform for the services to basically find each other (provides the meta-data).

The project `Battery-Passport application` provides a UI to request battery passports through edc connectors and the
final __representation of the data__.

### Application Installation

> Prerequisite: [Docker](https://www.docker.com/products/docker-desktop/), [MiniKube](https://minikube.sigs.k8s.io/docs/start/) & [Helm](https://helm.sh/docs/intro/install/): execute `minikube start` & `minikube addons enable ingress`

* __CatenaX-Registry__
    * Description: This component is usually managed by CatenaX and only required in some development situations.
    * __Service-Registry__
        * docker container hosted in locally and mapped to __Port 4243__
            * [Image from Guide Repo](https://github.com/catenax-ng/catenax-at-home/blob/main/getting-started-guide/docker-compose.yml#L89)
            * `docker pull ghcr.io/catenax-ng/catenax-at-home/registry:0.0.1`
            * `docker run -d -p 4243:4243 -p 5013:8090 --env SPRING_PROFILES_ACTIVE=local --env IDP_ISSUER_URI="" --env SPRING_DATASOURCE_URL="jdbc:h2:mem:semanticsdb;CASE_INSENSITIVE_IDENTIFIERS=TRUE" --name cx-registry ghcr.io/catenax-ng/catenax-at-home/registry:0.0.1`
            * optional: `docker stop cx-registry` & `docker rm cx-registry`
* __EDC-Provider__
    * Description: This component consists of different services which are described in
      the [Connector Setup 3](https://github.com/catenax-ng/product-edc/tree/develop/docs#connector-setup).
    * __Controlplane__ & __Dataplane__
        * [Helm Chart](helm/edc-provider) hosted locally
            * `cd .\helm\edc-provider\`
            * `helm dependency update`
            * `helm install edc-provider .` or `helm upgrade --install edc-provider .`
            * optional: `helm uninstall edc-provider`
        * Mapped to __Port 8181__
            * `kubectl get service edc-provider-controlplane`
            * `kubectl port-forward service/edc-provider-controlplane 8181:8181`
        * Int-
          Deployment: [https://materialpass.int.demo.catena-x.net/provider/](https://materialpass.int.demo.catena-x.net/provider/)
    * __Postgres-DB__
        * Installed with the mentioned [Helm Chart](helm/edc-provider)
        * Mapped to __Port 5432__
            * `kubectl get service provider-postgresql`
            * `kubectl port-forward service/provider-postgresql 5432:5432`
    * __Backend-Service__
        * Installed with the mentioned [Helm Chart](helm/edc-provider) _or_ with a docker container
            * `docker run -d -p 5010:8090 -p 8194:8080 --name provider-backend ghcr.io/catenax-ng/catenax-at-home/provider-backend-service:0.0.1`
            * optional: `docker stop provider-backend` & `docker rm provider-backend`
        * Mapped to __Port 8194__ (when started with helm)
            * `kubectl get service edc-provider-backend`
            * `kubectl port-forward service/edc-provider-backend 8194:8194`
    * __Hashicorp-Vault__ & __DAPS__
        * Integrated in CatenaX Components
* __EDC-Consumer__
    * Description: This component consists of different services which are described in
      the [Connector Setup 3](https://github.com/catenax-ng/product-edc/tree/develop/docs#connector-setup).
    * __Controlplane__ & __Dataplane__
        * [Helm Chart](helm/edc-consumer) hosted locally
            * `cd .\helm\edc-consumer\`
            * `helm dependency update`
            * `helm install edc-consumer .` or `helm upgrade --install edc-consumer .`
            * optional: `helm uninstall edc-consumer`
        * Mapped to __Port 8181__
            * `kubectl get service edc-consumer-controlplane`
            * `kubectl port-forward service/edc-consumer-controlplane 8181:8181`
        * Int-
          Deployment: [https://materialpass.int.demo.catena-x.net/consumer/](https://materialpass.int.demo.catena-x.net/consumer/)
    * __Postgres-DB__
        * Installed with the mentioned [Helm Chart](helm/edc-consumer)
        * Mapped to __Port 5432__
            * `kubectl get service consumer-postgresql`
            * `kubectl port-forward service/consumer-postgresql 5432:5432`
    * __Backend-Service__
        * Installed with the mentioned [Helm Chart](helm/edc-consumer) _or_ with a docker container
            * `docker run -d -p 5010:8090 -p 8194:8080 --name consumer-backend ghcr.io/catenax-ng/catenax-at-home/provider-backend-service:0.0.1`
            * optional: `docker stop consumer-backend` & `docker rm consumer-backend`
        * Mapped to __Port 8194__ (when started with helm)
            * `kubectl get service edc-consumer-backend`
            * `kubectl port-forward service/edc-consumer-backend 8194:8194`
    * __Hashicorp-Vault__ & __DAPS__
        * Integrated in CatenaX Components
* __Project__
    * __Battery-Passport-UI__
        * Started with from repository root on __Port 8080__:
            * `npm install --legacy-peer-deps`
            * `npm run serve`
        * Int- Deployment: [https://materialpass.int.demo.catena-x.net/](https://materialpass.int.demo.catena-x.net/)

__Notes:__

* Port mappings are `host-port:service-port`
* Keep in mind to either start the consumer or the provider locally and for the other one use the __Int-Deployment__.

### Application Usage

> Prerequisite: [Postman](https://www.postman.com/downloads/)

To use the application, data needs to be prepared in the __EDC-Provider__ via the postman collection
in `postman/Battery-Pass`. Import the collection in postman and use the prepared requests.

1. Prepare the __Provider__ with `sample data`, `assets`, `policies` and `contract definition`
2. Register the __Provider__ as `digital twin` to the __Registry__
3. Lookup the __Registry__ to get the `digital twin` for the __Consumer__
4. Get the `shell descriptor` and the `submodels` from the __Registry__ for the __Consumer__
5. Get the `specific submodel` from the __Registry__ (more than one could be delivered in the previous step)
6. Get the contract offer catalog for the __Consumer__
7. Negotiate the contract between the __Consumer__ and __Provider__ with the `submodel` for the `digital twin`
8. Do the `data transfer` between the __Consumer and Provider__

The __Battery Passport UI__ will make this process accessible to users.

__Note:__ Adjust the URLs according to the _local_ (`http://localhost:port/`) or
_integration_ (`https://materialpass.int.demo.catena-x.net/`) environment.

> [Documentation of EDC Data Transfer](https://github.com/catenax-ng/product-edc/blob/develop/docs/data-transfer/Transfer%20Data.md) & [End-to-End Use Case](https://catenax-ng.github.io/docs/catenax-at-home-getting-started-guide)
