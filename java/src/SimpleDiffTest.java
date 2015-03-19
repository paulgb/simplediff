import static org.junit.Assert.*;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;


public class SimpleDiffTest {

	@Test
	public void testListCharToString() {
		Character[] listChar = { 't', 'o', 't', 'o' };
		assertEquals( SimpleDiff.listCharToString( Arrays.asList(listChar) ), "toto" );
	}
	
	@Test
	public void testDiffString() {
        String olds = "The quick brown fox.";
        String news = "The kuick brown fix.";
        String[][] expectedArray = {
        		{ "=", "The " },
        		{ "-", "q" },
                { "+", "k" },
                { "=", "uick brown f" },
                { "-", "o" },
                { "+", "i" },
                { "=", "x." },
		};
        
		List< Map.Entry<SimpleDiff.DiffOp, String > > ret;
		List< Map.Entry<SimpleDiff.DiffOp, String > > expected;
		ret = SimpleDiff.diffString(olds, news);
		
		expected = Arrays.asList( expectedArray ).stream()
			.map(ar -> new AbstractMap.SimpleEntry<SimpleDiff.DiffOp, String>(
					SimpleDiff.DiffOp.fromString(ar[0]), ar[1]) )
			.collect(Collectors.toList());
		assertEquals(expected, ret);;
		
	}

}

