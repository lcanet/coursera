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
		BoggleSolver solver = new BoggleSolver(new String[] { "HELLO", "WORLD" });
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
		
	}
	
	@Test
	public void solve() {
		String[] dictionary = new In("data/boggle/dictionary-algs4.txt").readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard("data/boggle/board-q.txt");
		List<String> validWords = toList(solver.getAllValidWords(board));
		assertEquals(84, solver.scoreOf(validWords));
		assertTrue(validWords.contains("EQUATION"));
		assertTrue(validWords.contains("EQUATIONS"));
		assertTrue(validWords.contains("TRIES"));
		assertFalse(validWords.contains("BENCHMARK"));
		
	}
	
	private List<String> toList(Iterable<String> it) {
		List<String> ls = new ArrayList<String>();
		for (String s : it) {
			ls.add(s);
		}
		return ls;
	}
}
