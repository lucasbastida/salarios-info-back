# Salario.info backend

Provides api for persisting anonymous salary information and displaying salary statistics

## Getting Started

### Prerequisites

* [Java 17 or higher](https://adoptium.net/)
* [Maven](https://maven.apache.org/) (Optional as this project ships with Maven wrapper)

### Running the Application on Your Local Machine

* Make sure you have Docker running (`docker info`) and Docker Compose (`docker compose`)
* Run `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` to start the application
* Access http://localhost:8080/api/swagger-ui.html in your browser

### Application Profiles

- `dev` running the application locally for development. Infrastructure components are started
  with [spring boot docker compose support](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.docker-compose).
- `aws` running the application inside AWS. This requires the whole infrastructure setup inside your AWS account.

### Running the Tests

Run `./mvnw clean verify` from the command line.

## Architecture

### Infrastructure diagram

![infrastructure diagram][infrastructure-diagram]

[infrastructure-diagram]:https://github.com/lucasbastida/salarios-info-back/raw/main/docs/aws-infra.png "infrastructure diagram"

## Built with

* [Spring Boot](https://projects.spring.io/spring-boot/) and starters: Spring Web MVC, Spring Data JPA, Spring
  Validation, Spring Security, Actuator
* [reCAPTCHA v3](https://developers.google.com/recaptcha/docs/v3)

