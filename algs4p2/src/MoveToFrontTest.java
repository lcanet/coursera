import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Test;


public class MoveToFrontTest {

	@Test
	public void testEncode() {
		InputStream systemIn = System.in;
		PrintStream systemOut = System.out;
		try  {
			System.setIn(new ByteArrayInputStream("ABRACADABRA!".getBytes()));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(bos));
			MoveToFront.encode();
			
			byte[] result = bos.toByteArray(); 
			assertEquals(12, result.length);
			assertEquals(0x41, result[0]);
			assertEquals(0x42, result[1]);
			assertEquals(0x52, result[2]);
			assertEquals(0x02, result[3]);
			assertEquals(0x02, result[10]);
			assertEquals(0x26, result[11]);
			
			
			
		} finally {
			System.setIn(systemIn);
			System.setOut(systemOut);
		}
	}

	@Test
	public void testDecode() {
		InputStream systemIn = System.in;
		PrintStream systemOut = System.out;
		try  {
			System.setIn(new ByteArrayInputStream(new byte[] {0x41, 0x42,0x52,0x02,0x44,0x01,0x45,0x01,0x04,0x04,0x02,0x26}));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			System.setOut(new PrintStream(bos));
			MoveToFront.decode();
			
			String result = new String(bos.toByteArray());
			assertEquals("ABRACADABRA!", result);
			
			
		} finally {
			System.setIn(systemIn);
			System.setOut(systemOut);
		}
	}

}
