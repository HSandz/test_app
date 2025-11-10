$root = (Get-Location).Path + "\mongodb-cluster"

Write-Host "⏳ Initializing all replica sets..."

mongosh --port 27040 --eval "rs.initiate({_id:'configRS',configsvr:true,members:[{_id:0,host:'localhost:27040'},{_id:1,host:'localhost:27041'},{_id:2,host:'localhost:27042'}]})"
mongosh --port 27017 --eval "rs.initiate({_id:'rs0',members:[{_id:0,host:'localhost:27017'},{_id:1,host:'localhost:27018'},{_id:2,host:'localhost:27019'}]})"
mongosh --port 27020 --eval "rs.initiate({_id:'shardRS2',members:[{_id:0,host:'localhost:27020'},{_id:1,host:'localhost:27021'},{_id:2,host:'localhost:27022'}]})"

Start-Sleep -Seconds 5

Write-Host "🧩 Adding shards to mongos..."
mongosh --port 27000 --eval "sh.addShard('rs0/localhost:27017,localhost:27018,localhost:27019'); sh.addShard('shardRS2/localhost:27020,localhost:27021,localhost:27022'); sh.status()"

Write-Host "✅ Cluster initialized."
