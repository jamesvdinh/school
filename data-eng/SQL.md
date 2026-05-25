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
### `FILTER`
```sql
SELECT
	COUNT(*) FILTER (WHERE skill = 'laptop') AS laptop_views,
	COUNT(*) FILTER (WHERE skill IN ('tablet', 'phone')) AS mobile_views
FROM viewership
```

**BETWEEN**: inclusive
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
