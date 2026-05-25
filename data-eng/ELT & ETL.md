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

---
## DBT Project File Structure
### Root files
**`dbt_project.yml`**: tells DBT *WHERE* to look for models, macros, seeds, etc.
- also define *models* (tables) here such as *staging* and *marts*
- you can tell dbt to materialize models as views or tables using `+materialized: (view/table)`
**`packages.yml`**: define dbt packages such as dbt_utils
- find latest version [here](https://hub.getdbt.com/dbt-labs/dbt_utils/latest/)
```yml
packages:
  - package: dbt-labs/dbt_utils
	version: 1.3.3
```
- run `dbt deps` to install dependencies
## Directories
- **`/models`**: SQL logic, source datasets, staging files
	- good practice to *separate* staging files and source files (place in `/marts`)
	- staging files are *one-to-one* with source files
- **`/seeds`**: *static* files -> for files where data will not change very often
	- e.g. a CSV file that you need to reference with very occasional updates
- **`/macros`**: reusable macros
- **`/dbt_packages`**: auto-generated from running `dbt deps`, contains 3rd-party libraries
- **`/snapshots`**: incremental models
- **`/test`**: dbt test types
	- **singular tests**: stores SQL queries that return *failing rows*
		- executes `.sql` files on `dbt test`
	- **generic tests**: *parameterized* queries (can take arguments) that can be called in `.yml` files
		- e.g. check if this model has `NULL` values or if values are `> 0`
