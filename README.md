# ![Product Battery Passport Consumer Application (Frontend)](./docs/catena-x-logo.svg) Product Battery Passport Consumer App (Frontend)

## What is battery passport consumer application?

The battery passport consumer application provides a user interface to request a battery passport from a battery manufacturer using the standardized components and technologies in a Catena-X network. The passport will be displayed in a human-readable from any browser. The data exchange standards given by Catena-X are used to provide the battery passport to different personas (roles) in the network.

In particular, the appliction is used to access the battery passport data provided by battery manufacturer. By scanning QR-code or knowing the manufacturer and battery-ID, a user can request the passport  through **Eclipse Dataspace Connectors (EDCs)** over the Catena-X network. The passport provider will provide data attributes that is only visible to a permitted signed-in user.


## Getting Started

Before contributing please read:
- [Community Code of Conduct](./docs/tractusx/CODE_OF_CONDUCT.md)
- [Contributing Guidelines](./docs/tractusx/CONTRIBUTING.md)
- [Notice on tractus-x Repositories](./docs/tractusx/NOTICE_template.md)
- [Security Policy](./docs/tractusx/SECURITY.md)

### Prerequisites:

- Git
- Code editor (VS Code/ IntelliJ recommended)
- Nodejs 16 (Node Package Manager - npm)
- Vuejs
- Docker
- Git Bash (for windows operating system only)

## Installation
### Clone project repository

```bash
git clone https://github.com/catenax-ng/product-battery-passport-consumer-app.git
```

### Install dependencies

```bash
cd product-battery-passport-consumer-app/
npm install --legacy-peer-deps
```

## Run Project

### Compiles and minifies for production

```bash
npm run build
```
#### Method 1: Without Docker

#### Compiles and hot-reloads for development

```bash
npm run serve
```

#### Method 2: With Docker

The Dockerfile is located in the project root directory.

```bash
# build docker image
docker build -t consumer-ui:latest .

# run docker image
docker run -p 8080:8080 --name consumer-ui -d consumer-ui:latest

# check logs
docker logs consumer-ui

# stop and remove docker container
docker stop consumer-ui; docker rm consumer-ui;
```
You can run the application in docker container with existing image from GitHub packages.

See consumer frontend docker image in registry: [consumer-ui](https://github.com/catenax-ng/product-battery-passport-consumer-app/pkgs/container/product-battery-passport-consumer-app%2Fconsumer-ui)

```bash
# pull the image 
# Replace placeholder <LATEST_TAG> with the most recent tag in registry

docker pull ghcr.io/catenax-ng/product-battery-passport-consumer-app/consumer-ui:<LATEST_TAG>

# run docker image
docker run -p 8080:8080 --name consumer-ui -d ghcr.io/catenax-ng/product-battery-passport-consumer-app/consumer-ui:<LATEST_TAG>
```
The consumer frontend is available in browser at [http://localhost:8080](http://localhost:8080)

## Coding styles

### How to set up a code editor

See [VSCode configuration](https://confluence.catena-x.net/pages/viewpage.action?pageId=55009683).

## License

[Apache-2.0](https://raw.githubusercontent.com/catenax-ng/product-battery-passport-consumer-app/main/LICENSE)
