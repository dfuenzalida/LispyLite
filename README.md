## LispyLite

LispyLite is a bloated Java port of the [lis.py Scheme interpreter by Peter Norvig](http://norvig.com/lispy.html)

### Usage

You can use your favorite IDE or just compile from the command line with:

```
javac -d bin  src/lispy/*
```

Launch the REPL with:

```
java -cp bin lispy.Repl
```

#### Basic functions

```
(define adder (lambda (a) (lambda (b) (+ a b))))
(define plus2 (adder 2))
(plus2 3)
```

#### Fibonacci

```
(define fib (lambda (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2))))))
(fib 1)
(fib 2)
(fib 3)
(fib 10)
```

#### Square roots

```
;; Compute square roots using Newton's method as in SICP
;; See https://mitpress.mit.edu/sicp/full-text/sicp/book/node12.html

(define abs (lambda (x) (if (< x 0) (* -1 x) x)))

(define square (lambda (x) (* x x)))

(define good-enough? (lambda (guess x) (< (abs (- (square guess) x)) 0.001)))
  
(define average (lambda (x y) (/ (+ x y) 2)))

(define improve (lambda (guess x) (average guess (/ x guess))))
  
(define sqrt-iter (lambda (guess x) (if (good-enough? guess x) guess (sqrt-iter (improve guess x) x))))
                 
(define sqrt (lambda (x) (sqrt-iter 1.0 x)))

(sqrt 4)
(sqrt 256)
```

