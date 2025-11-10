// @ts-nocheck
// query-sample.js (mongosh script)

db = db.getSiblingDB("shop");

print("📈 Total documents in 'shop.orders':");
printjson(db.orders.countDocuments());

print("🔍 Random sample documents:");
printjson(db.orders.find().limit(5).toArray());

print("📊 Shard distribution:");
db.orders.getShardDistribution();
