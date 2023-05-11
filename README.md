# REST API для сервиса вопросов и ответов

Доступна регистрация и аутентификация.

У пользователей есть следующие возможности:
* Посмотреть список досок
* Посмотреть список вопросов на доске
* Посмотреть конкретный вопрос
* Создать вопрос на одной из досок
* Редактировать / удалить свой вопрос
* Ответить на чужой вопрос
* Редактировать / удалить свой ответ

Пользователи с ролью ADMIN также могут:
* Создать / редактировать / удалить доску
* Редактировать / удалить любой вопрос
* Редактировать / удалить любой ответ

## Требования:
Java 17

PostgreSQL

## Запуск:
1) ```git clone https://github.com/r1411/AskAPI.git```
2) ```cd AskAPI```
3) Настроить информацию о БД и секрет JWT в application.properties 
4) ```./mvnw package```
5) ```java -jar target/*.jar```

## Документация
После запуска документация доступна по пути /swagger-ui.html
