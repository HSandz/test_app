# API Testing Guide

Hướng dẫn kiểm tra các REST API của project Assignment 3 bằng cURL commands.

## Khởi động ứng dụng
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: http://localhost:8080

## 1. Authentication APIs

### 1.1 Đăng ký user mới (USER role)
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "email": "user1@example.com",
    "password": "password123"
  }'
```

### 1.2 Đăng ký admin mới (ADMIN role)
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "email": "admin1@example.com",
    "password": "password123",
    "role": "ADMIN"
  }'
```

### 1.3 Đăng nhập (USER)
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "password123"
  }'
```

### 1.4 Đăng nhập (ADMIN)
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "password123"
  }'
```

**Lưu ý**: Sao chép token từ response để sử dụng cho các API tiếp theo.

## 2. User Management APIs (Chỉ ADMIN có quyền)

### 2.1 GET - Lấy danh sách tất cả users
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 2.2 GET - Lấy user theo ID
```bash
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

### 2.3 PUT - Cập nhật user
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "username": "user1_updated",
    "email": "user1_updated@example.com"
  }'
```

### 2.4 DELETE - Xóa user
```bash
curl -X DELETE http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

## 3. Blog Management APIs

### 3.1 GET - Lấy tất cả blogs (Public)
```bash
curl -X GET http://localhost:8080/api/blogs
```

### 3.2 GET - Lấy blog theo ID (Public)
```bash
curl -X GET http://localhost:8080/api/blogs/1
```

### 3.3 GET - Lấy blogs của user cụ thể (Public)
```bash
curl -X GET http://localhost:8080/api/blogs/user/1
```

### 3.4 GET - Lấy blogs của chính mình (Cần đăng nhập)
```bash
curl -X GET http://localhost:8080/api/blogs/my \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

### 3.5 POST - Tạo blog mới (Cần đăng nhập)
```bash
curl -X POST http://localhost:8080/api/blogs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -d '{
    "title": "My First Blog",
    "content": "This is the content of my first blog post."
  }'
```

### 3.6 PUT - Cập nhật blog (Chỉ owner hoặc ADMIN)
```bash
curl -X PUT http://localhost:8080/api/blogs/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -d '{
    "title": "Updated Blog Title",
    "content": "Updated content of the blog."
  }'
```

### 3.7 DELETE - Xóa blog (Chỉ owner hoặc ADMIN)
```bash
curl -X DELETE http://localhost:8080/api/blogs/1 \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```

## 4. Testing Permission Scenarios

### 4.1 Test USER cố gắng truy cập User Management (Should fail)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_USER_TOKEN"
```
**Expected**: 403 Forbidden

### 4.2 Test USER cố gắng cập nhật blog của user khác (Should fail)
```bash
# Giả sử blog có ID 2 thuộc về user khác
curl -X PUT http://localhost:8080/api/blogs/2 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_USER_TOKEN" \
  -d '{
    "title": "Trying to hack",
    "content": "This should not work"
  }'
```
**Expected**: 403 Forbidden

### 4.3 Test ADMIN có thể cập nhật/xóa blog của bất kỳ user nào
```bash
curl -X PUT http://localhost:8080/api/blogs/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "title": "Admin Updated This",
    "content": "Admin can update any blog"
  }'
```
**Expected**: Success

## 5. Error Testing

### 5.1 Test truy cập API cần authentication mà không có token
```bash
curl -X GET http://localhost:8080/api/blogs/my
```
**Expected**: 401 Unauthorized

### 5.2 Test với token không hợp lệ
```bash
curl -X GET http://localhost:8080/api/blogs/my \
  -H "Authorization: Bearer INVALID_TOKEN"
```
**Expected**: 401 Unauthorized

## 6. H2 Console (Development Only)
Truy cập: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- Username: sa
- Password: password

## Validation Rules
- Username: 3-50 characters, unique
- Email: Valid email format, unique
- Password: Minimum 6 characters
- Blog title: Required, max 200 characters
- Blog content: Required