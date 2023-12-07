# Task Management System

Task Management System is a web application for managing tasks, users, and comments. It provides functionality for creating tasks, assigning them to users, updating task status, and adding comments to tasks.


## Features

- User authentication and authorization
- Task creation, assignment, and status updates
- Commenting on tasks
- Pagination and filtering of tasks
- RESTful API for task management
- Swagger UI for API documentation
- Error handling and validation
- User registration and password encryption
- Role-based access control (Admin, User)
- Task priority management (High, Medium, Low)
- Task status management (TODO, IN_PROGRESS, DONE)
- Task priority and status filtering
- Comment creation and association with tasks
- Integration tests using Testcontainers
- Dockerized application for easy deployment
- Logging for monitoring and debugging


## Technologies Used

- **Spring Boot**: Framework for building Java-based enterprise applications.
- **Spring Security**: Authentication and access control framework.
- **Spring Data JPA**: Simplifies database access and management.
- **PostgreSQL**: Open-source relational database system.
- **Docker**: Platform for automating containerized applications.
- **Hibernate**: Object-relational mapping framework.
- **JSON Web Tokens (JWT)**: Token-based authentication for secure communication.
- **Lombok**: Library for reducing boilerplate code in Java.
- **JUnit and Testcontainers**: Testing frameworks for unit and integration testing.
- **Swagger UI**: Tool for visualizing and interacting with the API.
- **Java Enums**: Representing task priorities, statuses, and permissions.
- **Maven**: Project management and comprehension tool.
- **Git**: Version control system for tracking changes in the project.
- **GitHub Actions**: CI/CD pipeline for automating build and tests.
- **MIT License**: Open-source license for project distribution.

## Getting Started

To run the application locally using Docker, follow these steps:

## Prerequisites

- Docker
- Docker Compose

## How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/AndrewTheCoder13/TaskManagementSystem.git

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