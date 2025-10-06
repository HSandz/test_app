@echo off
echo.
echo ============================================
echo   DUNG UNG DUNG SPRING BOOT
echo ============================================
echo.

REM Dung tat ca cac process Java dang chay ung dung
echo [1/3] Dang dung ung dung cu...
for /f "tokens=2" %%i in ('jps -l ^| findstr "Assignment4Application"') do (
    echo Tim thay process: %%i
    taskkill /F /PID %%i 2>nul
)

timeout /t 2 /nobreak >nul

echo.
echo [2/3] Xoa cookies va cache trung gian...
echo Vui long xoa cookies trong trinh duyet:
echo - Nhan Ctrl + Shift + Delete
echo - Chon xoa cookies cho localhost:8080 va localhost:8180
echo.
pause

echo.
echo [3/3] Khoi dong lai ung dung...
echo.
echo ============================================
echo   DANG KHOI DONG...
echo ============================================
echo.

start cmd /k "mvnw.cmd spring-boot:run"

echo.
echo ============================================
echo   HOAN THANH!
echo ============================================
echo.
echo Ung dung dang khoi dong...
echo.
echo Sau khi thay dong "Started Assignment4Application", 
echo mo trinh duyet va truy cap:
echo.
echo    http://localhost:8080
echo.
echo ============================================
pause
