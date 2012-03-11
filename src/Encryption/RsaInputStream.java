package Encryption;

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
	private final InputStream input;
	private final KeyPair key;

	/**
	 * Create a new RSA input stream.
	 * 
	 * @param key
	 *            Key to use.
	 * @param input
	 *            Input stream to use.
	 */
	public RsaInputStream(KeyPair key, InputStream input)
	{
		// Checking if the arguments are valid
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
	public int available() throws IOException
	{
		return input.available();
	}

	@Override
	public void close() throws IOException
	{
		input.close();
	}

	@Override
	public synchronized void mark(int readlimit)
	{
		input.mark(readlimit);
	}

	@Override
	public boolean markSupported()
	{
		return input.markSupported();
	}

	@Override
	public int read() throws IOException
	{
		//Filling the array with the crypted bytes
		byte[] bytes = new byte[key.getModulus().length / 8 + 1];
		int r = input.read(bytes);
		if (r != bytes.length)
			return -1;
		//Converting the array into a BigInteger
		BigInteger bint = new BigInteger(bytes);
		// Here comes the decryption process
		
		BigInteger result;
		try {
			result = key.decrypt(bint);
		} catch (DecryptWithoutPrivateKeyException e) {
			result = null;
		}
		
		return result.intValue();
	}

	@Override
	public synchronized void reset() throws IOException
	{
		input.reset();
	}

	@Override
	public long skip(long n) throws IOException
	{
		return input.skip(n);
	}
}
