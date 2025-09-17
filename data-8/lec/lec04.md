# Lec 04 - Data Types & Arrays

> ## Important Info
>
> Don't direct email GSI if not assigned

## Tables

- using a `select` or other operation will not update the original table
  - have to assign to new var

```py
cones.drop('Color').where('Color', 'pink')
# Error: doesn't work since label 'Color' doesn't exist within table after drop

cones.where('Color', 'pink').drop('Color')
# this works
```

## int & float

- int: integer of any size
- float: num w/ optional fraction
  - has size limit
  - precision only after 15-16 dec places

## strings

- enclosed within ', "
- are intrinsically arrays of chars

```py
'Ben' + 10
# Error, can't combine str and int

'ha' * 100
# hahahahahahaha...
```

## Conversions

```py
int('12')
# 12

str(10)
# '10'
```

## Types

```py
type(10)
# int

type(abs)
# builtin_function_or_method
```

## Arrays

```py
arr = make_array(1, 2, 3, 4)
type(arr)
# numpy.ndarray

arr * 2
# array([2, 4, 6, 8])
```
