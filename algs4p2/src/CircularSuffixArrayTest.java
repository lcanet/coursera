import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;


public class CircularSuffixArrayTest {

	@Test
	public void buildSuffixArray() {
		CircularSuffixArray suffixArray = new CircularSuffixArray("ABRACADABRA!");
		assertEquals(12, suffixArray.length());
		assertEquals(11, suffixArray.index(0));
		assertEquals(10, suffixArray.index(1));
		assertEquals(7, suffixArray.index(2));
		assertEquals(0, suffixArray.index(3));
		assertEquals(3, suffixArray.index(4));
		assertEquals(5, suffixArray.index(5));
		assertEquals(8, suffixArray.index(6));
	}
	
	@Test
	public void test2() {
		CircularSuffixArray suffixArray = new CircularSuffixArray("nk7ngl5upu");
		assertEquals(9, suffixArray.index(8));
	}

	@Test
	public void buildSuffixArrayDumb() {
		CircularSuffixArray suffixArray = new CircularSuffixArray("AAAAAAA");
		for (int i = 0; i < suffixArray.length(); i++) {
			assertEquals(6-i, suffixArray.index(i));
		}
	}
	
	@Test
	public void random() {
		Random r = new Random();
		for (int i = 0; i < 1000; i++) {
			for (int j = 0; j < 100; j++) {
				byte[] rnd = new byte[i];
				for (int k = 0; k < i ;k ++) {
					rnd[k] = (byte) r.nextInt();
				}
				new CircularSuffixArray(new String(rnd));
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void buildInvalid() {
		new CircularSuffixArray(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void buildInvalid2() {
		CircularSuffixArray suffixArray = new CircularSuffixArray("ABCD");
		suffixArray.index(10);
	}

}
