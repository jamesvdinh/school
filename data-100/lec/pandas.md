# Lec 2 - Pandas II

## `loc`

> `df.loc[row_indices, col_names]`
>
> _row_indices_: arr of row labels  
> _col_names_: arr of col labels

Ex.

```py
elections.loc[[1, 2], ["Candidate", "Party"]]
# elections w/ rows labeled 1, 2 & cols "Candidate" & "Party"

babynames.loc[babynames["Sex"] == "F", :]
# returns table w/ only females
```

## `iloc`

> `df.iloc[row_integers, col_integers]`
>
> _row_integers_: arr of row indices  
> _col_integers_: arr of col indices

Ex.

```py
elections.iloc[[1, 2, 3], :1]
# rows 1, 2, 3 & first col
```

## `[]`

> df[row_indices]
>
> _row_inices_: slice of row indices

Ex.

```py
elections[3:7]
# same as elections.iloc[3:7, :]
```

## `sort_values`

> `Series.sort_values(by = column_name, ascending = True)`
>
> _column_name_: String  
> _ascending_: Bool
