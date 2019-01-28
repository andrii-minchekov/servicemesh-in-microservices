1. ./gradlew clean jibDockerBuild  
Build docker images locally.
2. ./gradlew clean jib  
Push docker images to configured $dockerRepository
3. cd docker && docker-compose up -d --scale order-service=2 --scale user-service=2  
Up and run docker containers with all the business and technical services.