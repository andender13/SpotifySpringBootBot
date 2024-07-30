# Используем официальный образ OpenJDK
FROM openjdk:22-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл JAR в контейнер
COPY target/SpringBootMusicTgBot-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
