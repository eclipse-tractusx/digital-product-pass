# -buster is required to have apt available
FROM openjdk:11-jre-slim-buster

# by default curl is not available, so install it
RUN apt update -y && apt install curl vim -y

WORKDIR /app
COPY ./consumer.jar /app
COPY ./config/config.properties /app/config/config.properties
COPY ./04.0-file-transfer/ /app/samples/04.0-file-transfer/

EXPOSE 9191
EXPOSE 9192
EXPOSE 9292

ENTRYPOINT java \
    -Dedc.fs.config=./config/config.properties \
    -Dedc.ids.title="Eclipse Dataspace Connector" \
    -Dedc.ids.description="Eclipse Dataspace Connector with IDS extensions" \
    -jar consumer.jar
