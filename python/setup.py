from setuptools import setup
import simplediff

setup(
    name='simplediff',
    version=simplediff.__version__,
    description='Diff library with a dead-simple interface',
    long_description=simplediff.__doc__,
    author='Paul Butler',
    author_email='diff@paulbutler.org',
    url='https://github.com/paulgb/simplediff',
    keywords=['diff'],
    classifiers=(
        'Development Status :: 5 - Production/Stable',
        'Intended Audience :: Developers',
        'License :: OSI Approved :: zlib/libpng License',
        'Programming Language :: Python :: 3.8',
        'Programming Language :: Python :: 3.9',
        'Programming Language :: Python :: 3.10',
        'Topic :: Software Development :: Libraries',
        'Topic :: Software Development :: Libraries :: Python Modules',
        'Topic :: Software Development :: Version Control',
        'Topic :: Text Processing',
    ),
    packages=['simplediff'],
    include_package_data=True,
    zip_safe=True,
)

