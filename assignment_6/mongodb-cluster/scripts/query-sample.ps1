# query-sample.ps1
$ErrorActionPreference = "Stop"
$root = (Get-Location).Path + "\mongodb-cluster"

Write-Host "🔎 Querying data from mongos router (port 27000)..."
mongosh --port 27000 --file "$root\scripts\query-sample.js"
