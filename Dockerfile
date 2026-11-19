# Etapa 1: compila o projeto com Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Etapa 2: roda só com o JRE (imagem menor)
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/rota-segura-api.jar app.jar

# O Render informa a porta pela variável PORT (a aplicação já lê ela)
EXPOSE 8080
# MaxRAMPercentage: evita estourar os 512 MB do plano free do Render
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-Duser.timezone=America/Sao_Paulo", "-jar", "app.jar"]
