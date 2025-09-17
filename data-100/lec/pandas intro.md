# Lec 1 - pandas intro

```py
s = pd.Series([-1, 10, 2], index = ["a", "b", "c"])
s.index = ["first", "second", "third"]
# index can be changed

s = pd.Series([4, -2, 0, 6], index = ["a", "b", "c", "d"])

s["a"]
# 4

s[["a", "c"]]
# (series object)
# a 4
# c 0
# dtype: int64

even = (s%2 == 0)
even
# a True
# b False
# c False
# d True
# dtype: bool

even.values
# array([True, False, False, True])
```

Example

```py
ex = pd.Series([4, 5, 6], index = ["one", "two", "three"])
ex[ex > 4].values
# array([5, 6])
```

Creating a dataframe

```py
pandas.DataFrame(data, index, columns)

# csv
elections = pd.read_csv("data/elections.csv", index_col="Year")

# list/col name
pd.DataFrame([1, "one", 2, "two"], columns=["Num", "Desc"])

# dict
pd.DataFrame({"Fruit": ["Straberry", "Orange"], "Price": [5.49, 3.99]})
```

- from csv file
- using list and column name(s)
- from dict
- from Series

```py
s_a = pd.Series(["a1", "a2", "a3"], index = ["r1", "r2", "r3"])
s_b = pd.Series(["b1", "b2", "b3"], index = ["r1", "r2", "r3"])
pd.DataFrame({"A col": s_a, "B col": s_b})
```

- you can always call `reset_index()` on a dataframe to reset the index

```py
df.loc[row_labels, column_labels]
elections.loc[[87, 25, 179], "Popular vote": "%"]
```

.loc args

- list
- slice (inclusive of right hand side)
- single value

```py
elections.loc[:, ["Year", "Candidate", "Result"]]
# shows only year, candidate, and result columns
```
