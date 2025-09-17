## Intro to Java

### Types

- must be declared before use
- cannot assign different type value to a variable

## Examples

### Ex 1

- Python

```py
def larger(x, y):
    if x > y:
        return x
    return y
```

- Java

```java
public class LargerDemo {
    public static int larger(int x, int y) {
        if (x > y) {
            return x;
        }
        return y;
    }

    public static void main(String[] args) {
        System.out.println(larger(3, 5));
    }
}
```
