import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;

public class BurrowsWheelerTest {
	@Test
	public void testEncode() {
		InputStream systemIn = System.in;
		PrintStream systemOut = System.out;
		try {
			System.setIn(new ByteArrayInputStream("ABRACADABRA!".getBytes()));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(bos));
			BurrowsWheeler.encode();

			byte[] result = bos.toByteArray();
			assertEquals(16, result.length);
			// position
			assertEquals(0x0, result[0]);
			assertEquals(0x0, result[1]);
			assertEquals(0x0, result[2]);
			assertEquals(0x3, result[3]);

			// leters
			assertEquals(0x41, result[4]);
			assertEquals(0x52, result[5]);
			assertEquals(0x44, result[6]);
			assertEquals(0x21, result[7]);
			assertEquals(0x52, result[8]);
			assertEquals(0x43, result[9]);
			assertEquals(0x41, result[10]);

		} finally {
			System.setIn(systemIn);
			System.setOut(systemOut);
		}
	}

	@Test
	public void testDecode() {
		InputStream systemIn = System.in;
		PrintStream systemOut = System.out;
		try {
			System.setIn(new ByteArrayInputStream(new byte[] { 0x00, 0x00, 0x00, 0x03, 0x41, 0x52, 0x44, 0x21, 0x52, 0x43,
					0x41, 0x41, 0x41, 0x41, 0x42, 0x42 }));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(bos));
			BurrowsWheeler.decode();
			
			String output = new String(bos.toByteArray());
			assertEquals("ABRACADABRA!", output);

		} finally {
			System.setIn(systemIn);
			System.setOut(systemOut);
		}

	}

}
