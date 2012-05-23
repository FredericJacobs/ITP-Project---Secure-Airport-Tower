package tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;
import encryption.*;

public class RsaStreamTest {

	@Test
	public void test() throws IOException {
		KeyPair kp = KeyGenerator.generateRSAKeyPair(64);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RsaOutputStream ros = new RsaOutputStream(baos, kp);
		
		byte[] clearText ="Hello, world!".getBytes(); 
		ros.write(clearText);
		ros.flush();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		RsaInputStream ris = new RsaInputStream(bais, kp);
		
		byte[] readText = new byte[clearText.length];
		for (int i = 0; i < readText.length; ++i) {
			readText[i] = (byte) ris.read();
		}
		
		assertArrayEquals(clearText, readText);
		ris.close();
		ros.close();
	}

}
