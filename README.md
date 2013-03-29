**simplediff** is an implementation of a diff function similar to Ratcliff/Metzener<sup>1</sup>

It's not the fastest diff algorithm, but it's simple enough to understand in an afternoon. That said, it's been in use for five years and I've never heard a complaint about performance. There are now four implementations (Python, CoffeeScript, JavaScript, and PHP), all mostly sharing the same interface.

See the Python code for the reference implementation.

The Python version is available through pip:

    pip install simplediff

The Javascript version is available through npm:

    npm install simplediff

The PHP and Python code was written by me (Paul Butler) in 2007 and 2008 respectively.

The CoffeeScript port was contributed by vjeux (2012).

The JavaScript port was contributed by DJ Mountney (2012).

Python unit tests contributed by Felipe (fmartingr), 2013.

PHP fix contributed by multiwebinc, 2013.

For more details, see the [Python readme](simplediff/tree/master/python)

1. [Pattern Matching: The Gestalt Approach](http://collaboration.cmc.ec.gc.ca/science/rpn/biblio/ddj/Website/articles/DDJ/1988/8807/8807c/8807c.htm)
