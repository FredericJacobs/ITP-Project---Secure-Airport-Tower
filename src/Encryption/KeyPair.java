package Encryption;
import java.math.BigInteger;


public final class KeyPair
{
	private final BigInteger n, e, d;
	private final int N;
	//Building the constructor that stores n,e,d and the length of the key in a single KeyPair object.
	public KeyPair(BigInteger n, BigInteger e, BigInteger d, int N)
	{
		if (N % 8 != 0 || N <= 0){
			throw new IllegalArgumentException("N must be a multiple of 8");
		}
		this.N = N;
		if (n.signum() <= 0)
			throw new IllegalArgumentException("n must be possitive");
		this.n = n;
		this.e = e;
		this.d = d;
	}



	public BigInteger encrypt(BigInteger message)
	{
		return message.modPow(e, n);
	}

	public BigInteger decrypt(BigInteger cryptedMessage)
	{
		return cryptedMessage.modPow(d, n);
	}



	public void setPrivateKey() {
	}



	public int getKeySize() {
		return N;
	}



	public BigInteger getPublicKey() {
		return n;
	}



	public BigInteger getModulus() {
		// TODO Auto-generated method stub
		return null;
	}
	
}