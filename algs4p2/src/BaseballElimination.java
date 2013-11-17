import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lc
 */
public class BaseballElimination {

	private final List<String> teams;
	private final int[] w;
	private final int[] l;
	private final int[] r;
	private final int[][] g;

	/**
	 * create a baseball division from given filename in format specified below
	 * 
	 * @param filename file
	 */
	public BaseballElimination(String filename) {
		File f = new File(filename);
		if (!f.exists()) {
			throw new IllegalArgumentException("File " + filename + " not found");
		}
		In in = new In(filename);
		try {
			int nbTeams = in.readInt();

			teams = new ArrayList<String>(nbTeams);
			w = new int[nbTeams];
			l = new int[nbTeams];
			r = new int[nbTeams];
			g = new int[nbTeams][nbTeams];

			for (int i = 0; i < nbTeams; i++) {
				String teamName = in.readString();
				teams.add(teamName);

				w[i] = in.readInt();
				l[i] = in.readInt();
				r[i] = in.readInt();

				for (int j = 0; j < nbTeams; j++) {
					g[i][j] = in.readInt();
				}
			}

		} finally {
			in.close();
		}
	}

	/**
	 * number of teams
	 * 
	 * @return number of teams
	 */
	public int numberOfTeams() {
		return teams.size();
	}

	/**
	 * all teams
	 * 
	 * @return
	 */
	public Iterable<String> teams() {
		return teams;
	}

	/**
	 * number of wins for given team
	 * 
	 * @param team a team
	 * @return number of wins for given team
	 */
	public int wins(String team) {
		int idx = getIndexOfTeam(team);
		return w[idx];
	}

	/**
	 * number of losses for given team
	 * 
	 * @param team a team
	 * @return number of losses for given team
	 */
	public int losses(String team) {
		int idx = getIndexOfTeam(team);
		return l[idx];

	}

	/**
	 * number of remaining games for given team
	 * 
	 * @param team a team
	 * @return number of remaining games for given team
	 */
	public int remaining(String team) {
		int idx = getIndexOfTeam(team);
		return r[idx];

	}

	/**
	 * number of remaining games between team1 and team2
	 * 
	 * @param team1 a team
	 * @param team2 a team
	 * @return number of remaining games between team1 and team2
	 */
	public int against(String team1, String team2) {
		int idx1 = getIndexOfTeam(team1);
		int idx2 = getIndexOfTeam(team2);
		return g[idx1][idx2];

	}

	/**
	 * is given team eliminated?
	 * 
	 * @param team a team
	 * @return is given team eliminated?
	 */
	public boolean isEliminated(String team) {
		Iterable<String> it = certificateOfElimination(team);
		return it != null;
	}
	
	private FlowNetwork buildEliminationFlow(int x) {
		int nbVert = 2 + numberOfTeams();
		for (int i = 0; i < numberOfTeams(); i++) {
			for (int j = i + 1; j < numberOfTeams(); j++) {
				if (g[i][j] > 0 && i != x && j != x) {
					nbVert++;
				}
			}
		}

		int s = 0;
		int t = nbVert - 1;

		FlowNetwork G = new FlowNetwork(nbVert);

		// add games left edges
		int cur = 1;
		for (int i = 0; i < numberOfTeams(); i++) {
			for (int j = i + 1; j < numberOfTeams(); j++) {
				if (g[i][j] > 0 && i != x && j != x) {

					FlowEdge edge = new FlowEdge(s, cur, g[i][j]);
					G.addEdge(edge);

					// add infinite edges
					FlowEdge out1 = new FlowEdge(cur, t - numberOfTeams() + i, Double.POSITIVE_INFINITY);
					G.addEdge(out1);
					FlowEdge out2 = new FlowEdge(cur, t - numberOfTeams() + j, Double.POSITIVE_INFINITY);
					G.addEdge(out2);

					cur++;
				}
			}
		}

		// add out edges
		for (int i = 0; i < numberOfTeams(); i++) {
			FlowEdge e = new FlowEdge(t - numberOfTeams() + i, t, w[x] + r[x] - w[i]);

			G.addEdge(e);
		}
		return G;
	}
	
	private boolean isEliminated(FlowNetwork fn, int s) {
		for (FlowEdge fe : fn.adj(s)) {
			int to = fe.other(s);
			if (fe.residualCapacityTo(to) > 0) {
				return true;
			}
		}
		return false;
	}

	private int getIndexOfTeam(String team) {
		int x = teams.indexOf(team);
		if (x == -1) {
			throw new IllegalArgumentException("Team does not exist");
		}
		return x;
	}

	private boolean isTriviallyEliminatedBy(int x, int i) {
		return x != i && (w[x] + r[x]) < w[i];
	}

	/**
	 * subset R of teams that eliminates given team; null if not eliminated
	 * 
	 * @param team a team
	 * @return subset R of teams that eliminates given team; null if not eliminated
	 */
	public Iterable<String> certificateOfElimination(String team) {
		int x = getIndexOfTeam(team);

		// check trivial
		for (int i = 0; i < numberOfTeams(); i++) {
			if (isTriviallyEliminatedBy(x, i)) {
				return Arrays.asList(teams.get(i));
			}
		}
		
		// no trivial,
		FlowNetwork G = buildEliminationFlow(x);
		int s = 0;
		int t = G.V() - 1;
		
		// build FF
		FordFulkerson ff = new FordFulkerson(G, s, t);
		if (isEliminated(G, s)) {
			List<String> cert = new ArrayList<String>();
			for (int i = 0; i < numberOfTeams(); i++) {
				if (i != x && ff.inCut(t - numberOfTeams() + i)) {
					cert.add(teams.get(i));
				}
			}
			
			return cert;
		} 
		
		// not eliminated
		return null;
	}

}
