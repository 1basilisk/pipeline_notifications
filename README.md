# Pipeline Notification Service

## Project overview

The Pipeline Notification Service is a Spring Boot application that tracks pipeline events and sends notifications when a configured rule matches a pipeline event. It is designed for teams that want to monitor pipeline health across environments such as DEV, STAGE, and PROD.

The application exposes REST endpoints for creating and retrieving pipeline events, notification rules, and generated notifications. It stores data in a PostgreSQL database and uses Spring Data JPA for persistence.

## Architecture

This project follows a classic layered Spring Boot architecture, with a simple domain-driven flow for pipeline monitoring and notifications.

### 1. Presentation layer

The controllers live under `com.basilisk.pipeline_notification_service.controller` and are responsible for exposing REST endpoints. Each controller receives incoming HTTP requests, validates the body using request DTOs, and forwards the work to the appropriate service.

Examples:

- `PipelineEventController` handles event creation and lookup
- `NotificationRuleController` handles rule creation and lookup
- `NotificationController` exposes notification history

### 2. Application/service layer

The service layer contains the business logic and orchestrates persistence and notification creation.

- `PipelineEventService` is the core workflow service. When a pipeline event is created, it:
  1. saves the event
  2. loads enabled notification rules
  3. compares the event against each rule
  4. creates a notification record if the rule matches

- `NotificationRuleService` contains the rule lookup and matching logic, including the comparison of:
  - `pipelineName`
  - `environment`
  - `status`

- `NotificationService` manages persisted notification records and retrieval queries.

### 3. Persistence layer

The repositories are Spring Data JPA interfaces and provide database access without custom SQL implementations.

- `PipelineEventRepository`
- `NotificationRuleRepository`
- `NotificationRepository`

These repositories handle CRUD operations for the domain entities and support simple query methods like `findByEnabledTrue()`.

### 4. Domain model

The domain entities model the application’s core objects:

- `PipelineEvent`: represents a pipeline execution result such as success, failure, or aborted status.
- `NotificationRule`: defines a trigger condition for notifications, based on pipeline name, environment, and status.
- `Notification`: stores the generated notification message and links it to both the event and the matching rule.

Key enums:

- `Environment`: `DEV`, `STAGE`, `PROD`
- `PipelineStatus`: `SUCCESS`, `FAILURE`, `ABORTED`

### 5. Request and validation layer

Request DTOs in `com.basilisk.pipeline_notification_service.dto` are used to map incoming JSON payloads into validated objects.

Examples:

- `CreatePipelineEventRequest`
- `CreateNotificationRuleRequest`

Each request uses Jakarta validation annotations such as `@NotBlank` and `@NotNull` to ensure required fields are present before processing.

### 6. Error handling layer

`GlobalExceptionHandler` provides centralized exception handling for common API failures and returns structured error responses through `ErrorResponse`.

This keeps controller methods focused on request flow while giving a standard response format for errors.

### 7. End-to-end flow

A typical workflow looks like this:

```text
Client sends POST /api/events
        ↓
PipelineEventController receives request
        ↓
CreatePipelineEventRequest is mapped and validated
        ↓
PipelineEventService.createEvent(event)
        ↓
Event is saved to the database
        ↓
All enabled notification rules are evaluated
        ↓
Matching rules trigger Notification creation
        ↓
Notification records are persisted
        ↓
Client receives the created event response
```

This flow makes the application a straightforward event-driven notification service where pipeline incidents can automatically generate alerts based on predefined rules.

Main packages:

- `com.basilisk.pipeline_notification_service.controller`
- `com.basilisk.pipeline_notification_service.service`
- `com.basilisk.pipeline_notification_service.repository`
- `com.basilisk.pipeline_notification_service.entity`
- `com.basilisk.pipeline_notification_service.dto`
- `com.basilisk.pipeline_notification_service.exception`

## Technologies used

- Java 21
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- PostgreSQL
- Validation
- Maven
- JUnit 5 + Spring Boot Test
- H2 (for tests)

## Features

- Create and fetch pipeline events
- Create and fetch notification rules
- View all notifications generated for matching events
- Match events to enabled rules based on:
  - pipeline name
  - environment
  - status
- Persist notifications when a rule matches a pipeline event
- REST API with validation for request payloads
- Automatic app startup and test support with Spring profiles

## API endpoints

### Pipeline events

- `POST /api/events` — create a pipeline event
- `GET /api/events` — get all pipeline events
- `GET /api/events/{id}` — get a specific pipeline event

### Notification rules

- `POST /api/rules` — create a notification rule
- `GET /api/rules` — get all notification rules
- `GET /api/rules/{id}` — get a specific notification rule

### Notifications

- `GET /api/notifications` — get all notifications
- `GET /api/notifications/{id}` — get a specific notification

### Home

- `GET /` — serves the static application landing page

## How to run locally

### Prerequisites

- Java 21+
- Maven
- PostgreSQL running locally

### 1. Configure the database

The application expects PostgreSQL to be running and configured in `src/main/resources/application.properties`.

Current configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pipeline_notification
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Set your password before running the app:

```powershell
$env:DB_PASSWORD="your_postgres_password"
```

### 2. Start PostgreSQL

Create a local database named `pipeline_notification` if it does not already exist.

Example:

```sql
CREATE DATABASE pipeline_notification;
```

### 3. Run the application

From the project root:

```powershell
./mvnw.cmd spring-boot:run
```

The app will start on the default Spring Boot port:

- `http://localhost:8080`

## Database setup

This service uses JPA entities mapped to tables automatically by Hibernate.

The entities included are:

- `PipelineEvent`
- `NotificationRule`
- `Notification`

The database schema is created/updated with:

```properties
spring.jpa.hibernate.ddl-auto=update
```

For test execution, the project includes an H2 profile at `src/test/resources/application-test.properties`, which is used to avoid depending on a local PostgreSQL instance during automated tests.

## Example API requests

### Create a notification rule

```bash
curl -X POST http://localhost:8080/api/rules \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Deploy failure alert",
    "pipelineName": "build-service",
    "environment": "PROD",
    "status": "FAILURE",
    "enabled": true
  }'
```

### Create a pipeline event

```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "pipelineName": "build-service",
    "jobName": "deploy-job",
    "environment": "PROD",
    "status": "FAILURE",
    "errorMessage": "Build failed",
    "occurredAt": "2026-08-30T10:15:00"
  }'
```

### Get all rules

```bash
curl http://localhost:8080/api/rules
```

### Get all events

```bash
curl http://localhost:8080/api/events
```

### Get all notifications

```bash
curl http://localhost:8080/api/notifications
```

## How notification rules work

Notification rules define when a pipeline event should trigger a notification.

Each rule includes:

- `name`
- `pipelineName`
- `environment`
- `status`
- `enabled`

The matching logic is checked in `NotificationRuleService.matches(...)`:

```java
return rule.getPipelineName().equals(event.getPipelineName())
        && rule.getEnvironment() == event.getEnvironment()
        && rule.getStatus() == event.getStatus();
```

When a pipeline event is created:

1. The event is saved.
2. All enabled notification rules are loaded.
3. Matching rules are evaluated against the created event.
4. For each matching rule, a `Notification` record is created with a generated message such as:

```text
Pipeline build-service has status FAILURE in PROD
```

If a rule is disabled, it will not trigger a notification.

## How to run tests

### Run the full test suite

```powershell
./mvnw.cmd test
```

### Run tests with the H2 profile

```powershell
./mvnw.cmd -Dspring.profiles.active=test test
```

This project includes a test profile with H2 in-memory database configuration to make automated tests easier and not dependent on a local PostgreSQL installation.

## Notes

- The project contains a basic static home page served from `src/main/resources/static/index.html`.
- The application is small and focused, making it easy to extend with email/SMS integrations, rule filters, or a UI dashboard later.
