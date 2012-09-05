
SimpleDiff 1.0
==============

A Python module to annotate two versions of a list with the
values that have been changed between the versions, similar
to unix's `diff` but with a dead-simple Python interface.

Install
-------

SimpleDiff can be installed from `pip` or `easy_install`

    $ pip install simplediff

Test
----

You can use `test.py` to run the included doctests

    $ python test.py

No output means that all the tests have passed.

Use
---

The module exposes three functions, `diff`, `string_diff`,
and `html_diff`. The `diff` function works on lists, which
could represent lines of a file, tokens from a tokenizer,
integers, or just about anything else.

`string_diff` and `html_diff` take strings, tokenize them
on whitespace, and return the `diff` output or HTML code
respectively. These are meant as an example implementation,
they're probably not what you want in practice.

License
-------

SimpleDiff is copyright 2008-2012 by Paul Butler. It may
be used and redistributed under a liberal zlib/libpng-like
license provided in the LICENSE file.

