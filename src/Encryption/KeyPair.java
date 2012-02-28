package Encryption;
import java.math.BigInteger;

/** Description of KeyPair 
 * @author Frederic Jacobs
 * @author Hantao Zhao 
 * @version 1.0
 */

public final class KeyPair
{
	private  BigInteger n, e, d;
	private int N;
	
	/** 
	* KeyPair Constructer builds a pair of keys based on 4 parameters.  
	* @param n
	* 
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

	// Implementing the encrypt method and decrypt method

	public BigInteger encrypt(BigInteger message)
	{
		return message.modPow(e, n);
	}

	public BigInteger decrypt(BigInteger cryptedMessage)
	{
		return cryptedMessage.modPow(d, n);
	}


	// Implementing the getters and setters required for the Unit Test 
	
	public int getKeySize() {
		return N;
	}

	public  byte[] getPublicKey() {
		return e.toByteArray();
	}

	public  byte[] getModulus() {
		
		return n.toByteArray();
	}

	public void setPrivateKey(BigInteger privateKey) {
		d = privateKey ; 
	}

	public BigInteger getPrivateKey() {
		return d;
	}

	public void hidePrivateKey() {
		d=null;
	}
	
}