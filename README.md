# Education Management System

A complete system for managing educational institutions, including students, teachers, classes, examinations, and more.

## Features

- Role-based access control (Admin, Teacher, Student)
- JWT-based authentication and authorization
- Student management
- Teacher management
- Class scheduling
- Attendance tracking
- Examination and grading system
- Tuition payment tracking
- Homeroom management
- Announcements for different user roles

## Architecture

- **Model**: Entity classes mapping to database tables
- **Repository**: Data access layer using Spring Data JPA
- **Service**: Business logic layer
- **Controller**: API endpoints for the frontend
- **Configuration**: Security settings and other configurations
- **DTO**: Data transfer objects for API requests/responses
- **Exception**: Custom exception classes

## Technology Stack

- Java 17
- Spring Boot
- Spring Security with JWT
- Spring Data JPA
- Hibernate
- MySQL Database
- Lombok
- Maven

## Database Schema

The system uses a relational database with the following main entities:

- User - Base user entity with common attributes
- Admin, Teacher, Student - User role-specific entities
- Class - Represents a course or subject being taught
- ClassSchedule - Weekly schedule for classes
- ClassStudent - Many-to-many relationship between students and classes
- Attendance - Class attendance records
- Examination - Tests and assessments
- TeacherInformation, StudentInformation - Extended information for users
- Homeroom - Class groups with dedicated teachers
- Announcement - System-wide or targeted announcements

## API Endpoints

### Authentication
- POST /api/v1/auth/login - Authenticate a user
- POST /api/v1/auth/refresh - Refresh access token

### Admin specific
- GET /api/v1/admin/dashboard - Get admin dashboard data
- Various endpoints for user management

### Teacher specific
- GET /api/v1/teacher/classes - Get classes for a teacher
- GET /api/v1/teacher/homerooms - Get homerooms for a teacher

### Student specific
- GET /api/v1/student/classes - Get classes for a student
- GET /api/v1/student/examinations - Get examinations for a student
- GET /api/v1/student/tuition - Get tuition information for a student

### Announcements
- GET /api/v1/announcements - Get announcements for current user
- GET /api/v1/announcements/admin/create - Create announcement (admin only)
- GET /api/v1/announcements/admin/manage - Manage announcements (admin only)
- GET /api/v1/announcements/teacher - Get teacher announcements
- GET /api/v1/announcements/student - Get student announcements

## Getting Started

1. Clone the repository
2. Configure database settings in `application.properties`
3. Run the application using `./mvnw spring-boot:run`
4. Access the API at `http://localhost:8080`

## Security

The application uses JWT tokens for authentication with the following security features:

- Access tokens (24-hour validity)
- Refresh tokens (7-day validity)
- Role-based authorization
- Token revocation
- Stateless authentication

## Contributors

This project was developed as a complete solution for educational institution management.
