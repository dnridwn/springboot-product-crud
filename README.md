# Spring Boot MySQL Docker Project

This project is a Spring Boot application connected to a MySQL database and packaged using Docker. The application manages `Product` and `Category` entities with CRUD operations.

## Table of Contents

- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
    - [Using Docker Compose](#using-docker-compose)
    - [Running Manually](#running-manually)
- [Configuration](#configuration)
- [Database Initialization](#database-initialization)
- [API Endpoints](#api-endpoints)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java 8 or higher
- Maven
- Docker and Docker Compose (if running with Docker)
- MySQL (if running manually)

### Running the Application

#### Using Docker Compose

1. **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/your-repo.git
    cd your-repo
    ```

2. **Build the project:**
    ```sh
    mvn clean package
    ```

3. **Build and start the containers:**
    ```sh
    docker-compose up --build
    ```

4. The application should now be running at `http://localhost:8080`.

#### Running Manually

1. **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/your-repo.git
    cd your-repo
    ```

2. **Update `application.properties` with your MySQL database configuration:**
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database
    spring.datasource.username=your_user
    spring.datasource.password=your_password
    ```

3. **Build the project:**
    ```sh
    mvn clean install
    ```

4. **Run the application:**
    ```sh
    mvn spring-boot:run
    ```

5. The application should now be running at `http://localhost:8080`.

## Configuration

Configuration is handled in the `application.properties` file or via environment variables.

**Example `application.properties`:**
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/your_database}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:your_user}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:your_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

## API Endpoints

Please refer to [Swagger API Docs](./api-docs.yml)