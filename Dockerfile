# ==========================
# Etapa 1: Construcción del JAR
# ==========================
FROM maven:3.9-eclipse-temurin-17 AS builder

# Crea directorio de trabajo
WORKDIR /app

# Copia los archivos pom.xml y descarga dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente
COPY src ./src

# Compila el proyecto sin correr tests
RUN mvn clean package -DskipTests

# ==========================
# Etapa 2: Imagen final
# ==========================
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

ARG SERVER_PORT=8080
ENV SERVER_PORT=${SERVER_PORT}

COPY --from=builder /app/target/*.jar app.jar

EXPOSE ${SERVER_PORT}

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "app.jar"]
