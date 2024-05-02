FROM eclipse-temurin:21
COPY target/*.jar userService.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "userService.jar" ]