# Local Setup

These docker commands are used to setup the system locally.

## Cloning repository

```bash
git clone https://github.com/saudkhan116/DataSpaceConnector.git
```
## Create docker network

```bash
docker network create edc-network
```

## Create shared docker volume

```bash
docker volume create DataVolume
```

## Configure config.properties

For the local run, we need to change the data path variable in the consumer and provider config properties as these paths are used to call the APIs in the consumer application.
Later on, these config files will be mounted as volumes in containers.\
Note: Since these config files are located inside the connector directories, each container must be launched while being in the respective container directory. In this way, the current updated configuration is loaded at container startup.
### For edc-consumer:

- Go to the ./docker/edc-consumer/config/config.properties
- Edit the value of 'web.http.data.path=' to '/consumer/data'
- Save the file

### For edc-provider:

- Go to the ./docker/edc-provider/config/config.properties
- Edit the value of 'web.http.data.path=' to '/provider/data'
- Save the file

## Launch containers

The containers must be launched in correct order because of the shared data volume:
1. edc-consumer
2. edc-provider
3. consumer-ui

#### Container 1: edc-consumer

```bash

# Navigate to the working directory where Dockerfile is located
cd edc-consumer/

# build docker image
docker build -t edc-consumer:latest .

# run docker container and mount ./edc-consumer/config/config.properties and shared data volume
docker run -p 9191:9191 -p 9292:9292 -p 9192:9192 --name edc-consumer --network edc-network --volume /$(pwd)/config:/app/config -v DataVolume:/app/samples/04.0-file-transfer/data/ -d edc-consumer:latest

# check logs
docker logs edc-consumer

# stop and remove docker container
docker stop edc-consumer; docker rm edc-consumer;
```

#### Alternative: Using existing docker image
- Pull image using the command  ```docker pull muhammadsaudkhan/edc-consumer:latest```
- Run the container using the command ```docker run -p 9191:9191 -p 9292:9292 -p 9192:9192 --name edc-consumer --network edc-network --volume /$(pwd)/config:/app/config -v DataVolume:/app/samples/04.0-file-transfer/data/ -d muhammadsaudkhan/edc-consumer:latest```

#### Container 2: edc-provider
```bash
# Navigate to the working directory where Dockerfile is located
cd edc-provider/

# build docker image
docker build -t edc-provider:latest .

# run docker container and mount ./edc-provider/config/config.properties and attach shared data volume from edc-consumer container
docker run -p 8181:8181 -p 8282:8282 -p 8182:8182 --volume /$(pwd)/config:/app/config --volumes-from edc-consumer --name edc-provider --network edc-network -d edc-provider:latest

# check logs
docker logs edc-provider

# stop and remove docker container
docker stop edc-provider; docker rm edc-provider;
```

#### Alternative: Using existing docker image
- Pull image using the command  ```docker pull muhammadsaudkhan/edc-provider:latest```
- Run the container using the command ```docker run -p 8181:8181 -p 8282:8282 -p 8182:8182 --volume /$(pwd)/config:/app/config --volumes-from edc-consumer --name edc-provider --network edc-network -d muhammadsaudkhan/edc-provider:latest```

#### Container 3: consumer-ui
```bash
# Navigate to the working directory where Dockerfile is located
cd consumer-ui/

# build docker image
docker build -t consumer-ui:latest .

# run docker image
docker run -p 8080:80 --name consumer-ui --network edc-network -d consumer-ui:latest

# check logs
docker logs consumer-ui

# stop and remove docker container
docker stop consumer-ui; docker rm consumer-ui;
```

#### Alternative: Using existing docker image
- Pull image using the command  ```docker pull muhammadsaudkhan/consumer-ui:latest```
- Run the container using the command ```docker run -p 8080:80 --name consumer-ui --network edc-network -d muhammadsaudkhan/consumer-ui:latest```
- The consumer frontend is available in browser at [http://localhost:8080](http://localhost:8080)

