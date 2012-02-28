package Encryption;
import java.math.BigInteger;
import java.security.SecureRandom;

/** Description of KeyGenerator 
 * KeyGenerator generates keys and stores them in a KeyPair object. This is not the safest way to implement RSA encryption.
 * @author Frederic Jacobs
 * @author Hantao Zhao 
 * @version 1.0
 */


public final class KeyGenerator
{
	/* Initializing the objects that will help us 
	 * SecureRandom provides a cryptographically strong pseudo-random number generator (PRNG) according to the JavaDoc
	 * e is the exponent that will be used as exponent during the encryption process. We defined it as a constant with the value 65537 according to Prof Telatar's presentation. 
	 * We can define those instance variable as static since they are linked to the class and not to a specific object. 
	 */
	private static final SecureRandom random = new SecureRandom();
	private static final BigInteger e = BigInteger.valueOf(65537);
	
	/** 
	* This is the method used to generate a KeyPair of length N. 
	* @param N The length of the keys in bits.
	* @return A Keypair object containing both public and private keys of length N.
	**/
	
	public static KeyPair generateRSAKeyPair(int N)
	{
		//Let's see if the user's input matches our expectations and is a multiple of a byte (8 bits)
		if (N % 8 != 0 || N <= 0){
			throw new IllegalArgumentException("N is not a multiple of 8");
		}
		
		BigInteger p, q, n, phi, d;

		while (true)
		{
		
			p = BigInteger.probablePrime(N / 2, random);
			q = BigInteger.probablePrime(N / 2, random);
			if (!p.equals(q))
			{

				//Doing some dirty maths operations on the numbers to compute the keys
				n = p.multiply(q);

				if (n.bitLength() == N)
				{
					phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
					
					if (e.gcd(phi).equals(BigInteger.ONE))
					{
						try
						{
							d = e.modInverse(phi);
						}
						catch (ArithmeticException exception)
						{
							continue;
						}
					
						KeyPair key = new KeyPair(n, e, d, N);
					
						BigInteger message = randomBigInteger(N);
						BigInteger cryptedMessage = key.encrypt(message);
						try {
							BigInteger decryptedMessage = key.decrypt(cryptedMessage);
								if (decryptedMessage.equals(message)){
								return key;
								}
						}
						catch (DecryptWithoutPrivateKeyException e){
							System.out.println("Decoding failed. No Private Key is given.");
						}

						}
					}
				}
			}
		}


	/** 
	* This method is required to generate a randomBigInteger for testing purposes
	* @param N The size of the key in bits
	* @return A random message within the capabilities of our RSA encryption method
	**/
	private static BigInteger randomBigInteger(int N)
	{	
		byte[] buffer = new byte[N / 8];
		random.nextBytes(buffer);
		return new BigInteger(buffer);
	}


}