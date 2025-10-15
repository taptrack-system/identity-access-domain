# identity-access-domain/identity-profiles/Dockerfile
FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app

# Copiar e instalar biblioteca local (caso exista)
COPY ../libraries/exceptions /libraries/exceptions
RUN mvn -f /libraries/exceptions/pom.xml clean install -DskipTests || true

# Copiar projeto
COPY pom.xml .
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/identity-profiles-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=dev,h2
ENV APP_PORT=8081
EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]