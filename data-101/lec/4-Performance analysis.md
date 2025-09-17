## Types of scans
### Index scan
- for each index key match, fetch corresponding page
- very few records

#### Index Only Scan
- lookups only the index and does not return the values

### Sequential scan
- load in each page in relation, then scan all records in that page

### Bitmap heap scan
- first scan the index page to identify the unique pages to visit, then sequentially scan the identified pages

## Maintenance costs
Inserts, deletes, and updates must update on every index
Indexes might not be automatically maintained

## Cardinality
Cardinality is quantified by its number of distinct values

If an attribute is high cardinality (many distinct values), then indexing on that attribute may be useful
- PostgreSQL `CLUSTER` clusters data based on an index

Indexes are even more useful if data is *clustered* on that attribute
- ex. sorted and stored in that order on disk

## Summary
Good
- on attributes used often in `WHERE` clauses
- on key attributes (sometimes done by default)
- on attributes that are clustered

Not very good
- on attributes with low cardinality, where there are many tuples per value of attribute
- on tables that are modified more often than queried