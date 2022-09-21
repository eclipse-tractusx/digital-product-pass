# Product-Battery-Passport Consumer-App

This battery pass consumer application is used with the **Asset Administration Shell (AAS)** locally.

## Setup Project

### Cloning repository

```bash
# Project repository
git clone https://github.com/catenax-ng/product-battery-passport-consumer-app.git

# AAS repository
git clone https://github.com/catenax-ng/catenax-at-home.git
```

### Launch docker containers for the Asset Administration Shell (AAS)

```bash
cd catenax-at-home/getting-started-guide/
docker compose up -d
```

### Init Data Provider

```bash
cd ../../product-battery-passport-consumer-app/aas-int-provider/
chmod ug+x ./init-provider.sh

# Executing this script will create a sample battery passport data, create EDC asset, policies, contract definitions and register it as a digital twin inside registry.
./init-provider.sh
```

**Note:** To execute the shell-script with windows, the [Git Bash](https://gitforwindows.org/) is required.

### Install dependencies

```bash
cd ..
npm install --legacy-peer-deps
```

## Run Project

### Compiles and hot-reloads for development

```bash
npm run serve
```

### Compiles and minifies for production

```bash
npm run build
```

### How to set up a code editor

See [VSCode configuration](https://confluence.catena-x.net/pages/viewpage.action?pageId=55009683).

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

