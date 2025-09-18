## Query Optimization Process: 4 steps
### Step 1: Convert into a logical query plan (in RA)

![[Screenshot 2025-09-18 at 2.21.36 PM.png]]

**Example**
![[Screenshot 2025-09-18 at 2.34.57 PM.png]]
### Step 2: Apply rewriting to find other equivalent logical plans

Use query plan tree to visualize order of operations 

![[Screenshot 2025-09-18 at 2.21.02 PM.png]]

### Step 3: Use cost estimates to pick logical plan and physical plan

Cost estimates are combination of CPU and I/O costs

A physical plan is compose of physical operators: implementations of logical ops
- Ex. which algorithms swap in for the symbols in query plan tree
- choose whether to index on a key
- choose which join algorithm to use for least cost
- choose whether to wait until joining results before doing projection

![[Screenshot 2025-09-18 at 2.23.54 PM.png]]

### Step 4: Feed physical plan to query processor

![[Screenshot 2025-09-18 at 2.24.26 PM.png]]

## Logical & Physical Operators

Logical operators
- extended RA operators
- describe "what" is done: union, select, grouping, project

Physical operators
- describe "how" to do it
- physical ops rely on small set of techniques: sorting, hashing, indexing, nested-looping
- Ex.
	- table scan (sequential, index, heap/bitmap heat)
	- join algorithms


## Ops in **Query-centric** vs **Code-centric** data systems

In **query-centric** data systems, the *system* is responsible for both logical and physical choices
- Systems will attempt to convert "complex" queries (with subqueries) into an RA expression format
![[Screenshot 2025-09-18 at 2.38.37 PM.png]]

In **code-centric** data systems, *both* are possible:
- user supplies logical query plan, system selects physical impl.
	- Ex. in dataframes
- user supplies logical query plan + parts of physical impl.
	- Ex. in MapReduce or MongoDB

## Relational Algebra Rules
![[Screenshot 2025-09-18 at 2.44.14 PM.png]]

![[Screenshot 2025-09-18 at 2.44.23 PM.png]]

**Predicate pushdown**
![[Screenshot 2025-09-18 at 3.06.37 PM.png]]
![[Screenshot 2025-09-18 at 3.12.25 PM.png]]
TLDR: nest `WHERE` conditions ($\sigma$ in RA) with their respective table *BEFORE* applying any joins

**Projection pushdown**
![[Screenshot 2025-09-18 at 3.08.11 PM.png]]
TLDR: nest `SELECT` operations (**projection** in RA) with their respective table *BEFORE* applying any joins