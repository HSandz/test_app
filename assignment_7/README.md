# Hướng dẫn chạy hệ thống Kafka Cluster với Spring Boot

## Yêu cầu hệ thống
- Docker Desktop
- Java 21
- Maven

## Bước 1: Khởi động Kafka Cluster (3 brokers)

```powershell
# Từ thư mục gốc của project
docker-compose up -d

# Kiểm tra các containers đang chạy
docker ps
```

Hệ thống sẽ khởi động:
- 1 Zookeeper (port 2181)
- 3 Kafka brokers:
  - kafka-1 (port 9092)
  - kafka-2 (port 9093)
  - kafka-3 (port 9094)

## Bước 2: Tạo topic với replication factor 3

```powershell
# Tạo topic "messages" với 3 partitions và 3 replicas
docker exec kafka-1 kafka-topics --create `
    --topic messages `
    --bootstrap-server localhost:29092 `
    --partitions 3 `
    --replication-factor 3
```

## Bước 3: Khởi động Producer Service

```powershell
cd producer-service
./mvnw spring-boot:run
```

Producer service sẽ chạy trên port 8080

## Bước 4: Khởi động Consumer Service

```powershell
# Mở terminal mới
cd consumer-service
./mvnw spring-boot:run
```

Consumer service sẽ chạy trên port 8081

## Bước 5: Test gửi message

```powershell
# Gửi message qua REST API
curl -X POST http://localhost:8080/api/messages `
    -H "Content-Type: application/json" `
    -d '{"content":"Hello Kafka Cluster!"}'
```

Hoặc sử dụng script quản lý:

```powershell
.\kafka-management.ps1
```

## Kiểm tra Leader và Test Failover

### 1. Kiểm tra topic và tìm leader

```powershell
docker exec kafka-1 kafka-topics --describe `
    --topic messages `
    --bootstrap-server localhost:29092
```

Output sẽ hiển thị:
```
Topic: messages	PartitionCount: 3	ReplicationFactor: 3
	Topic: messages	Partition: 0	Leader: 1	Replicas: 1,2,3	Isr: 1,2,3
	Topic: messages	Partition: 1	Leader: 2	Replicas: 2,3,1	Isr: 2,3,1
	Topic: messages	Partition: 2	Leader: 3	Replicas: 3,1,2	Isr: 3,1,2
```

- **Leader**: Broker ID đang xử lý read/write cho partition đó
- **Replicas**: Danh sách brokers có replica
- **ISR (In-Sync Replicas)**: Replicas đang sync với leader

### 2. Dừng broker leader

Giả sử Partition 0 có Leader là broker 1:

```powershell
# Dừng kafka-1
docker stop kafka-1

# Đợi 5-10 giây để cluster elect leader mới
Start-Sleep -Seconds 10

# Kiểm tra lại
docker exec kafka-2 kafka-topics --describe `
    --topic messages `
    --bootstrap-server localhost:29093
```

### 3. Test hệ thống sau khi dừng leader

```powershell
# Gửi message mới
curl -X POST http://localhost:8080/api/messages `
    -H "Content-Type: application/json" `
    -d '{"content":"Test sau khi dừng leader"}'
```

**Kết quả mong đợi**:
- ✅ Hệ thống vẫn hoạt động bình thường
- ✅ Consumer vẫn nhận được message
- ✅ Broker khác được elect làm leader mới

### 4. Khởi động lại broker đã dừng

```powershell
docker start kafka-1
```

Broker sẽ rejoin cluster và sync lại dữ liệu.

## Giải thích cơ chế Failover

1. **Replication Factor = 3**: Mỗi partition có 3 bản sao trên 3 brokers khác nhau
2. **ISR (In-Sync Replicas)**: Chỉ những replicas đang sync với leader
3. **Min ISR = 2**: Cần ít nhất 2 replicas sync để write thành công
4. **Leader Election**: 
   - Khi leader die, Zookeeper sẽ tự động elect 1 broker từ ISR làm leader mới
   - Process này thường mất 5-10 giây
   - Client tự động kết nối đến leader mới

## Tắt hệ thống

```powershell
# Dừng các Spring Boot services (Ctrl+C)

# Dừng Kafka cluster
docker-compose down

# Xóa data (optional)
docker-compose down -v
```

## Monitoring

### Xem logs của các services

```powershell
# Producer logs
cd producer-service
./mvnw spring-boot:run

# Consumer logs
cd consumer-service
./mvnw spring-boot:run

# Kafka broker logs
docker logs kafka-1
docker logs kafka-2
docker logs kafka-3
```

### Kiểm tra consumer groups

```powershell
docker exec kafka-1 kafka-consumer-groups --list `
    --bootstrap-server localhost:29092

docker exec kafka-1 kafka-consumer-groups --describe `
    --group message-consumer-group `
    --bootstrap-server localhost:29092
```

## Các tính năng đã implement

✅ Producer Service với REST API gửi JSON messages  
✅ Consumer Service lắng nghe và xử lý messages  
✅ Kafka Cluster 3 brokers với high availability  
✅ Replication factor 3 cho fault tolerance  
✅ Automatic leader election khi broker die  
✅ Script quản lý và test Kafka cluster  

## Troubleshooting

### Lỗi kết nối Kafka
- Kiểm tra Docker containers: `docker ps`
- Xem logs: `docker logs kafka-1`
- Đảm bảo ports không bị conflict: 9092, 9093, 9094

### Message không được nhận
- Kiểm tra consumer logs
- Verify topic đã được tạo: `docker exec kafka-1 kafka-topics --list --bootstrap-server localhost:29092`
- Kiểm tra consumer group offset

### Port đã được sử dụng
- Thay đổi ports trong `application.properties` của producer/consumer
- Thay đổi ports trong `docker-compose.yml` nếu conflict
