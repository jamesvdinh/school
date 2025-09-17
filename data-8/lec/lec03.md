# Lec 03

## Important info

- DSP stuff

## Python

- Apps:
  - Data science
  - SDE
- mastering is critical -> practice

### Syntax

```py
2 ** 4
# 16

2 * * 4
# SyntaxError: invalid syntax

(2 + 3) * 6
# 30

'hello world'
# ;hello world'
```

- expressions -> values
- divison -> float

### Names

```py
hours_per_wk = 24 * 7
```

- assignment statment: changes meaning of name to left of '='

```py
a = 2
a
# 2

b
# NameError: name 'b' is not defined

b = 3
b
# 3

a = 2
a = a + 5
a
# 7

a, b = 2, 3
total = a + b
total
# 5

a = 10
total
# 5
```

- why names?

```py
40 * 16 * 50
# 32000

min_wage = 16
hrs_per_wk = 40
wks_per_yr = 50

hrs_per_yr = hrs_per_wk * wks_per_yr

wages = hrs_per_yr * min_wage
wages
# 32000
```

### Functions, call expression, arguments

```py
abs(-5)
# 5

min(1, 2, 3)
# 1
```

## Tables

```py
cones = Table.read_table('cones.csv')
cones
```

| Flavor     | Cones       | Price |
| ---------- | ----------- | ----- |
| strawberry | pink        | 3.55  |
| chocolate  | light brown | 4.75  |
| chocolate  | dark brown  | 5.25  |
| ...        | ...         | ...   |

```py
cones.drop('Price')
```

| Flavor     | Cones       |
| ---------- | ----------- |
| strawberry | pink        |
| chocolate  | light brown |
| chocolate  | dark brown  |
| ...        | ...         |
