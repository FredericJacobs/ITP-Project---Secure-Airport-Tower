package encryption;

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
	private InputStream input;
	private KeyPair key;
	private int blockSize;
	private int bufferSize;
	private int bufferLength = 0;
	private int bufferPosition = 0;
	private byte[] buffer;

	/**
	 * Create a new RSA input stream.
	 * 
	 * @param input
	 *            Input stream to use.
	 * @param key
	 *            Key to use.
	 */
	public RsaInputStream(InputStream input, KeyPair key)
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

		blockSize = (key.getKeySize() / 8) + 1;
		bufferSize = blockSize - 4;

		buffer = new byte[bufferSize];
	}

	public int available() throws IOException {
		return bufferLength - bufferPosition;
	}

	protected void load() throws IOException {
		byte[] block = new byte[blockSize];

		for(int i = 0; i < blockSize; i++) {
			block[i] = (byte) (input.read());
		}



		block = key.decrypt(new BigInteger(block)).toByteArray();

		// BigInteger outputs:
		//
		//    [----key---] --> l = n-1
		//    [----block---] -> l = n
		//
		// 1) [0|pad|0|buff] -> l = n
		// 2)   [pad|0|buff] -> l = n-1

		int drop = block[0] == 0 ? 1 : 0;
		int bufferOffset = -1;

		for(int i = drop; i < block.length; i++) {
			if(block[i] == 0) {
				bufferOffset = i + 1;
				break;
			}
		}

		if(bufferOffset < 0)
			throw new IOException("Invalid RSA block");

		// Buffer-reset
		bufferLength = block.length - bufferOffset;
		bufferPosition = 0;

		// Copy bytes
		System.arraycopy(block, bufferOffset, buffer, 0, bufferLength);
	}

	public int read() throws IOException {
		if(bufferPosition >= bufferLength)
			load();

		return (buffer[bufferPosition++]& 0xff);
	}


}
