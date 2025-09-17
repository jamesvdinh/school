# Lec 17

- `cons` creates a list pair
  - `car` (first) and `cdr` (rest) returns the elements of `cons`

```scm
(let x (cons 1 2))
x
; 1 . 2)
(car x)
; 1
(cdr x)
; 2
```

- `list` creates a list with mulitple pairs

  > Note: `cons` is used to add elements to the start of a list

```scm
(cons 1
    (cons 2
        (cons 3 nil)))
; (1 2 3)
(define one-thru-three (list 1 2 3))
(one-thru-three)
; (1 2 3)
(car one-thru-three)
; 1
(cdr one-thru-three)
; (2, 3, 4)
(cons 10 one-thru-three)
; (10 1 2 3)
```

- append combines 2 lists and returns that new list
  - must be lists
- `'` is needed to access list literals

```scm
(append (list 1 2) (list 3 4))
; (1 2 3 4)
(define a 1)
(define b 2)
(list 'a b)
; (a 2)
```
