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
