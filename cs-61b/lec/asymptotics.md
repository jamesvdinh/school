# Asymptotics

## Notation

### Big-O -> Upper Bound

tells us that a function grow **at most** as fast as $f(N)$, could be less

- gives a **worst-case** guarantee

Ex. if an algorithm is $O(N^2)$, it never runs _worse_ than some multiple of $N^2$, but it could be faster

### Omega -> Lower Bound

tells us that afunction grow **at least** as fast as $f(N)$, could be greater

- gives a **best-case** possible runtime

Ex. if an algorithm is $O(N^2)$, it never runs _faster_ than some multiple of $N^2$, but it could be worse

### Theta -> Tight Bound

tells us the function grows at exactly the same rate as $f(N)$

- gives an **exact** asymptotic behavior
- $\large \Omega(N^2) <= \theta(N^2) <= O(N^2)$

Ex. if an algorithm is $O(N^2)$, it _always_ behaves like $N^2$

## Amortized Runtime

- gets the _AVERAGE_ runtime of a function
  - if we use a function over many ops, guaranteed to get better _AVERAGE_ performance
- "shave" down the peaks of a really large function
- benefit: gives a better estimate of how much time something takes to run

Ex. Nested for loop

```java
public static void f1(int N) {
  for (int i = 0; i < N; i += 1) {
    for (int j = i; j < N; j += 1) {
      if (j % 2 == 0) {
        i += 1;
      }
      System.out.println("naming things is hard - Dylan");
    }
  }
}
```

|  i   |      j       | work |
| :--: | :----------: | :--: |
|  0   |  0 ... N-1   |  N   |
| N/2  | N/2 ... N-1  | N/2  |
| 3N/4 | 3N/4 ... N-1 | N/4  |
| 7N/8 | 7N/8 ... N-1 | N/8  |
| ...  |     ...      | ...  |

Work: $N(1 + \dfrac12 + \dfrac14 + ...) => N$

Best case: $\theta(N)$  
Worst case: $\theta(N)$

> The dominating factor in a geometric sequence is the **asymptotic behavior**

Ex.
dominating factor: $\sqrt{N}$

$\sqrt{N}(1 + \sqrt2 + \sqrt4 + ... + \sqrt{N})$

$= \sqrt{N}(\sqrt{N}) = \theta(N)$
