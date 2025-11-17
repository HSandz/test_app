# Quick Start Guide

## Khởi động nhanh hệ thống

### 1. Start Kafka Cluster
```powershell
docker-compose up -d
```

### 2. Tạo Topic
```powershell
docker exec kafka-1 kafka-topics --create --topic messages --bootstrap-server localhost:29092 --partitions 3 --replication-factor 3
```

### 3. Start Services

Terminal 1 - Producer:
```powershell
cd producer-service
./mvnw spring-boot:run
```

Terminal 2 - Consumer:
```powershell
cd consumer-service
./mvnw spring-boot:run
```

### 4. Test Message
```powershell
curl -X POST http://localhost:8080/api/messages -H "Content-Type: application/json" -d '{"content":"Hello Kafka!"}'
```

### 5. Kiểm tra Leader
```powershell
docker exec kafka-1 kafka-topics --describe --topic messages --bootstrap-server localhost:29092
```

### 6. Test Failover
```powershell
# Dừng leader (ví dụ kafka-1)
docker stop kafka-1

# Kiểm tra lại leader
docker exec kafka-2 kafka-topics --describe --topic messages --bootstrap-server localhost:29093

# Gửi message để test
curl -X POST http://localhost:8080/api/messages -H "Content-Type: application/json" -d '{"content":"Test failover!"}'

# Khởi động lại
docker start kafka-1
```

## Hoặc sử dụng script tự động:
```powershell
.\kafka-management.ps1
```
