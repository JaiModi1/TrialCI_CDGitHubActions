FROM eclipse-temurin:21

COPY ./api-gateway-service/target/*.jar apiGateway.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "apiGateway.jar" ]