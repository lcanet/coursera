import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Boggle game solver
 * 
 * alg4-pt2 hw
 * 
 * @author lc
 *
 */
public class BoggleSolver {

	private final BoggleDictionary dictionaryTrie;
	
  /**
   *  Initializes the data structure using the given array of strings as the dictionary.
   *  (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
   * @param dictionary
   * 
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
   *  Returns all valid words in the given Boggle board, as an Iterable.
   * @param board
   * @return
   */
  public Iterable<String> getAllValidWords(BoggleBoard board) {
  	if (board == null) {
  		throw new IllegalArgumentException("Board is mandatory");
  	}
  	
  	Set<String> results = new HashSet<String>();
  	for (int i = 0; i < board.rows(); i++) {
  		for (int j = 0; j < board.cols(); j++) {
  			exploreBoard(results, board, i, j, "", new VisitedPositions(board));
  		}
  	}
  	
  	// sort
  	List<String> sortedResults = new ArrayList<String>(results);
  	Collections.sort(sortedResults);
  	
  	return sortedResults;
  }
  
  private void exploreBoard(Set<String> words, BoggleBoard board, int x, int y, String prefix, VisitedPositions visitedPositions) {
  	if (prefix.length() >= 3) {
  		if (dictionaryTrie.contains(prefix)) {
  			words.add(prefix);
  		}
  	}
  	// mark the current position as visited
  	VisitedPositions newVisitedPositions = new VisitedPositions(visitedPositions, x, y);

  	for (int i = Math.max(0, x-1); i <= Math.min(x+1, board.rows()-1); i++) {
    	for (int j = Math.max(0, y-1); j <= Math.min(y+1, board.cols()-1); j++) {
    		if (newVisitedPositions.wasVisited(i, j)) {
    			continue;
    		}
    		char letter = board.getLetter(i, j);
    		String newPrefix ;
    		if (letter == 'Q') {
      		newPrefix = prefix + "QU";
    		} else {
    			newPrefix = prefix + letter;
    		}
    		// oontinue only if newprefix is in trie
    		if (dictionaryTrie.hasPrefix(newPrefix)) {
    			exploreBoard(words, board, i, j, newPrefix, newVisitedPositions);
    		}
    	}
  	}
  }
  
  /**
   * Bit set marking which positions were already visited
   * @author lc
   *
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
   *  Returns the score of the given (not necessarily valid) word.
   *  (You can assume the word contains only the uppercase letters A through Z.)
   * @param word
   * @return
   */
  public int scoreOf(String word) {
  	if (word == null) {
  		throw new IllegalArgumentException("Word is mandatory");
  	}
  	switch (word.length()) {
  		case 0:
  		case 1:
  		case 2:
  			return 0;
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
   * Returns the score of a set of word
   * @param words
   * @return 
   */
  public int scoreOf(Iterable<String> words)  {
  	int score = 0;
  	for (String s : words) {
  		score += scoreOf(s);
  	}
  	return score;
  }
  
  /**
   * Sample main
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
		for (String word : validWords) {
			System.out.println(word);
		}
		System.out.format("Score = %d\n", solver.scoreOf(validWords));
	}
}
