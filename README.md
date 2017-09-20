# LispyLite

LispyLite is a toy Scheme interpreter and compiler to JavaScript, based on the [lis.py Scheme interpreter by Peter Norvig](http://norvig.com/lispy.html)

## Usage

You can use your favorite IDE to add the source files to a project, or just compile them from the command line with:

```
javac -d bin  src/lispy/*
```

### Interpreter

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

### Compiler

The compiler will try to output a JavaScript file with the core functions and your program in its own JavaScript object (as a namespace) so it can be used in other projects.

The namespace is inferred from the name of the file being compiled. For instance, compiling the file`examples/roots.scm` results in the namespace `roots` in an output file called `roots.js` in the current directory:

```
$ cat examples/roots.scm
(define abs (lambda (x) (if (< x 0) (* -1 x) x)))
(define square (lambda (x) (* x x)))
(define good-enough? (lambda (guess x) (< (abs (- (square guess) x)) 0.001)))
(define average (lambda (x y) (/ (+ x y) 2)))
(define improve (lambda (guess x) (average guess (/ x guess))))
(define sqrt-iter (lambda (guess x) (if (good-enough? guess x) guess (sqrt-iter (improve guess x) x))))
(define sqrt (lambda (x) (sqrt-iter 1.0 x)))
(display (sqrt 256))

$ java -cp bin lispy.Compiler examples/roots.scm
Output: roots.js

$ node roots.js
16.00000352670594

```

### License

MIT License

Copyright (c) 2017 Denis Fuenzalida
