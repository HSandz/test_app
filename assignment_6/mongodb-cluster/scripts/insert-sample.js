// @ts-nocheck
// insert-sample.js (mongosh script)

print("🏗️ Preparing sample database and collection...");

// Switch DB in scripts:
db = db.getSiblingDB("shop");

// Enable sharding for the DB (idempotent)
sh.enableSharding("shop");

// Ensure index on shard key first (required)
db.orders.createIndex({ userId: 1 });

// Shard the collection (idempotent)
printjson(sh.shardCollection("shop.orders", { userId: 1 }));

print("🧮 Inserting 1000 random sample documents...");
for (let i = 1; i <= 1000; i++) {
  const doc = {
    userId: Math.floor(Math.random() * 200), // 0–199
    orderNo: i,
    item: ["Keyboard", "Mouse", "Monitor", "Headphones", "Cable"][Math.floor(Math.random() * 5)],
    price: Math.floor(Math.random() * 100) + 10,
    createdAt: new Date()
  };
  db.orders.insertOne(doc);
}
print("✅ Insert complete!");

print("🔍 Sample documents:");
printjson(db.orders.find().limit(3).toArray());

print("📊 Shard distribution:");
db.orders.getShardDistribution();
