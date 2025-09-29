# 📝 Assignment 3 - Blog Management API

A comprehensive **SpringBoot REST API** for blog management with **JWT authentication** and **role-based authorization**.

## 🚀 Features

### ✅ **REST API CRUD Operations**
- **Users Management**: GET, POST, PUT, DELETE endpoints
- **Blogs Management**: GET, POST, PUT, DELETE endpoints
- **Public & Protected** endpoints with proper authorization

### 🔐 **Authentication & Authorization**
- **JWT Token-based Authentication** 
- **Role-based Access Control** (ADMIN/USER roles)
- **Spring Security** integration
- **Password Encryption** with BCrypt

### 🛡️ **Security Features**
- **ADMIN** can manage all users and blogs
- **USER** can only view/update their own blogs
- **Protected endpoints** require valid JWT tokens
- **Input validation** and **error handling**

### 🗄️ **Database**
- **H2 In-memory Database** for development
- **JPA/Hibernate** for data persistence
- **Automatic schema generation**

## 🏗️ Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | 24 | Programming Language |
| **Spring Boot** | 3.5.6 | Web Framework |
| **Spring Security** | 6.x | Security Framework |
| **Spring Data JPA** | 6.x | Data Access Layer |
| **JWT** | 0.12.3 | Token Authentication |
| **H2 Database** | Runtime | In-memory Database |
| **Maven** | 3.9+ | Build Tool |
| **Bean Validation** | 3.x | Input Validation |

## 📋 API Endpoints

### 🔓 **Public Endpoints**
```http
GET    /api/blogs              # Get all blogs
GET    /api/blogs/{id}         # Get blog by ID  
GET    /api/blogs/user/{id}    # Get blogs by user ID
POST   /api/auth/signup        # User registration
POST   /api/auth/signin        # User login
```

### 🔒 **Protected Endpoints**

#### **User Endpoints** (Authenticated)
```http
GET    /api/blogs/my           # Get my blogs
POST   /api/blogs              # Create new blog
PUT    /api/blogs/{id}         # Update my blog
DELETE /api/blogs/{id}         # Delete my blog
```

#### **Admin Endpoints** (ADMIN role only)
```http
GET    /api/users              # Get all users
GET    /api/users/{id}         # Get user by ID
PUT    /api/users/{id}         # Update user
DELETE /api/users/{id}         # Delete user
```

## 🚀 Quick Start

### Prerequisites
- **Java 24+** installed
- **Maven 3.9+** installed
- **Git** (optional)

### 1. Clone Repository
```bash
git clone <your-repo-url>
cd assignment_3
```

### 2. Run Application
```bash
# Using Maven
mvn spring-boot:run

# Or compile first
mvn clean compile
mvn spring-boot:run
```

### 3. Application URLs
- **API Base URL**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## 🧪 API Testing

### **Method 1: cURL Commands**

#### 1. Register Users
```bash
# Register regular user
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "email": "user1@example.com", "password": "123456"}'

# Register admin user  
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username": "admin1", "email": "admin1@example.com", "password": "123456", "role": "ADMIN"}'
```

#### 2. Login & Get Token
```bash
# Login as user
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username": "user1", "password": "123456"}'

# Save the token from response for next requests
```

#### 3. Test Protected Endpoints
```bash
# Create blog (replace YOUR_TOKEN with actual token)
curl -X POST http://localhost:8080/api/blogs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"title": "My First Blog", "content": "This is my blog content"}'

# Get my blogs
curl -X GET http://localhost:8080/api/blogs/my \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **Method 2: Postman**
1. Import `Postman_Collection.json` file
2. Collection includes all endpoints with automatic token management
3. Test scenarios for all CRUD operations

### **Method 3: VS Code REST Client**
1. Install **REST Client** extension
2. Use `api-tests.http` file for testing
3. Click "Send Request" on each endpoint

### **Method 4: Automated Testing**
```bash
# Run integration tests
mvn test -Dtest=ApiIntegrationTest
```

## 📖 Usage Examples

### **1. User Registration & Authentication**
```bash
# Step 1: Register
POST /api/auth/signup
{
  "username": "testuser",
  "email": "test@example.com", 
  "password": "123456"
}

# Step 2: Login
POST /api/auth/signin  
{
  "username": "testuser",
  "password": "123456"
}

# Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "testuser",
  "email": "test@example.com", 
  "role": "USER"
}
```

### **2. Blog Management**
```bash
# Create Blog
POST /api/blogs
Authorization: Bearer <token>
{
  "title": "Spring Boot Tutorial",
  "content": "Learn how to build REST APIs with Spring Boot..."
}

# Update Blog (only owner or admin)
PUT /api/blogs/1
Authorization: Bearer <token>
{
  "title": "Updated Title",
  "content": "Updated content..."
}
```

### **3. Admin Operations**
```bash
# Get all users (admin only)
GET /api/users
Authorization: Bearer <admin-token>

# Delete user (admin only)
DELETE /api/users/2
Authorization: Bearer <admin-token>
```

## 🔒 Security & Permissions

### **Role-Based Access Control**

| Endpoint | USER | ADMIN | Public |
|----------|------|-------|---------|
| `GET /api/blogs` | ✅ | ✅ | ✅ |
| `POST /api/blogs` | ✅ | ✅ | ❌ |
| `PUT /api/blogs/{id}` | ✅ (own) | ✅ (all) | ❌ |
| `DELETE /api/blogs/{id}` | ✅ (own) | ✅ (all) | ❌ |
| `GET /api/users` | ❌ | ✅ | ❌ |
| `DELETE /api/users/{id}` | ❌ | ✅ | ❌ |

### **JWT Token Security**
- **256-bit Secret Key** for token signing
- **24-hour Token Expiration** 
- **Stateless Authentication** 
- **Automatic Token Validation** on protected routes

## ✅ Validation Rules

### **User Registration**
- Username: 3-50 characters, unique
- Email: Valid email format, unique  
- Password: Minimum 6 characters
- Role: USER (default) or ADMIN

### **Blog Creation**
- Title: Required, max 200 characters
- Content: Required, no length limit
- User: Auto-assigned from JWT token

## 🗂️ Project Structure

```
src/
├── main/
│   ├── java/com/example/assignment_3/
│   │   ├── controller/          # REST Controllers
│   │   │   ├── AuthController.java
│   │   │   ├── BlogController.java  
│   │   │   └── UserController.java
│   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── BlogRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── RegisterRequest.java
│   │   │   └── JwtResponse.java
│   │   ├── entity/              # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Blog.java
│   │   │   └── Role.java
│   │   ├── repository/          # Data Repositories  
│   │   │   ├── UserRepository.java
│   │   │   └── BlogRepository.java
│   │   ├── service/             # Business Logic
│   │   │   ├── UserService.java
│   │   │   ├── BlogService.java
│   │   │   └── UserDetailsServiceImpl.java
│   │   ├── security/            # Security Components
│   │   │   ├── JwtUtils.java
│   │   │   └── AuthTokenFilter.java
│   │   ├── config/              # Configuration
│   │   │   └── WebSecurityConfig.java
│   │   └── Assignment3Application.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/assignment_3/
        └── ApiIntegrationTest.java
```

## 📊 Database Schema

### **Users Table**
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| username | VARCHAR(50) | UNIQUE, NOT NULL |
| email | VARCHAR(255) | UNIQUE, NOT NULL |  
| password | VARCHAR(255) | NOT NULL (BCrypt) |
| role | VARCHAR(20) | ENUM(USER, ADMIN) |
| created_at | TIMESTAMP | DEFAULT NOW() |

### **Blogs Table**  
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| title | VARCHAR(200) | NOT NULL |
| content | TEXT | NOT NULL |
| user_id | BIGINT | FOREIGN KEY → users(id) |
| created_at | TIMESTAMP | DEFAULT NOW() |
| updated_at | TIMESTAMP | DEFAULT NOW() |

## 🐛 Troubleshooting

### **Common Issues**

#### 1. **403 Forbidden**
- **Cause**: Invalid/expired token or insufficient permissions
- **Solution**: Login again to get fresh token, check role permissions

#### 2. **401 Unauthorized**
- **Cause**: Missing or invalid Authorization header
- **Solution**: Include `Authorization: Bearer <token>` header

#### 3. **JWT Key Error**
- **Cause**: JWT secret key too short
- **Solution**: Already fixed with 256-bit Base64 encoded key

#### 4. **Database Connection**
- **Cause**: H2 database initialization issue
- **Solution**: Restart application, check H2 console

### **Debug Commands**
```bash
# Check if server is running
curl -X GET http://localhost:8080/api/blogs

# Verify H2 database  
# Visit: http://localhost:8080/h2-console

# View application logs
# Check console output for detailed error messages
```

## 📁 Additional Files

- **`API_TEST.md`**: Complete cURL testing guide
- **`TEST_GUIDE.md`**: Multiple testing methods (cURL, Postman, VS Code)  
- **`Postman_Collection.json`**: Ready-to-import Postman collection
- **`api-tests.http`**: VS Code REST Client test file
- **`FIX_403_ERROR.md`**: Troubleshooting guide for permission issues

## 🔧 Development

### **Build Commands**
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Run specific test
mvn test -Dtest=ApiIntegrationTest
```

### **Environment Configuration**
All configurations are in `application.properties`:
- Server port: 8080
- Database: H2 in-memory
- JWT expiration: 24 hours
- Logging: Console output

## 📝 License

This project is for educational purposes as part of Assignment 3.

## 👨‍💻 Author

**Student Name**: [Your Name]  
**Course**: PTUDDN (Phát Triển Ứng Dụng Đa Nền)  
**Assignment**: 3 - SpringBoot REST API with JWT Authentication

---

## ⭐ Key Achievement

✅ **Complete REST API** with CRUD operations  
✅ **JWT Authentication** and **Role-based Authorization**  
✅ **Spring Security** integration  
✅ **Comprehensive Testing** with multiple tools  
✅ **Professional Documentation** and **Error Handling**  

**Ready for production deployment! 🚀**