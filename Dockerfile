FROM maven:latest as stage1
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:resolve
COPY . /app
RUN mvn clean
RUN mvn package

FROM openjdk:latest as final
COPY --from=stage1 /app/target/*.jar back.jar
EXPOSE 8080
CMD ["java", "-jar", "back.jar"]