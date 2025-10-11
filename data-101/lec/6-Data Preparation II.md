## Table Sample Performance
`ORDER BY RANDOM`
- expensive for huge tables (due to sorting)

`TABLESAMPLE BERNOULLI(p)`
- Essentially equivalent to above, but sometimes a bit faster in implementation

`TABLESAMPLE SYSTEM(p)`
- Faster, but less random (due to page-level sampling)

## Scalar functions
A function on atomic values
- in the relational model, this is a function on constants and individual attributes of a single relational tuple

**Scalar**: a value for any individual attribute
![[Screenshot 2025-10-06 at 4.21.25 PM.png]]

Efficiencies on the processor:
- run in parallel on many different records
- memory reuse
- uses pipelining to execute scalar functions "on-the-fly" as tuples are emitted

## Aggregate Functions
Aggregate functions take a set (or vector) of values as their input

- **Univariate functions** on sets: min, max, sum, avg, stddev, variance
- **Bivariate functions** on sets: correlation, covariance, regression

Agg functions will not return an answer until a **full pass** on the data
- this *blocking* makes them time-consuming on large datasets

Possible solution: **Table samples**

## Window Functions
Window functions consider rows beyond the current row (a window) in calculation
- without collapsing rows into groups, rows retain separate identities

![[Screenshot 2025-10-06 at 4.30.00 PM.png]]

The value at a particular "position" in a distribution
- SQL: use `WITHIN GROUP (ORDER BY...)`
Ex.
```sql
percentile_cont(0.25) WITHIN GROUP (ORDER BY HR) AS p25,
```
## Granularity Transformations
Discretization of numerical granularity
- encoded as fixed number of bits

Data Hierarchy:
Numerical data is measured in a hierarchy of units
- seconds → hours → minutes → days
- mm → cm → m → km
- in → ft → mi

**"rolling up"**: coarser granularity
- go *up* in a hierarchy
**"drilling down"**: finer granularity
- go *down* in a hierarchy