import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class BoggleSolverTest {

	@Test
	public void testInstantiateOk() {
		String dict[] = new String[] {
				"HELLO",
				"WORLD"
		};
		BoggleSolver solver = new BoggleSolver(dict);
		assertNotNull(solver);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstantiateBad() {
		BoggleSolver solver = new BoggleSolver(null);
	}
	
	@Test
	public void scores() {
		BoggleSolver solver = new BoggleSolver(new String[] { "DOG", "PI", "CAKE", "QUEUE", "MANSION", "FOLLOWING",
				"HELLO", "WORLD", "LETTER", "BAGLABAGLAYOUPIEEDOO" });
		assertEquals(0, solver.scoreOf(""));
		assertEquals(0, solver.scoreOf("A"));
		assertEquals(0, solver.scoreOf("PI"));
		assertEquals(1, solver.scoreOf("DOG"));
		assertEquals(1, solver.scoreOf("CAKE"));
		assertEquals(2, solver.scoreOf("HELLO"));
		assertEquals(2, solver.scoreOf("QUEUE")); // special case
		assertEquals(3, solver.scoreOf("LETTER"));
		assertEquals(5, solver.scoreOf("MANSION"));
		assertEquals(11, solver.scoreOf("FOLLOWING"));
		assertEquals(11, solver.scoreOf("BAGLABAGLAYOUPIEEDOO"));
		assertEquals(0, solver.scoreOf("BAGLABAGLAYOUPIEEDOO2"));
		assertEquals(0, solver.scoreOf("CAT"));
		
	}
	
	@Test
	public void solveDumb() {
		BoggleSolver solver = new BoggleSolver(new String[] { "HELLO"});
		BoggleBoard board = new BoggleBoard(new char[][]{ "HELLO".toCharArray()});
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(1, validWords.size());
		
	}
	
	@Test
	public void solve1() {
		String[] dictionary = new In("data/boggle/dictionary-algs4.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board-q.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		int score = 0;
		for (String word : validWords) {
			score += solver.scoreOf(word);
		}
		
		assertEquals(84, score);
		assertTrue(validWords.contains("EQUATION"));
		assertTrue(validWords.contains("EQUATIONS"));
		assertTrue(validWords.contains("TRIES"));
		assertFalse(validWords.contains("BENCHMARK"));
		
	}

	@Test
	public void solve2() {
		String[] dictionary = new In("data/boggle/dictionary-yawl.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board4x4.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(204, validWords.size());
		
	}

	@Test
	public void solve3() {
		String[] dictionary = new In("data/boggle/dictionary-yawl.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board-points200.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(129, validWords.size());
	}

	@Test
	public void solve4() {
		String[] dictionary = new In("data/boggle/dictionary-yawl.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board-points1500.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(524, validWords.size());
	}

	@Test
	public void solve5() {
		String[] dictionary = new In("data/boggle/dictionary-yawl.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board-points13464.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(3123, validWords.size());
	}

	@Test
	public void solve6() {
		String[] dictionary = new In("data/boggle/dictionary-yawl.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board-dichlorodiphenyltrichloroethanes.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(27, validWords.size());
	}

	private List<String> toList(Iterable<String> it) {
		List<String> ls = new ArrayList<String>();
		for (String s : it) {
			ls.add(s);
		}
		return ls;
	}
}
