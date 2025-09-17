# Lec 13

```py
len(t.branches)
t.branches[i].label == t.branches[j].label
return sum([twins(branch) for branch in t.branches]) + count
```

## Big O

Exponential:

- Fib recursive sequence: O(2<sup>n</sup>)

Quadratic:

- O(n<sup>2</sup>)

Linear:

- O(n)

Log:

- O(log<sub>2</sub>(n))

Constant:

- O(1)
