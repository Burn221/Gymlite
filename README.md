Gymlite REST API
---

Чистый и расширяемый REST API для управления зонами зала, оборудованием и бронированиями. Проект демонстрирует современный стек Spring: слоистая архитектура, DTO и валидация, пагинация, MapStruct и документация OpenAPI/Swagger UI.

---

Возможности
---
-CRUD для Зон / Оборудования / Бронирований

-Пагинация и сортировка через Pageable

-DTO (Java Records) + Bean Validation

-MapStruct: маппинг сущностей - DTO

-Глобальный обработчик ошибок (единый формат ответа)

-OpenAPI / Swagger UI из коробки

-JUnit, Mockito тестинг

Профилируемая конфигурация (application.yaml)

---

Технологический стек
---
-Java 17+, Spring Boot 3.x

-Spring Web, Spring Data JPA (Hibernate)

-MapStruct, Jakarta Validation

-springdoc-openapi (Swagger UI)

-JUnit, Mockito

-PostgreSQL или H2 для демо

---

Быстрый старт
---

1) H2
```bash
mvn spring-boot:run (H2)
# Swagger UI → http://localhost:8080/swagger-ui.html
```

2) Локально постгрес:
   application.yml
```bash
spring:
    datasource:
      url: jdbc:postgresql://localhost:5432/gymlite
      username: gymlite
      password: gymlite
    jpa:
      hibernate:
        ddl-auto: update   
      show-sql: true

server:
      port: 8080

springdoc:
  api-docs.path: /v3/api-docs
  swagger-ui.path: /swagger-ui.html
```

Старт: mvn spring-boot:run

---

#Обзор API
---

Базовый префикс: /api/v1
Интерактивная документация: /swagger-ui.html

Zones:
---
GET    /api/v1/zones?active=true
GET    /api/v1/zones/{id}
GET    /api/v1/zones/by-name/{name}
GET    /api/v1/zones/active-zone/{id}
POST   /api/v1/zones
PUT    /api/v1/zones/{id}
PATCH  /api/v1/zones/activate/{id}
PATCH  /api/v1/zones/deactivate/{id}
DELETE /api/v1/zones/{id}

Пример ZoneCreateRequest:
```
{
  "zoneName": "Cardio",
  "description": "Zone for cardio exercises",
  "active": true
}
```

Пример ZoneUpdateRequest:
```
{
  "id": 1,
  "zoneName": "string",
  "description": "string",
  "active": true
}
```

Пример ZoneResponse
```
{
  "id": 0,
  "zoneName": "string",
  "description": "string",
  "active": true,
  "createdAt": "2025-08-31T20:33:37.658Z",
  "updatedAt": "2025-08-31T20:33:37.658Z"
}
```
---

Equipment:
---
GET    /api/v1/equipment?active=true&page=0&size=20&sort=id,asc
GET    /api/v1/equipment/{id}
GET    /api/v1/equipment/{zoneId}/all-equipment-zone
GET    /api/v1/equipment/{zoneId}/all-equipment-zone-active
POST   /api/v1/equipment
PUT    /api/v1/equipment/{id}
PATCH  /api/v1/equipment/activate/{id}
PATCH  /api/v1/equipment/deactivate/{id}
DELETE /api/v1/equipment/{id}

Пример EquipmentCreateRequest:
```
{
  "zoneId": 1,
  "equipmentName": "barbell",
  "description": "equipment for bench",
  "price": 100,
  "active": true
}
```

Пример EquipmentUpdateRequest:
```
{
  "equipmentId": 0,
  "zoneId": 0,
  "equipmentName": "string",
  "description": "string",
  "price": 0,
  "active": true
}
```

Пример EquipmentResponse:
```
{
  "id": 0,
  "zoneId": 0,
  "equipmentName": "string",
  "description": "string",
  "price": 0,
  "active": true,
  "createdAt": "2025-08-31T20:39:30.520Z",
  "updatedAt": "2025-08-31T20:39:30.520Z"
}
```
---

Bookings:
---
GET     /api/v1/bookings?page=0&size=20&sort=id,desc
GET     /api/v1/bookings/by-user/{userName}
GET     /api/v1/bookings/by-equipment/{equipmentId}
GET     /api/v1/bookings/by-equipment-and-time-period/{equipmentId}?start=2025-08-20T11:00&end=2025-08-20T12:00
POST    /api/v1/bookings
PUT     /api/v1/bookings/{id}
PATCH   /api/v1/bookings/cancel/{id}
PATCH   /api/v1/bookings/complete/{id}
DELETE  /api/v1/bookings/{id}
DELETE  /api/v1/bookings/by-status/{bookingStatus}

Пример BookingCreateRequest:
```
{
  "equipmentId": 2,
  "userName": "john.doe",
  "startTime": "2025-08-28T11:00",
  "endTime": "2025-08-28T12:00",
  "bookingStatus": "NEW"
}
```

Пример BookingUpdateRequest:
```
{
  "bookingId": 0,
  "equipmentId": 0,
  "userName": "string",
  "startTime": "2025-08-31T20:41:08.552Z",
  "endTime": "2025-08-31T20:41:08.552Z",
  "bookingStatus": "BookingStatus.BOOKED"
}
```

Пример BookingResponse:
```
{
      "id": 0,
      "zoneId": 0,
      "equipmentName": "string",
      "description": "string",
      "price": 0,
      "active": true,
      "createdAt": "2025-08-31T20:41:49.739Z",
      "updatedAt": "2025-08-31T20:41:49.739Z"
    }
```
---


Структура проекта
---
src
└── main
    ├── java/ru/burn221/gymlite
    │   ├── controller     # REST-эндпоинты (Zones, Equipment, Bookings)
    │   ├── service        # бизнес-логика (интерфейсы + реализации)
    │   ├── repository     # Spring Data JPA репозитории
    │   ├── dto            # запросы/ответы (records + validation)
    │   ├── mapper         # MapStruct мапперы
    │   ├── model          # JPA-сущности, enum'ы
    │   └── configuration  # OpenAPI-конфиг и пр.
    └── resources
        ├── application.yaml
        └── static/        # опционально: мини-фронт (index.html)

---

Тестирование
---
Unit тесты с Mockito и JUnit

Запуск: mvn test

---

Документация API
---
Документация API

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: /v3/api-docs

OpenAPI YAML: /v3/api-docs.yaml

