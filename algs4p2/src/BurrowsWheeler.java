import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class BurrowsWheeler {
	private static final int R = 8;

	/**
	 * apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
	 */
	public static void encode() {
		// read the input
		String input = BinaryStdIn.readString();
		CircularSuffixArray suffixArray = new CircularSuffixArray(input);

		int n = suffixArray.length();

		// find original position
		for (int i = 0; i < n; i++) {
			if (suffixArray.index(i) == 0) {
				BinaryStdOut.write(i);
				break;
			}
		}

		// the write the suffix
		for (int i = 0; i < n; i++) {
			int position = (suffixArray.index(i) + n - 1) % n;
			if (position < 0) {
				position += n;
			}
			BinaryStdOut.write(input.charAt(position), R);
		}

		BinaryStdOut.close();
	}

	/**
	 * apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
	 */
	public static void decode() {
		int first = BinaryStdIn.readInt();
		String s = BinaryStdIn.readString();
		char[] t = s.toCharArray();

		// map list of positions for each characters 
		Map<Character, Deque<Integer>> positions = new HashMap<Character, Deque<Integer>>();
		for (int i = 0; i < t.length; i++ ){
			Deque<Integer> li = positions.get(t[i]);
			if (li == null) {
				li = new LinkedList<Integer>();
				positions.put(t[i], li);
			}
			li.add(i);
		}
		
		// sort last word
		Arrays.sort(t);
		
		// construct the next[] array
		int[] next = new int[t.length];
		for (int i = 0; i < t.length; i++) {
			Deque<Integer> li = positions.get(t[i]);
			next[i] = li.removeFirst();
		}
		
		// reconstruct string
		int cur = first;
		for (int i = 0; i < t.length; i++) {
			char c = t[cur];
			BinaryStdOut.write(c);
			cur = next[cur];
		}

		BinaryStdOut.close();
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");

	}

}
