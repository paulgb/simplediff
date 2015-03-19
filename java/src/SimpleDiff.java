
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class SimpleDiff {
	public static void main(String[] args) {
		List< Map.Entry<DiffOp, String > > ret;
		ret = diffString("tito", "toto");
		System.out.println(ret);
	}
	
	static public enum DiffOp {
		ADD('+'), DEL('-'), EQ('=');
		
		private char c;
		
		DiffOp( char c ) {
			this.c = c;
		}
		
		static public DiffOp fromString( String s ) {
			return fromChar( s.charAt(0));
		}

		static public DiffOp fromInt( int i ) {
			switch( i ) {
			case 0:
				return ADD;
			case 1:
				return DEL;
			case 2:
				return EQ;
			}
			
			return EQ;
		}
		

		
		static public DiffOp fromChar( char c ) {
			switch( c ) {
			case '+':
				return ADD;
			case '-':
				return DEL;
			case '=':
				return EQ;
			}
			
			return EQ;
		}
		
		public String toString() {
			return String.valueOf(c);
		}
	}
	
	static public <E> List< Map.Entry<DiffOp, List<E> > > 
		diff( List<E> olds, List<E> news ) {
		//System.out.printf("diff \"%s\" vs \"%s\"%n", olds, news );
	    // Create a map from old values to their indices
	    Map< E, List<Integer> > oldIndexMap = new HashMap<>();
		for( int i=0; i<olds.size(); i++) {
			List<Integer> indexList = oldIndexMap.getOrDefault(olds.get(i), new ArrayList<Integer>());
			indexList.add(i);
			oldIndexMap.put(olds.get(i), indexList);
		}
		
		//System.out.printf( "oldIndexMap: %s%n", oldIndexMap );
		
		// Find the largest substring common to old and new.
	    // We use a dynamic programming approach here.
	    // 
	    // We iterate over each value in the `new` list, calling the
	    // index `inew`. At each iteration, `overlap[iold]` is the
	    // length of the largest suffix of `old[:iold]` equal to a suffix
	    // of `new[:inew]` (or unset when `old[iold]` != `new[inew]`).
	    //
	    // At each stage of iteration, the new `overlap` (called
	    // `_overlap` until the original `overlap` is no longer needed)
	    // is built from the old one.
	    //
	    // If the length of overlap exceeds the largest substring
	    // seen so far (`sub_length`), we update the largest substring
	    // to the overlapping strings.
		
		// `subStartOld` is the index of the beginning of the largest overlapping
		// substring in the old list. `subStartNew` is the index of the beginning
		// of the same substring in the new list. `subLength` is the length that
		// overlaps in both.
		
		// These track the largest overlapping substring seen so far, so naturally
		// we start with a 0-length substring.
		int subStartOld=0, subStartNew=0, subLength=0;
		HashMap<Integer, Integer> overlap, _overlap;
		
		overlap = new HashMap<>();
		
		for( int inew=0; inew<news.size(); inew++) {
			_overlap = new HashMap<>();
			E val=news.get(inew);
			for( Integer iold : oldIndexMap.getOrDefault(val, new ArrayList<Integer>()) ) {
				// now we are considering all values of iold such that
	            // `old[iold] == new[inew]`.
				if (iold == 0) {
					_overlap.put(iold, 1);
				} else {
					_overlap.put(iold, overlap.getOrDefault(iold-1, 0)+1 );
				}
				
				if (_overlap.get(iold) > subLength) {
			        // this is the largest substring seen so far, so store its
		            // indices
					subLength = _overlap.get(iold);
					subStartOld = iold - subLength + 1;
					subStartNew = inew - subLength + 1;
				}				
			}
			overlap = _overlap;
			//System.out.printf("inew=%d %s%n", inew, overlap);
		}
		
		List< Map.Entry<DiffOp, List<E> > > ret = new ArrayList<>();
		if (subLength==0) {
			// no common substring found
			if (olds.size() > 0) {
				ret.add( new AbstractMap.SimpleEntry<> (DiffOp.DEL, olds ) );
			}
			if (news.size() > 0) {
				ret.add( new AbstractMap.SimpleEntry<> (DiffOp.ADD, news ) );
			}
		} else {
			ret.addAll( diff( olds.subList(0, subStartOld), news.subList(0, subStartNew)) );
			ret.add( new AbstractMap.SimpleEntry<>(DiffOp.EQ, news.subList(subStartNew, subStartNew + subLength) ) );
			ret.addAll( diff( olds.subList(subStartOld+subLength, olds.size()), news.subList(subStartNew+subLength, news.size())));
		}
		//System.out.println( ret );
		return ret;
	}
	
	static String listCharToString( List<Character> l ) {
		StringBuilder builder = new StringBuilder();
		for( Character c : l ) {
			builder.append( c );
		}
		return builder.toString();
	}
	
	static public List< Map.Entry<DiffOp, String > > diffString( String olds, String news ) {
		List<Character> oldList = new ArrayList<>();
		List<Character> newList = new ArrayList<>();
		for( int i=0; i<olds.length(); i++) {
			oldList.add( olds.charAt(i));
		}
		for( int i=0; i<news.length(); i++) {
			newList.add( news.charAt(i));
		}
		
		List< Map.Entry<DiffOp, List<Character> > > retList = diff( oldList, newList );
		
		List< Map.Entry<DiffOp, String> > retString = retList.stream().map(
			entry -> new AbstractMap.SimpleEntry<>(
				entry.getKey(),
				listCharToString( entry.getValue() ) ) ).collect( Collectors.toList());
		
		return retString;
	}

}

