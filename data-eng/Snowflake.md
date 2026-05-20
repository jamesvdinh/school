> A cloud-based data warehouse platform built to be used in the cloud (AWS, Azure, Google Cloud Platforms)

First analytics database solution in the cloud
- runs entirely on *public cloud infrastructure*

## Snowflake's Architecture
Built specifically for the *cloud*

Unique *multi-cluster* architecture delivers:
- performance & efficiency
	- computes queries using the massively parallel processing compute clusters -- each node in the cluster stores a part of the entire dataset locally
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

### Data Warehouse Layers
By design, all layers are automatically scalable and are redundant

**Storage Layer**
- stores all data in loaded in snowflake including *structured* and *semi-structured* data
- automatically manages all aspects of how data is stored
	- organization
	- file size
	- structure
	- compression
	- metadata
	- statistics

> **structured data**: highly rigid and organized; fits in tables with rows and columns (ex. SQL databases, CSV, Excel)
> 	- relational databases (RDBMS)
> 	- *difficult* to change, yet highly efficient using SQL
> **semi-structured data**: flexible hierarchy of data; uses tags, markers, or keys to separate data elements and enforces hierarchy within data itself (ex. JSON, XLM, YAML)
> 	- NoSQL databases
> 	- *easy* to change, yet highly inefficient on JOINs

**Compute Layer**
- made up of *virtual warehouses* that execute data processing tasks required by queries
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