# Local Keycloak Setup

This guide describes how to setup a keycloak instance in local docker container and import existing realm.json file.

## Launch keycloak docker container

- Keycloak official image: [jboss/keycloak](https://registry.hub.docker.com/r/jboss/keycloak)

```
docker run --name keycloak -p 8088:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -d jboss/keycloak
```

## Import realm

Import the [realm.json](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/feature/cmp-266/docker/local/Keycloak/realm.json) located in current directory

## Create users

After importing the realm, the users need to be created manually. 

***Example users:***
- **User 1:** company 1 user  \
  **Password:** changeme \
  **Role:** OEM, Dismantler

- **User 2:** company 2 user \
  **Password:** changeme \
  **Role:** Recycler

## Integration with vuejs app

### Enable keycloak configuration

Install the keycloak plugin for vuejs app from ```npm install keycloak-js```

The keycloak configurations are defined in [src/services/service.const.js](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/feature/cmp-266/src/services/service.const.js) and [src/services/authentication.js](https://github.com/catenax-ng/product-battery-passport-consumer-app/blob/feature/cmp-266/src/services/authentication.js) files for different deployment environments.

### Build and run the app

```
npm install --legacy-peer-deps
npm run serve
```
