# Script kiểm tra Kafka cluster và test failover
# Chạy script này sau khi đã start Docker Compose

Write-Host "=== KAFKA CLUSTER MANAGEMENT SCRIPT ===" -ForegroundColor Cyan
Write-Host ""

function Show-Menu {
    Write-Host "Chọn thao tác:" -ForegroundColor Yellow
    Write-Host "1. Tạo topic 'messages' với replication factor 3"
    Write-Host "2. Kiểm tra danh sách topics"
    Write-Host "3. Kiểm tra chi tiết topic 'messages' và tìm leader"
    Write-Host "4. Dừng Kafka broker leader"
    Write-Host "5. Kiểm tra lại leader sau khi dừng broker"
    Write-Host "6. Khởi động lại tất cả brokers"
    Write-Host "7. Test gửi message"
    Write-Host "8. Thoát"
    Write-Host ""
}

function Create-Topic {
    Write-Host "`n=== TẠO TOPIC 'messages' ===" -ForegroundColor Green
    docker exec kafka-1 kafka-topics --create `
        --topic messages `
        --bootstrap-server localhost:29092 `
        --partitions 3 `
        --replication-factor 3
    Write-Host "Topic đã được tạo!" -ForegroundColor Green
}

function List-Topics {
    Write-Host "`n=== DANH SÁCH TOPICS ===" -ForegroundColor Green
    docker exec kafka-1 kafka-topics --list --bootstrap-server localhost:29092
}

function Describe-Topic {
    Write-Host "`n=== CHI TIẾT TOPIC 'messages' ===" -ForegroundColor Green
    docker exec kafka-1 kafka-topics --describe `
        --topic messages `
        --bootstrap-server localhost:29092
    Write-Host "`nChú ý: Cột 'Leader' cho biết broker ID nào đang là leader cho mỗi partition" -ForegroundColor Yellow
}

function Stop-Leader {
    Write-Host "`nNhập Broker ID muốn dừng (1, 2, hoặc 3): " -NoNewline
    $brokerId = Read-Host
    
    if ($brokerId -match '^[1-3]$') {
        Write-Host "`n=== DỪNG KAFKA-$brokerId ===" -ForegroundColor Red
        docker stop "kafka-$brokerId"
        Write-Host "Kafka-$brokerId đã bị dừng!" -ForegroundColor Red
        Write-Host "Đợi 10 giây để cluster tự động elect leader mới..." -ForegroundColor Yellow
        Start-Sleep -Seconds 10
        Describe-Topic
    } else {
        Write-Host "Broker ID không hợp lệ!" -ForegroundColor Red
    }
}

function Check-Leader-After-Stop {
    Write-Host "`n=== KIỂM TRA LEADER SAU KHI DỪNG BROKER ===" -ForegroundColor Green
    Describe-Topic
    Write-Host "`nKafka cluster tự động elect leader mới từ các broker còn lại (ISR)" -ForegroundColor Cyan
}

function Restart-All-Brokers {
    Write-Host "`n=== KHỞI ĐỘNG LẠI TẤT CẢ BROKERS ===" -ForegroundColor Green
    docker start kafka-1 kafka-2 kafka-3
    Write-Host "Đang chờ các brokers khởi động..." -ForegroundColor Yellow
    Start-Sleep -Seconds 15
    Write-Host "Tất cả brokers đã được khởi động!" -ForegroundColor Green
}

function Test-SendMessage {
    Write-Host "`n=== TEST GỬI MESSAGE ===" -ForegroundColor Green
    Write-Host "Nhập nội dung message: " -NoNewline
    $content = Read-Host
    
    $body = @{
        content = $content
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "http://localhost:8080/api/messages" `
            -Method Post `
            -Body $body `
            -ContentType "application/json"
        Write-Host "Message đã được gửi: $response" -ForegroundColor Green
        Write-Host "Kiểm tra logs của consumer-service để xem message đã được nhận!" -ForegroundColor Cyan
    } catch {
        Write-Host "Lỗi: $_" -ForegroundColor Red
        Write-Host "Đảm bảo producer-service đang chạy trên port 8080" -ForegroundColor Yellow
    }
}

# Main loop
do {
    Show-Menu
    $choice = Read-Host "Nhập lựa chọn"
    
    switch ($choice) {
        "1" { Create-Topic }
        "2" { List-Topics }
        "3" { Describe-Topic }
        "4" { Stop-Leader }
        "5" { Check-Leader-After-Stop }
        "6" { Restart-All-Brokers }
        "7" { Test-SendMessage }
        "8" { 
            Write-Host "`nTạm biệt!" -ForegroundColor Cyan
            exit 
        }
        default { Write-Host "Lựa chọn không hợp lệ!" -ForegroundColor Red }
    }
    
    Write-Host "`nNhấn Enter để tiếp tục..." -ForegroundColor Gray
    Read-Host
    Clear-Host
} while ($true)
