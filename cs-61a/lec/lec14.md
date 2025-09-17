# Lec 14

```py
def f(start, end):
    s = Link.empty
    k = end - 1
    while k >= start:
        s = Link(k, s)
        k -= 1
    return s
```

```py
def append(s, x):
    if s.rest is not Link.empty:
        append(s.rest, x)
    else:
        s.rest = Link(x)
```
