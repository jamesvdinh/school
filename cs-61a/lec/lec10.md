# Lec 10

```py
>>> silver
[1, [3]]

>>> gold
[2, [4, 5]]

>>> platinum
[6, [[7, 8]]]
```

```py
def park(n):
    if n < 0:
        return []
    elif n == 0:
        return [""]
    else:
        return ['%' + c for c in park(n-1)] + ['.' + c for c in park(n-1)] + ['<>' + c for c in park(n-2)]


print(park(4))

```
