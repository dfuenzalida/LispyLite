(define adder (lambda (a) (lambda (b) (+ a b))))
(define plus2 (adder 2))
(display (plus2 3))
