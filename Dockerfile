# Usando imagem oficial do OpenJDK
FROM openjdk:17-jdk-slim

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o jar para dentro do container
COPY target/semcitecsystem-1.0.0.jar app.jar

# Expõe a porta da aplicação (exemplo 8080)
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
