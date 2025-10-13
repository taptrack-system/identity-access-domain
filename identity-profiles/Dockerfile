# Etapa 1: Build da aplicação
FROM maven:3.9.2-eclipse-temurin-25 AS build

WORKDIR /app

# Copiar arquivos do Maven
COPY pom.xml .
COPY src ./src

# Build da aplicação (packaging)
RUN mvn clean package -DskipTests

# Etapa 2: Criar imagem de runtime
FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app

# Copiar JAR gerado
COPY --from=build /app/target/identity-profiles-*.jar app.jar

# Variáveis de ambiente padrão (podem ser sobrescritas pelo .env ou docker-compose)
ENV SPRING_PROFILES_ACTIVE=h2,dev
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV APP_PORT=8081

# Expor porta
EXPOSE ${APP_PORT}

# Comando para rodar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]