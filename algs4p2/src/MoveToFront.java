import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author lc
 */
public class MoveToFront {

	private static final int R = 8;
	private static final int SIZE = 1 << R;
	
	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		// use linked list for fast reordering
		LinkedList<Integer> seq = new LinkedList<Integer>();
		for (int i = 0; i < SIZE; i++) {
			seq.add(i);
		}
    // read the input
    String s = BinaryStdIn.readString();
    char[] input = s.toCharArray();

    for (char c : input) {
    	int index = 0;
    	for (Iterator<Integer> it = seq.iterator(); it.hasNext(); index++) {
    		Integer v = it.next();
    		if (v == c) {
    			it.remove();
    			break;
    		}
    	}
  		seq.addFirst(Integer.valueOf(c));
			BinaryStdOut.write(index, 8);
    }
    BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
		LinkedList<Integer> seq = new LinkedList<Integer>();
		for (int i = 0; i < SIZE; i++) {
			seq.add(i);
		}
		while (!BinaryStdIn.isEmpty()) {
			int r = BinaryStdIn.readInt(R);
			int  c = seq.remove(r);
			BinaryStdOut.write(c, R);
			seq.addFirst(c);
		}
		
    BinaryStdOut.close();
	}
	
	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");

	}
}
