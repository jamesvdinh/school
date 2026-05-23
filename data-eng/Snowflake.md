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
