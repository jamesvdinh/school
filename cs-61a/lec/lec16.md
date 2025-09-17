# Lec 16

```py
def abs(a, b):
    return sub(a, b) if b < 0 else add(a, b)
```

```scm
(define (a-plus-abs-b a b))
    ((if (< b 0) - +) a b)
```

```scm
((lambda (g y) (g (g y))) (lambda (x) (+ x 1)) 3)
# 5

(define (f g)
    (lambda (y) (g (g y))))
((f (lambda (x) (* x x))) 3)
# 81
```

ジェムズがかっこいいです
ノアーが可愛いですよね
