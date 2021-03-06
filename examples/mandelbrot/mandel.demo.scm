(define maxIter 255)
(define scale (lambda (pos size minimum maximum) (+ minimum (* (/ pos size) (- maximum minimum)))))
(define iterating (lambda (i j iter) (and (< (+ (* i i) (* j j)) 4) (< iter maxIter))))
(define nextX (lambda (x0 i j) (+ x0 (- (* i i) (* j j)))))
(define nextY (lambda (y0 i j) (+ (* 2 (* i j)) y0)))
(define iterate (lambda (x0 y0 i j iter) (if (iterating i j iter) (iterate x0 y0 (nextX x0 i j) (nextY y0 i j) (+ 1 iter)) iter)))
(define iterations (lambda (x y) (iterate x y 0.0 0.0 0)))
