package tests;


/**
 *
 * @author acevedoma
 */
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.junit.Test;

import encryption.*;



public class RSAEncodeDecodeTest {

    /**
     * This test shows how to test expected exceptions.
     * 
     * Your KeyPair class should throw an exception
     * when the size of the key is not a multiple of 8
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBadModulusSize(){
        new KeyPair(new BigInteger("3233"), new BigInteger("17"), new BigInteger("2753"), 20);
    }
    
    @Test
    public void testKeyPair() {
        KeyPair keyPair = new KeyPair(new BigInteger("3233"), new BigInteger("17"), new BigInteger("2753"), 16);
        BigInteger encrypted = keyPair.encrypt(new BigInteger("65"));
        assertEquals(2790, encrypted.intValue());
        assertEquals(65, keyPair.decrypt(encrypted).intValue());

        encrypted = keyPair.encrypt(new BigInteger("123"));
        assertEquals(855, encrypted.intValue());
        assertEquals(123, keyPair.decrypt(encrypted).intValue());

        keyPair = new KeyPair(new BigInteger("17947"), new BigInteger("3"), new BigInteger("11787"), 16);
        encrypted = keyPair.encrypt(new BigInteger("513"));
        assertEquals(8363, encrypted.intValue());
        assertEquals(513, keyPair.decrypt(encrypted).intValue());

        encrypted = keyPair.encrypt(new BigInteger("14313"));
        assertEquals(13366, encrypted.intValue());
        assertEquals(14313, keyPair.decrypt(encrypted).intValue());
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        String message = "A pigeon has no use for keys.";

        for (int pow = 3; pow < 11; pow++) {
            int N = 1 << pow;
            System.out.println("Testing with N = " + N);
            KeyPair keyPair = KeyGenerator.generateRSAKeyPair(N);
            byte[] bytes = message.getBytes("UTF-8");

            BigInteger[] encrypted = encrypt(keyPair, bytes);
            byte[] bytesDecrypted = decrypt(keyPair, encrypted);

            System.out.println("message  = " + message);

            String decryptedMessage = new String(bytesDecrypted, "UTF-8");
            System.out.println("encrypted and decrypted message = " + decryptedMessage);

            assertEquals("The original message must be equal to the encrypted-decrypted message", decryptedMessage, message);
        }
    }


    private BigInteger[] encrypt(KeyPair keyPair, byte[] input) throws UnsupportedEncodingException {
        BigInteger[] output = new BigInteger[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = keyPair.encrypt(new BigInteger(Integer.toString(input[i])));
        }

        return output;
    }

    private byte[] decrypt(KeyPair keyPair, BigInteger[] input) throws UnsupportedEncodingException {
        byte[] output = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = keyPair.decrypt(input[i]).byteValue();
        }

        return output;
    }
}