# Simple Diff for Python v0.1
# (C) Paul Butler 2008 <http://www.paulbutler.org/>
diff = (before, after) ->
    # Find the differences between two lists. Returns a list of pairs, where the first value
    # is in ['+','-','='] and represents an insertion, deletion, or no change for that list.
    # The second value of the pair is the element.

    # Build a hash map with elements from before as keys, and
    # a list of indexes as values
    ohash = {}
    for val, i in before
        if val not of ohash
            ohash[val] = []
        ohash[val].push i

    # Find the largest substring common to before and after
    lastRow = (0 for i in [0 ... before.length])
    subStartBefore = subStartAfter = subLength = 0
    for val, j in after
        thisRow = (0 for i in [0 ... before.length])
        for k in ohash[val] ? []
            thisRow[k] = (if k and lastRow[k - 1] then 1 else 0) + 1
            if thisRow[k] > subLength
                subLength = thisRow[k]
                subStartBefore = k - subLength + 1
                subStartAfter = j - subLength + 1
        lastRow = thisRow

    # If no common substring is found, assume that an insert and
    # delete has taken place
    if subLength == 0
        [].concat(
            (if before.length then [['-', before]] else []),
            (if after.length then [['+', after]] else []),
        )

    # Otherwise, the common substring is considered to have no change, and we recurse
    # on the text before and after the substring
    else
        [].concat(
            diff(before[...subStartBefore], after[...subStartAfter]),
            [['=', after[subStartAfter...subStartAfter + subLength]]],
            diff(before[subStartBefore + subLength...], after[subStartAfter + subLength...])
        )

# The below functions are intended for simple tests and experimentation; you will want to write more sophisticated wrapper functions for real use

stringDiff = (before, after) ->
    # Returns the difference between the before and after strings when split on whitespace. Considers punctuation a part of the word
    diff(before.split(/[ ]+/), after.split(/[ ]+/))

htmlDiff = (before, after) ->
    # Returns the difference between two strings (as in stringDiff) in HTML format.
    con =
        '=': ((x) -> x),
        '+': ((x) -> '<ins>' + x + '</ins>'),
        '-': ((x) -> '<del>' + x + '</del>')
    ((con[a])(b.join ' ') for [a, b] in stringDiff(before, after)).join ' '

#Examples:
#console.log htmlDiff('The world is a tragedy to those who feel, but a comedy to those who think',
#  'Life is a tragedy for those who feel, and a comedy to those who think') # Horace Walpole

#console.log htmlDiff('I have often regretted my speech, never my silence',
#  'I have regretted my speech often, my silence never') # Xenocrates
