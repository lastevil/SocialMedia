FROM maven:3.8.1-jdk-11-slim AS build
COPY /src /socialApi/src
COPY /pom.xml /socialApi
RUN mvn -f /socialApi/pom.xml clean package

FROM openjdk:11
COPY --from=build /socialApi/target/SocialMedia-0.0.1-SNAPSHOT.jar SocialMedia-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","SocialMedia-0.0.1-SNAPSHOT.jar"]