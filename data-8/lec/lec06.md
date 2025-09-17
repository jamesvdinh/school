# Lec 06

## Table queries

- proprtion of nba players with salaries above $15M

```py
nba.where('salary', are.above(15000000)).num_rows / nba.num_rows
```

- columns must be same row length for each label
- table operators do not automatically reassign to the original table
