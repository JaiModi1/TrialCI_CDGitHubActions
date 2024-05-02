FROM eclipse-temurin:21
COPY ./user-service/target/*.jar userService.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "userService.jar" ]