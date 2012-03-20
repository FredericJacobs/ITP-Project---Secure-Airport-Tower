package encryption;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * Read and decrypt bytes from an input stream. This stream uses RSA decryption.
 * @author Frederic Jacobs
 * @author Hantao Zhao
 * @see InputStream
 * @see KeyPair
 */
public class RsaInputStream extends InputStream
{
	// Defining the inputStream and the KeyPair used for encryption
	private final ByteArrayInputStream input;
	private final KeyPair key;

	/**
	 * Create a new RSA input stream.
	 * 
	 * @param input
	 *            Input stream to use.
	 * @param key
	 *            Key to use.
	 */
	public RsaInputStream(ByteArrayInputStream input, KeyPair key)
	{	// Checking if the arguments are valid
		if (key == null)
			throw new IllegalArgumentException(
			        "Key must contains at least one byte!");
		if (key.getPrivateKey() == null)
			throw new IllegalArgumentException(
			        "D value of key must be specified!");
		if (input == null)
			throw new IllegalArgumentException(
			        "An input stream must be specified!");
		// Since these arguments are valid, we are setting the key and input.
		this.key = key;
		this.input = input;
	}


	@Override
	public int read() throws IOException 
	{
		byte[] bytes = new byte[key.getKeySize() / 8 + 1];
		int r = input.read(bytes);
		if (r != bytes.length)
			return -1;
		BigInteger bint = new BigInteger(bytes);
		BigInteger result =key.decrypt(bint);
		return result.intValue();
	}

}
