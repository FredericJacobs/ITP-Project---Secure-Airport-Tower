package encryption;


import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Write and encrypt bytes to an output stream. This stream uses RSA decryption.
 * @author Frederic Jacobs
 * @author Hantao Zhao
 * @see InputStream
 * @see KeyPair
 */
public class RsaOutputStream extends OutputStream

{
	// Defining the OutputStream and the KeyPair used for encryption
	private final OutputStream output;
	private final KeyPair key;
	private int blockSize;
	private byte[] block;
	private int bufferSize;
	private int bufferLength = 0;
	private byte[] buffer;
	private SecureRandom rand = null;

	/**
	 * Create a new RSA output stream.
	 * 
	 * @param output
	 *            Output stream to use.
	 * @param key
	 *            Key to use.
	 */
	public RsaOutputStream(OutputStream output, KeyPair key)
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

		rand = new SecureRandom();

		blockSize = (key.getKeySize() / 8) + 1;
		bufferSize = blockSize - 4; // Front byte + Padding boundary

		buffer = new byte[bufferSize];

		// Only one block allocation (no zero-ing memory on each flush)
		block = new byte[blockSize];
	}

	@Override
	public void flush() throws IOException {
		// Nothing to send, so we don't send anything.
		if(bufferLength == 0)
			return;

		// Front zero
		// BigInt(block) < (key-modulo - 1)
		block[0] = 0;
		block[1] = 0;

		// Padding
		int padding = (blockSize - 3) - bufferLength;

		byte[] padding_bytes = new byte[padding];
		rand.nextBytes(padding_bytes);

		for(int i = 0; i < padding_bytes.length; i++) {
			// Check zero-byte
			while(padding_bytes[i] == 0) {
				padding_bytes[i] = (byte) (rand.nextInt() & 0xff);
			}
		}
		// Correcting padding 
		System.arraycopy(padding_bytes, 0, block, 2, padding);
		block[padding + 2] = 0;
		System.arraycopy(buffer, 0, block, padding + 3, bufferLength);

		// Encrypt
		byte[] block_encrypted = key.encrypt(new BigInteger(block)).toByteArray();

		padding = block.length - block_encrypted.length;

		for(int i = 0; i < padding; i++)
			block[i] = 0;

		System.arraycopy(block_encrypted, 0, block, padding, block_encrypted.length);

		output.write(block);
		output.flush();
		bufferLength = 0;
	}

	@Override
	public void close() throws IOException {
		flush(); 
		output.close();
	}

	@Override
	public void write(int b) throws IOException {
		buffer[bufferLength++] = (byte) (b & 0xff);

		if(bufferLength >= bufferSize)
			flush();
	}

}
