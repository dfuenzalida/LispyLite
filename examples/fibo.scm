(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))
(display (fib 1))
(display (fib 2))
(display (fib 3))
(display (fib 10))
