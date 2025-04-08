# Stage 1: Сборка приложения с использованием Maven и JDK 17
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
# Сначала копируем pom.xml для кэширования зависимостей
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Затем копируем исходный код приложения
COPY src ./src
# Собираем проект (без тестов для ускорения сборки)
RUN mvn clean package -DskipTests

# Stage 2: Запуск приложения с использованием OpenJDK 17
FROM openjdk:17
WORKDIR /app
# Копируем скомпилированный jar из этапа сборки
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
