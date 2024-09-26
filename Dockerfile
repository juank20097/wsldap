# Etapa de construcción
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml ./ 
COPY src ./src

RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia el archivo JAR generado desde la etapa anterior (usando un comodín)
COPY --from=build /app/target/*.jar /wsldap.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/wsldap.jar"]
