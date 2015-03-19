import static org.junit.Assert.*;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;


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
	


	@Test
	public void testListCharToString() {
		Character[] listChar = { 't', 'o', 't', 'o' };
		assertEquals( SimpleDiff.listCharToString( Arrays.asList(listChar) ), "toto" );
	}
	


}

