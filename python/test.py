from doctest import testmod, testfile
import unittest
import simplediff

TESTS = {
    'insert': [
        {
            "old": [1, 3, 4],
            "new": [1, 2, 3, 4],
            "diff": [("=", [1]),
                     ("+", [2]),
                     ("=", [3, 4])]
        },
        {
            "old": [1, 2, 3, 8, 9, 12, 13],
            "new": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15],
            "diff": [("=", [1, 2, 3]),
                     ("+", [4, 5, 6, 7]),
                     ("=", [8, 9]),
                     ("+", [10, 11]),
                     ("=", [12, 13]),
                     ("+", [14, 15])]
        },
        {
            "old": [1, 2, 3, 4, 5],
            "new": [1, 2, 2, 3, 4, 5],
            "diff": [("=", [1]),
                     ("+", [2]),
                     ("=", [2, 3, 4, 5])]
        },
        {
            "old": [1, 2, 3, 4, 5],
            "new": [1, 2, 2, 3, 4, 4, 5],
            "diff": [("=", [1]),
                     ("+", [2]),
                     ("=", [2, 3, 4]),
                     ("+", [4]),
                     ("=", [5])]
        },
        {
            "old": [1, 2, 3, 4, 5],
            "new": [1, 2, 1, 2, 3, 3, 2, 1, 4, 5],
            "diff": [("+", [1, 2]),
                     ("=", [1, 2, 3]),
                     ("+", [3, 2, 1]),
                     ("=", [4, 5])]
        }
    ],
    'delete': [
        {
            "old": [1, 2, 3, 4, 5],
            "new": [1, 2, 5],
            "diff": [("=", [1, 2]),
                     ("-", [3, 4]),
                     ("=", [5])]
        },
        {
            "old": [1, 2, 3, 4, 5, 6, 7, 8],
            "new": [3, 6, 7],
            "diff": [("-", [1, 2]),
                     ("=", [3]),
                     ("-", [4, 5]),
                     ("=", [6, 7]),
                     ("-", [8])]
        },
        {
            "old": [1, 2, 3, 4, 5, 1, 2, 3, 4, 5],
            "new": [1, 2, 3, 4, 5],
            "diff": [("=", [1, 2, 3, 4, 5]),
                     ("-", [1, 2, 3, 4, 5])]
        }
    ],
    'words': [
        {
            "old": ["The", "quick", "brown", "fox"],
            "new": ["The", "slow", "green", "turtle"],
            "diff": [("=", ["The"]),
                     ("-", ["quick", "brown", "fox"]),
                     ("+", ["slow", "green", "turtle"])]
        },
        {
            "old": ["jumps", "over", "the", "lazy", "dog"],
            "new": ["walks", "around", "the", "orange", "cat"],
            "diff": [("-", ["jumps", "over"]),
                     ("+", ["walks", "around"]),
                     ("=", ["the"]),
                     ("-", ["lazy", "dog"]),
                     ("+", ["orange", "cat"])]
        }
    ],
    'character': [
        {
            "old": "The quick brown fox.",
            "new": "The kuick brown fix.",
            "diff": [("=", "The "),
                     ("-", "q"),
                     ("+", "k"),
                     ("=", "uick brown f"),
                     ("-", "o"),
                     ("+", "i"),
                     ("=", "x.")]
        }
    ]
}

class DiffTests(unittest.TestCase):
    def test_delete_diff(self):
        strings = TESTS['delete']
        for check in strings:
            self.assertEqual(simplediff.diff(check['old'], check['new']),
                             check['diff'])

    def test_insert_diff(self):
        strings = TESTS['insert']
        for check in strings:
            self.assertEqual(simplediff.diff(check['old'], check['new']),
                             check['diff'])

    def test_words_diff(self):
        strings = TESTS['words']
        for check in strings:
            self.assertEqual(simplediff.diff(check['old'], check['new']),
                             check['diff'])

    def test_character_diff(self):
        strings = TESTS['character']

        for check in strings:
            self.assertEqual(simplediff.diff(check['old'], check['new']),
                             check['diff'])

    def test_doctests(self):
        testmod(simplediff)
        testfile('README.md')

if __name__ == '__main__':
    unittest.main()

