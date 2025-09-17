# Lec 05 - Environment Diagrams

## None

```py
s = "Knock"
print(print(print(s, s) or print("Who's There?")), "Who?")
# Knock Knock
# Who's There
# None
# None Who?
```

- `print()` always returns `None`
- `<left>` or `<right>` -> returns `<left>` if left, else, return `<right>`

## Environment Diagrams

- When function is defined:
  - func `<name>`(`<formal parameters>`) [parent=`<label>`]
    - Ex. `f1: make_adder`
    - draw `<name>` to func val of curr frame
    - it's parent is curr frame
- order: global, f1, f2, ...
