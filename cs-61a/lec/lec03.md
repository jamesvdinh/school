# Lec 03 - Print & None

## print & none

- pure: function w/ only a return statement
- non pure: function w/ other statements in body (print, etc.)

```py
def f(x):
    return print(x + 1)

def h(x):
    print(f(x))
    return f(x)

def h2(x):
    y = f(x)
    print(y)
    return y

h(2)
# 3
# None
# 3

h2(2)
# None
# 3
```
