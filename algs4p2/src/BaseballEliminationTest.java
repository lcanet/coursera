import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;


public class BaseballEliminationTest {

	@Test
	public void testBaseballElimination() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertNotNull(b);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBaseballEliminationFail() {
		new BaseballElimination("__INEXISTANT__");
	}

	@Test
	public void testNumberOfTeams() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertEquals(4, b.numberOfTeams());
		b = new BaseballElimination("data/baseball/teams5.txt");
		assertEquals(5, b.numberOfTeams());
	}

	@Test
	public void testTeams() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		List<String> ls = toList(b.teams());
		assertEquals(4, ls.size());
		assertTrue(ls.contains("Atlanta"));
		assertTrue(ls.contains("Montreal"));
		assertFalse(ls.contains("Paris SG"));
	}

	@Test
	public void testWins() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertEquals(83, b.wins("Atlanta"));
		assertEquals(77, b.wins("Montreal"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWinsInvalid() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertEquals(0, b.wins("Paris SG"));
	}

	@Test
	public void testLosses() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertEquals(71, b.losses("Atlanta"));
		assertEquals(82, b.losses("Montreal"));
	}

	@Test
	public void testRemaining() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertEquals(8, b.remaining("Atlanta"));
		assertEquals(3, b.remaining("Montreal"));
	}

	@Test
	public void testAgainst() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertEquals(1, b.against("Atlanta", "Montreal"));
		assertEquals(0, b.against("New_York", "Philadelphia"));
	}

	@Test
	public void testIsEliminated() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertTrue(b.isEliminated("Montreal"));
		assertTrue(b.isEliminated("Philadelphia"));
		assertFalse(b.isEliminated("Atlanta"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsEliminatedInvalid() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertTrue(b.isEliminated("Paris SG"));
	}

	@Test
	public void testCertificateOfElimination() {
		BaseballElimination b = new BaseballElimination("data/baseball/teams4.txt");
		assertTrue(toList(b.certificateOfElimination("Montreal")).contains("Atlanta"));
		assertEquals(1, toList(b.certificateOfElimination("Montreal")).size());

		assertTrue(toList(b.certificateOfElimination("Philadelphia")).contains("Atlanta"));
		assertTrue(toList(b.certificateOfElimination("Philadelphia")).contains("New_York"));
		assertEquals(2, toList(b.certificateOfElimination("Philadelphia")).size());

	}
	
	private List<String> toList(Iterable<String> iterable) {
		List<String> ls = new ArrayList<String>();
		for (Iterator<String> it = iterable.iterator(); it.hasNext(); ){
			ls.add(it.next());
		}
		return ls;
	}

}
