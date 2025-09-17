# CTEs, Views, and Subqueries

## CTE (Common Table Expression)

### For use now or later

1. create new table

    > USE CASE: independent copy of data

    ```sql
    CREATE TABLE CitationStops AS (...)
    ```

    - treated as regular tabl
    - if base table chanes, we must MANUALLY change any derived tables

2. create virtual view

    > USE CASE: "save" a complex query string for reuse

    ```sql
    CREATE VIEW f AS (...)
    ```

    - view for short
    - queried just like table: `SELECT * FROM f`
    - output is not stored, computed on demand
    - kind of like a variable that is more convenient to query

3. create materialized view

    > USE CASE: "save" results of complex query, where base data doesn't change much

    ```sql
    CREATE MATERIALIZED VIEW CitationStops AS (...)
    ```

    - somewhere between views and tables
    - output is stored on disk, unlike views
    - some MVs update as base table changes
    - MVs can add unnecessary changes to percolated tables (when you don't need the subtables to update as often) -> too much overhead

### For immediate use

1. create temporary table (ex. a CTE)

    > USE CASE: need to reuse subquery multiple times

    ```sql
    WITH citation_stops AS (...)
    SELECT *
    FROM citation_stops;
    ```

    - CTE for short
    - like virtual views, computed on demand
    - can improve query readability

    Ex.

    > How do we find the stops that happened in the same location as the stop with ID 123?

    ```txt
        Stops
    Column   | Type       
    ----------+------------------
    id       | bigint           
    race     | text            
    sex      | text            
    age      | double precision 
    arrest   | boolean     
    citation | boolean        
    warning  | boolean       
    search   | boolean        
    location | text           
    ```

    Without CTE

    ```sql
    SELECT s1.id, s1.race, s1.location
    FROM Stops as s1,
        Stops as s2,
    WHERE s1.id = 123
        AND s1.location = s2.location
    ```

    With CTE

    ```sql
    <!-- stores the location of stop 123 -->
    WITH Location123 AS (
        SELECT location FROM Stops WHERE id = 123
    )
    SELECT S.id, S.race, S.location
    FROM Stops as S,
        Location123
    WHERE S.location = Location123.location;
    ```

2. use a subquery

    > USE CASE: when using `EXISTS/NOT EXISTS` or `IN/ANY/ALL`

    A query that appears inside another query

    `scalar subquery`: returns single tuple w/ single attribute value

    ```sql
    SELECT S1.id, S1.race, S1.location
    FROM Stops AS S1
    WHERE S1.location = (
            SELECT S2.location
            FROM Stops AS S2
            WHERE S2.id = 123);
    ```

    `EXISTS` subquery (correlated subquery): checks if a query is non-empty via `EXISTS`

    Determine locations in Stops that don’t have a corresponding zipcode in Zips:

    - iterate each tuple in Stops -> run the NOT EXISTS query
      - if output from query is empty -> add the output

    ```sql
    SELECT DISTINCT Stops.location 
    FROM Stops
    WHERE NOT EXISTS (
        SELECT * FROM Zips
        WHERE Zips.location = Stops.location
    );

    ```

    `IF/ANY/ALL` subquery

    true iff `if/any/all` matches the chosen word

    Determine the locations in Stops that have a location that could be in multiple zip codes:

    ```sql
    SELECT DISTINCT Stops.location 
    FROM Stops
    WHERE location IN (
        SELECT location FROM Zips z
        GROUP BY location HAVING COUNT(*) > 1
    );
    ```
