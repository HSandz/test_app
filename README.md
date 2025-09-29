# 📚 Phát triển Ứng dụng Doanh nghiệp (INT3236E_1)

> **Bài tập môn học Phát triển Ứng dụng Doanh nghiệp - Trường Đại học Công nghệ**

Đây là repository chứa các bài tập thực hành của môn học **INT3236E_1 - Phát triển Ứng dụng Doanh nghiệp**, tập trung vào việc xây dựng các ứng dụng web sử dụng **Spring Boot Framework**.

## 🎯 Mục tiêu học tập

- Nắm vững kiến thức về **Spring Boot Framework**
- Hiểu và thực hành **Spring MVC Pattern**
- Tìm hiểu về **Spring Security** và xác thực người dùng
- Xây dựng **RESTful API** với **JWT Authentication**
- Quản lý cơ sở dữ liệu với **Spring Data JPA**

## 📂 Cấu trúc dự án

```
PTUDDN/
├── assignment_3/          # Bài tập 3: REST API + JWT Authentication
├── assignment_12/         # Bài tập 1 và 2: Spring Boot MVC + Security
└── README.md             # File README tổng quan
```

---

## 🔢 Danh sách bài tập

### 📝 [Assignment 3](./assignment_3/) - REST API + Xác thực JWT

**🎯 Mục tiêu**: Xây dựng Blog Management API với xác thực JWT

**✨ Tính năng chính**:
- 🔐 **JWT Authentication** - Xác thực bằng token
- 👥 **Role-based Authorization** - Phân quyền theo vai trò (ADMIN/USER)
- 📊 **CRUD Operations** - Quản lý Users và Blogs
- 🛡️ **Spring Security** - Bảo mật ứng dụng
- 🗄️ **H2 Database** - Cơ sở dữ liệu trong bộ nhớ

**🛠️ Công nghệ sử dụng**:
- Spring Boot 3.5.6
- Spring Security 6.x
- Spring Data JPA
- JWT 0.12.3
- H2 Database
- Maven

**📋 API Endpoints**:
- `GET /api/blogs` - Lấy danh sách tất cả blog
- `POST /api/auth/signup` - Đăng ký người dùng
- `POST /api/auth/signin` - Đăng nhập
- `GET /api/users` - Quản lý người dùng (Admin)
- Và nhiều endpoints khác...

---

### 🎓 [Assignment 12](./assignment_12/) - Spring Boot MVC + Security

**🎯 Mục tiêu**: Phát triển hệ thống quản lý khóa học với giao diện web

#### 📚 **BT 1: Spring Boot MVC**
**✨ Tính năng**:
- 🌐 **Web MVC** - Giao diện web hoàn chỉnh
- 📋 **Course Management** - Quản lý khóa học
- 🔍 **Search Functionality** - Tìm kiếm khóa học
- 📱 **Responsive Design** - Giao diện responsive
- 🗄️ **File-based H2 Database** - Lưu trữ dữ liệu bền vững

#### 🔒 **BT 2: Securing Web**
**✨ Tính năng bảo mật**:
- 🔐 **User Authentication** - Xác thực người dùng
- 📝 **Registration System** - Hệ thống đăng ký
- 👤 **Role-based Access** - Phân quyền USER/ADMIN
- 🛡️ **CSRF Protection** - Bảo vệ CSRF
- 🔒 **Secure Password** - Mã hóa mật khẩu

**🛠️ Công nghệ sử dụng**:
- Spring Boot 3.x
- Spring MVC
- Spring Security
- Thymeleaf Template Engine
- Bootstrap CSS
- H2 Database (File-based)
- Maven

---

## 🚀 Hướng dẫn chạy dự án

### 📋 Yêu cầu hệ thống
- **Java**: 17 hoặc cao hơn
- **Maven**: 3.9 hoặc cao hơn
- **IDE**: IntelliJ IDEA, Eclipse, hoặc VS Code

### 🏃‍♂️ Cách chạy từng bài tập

#### 1️⃣ Assignment 3 (REST API):
```bash
cd assignment_3
./mvnw spring-boot:run
```
- **URL**: http://localhost:8080
- **API Documentation**: Xem file [API_TEST.md](./assignment_3/API_TEST.md)
- **Postman Collection**: [Postman_Collection.json](./assignment_3/Postman_Collection.json)

#### 2️⃣ Assignment 12 (Web MVC):
```bash
cd assignment_12
./mvnw spring-boot:run
```
- **URL**: http://localhost:8080
- **Admin Login**: username/password được cấu hình trong ứng dụng
- **H2 Console**: http://localhost:8080/h2-console

---

## 🛠️ Kiến thức đã học

### 🌱 Spring Boot Framework
- ✅ **Auto-configuration** - Tự động cấu hình
- ✅ **Dependency Injection** - Tiêm phụ thuộc
- ✅ **Spring Boot Starters** - Các starter packages
- ✅ **Application Properties** - Cấu hình ứng dụng

### 🌐 Spring MVC
- ✅ **Controller Layer** - Tầng điều khiển
- ✅ **Model-View-Controller** - Mô hình MVC
- ✅ **Thymeleaf Templates** - Template engine
- ✅ **Request Mapping** - Mapping các request

### 🔐 Spring Security
- ✅ **Authentication** - Xác thực người dùng
- ✅ **Authorization** - Phân quyền
- ✅ **JWT Tokens** - JSON Web Tokens
- ✅ **Password Encoding** - Mã hóa mật khẩu
- ✅ **CSRF Protection** - Bảo vệ CSRF

### 🗄️ Spring Data JPA
- ✅ **Entity Mapping** - Mapping thực thể
- ✅ **Repository Pattern** - Mẫu Repository
- ✅ **CRUD Operations** - Các thao tác CRUD
- ✅ **Query Methods** - Phương thức truy vấn

### 🌍 RESTful API
- ✅ **HTTP Methods** - GET, POST, PUT, DELETE
- ✅ **Status Codes** - Mã trạng thái HTTP
- ✅ **JSON Responses** - Phản hồi JSON
- ✅ **Error Handling** - Xử lý lỗi

---

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://spring.io/projects/spring-security)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [JWT.io](https://jwt.io/)

---

## 👨‍💻 Thông tin sinh viên

- **Môn học**: INT3236E_1 - Phát triển Ứng dụng Doanh nghiệp
- **Trường**: Đại học Công nghệ - ĐHQGHN
- **Học kỳ**: N3-K1

---

## 📞 Liên hệ

Nếu có bất kỳ câu hỏi nào về các bài tập, vui lòng tạo **Issue** trong repository này hoặc liên hệ qua email.

---

<div align="center">

**🌟 Cảm ơn bạn đã xem repository này! 🌟**

Made with ❤️ for learning Spring Boot

</div>