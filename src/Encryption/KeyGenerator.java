package Encryption;
import java.math.BigInteger;
import java.security.SecureRandom;


public final class KeyGenerator
{
	/* Initializing the objects that will help us 
	 * SecureRandom provides a cryptographically strong pseudo-random number generator (PRNG) according to the JavaDoc
	 * e is the exponent that will be used as exponent during the encryption process. We defined it as a constant with the value 65537 according to Prof Telatar's presentation. 
	 * We can define those instance variable as static since they are linked to the class and not to a specific object. 
	 */
	private static final SecureRandom random = new SecureRandom();
	private static final BigInteger e = BigInteger.valueOf(65537);
	
	// generateRSAKeyPair does generate
	
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

				//Doing some dirty operations on the numbers to compute the keys
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
						BigInteger decryptedMessage = key.decrypt(cryptedMessage);
						//Avoiding Invalid Keys by checking the whole encryption decryption process
						if (decryptedMessage.equals(message)){
							return key;
						}
					}
				}
			}
		}
	}

	private static BigInteger randomBigInteger(int N)
	{
		
		byte[] buffer = new byte[N / 8];
		random.nextBytes(buffer);
		return new BigInteger(buffer);
	}

	public static byte[] generateSymmetricKey(int keyLength)
	{
		if (keyLength <= 0)
			throw new IllegalArgumentException("Key length must be positive.");
		
		byte[] key = new byte[keyLength];
		random.nextBytes(key);
		return key;
	}
}