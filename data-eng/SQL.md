## Basics
## Casting
```ad-example
~~~sql
SELECT TO_DATE(order_date, 'MM/DD/YYYY HH24:MI') AS order_date
~~~
```

## Snowflake example:
> Very Simple ETL Pipeline in Snowflake: https://www.youtube.com/watch?v=x_4XUVgarqs

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
