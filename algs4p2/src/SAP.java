/**
 * Assignement 1
 * @author lc
 *
 */
public class SAP {

	private final Digraph graph;
	
	/**
	 * constructor takes a digraph (not necessarily a DAG)
	 * 
	 * @param G
	 */
	public SAP(Digraph G) {
		assert G != null;
		this.graph = new Digraph(G);
	}

	/**
	 * length of shortest ancestral path between v and w; -1 if no such path
	 * 
	 * @param v
	 * @param w	
	 * @return
	 */
	public int length(int v, int w) {
		if (v < 0 || v >= graph.V()) throw new IndexOutOfBoundsException("index v out of bounds");
		if (w < 0 || w >= graph.V()) throw new IndexOutOfBoundsException("index w out of bounds");
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
		
		int minDist = -1;
		for (int i = 0; i < graph.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);
				if (minDist == -1 || dist < minDist) {
					minDist = dist;
				}
			}
		}
		return minDist;
	}

	/**
	 * a common ancestor of v and w that participates in a shortest ancestral
	 * path; -1 if no such path
	 * 
	 * @param v
	 * @param w
	 * @return
	 */
	public int ancestor(int v, int w) {
		if (v < 0 || v >= graph.V()) throw new IndexOutOfBoundsException("index v out of bounds");
		if (w < 0 || w >= graph.V()) throw new IndexOutOfBoundsException("index w out of bounds");
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
		
		int minDist = Integer.MAX_VALUE;
		int argMinDist = -1;
		for (int i = 0; i < graph.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);
				if (dist < minDist) {
					minDist = dist;
					argMinDist = i;
				}
			}
		}

		return argMinDist;
	}


	/**
	 * length of shortest ancestral path between any vertex in v and any vertex
	 * in w; -1 if no such path
	 * 
	 * @param v a vertex index
	 * @param w a vertex index
	 * @return
	 */
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		checkIndex(v);
		checkIndex(w);
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);

		int minDist = -1;
		for (int i = 0; i < graph.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);
				if (minDist == -1 || dist < minDist) {
					minDist = dist;
				}
			}
		}
		return minDist;
	}

	private void checkIndex(Iterable<Integer> v) {
		if (v == null) {
			throw new IllegalArgumentException("v cannot be null");
		}
		for (int vv : v) {
			if (vv < 0 || vv >= graph.V()) {
				throw new IllegalArgumentException("out of bound index " + vv);
			}
		}

	}

	/**
	 * a common ancestor that participates in shortest ancestral path; -1 if no
	 * such path
	 * 
	 * @param v
	 * @param w
	 * @return
	 */
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		checkIndex(v);
		checkIndex(w);
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
		
		int minDist = Integer.MAX_VALUE;
		int argMinDist = -1;
		for (int i = 0; i < graph.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);
				if (dist < minDist) {
					minDist = dist;
					argMinDist = i;
				}
			}
		}

		return argMinDist;

	}

	/**
	 * for unit testing of this class (such as the one below)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			int length = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
