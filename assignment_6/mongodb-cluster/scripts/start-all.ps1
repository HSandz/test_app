function Start-Node($exe, $cfg) {
  Start-Process -FilePath $exe -ArgumentList "--config `"$cfg`"" -WindowStyle Minimized
}

$root = (Get-Location).Path + "\mongodb-cluster"

# ConfigRS
Start-Node "mongod" "$root\configrs\27040\mongod.yaml"
Start-Node "mongod" "$root\configrs\27041\mongod.yaml"
Start-Node "mongod" "$root\configrs\27042\mongod.yaml"

# Shard1
Start-Node "mongod" "$root\shard1\27017\mongod.yaml"
Start-Node "mongod" "$root\shard1\27018\mongod.yaml"
Start-Node "mongod" "$root\shard1\27019\mongod.yaml"

# Shard2
Start-Node "mongod" "$root\shard2\27020\mongod.yaml"
Start-Node "mongod" "$root\shard2\27021\mongod.yaml"
Start-Node "mongod" "$root\shard2\27022\mongod.yaml"

# mongos
Start-Node "mongos" "$root\mongos\mongos.yaml"

Write-Host "✅ All MongoDB nodes started (configRS, shard1, shard2, mongos)."
