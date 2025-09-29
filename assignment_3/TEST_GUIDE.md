# Hướng dẫn Test API Endpoints - Chi Tiết

## **Phương pháp 1: Sử dụng cURL (Command Line)**

### Bước 1: Khởi động ứng dụng
```bash
cd "c:\Users\lehoa\OneDrive\Documents\College\N3-K1\PTUDDN\PTUDDN\assignment_3"
mvn spring-boot:run
```

### Bước 2: Test theo thứ tự
1. **Đăng ký users**: Tạo user và admin
2. **Đăng nhập**: Lấy JWT tokens
3. **Test các API**: Sử dụng tokens đã lấy được

### Ví dụ flow hoàn chỉnh với cURL:

```bash
# 1. Đăng ký user thường
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "email": "test@example.com", "password": "123456"}'

# 2. Đăng nhập và lấy token
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "password": "123456"}'

# Sao chép token từ response, ví dụ: eyJhbGciOiJIUzUxMiJ9...

# 3. Tạo blog với token
curl -X POST http://localhost:8080/api/blogs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -d '{"title": "Test Blog", "content": "This is test content"}'

# 4. Xem blogs của mình
curl -X GET http://localhost:8080/api/blogs/my \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## **Phương pháp 2: Sử dụng Postman**

### Bước 1: Import Collection
1. Mở Postman
2. Click **Import** 
3. Chọn file `Postman_Collection.json` trong thư mục project
4. Collection "Assignment 3 - Blog API" sẽ xuất hiện

### Bước 2: Cấu hình Environment (Optional)
1. Tạo Environment mới tên "Local Development"
2. Thêm biến:
   - `baseUrl`: `http://localhost:8080`
   - `userToken`: (để trống)
   - `adminToken`: (để trống)

### Bước 3: Test theo thứ tự
1. **Authentication → Register User**: Đăng ký user
2. **Authentication → Register Admin**: Đăng ký admin  
3. **Authentication → Login User**: Đăng nhập user (token tự động lưu)
4. **Authentication → Login Admin**: Đăng nhập admin (token tự động lưu)
5. Test các API khác với tokens đã được lưu tự động

## **Phương pháp 3: Sử dụng VS Code REST Client**

### Bước 1: Cài đặt extension
- Tìm và cài đặt extension "REST Client" trong VS Code

### Bước 2: Tạo file test
Sử dụng file `api-tests.http` đã tạo sẵn trong project.

### Bước 3: Test trong VS Code
1. Mở file `api-tests.http`
2. Click "Send Request" trên mỗi request
3. Sao chép tokens từ login responses vào variables ở đầu file
4. Test các API khác với tokens đã có

## **Phương pháp 4: Automated Testing với JUnit**

### Chạy Integration Test
```bash
mvn test -Dtest=ApiIntegrationTest
```

File test tự động đã được tạo trong `src/test/java/com/example/assignment_3/ApiIntegrationTest.java`

## **Phương pháp 5: Sử dụng Browser (Cho GET requests)**

Một số API GET có thể test trực tiếp bằng browser:

### Public endpoints:
- http://localhost:8080/api/blogs (Xem tất cả blogs)
- http://localhost:8080/api/blogs/1 (Xem blog ID = 1)
- http://localhost:8080/api/blogs/user/1 (Xem blogs của user ID = 1)

### H2 Database Console:
- http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## **Checklist Test Scenarios**

### ✅ Authentication Tests
- [ ] Đăng ký user mới (role USER)
- [ ] Đăng ký admin mới (role ADMIN)
- [ ] Đăng nhập thành công
- [ ] Đăng nhập với thông tin sai
- [ ] Token được sinh ra có đúng format không

### ✅ Authorization Tests  
- [ ] User có thể truy cập blog endpoints
- [ ] User KHÔNG thể truy cập user management endpoints
- [ ] Admin có thể truy cập tất cả endpoints
- [ ] Truy cập protected endpoints mà không có token (401)
- [ ] Truy cập admin endpoints với user token (403)

### ✅ Blog CRUD Tests
- [ ] GET /api/blogs (Public - thành công)
- [ ] GET /api/blogs/{id} (Public - thành công)  
- [ ] GET /api/blogs/my (Authenticated - thành công)
- [ ] POST /api/blogs (Authenticated - tạo blog)
- [ ] PUT /api/blogs/{id} (Owner/Admin - cập nhật blog)
- [ ] DELETE /api/blogs/{id} (Owner/Admin - xóa blog)
- [ ] User cố gắng sửa blog của user khác (403)

### ✅ User CRUD Tests (Admin only)
- [ ] GET /api/users (Admin - danh sách users)
- [ ] GET /api/users/{id} (Admin - user chi tiết)
- [ ] PUT /api/users/{id} (Admin - cập nhật user)
- [ ] DELETE /api/users/{id} (Admin - xóa user)

### ✅ Input Validation Tests
- [ ] Đăng ký với username quá ngắn (<3 chars)
- [ ] Đăng ký với email không hợp lệ
- [ ] Đăng ký với password quá ngắn (<6 chars)
- [ ] Tạo blog với title trống
- [ ] Tạo blog với content trống
- [ ] Tạo blog với title quá dài (>200 chars)

## **Tip để Test hiệu quả**

### 1. Test theo thứ tự:
1. **Authentication** (Register → Login → Get Tokens)
2. **Public APIs** (No auth needed)  
3. **User APIs** (With user token)
4. **Admin APIs** (With admin token)
5. **Permission Tests** (Wrong roles/no tokens)

### 2. Lưu tokens để tái sử dụng:
- Sau khi login, lưu token vào biến/environment
- Không cần login lại cho mỗi request

### 3. Kiểm tra Response:
- **Status Code**: 200, 401, 403, 404, etc.
- **Response Body**: JSON structure, data correctness
- **Headers**: Content-Type, Authorization

### 4. Database Verification:
- Sử dụng H2 Console để xem data trong database
- Verify data được tạo/cập nhật/xóa đúng chưa

## **Troubleshooting**

### Lỗi thường gặp:
1. **401 Unauthorized**: Token expired hoặc không hợp lệ
2. **403 Forbidden**: Không đủ quyền truy cập
3. **404 Not Found**: Endpoint hoặc resource không tồn tại
4. **400 Bad Request**: Dữ liệu input không hợp lệ

### Solutions:
- Kiểm tra server có đang chạy không (port 8080)
- Verify token format và expiration
- Check request body JSON syntax
- Xem logs trong console để debug