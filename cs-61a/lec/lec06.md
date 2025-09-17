# Lec 06 - Lambda Functions

## Zero-argument functions

```py
>>> (3 and 4) - 5
# -1
```

### lambda

```py
>>> (lambda f: lambda x: f(f(x)))(lambda y: y * y)(3)
# 81
```
