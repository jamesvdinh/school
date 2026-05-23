## Data Lake
```ad-tldr
a centralized repository that can store any raw data at any scale.
```

This centralized repository allows any data (structured, semi-structured, or unstructured) to be stored before processing, so that the actual transformation occurs AFTER loading the raw data.

Uses the philosophy **Schema-on-Read**: the data sits in its *raw form* and you only define its structure and shape when you pull it out to analyze it.

| Pros                                                                                      | Cons                                                    |
| ----------------------------------------------------------------------------------------- | ------------------------------------------------------- |
| incredibly agile -- can store anything instantly without asking permissions from DB admin | querying raw files directly can be significantly slower |
|                                                                                           | requires more complex processing engines (e.g Spark)    |
## Data Warehouse
```ad-tldr
data goes through a strict transformation being stored in the warehouse.
```

![[Pasted image 20260521172825.png]]

| **Feature**             | **Data Lake**                                                                       | **Data Warehouse**                                                                |
| ----------------------- | ----------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- |
| **Data Structure**      | Raw, unstructured, semi-structured, and structured.                                 | Highly structured, cleaned, and transformed.                                      |
| **Processing Paradigm** | **ELT** (Extract, Load, Transform) — transform it later when needed.                | **ETL** (Extract, Transform, Load) — must clean/shape it before loading.          |
| **Schema Model**        | **Schema-on-Read** (defined during analysis).                                       | **Schema-on-Write** (defined before storage).                                     |
| **Primary Users**       | Data Scientists, ML Engineers, Data Engineers.                                      | Business Analysts, BI Developers, Executives.                                     |
| **Optimal Use Cases**   | Machine learning, big data analytics, log analysis, archiving raw data.             | Business Intelligence (BI), routine reporting, historical performance dashboards. |
| **Storage Cost**        | Relatively low (built on cheap object storage like AWS S3 or Google Cloud Storage). | Higher cost per terabyte due to optimized compute-and-storage scaling.            |
## Data Lakehouse
```ad-tldr
combines two concepts of Data Lake and Warehouse into one -- structured, ACID-compliant transformation layer on top of cheap cloud object storage
```

Example frameworks:
- Apache Iceberg
- Delta Lake
- Hive

## OLTP vs OLAP
```ad-tldr
**OLTP**: normalizes data for many small *safe writes* (`INSERT`, `UPDATE`, `DELETE`)

**OLAP** (Data Warehouse): denormalizes data for *fast reads*
```

**OLTP (Transactional)**
- Purpose: run the business & record day-to-day operations
- Optimized for speed, integrity, and concurrency

**OLAP (Analytical)**
- Purpose: analyze the business -- reporting, BI, trends
- large operations, complex read `SELECT`s with `GROUP BY`s
- optimized for fast reading over huge scans
- Star schema
- Examples: Snowflake, Redshift, BigQuery

```ad-info
**normalized data**: reduces redundancy when writing multiple lines by splitting columns into multiple relations
- 1NF -> 2NF -> 3NF (each removes a type of redundancy/dependency)
- pros: less storage, no update anomalies
- cons: more joins -> slower reads
```

**Normal Forms**
- *1NF*: atomic values, no lists in a cell
- *2NF*: 1NF + no partial dependency (non-key columns depend on multiple non-key columns)
- *3NF*: 2NF + no transitive dependency (non-key columns don't depend on other non-key columns)
## Star vs Snowflake Schema
```ad-tldr
**star**: fact table + *denormalized dimension tables* (1 level) -- used for simplicity and when storage costs low, *faster* query speed (less joins)
**snowflake**: fact table + *normalized dimensions* (multiple levels) -- used for save storage cost & data integrity, *slower* query speed (more joins)
```

![[Pasted image 20260521190246.png]]

**Fact table**: stores measurements, metrics, events
- numeric and additive
- contains *foreign keys* to dimensions

**Dimension table**: stores descriptive context
- the "who/what/where/when"
- used to filter and group facts

**Grain**: the level of detail of *ONE ROW* in the fact table
- e.g. "one row per order"

## Slowly Changing Dimensions (SCD)
|Type|What it does|History?|When to use|
|---|---|---|---|
|**Type 0**|Never changes (fixed/retain original)|N/A|Birth date, original signup|
|**Type 1**|Overwrite old value|❌ No history|Correcting errors; history doesn't matter|
|**Type 2**|Add a _new row_ with version flag / effective dates|✅ Full history|Most common; you need to track changes over time|
|**Type 3**|Add a _new column_ (e.g., previous_value)|⚠️ Limited (one prior)|Only need to keep the immediately prior value|
Type 2 is most common
- Mechanism: keep old row, but mark it inactive with an `is_current` = FALSE column

## Data Modeling Approaches
**Kimball** (bottom-up)
- build dimensional data marts (star schemas)
- integrate via conformed dimem