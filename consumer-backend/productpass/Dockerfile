FROM openjdk:17-alpine

RUN addgroup -g 10001 appgroup \
	&& adduser -u 10000 -g 10001 -h /home/appuser -D appuser
WORKDIR /app

VOLUME data log

RUN mkdir -p ./log
RUN mkdir -p ./data


COPY  ./target .

#HEALTHCHECK --interval=60s --timeout=4s CMD curl -f http://localhost:8080/health || exit 1
HEALTHCHECK NONE


# add permissions for a user
RUN chown -R appuser:appuser /app && chmod -R 755 /app

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./productpass-0.2.6-SNAPSHOT.jar"]
