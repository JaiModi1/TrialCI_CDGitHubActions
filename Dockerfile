FROM openjdk:21-oracle
COPY target/*.jar userService.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","userService.jar"]
