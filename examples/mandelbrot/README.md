# Mandelbrot example

This example shows how to iteroperate with JavaScript code compiled from an `scm` file.

## Usage

Compile the LispyLite classes with

```
$ javac -d bin  src/lispy/*
```

Use the compiler to generate JavaScript from the example file:

```
$ java -cp bin lispy.Compiler examples/mandelbrot/mandel.demo.scm
```

The compiled file will be generated in your current folder, you'll need to move it to the examples folder:

```
$ mv mandel.demo.js examples/mandelbrot
```

And now open the file `examples/mandelbrot/index.html` in your browser. It will take a few seconds to render. If the browser tells you the tab is unresponsive, give it a few seconds, since the generated code is far from being optimized :-)

If you inspect the code of `index.html`, you'll notice that it's just a loop over each pixel of the canvas element, and the functions to compute the color and scale are generated out of the `scm` file in the `mandel.demo` namespace.

### License

MIT License

Copyright (c) 2017 Denis Fuenzalida
