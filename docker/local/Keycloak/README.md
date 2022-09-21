# Local Setup

Running the keycloak instance in localhost requires to launch keycloak docker container and import realm.json file.

## Launch keycloak docker container

```
docker run --name keycloak1 -p 8088:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -d jboss/keycloak
```

## Import realm

Import the realm.json file located in current directory

## Create users

After importing the realm, the users need to be created manually. 

Example users:
- User 1: company user 1 \
  password: changeme
- User 2: company user 2 \
  password: changeme

## Enable keycloak configuration in vuejs app

Install the keycloak plugin for vuejs app from ```npm install keycloak-js```

The keycloak configurations are defined in 'src/services/service.const.js' file for dev and prod environment. In order to use keycloak dev settings, the dev configuration need to be enabled and prod one should be disabled.

## Build and run the app

```
npm install --legacy-peer-deps
npm run serve
```
