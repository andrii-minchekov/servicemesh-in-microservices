This project demonstrate how to leverage [Service Mesh](https://linkerd.io/1/getting-started/docker/) Layer in Microservice Architecture.  

Build and run:
1. Build docker images locally
    ```bash
   ./gradlew clean jibDockerBuild
    ```
   or build and push docker images to configured $dockerRepository
   ```bash
   ./gradlew clean jib
   ``` 
2. Up and run docker containers with all the business and technical services
    ```bash
    cd docker && docker-compose up -d --scale order-service=2 --scale user-service=2
    ```
3. Test "get orders" endpoint works (request chain: api-gateway -> linkerd -> order-service -> linkerd -> user-service
    ```bash
    curl -v http://localhost:8070/order-service/api/orders
    ```
Run:
```bash
./gradlew clean jibDockerBuild && cd docker && docker-compose up -d --remove-orphans --scale order-service=2 --scale user-service=2
```

Stop:
```bash
cd docker && docker-compose stop
```

Project Architecture

![ArchitectureDiagram](doc/architecture.png)

Service Mesh Demo [Presentation](doc/Service-Mesh-Presentation.pdf)

Notes:  
Desired name of the dockerRepository should be configured in `gradle.properties`.

Tags:  
Example of Microservice Architecture with Spring Boot, Microservice Architecture with Service Mesh, Service Mesh example Java Kotlin, Service Mesh POC, Linkerd example Java, Linkerd Docker example