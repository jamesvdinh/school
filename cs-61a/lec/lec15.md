# Lec 15

```py
if hydra.is_leaf():
    hydra.label = 2
    hydra.branches = [Tree(1), Tree(1)]
else:
    hydra.label += 1
if n > left.label:
    chop_head(right, n - left.label)
```
