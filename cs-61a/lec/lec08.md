# Lec 08 - Recursion

## Recursion

```py
def add_next(n):
    print(n)
    return lambda f: subtract_next(n + f)

def subtract_next(n):
    print(n)
    return lambda f: add_next(n - f)

add_next(2500)(500)(1000)(24)
'''
2500
3000
2000
2024
'''
```
