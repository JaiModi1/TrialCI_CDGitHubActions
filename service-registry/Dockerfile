FROM eclipse-temurin:21
COPY target/*.jar serviceRegistry.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "serviceRegistry.jar" ]