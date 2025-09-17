# Lec 02 - Functions

## Important info

- lab 01 released
- hw 1 due week after next

## Functions

- Ex.

  ```py
    x = 2
    y = x + 1
    y

    # Output: 3
  ```

- built in functions

  - pow

    ```py
    pow(2, 10)

    # Output: 1024
    ```

  - max

    ```py
    max = max.__builtins__.max
    max

    # Output: <built-in function max>
    ```

## Environment Diagrams

- User-defined functions steps

  1. call local frame -> form new env
  2. bind params -> args
  3. execute body in new env

  ```py
  from operator import mul
  def square(x): # local frame
      return mul(x, x)
  square(-2)

  # Output: 4
  ```

- Looking up names in env context

  - **environment**: sequence of frames
  - **name**: evaluates to value bound to name of earliest frame
  - context
    - global frame: main program
    - local frame: in a function
  - Ex.

    ```py
    def jedi(jedi):
      return jedi

    jedi = jedi(5)

    # Output: 5
    ```

- Multiple Assignment

  - Ex:

    ```py
    a = 1
    b = 2
    b, a = a + b, b

    # Debugger
    # a = 2
    # b = 3
    ```

    - evaluates the values of assignment from previous vals
    - calculates vals async per line

  - Practice:

    ```py
    def diff(x, y):
      x, y = y, x
      return y - x

    x, y = 6, 1
    x, y = y, x - y
    print(diff(y, x))

    # Output: 4
    ```

    - frame priority: local > global

## Print & None

```py
def noisy(x):
  print("NOISY", x)
  return x + 1

noisy(noisy(2) + noisy(3))

# Output:
# NOISY 2
# NOISY 3
# NOISY 7
# 8
```
