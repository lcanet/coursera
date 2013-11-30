import static org.junit.Assert.*;

import org.junit.Test;


public class BoggleDictionaryTest {


	
	@Test
	public void getPut() {
		
		BoggleDictionary dict = new BoggleDictionary();
		assertFalse(dict.contains("HELLO"));
		dict.add("HELL");
		assertFalse(dict.contains("HELLO"));
		dict.add("WORLD");
		assertFalse(dict.contains("HELLO"));
		dict.add("HELLO");
		assertTrue(dict.contains("HELLO"));
		assertTrue(dict.contains("WORLD"));
		assertTrue(dict.contains("HELL"));
		
	}
	
	@Test
	public void hasPrefix() {
		BoggleDictionary dict = new BoggleDictionary();
		dict.add("HELLO");
		dict.add("HELL");
		dict.add("WORLD");
		dict.add("HELLSHIT");
		
		assertTrue(dict.hasPrefix("HE"));
		assertTrue(dict.hasPrefix("HELL"));
		assertTrue(dict.hasPrefix("HELLO"));
		assertTrue(dict.hasPrefix("WO"));
		assertTrue(dict.hasPrefix("WORLD"));
		assertFalse(dict.hasPrefix("WORLDWIDE"));
	}

}
