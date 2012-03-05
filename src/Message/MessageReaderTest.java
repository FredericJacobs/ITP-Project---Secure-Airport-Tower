package Message;// Why this is wrong to import the package Message 
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

public class MessageReaderTest {


    private void testMessageReaderWithMessage(Messages message) {
    	// Have no idea what this part want to do? 
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
        message.writeMessage(os);
        
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        
        // Une possibilitest de faire avec une mehode statique dans une classe qu'on pourrait appeler MessageReader
        // Une autre possibilite serait de mettre la fonctionnalitede lecture dans une mehode statique de Message
        Messages actual = MessageReader.readMessage(is);
       
        
        assertEquals(message, actual);
    }
    
    @Test
    public void testHello() {
    	Hello hello = new Hello("HB-45235".getBytes(), 10, 20, (byte) (1 << 4));// What is (byte) (1 << 4)? 
    	testMessageReaderWithMessage(hello);
    }
    
    @Test
    public void testBye() {
    	Bye bye = new Bye("HB-45235".getBytes());
    	testMessageReaderWithMessage(bye);
    }
    
    // Add more tests for other message types
    
    // T.B.D
    @Test
    public void testHelloEncodeDecode() {
    	// If your project follows the protocol specification, you should be able to decode and encode messages according to the specified format
    	byte[] messageBytes = {
    	    72, 66, 45, 52, 53, 50, 51, 53,  // Plane Id, in this case HB-45235
    	    0, 0, 0, 0,                      // Length: int = zero
    	    0, 0, 0, 0,                      // Priority: int = zero
    	    0, 0, 0, 10,                     // x coordinate: int = 10  (note that we require big endian encoding)
    	    0, 0, 0, 20,                     // y coordinate: int = 20
    	    0, 0, 0, 0,                      // MessageType: Enum = HELLO, encoded as int
    	    16                               // The "reserved" byte
    	};
        
    	InputStream is = new ByteArrayInputStream(messageBytes);
        Messages actual = MessageReader.readMessage(is);

        assert (actual instanceof Hello);
        Hello actualHello = (Hello) actual;

        byte[] expectedPlaneId = {72, 66, 45, 52, 53, 50, 51, 53};
        assertArrayEquals(actual.getPlaneID(), expectedPlaneId);
        assertEquals(actualHello.getLength(), 0);
        assertEquals(actualHello.getPosx(), 10);
        assertEquals(actualHello.getPosy(), 20);
        assertEquals(actualHello.getReserved(), 16);
    }
}
