# Course Management System

A Spring Boot web application for managing online courses with user authentication and role-based access control.

## 📋 Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Database](#database)
- [Project Structure](#project-structure)
- [Contributing](#contributing)

## ✨ Features

- **User Authentication & Authorization**
  - User registration and login
  - Role-based access control (USER, ADMIN)
  - Secure password encryption

- **Course Management**
  - Browse available courses
  - Search courses by title
  - View course details
  - Admin course management

- **Security**
  - Spring Security integration
  - Protected routes
  - CSRF protection

- **Database**
  - H2 in-memory database for development
  - File-based persistence
  - JPA/Hibernate for data management

## Visualize
### Home Page
<img width="1845" height="860" alt="image" src="https://github.com/user-attachments/assets/f7f9ecee-4839-4dab-9f5e-75b937296d8a" />

### Register Account
<img width="1822" height="956" alt="image" src="https://github.com/user-attachments/assets/f7b611ea-13f8-4195-aecb-241a002edec3" />

### Login Account
<img width="1837" height="953" alt="image" src="https://github.com/user-attachments/assets/e49f3ac3-3add-4ed8-8ed8-9d488347b554" />

### Welcome Page
<img width="1827" height="957" alt="image" src="https://github.com/user-attachments/assets/048bf21a-81bf-4849-b6b8-dc894feed48e" />

### Courses Page
<img width="1822" height="958" alt="image" src="https://github.com/user-attachments/assets/2fd53048-7b0e-4d1b-ad29-7e4eadd6ddbe" />

### Admin Page (/admin)
<img width="1823" height="960" alt="image" src="https://github.com/user-attachments/assets/8e4df7aa-c856-4e57-9cee-a2d2a65694f4" />

## 🛠 Technologies Used

- **Backend:**
  - Java 24
  - Spring Boot 3.5.5
  - Spring Security
  - Spring Data JPA
  - Maven

- **Database:**
  - H2 Database (embedded)
  - Hibernate ORM

- **Build Tool:**
  - Maven

- **Development:**
  - Lombok (for reducing boilerplate code)
  - Spring Boot DevTools

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- Java Development Kit (JDK) 24 or higher
- Maven 3.6+ (or use the included Maven Wrapper)
- Git (for cloning the repository)

## 🚀 Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/HSandz/PTUDDN.git
   cd PTUDDN
   ```

2. **Build the project:**
   ```bash
   # On Windows
   .\mvnw.cmd clean install
   
   # On macOS/Linux
   ./mvnw clean install
   ```

## ▶️ Running the Application

### Using Maven Wrapper (Recommended)

```bash
# On Windows
.\mvnw.cmd spring-boot:run

# On macOS/Linux
./mvnw spring-boot:run
```

### Using Maven (if installed globally)

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📖 Usage

### Default User Accounts

The application comes with pre-configured user accounts:

- **Admin Account:**
  - Username: `admin`
  - Password: `admin123`
  - Role: ADMIN

- **Regular User Account:**
  - Username: `user`
  - Password: `user123`
  - Role: USER

### Available Endpoints

- **Home:** `http://localhost:8080/`
- **Login:** `http://localhost:8080/login`
- **Registration:** `http://localhost:8080/register`
- **Courses:** `http://localhost:8080/courses` (authenticated users only)
- **Admin Panel:** `http://localhost:8080/admin` (admin only)
- **H2 Console:** `http://localhost:8080/h2-console` (development only)

### H2 Database Console

For development purposes, you can access the H2 database console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/coursedb`
- Username: `sa`
- Password: `password`

## 📊 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/login` | Login page | Public |
| POST | `/login` | Process login | Public |
| GET | `/register` | Registration page | Public |
| POST | `/register` | Process registration | Public |
| POST | `/logout` | Logout | Authenticated |

### Course Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/courses` | List all courses | USER, ADMIN |
| GET | `/courses/{id}` | View course details | USER, ADMIN |
| GET | `/courses?search={query}` | Search courses | USER, ADMIN |

### Admin Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| GET | `/admin` | Admin dashboard | ADMIN |
| GET | `/admin/courses` | Manage courses | ADMIN |
| POST | `/admin/courses` | Create new course | ADMIN |
| PUT | `/admin/courses/{id}` | Update course | ADMIN |
| DELETE | `/admin/courses/{id}` | Delete course | ADMIN |

## 🗄️ Database

### Database Schema

The application uses the following main entities:

#### Users Table
- `id` (Primary Key)
- `username` (Unique)
- `password` (Encrypted)
- `email`
- `full_name`
- `enabled`

#### Courses Table
- `id` (Primary Key)
- `title`
- `description`
- `instructor`
- `duration` (in hours)
- `price`

#### Roles Table
- `id` (Primary Key)
- `name` (ROLE_USER, ROLE_ADMIN)

#### User-Role Relationships
- Many-to-Many relationship between Users and Roles

### Sample Data

The application automatically initializes with sample course data and user accounts on first run.

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/
│   │   │   ├── DataInitializer.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AdminController.java
│   │   │   ├── CourseController.java
│   │   │   ├── HomeController.java
│   │   │   └── RegistrationController.java
│   │   ├── entity/
│   │   │   ├── Course.java
│   │   │   ├── Role.java
│   │   │   └── User.java
│   │   ├── repository/
│   │   │   ├── CourseRepository.java
│   │   │   ├── RoleRepository.java
│   │   │   └── UserRepository.java
│   │   ├── service/
│   │   │   ├── CourseService.java
│   │   │   ├── CustomUserDetailsService.java
│   │   │   └── UserService.java
│   │   └── DemoApplication.java
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
└── test/
    └── java/com/example/demo/
        └── DemoApplicationTests.java
```

## 🛠️ Development

### Running Tests

```bash
# On Windows
.\mvnw.cmd test

# On macOS/Linux
./mvnw test
```

### Building for Production

```bash
# On Windows
.\mvnw.cmd clean package

# On macOS/Linux
./mvnw clean package
```

The JAR file will be created in the `target/` directory.

### Running the JAR file

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

If you encounter any issues or have questions, please:
1. Check the [Issues](https://github.com/HSandz/PTUDDN/issues) page
2. Create a new issue if your problem isn't already reported
3. Provide detailed information about the error and steps to reproduce

## 🚀 Future Enhancements

- [ ] Add course enrollment functionality
- [ ] Implement course progress tracking
- [ ] Add file upload for course materials
- [ ] Create REST API endpoints
- [ ] Add email verification for registration
- [ ] Implement course categories and filtering
- [ ] Add payment integration
- [ ] Create mobile-responsive design

---

**Happy Coding! 🎉**
