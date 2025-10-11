Instead of "data cleaning", think data transformation
- inherently imposing a model over our data

## Outliers
An **outlier** is a value "*far enough*" from "*average*"
- helps us identify aberrances with respect to a model
- handling outliers helps us model the majority of the data

Strategy for handling outliers requires 2 statistical measures on a set of values:
- **center**: average
	- mean, median
- **dispersion**: what is "far" from average
	- standard deviation, variance, IQR

Common outlier handling strategies:
1. dropping **gaussian outliers**
2. **trimming** outliers based on percentiles
3. **winsorizing**
	- replaceing outliers with percentile statistics
4. dropping "median outliers" w/ **Hampel x84**

### Dropping gaussian outliers
Model data as normal (Gaussian) distribution
- center: mean
- dispersion unit: std
- define *gaussian outlier* as
	- $\pm$ 2 std from mean
	- below p2.5 and above p97.5

![[Screenshot 2025-10-11 at 10.57.42 AM.png]]

Strategy:
- find outliers via a SQL query and store them in a `VIEW`
	- `WHERE x NOT BETWEEN b.lo AND b.hi`
- drop outliers from original distribution table
- the cleaned distribution has fewer extreme, has lower dispersion
![[Screenshot 2025-10-11 at 11.03.25 AM.png]]

Summary:
- simple to implement
- definition of dispersion (and center) are not robust and are sensitive to distribution of outliers themselves

### Trimming outliers based on percentiles
k% trimming drops both k% tails of the distribution
- ex. 5% trimmed distribution drops below p5 and above p95

### Winsorizing
Ex. replacing outliers with percentile statistics
k% winsorization replaces tails with k-, (100-k)-percentile values
- ex. 5% winsorization
	- replace values $\le$ p5 with the p5 value
	- replace values $\ge$ p95 with the p95 value
	- Note: sometimes called 90% winsorization

![[Screenshot 2025-10-11 at 11.08.07 AM.png]]
![[Screenshot 2025-10-11 at 11.09.06 AM.png]]

## Robustness
**Robustness** is a worst-case analysis of an estimator
- robust statistics "*maintain* their properties even if the underlying distribution is incorrect"
- Ex. non-robust statistic: mean
	- if extreme outlier in dataset, mean is thrown off
	- the mean is no longer in "*center*"; it could even become an outlier in itself
- Ex. robust statistic: median
	- maximally robust -> can handle up to 50% corruption of the data

### Breakdown points
A **breakdown point** of an estimator
- the *smallest fraction* of corrupted values an estimator can handle before an incorrect (ex. arbitrarily large) result
- for data preparation, knowing the *breakdown point* determines when masking can occur (ex. when our ability to detect all outliers is impacted by a few extra extreme outliers)

![[Screenshot 2025-10-11 at 11.23.03 AM.png]]

### Robust estimators
Center:
- median (maximally robust, 50% trimmed mean)
- k% trimmed mean
	- generally, order statistics (like percentiles) robust
- k% winsorized mean

Dispersion:
- **Median Absolute Deviation** (MAD)
	- maximally robust
	- $MAD(X) = median(|X_i - \bar{X})$
		- $\bar{X} = median(X)$

## Dropping "median outliers" w/ Hampel x84

> Trim with Gaussian Outliers: 2x stddev from the mean

Center: **median**
Dispersion unit: **MAD**
Trim: 2x **1.4826 MADs** from median
- 1 stddev = 1.4826 MADs


## Imputation

### Default values for a column
Impute missing data using the mean, or some other univariate data statistic

Steps:
1. Aggregate CTE to compute mean
2. Query

```sql
WITH elevavg AS (
  SELECT avg(elev_in_m)::int FROM holey
)
SELECT h.*, 
       CASE WHEN h.elev_in_m IS NOT NULL
            THEN h.elev_in_m
            ELSE e.avg
         END AS imputed_elev_in_m
FROM holey h,
     elevavg e
LIMIT 100;
```

### Correlation across columns
Interpolate by assuming a relation between columns
- given a correlation model, apply a scalar function

Ex. linear regression model
- elevation meters = $f(longitude)$

### General model-based interpolation
Similar to above method, but trained on different data in advance
- call scalar function taking a model prediction function
- pass values in tow as parameters to model predictor

General example
```sql
SELECT *,
       CASE WHEN column IS NOT NULL
            THEN column
            ELSE model_predict(<constants>,  
                              <columns>)
       END AS imputed_column
FROM table;
```

### Interpolation across ordered rows
Order rows in relation, then use that order to impute values
- Ex. "fill down" until next non-null value

![[Screenshot 2025-10-11 at 1.14.07 PM.png]]
