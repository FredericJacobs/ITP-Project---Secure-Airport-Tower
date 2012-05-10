package messaging.messages;

import java.io.DataOutputStream;
import java.io.IOException;

public class ByeMessage extends Message {
	
	public ByeMessage (byte[] planeID, int length, int posx,
			int posy) {
		super (planeID, length, 4, posx, posy, MessageType.BYE );
	}
	public void write(DataOutputStream out) throws IOException{
		super.write(out);
	}
}
