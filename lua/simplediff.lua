local M={}

function M.table_join( t1, t2, t3 )
    -- return a table containing all elements of t1 then t2 then t3
    local t = {}
    for i,v in ipairs(t1) do
        table.insert(t,v)
    end
    for i,v in ipairs(t2) do
        table.insert(t,v)
    end
    if t3 then
        for i,v in ipairs(t3) do
            table.insert(t,v)
        end
    end
    return t
end

function M.table_subtable( t, start, stop ) 
    -- 0 is first element, stop is last element
    local ret = {}
    if stop == nil then
        stop = #t
    end
    if start < 0 or stop < 0 or start > stop then
        error('Invalid values: '..start..' '..stop )
    end
    for i,v in ipairs(t) do
        if (i-1) >= start and (i-1) < stop then
            table.insert( ret, v )
        end
    end
    return ret
end

function M.diff(old, new)
    -- print('diff('..mytostring(old)..', '..mytostring(new)..')' )
    --[[
    Find the differences between two lists. Returns a list of pairs, where the
    first value is in ['+','-','='] and represents an insertion, deletion, or
    no change for that list. The second value of the pair is the list
    of elements.

    Params:
        old     the old list of immutable, comparable values (ie. a list
                of strings)
        new     the new list of immutable, comparable values
   
    Returns:
        A list of pairs, with the first part of the pair being one of three
        strings ('-', '+', '=') and the second part being a list of values from
        the original old and/or new lists. The first part of the pair
        corresponds to whether the list of values is a deletion, insertion, or
        unchanged, respectively.

    Examples:
        >>> diff([1,2,3,4],[1,3,4])
        { {'=', {1} }, {'-', {2} }, {'=', {3, 4}}}

        >>> diff{{1,2,3,4},{2,3,4,1}}
        {{'-', {1}}, {'=', {2, 3, 4}}, {'+', {1}}}

        >>> diff('The quick brown fox jumps over the lazy dog'.split(),
        ...      'The slow blue cheese drips over the lazy carrot'.split())
        { {'=', {'The'} },
          {'-', {'quick', 'brown', 'fox', 'jumps'} },
          {'+', {'slow', 'blue', 'cheese', 'drips'} },
          {'=', {'over', 'the', 'lazy'} },
          {'-', {'dog'} },
          {'+', {'carrot'} }
        }

    ]]

    -- Create a map from old values to their indices
    local old_index_map = {}
    for i, val in ipairs(old) do
        if not old_index_map[val] then
            old_index_map[val] = {}
        end
        table.insert( old_index_map[val], i-1 )
    end

    --[[
    Find the largest substring common to old and new.
    We use a dynamic programming approach here.
    
    We iterate over each value in the `new` list, calling the
    index `inew`. At each iteration, `overlap[i]` is the
    length of the largest suffix of `old[:i]` equal to a suffix
    of `new[:inew]` (or unset when `old[i]` != `new[inew]`).
   
    At each stage of iteration, the new `overlap` (called
    `_overlap` until the original `overlap` is no longer needed)
    is built from the old one.
   
    If the length of overlap exceeds the largest substring
    seen so far (`sub_length`), we update the largest substring
    to the overlapping strings.

    --[[
    `sub_start_old` is the index of the beginning of the largest overlapping
    substring in the old list. `sub_start_new` is the index of the beginning
    of the same substring in the new list. `sub_length` is the length that
    overlaps in both.
    These track the largest overlapping substring seen so far, so naturally
    we start with a 0-length substring.
    ]]
    local overlap = {}
    local sub_start_old = 0
    local sub_start_new = 0
    local sub_length = 0

    for inewInc, val in ipairs(new) do
        local inew = inewInc-1
        local _overlap = {}
        if old_index_map[val] then
            for _,iold in ipairs(old_index_map[val]) do
                -- now we are considering all values of iold such that
                -- `old[iold] == new[inew]`.
                if iold <= 0 then
                    _overlap[iold] = 1
                else
                    _overlap[iold] = (overlap[iold - 1] or 0) + 1
                end
                if (_overlap[iold] > sub_length) then
                    sub_length = _overlap[iold]
                    sub_start_old = iold - sub_length + 1
                    sub_start_new = inew - sub_length + 1
                end
            end
        end
        overlap = _overlap
    end

    if sub_length == 0 then
        -- If no common substring is found, we return an insert and delete...
        local oldRet = {}
        local newRet = {}

        if #old > 0 then
            oldRet = { {'-', old} }
        end
        if #new > 0 then
            newRet = { {'+', new} }
        end

        return M.table_join( oldRet, newRet )
    else
        -- ...otherwise, the common substring is unchanged and we recursively
        -- diff the text before and after that substring
        return M.table_join(
            M.diff(M.table_subtable( old, 0, sub_start_old), 
                   M.table_subtable( new, 0, sub_start_new)),
            { {'=', M.table_subtable(new,sub_start_new,sub_start_new + sub_length)} },
            M.diff(M.table_subtable( old, sub_start_old + sub_length ),
                   M.table_subtable( new, sub_start_new + sub_length ) )
       )
    end
end

return M


