

// Id of customer, due to imported data, is built with lastName_firstName
CREATE CONSTRAINT unique_customer IF NOT EXISTS ON (c:Customer) ASSERT c.id IS UNIQUE;

// Id of merchant, due to imported data, is built with merchant name
CREATE CONSTRAINT unique_merchant ON (m:Merchant) ASSERT m.id IS UNIQUE;

// Id of address, due to imported data, is built with lat_lon
CREATE CONSTRAINT unique_address  IF NOT EXISTS ON (a:Address) ASSERT a.id IS UNIQUE;

// Id of transaction, due to imported data, is built with trans_num
CREATE CONSTRAINT unique_transaction  IF NOT EXISTS ON (t:Transaction) ASSERT t.number IS UNIQUE;


// Libraries APOC required for data conversion

USING PERIODIC COMMIT 500 
LOAD CSV WITH HEADERS 
FROM "file:///fraudTest.csv" AS line
WITH line
MERGE (merchant:Merchant { id: toUpper(substring(line.merchant+"_"+line.category, 6)), name: substring(line.merchant, 6), category: line.category})
MERGE (customer:Customer { id: toUpper(line.last+"_"+line.first+"_"+line.job), firstName: line.first, lastName: line.last, job: line.job,gender: line.gender, birthDate :  line.dob})
MERGE (address:Address { id: toUpper(line.zip+"_"+line.city+"_"+line.street), street: line.street, city: line.city, zip : line.zip, state : line.state, lat: line.lat, lon : line.long})
MERGE (address)-[:USED_BY]-(customer)
CREATE (transaction:Transaction { number : line.trans_num, amount : line.amt, transactionTime: line.unix_time, cardNumber : line.cc_num, isFraud: apoc.convert.toBoolean(line.is_fraud),
		validated: NOT apoc.convert.toBoolean(line.is_fraud)})
CREATE (transaction)-[:DELIVERED_TO]->(address)
CREATE (transaction)-[:SOLD_BY]->(merchant)
CREATE (transaction)-[:PURCHASED_BY]->(customer);
