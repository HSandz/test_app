Get-Process mongos -ErrorAction SilentlyContinue | Stop-Process -Force
Get-Process mongod -ErrorAction SilentlyContinue | Stop-Process -Force
Write-Host "🛑 All mongod/mongos processes stopped."
