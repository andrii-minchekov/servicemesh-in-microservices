FROM ${dockerRepository}/gateway-mirror
#RUN ln -s `which sh` /bin/sh

# Copy src files to non public dir
COPY conf/linkerd.yaml /var/linkerd/conf
COPY linkerd-1.6.0-exec .
#RUN ["wget", "https://github.com/linkerd/linkerd/releases/download/1.6.0/linkerd-1.6.0-exec"]
RUN ["chmod", "755", "linkerd-1.6.0-exec"]
#RUN ["sh", "linkerd-1.6.0-exec", "/var/linkerd/conf/linkerd.yaml"]

CMD ["java","-cp","/app/resources:/app/classes:/app/libs/*","com.example.gateway.GatewayApplication"]
#ENTRYPOINT [ "sh", "-c", "echo $HOME" ]
#ENTRYPOINT         "java",
#                   "-cp",
#                   "/app/resources:/app/classes:/app/libs/*",
#                   "com.example.gateway.GatewayApplication"

EXPOSE 4141
