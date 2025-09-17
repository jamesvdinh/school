# Lec 05

## Tables

```py
# which one calculates avg?
np.average(recent_curry.select('salary'))

np.average(recent_curry.column('salary')) # <- this one
```

```py
du_bois = Table().read_table("du_bois.csv")
du_bois.num_rows
# # of rows

du_bois.num_columns
# # of columns

du_bois.labels
# array of str labels
```

## Ranges

- arrays of consecutive nums

```py
make_array(np.arange(7))
# array([0, 1, 2, 3, 4, 5, 6])
```

- np.arange(start, stop, step)
- stop is **not inclusive**
