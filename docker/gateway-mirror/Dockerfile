FROM menya84/gateway-mirror

# Copy src files to non public dir
COPY conf/linkerd.yaml /var/linkerd/
RUN wget https://github.com/linkerd/linkerd/releases/download/1.6.0/linkerd-1.6.0-exec

RUN ./linkerd-1.6.0-exec /var/linkerd/config/linkerd.yaml

EXPOSE 4141
