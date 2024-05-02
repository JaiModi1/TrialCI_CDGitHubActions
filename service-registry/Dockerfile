FROM eclipse-temurin:21
COPY ./service-registry/target/*.jar serviceRegistry.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "serviceRegistry.jar" ]