package Encryption;
import java.math.BigInteger;

/** Description of KeyPair 
 * KeyPair is a class to store Keys. I would have prefered to do it a different way but we sticked to the JUnit test. 
 * It has getters and setters to store and retrieve the keys.
 * The Constructor has a useless parameter for the length of the keys. Doesn't make much sense to me but required for the JUnit.
 * @author Frederic Jacobs
 * @author Hantao Zhao 
 * @version 1.0
 */

public final class KeyPair
{
	private  BigInteger n, e, d;
	private int N;
	
	/** 
	* KeyPair Constructor builds a pair of keys based on 4 parameters.  
	* @param n The Modulus used for RSA encryption
	* @param e Exponent used for RSA Encryption
	* @param d Private key used for decryption
	* @param N Length in bits of the key (useless)
	**/
	
	public KeyPair(BigInteger n, BigInteger e, BigInteger d, int N)
	{
		if (N % 8 != 0 || N <= 0){
			throw new IllegalArgumentException("N must be a multiple of 8");
		}
		this.N = N;
		if (!(n == null)){
			if (n.signum() <= 0){
				throw new IllegalArgumentException("n must be positive");
			}
		}
		this.n = n;
		this.e = e;
		this.d = d;
	}

	/** 
	* The encrypt method encrypts numbers.
	* @param message This BigInteger is the number to be encrypted
	* @return The encrypted message
	**/

	public BigInteger encrypt(BigInteger message)
	{
		return message.modPow(e, n);
	}

	/** 
	* The decrypt method decrypts numbers.
	* @param cyptedMessage This BigInteger is the number to be decrypted
	* @return The decrypted message
	**/
	public BigInteger decrypt(BigInteger cryptedMessage)
	{
		return cryptedMessage.modPow(d, n);
	}
	/** 
	* Required in the project documentation, using this method is not the safest way to check the keysize. It is NOT advised to use this method but it is required for the JUnit to work.
	* @return the keysize
	**/
	public int getKeySize() {
		return N;
	}
	
	/** 
	* This method returns the Exponent of the Public Key. Still the modulus should also be part of the private key but to stick to the JUnit we programmed it this way. 
	* @return the Public Key's Exponent required for encryption. 
	**/
	public  byte[] getPublicKey() {
		return e.toByteArray();
	}

	/** 
	* This method returns the modulus, part of the PrivateKey. It would have been better to create an object privateKey and publicKey but to stick to the JUnit we programmed it this way.
	* @return the Public Key's Modulus required for encryption.
	**/
	public  byte[] getModulus() {
		
		return n.toByteArray();
	}

	/** 
	* This sets the privateKey to a given value
	* @param privateKey Sets the private key to this value
	**/
	public void setPrivateKey(BigInteger privateKey) {
		d = privateKey ; 
	}

	/** 
	* This method returns a part of the private key (d)
	* @return d, part of the private key
	**/
	public BigInteger getPrivateKey() {
		return d;
	}

	/** 
	* This method's name isn't very straightforward but it does delete the Private Key by setting it to null
	**/
	public void hidePrivateKey() {
		d=null;
	}
	
}