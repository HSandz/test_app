# insert-sample.ps1
$ErrorActionPreference = "Stop"
$root = (Get-Location).Path + "\mongodb-cluster"

Write-Host "📦 Inserting sample data via mongos (port 27000)..."

# Ensure mongos is up
Start-Sleep -Seconds 2

# Run the JS file
mongosh --port 27000 --file "$root\scripts\insert-sample.js"

Write-Host "✅ Sample data inserted. Open MongoDB Compass → mongodb://localhost:27000 → DB 'shop' → Collection 'orders'"
