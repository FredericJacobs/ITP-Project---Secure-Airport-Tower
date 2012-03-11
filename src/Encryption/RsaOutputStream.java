package Encryption;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

/**
 * Write and encrypt bytes to an output stream. This stream uses RSA decryption.
 * @author Frederic Jacobs
 * @author Hantao Zhao
 * @see InputStream
 * @see KeyPair1.0
 */
public class RsaOutputStream extends OutputStream
{
	private final OutputStream output;
	private final KeyPair key;

	/**
	 * Create a new RSA output stream.
	 * 
	 * @param key
	 *            Key to use.
	 * @param output
	 *            Output stream to use.
	 */
	public RsaOutputStream(KeyPair key, OutputStream output)
	{
		// Checking if the arguments are valid
		if (key == null)
			throw new IllegalArgumentException(
			        "Key must contains at least one byte!");
		if (key.getPublicKey() == null)
			throw new IllegalArgumentException(
			        "E value of key must be specified!");
		if (output == null)
			throw new IllegalArgumentException(
			        "An input stream must be specified!");
		// Since these arguments are valid, we are setting the key and input.
		this.key = key;
		this.output = output;
	}

	@Override
	public void close() throws IOException
	{
		output.close();
	}

	@Override
	public void flush() throws IOException
	{
		output.flush();
	}

	@Override
	public void write(int b) throws IOException
	{
		//Converting Byte to BigInteger, encrypting it and putting it back to a byte array.
		BigInteger raw = BigInteger.valueOf(b & 0xFF);
		BigInteger crypted = key.encrypt(raw);
		byte[] cryptedBytes = crypted.toByteArray();
		//Making sure the size of the array is big enough to store each Byte.
		byte[] result = new byte[key.getKeySize() / 8 + 1];
		System.arraycopy(cryptedBytes, 0, result, result.length
		        - cryptedBytes.length, cryptedBytes.length);
		// Write results needed
		output.write(result);
	}
}
