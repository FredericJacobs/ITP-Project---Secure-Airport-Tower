package tests;

import java.math.BigInteger;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import encryption.*;
import static org.junit.Assert.*;

/**
 *
 * @author gvero
 */
public class KeyPairTest {
    
    public KeyPairTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setPrivateKey method, of class KeyPair.
     */
    @Test
    public void testSetGetPrivateKey() {
        System.out.println("setPrivateKey");
        BigInteger privateKey = BigInteger.TEN;
        KeyPair instance = new KeyPair(null, null, null, 128);
        instance.setPrivateKey(privateKey);
        
        assertEquals(instance.getPrivateKey(), privateKey);
    }

    /**
     * Test of encrypt method, of class KeyPair.
     */
    @Test(expected=java.lang.NullPointerException.class)
    public void testEncrypt() {
        System.out.println("encrypt");
        KeyPair instance = KeyGenerator.generateRSAKeyPair(128);
        instance.encrypt(null);
    }

    /**
     * Test of decrypt method, of class KeyPair.
     * @throws DecryptWithoutPrivateKeyException 
     */
    @Test(expected=java.lang.NullPointerException.class)
    public void testDecrypt()  {
        System.out.println("decrypt");
        KeyPair instance = KeyGenerator.generateRSAKeyPair(128);
        instance.decrypt(null);
    }
    
    @Test
    public void testEncryptDecrypt()  {
        System.out.println("encrypt-decrypt");
        KeyPair instance = KeyGenerator.generateRSAKeyPair(128);
        BigInteger result = instance.encrypt(instance.decrypt(BigInteger.valueOf(294239487)));
        assertEquals(BigInteger.valueOf(294239487), result);
    }

    /**
     * Test of hidePrivateKey method, of class KeyPair.
     */
    @Test
    
    // WTF is THIS ?
    
    public void testHidePrivateKey() {
        System.out.println("hidePrivateKey");
        KeyPair instance = KeyGenerator.generateRSAKeyPair(128);
        instance.hidePrivateKey();
    }

    /**
     * Test of getKeySize method, of class KeyPair.
     */
    @Test
    public void testGetKeySize() {
        System.out.println("getKeySize");
        int size = 8;
        KeyPair instance = KeyGenerator.generateRSAKeyPair(size);
        int result = instance.getKeySize();
        assertEquals(size, result);
    }

    /**
     * Test of getPublicKey method, of class KeyPair.
     */
    @Test
    public void testGetPublicKey() {
        System.out.println("getPublicKey");
        BigInteger publicKey = BigInteger.TEN;
        KeyPair instance = new KeyPair(null, publicKey, null, 128);
        assertArrayEquals(instance.getPublicKey(), publicKey.toByteArray());
    }

    /**
     * Test of getModulus method, of class KeyPair.
     */
    @Test
    public void testGetModulus() {
        System.out.println("getModulus");
        BigInteger modulus = BigInteger.TEN;
        KeyPair instance = new KeyPair(modulus, null, null, 128);
        assertArrayEquals(instance.getModulus(), modulus.toByteArray());
    }
}
