simplediff = require('simplediff')

function mytostring(v, level)
    level = level or 1
    if v == nil then
        return 'nil'
    end
    if type(v) == 'table' then
        local t = {}
        -- table.insert(t, string.rep('    ', level) )
        table.insert(t, '{ ' )
        for i,x in pairs(v) do
            table.insert( t, i..'='..mytostring(x,level+1)..', ' )
        end
        table.insert( t, '}')
        return table.concat( t )
    else
        return tostring(v)
    end
end

function test_table_join()
	assert_equals( simplediff.table_join( {'a', 'b', 'c' }, {1,2,3}, {'x','y','z'} ) , { 'a', 'b', 'c', 1, 2, 3, 'x', 'y', 'z' } )
	assert_equals( simplediff.table_join( {'a', 'b', 'c' }, {1,2,3} ) , { 'a', 'b', 'c', 1, 2, 3 } )
end

function test_table_subtable()
	assert_equals( simplediff.table_subtable({ 1,2,3,4},0,2), {1,2} )	
	assert_equals( simplediff.table_subtable({ 1,2,3,4},1,1), {} )	
	assert_equals( simplediff.table_subtable({ 1,2,3,4},0,0), {} )	
	assert_equals( simplediff.table_subtable({ 1,2,3,4},2), {3,4} )	
end

local TEST_DATA = {
    phil= {
        {
            old= {'t', 'i', 't', 'o'},
            new= {'t', 'o', 't', 'o'},
            diff= {  {"-", {'t', 'i'}},
                     {"=", {'t', 'o'}},
                     {"+", {'t', 'o'}},
                 }
        },
    },
    insert= {
        {
            old= {1, 3, 4},
            new= {1, 2, 3, 4},
            diff= {{"=", {1}},
                     {"+", {2}},
                     {"=", {3, 4}}}
        },
        {
            old= {1, 2, 3, 8, 9, 12, 13},
            new= {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            diff= {{"=", {1, 2, 3}},
                     {"+", {4, 5, 6, 7}},
                     {"=", {8, 9}},
                     {"+", {10, 11}},
                     {"=", {12, 13}},
                     {"+", {14, 15}}}
        },
        {
            old= {1, 2, 3, 4, 5},
            new= {1, 2, 2, 3, 4, 5},
            diff= {{"=", {1}},
                     {"+", {2}},
                     {"=", {2, 3, 4, 5}}}
        },
        {
            old= {1, 2, 3, 4, 5},
            new= {1, 2, 2, 3, 4, 4, 5},
            diff= {{"=", {1}},
                     {"+", {2}},
                     {"=", {2, 3, 4}},
                     {"+", {4}},
                     {"=", {5}}}
        },
        {
            old= {1, 2, 3, 4, 5},
            new= {1, 2, 1, 2, 3, 3, 2, 1, 4, 5},
            diff= {{"+", {1, 2}},
                     {"=", {1, 2, 3}},
                     {"+", {3, 2, 1}},
                     {"=", {4, 5}}}
        }
    },
    delete= {
        {
            old= {1, 2, 3, 4, 5},
            new= {1, 2, 5},
            diff= {{"=", {1, 2}},
                     {"-", {3, 4}},
                     {"=", {5}}}
        },
        {
            old= {1, 2, 3, 4, 5, 6, 7, 8},
            new= {3, 6, 7},
            diff= {{"-", {1, 2}},
                     {"=", {3}},
                     {"-", {4, 5}},
                     {"=", {6, 7}},
                     {"-", {8}}}
        },
        {
            old= {1, 2, 3, 4, 5, 1, 2, 3, 4, 5},
            new= {1, 2, 3, 4, 5},
            diff= {{"=", {1, 2, 3, 4, 5}},
                     {"-", {1, 2, 3, 4, 5}}}
        }
    },
    words= {
        {
            old= {"The", "quick", "brown", "fox"},
            new= {"The", "slow", "green", "turtle"},
            diff= {{"=", {"The"}},
                     {"-", {"quick", "brown", "fox"}},
                     {"+", {"slow", "green", "turtle"}}}
        },
        {
            old= {"jumps", "over", "the", "lazy", "dog"},
            new= {"walks", "around", "the", "orange", "cat"},
            diff= {{"-", {"jumps", "over"}},
                     {"+", {"walks", "around"}},
                     {"=", {"the"}},
                     {"-", {"lazy", "dog"}},
                     {"+", {"orange", "cat"}}}
        }
    },
    character= {
        {
            old= "The quick brown fox.",
            new= "The kuick brown fix.",
            diff= {{"=", "The "},
                     {"-", "q"},
                     {"+", "k"},
                     {"=", "uick brown f"},
                     {"-", "o"},
                     {"+", "i"},
                     {"=", "x."}}
        }
    }
}

function test_data()
	for testname, testcontent in pairs(TEST_DATA) do
		-- print(testname)
		for i, testcase in ipairs(testcontent) do
			-- print('\t'..testname..i)
			-- print(mytostring(testcase) )
			local old=testcase.old
			local new=testcase.new
			local expected=testcase.diff
			local result
			if type(old) == 'string' then
				-- result = simplediff.diffString( old, new )
			else
				result = simplediff.diff( old, new )
				for i,item in ipairs(expected) do
					assert_equals(item, result[i], 'error in '..testname..i )
				end
				assert_equals( #expected, #result, 'error in '..testname..i )
			end
		end
	end
end

function is_equal( v1, v2)
	if type(v1) ~= type(v2) then return false end
	if type(v1) ~= 'table' then return (v1 == v2) end
	-- v1 and v2 are tables
	for k,v in pairs(v1) do
		if not is_equal(v1[k], v2[k]) then return false end
	end
	for k,v in pairs(v2) do
		if not v1[k] then return false end
	end
	return true
end

local nb_asserts = 0

function assert_equals( v1, v2, msg )
	nb_asserts = nb_asserts + 1
	local result = true
	if type(v1) == 'table' then
		result = is_equal( v1, v2 )
		if not result then
			if msg then print(msg) end
			print('expected: '..mytostring(v1) )
			print('got     : '..mytostring(v2) )
			assert( false )
		end
	else
		result = (v1 == v2)
		if not result then
			if msg then print(msg) end
			print('expected: '..mytostring(v1) )
			print('got     : '..mytostring(v2) )
		end
		assert( v1 == v2 )
	end
end

function main()
	test_table_join()
	test_table_subtable()
	test_data()
	print("OK! Ran "..nb_asserts..' tests')
end

main()