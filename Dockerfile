FROM openjdk:21
WORKDIR /app
ARG JAR_FILE=AgroMarket-0.0.1-SNAPSHOT.jar
COPY target/${JAR_FILE} /app/${JAR_FILE}

# Создаем директорию для изображений и устанавливаем права
RUN mkdir -p /app/images && \
    chmod 777 /app/images

CMD ["java", "-jar", "AgroMarket-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081

# Копируем изображения
COPY uploads/* /app/images/