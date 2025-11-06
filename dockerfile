# ====== ETAPA 1: Compilación ======
FROM maven:3.9.9-eclipse-temurin-17 AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos del proyecto
COPY pom.xml .
# Descarga las dependencias para aprovechar la caché de Docker
RUN mvn dependency:go-offline -B

# Copia el resto del código fuente
COPY src ./src

# Compila el proyecto y genera el JAR sin ejecutar tests
RUN mvn clean package -DskipTests

# ====== ETAPA 2: Ejecución ======
FROM eclipse-temurin:17-jre-alpine

# Crea un usuario no root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Crea un directorio de trabajo
WORKDIR /app

# Copia el JAR desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]
