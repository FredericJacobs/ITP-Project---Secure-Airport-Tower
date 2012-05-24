package tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import encryption.KeyGenerator;

/**
 *
 * @author gvero
 */
public class KeyGeneratorTest {
    
    public KeyGeneratorTest() {
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
     * Test of generateRSAKeyPair method, of class KeyGenerator.
     */

    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void testGenerateRSAKeyPair2() {
        System.out.println("generateRSAKeyPair2");
        KeyGenerator.generateRSAKeyPair(4);
    }

        
    @Test
    public void testGenerateRSAKeyPair3() {
        System.out.println("generateRSAKeyPair3");
        KeyGenerator.generateRSAKeyPair(8);
    }

}
