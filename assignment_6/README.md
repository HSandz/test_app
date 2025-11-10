# MongoDB Cluster (Replication + Sharding) — Windows, Local, Repo-Scoped

This repo contains a **self-contained MongoDB cluster** that runs entirely **inside this project folder**.
You’ll get:

* **Config Server Replica Set**: ports **27040/27041/27042** (name: `configRS`)
* **Shard #1 Replica Set**: ports **27017/27018/27019** (name: `rs0`)
* **Shard #2 Replica Set**: ports **27020/27021/27022** (name: `shardRS2`)
* **mongos Router**: port **27000**

The cluster uses YAML config files and PowerShell scripts so you don’t juggle many terminals or global services.

---

## Prerequisites

* **Windows 10/11**
* **MongoDB Community Server** installed (includes `mongod.exe` and `mongos.exe`)
* **MongoDB Shell (mongosh)** installed
* Both `mongod`, `mongos`, and `mongosh` available in **PATH**
  (If not, use full paths in scripts or add e.g. `C:\Program Files\MongoDB\Server\7.x\bin` and `C:\Program Files\MongoDB\mongosh\bin` to PATH.)
* **PowerShell** (Run as a normal user is fine; allow Firewall prompts on first run)

---

## Folder Layout

Everything lives under `mongodb-cluster/` (already included in this repo):

```
mongodb-cluster/
├── configrs/
│   ├── 27040/mongod.yaml
│   ├── 27041/mongod.yaml
│   └── 27042/mongod.yaml
├── shard1/
│   ├── 27017/mongod.yaml
│   ├── 27018/mongod.yaml
│   └── 27019/mongod.yaml
├── shard2/
│   ├── 27020/mongod.yaml
│   ├── 27021/mongod.yaml
│   └── 27022/mongod.yaml
├── mongos/mongos.yaml
└── scripts/
    ├── start-all.ps1
    ├── stop-all.ps1
    └── init-all.ps1
```

Each `mongod.yaml` points to local `data/` and `log/` subfolders that will be created automatically when instances start.

---

## Quick Start

Open **PowerShell** at the **repo root** (where `mongodb-cluster/` lives).

### 0) (Recommended) Stop any existing MongoDB processes

```powershell
Get-Process mongod -ErrorAction SilentlyContinue | Stop-Process -Force
Get-Process mongos -ErrorAction SilentlyContinue | Stop-Process -Force
```

### 1) Start the whole cluster (config servers, both shards, mongos)

```powershell
.\mongodb-cluster\scripts\start-all.ps1
```

* Windows Firewall may prompt the first time → **Allow** on Private networks.
* Processes start minimized.

### 2) One-time initialization (init replica sets & add shards)

```powershell
.\mongodb-cluster\scripts\init-all.ps1
```

This will:

* `rs.initiate()` for `configRS`, `rs0`, `shardRS2`
* Add both shards to `mongos`
* Print `sh.status()` at the end

### 3) Verify the cluster

```powershell
# From mongos router:
mongosh --port 27000 --eval "sh.status()"

# Optional: check each RS name
mongosh --port 27017 --quiet --eval "printjson(db.hello().setName)"   # expect rs0
mongosh --port 27020 --quiet --eval "printjson(db.hello().setName)"   # expect shardRS2
mongosh --port 27040 --quiet --eval "printjson(rs.status().set)"      # expect configRS
```

If you see both shards listed in `sh.status()` → you’re good to go ✅

---

## (Optional) Try Sharding a Collection

Connect to the router (port 27000) and run:

```powershell
mongosh --port 27000
```

In the shell:

```javascript
// Pick a DB and enable sharding
sh.enableSharding("shop")

// Choose a shard key (must be indexed)
db = db.getSiblingDB("shop")
db.orders.createIndex({ userId: 1 })

// Shard the collection
sh.shardCollection("shop.orders", { userId: 1 })

// Insert sample data
for (let i = 1; i <= 1000; i++) {
  db.orders.insertOne({ userId: i % 200, orderNo: i })
}

// See data distribution
db.orders.getShardDistribution()
```

---

## Stop the Cluster

```powershell
.\mongodb-cluster\scripts\stop-all.ps1
```

This stops **all** `mongod` and `mongos` processes started for this cluster.

---

## Common Issues & Fixes

* **“mongosh/mongod/mongos not recognized”**
  Not in PATH. Either:

  * Add to PATH (e.g., `C:\Program Files\MongoDB\Server\7.x\bin` and `C:\Program Files\MongoDB\mongosh\bin`), **or**
  * Edit `start-all.ps1` to use full paths for the executables.

* **Port already in use**
  Another MongoDB service might be running on 27017. Stop it or free the port:

  ```powershell
  netstat -ano | findstr LISTENING | findstr 2701
  tasklist | findstr /i mongo
  # Then Stop-Process -Id <PID>
  ```

* **`rs.initiate` fails**
  Make sure all nodes for that RS are started and reachable (correct ports) before running `init-all.ps1`.

* **Firewall prompts keep appearing**
  Allow for Private networks; or pre-create Windows Defender inbound rules for the used ports.

---

## Customizing

* **Change ports or names**: edit the YAML files under `mongodb-cluster/`.

  * Replica set names: `replication.replSetName`
  * Ports: `net.port`
  * Shard router config: `mongos/mongos.yaml` → `sharding.configDB`
* **Logs & data paths** are relative to `mongodb-cluster/` by default; change `storage.dbPath` and `systemLog.path` in YAML if needed.

---

## What’s Running (after start)

* **Router**: `mongos` on `localhost:27000`
* **Shard #1 (rs0)** primaries/secondaries on `27017/27018/27019`
* **Shard #2 (shardRS2)** primaries/secondaries on `27020/27021/27022`
* **Config RS (configRS)** on `27040/27041/27042`

Connect your app to **`mongodb://localhost:27000`** (the router) for a sharded cluster experience.

---

## Commands Reference

Start everything:

```powershell
.\mongodb-cluster\scripts\start-all.ps1
```

Initialize (one time, or if you wipe data):

```powershell
.\mongodb-cluster\scripts\init-all.ps1
```

Check status (router):

```powershell
mongosh --port 27000 --eval "sh.status()"
```

Stop everything:

```powershell
.\mongodb-cluster\scripts\stop-all.ps1
```