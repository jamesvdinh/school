# Lec 18

```scm
;quasi quotes
(define n 2)
'(define d (lamda n `(+ d ,n)))
d
; (lambda n (+ d 2))
```

```scm
(define (repeated-expr n expr)
    (if (zero? n) nil (cons expr (repeated-expr (- n 1) expr))

(define-macro (repeat n expr)
    (cons 'begin expr (repeated-expr (eval n) expr)))
```
