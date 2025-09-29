# 🎯 DEMO GUIDE - Assignment 3

## ⚡ Quick Demo Script (5 phút)

### **1. Khởi động ứng dụng**
```bash
mvn spring-boot:run
```
Server chạy tại: http://localhost:8080

### **2. Demo với Postman (Khuyên dùng)**
1. Import file `Postman_Collection.json`
2. Demo theo thứ tự:

#### **Step 1: Authentication**
- **Register User**: `Authentication → Register User`
- **Register Admin**: `Authentication → Register Admin`  
- **Login User**: `Authentication → Login User` *(token tự động lưu)*
- **Login Admin**: `Authentication → Login Admin` *(token tự động lưu)*

#### **Step 2: Blog Operations**
- **Get All Blogs**: `Blog Management → Get All Blogs (Public)`
- **Create Blog**: `Blog Management → Create Blog` *(với user token)*
- **Get My Blogs**: `Blog Management → Get My Blogs` *(với user token)*
- **Update Blog**: `Blog Management → Update Blog (Owner or Admin)`

#### **Step 3: User Management (Admin)**
- **Get All Users**: `User Management → Get All Users` *(với admin token)*
- **Get User by ID**: `User Management → Get User by ID`

#### **Step 4: Permission Testing**
- **User tries Admin endpoint**: `Permission Tests → User tries to access Admin endpoint (Should fail)` *(403 Forbidden)*
- **No Auth**: `Permission Tests → Access protected endpoint without token (Should fail)` *(401 Unauthorized)*

### **3. Database Verification**
- Mở: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa` | Password: `password`
- Query: `SELECT * FROM users;` và `SELECT * FROM blogs;`

## 🎯 Key Points để Emphasize

### ✅ **Technical Requirements Met**
1. **✓ REST API CRUD** - Users + Blogs với GET, POST, PUT, DELETE
2. **✓ SpringBoot Framework** - Version 3.5.6
3. **✓ JWT Authentication** - Token-based login system  
4. **✓ Role-based Authorization** - USER vs ADMIN permissions
5. **✓ Spring Security** - Comprehensive security configuration

### 🛡️ **Security Features**
- **Password Encryption**: BCrypt hashing
- **JWT Tokens**: 256-bit secure key, 24h expiration
- **Role Permissions**: 
  - ADMIN: Full access to users + blogs
  - USER: Only own blogs management
- **Protected Endpoints**: Require valid authentication

### 🔧 **Professional Implementation**
- **Input Validation**: Bean validation annotations
- **Error Handling**: Proper HTTP status codes
- **Database Relations**: User ↔ Blogs relationship
- **Clean Architecture**: Controller → Service → Repository pattern

## 🚨 Demo Highlights

### **Show Permission Control**
1. Login as USER → Try to access `/api/users` → **403 Forbidden** ✅
2. Login as ADMIN → Access `/api/users` → **Success** ✅
3. USER can only edit own blogs → **403** when trying others ✅

### **Show Authentication**
1. Access `/api/blogs/my` without token → **401 Unauthorized** ✅
2. With valid token → **Success** ✅
3. Token format validation → **Bearer eyJ...** ✅

### **Show Data Persistence**
1. Create blog via API
2. Show in H2 console that data persisted
3. Relationships maintained (blog.user_id → users.id)

## 📊 Success Metrics

- ✅ **All CRUD operations** working
- ✅ **Authentication flow** complete  
- ✅ **Authorization rules** enforced
- ✅ **Database integration** functional
- ✅ **Error handling** proper
- ✅ **Professional documentation**

## 🎤 Demo Script

> "Tôi đã xây dựng một REST API hoàn chỉnh với SpringBoot, có đầy đủ các tính năng authentication và authorization. 
>
> **Features chính:**
> - REST API cho Users và Blogs với đầy đủ CRUD
> - JWT authentication với role-based permissions  
> - ADMIN có thể quản lý tất cả, USER chỉ được quản lý blog của mình
> - Database integration với JPA/Hibernate
> - Comprehensive testing với Postman
>
> **Demo bây giờ...**"

### Estimated Demo Time: **5-7 phút**

---
*File này dành riêng cho việc demo/thuyết trình bài assignment*