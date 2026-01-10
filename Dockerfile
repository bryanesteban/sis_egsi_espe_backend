# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (esta capa se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación (genera WAR)
RUN mvn clean package -DskipTests

# Etapa de producción
FROM eclipse-temurin:17-jre-alpine

# Crear usuario no-root para ejecutar la aplicación
RUN addgroup -S spring && adduser -S spring -G spring

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el WAR desde la etapa de build
COPY --from=build /app/target/*.war app.war

# Cambiar propiedad del archivo
RUN chown spring:spring app.war

# Cambiar a usuario no-root
USER spring:spring

# Exponer puerto de la aplicación
EXPOSE 9090

# Variables de entorno por defecto (pueden ser sobrescritas por docker-compose)
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.war"]
