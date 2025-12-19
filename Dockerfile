# Importación del JDK y copia de los archivos necesarios
FROM openjdk:19-jdk AS build
WORKDIR /app
COPY pom.xml .
COPY src src

# Copiar el contenedor de Maven
COPY mvnw .
COPY .mvn .mvn

# Establecer el permiso de ejecución para el contenedor de Maven
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Etapa 2: Crear la imagen final de Docker con OpenJDK 19
 FROM openjdk:19-jdk
VOLUME /tmp

# Copiar el JAR desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT [ "java" , "-jar" , "/app.jar" ]
EXPOSE 8080