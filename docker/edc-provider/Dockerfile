# -buster is required to have apt available
FROM openjdk:11-jre-slim-buster

# by default curl is not available, so install it
RUN apt update -y && apt install curl vim -y

WORKDIR /app
COPY ./provider.jar .
COPY config/ ./config/
COPY ./04.0-file-transfer/ ./samples/04.0-file-transfer/
COPY ./assets ./assets

EXPOSE 8181
EXPOSE 8182
EXPOSE 8282


ENTRYPOINT java \
    -Dedc.fs.config=./config/config.properties \
    -Dedc.ids.title="Eclipse Dataspace Connector" \
    -Dedc.ids.description="Eclipse Dataspace Connector with IDS extensions" \
    -jar provider.jar