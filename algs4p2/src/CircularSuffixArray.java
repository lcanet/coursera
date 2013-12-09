import java.util.Arrays;
import java.util.Comparator;


/**
 * 
 * @author lc
 *
 */
public class CircularSuffixArray {
	
	private final String input;
	private final int[] index;
	
	/**
	 * circular suffix array of s
	 * @param s
	 */
	public CircularSuffixArray(String s)  {
		if (s == null) {
			throw new IllegalArgumentException("Input cannot be null");
		}
		this.input = s;
		final int n = input.length();
		
		// generate all pseudo suffixes
		Integer[] suffixes = new Integer[n];
		for (int i = 0; i < n; i++) {
			suffixes[i] = i;
		}
		
		Arrays.sort(suffixes, new Comparator<Integer>() {
			@Override
			public int compare(Integer s1, Integer s2) {
				for (int i = 0; i < n; i++) {
					char c1 = input.charAt((s1 + i) % n);
					char c2 = input.charAt((s2 + i) % n);
					if (c1 < c2) {
						return -1;
					} else if (c1 > c2) {
						return 1;
					}
				}
				// the longest string is the most
				return (int) Math.signum(s2 - s1);
			}
		});
		
		// recopy
		this.index = new int[n];
		for (int i = 0; i < n; i++) {
			index[i] = suffixes[i];
		}
	}
	
	/**
	 * length of s
	 * @return length of s
	 */
  public int length() {
  	return input.length();
  }
  
  /**
   * returns index of ith sorted suffix
   * @param i
   * @return returns index of ith sorted suffix
   */
  public int index(int i) {
  	if (i < 0 || i > length()) {
  		throw new IllegalArgumentException();
  	}
  	return index[i];
  }
	
}
