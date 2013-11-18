import java.util.HashMap;
import java.util.Map;


/**
 * homework 1 submission
 * @author lcanet
 *
 */
public class WordNet {
	
	private final Digraph graph;
	private final Map<String, Bag<Integer>> words;
	private final Map<Integer, String> synsetsTable;
	private final SAP sap;
	
	/**
	 *  constructor takes the name of the two input files
	 * @param synsets synset file name
	 * @param hypernyms hypernyms file name
	 * @throws IllegalArgumentException
	 */
	public WordNet(String synsets, String hypernyms) {

		words = new HashMap<String, Bag<Integer>>();
		synsetsTable = new HashMap<Integer, String>();
		
		In synsetsInput = new In(synsets);
		String line;
		int n = 0;
		while ((line = synsetsInput.readLine()) != null) {
			String[] parts = line.split(",");
			if (parts.length >= 2) {
				n = Integer.parseInt(parts[0]);
				String[] syns = parts[1].split(" ");
				for (String syn : syns) {
					Bag<Integer> ids = words.get(syn);
					if (ids == null) {
						ids = new Bag<Integer>();
						words.put(syn, ids);
					} 
					ids.add(n);
				}
				synsetsTable.put(n, parts[1]);
			}
		}
		
		graph = new Digraph(n+1);
		
		In hypernymsInput = new In(hypernyms);
		while ((line = hypernymsInput.readLine()) != null) {
			String[] parts = line.split(",");
			if (parts.length >= 2) {
				int from = Integer.parseInt(parts[0]);
				for (int i = 1; i < parts.length; i++) {
					int to = Integer.parseInt(parts[i]);
					graph.addEdge(from, to);
				}
			}
		}
		
		// check that graph is a dag
        DirectedCycle finder = new DirectedCycle(graph);
        if (finder.hasCycle()) {
        	throw new IllegalArgumentException("wordnet is not a graph");
        }
        int roots = -1;
        for (int i = 0; i < graph.V(); i++) {
        	if (!graph.adj(i).iterator().hasNext()) {
        		if (roots != -1) {
        			throw new IllegalArgumentException("More than one roots (" + roots + " and " + i + ")");
        		} else {
        			roots = i;
        		}
        	}
        }
        
        sap = new SAP(graph);
	}

	/**
	 * the set of nouns (no duplicates), returned as an Iterable
	 * @return a set of nouns
	 */
	public Iterable<String> nouns() {
		return words.keySet();
	}

	/**
	 *  is the word a WordNet noun?
	 * @param word a word
	 * @return true if it is a wordnet noun
	 */
	public boolean isNoun(String word) {
		return words.containsKey(word);
	}

	/**
	 *  distance between nounA and nounB (defined below)
	 * @param nounA a noun
	 * @param nounB noun
	 * @return the distance
	 */
	public int distance(String nounA, String nounB) {
		Iterable<Integer> nA = words.get(nounA);
		Iterable<Integer> nB = words.get(nounB);
		if (nA == null || nB == null) {
			throw new IllegalArgumentException("words are not member");
		}
		return sap.length(nA, nB);
	}

	/**
	 * a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	 * in a shortest ancestral path (defined below)
	 * @param nounA a noun
	 * @param nounB a noun
	 * @return the shortest ancestral path
	 */
	// 
	public String sap(String nounA, String nounB) {
		Iterable<Integer> nA = words.get(nounA);
		Iterable<Integer> nB = words.get(nounB);
		if (nA == null || nB == null) {
			throw new IllegalArgumentException("words are not member");
		}
		int ancestor = sap.ancestor(nA, nB);
		if (ancestor == -1) {
			throw new IllegalArgumentException("Fields are not synset");
		}
		return synsetsTable.get(ancestor);
		
	}

	// for unit testing of this class
	public static void main(String[] args) {
		new WordNet(args[0], args[1]);
		
	}
	
}
