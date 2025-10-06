# Script khởi động lại ứng dụng Spring Boot

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   DỪNG VÀ KHỞI ĐỘNG LẠI ỨNG DỤNG" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# Dừng tất cả process Java đang chạy ứng dụng Assignment4Application
Write-Host "[1/4] Đang dừng ứng dụng cũ..." -ForegroundColor Yellow

$javaProcesses = Get-Process | Where-Object {
    $_.ProcessName -eq "java" -and 
    $_.Path -like "*jdk*" -and
    $_.Id -ne $PID
}

if ($javaProcesses) {
    foreach ($proc in $javaProcesses) {
        Write-Host "  → Dừng process ID: $($proc.Id)" -ForegroundColor Gray
        Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
    }
    Write-Host "  ✓ Đã dừng các process cũ" -ForegroundColor Green
} else {
    Write-Host "  ℹ Không có process nào đang chạy" -ForegroundColor Gray
}

Start-Sleep -Seconds 2

# Xóa cookies
Write-Host ""
Write-Host "[2/4] Vui lòng xóa cookies trong trình duyệt:" -ForegroundColor Yellow
Write-Host "  → Nhấn Ctrl + Shift + Delete" -ForegroundColor Gray
Write-Host "  → Xóa cookies cho localhost:8080 và localhost:8180" -ForegroundColor Gray
Write-Host ""
Write-Host "Nhấn Enter để tiếp tục sau khi đã xóa cookies..." -ForegroundColor Cyan
$null = Read-Host

# Biên dịch lại
Write-Host ""
Write-Host "[3/4] Đang biên dịch lại ứng dụng..." -ForegroundColor Yellow
& .\mvnw.cmd clean compile -q
Write-Host "  ✓ Biên dịch hoàn tất" -ForegroundColor Green

# Khởi động ứng dụng
Write-Host ""
Write-Host "[4/4] Đang khởi động ứng dụng..." -ForegroundColor Yellow
Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   ĐANG KHỞI ĐỘNG..." -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Start-Process powershell -ArgumentList "-NoExit", "-Command", "& '.\mvnw.cmd' spring-boot:run"

Start-Sleep -Seconds 5

Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "   HOÀN THÀNH!" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "Ứng dụng đang khởi động trong cửa sổ mới..." -ForegroundColor White
Write-Host ""
Write-Host "Sau khi thấy dòng 'Started Assignment4Application'," -ForegroundColor Yellow
Write-Host "mở trình duyệt và truy cập:" -ForegroundColor Yellow
Write-Host ""
Write-Host "    http://localhost:8080" -ForegroundColor Cyan
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
