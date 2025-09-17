# 3 - Memory

## new declaration

- when calling new <instance>, `new` returns the address in memory

```java
Walrus a;
a = new Wlarus(1000, 8.3);
Walrus b;
b = a
// a, b both point to same Walrus object
```

## Arrays.equals vs ==

```java
int[] x = new []{1, 2, 3};
int[] y = new []{1, 2, 3};
System.out.println(x == y); // false
Arrays.equals(x, y); // true

int[] z = x;
System.out.println(x == z) // true
```
