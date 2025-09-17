# Lec 04 - Higher Order Functions

## Important info

- HW 1 due 9/9
- HW 2 due 9/12
- Hog (project) due 9/19
  - checkpoint due 9/12

## Doctests

```py
def prime_factors(x):
    '''
    >>> prime_factors(6)
    2
    3
    '''
    ...
```

```sh
$ python3 -m doctest -v prime_factors.py
2
3
```

## Higher Order Functions

```py
def cube(x):
    return pow(x, 3)

def summation(n, terrm):
    '''
    >>> summation(5, cube)
    225
    '''
    total, k = 0, 1
    while k < n:
        total += term(k)
        k += 1

summation(5, cube)
# 225
```

## 21 game

```py
def plya(strat0, strat1, goal = 21):
    n, who = 0, 0
    while n < goal:
        if who == 0:
            n += strat0(n)
            who = 1
        elif who == 1:
            n += strat1(n)
            who = 0
    return who

def add_three(x):
    return x += 3
```
