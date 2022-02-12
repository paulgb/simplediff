import static org.junit.Assert.*;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SimpleDiffTest {

	static String diffDataCharOld = "The quick brown fox.";
	static String diffDataCharNew = "The kuick brown fix.";
    static String[][] diffDataCharExpected = {
    		{ "=", "The " },
    		{ "-", "q" },
            { "+", "k" },
            { "=", "uick brown f" },
            { "-", "o" },
            { "+", "i" },
            { "=", "x." },
	};
	
	@Test
	public void testDiffString() {
		List< Map.Entry<SimpleDiff.DiffOp, String > > ret;
		List< Map.Entry<SimpleDiff.DiffOp, String > > expected;
		ret = SimpleDiff.diffString(diffDataCharOld, diffDataCharNew);
		
		expected = Arrays.asList( diffDataCharExpected ).stream()
			.map(ar -> new AbstractMap.SimpleEntry<SimpleDiff.DiffOp, String>(
					SimpleDiff.DiffOp.fromString(ar[0]), ar[1]) )
			.collect(Collectors.toList());
		assertEquals(expected, ret);;
		
	}
    
  static String[] diffDataWordOld1 = { "The", "quick", "brown", "fox" };
  static String[] diffDataWordNew1 = { "The", "slow", "green", "turtle" };
  static String[][] diffDataWordExpected1 = {
    	{"=", "The"},
      {"-", "quick", "brown", "fox"},
      {"+", "slow", "green", "turtle"},
  };
    
  static String[] diffDataWordOld2 = { "jumps", "over", "the", "lazy", "dog" };
  static String[] diffDataWordNew2 = { "walks", "around", "the", "orange", "cat" };
  static String[][] diffDataWordExpected2 = {
    {"-", "jumps", "over" },
    {"+", "walks", "around" },
    {"=", "the" },
    {"-", "lazy", "dog" },
    {"+", "orange", "cat" },
  };
    
	@Test
	public void testDiffWord1() {
		subDiffWord(diffDataWordOld1, diffDataWordNew1, diffDataWordExpected1 );
	}
	
	@Test
	public void testDiffWord2() {
		subDiffWord(diffDataWordOld2, diffDataWordNew2, diffDataWordExpected2 );
	}
	
	public void subDiffWord( String[] oldWord, String[] newWord, String[][] expectedWord ) {
		List< Map.Entry<SimpleDiff.DiffOp, List<String> > > ret;
		List< Map.Entry<SimpleDiff.DiffOp, List<String> > > expected;
		ret = SimpleDiff.diff(Arrays.asList(oldWord), Arrays.asList(newWord));
		
		expected = Arrays.asList( expectedWord ).stream()
			.map(ar -> new AbstractMap.SimpleEntry<SimpleDiff.DiffOp, List<String> >(
					SimpleDiff.DiffOp.fromString(ar[0]), 
						Arrays.asList(ar).subList(1, ar.length) ) )
			.collect(Collectors.toList());
		assertEquals(expected, ret);
	}

  static int ADD=0;
  static int DEL=1;
  static int EQ=2;

  static Integer[] diffDataIntOld1 = {1, 3, 4};
  static Integer[] diffDataIntNew1 = {1, 2, 3, 4};
  static Integer[][] diffDataIntExpected1 = {
    {EQ ,1 },
    {ADD, 2},
    {EQ, 3, 4},
  };

  static Integer[] diffDataIntOld2 = {1, 2, 3, 8, 9, 12, 13 };
  static Integer[] diffDataIntNew2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
  static Integer[][] diffDataIntExpected2 = {
      { EQ, 1, 2, 3 },
      { ADD, 4, 5, 6, 7 },
      { EQ, 8, 9 },
      { ADD, 10, 11 },
      { EQ, 12, 13 },
      { ADD, 14, 15 },
  };
        
  static Integer[] diffDataIntOld34 = {1, 2, 3, 4, 5 };
  static Integer[] diffDataIntNew34 = {1, 2, 2, 3, 4, 5};
  static Integer[][] diffDataIntExpected34 = {
      { EQ, 1 },
      { ADD, 2 },
      { EQ, 2, 3, 4, 5 },
  };
        
  static Integer[] diffDataIntOld4 = {1, 2, 3, 4, 5 };
  static Integer[] diffDataIntNew4 = {1, 2, 2, 3, 4, 4, 5};
  static Integer[][] diffDataIntExpected4 = {
      { EQ, 1 },
      { ADD, 2 },
      { EQ, 2, 3, 4 },
      { ADD, 4 },
      { EQ, 5 },
  };
        
  static Integer[] diffDataIntOld5 = {1, 2, 3, 4, 5 };
  static Integer[] diffDataIntNew5 = {1, 2, 1, 2, 3, 3, 2, 1, 4, 5};
  static Integer[][] diffDataIntExpected5 = {
      { ADD, 1, 2 },
      { EQ, 1, 2, 3 },
      { ADD, 3, 2, 1 },
      { EQ, 4, 5 },
  };
        
  static Integer[] diffDataIntOld6 = {1, 2, 3, 4, 5};
  static Integer[] diffDataIntNew6 = {1, 2, 5};
  static Integer[][] diffDataIntExpected6 = {
      { EQ, 1, 2 },
      { DEL, 3, 4 },
      { EQ, 5 },
  };
        
  static Integer[] diffDataIntOld7 = {1, 2, 3, 4, 5, 6, 7, 8};
  static Integer[] diffDataIntNew7 = {3, 6, 7};
  static Integer[][] diffDataIntExpected7 = {
      { DEL, 1, 2 },
      { EQ, 3 },
      { DEL, 4, 5 },
      { EQ, 6, 7 },
      { DEL, 8 },
  };
        
  static Integer[] diffDataIntOld8 = {1, 2, 3, 4, 5, 1, 2, 3, 4, 5};
  static Integer[] diffDataIntNew8 = {1, 2, 3, 4, 5};
  static Integer[][] diffDataIntExpected8 = {
     {EQ, 1, 2, 3, 4, 5},
     {DEL, 1, 2, 3, 4, 5},
  };

	@Test
	public void testDiffInt1() {
		subDiffInt(Arrays.asList(diffDataIntOld2), 
				Arrays.asList(diffDataIntNew2), 
				Arrays.asList(diffDataIntExpected2).stream()
				.map(ar -> Arrays.asList(ar)).collect(Collectors.toList()));
	}
	
	public void subDiffInt( List<Integer> oldIntList, List<Integer> newIntList, List<List<Integer>> expectedIntList ) {
		List< Map.Entry<SimpleDiff.DiffOp, List<Integer> > > ret;
		List< Map.Entry<SimpleDiff.DiffOp, List<Integer> > > expected;
		ret = SimpleDiff.diff(oldIntList, newIntList);
		
		expected = expectedIntList.stream()
			.map(li -> new AbstractMap.SimpleEntry<SimpleDiff.DiffOp, List<Integer> >(
					SimpleDiff.DiffOp.fromInt(li.get(0)), 
						li.subList(1, li.size()) ) )
			.collect(Collectors.toList());
		assertEquals(expected, ret);
	}

	@Test
	public void testListCharToString() {
		Character[] listChar = { 't', 'o', 't', 'o' };
		assertEquals( SimpleDiff.listCharToString( Arrays.asList(listChar) ), "toto" );
	}
	


}

