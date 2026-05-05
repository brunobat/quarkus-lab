
```shell
docker run \
--name lgtm \
-p 3000:3000 \
-p 4317:4317 \
-p 4318:4318 \
--rm \
-ti \
docker.io/grafana/otel-lgtm:latest
```

To not lose the data and include the tempo MCP:
```shell
docker run -d --name lgtm -p 3000:3000 -p 4317:4317 -p 4318:4318 -p 3200:3200 -v /home/<user>/tools/grafana-data:/data  grafana/otel-lgtm
```
