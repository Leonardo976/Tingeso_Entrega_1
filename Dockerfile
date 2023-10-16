# Usa la imagen oficial de OpenJDK
FROM openjdk:17

# Copia el archivo JAR de tu aplicación al contenedor
COPY target/monolithic-webapp-0.0.1-SNAPSHOT.jar /app/monolithic-webapp-0.0.1-SNAPSHOT.jar

# Expone el puerto de tu aplicación
EXPOSE 8090

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "/app/monolithic-webapp-0.0.1-SNAPSHOT.jar"]
