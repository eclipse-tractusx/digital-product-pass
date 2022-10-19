## General Docker commands

### Build Image
```bash
docker build -t <IMAGE_NAME>:<IMAGE_TAG> .
```

### Run Container
```bash
docker run -p <HOST_PORT>:<CONTAINER_PORT> --name <CONTAINER_NAME> -d <IMAGE_NAME>:<IMAGE_TAG>
```

### Tag Image
```bash
docker tag <IMAGE_NAME>:<IMAGE_TAG> <REGISTRY>/<IMAGE_NAME>:<IMAGE_TAG>
```

### Push Image
```bash
docker push <REGISTRY>/<IMAGE_NAME>:<IMAGE_TAG>
```

### Stop Container
```bash
docker stop <CONTAINER_NAME>;
```

### Remove Container
```bash
docker rm <CONTAINER_NAME>;
```

### Java Remote Debugging:
Add this parameter when running docker run:

```bash
-e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000" -p 8000:8000
```
