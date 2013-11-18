
/**
 * Assignement 1 submission
 * @author lcanet
 *
 */
public class Outcast {

	// a wordnet
	private WordNet wordnet;
	
	/**
	 * constructor takes a WordNet object
	 * @param wordnet a wordnet
	 */
	public Outcast(WordNet wordnet) {
		if (wordnet == null) {
			throw new IllegalArgumentException();
		}
		this.wordnet = wordnet;
	}

	/**
	 *  given an array of WordNet nouns, return an outcast
	 * @param nouns an array of nouns
	 * @return the outcast noun
	 */
	public String outcast(String[] nouns) {
		int maxDist = Integer.MIN_VALUE;
		String argMaxDist = null;
		for (String s : nouns) {
			int dist = computeDistance(s, nouns);
			if (dist > maxDist) {
				maxDist = dist;
				argMaxDist = s;
			}
		}
		return argMaxDist;
	}

	private int computeDistance(String s, String[] nouns) {
		int d = 0;
		for (String ss : nouns ){
			if (!s.equals(ss)) {
				d += wordnet.distance(s, ss);
			}
		}
		return d;
	}

	// for unit testing of this class (such as the one below)
	public static void main(String[] args) {
	}
}
