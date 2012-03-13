package encryption;

import java.io.ByteArrayOutputStream;
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
	private final ByteArrayOutputStream output;
	private final KeyPair key;

	/**
	 * Create a new RSA output stream.
	 * 
	 * @param output
	 *            Output stream to use.
	 * @param key
	 *            Key to use.
	 */
	public RsaOutputStream(ByteArrayOutputStream output, KeyPair key)
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
	public void write(int b) throws IOException {
		// Convertir le byte en BigInteger et le crypter.
		BigInteger raw = BigInteger.valueOf(b & 0xFF);
		BigInteger crypted = key.encrypt(raw);
		// Convertir en tableau de bytes.
		byte[] cryptedBytes = crypted.toByteArray();
		// Le tableau doit avoir une taille de T = N/8 + 1
		byte[] result = new byte[key.getKeySize() / 8 + 1];
		System.arraycopy(cryptedBytes, 0, result, result.length
		        - cryptedBytes.length, cryptedBytes.length);
		// Ecrire le résultat.
		output.write(result);
		
	}
	
}
