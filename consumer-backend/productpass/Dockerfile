FROM openjdk:17-alpine

RUN addgroup -g 10001 appgroup \
	&& adduser -u 10000 -g 10001 -h /home/appuser -D appuser

WORKDIR /app

RUN mkdir -p ./data
RUN mkdir -p ./log

COPY  ./target .
COPY  ./data ./data/
COPY  ./log ./log/

#HEALTHCHECK --interval=60s --timeout=4s CMD curl -f http://localhost:8080/health || exit 1
HEALTHCHECK NONE


# add permissions for a user
RUN chown -R appuser:appuser /app && chmod -R 755 /app

USER appuser
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./productpass-0.0.1-SNAPSHOT.jar"]
