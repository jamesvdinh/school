(define (curry-cook formals body)
  (define (curry args)
    (if (null? args)
        body
        `(lambda (,(car args)) ,(curry (cdr args)))))
  (curry formals)
)

(define (curry-consume curry args)
  (define (consume curry args)
    (if (null? args)
        curry
        (consume (curry (car args)) (cdr args))))
  (consume curry args)
)

(define-macro (switch expr options)
  (switch-to-cond (list 'switch expr options)))

(define (switch-to-cond switch-expr)
  (cons 'cond
        (map (lambda (option)
            (cons (list 'equal? (car (cdr switch-expr)) (car option)) (cdr option)))
            (car (cdr (cdr switch-expr))))))
