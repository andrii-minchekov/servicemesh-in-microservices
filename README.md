This project demonstrate how to leverage [Service Mesh](https://linkerd.io/1/getting-started/docker/) Layer in Microservice Architecture.  

### Build:
1. Build docker images locally
    ```bash
   ./gradlew clean jibDockerBuild
    ```
   or build and push docker images to configured $dockerRepository
   ```bash
   ./gradlew clean jib
   ``` 
Build separate service
   ```bash
   ./gradlew clean :order-service:jib -x test
   ``` 

### Run using docker-compose:
1. Up and run docker containers with all the business and technical services
    ```bash
    cd docker && docker-compose up -d --scale order-service=2 --scale user-service=2
    ```
2. Test "get orders" endpoint works (request chain: api-gateway -> linkerd -> order-service -> linkerd -> user-service
    ```bash
    curl -v http://localhost:8070/order-service/api/orders
    ```
One-liner Run:
```bash
./gradlew clean jibDockerBuild && cd docker && docker-compose up -d --remove-orphans --scale order-service=2 --scale user-service=2
```

Stop:
```bash
cd docker && docker-compose stop
```

### Run using Kubernetes cluster
1. Install order-service
   ```bash
   cd order-service && helm install order-service ./chart
   ```
   Uninstall order-service
   ```bash
   cd order-service && helm uninstall order-service
   ```
   
2. Install user-service
   ```bash
   cd user-service && helm install user-service ./chart
   ```
   Uninstall order-service
   ```bash
   cd user-service && helm uninstall user-service
   ```
3. Install all MSA infra
   ```bash
   helm install msa-infra ./msa-infra-chart
   ```
   Uninstall all MSA infra
   ```bash
   helm uninstall msa-infra
   ```

Project Architecture

![ArchitectureDiagram](doc/architecture.png)

Service Mesh Demo [Presentation](doc/Service-Mesh-Presentation.pdf)

Notes:  
Desired name of the dockerRepository should be configured in `gradle.properties`.

Tags:  
Example of Microservice Architecture with Spring Boot, Microservice Architecture with Service Mesh, Service Mesh example Java Kotlin, Service Mesh POC, Linkerd example Java, Linkerd Docker example