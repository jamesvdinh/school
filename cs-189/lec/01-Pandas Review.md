## iloc[]

```py
df.iloc[[1,2]]
```

Selects rows 1 and 2 (omits 0-th index row)
- includes all columns

```py
df.iloc[[1,2], [1, 2, 3]]
```

Selects rows 1 and 2 **AND** columns 1, 2, 3

```py
df.iloc[2:3]
```

Selects rows 2 and 3
- a **slice** is inclusive of the right-hand side
## drop

```py
df.drop(columns=["Year"], inplace=True)
```

Deletes the column of the **original** df with `inplace=True`