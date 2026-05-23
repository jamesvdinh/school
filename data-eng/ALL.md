```ad-tldr
title: What is Snowflake?
A cloud-based data warehouse platform built to be used in the cloud (AWS, Azure, Google Cloud Platforms)
```

First analytics database solution in the cloud
- runs entirely on *public cloud infrastructure*
## Snowflake's Architecture
Built specifically for the *cloud*

```ad-important
Snowflake specifically pushes ELT because it separates storage from compute and offers *cheap storage* and *flexible compute*. Thus, loading raw data and then transforming in-warehouse (via `SQL` or `dbt`) is efficient and scalable.
```

Unique *multi-cluster* architecture delivers:
- performance & efficiency
	- computes queries using the *massively parallel processing* compute clusters -- each node in the cluster stores a part of the entire dataset locally
- concurrency
- elasticity

Handles:
- authentication
- resource management
- optimization
- data protection
- configuration
- availability

Combines the benefits of *shared disk* and *shared nothing* architectures
```ad-info
**shared disk**: where multiple computers access the *same storage disk* simultaneously, allowing all nodes to read/write to a common storage pool
**shared nothing**: partitions data across *independent nodes* (that have their own memory and storage) and only communicate with each other through a network -- best for scalability with more nodes
```

### Data Warehouse Layers

**Storage Layer**
- stores all data in loaded in snowflake including *structured* and *semi-structured* data
- automatically manages all aspects of how data is stored
	- organization
	- file size
	- structure
	- compression
	- metadata
	- statistics

```ad-info
title: Structured vs Unstructured Data
**structured data**: highly rigid and organized; fits in tables with rows and columns (ex. SQL databases, CSV, Excel)
-  relational databases (RDBMS)
- *difficult* to change, yet highly efficient using SQL

**semi-structured data**: flexible hierarchy of data; uses tags, markers, or keys to separate data elements and enforces hierarchy within data itself (ex. JSON, XLM, YAML)
- NoSQL databases
- *easy* to change, yet highly inefficient on JOINs
```

**Compute Layer**
- made up of *virtual warehouses* that execute data processing tasks required by queries
	- **virtual warehouses**: a *cluster* of compute resources (the "engine" that runs queries) -> can be resized
	- multiple *VWs* can access the same data without conflicts
- can access all data in storage later, then work independently to save resources
	- allows for *non-disruptive* automatic scaling
	- while queries run, compute resources can *scale* without the need to redistribute or rebalance data in the storage layer

**Cloud Services Layer**
- uses ANSI SQL and coordinates entire system
- eliminates the need for manual data warehouse management
	- authentication
	- infrastructure management
	- metadata management
	- query parsing/optimization
	- access control

```ad-important
By design, all layers are automatically *scalable* and are *redundant*
```

## Data Storage Concepts
```ad-tldr
storage and compute scale *independently*; you pay for it *separately*.
```

**Data Partitioning** (micro-partitions)
- data is auto-divided into small, compressed immutable columnar partitions
- Snowflake stores min/max metadata per partition
- *faster* query speed due to indexing on metadata

**Zero-copy Cloning**
- clone a table/schema/db instantly without duplicating storage using pointers =
	- only adds to storage on data modification
## Designing a Database

### Step 1: create the DB
```sql
CREATE OR REPLACE DATABASE SALES_DB
```

**Creating schemas**
Databases usually only provide two basic schemas on creation: `INFORMATION_SCHEMA` and `PUBLIC`
```sql
CREATE SCHEMA IF NOT EXISTS SALES_DB.RAW
CREATE SCHEMA IF NOT EXISTS SALES_DB.ANALYTICS
```

### Step 2: load the data into `RAW`
each **ELT** pipeline begins at a data source and ends at a database (e.g. data warehouse). In this case, the source is the raw CSV file and the destination is the `RAW` database

```ad-abstract
title: steps
1. go to `+` -> `Load data into a Table`
2. select the CSV file
3. choose dataase `SALES_DB`
4. choose schema `RAW`
5. name the table `SALES`
```

```ad-info
Snowflake automatically creates the *schema* from the imported data from the column names themselves
```


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

> [Code along - build an ELT Pipeline in 1 Hour (dbt, Snowflake, Airflow)](https://www.youtube.com/watch?v=OLXkGB7krGo)

## ETL Orchestration

**Batch vs Streaming**
- *batch* processes data in scheduled chunks
- *streaming* processes records continuously as they arrive

**Docker**
- packages an app + dependencies into a portable container
- lightweight and isolated, not a full VM

**Kubernetes**
- orchestrates many containers across machines
- scheduling, scaling, self-healing, networking

**Data quality dimensions**
- completeness, uniqueness, validity, consistency, accuracy, timeliness
### Airflow
```ad-tldr
orchestrates workflows as DAGs of tasks with dependencies and schedules
```

- An *operator* defines a unit of work
	- A *task* is an instance of an *operator*
- The *scheduler* triggers runs
- *Idempotent tasks + retries* make pipelines reliable
- no dependencies -> always resolves
- acyclic = no loops

```ad-info
**idempotency**: running the same task twice produces the same result
- no duplicates
- essential for safe retries/backfills
  
**backfill**: running a pipeline for past dates to populate historical data
```


```ad-example
~~~py
def add(item, bucket=[]):
	bucket.append(item)
	return bucket

print(add(1))  # [1]
print(add(2))  # [1, 2]
~~~
Bug: the default parameter `bucket = []` are evaluated **once**, not on each call
- mutable defaults (list/dict) are shared across all calls

```

## Shallow vs Deep Copy
```ad-example
~~~py
# Pointer
a = [1, 2, 3]
b = a  # pointer ref
b.append(4)
print(b)  # [1, 2, 3, 4]

# Shallow copy: new outer container, SAME inner objects
x = [[1, 2], [3]]
y = list(x)
y[0].append(4)
print(x)  # [[1, 2, 4], [3]]

# Deep copy: fully independent
import copy
z = copy.deepcopy
z[0].append(0)
print(x)  # unchanged
~~~
```

**Mutable**: primitives such as ints, strings, tuples
**Immtable**: lists, dicts, sets, objects

## Generators vs Lists
```ad-example
~~~py
nums = [x ** 2 for x in range(5)]  # creates a list in memory
gen = (x ** 2 for x in range(5))
~~~

print(sum(gen))   # 30
print(sum(gen))   # 0 <-- already used up gen
```

use `yield` to return the next iter value

## OOP (Object Oriented Programming)
| **Principle**     | **Key Concept**   | **In Simple Terms**                                    | **Python Mechanism**                 |
| ----------------- | ----------------- | ------------------------------------------------------ | ------------------------------------ |
| **Encapsulation** | Data hiding       | Keep private data safe inside the object.              | Double underscores (`__variable`)    |
| **Inheritance**   | Reusability       | Pass down traits from a parent class to a child class. | Class syntax: `class Child(Parent):` |
| **Polymorphism**  | Many forms        | Same method name, completely different behavior.       | Overriding methods across classes    |
| **Abstraction**   | Hiding complexity | Show the user _what_ it does, hide _how_ it does it.   | `abc` module & `@abstractmethod`     |

## Decorators
```ad-example
~~~py
def logged(func):
    def wrapper(*args, **kwargs):
        print(f"calling {func.__name__}")
        return func(*args, **kwargs)
    return wrapper

@logged   # same as: greet = logged(greet)
def greet(name):
    return f"Hi {name}"

greet("Sam")  # prints "calling greet", returns "Hi Sam"
~~~
```

- `*args` and `**kwargs` (keyword args) accepts any signature

## Basics
```ad-tldr
title: Query Execution Order

`FROM` -> `JOIN/ON` -> `WHERE` -> `GROUP BY` -> `HAVING` -> `SELECT` -> `DISTINCT` -> `ORDER BY` -> `LIMIT`

Example:
~~~sql
(6, 7)  SELECT DISTINCT a.id, a.name, b.item, SUM(b.cost) AS total
(1)     FROM customers a
(2a)    JOIN orders b
(2b)    ON a.id = b.order_id
(3)     WHERE a.name = "John"
(4)     GROUP BY a.id, a.name, b.item
(5)     HAVING SUM(b.cost) > 100
(8)     ORDER BY total DESC
(9)     LIMIT 10;
~~~
```

**`WHERE`**: filters rows
**`HAVING`**: filters groups
**`ORDER BY`**: can use `SELECT` alias
## `JOIN`s
using `ON` vs `WHERE` on a `JOIN`
- `ON` -> compares keys before joining
- `WHERE` -> performs Cartesian product on tables and then filters

| **Join Type**              | **What it Returns**                                                                                 |
| -------------------------- | --------------------------------------------------------------------------------------------------- |
| **`INNER JOIN`**           | Only rows where there is a **perfect match** in both tables.                                        |
| **`LEFT JOIN`** _(Outer)_  | **All rows from the left table**, plus matching rows from the right table.                          |
| **`RIGHT JOIN`** _(Outer)_ | **All rows from the right table**, plus matching rows from the left table.                          |
| **`FULL JOIN`** _(Outer)_  | **All rows from both tables**. If there's a match, they link. If not, the missing side gets `NULL`. |
| **`CROSS JOIN`**           | The **Cartesian product**—every single row from Table A paired with every single row from Table B.  |
## Window Functions
Example
```sql
SELECT
  name, dept, salary,
  ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC) AS rn,
  RANK() OVER (PARTITION BY dept ORDER BY salary DESC) AS rnk,
  SUM(salary) OVER (PARTITION BY dept) AS dept_total,
  LAG(salary) OVER (PARTITION BY dept ORDER BY salary) AS prev_salary
FROM employees;
```

| **Function**          | **Behavior**                                                   |
| --------------------- | -------------------------------------------------------------- |
| **`ROW_NUMBER()`**    | 1,2,3,4 — always unique, no ties                               |
| **`RANK()`**          | 1,2,2,4 — ties share rank, gaps after                          |
| **`DENSE_RANK()`**    | 1,2,2,3 — ties share rank, no gaps                             |
| **`LAG()/LEAD()`**    | Value from previous / next row (great for diffs, growth)       |
| **`SUM() OVER(...)`** | Running total or partition total depending on ORDER BY / frame |
## Aggregation & NULLs
| **Trap**             | **Fact**                                                                    |
| -------------------- | --------------------------------------------------------------------------- |
| COUNT & NULL         | `COUNT(*)` counts all rows; `COUNT(col)` ignores NULLs.                     |
| NULL comparison      | `= NULL` never matches. Use `IS NULL` / `IS NOT NULL`.                      |
| NULL in aggregates   | `SUM/AVG` skip NULLs. `AVG` divides by non-null count — may surprise you.   |
| GROUP BY rule        | Every non-aggregated SELECT column must be in GROUP BY.                     |
| WHERE vs HAVING      | WHERE filters rows pre-aggregation; HAVING filters groups post-aggregation. |
| DISTINCT vs GROUP BY | Both dedupe; GROUP BY needed when aggregating per group.                    |
| COALESCE             | `COALESCE(col, 0)` returns first non-null — replace NULLs.                  |
| Integer division     | In many DBs `5/2 = 2`. Cast: `5.0/2` or `CAST(x AS FLOAT)`.                 |

## Casting
```ad-example
~~~sql
SELECT TO_DATE(order_date, 'MM/DD/YYYY HH24:MI') AS order_date
~~~
```

## Snowflake example:
> [Very Simple ETL Pipeline in Snowflake](https://www.youtube.com/watch?v=x_4XUVgarqs)

**ONE** database with multiple *splits* (raw, analytics)

structure:
```md
SALES_DB
├── ANALYTICS
│   └── SALES
├── INfORMATION_SCHEMA
├── PUBLIC
├── RAW
│   └── SALES_DATA
```

```ad-example
~~~sql
CREATE OR REPLACE TABLE SALES_DB.ANALYTICS.SALES AS
SELECT
	order_num,
	SELECT TO_DATE(order_date, 'MM/DD/YYYY HH24:MI') AS order_date,
	year_id,
	SUM(sales) AS total_sales
FROM SALES_DB.RAW.SALES_DATA
GROUP BY order_num, TO_DATE(order_date, 'MM/DD/YYYY HH24:MI'), year_id;
~~~
The query from the `SELECT` onwards is the **Transform**, while the `CREATE OR REPLACE` is the **Load**
```
