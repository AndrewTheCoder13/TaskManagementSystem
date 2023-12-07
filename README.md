
This is a simple Task Management System built with Spring Boot and Docker.

## Prerequisites

- Docker
- Docker Compose

## How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com//TaskManagementSystem.git

2. Navigate to the project directory:

   ```bash
    cd TaskManagementSystem

3. Build the Docker image:

    ```bash
    docker-compose build

4. Run the Docker container:
    ```bash
    docker-compose up

This will start the Spring Boot application along with a PostgreSQL database.

## Access the application:

Open your web browser and go to http://localhost:8080

API Documentation

### The API documentation is available at:

http://localhost:8080/swagger-ui.html

## Cleaning Up
To stop and remove the Docker containers, use the following command:

```bash
docker-compose down