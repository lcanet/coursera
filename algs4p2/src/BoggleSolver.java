import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Boggle game solver alg4-pt2 hw
 * 
 * @author lc
 */
public class BoggleSolver {

	private final BoggleDictionary dictionaryTrie;

	private class DictionaryNode {
		private char c; // character
		private DictionaryNode left, mid, right; // left, middle, and right subtries
		private String val; // value associated with string
	}

	private class BoggleDictionary {
		private DictionaryNode root;

		public void add(String s) {
			root = put(root, s, s, 0);
		}

		private DictionaryNode put(DictionaryNode x, String s, String val, int d) {
			char c = s.charAt(d);
			if (x == null) {
				x = new DictionaryNode();
				x.c = c;
			}
			if (c < x.c)
				x.left = put(x.left, s, val, d);
			else if (c > x.c)
				x.right = put(x.right, s, val, d);
			else if (d < s.length() - 1)
				x.mid = put(x.mid, s, val, d + 1);
			else
				x.val = val;
			return x;
		}

		private boolean contains(String key) {
			return get(key) != null;
		}

		private String get(String key) {
			if (key == null)
				throw new NullPointerException();
			if (key.length() == 0)
				throw new IllegalArgumentException("key must have length >= 1");
			DictionaryNode x = get(root, key, 0);
			if (x == null)
				return null;
			return x.val;
		}

		// return subtrie corresponding to given key
		private DictionaryNode get(DictionaryNode x, String key, int d) {
			if (x == null)
				return null;
			char c = key.charAt(d);
			if (c < x.c)
				return get(x.left, key, d);
			else if (c > x.c)
				return get(x.right, key, d);
			else if (d < key.length() - 1)
				return get(x.mid, key, d + 1);
			else
				return x;
		}
	}

	/**
	 * Initializes the data structure using the given array of strings as the dictionary. (You can assume each word in the
	 * dictionary contains only the uppercase letters A through Z.)
	 * 
	 * @param dictionary
	 */
	public BoggleSolver(String[] dictionary) {
		if (dictionary == null) {
			throw new IllegalArgumentException("Dictionnary is mandatory");
		}
		dictionaryTrie = new BoggleDictionary();
		for (String word : dictionary) {
			dictionaryTrie.add(word);
		}
	}

	/**
	 * Returns all valid words in the given Boggle board, as an Iterable.
	 * 
	 * @param board
	 * @return
	 */
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		if (board == null) {
			throw new IllegalArgumentException("Board is mandatory");
		}

		List<String> results = new ArrayList<String>();
		for (int i = 0; i < board.rows(); i++) {
			for (int j = 0; j < board.cols(); j++) {
				String prefix = getSuffix(board, i, j);
				DictionaryNode node = dictionaryTrie.get(dictionaryTrie.root, prefix, 0);
				if (node != null) {
					exploreBoard(results, board, i, j, new VisitedPositions(board), node);
				}
			}
		}

		// extract uniques words and sort
		List<String> sortedResults = new ArrayList<String>(new HashSet<String>(results));
		Collections.sort(sortedResults);

		return sortedResults;
	}

	private String getSuffix(BoggleBoard board, int i, int j) {
		char letter = board.getLetter(i, j);
		if (letter == 'Q') {
			return "QU";
		} else {
			return String.valueOf(letter);
		}
	}

	private void exploreBoard(List<String> words, BoggleBoard board, int x, int y,
			VisitedPositions visitedPositions, DictionaryNode node) {
		String v = node.val;
		if (v != null && v.length() >= 3) {
			words.add(v);
		}
		
		// mark the current position as visited
		VisitedPositions newVisitedPositions = new VisitedPositions(visitedPositions, x, y);

		for (int i = Math.max(0, x - 1); i <= Math.min(x + 1, board.rows() - 1); i++) {
			for (int j = Math.max(0, y - 1); j <= Math.min(y + 1, board.cols() - 1); j++) {
				if (newVisitedPositions.wasVisited(i, j)) {
					continue;
				}
				String suffix = getSuffix(board, i, j);
				DictionaryNode nextNode = dictionaryTrie.get(node.mid, suffix, 0);
				if (nextNode != null) {
					exploreBoard(words, board, i, j, newVisitedPositions, nextNode);
				}
			}
		}
	}

	/**
	 * Bit set marking which positions were already visited
	 * 
	 * @author lc
	 */
	private class VisitedPositions {
		private final boolean[] positions;
		private final int n;

		public VisitedPositions(BoggleBoard board) {
			this.positions = new boolean[board.rows() * board.cols()];
			this.n = board.cols();
		}

		public VisitedPositions(VisitedPositions visited, int i, int j) {
			this.positions = Arrays.copyOf(visited.positions, visited.positions.length);
			this.n = visited.n;
			setPosition(i, j);
		}

		private void setPosition(int i, int j) {
			this.positions[i * n + j] = true;
		}

		public boolean wasVisited(int i, int j) {
			return this.positions[i * n + j];
		}
	}

	/**
	 * Returns the score of the given (not necessarily valid) word. (You can assume the word contains only the uppercase
	 * letters A through Z.)
	 * 
	 * @param word
	 * @return
	 */
	public int scoreOf(String word) {
		if (word == null) {
			throw new IllegalArgumentException("Word is mandatory");
		}
		if (word.length() <= 2 || !dictionaryTrie.contains(word)) {
			return 0;
		}

		switch (word.length()) {
			case 3:
			case 4:
				return 1;
			case 5:
				return 2;
			case 6:
				return 3;
			case 7:
				return 5;
			case 8:
			default:
				return 11;
		}
	}

	/**
	 * Sample main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usave: BoggleSolver <dictionnary> <board>");
			System.exit(-1);
		}
		String[] dictionary = new In(args[0]).readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);

		BoggleBoard board = new BoggleBoard(args[1]);
		Iterable<String> validWords = solver.getAllValidWords(board);
		int score = 0;
		for (String word : validWords) {
			System.out.println(word);
			score += solver.scoreOf(word);
		}
		System.out.format("Score = %d\n", score);
	}
}
