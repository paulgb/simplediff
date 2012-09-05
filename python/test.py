
from doctest import testmod, testfile
import simplediff

if __name__ == '__main__':
    testmod(simplediff)
    testfile('README.md')

